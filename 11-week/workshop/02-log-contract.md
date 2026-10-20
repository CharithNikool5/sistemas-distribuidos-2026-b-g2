# CP2 · Log contract — notification journey

All services print **one JSON object per line** to stdout. Notification adds the fields
of a delivery. Filtering by `trace_id` joins the Saga that confirmed the reservation with
the e-mail that was (or was not) sent; filtering by `event_id` finds every redelivery.

## Fields every service writes

| Name | Type | Required | Example | Why we need it |
|---|---|---|---|---|
| `timestamp` | ISO-8601, UTC, ms | yes | `2026-10-19T16:40:02.334Z` | Sort lines from Booking and Notification together |
| `level` | `ERROR`/`WARN`/`INFO`/`DEBUG` | yes | `WARN` | Severity |
| `service` | string | yes | `notification-service` | Origin |
| `version` | string | yes | `2.0.0` | Which release wrote it |
| `environment` | `dev`/`qa`/`main` | yes | `qa` | Environment |
| `trace_id` | 32 hex | yes | `5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2` | Same id from the Saga to the e-mail |
| `span_id` | 16 hex | yes | `2a3b4c5d6e7f8091` | Which step |
| `correlation_id` | string | yes | `corr-3e7a` | `X-Correlation-Id` of the Saga, copied into the event |
| `event` | `noun.verb` | yes | `delivery.skipped` | Name to count |
| `message` | string | yes | `e-mail not sent` | For people |
| `duration_ms` | integer | on calls and at the end of a consume | `2001` | Time per hop |

## Fields of the notification domain

| Name | Type | Example | Why |
|---|---|---|---|
| `event_id` | string | `ev-5d0b` | Key of `notification.processed_event`: the same event is never delivered twice |
| `event_type` | `ReservaConfirmada` / `ReservaCancelada` / `ReservaExpirada` | `ReservaConfirmada` | Which template of `message_template` is used |
| `reserva_id` | uuid | `a4b5…` | Link to the `Reserva` in Booking |
| `usuario_ref` | hash prefix of `usuarioId` | `usr_7f3c1a` | Follow one guest without exposing the id |
| `channel` | `email`/`sms`/`push` | `email` | Channel of the delivery |
| `delivery_status` | `SENT`/`FAILED`/`SKIPPED` | `SKIPPED` | Same value saved in `delivery_log` |
| `delivery_attempt` | integer | `1` | RabbitMQ redelivery count |

## Never log

| Data | Where it shows up | What we do |
|---|---|---|
| Guest e-mail address | answer of Identity, e-mail provider | Never printed; only `usuario_ref` |
| Guest name, phone | template variables | Not logged; the rendered body is never logged |
| `usuarioId` in clear | event payload | Hashed to `usuario_ref` |
| `SERVICE_TOKEN` (`svc-notification`), JWT | `Authorization` header | Header dropped |
| API key of the e-mail provider | config | Comes from an environment variable, never logged |

## Seven lines: one confirmation that was not delivered

Identity times out, the retry answers `422`, and the e-mail is skipped and recorded.

```json
{"timestamp":"2026-10-19T16:40:02.310Z","level":"INFO","service":"booking-service","version":"2.0.0","environment":"qa","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"1f2e3d4c5b6a7980","correlation_id":"corr-3e7a","event":"outbox.event.published","message":"ReservaConfirmada published","event_id":"ev-5d0b","event_type":"ReservaConfirmada","reserva_id":"a4b5c6d7-e8f9-4a0b-8c1d-2e3f4a5b6c7d"}
{"timestamp":"2026-10-19T16:40:02.334Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"qa","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"2a3b4c5d6e7f8091","correlation_id":"corr-3e7a","event":"event.received","message":"ReservaConfirmada received","event_id":"ev-5d0b","event_type":"ReservaConfirmada","reserva_id":"a4b5c6d7-e8f9-4a0b-8c1d-2e3f4a5b6c7d","delivery_attempt":1}
{"timestamp":"2026-10-19T16:40:02.349Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"qa","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"2a3b4c5d6e7f8091","correlation_id":"corr-3e7a","event":"preferences.loaded","message":"guest wants e-mail and push","usuario_ref":"usr_7f3c1a","channel_email":true,"channel_push":true,"duration_ms":9}
{"timestamp":"2026-10-19T16:40:04.352Z","level":"ERROR","service":"notification-service","version":"2.0.0","environment":"qa","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"3b4c5d6e7f809102","correlation_id":"corr-3e7a","event":"identity.request.timeout","message":"identity-service did not answer GET /api/v1/users/{id}/contact in 2000 ms, retry 1 of 2","usuario_ref":"usr_7f3c1a","duration_ms":2001,"attempt":1}
{"timestamp":"2026-10-19T16:40:04.981Z","level":"WARN","service":"notification-service","version":"2.0.0","environment":"qa","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"4c5d6e7f80910213","correlation_id":"corr-3e7a","event":"identity.contact.rejected","message":"identity answered 422: the account has no verified e-mail","usuario_ref":"usr_7f3c1a","error_code":"BUSINESS_RULE_VIOLATION","duration_ms":128,"attempt":2}
{"timestamp":"2026-10-19T16:40:04.996Z","level":"WARN","service":"notification-service","version":"2.0.0","environment":"qa","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"2a3b4c5d6e7f8091","correlation_id":"corr-3e7a","event":"delivery.skipped","message":"e-mail not sent, saved in delivery_log","event_id":"ev-5d0b","reserva_id":"a4b5c6d7-e8f9-4a0b-8c1d-2e3f4a5b6c7d","channel":"email","delivery_status":"SKIPPED","reason":"NO_VERIFIED_EMAIL"}
{"timestamp":"2026-10-19T16:40:05.004Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"qa","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"2a3b4c5d6e7f8091","correlation_id":"corr-3e7a","event":"event.acked","message":"ReservaConfirmada acknowledged and saved in processed_event","event_id":"ev-5d0b","duration_ms":2670}
```

## When we use each level

| Level | Rule | Example |
|---|---|---|
| `ERROR` | A dependency failed; without a retry the guest gets nothing | `identity.request.timeout`, e-mail provider `5xx` |
| `WARN` | Handled, but the guest did not get the message or something unusual happened | `identity.contact.rejected`, `delivery.skipped`, duplicate `event_id` |
| `INFO` | Normal step of a delivery | `event.received`, `delivery.sent`, `event.acked` |
| `DEBUG` | Only on a laptop | The template variables (never in `qa`/`main`, they contain personal data) |
