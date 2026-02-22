package jpmorgen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class CircuitBreakerTest {

    private CircuitBreaker circuitBreaker;

    // Test parameters
    private static final long WINDOW_IN_MILLISECONDS = 1000;
    private static final long TIMEOUT_IN_MILLISECONDS = 500; // Short timeout for faster tests
    private static final long FAILURE_THRESHOLD = 3;

    @BeforeEach
    public void setUp() {
        circuitBreaker = new CircuitBreaker(WINDOW_IN_MILLISECONDS, TIMEOUT_IN_MILLISECONDS, FAILURE_THRESHOLD);
    }

    @Test
    public void shouldBeClosedInitially() {
        String result = circuitBreaker.execute(() -> "Success");
        assertEquals("Success", result);
    }

    @Test
    public void shouldOpenAfterReachingFailureThreshold() {
        // 1. Fail up to threshold - 1
        for (int i = 0; i < FAILURE_THRESHOLD - 1; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("Failure");
            }));
        }
        // Circuit should still be closed
        assertEquals("Success", circuitBreaker.execute(() -> "Success"));

        // 2. Fail one more time to reach threshold
        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
            throw new RuntimeException("Failure");
        }));

        // 3. Next call should fail fast
        assertThrows(CircuitBreakerOpenException.class, () -> circuitBreaker.execute(() -> "Should not execute"));
    }

    @Test
    public void shouldTransitionToHalfOpenAndCloseOnSuccess() throws InterruptedException {
        // Open the circuit
        for (int i = 0; i < FAILURE_THRESHOLD; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("Failure");
            }));
        }

        // Wait for timeout
        Thread.sleep(TIMEOUT_IN_MILLISECONDS + 100);

        // This call should be allowed (Half-Open trial) and succeed
        String result = circuitBreaker.execute(() -> "Success");
        assertEquals("Success", result);

        // Circuit should now be CLOSED. Subsequent calls should succeed.
        assertEquals("Success", circuitBreaker.execute(() -> "Success"));
    }

    @Test
    public void shouldTransitionToHalfOpenAndReOpenOnFailure() throws InterruptedException {
        // Open the circuit
        for (int i = 0; i < FAILURE_THRESHOLD; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("Failure");
            }));
        }

        // Wait for timeout
        Thread.sleep(TIMEOUT_IN_MILLISECONDS + 100);

        // This call (Half-Open trial) fails
        assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
            throw new RuntimeException("Trial Failure");
        }));

        // Circuit should be OPEN again immediately.
        assertThrows(CircuitBreakerOpenException.class, () -> circuitBreaker.execute(() -> "Should not execute"));
    }

    @Test
    public void shouldAllowOnlyOneThreadInHalfOpenState() throws InterruptedException {
        // Open the circuit
        for (int i = 0; i < FAILURE_THRESHOLD; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("Failure");
            }));
        }

        // Wait for timeout
        Thread.sleep(TIMEOUT_IN_MILLISECONDS + 100);

        int numberOfThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successfulExecutions = new AtomicInteger(0);
        AtomicInteger rejectedExecutions = new AtomicInteger(0);

        // We want to simulate a race where multiple threads try to enter HALF_OPEN at the same time.
        // We'll use a slow supplier for the trial to keep the state in HALF_OPEN for a bit.
        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    circuitBreaker.execute(() -> {
                        try {
                            Thread.sleep(100); // Simulate work during trial
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        return "Success";
                    });
                    successfulExecutions.incrementAndGet();
                } catch (CircuitBreakerOpenException e) {
                    rejectedExecutions.incrementAndGet();
                } catch (Exception e) {
                    // Should not happen in this test setup
                    fail();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(2, TimeUnit.SECONDS);
        executor.shutdown();

        // Only ONE thread should have succeeded (the one that won the race to HALF_OPEN).
        assertEquals(1, successfulExecutions.get(), "Only one thread should succeed in HALF_OPEN state");
        // All other threads should have been rejected.
        assertEquals(numberOfThreads - 1, rejectedExecutions.get(), "Other threads should be rejected");
    }
    
    @Test
    public void shouldHandleConcurrentFailuresCorrectly() throws InterruptedException {
        int numberOfThreads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger openExceptions = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Failure");
                    });
                } catch (CircuitBreakerOpenException e) {
                    openExceptions.incrementAndGet();
                } catch (RuntimeException e) {
                    fail();
                    // Expected failure
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(2, TimeUnit.SECONDS);
        executor.shutdown();
        
        // We expect at least (numberOfThreads - FAILURE_THRESHOLD) exceptions to be CircuitBreakerOpenException
        // because the first FAILURE_THRESHOLD threads will trip the circuit.
        assertTrue(openExceptions.get() >= numberOfThreads - FAILURE_THRESHOLD);
    }
}
