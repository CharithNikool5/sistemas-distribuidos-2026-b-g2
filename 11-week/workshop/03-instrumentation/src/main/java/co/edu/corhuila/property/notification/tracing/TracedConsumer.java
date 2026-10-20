package co.edu.corhuila.property.notification.tracing;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Middleware of the RabbitMQ consumer: before the handler runs it reads the trace from
 * the message headers (or starts one), keeps it for the whole delivery and logs the end.
 * A consumer has no HTTP response, so the context is "returned" with the ack: the handler
 * saves the traceparent in notification.delivery_log.
 */
public final class TracedConsumer implements Consumer<TracedConsumer.Message> {

    /** What arrives from RabbitMQ: headers and body. */
    public record Message(Map<String, Object> headers, String body) { }

    private static final ThreadLocal<SpanContext> SPAN = new ThreadLocal<>();
    private static final ThreadLocal<String> CORRELATION = new ThreadLocal<>();

    private final Consumer<Message> handler;

    public TracedConsumer(Consumer<Message> handler) {
        this.handler = handler;
    }

    @Override
    public void accept(Message message) {
        long t0 = System.currentTimeMillis();
        SPAN.set(SpanContext.extract(message.headers()));
        Object corr = message.headers() == null ? null : message.headers().get("correlation_id");
        CORRELATION.set(corr == null ? null : corr.toString());
        try {
            handler.accept(message);
        } finally {
            Log.write("INFO", "event.consumed", "delivery finished", "duration_ms", System.currentTimeMillis() - t0);
            SPAN.remove();
            CORRELATION.remove();
        }
    }

    public static SpanContext currentSpan() { return SPAN.get(); }
    public static String currentCorrelationId() { return CORRELATION.get(); }
}
