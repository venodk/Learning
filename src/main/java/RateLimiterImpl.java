import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

interface RateLimiter {
    boolean limit();
}

public class RateLimiterImpl implements RateLimiter {
    private final Queue<Long> set = new LinkedList<>();
    private final long maxRequest;
    private final long timeLimit;

    public RateLimiterImpl(TimeUnit timeUnit, long duration, long threshold) {
        this.timeLimit = TimeUnit.MILLISECONDS.convert(duration, timeUnit);
        this.maxRequest = threshold;
    }

    @Override
    public boolean limit() {
        long timeNow  = Instant.now().toEpochMilli(); // System.currentTimeMillis();
        while (!set.isEmpty() && set.peek() + timeLimit < timeNow) {
            set.poll();
        }
        boolean retValue = false;
        if (set.size() >= maxRequest) {
            retValue = true;
        }
        set.add(timeNow);
        return retValue;
    }

    public static void main(String[] args) {
        RateLimiterImpl limiter = new RateLimiterImpl(TimeUnit.SECONDS, 3, 5);
        boolean ret1 = limiter.limit();
        boolean ret2 = limiter.limit();
        boolean ret3 = limiter.limit();
        boolean ret4 = limiter.limit();
        boolean ret5 = limiter.limit();
        boolean ret6 = limiter.limit();
        boolean ret7 = limiter.limit();
    }
}
