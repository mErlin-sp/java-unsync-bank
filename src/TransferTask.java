import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class TransferTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10000;
    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;
    private final int reps;

    public TransferTask(Bank b, int from, int max, int reps) {
        bank = b;
        fromAccount = from;
        maxAmount = max;
        this.reps = reps;
    }

    @Override
    protected Long compute() {
        if (reps > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks()).stream().mapToLong(ForkJoinTask::join).sum();
        } else {
            return processing(reps);
        }
    }

    private Collection<TransferTask> createSubtasks() {
        List<TransferTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new TransferTask(bank, fromAccount, maxAmount, reps / 2));
        dividedTasks.add(new TransferTask(bank, fromAccount, maxAmount, (reps - (reps / 2))));
        return dividedTasks;
    }

    private Long processing(int reps) {
        long sum = 0;
        for (int i = 0; i < reps; i++) {
            try {
                int toAccount = (int) (bank.size() * Math.random());
                int amount = (int) (maxAmount * Math.random());
                bank.transfer(fromAccount, toAccount, amount);
                sum += amount;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return sum;
    }
}
