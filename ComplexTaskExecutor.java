import java.util.concurrent.*;

public class ComplexTaskExecutor {
    private final ExecutorService executor;
    private final CyclicBarrier barrier;

    public ComplexTaskExecutor(int numberOfTasks) {
        executor = Executors.newFixedThreadPool(numberOfTasks);
        barrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks are completed. Proceeding with the next step.");
        });
    }

    public void executeTasks(int numberOfTasks) {
        for (int i = 0; i < numberOfTasks; i++) {
            ComplexTask task = new ComplexTask(i);
            executor.submit(() -> {
                task.execute();
                try {
                    // Ждем завершения всех задач
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
