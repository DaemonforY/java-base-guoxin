package day03;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {
    public static void main(String[] args) {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        map.put("Key1", 1);
        map.put("Key2", 2);

        Runnable task1 = () -> {
            map.put("Key3", 3);
            System.out.println(Thread.currentThread().getName() + " set Key3");
        };

        Runnable task2 = () -> {
            map.put("Key4", 4);
            System.out.println(Thread.currentThread().getName() + " set Key4");
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();
    }
}
