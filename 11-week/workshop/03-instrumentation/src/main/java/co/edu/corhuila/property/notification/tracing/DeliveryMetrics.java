package co.edu.corhuila.property.notification.tracing;

import java.util.TreeMap;

/**
 * notification_deliveries_total{channel,outcome} and notification_delivery_duration_seconds.
 * outcome is a closed list: sent, failed, skipped, duplicate.
 */
public class DeliveryMetrics {

    /** Seconds from "event received" to "e-mail handed to the provider". A guest expects it within a minute. */
    private static final double[] LE = {0.5, 1, 2, 5, 10, 30, 60};

    private final TreeMap<String, Long> deliveries = new TreeMap<>();
    private final long[] bucket = new long[LE.length];
    private long count;
    private double sum;

    public synchronized void record(String channel, String outcome, double seconds) {
        deliveries.merge("channel=\"" + channel + "\",outcome=\"" + outcome + "\"", 1L, Long::sum);
        for (int i = 0; i < LE.length; i++) {
            if (seconds <= LE[i]) bucket[i]++;
        }
        count++;
        sum += seconds;
    }

    public synchronized String exposition() {
        StringBuilder sb = new StringBuilder("# TYPE notification_deliveries_total counter\n");
        deliveries.forEach((labels, n) ->
                sb.append("notification_deliveries_total{").append(labels).append("} ").append(n).append('\n'));
        sb.append("# TYPE notification_delivery_duration_seconds histogram\n");
        for (int i = 0; i < LE.length; i++) {
            sb.append("notification_delivery_duration_seconds_bucket{le=\"").append(LE[i]).append("\"} ")
                    .append(bucket[i]).append('\n');
        }
        sb.append("notification_delivery_duration_seconds_bucket{le=\"+Inf\"} ").append(count).append('\n');
        sb.append("notification_delivery_duration_seconds_sum ").append(sum).append('\n');
        sb.append("notification_delivery_duration_seconds_count ").append(count).append('\n');
        return sb.toString();
    }
}
