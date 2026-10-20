# CP9 · Drill — Identity slow while sending confirmation e-mails

**What we simulate.** Hop 5 of CP1: `notification-service` → `identity-service`
(`GET /api/v1/users/{id}/contact`). For **40 minutes**, Identity answers in **3 s for 25 %**
of the calls. Our client gives up at **2 s**, so each of those calls is a failed attempt:
the event goes back to RabbitMQ and is retried (3 retries, 1 s / 2 s / 4 s, then DLQ).

**Traffic in the window.** A holiday Saturday: **30 confirmations per hour** (normal is
about 6 e-mails an hour). 40 minutes → **20 events**. With retries, about **26 delivery
attempts**, of which about **6 fail**. An event reaches the DLQ only if 4 attempts in a
row are slow: 20 × 0.25⁴ ≈ 0.08 → **no e-mail is lost**, some arrive a few seconds late.

## Trace of one delayed e-mail (span tree)

`trace_id 9e1f3a5c7b2d4e6f8a0b1c2d3e4f5a6b` — from the Saga to the e-mail, 5.4 s in total
after the event was published.

```
saga reservar ..................................................... property-workflow   1,180 ms
└─ POST /api/v1/reservas/{id}/confirmar .......................... booking-service       141 ms
relay publish ReservaConfirmada .................................. booking-service        14 ms
├─ consume ReservaConfirmada (delivery 1) ........................ notification-service 2,031 ms  failed
│  └─ GET /api/v1/users/{id}/contact ............................ identity-service     2,000 ms  timeout
└─ consume ReservaConfirmada (delivery 2, after 1 s) ............ notification-service   402 ms  sent
   ├─ GET /api/v1/users/{id}/contact ............................ identity-service        96 ms
   └─ send e-mail ................................................ e-mail provider        281 ms
```

## Four log lines of that trace

```json
{"timestamp":"2026-10-24T15:22:10.441Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"main","trace_id":"9e1f3a5c7b2d4e6f8a0b1c2d3e4f5a6b","span_id":"1a1b1c1d1e1f1a2b","correlation_id":"corr-61e0","event":"event.received","message":"ReservaConfirmada received","event_id":"ev-a3c9","delivery_attempt":1}
{"timestamp":"2026-10-24T15:22:12.472Z","level":"ERROR","service":"notification-service","version":"2.0.0","environment":"main","trace_id":"9e1f3a5c7b2d4e6f8a0b1c2d3e4f5a6b","span_id":"2b2c2d2e2f2a3b3c","correlation_id":"corr-61e0","event":"identity.request.failed","message":"identity-service did not answer in 2000 ms, message requeued","event_id":"ev-a3c9","duration_ms":2000}
{"timestamp":"2026-10-24T15:22:13.480Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"main","trace_id":"9e1f3a5c7b2d4e6f8a0b1c2d3e4f5a6b","span_id":"3c3d3e3f3a3b4c4d","correlation_id":"corr-61e0","event":"event.received","message":"ReservaConfirmada received again","event_id":"ev-a3c9","delivery_attempt":2}
{"timestamp":"2026-10-24T15:22:13.882Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"main","trace_id":"9e1f3a5c7b2d4e6f8a0b1c2d3e4f5a6b","span_id":"3c3d3e3f3a3b4c4d","correlation_id":"corr-61e0","event":"delivery.sent","message":"e-mail sent","event_id":"ev-a3c9","channel":"email","delivery_status":"SENT","duration_ms":402}
```

## SLIs in the 40 minutes

- **SLO 1 (delivered):** our metric counts every failed **attempt** as `failed`: 6 of 26 →
  **77 %**. E-mails really lost: **0**.
- **SLO 2 (≤ 60 s):** the slowest e-mail took about 9 s with retries → **100 %**.
- **SLO 3 (nothing lost):** **100 %**.

## Alert timeline

`NotificationEmailFastBurn`: the 5 min window is at ~23 % (> 14.4 %) almost at once; the
1 h window grows as 23 % × t / 60 and passes 14.4 % at **t ≈ 38 min**; plus `for: 2m`
→ it pages at **minute 40**, right when the slowness ends. The slow and late alerts do
not fire. So the page arrives late **and** for e-mails that were in fact delivered.

## The step of the runbook that ends it

Step 2 shows `dependency="identity"` with p95 at 3 s; the trace shows the timeout on
hop 5. Option C, **scale `identity-service` to 2 replicas** (more DB connections), stops
the slow answers. Option A (pause) was not needed: e-mails were still going out.

## Postmortem (blameless)

| When (24/10) | What |
|---|---|
| 15:00 | Holiday peak: many guests log in and book at the same time |
| 15:10 | Identity's pool of 5 DB connections is shared by `POST /login` and the new contact read; 1 of 4 contact reads waits ~3 s |
| 15:10–15:50 | ~6 failed attempts; all e-mails go out on retry |
| 15:50 | `NotificationEmailFastBurn` pages |
| 15:58 | On-call scales Identity; contact reads back to ~90 ms |
| 16:05 | Alert resolved |

**Impact.** No e-mail lost, a few seconds of delay for 6 guests. But SLO 1 counts 6
"failed" of 45 allowed: **13 %** of the month's budget gone on a day where no guest
missed an e-mail. In minutes: 40 × 23 % = 9 min of 432 = 2 %.

**Root cause.** Identity uses one small connection pool for the login and for the new
internal contact read, and the holiday peak needed more than 5 connections.

**What made it worse.**
- The SLI counts attempts, not the final result of an event, so retries look like losses.
- The timeout of 2 s on a 3 s answer turned "slow" into "failed".
- The contact read of GAP-017 was never tested with login traffic at the same time.

**Action items**

1. *Story — as the Notification owner, I want the delivery SLI to count the final result
   of each event (sent or dead-lettered), so that a retry that works is not a failure.*
   AC: given an event that fails once and is sent on retry, when the metrics are read,
   then `notification_deliveries_total` shows 1 `sent` and 0 `failed`.
2. *Story — as the Identity owner, I want a separate connection pool for internal reads,
   so that a login peak cannot slow down Notification.*
   AC: given 50 logins per minute and 30 contact reads per hour, when the load test runs,
   then p95 of `GET /api/v1/users/{id}/contact` stays under 250 ms.
3. *Story — as the team, I want to close GAP-017 with this endpoint documented in
   `identity-service.yaml` (service credential only), so that its timeout and its limits
   are part of the contract.*
   AC: given the PR to `property-docs`, when it is reviewed, then the endpoint lists its
   time limit, its error codes from the norm and the `svc-notification` scope.
