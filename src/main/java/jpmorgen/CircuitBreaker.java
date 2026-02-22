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
 *
 # Requirements
 - Monitor the number of errors within a given time period, if the number of
 errors in the period exceeds or reaches a configurable level then the
 circuit breaker should open.
 - When the circuit breaker is open, this means that all executions should
 fail fast.
 - After a configurable timeout, the circuit breaker should close. This means
 that executions will no longer fail fast.
 *
 * */
public class CircuitBreaker {
    enum State {
        OPEN, HALF_OPEN, CLOSED
    }
    private final long windowInMilliseconds;
    private final long timeoutInMilliseconds;
    private final long failureThreshold;

    private final AtomicReference<State> state;
    private final AtomicLong circuitOpenTimestamp;
    private final Queue<Long> failureTimestamps;

    public CircuitBreaker(long windowInMilliseconds, long timeoutInMilliseconds, long failureThreshold) {
        this.windowInMilliseconds = windowInMilliseconds;
        this.timeoutInMilliseconds = timeoutInMilliseconds;
        this.failureThreshold = failureThreshold;
        this.state = new AtomicReference<>(State.CLOSED);
        this.circuitOpenTimestamp = new AtomicLong(0);
        this.failureTimestamps = new ConcurrentLinkedQueue<>();
    }

    public <T> T execute(Supplier<T> supplier) {
        return switch (state.get()) {
            case OPEN -> executeOpen(supplier);
            // If the state is HALF_OPEN, it means a trial is already in progress by another thread.
            // All other threads should fail fast.
            case HALF_OPEN -> throw new CircuitBreakerOpenException("Circuit breaker is half-open");
            //case HALF_OPEN -> executeHalfOpen(supplier);
            case CLOSED -> executeClosed(supplier);
        };
    }

    private <T> T executeClosed(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            recordFailure();
            throw e;
        }
    }

    private <T> T executeOpen(Supplier<T> supplier) {
        long openTime = circuitOpenTimestamp.get();
        long currentTime = System.currentTimeMillis();
        if (currentTime > openTime + timeoutInMilliseconds) {
            // The timeout has expired. Attempt to move to HALF_OPEN.
            // Only one thread will succeed in this transition.
            if (state.compareAndSet(State.OPEN, State.HALF_OPEN)) {
                // This thread won the race and can attempt the trial execution.
                return executeHalfOpen(supplier);
            }
        }
        // The circuit is still open because the timeout has not passed or another thread is attempting a trial.
        throw new CircuitBreakerOpenException("Circuit breaker is open");
    }

    private <T> T executeHalfOpen(Supplier<T> supplier) {
        try {
            // Execute the single trial request.
            T result = supplier.get();
            // The trial was successful, close the circuit.
            if (state.compareAndSet(State.HALF_OPEN, State.CLOSED)) {
                failureTimestamps.clear();
            }
            return result;
        } catch (Exception e) {
            // The trial failed, re-open the circuit immediately.
            if (state.compareAndSet(State.HALF_OPEN, State.OPEN)) {
                circuitOpenTimestamp.set(System.currentTimeMillis());
            }
            throw e;
        }
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
            if (state.compareAndSet(State.CLOSED, State.OPEN)) {
                circuitOpenTimestamp.set(currentTime);
            }
        }
    }
}
