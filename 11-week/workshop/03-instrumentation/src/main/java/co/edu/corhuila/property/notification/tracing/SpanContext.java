package co.edu.corhuila.property.notification.tracing;

import java.util.Map;
import java.util.UUID;

/**
 * Trace context of one delivery in notification-service.
 * Wire format: W3C traceparent "00-{traceId}-{spanId}-01", read from the AMQP headers.
 */
public record SpanContext(String traceId, String spanId) {

    public static final String HEADER = "traceparent";

    /** Reads traceparent from the message headers; a missing or broken one starts a new trace. */
    public static SpanContext extract(Map<String, ?> headers) {
        Object raw = headers == null ? null : headers.get(HEADER);
        if (raw != null) {
            String[] p = raw.toString().trim().split("-");
            boolean valid = p.length == 4 && "00".equals(p[0])
                    && p[1].matches("[0-9a-f]{32}") && p[2].matches("[0-9a-f]{16}");
            if (valid) {
                return new SpanContext(p[1], newSpanId());
            }
        }
        return new SpanContext(UUID.randomUUID().toString().replace("-", ""), newSpanId());
    }

    /** Context for an outbound call: keeps traceId, opens a new span. */
    public SpanContext childSpan() {
        return new SpanContext(traceId, newSpanId());
    }

    public String traceparent() {
        return "00-" + traceId + "-" + spanId + "-01";
    }

    private static String newSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
