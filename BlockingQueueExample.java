import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueueExample {

    static class BlockingQueue<T> {
        private final Queue<T> queue = new LinkedList<>();
        private final int capacity;

        public BlockingQueue(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void enqueue(T item) throws InterruptedException {
            while (queue.size() == capacity) {
                wait(); 
            }
            queue.add(item);
            notifyAll();
        }

        public synchronized T dequeue() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }
            T item = queue.poll();
            notifyAll(); 
            return item;
        }

        public synchronized int size() {
            return queue.size();
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new BlockingQueue<>(5);

        
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    queue.enqueue(i);
                    System.out.println("Производитель добавил: " + i);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    int value = queue.dequeue();
                    System.out.println("Потребитель взял: " + value);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();
    }
}
