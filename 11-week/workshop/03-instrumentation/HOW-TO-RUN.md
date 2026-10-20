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

```text
(paste here the output of the test, from "T E S T S" to "BUILD SUCCESS")
```
