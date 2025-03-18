public class TestComplexTaskExecutor {
    public static void main(String[] args) {
        ComplexTaskExecutor taskExecutor = new ComplexTaskExecutor(5);

        taskExecutor.executeTasks(5);

        taskExecutor.shutdown();
    }
}