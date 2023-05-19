import java.util.Arrays;

class Bank {
    private final int[] accounts;

    public Bank(int n, int initialBalance) {
        accounts = new int[n];
        Arrays.fill(accounts, initialBalance);
    }

    public synchronized void transfer(int from, int to, int amount) throws InterruptedException {
        accounts[from] -= amount;
        accounts[to] += amount;
    }

    public int size() {
        return accounts.length;
    }

    public synchronized long sum() {
        return Arrays.stream(accounts).sum();
    }
}