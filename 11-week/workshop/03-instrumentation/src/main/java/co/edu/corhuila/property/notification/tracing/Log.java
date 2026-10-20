package co.edu.corhuila.property.notification.tracing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * CP2 log contract. Usage: Log.write("WARN", "delivery.skipped", "e-mail not sent", "channel", "email");
 * The trailing arguments are key/value pairs.
 */
public final class Log {

    private static final String SERVICE = env("SERVICE_NAME", "notification-service");
    private static final String VERSION = env("SERVICE_VERSION", "2.0.0");
    private static final String ENVIRONMENT = env("APP_ENV", "dev");
    private static final Set<String> FORBIDDEN = Set.of("email", "usuarioId", "nombre", "telefono", "authorization");

    private Log() { }

    public static String write(String level, String event, String message, Object... pairs) {
        SpanContext span = TracedConsumer.currentSpan();
        StringBuilder sb = new StringBuilder(256).append('{');
        field(sb, "timestamp", Instant.now().truncatedTo(ChronoUnit.MILLIS).toString());
        field(sb, "level", level);
        field(sb, "service", SERVICE);
        field(sb, "version", VERSION);
        field(sb, "environment", ENVIRONMENT);
        field(sb, "trace_id", span == null ? null : span.traceId());
        field(sb, "span_id", span == null ? null : span.spanId());
        field(sb, "correlation_id", TracedConsumer.currentCorrelationId());
        field(sb, "event", event);
        field(sb, "message", message);
        for (int i = 0; i + 1 < pairs.length; i += 2) {
            String key = String.valueOf(pairs[i]);
            if (!FORBIDDEN.contains(key)) field(sb, key, pairs[i + 1]);
        }
        String line = sb.append('}').toString();
        System.out.println(line);
        return line;
    }

    private static void field(StringBuilder sb, String key, Object value) {
        if (sb.length() > 1) sb.append(',');
        sb.append('"').append(key).append("\":");
        boolean raw = value == null || value instanceof Number || value instanceof Boolean;
        sb.append(raw ? String.valueOf(value) : '"' + String.valueOf(value).replace("\"", "\\\"") + '"');
    }

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return v == null || v.isBlank() ? fallback : v;
    }
}
