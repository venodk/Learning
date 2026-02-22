package jpmorgen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class CircuitBreaker1Test {

    private CircuitBreaker1 circuitBreaker;

    // Test parameters
    private static final long WINDOW_IN_MILLISECONDS = 1000;
    private static final long TIMEOUT_IN_MILLISECONDS = 2000;
    private static final long FAILURE_THRESHOLD = 3;

    @BeforeEach
    public void setUp() {
        circuitBreaker = new CircuitBreaker1(WINDOW_IN_MILLISECONDS, TIMEOUT_IN_MILLISECONDS, FAILURE_THRESHOLD);
    }

    @Test
    public void shouldBeClosedInitially() {
        // Initially, the circuit breaker should allow execution.
        String result = circuitBreaker.execute(() -> "Success");
        assertEquals("Success", result);
    }

    @Test
    public void shouldRemainClosedAfterSuccessfulExecutions() {
        for (int i = 0; i < 10; i++) {
            circuitBreaker.execute(() -> "Success");
        }
        // After multiple successful executions, it should still be closed.
        String result = circuitBreaker.execute(() -> "Success");
        assertEquals("Success", result);
    }

    @Test
    public void shouldOpenAfterReachingFailureThreshold() {
        // Simulate failures until the threshold is reached.
        for (int i = 0; i < FAILURE_THRESHOLD; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("Failure");
            }));
        }

        // The next call should fail fast because the circuit is now open.
        assertThrows(CircuitBreakerOpenException.class, () -> circuitBreaker.execute(() -> "Should not execute"));
    }

    @Test
    public void shouldFailFastWhenOpen() {
        // Manually trigger failures to open the circuit.
        for (int i = 0; i < FAILURE_THRESHOLD; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("Failure");
            }));
        }

        // Verify that subsequent calls fail fast.
        long startTime = System.currentTimeMillis();
        assertThrows(CircuitBreakerOpenException.class, () -> circuitBreaker.execute(() -> "Should not execute"));
        long endTime = System.currentTimeMillis();

        // The execution should be very fast, as it's not waiting for a timeout.
        assertTrue((endTime - startTime) < 50, "Execution should fail fast.");
    }

    @Test
    public void shouldCloseAgainAfterTimeout() throws InterruptedException {
        // Open the circuit.
        for (int i = 0; i < FAILURE_THRESHOLD; i++) {
            assertThrows(RuntimeException.class, () -> circuitBreaker.execute(() -> {
                throw new RuntimeException("Failure");
            }));
        }

        // Verify it's open.
        assertThrows(CircuitBreakerOpenException.class, () -> circuitBreaker.execute(() -> "Should not execute"));

        // Wait for the timeout period to elapse.
        Thread.sleep(TIMEOUT_IN_MILLISECONDS /*+ 500*/);

        // The circuit should now be closed, and a new execution should succeed.
        String result = circuitBreaker.execute(() -> "Success after timeout");
        assertEquals("Success after timeout", result);
    }

    @Test
    @Timeout(5)
    public void shouldHandleConcurrentFailuresAndOpenCircuitCorrectly() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger failures = new AtomicInteger(0);

        // All threads will try to execute and fail.
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    circuitBreaker.execute(() -> {
                        throw new RuntimeException("Concurrent Failure");
                    });
                } catch (RuntimeException e) {
                    if ((e instanceof CircuitBreakerOpenException)) {
                        failures.incrementAndGet();
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);

        // 7 should fail because of the circuit breaker. 3 fails due to supplier exception
        assertEquals(7, failures.get());

        // After the concurrent storm, the circuit should be open.
        assertThrows(CircuitBreakerOpenException.class, () -> circuitBreaker.execute(() -> "Should not execute"));
    }
}
