# CP8 · Runbook — NotificationEmailFastBurn

| | |
|---|---|
| Alert | `NotificationEmailFastBurn` (page) |
| Meaning | More than 14.4 % of confirmation e-mails failed in the last hour and the last 5 min |
| Guest impact | Paid, booked, and no e-mail. May contact support or try to pay again |
| Service | `notification-service`; depends on RabbitMQ, `identity-service`, the e-mail provider |

## 1 · What you will see

- `delivery.failed` and `identity.request.failed` lines in the logs of Notification.
- Messages going back to `notification.reservas` (redeliveries) and maybe into
  `notification.reservas.dlq`.
- `delivery_log` rows with `delivery_status = FAILED`.

## 2 · Confirm and find the broken dependency

```promql
sum by (outcome) (rate(notification_deliveries_total{channel="email"}[5m]))
histogram_quantile(0.95, sum by (le, dependency) (rate(notification_dependency_duration_seconds_bucket[5m])))
sum by (dependency, status_class) (rate(notification_dependency_requests_total[5m]))
max(rabbitmq_queue_messages_ready{queue="notification.reservas.dlq"})
```

Then follow one failed e-mail. Take a `trace_id` from a `delivery.failed` line and read
the whole path, from the Saga that confirmed the reservation to Notification:

```
docker compose logs --since 15m notification-service | findstr "delivery.failed"
docker compose logs --since 15m | findstr "<trace_id>"
```

- Timeouts on `identity.contact` → Identity (hop 5).
- `5xx`/`429` from the provider → the e-mail provider (hop 6).
- Neither, and errors on `event.received` → our own release.

## 3 · Stop the damage (start with the safest)

**A. Pause the consumer.** Set `NOTIFICATION_CONSUMER_ENABLED=false` and restart
Notification. Messages are durable: they wait in `notification.reservas`, nothing goes
to the DLQ, nothing is lost. E-mails arrive late instead of never. Use it when a
dependency is down and every retry only fails again.

**B. Roll back** Notification (or Identity, if the trace points there and it was just
released): previous image tag in `property-infra`, `docker compose up -d <service>`.

**C. Scale** the dependency that is saturated (for example
`docker compose up -d --scale identity-service=2`). Do **not** scale Notification when
Identity is the slow one: more consumers means more calls to an Identity that already
can't keep up.

## 4 · Who joins

1. On-call of the week — steps 1–2 and option A (first 10 minutes).
2. Owner of the dependency the trace points to (Identity or Notification) — B or C.
3. Whole team + teacher — if the DLQ has messages after 30 minutes: those guests need
   a manual e-mail.

## 5 · Back to normal when

- `outcome="failed"` at 0 for 15 minutes and the alert resolves.
- The consumer is running again (`NOTIFICATION_CONSUMER_ENABLED=true`) and the queue is
  drained; `NotificationEmailLate` stays quiet.
- DLQ empty: replay its messages (safe, the `event_id` check prevents double e-mails).
- One test booking in `qa` produces a `delivery.sent` with the same `trace_id` as its Saga.
