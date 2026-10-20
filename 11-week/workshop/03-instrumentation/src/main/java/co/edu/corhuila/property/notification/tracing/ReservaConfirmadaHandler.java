package co.edu.corhuila.property.notification.tracing;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Handles one ReservaConfirmada: skip duplicates (processed_event), ask Identity for the
 * contact, "send" the e-mail and count the result. The real service sends through the
 * e-mail provider and writes notification.delivery_log.
 */
public class ReservaConfirmadaHandler implements Consumer<TracedConsumer.Message> {

    private final Set<String> processedEvents = ConcurrentHashMap.newKeySet();
    private final IdentityContactClient identity;
    private final DeliveryMetrics metrics;

    public ReservaConfirmadaHandler(IdentityContactClient identity, DeliveryMetrics metrics) {
        this.identity = identity;
        this.metrics = metrics;
    }

    @Override
    public void accept(TracedConsumer.Message message) {
        long t0 = System.nanoTime();
        String eventId = String.valueOf(message.headers().get("event_id"));
        String usuarioId = String.valueOf(message.headers().get("usuario_id"));
        if (!processedEvents.add(eventId)) {
            Log.write("WARN", "event.duplicate", "event already delivered, ignored", "event_id", eventId);
            metrics.record("email", "duplicate", seconds(t0));
            return;
        }
        Log.write("INFO", "event.received", "ReservaConfirmada received", "event_id", eventId);
        String outcome;
        try {
            int status = identity.fetchContact(usuarioId);
            outcome = status == 200 ? "sent" : "skipped";
        } catch (Exception e) {
            Log.write("ERROR", "identity.request.failed", "identity-service did not answer", "event_id", eventId);
            processedEvents.remove(eventId); // let RabbitMQ redeliver it
            outcome = "failed";
        }
        Log.write("sent".equals(outcome) ? "INFO" : "WARN", "delivery." + outcome, "e-mail " + outcome,
                "event_id", eventId, "channel", "email", "traceparent", TracedConsumer.currentSpan().traceparent());
        metrics.record("email", outcome, seconds(t0));
    }

    private static double seconds(long t0) {
        return (System.nanoTime() - t0) / 1e9;
    }
}
