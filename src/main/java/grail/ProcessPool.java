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
