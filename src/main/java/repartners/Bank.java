package repartners;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * So make thread safe
 */
public class Bank {

    private static class Account {
        private volatile int balance;
        private final String name;


        Account(String name, int balance) {
            this.name = name;
            this.balance = balance;
        }

        public String getName() {
            return name;
        }

        int getBalance() {
            return balance;
        }

        void setBalance(int balance) {
            this.balance = balance;
        }
    }

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public void addAccount(String name, int balance) {
        accounts.put(name, new Account(name, balance));
    }

    public void transfer(String from, String to, int amount) {
        if (from == null || to == null || from.equals(to)) {
            return; // Avoid locking the same account twice
        }

        Account fromAccount = accounts.get(from);
        Account toAccount = accounts.get(to);

        if (fromAccount == null || toAccount == null) {
            // One or both accounts do not exist
            return;
        }

        // To prevent deadlocks, we always acquire locks in a consistent order.
        Account lock1 = from.compareTo(to) < 0 ? fromAccount : toAccount;
        Account lock2 = from.compareTo(to) < 0 ? toAccount : fromAccount;

        if (fromAccount.getBalance() >= amount) {
            synchronized (lock1) {
                synchronized (lock2) {
                    if (fromAccount.getBalance() >= amount) {
                        fromAccount.setBalance(fromAccount.getBalance() - amount);
                        toAccount.setBalance(toAccount.getBalance() + amount);
                    }
                }
            }
        }
    }

    public int getTotalBalance() {
        // The sum operation is thread-safe as it operates on a snapshot of the values.
        return accounts.values().stream()
                .mapToInt(Account::getBalance)
                .sum();
    }
}
