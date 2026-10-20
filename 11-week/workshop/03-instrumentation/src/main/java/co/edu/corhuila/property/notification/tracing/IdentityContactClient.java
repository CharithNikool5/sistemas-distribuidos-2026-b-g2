package co.edu.corhuila.property.notification.tracing;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Outbound call of the journey (hop 5): GET /api/v1/users/{id}/contact on identity-service,
 * the internal read proposed for GAP-017. It carries the child span in traceparent.
 */
public class IdentityContactClient {

    private final HttpClient http = HttpClient.newHttpClient();
    private final String identityUrl;
    private final String serviceToken;

    public IdentityContactClient(String identityUrl, String serviceToken) {
        this.identityUrl = identityUrl;
        this.serviceToken = serviceToken;
    }

    /** Returns the HTTP status; the body (the e-mail) never reaches the logs. */
    public int fetchContact(String usuarioId) throws Exception {
        SpanContext out = TracedConsumer.currentSpan().childSpan();
        HttpRequest req = HttpRequest.newBuilder(URI.create(identityUrl + "/api/v1/users/" + usuarioId + "/contact"))
                .timeout(Duration.ofSeconds(2))
                .header(SpanContext.HEADER, out.traceparent())
                .header("Authorization", "Bearer " + serviceToken)
                .GET()
                .build();
        long t0 = System.currentTimeMillis();
        int status = http.send(req, HttpResponse.BodyHandlers.ofString()).statusCode();
        Log.write(status == 200 ? "INFO" : "WARN", "identity.contact.answered", "identity-service answered " + status,
                "usuario_ref", "usr_" + Integer.toHexString(usuarioId.hashCode()), "duration_ms", System.currentTimeMillis() - t0);
        return status;
    }
}
