public class ComplexTask implements Runnable {
    private final int taskId;

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        execute();
    }

    public void execute() {
        System.out.println("Task " + taskId + " is being executed by " 
		+ Thread.currentThread().getName());
        try {
            Thread.sleep(1000); // Симуляция работы задачи
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task " + taskId + " completed.");
    }
}
