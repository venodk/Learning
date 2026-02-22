package grail;

import java.util.HashSet;
import java.util.Set;

/**
 * Process poll for generating a unique process id that is not in use
 * 1. Check only positive process ids possible
 * 2. check reusability of released ids
 * 3. expected behaviour on all ids used.
 * */
public class ProcessPool {

    private final Set<Integer> pool = new HashSet<>();
    private int nextProcessId;
    private final int maxProcessId;

    public ProcessPool() {
        this(64000);
    }
    protected ProcessPool(int maxId) {
        this.maxProcessId = maxId;
        this.nextProcessId = 1;
    }


    public synchronized int getId() {

        if (nextProcessId <= maxProcessId) {
            pool.add(nextProcessId);
            return nextProcessId++;
        } else {
            int nextFreeId = 1;
            while (nextFreeId <= maxProcessId && pool.contains(nextFreeId)) {
                nextFreeId++;
            }
            if (nextFreeId > maxProcessId) {
                return -1;
            } else {
                pool.add(nextFreeId);
                return nextFreeId;
            }
        }
    }

    public synchronized void releaseId(int id) {
        pool.remove(id);
    }
}
/**
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

 * A simplified conceptual model of the Linux Kernel's PID allocator using Java concepts.
 The Allocation Process
 1. Fork/Clone System Call: When a new process or thread is created using fork() or clone(), the kernel's internal alloc_pid() function is called to assign a unique ID.
 2. Sequential Search: The kernel starts searching from the value of the last assigned PID, incrementing sequentially to find the next available number.
 3. Bitmap Check: It uses the highly efficient bitmap data structure to quickly check if a potential PID is already in use by a live process.
 4. Wrap-Around: If the kernel reaches pid_max while searching, it wraps around and starts searching for a free PID from a low number (e.g., 300).
 5. Assignment: Once an unused PID is found, it is marked as "in use" in the bitmap and assigned to the new process.

 public class LinuxPIDAllocatorModel {

    // Default max PID value in Linux is often 32768
    private static final int PID_MAX = 32768;
    // PIDs generally start higher than 1 in the kernel
    private static final int PID_MIN = 300;
    private static final int PID_RANGE = PID_MAX - PID_MIN + 1;

    // A BitSet acts as the "bitmap" for O(1) checks.
    // Index i corresponds to PID (PID_MIN + i).
    private final BitSet pidBitmap = new BitSet(PID_RANGE);

    // An atomic integer to track the next potential PID to check, safely across threads.
    private final AtomicInteger nextPid = new AtomicInteger(PID_MIN);

    // A lock to protect critical sections where the bitmap is read/written
    // and the counter is updated to prevent race conditions during allocation.
    private final Reentran6tLock lock = new ReentrantLock();

    *//**
     * Allocates a unique PID using sequential search and a bitmap.
     * @return A unique PID, or -1 if the pool is full.
     *//*
    public int allocPid() {
        lock.lock(); // Acquire lock before accessing shared state (bitmap/counter)
        try {
            // Start searching from the last allocated point
            int startPid = nextPid.get();
            int currentPid = startPid;
            int foundPid = -1;
            int attempts = 0;

            // Loop to search for a free PID (mimics kernel sequential search)
            while (attempts < PID_RANGE) {

                // Index corresponds to the PID value minus the starting offset
                int bitmapIndex = currentPid - PID_MIN;

                // Check if the bit for this PID is clear (available)
                if (!pidBitmap.get(bitmapIndex)) {
                    // Mark the PID as used in the bitmap
                    pidBitmap.set(bitmapIndex);
                    foundPid = currentPid;

                    // Update the global nextPid counter for the next request
                    nextPid.set(wrapAround(currentPid + 1));
                    break;
                }

                // If not available, move to the next PID
                currentPid = wrapAround(currentPid + 1);
                attempts++;
            }

            return foundPid;

        } finally {
            lock.unlock(); // Ensure lock is always released
        }
    }

    *//**
     * Releases a PID back to the pool, clearing its bit in the bitmap.
     * @param pid The PID to release.
     *//*
    public void releasePid(int pid) {
        if (pid >= PID_MIN && pid <= PID_MAX) {
            lock.lock();
            try {
                int bitmapIndex = pid - PID_MIN;
                // Clear the bit to mark as free
                pidBitmap.clear(bitmapIndex);
            } finally {
                lock.unlock();
            }
        }
    }

    *//**
     * Helper function to handle the wrap-around logic.
     *//*
    private int wrapAround(int pid) {
        if (pid > PID_MAX) {
            return PID_MIN; // Wrap back to the minimum start PID
        }
        return pid;
    }

    // Example Usage:
    public static void main(String[] args) {
        LinuxPIDAllocatorModel allocator = new LinuxPIDAllocatorModel();
        int pid1 = allocator.allocPid();
        int pid2 = allocator.allocPid();
        System.out.println("Allocated PID 1: " + pid1); // e.g., 300
        System.out.println("Allocated PID 2: " + pid2); // e.g., 301

        allocator.releasePid(pid1);
        System.out.println("Released PID 1: " + pid1);

        int pid3 = allocator.allocPid();
        System.out.println("Allocated PID 3 (reused): " + pid3); // Should eventually reuse 300
    }
}
*/

