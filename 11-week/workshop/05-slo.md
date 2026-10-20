# CP6 · SLOs — the confirmation e-mail

The guest feels this journey in one way: after paying, the e-mail arrives — or not.
So the SLOs look at the result of each event, not at CPU or queues.

## Traffic we expect

- About 200 booking attempts a day (team estimate); a bit more than half end
  `CONFIRMADA`, others `CANCELADA` or `EXPIRADA`. Notification consumes the three events:
  **about 150 events a day → 4,500 per 30 days**.
- Most guests keep e-mail on (default `email: true` in `PreferenciasResponse`).
- Window: 30 days = **43,200 minutes**.

## SLO 1 · The e-mail is delivered

**SLI:** deliveries `sent` / deliveries `sent` + `failed`. `skipped` (the guest turned
e-mail off, or has no verified e-mail) and `duplicate` (already sent) are not failures of
ours, so they are out of the count.

```promql
sum(increase(notification_deliveries_total{channel="email",outcome="sent"}[30d]))
/
sum(increase(notification_deliveries_total{channel="email",outcome=~"sent|failed"}[30d]))
```

**Target 99 %.** **Budget:** 1 % of 4,500 = **45 failed deliveries**, or 1 % of 43,200 =
**432 minutes**.

## SLO 2 · The e-mail arrives quickly

**SLI:** deliveries finished in 60 s or less / all deliveries. 60 s because the guest
checks the inbox right after paying.

```promql
sum(increase(notification_delivery_duration_seconds_bucket{le="60.0"}[30d]))
/
sum(increase(notification_delivery_duration_seconds_count[30d]))
```

**Target 95 %.** **Budget:** 5 % of 4,500 = **225 late e-mails**, 5 % of 43,200 =
**2,160 minutes**.

## SLO 3 · No event is lost on the way (our own SLO)

**SLI:** events Notification handled / events Booking published for it. If the relay
publishes and nobody consumes (wrong binding, a message stuck in the DLQ), the guest
simply never hears from us — and no error shows anywhere else.

```promql
sum(increase(notification_deliveries_total{outcome!="duplicate"}[30d]))
/
sum(increase(booking_outbox_published_total{event_type=~"ReservaConfirmada|ReservaCancelada|ReservaExpirada"}[30d]))
```

**Target 99.9 %.** **Budget:** 0.1 % of 4,500 = **4.5 events**, 43.2 minutes.

## In one table

| | Target | Budget (count) | Budget (minutes) |
|---|---|---|---|
| SLO 1 delivered | 99 % | 45 e-mails | 432 |
| SLO 2 within 60 s | 95 % | 225 e-mails | 2,160 |
| SLO 3 nothing lost | 99.9 % | 4.5 events | 43.2 |

Why these targets: e-mail depends on an outside provider and on Identity, so 99 % is
honest for SLO 1; losing an event has no excuse, so SLO 3 is strict.

## Error budget policy

- **At 50 %** — message in the team chat with the top `outcome`/`dependency` from the
  dashboard; check the last releases of `notification-service` and `identity-service`.
- **At 75 %** — only bug fixes to Notification; the owner reviews the DLQ
  `notification.reservas.dlq` every day and replays what is safe.
- **At 100 %** — no new features in Notification until the window recovers. The first
  sprint task is the postmortem actions. For SLO 3, every lost event is traced by its
  `trace_id` and the guest is notified by hand.
