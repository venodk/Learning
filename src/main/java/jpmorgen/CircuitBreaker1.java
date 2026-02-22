package jpmorgen;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * A circuit breaker is a design pattern used to encapsulate logic to prevent a
 * system from repeatedly trying to execute a failing operation. It monitors
 * failed calls to a service or resource. If failures reach a threshold, the
 * circuit "opens," blocking further attempts for a period. After a timeout, it
 * "closes" and resumes normal operation.
 * public <T> T execute(Supplier<T> supplier) {
 * }
 # Requirements
 - Monitor the number of errors within a given time period, if the number of
 errors in the period exceeds or reaches a configurable level then the
 circuit breaker should open.
 - When the circuit breaker is open, this means that all executions should
 fail fast.
 - After a configurable timeout, the circuit breaker should close. This means
 that executions will no longer fail fast.
 **/
public class CircuitBreaker1 {
    private enum State {
        OPEN, CLOSED
    }
    private final long windowInMilliseconds;
    private final long timeoutInMilliseconds;
    private final long failureThreshold;

    private final AtomicReference<State> state;
    private final AtomicLong circuitOpenTimestamp;
    private final Queue<Long> failureTimestamps;

    public CircuitBreaker1(long windowInMilliseconds, long timeoutInMilliseconds, long failureThreshold) {
        this.windowInMilliseconds = windowInMilliseconds;
        this.timeoutInMilliseconds = timeoutInMilliseconds;
        this.failureThreshold = failureThreshold;
        this.state = new AtomicReference<>(State.CLOSED);
        this.circuitOpenTimestamp = new AtomicLong(0);
        this.failureTimestamps = new ConcurrentLinkedQueue<>();
    }

    public <T> T execute(Supplier<T> supplier) {
        if (isCircuitOpen()) {
            throw new CircuitBreakerOpenException("Circuit breaker is open");
        }

        try {
            return supplier.get();
        } catch (Exception e) {
            recordFailure();
            throw e;
        }
    }

    private boolean isCircuitOpen() {
        if (state.get() == State.OPEN) {
            long openTime = circuitOpenTimestamp.get();
            long currentTime = System.currentTimeMillis();
            if (currentTime > openTime + timeoutInMilliseconds) {
                // The timeout has expired. Attempt to close the circuit.
                // We use compareAndSet to ensure that only one thread can close the circuit.
                if (state.compareAndSet(State.OPEN, State.CLOSED)) {
                    failureTimestamps.clear();
                    return false; // The circuit is now closed; allow execution.
                }
            }
            return state.get() == State.OPEN; // The circuit is still open.
        }
        return false; // The circuit is closed.
    }

    private void recordFailure() {
        long currentTime = System.currentTimeMillis();
        failureTimestamps.add(currentTime);

        // Prune old timestamps to keep the window current.
        Long oldestTimestamp;
        while ((oldestTimestamp = failureTimestamps.peek()) != null && oldestTimestamp < currentTime - windowInMilliseconds) {
            failureTimestamps.poll();
        }

        // If the failure threshold is met, try to open the circuit.
        if (failureTimestamps.size() >= failureThreshold) {
            // Use compareAndSet to ensure only one thread opens the circuit.
            if (state.compareAndSet(State.CLOSED, State.OPEN)) {
                circuitOpenTimestamp.set(currentTime);
            }
        }
    }
}
