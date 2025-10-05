package day03;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListExample {
    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>();

        list.add("Hello");
        list.add("World");

        Runnable task1 = () -> {
            list.add("Task1");
            System.out.println(Thread.currentThread().getName() + " added Task1");
        };

        Runnable task2 = () -> {
            list.add("Task2");
            System.out.println(Thread.currentThread().getName() + " added Task2");
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        // 等待任务完成然后打印列表
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final list: " + list);
    }
}
