package day03;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueExample {
    public static void main(String[] args) {
        Queue<String> queue = new ConcurrentLinkedQueue<>();

        Runnable task1 = () -> {
            queue.add("Element1");
            System.out.println(Thread.currentThread().getName() + " added Element1");
        };

        Runnable task2 = () -> {
            queue.add("Element2");
            System.out.println(Thread.currentThread().getName() + " added Element2");
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        // 等待任务完成然后打印队列
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final queue: " + queue);
    }
}
