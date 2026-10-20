# CP3 · How to run (notification-service)

Java 21 module, no framework, for the consumer of `ReservaConfirmada`:

| Class | Role |
|---|---|
| `SpanContext` | Reads `traceparent` from the AMQP headers or starts a trace; makes child spans |
| `TracedConsumer` | Middleware of the consumer: opens the trace for the delivery and logs its end |
| `Log` | CP2 log contract (drops e-mail, names and tokens by key) |
| `IdentityContactClient` | Outbound call `GET /api/v1/users/{id}/contact` with `traceparent` (GAP-017 option) |
| `DeliveryMetrics` | `notification_deliveries_total{channel,outcome}` and `notification_delivery_duration_seconds` |
| `ReservaConfirmadaHandler` | Skips duplicates by `event_id`, calls Identity, records the outcome |

## Test

From `11-week/workshop/03-instrumentation`:

```
mvn test
```

No Maven? Open Docker Desktop, wait for "Engine running", then in CMD:

```
docker run --rm -v "%cd%":/app -w /app maven:3.9-eclipse-temurin-21 mvn test
```

`ConfirmationTraceTest` starts a fake `identity-service`, gives the consumer a
`ReservaConfirmada` whose headers carry the Saga's `traceparent`, and checks that Identity
receives the same `trace_id` with a new span id. The second test sends the same
`event_id` twice and checks Identity is called once and the metrics count `sent` and
`duplicate`.

## Console output on my machine

``[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running co.edu.corhuila.property.notification.tracing.ConfirmationTraceTest
{"timestamp":"2026-10-20T03:49:12.425Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"540d93ff4bbe4a48","correlation_id":null,"event":"event.received","message":"ReservaConfirmada received","event_id":"ev-77aa"}
{"timestamp":"2026-10-20T03:49:12.512Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"540d93ff4bbe4a48","correlation_id":null,"event":"identity.contact.answered","message":"identity-service answered 200","usuario_ref":"usr_39117564","duration_ms":83}
{"timestamp":"2026-10-20T03:49:12.513Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"540d93ff4bbe4a48","correlation_id":null,"event":"delivery.sent","message":"e-mail sent","event_id":"ev-77aa","channel":"email","traceparent":"00-5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2-540d93ff4bbe4a48-01"}
{"timestamp":"2026-10-20T03:49:12.513Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"540d93ff4bbe4a48","correlation_id":null,"event":"event.consumed","message":"delivery finished","duration_ms":107}
{"timestamp":"2026-10-20T03:49:12.513Z","level":"WARN","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"99a260352c574eac","correlation_id":null,"event":"event.duplicate","message":"event already delivered, ignored","event_id":"ev-77aa"}
{"timestamp":"2026-10-20T03:49:12.514Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"99a260352c574eac","correlation_id":null,"event":"event.consumed","message":"delivery finished","duration_ms":1}
{"timestamp":"2026-10-20T03:49:12.548Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"c495b54990ee4538","correlation_id":null,"event":"event.received","message":"ReservaConfirmada received","event_id":"ev-5d0b"}
{"timestamp":"2026-10-20T03:49:12.551Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"c495b54990ee4538","correlation_id":null,"event":"identity.contact.answered","message":"identity-service answered 200","usuario_ref":"usr_39117564","duration_ms":3}
{"timestamp":"2026-10-20T03:49:12.552Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"c495b54990ee4538","correlation_id":null,"event":"delivery.sent","message":"e-mail sent","event_id":"ev-5d0b","channel":"email","traceparent":"00-5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2-c495b54990ee4538-01"}
{"timestamp":"2026-10-20T03:49:12.552Z","level":"INFO","service":"notification-service","version":"2.0.0","environment":"dev","trace_id":"5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2","span_id":"c495b54990ee4538","correlation_id":null,"event":"event.consumed","message":"delivery finished","duration_ms":4}
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.398 s -- in co.edu.corhuila.property.notification.tracing.ConfirmationTraceTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
```
