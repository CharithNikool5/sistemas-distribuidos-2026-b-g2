# CP4 · Golden signals — notification journey

Names: `snake_case`, units in the name, `_total` on counters. The scrape config adds
`service`, `environment` and `version`.

### booking-service — outbox relay (the start of the journey)

| Signal | Metric | Type | Labels | PromQL |
|---|---|---|---|---|
| Latency | `booking_outbox_publish_delay_seconds` | histogram | `event_type` | `histogram_quantile(0.95, sum by (le) (rate(booking_outbox_publish_delay_seconds_bucket{event_type="ReservaConfirmada"}[5m])))` |
| Traffic | `booking_outbox_published_total` | counter | `event_type` | `sum by (event_type) (rate(booking_outbox_published_total[5m]))` |
| Errors | `booking_outbox_publish_errors_total` | counter | `reason` (`broker_down`, `nack`) | `sum(rate(booking_outbox_publish_errors_total[5m]))` |
| Saturation | `booking_outbox_pending_events` | gauge | — | `max(booking_outbox_pending_events)` |

### RabbitMQ — queue of Notification

| Signal | Metric | Type | Labels | PromQL |
|---|---|---|---|---|
| Latency | `notification_event_lag_seconds` | histogram | `event_type` | `histogram_quantile(0.95, sum by (le) (rate(notification_event_lag_seconds_bucket[5m])))` (from `occurredAt` to consume) |
| Traffic | `rabbitmq_queue_messages_delivered_total` | counter | `queue` | `sum(rate(rabbitmq_queue_messages_delivered_total{queue="notification.reservas"}[5m]))` |
| Errors | `rabbitmq_queue_messages_ready` on the DLQ | gauge | `queue` | `max(rabbitmq_queue_messages_ready{queue="notification.reservas.dlq"})` |
| Saturation | `rabbitmq_queue_messages_ready` | gauge | `queue` | `max(rabbitmq_queue_messages_ready{queue="notification.reservas"})` |

### notification-service — the delivery

| Signal | Metric | Type | Labels | PromQL |
|---|---|---|---|---|
| Latency | `notification_delivery_duration_seconds` | histogram | — | `histogram_quantile(0.95, sum by (le) (rate(notification_delivery_duration_seconds_bucket[5m])))` |
| Traffic | `notification_deliveries_total` | counter | `channel`, `outcome` | `sum by (channel) (rate(notification_deliveries_total[5m]))` |
| Errors | `notification_deliveries_total` | counter | `outcome="failed"` | `sum(rate(notification_deliveries_total{outcome="failed"}[5m])) / sum(rate(notification_deliveries_total{outcome!="duplicate"}[5m]))` |
| Saturation | `notification_consumer_inflight` | gauge | — | `max(notification_consumer_inflight) / max(notification_consumer_prefetch)` |

### identity-service — contact read (hop 5) and e-mail provider (hop 6)

| Signal | Metric | Type | Labels | PromQL |
|---|---|---|---|---|
| Latency | `notification_dependency_duration_seconds` | histogram | `dependency` (`identity`, `email_provider`) | `histogram_quantile(0.95, sum by (le, dependency) (rate(notification_dependency_duration_seconds_bucket[5m])))` |
| Traffic | `notification_dependency_requests_total` | counter | `dependency`, `status_class` | `sum by (dependency) (rate(notification_dependency_requests_total[5m]))` |
| Errors | same counter | counter | `status_class=~"5xx\|timeout"` | `sum by (dependency) (rate(notification_dependency_requests_total{status_class=~"5xx\|timeout"}[5m]))` |
| Saturation | `identity_db_pool_active` / `_max` | gauge | — | `max(identity_db_pool_active) / max(identity_db_pool_max)` |

## Labels and their size

| Label | Possible values | Size |
|---|---|---|
| `channel` | `email`, `sms`, `push` | 3 |
| `outcome` | `sent`, `failed`, `skipped`, `duplicate` | 4 |
| `event_type` | the three events Notification consumes | 3 |
| `dependency` | `identity`, `email_provider` | 2 |
| `status_class` | `2xx`, `4xx`, `5xx`, `timeout` | 4 |
| `queue`, `reason` | closed lists | < 5 |

Never as labels: `usuario_id` (one series per guest), the e-mail address (personal data
and unbounded), `event_id`, `reserva_id`, the template text. They live in the logs (CP2),
hashed or not at all.

## Buckets

`notification_delivery_duration_seconds`: `0.5, 1, 2, 5, 10, 30, 60`. Nobody stares at
the screen for the e-mail, but a guest who just paid checks the inbox within a minute.
Normal is under 2 s (one DB read, one Identity call, one provider call); 60 s is the
edge of the SLO (CP6).

`notification_dependency_duration_seconds`: `0.05, 0.1, 0.25, 0.5, 1, 2` — the client
gives up at 2 s, so nothing above is useful.
