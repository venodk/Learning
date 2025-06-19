package gemini;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * fixed window rate limiter
* */
public class RateLimiter {
    private final long timeWindow;
    private final int threshold;
    private final Map<Integer, ConcurrentLinkedQueue<Long>> requestLog;

    public RateLimiter(TimeUnit timeUnit, int time, int threshold) {
        // gemini suggested validation
        if (time <= 0 || threshold <= 0) {
            throw new IllegalArgumentException("Time and threshold must be greater than 0.");
        }
        this.timeWindow = timeUnit.toMillis(time);
        this.threshold = threshold;
        this.requestLog = new ConcurrentHashMap<>();
    }

    public boolean allowRequest(int key) {
        long timeNow = System.currentTimeMillis();
        ConcurrentLinkedQueue<Long> requestTimes = requestLog.computeIfAbsent(key, k -> new ConcurrentLinkedQueue<>());
        synchronized (requestTimes) {
            requestTimes.add(timeNow);
            while (!requestTimes.isEmpty() && requestTimes.peek() <= timeNow - timeWindow) {
                requestTimes.poll(); // Use poll() instead of remove()
            }
            return requestTimes.size() <= threshold;
        }
    }
}
