package co.edu.corhuila.property.notification.tracing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfirmationTraceTest {

    static final String SAGA_TRACE = "5a8c2e4f6b1d43a7c9e0f2a4b6c8d0e2";
    static final String RELAY_SPAN = "1f2e3d4c5b6a7980";

    final List<String> seenByIdentity = new CopyOnWriteArrayList<>();
    HttpServer identity;
    DeliveryMetrics metrics;
    TracedConsumer consumer;

    @BeforeEach
    void start() throws Exception {
        identity = HttpServer.create(new InetSocketAddress(0), 0);
        identity.createContext("/api/v1/users/", ex -> {
            seenByIdentity.add(ex.getRequestHeaders().getFirst("traceparent"));
            ex.sendResponseHeaders(200, -1);
            ex.close();
        });
        identity.start();
        metrics = new DeliveryMetrics();
        IdentityContactClient client = new IdentityContactClient(
                "http://localhost:" + identity.getAddress().getPort(), "test-service-token");
        consumer = new TracedConsumer(new ReservaConfirmadaHandler(client, metrics));
    }

    @AfterEach
    void stop() {
        identity.stop(0);
    }

    TracedConsumer.Message reservaConfirmada(String eventId) {
        return new TracedConsumer.Message(Map.of(
                "traceparent", "00-" + SAGA_TRACE + "-" + RELAY_SPAN + "-01",
                "event_id", eventId,
                "usuario_id", "7f3c1a2b-0000-4000-8000-000000000001"), "{}");
    }

    @Test
    void traceIdFromTheMessageReachesIdentity() {
        consumer.accept(reservaConfirmada("ev-5d0b"));

        assertEquals(1, seenByIdentity.size());
        String[] sent = seenByIdentity.get(0).split("-");
        assertEquals(SAGA_TRACE, sent[1], "identity-service must see the trace_id of the Saga");
        assertNotEquals(RELAY_SPAN, sent[2], "the outbound call opens its own span");
    }

    @Test
    void duplicateEventIsNotDeliveredTwice() {
        consumer.accept(reservaConfirmada("ev-77aa"));
        consumer.accept(reservaConfirmada("ev-77aa"));

        assertEquals(1, seenByIdentity.size());
        String exposition = metrics.exposition();
        assertTrue(exposition.contains("notification_deliveries_total{channel=\"email\",outcome=\"sent\"} 1"));
        assertTrue(exposition.contains("notification_deliveries_total{channel=\"email\",outcome=\"duplicate\"} 1"));
        assertTrue(exposition.contains("notification_delivery_duration_seconds_count 2"));
    }
}
