import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * author Cay Horstmann
 * modified by Oleksandr Popov
 */

public class UnSyncBankTest {
    public static final int N_ACCOUNTS = 30;
    public static final int INITIAL_BALANCE = 10000;
    public static final int REPS_FOR_ACCOUNT = 1000000;

    public static void main(String[] args) {
        System.out.println("UnSync Bank Test with ForkJoinFramework");
        Bank b = new Bank(N_ACCOUNTS, INITIAL_BALANCE);

        long timer = System.currentTimeMillis();

        try (ForkJoinPool commonPool = new ForkJoinPool(4)) {

            List<Future<Long>> futures = new ArrayList<>();
            for (int i = 0; i < N_ACCOUNTS; i++) {
                futures.add(commonPool.submit(new TransferTask(b, i, INITIAL_BALANCE, REPS_FOR_ACCOUNT)));
            }

            for (int i = 0; i < futures.size(); i++) {
                long result = futures.get(i).get();
                System.out.println("Total transaction amount for account " + i + ": " + result);
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Execution time: " + (System.currentTimeMillis() - timer));
            System.out.println("Total amount in Bank: " + b.sum());
        }
    }
}



