import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class FactorialTask extends RecursiveTask<Long> {
    private final int n;

    public FactorialTask(int n) {
        this.n = n;
    }

    @Override
    protected Long compute() {
        if (n == 0 || n == 1) {
            return 1L;
        }

        // Разделяем задачу на две части:
        FactorialTask subTask = new FactorialTask(n - 1);
        subTask.fork(); // Асинхронно выполняем подзадачу

        // Считываем результат для текущего значения n
        long result = n * subTask.join();

        return result;
    }

    public static void main(String[] args) {
        int n = 10;

        
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        
        FactorialTask factorialTask = new FactorialTask(n);

        
        long result = forkJoinPool.invoke(factorialTask);

        
        System.out.println("Факториал " + n + "! = " + result);
    }
}
