package gemini;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RateLimiterTest {

    @Test
    void testAllowAllRequestsWithinThreshold() { // Added InterruptedException
        RateLimiter limiter = new RateLimiter(TimeUnit.SECONDS, 1, 5);
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.allowRequest(1));
        }
        assertFalse(limiter.allowRequest(1));
    }

    @Test
    void testBlockRequestsExceedingThreshold() throws InterruptedException {
        RateLimiter limiter = new RateLimiter(TimeUnit.MILLISECONDS, 100, 2);
        assertTrue(limiter.allowRequest(1));
        assertTrue(limiter.allowRequest(1));
        assertFalse(limiter.allowRequest(1));
        Thread.sleep(100); // Wait for the window to pass
        assertTrue(limiter.allowRequest(1)); // Should allow again after the window
    }

    @Test
    void testSeparateLimitsForDifferentKeys() {
        RateLimiter limiter = new RateLimiter(TimeUnit.SECONDS, 1, 2);
        assertTrue(limiter.allowRequest(1));
        assertTrue(limiter.allowRequest(1));
        assertFalse(limiter.allowRequest(1));
        assertTrue(limiter.allowRequest(2));
        assertTrue(limiter.allowRequest(2));
        assertFalse(limiter.allowRequest(2));
    }

    @Test
    void testAllowRequestAfterTimeWindow() throws InterruptedException {
        RateLimiter limiter = new RateLimiter(TimeUnit.MILLISECONDS, 50, 1);
        assertTrue(limiter.allowRequest(1));
        assertFalse(limiter.allowRequest(1));
        Thread.sleep(50);
        assertTrue(limiter.allowRequest(1));
    }

    @Test
    void testConstructorWithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new RateLimiter(TimeUnit.SECONDS, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> new RateLimiter(TimeUnit.SECONDS, 10, 0));
        assertThrows(IllegalArgumentException.class, () -> new RateLimiter(TimeUnit.SECONDS, -1, 10));
        assertThrows(IllegalArgumentException.class, () -> new RateLimiter(TimeUnit.SECONDS, 10, -1));
    }

    @Test
    void testConcurrentAccess() throws InterruptedException {
        int numThreads = 10;
        int requestsPerThread = 20;
        int threshold = 100;
        long timeWindowMillis = 1000; // 1 second
        RateLimiter limiter = new RateLimiter(TimeUnit.MILLISECONDS, (int) timeWindowMillis, threshold);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger allowedRequests = new AtomicInteger(0);
        AtomicInteger deniedRequests = new AtomicInteger(0);

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        if (limiter.allowRequest(1)) {
                            allowedRequests.incrementAndGet();
                        } else {
                            deniedRequests.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // Wait for all threads to finish
        executorService.shutdown();

        // Check that the total number of allowed requests is within the threshold
        assertEquals(threshold, allowedRequests.get());
        System.out.println("Allowed Requests: " + allowedRequests.get());
        System.out.println("Denied Requests: " + deniedRequests.get());
    }
}
