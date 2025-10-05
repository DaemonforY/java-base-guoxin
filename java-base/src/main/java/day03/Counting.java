package day03;

public class Counting {
    public static void main(String[] args) {
        class Counter {
            private int count = 0;
            public void increment() {
                count++;
            }
            public int getCount() {
                return count;
            }
        }
        final Counter counter = new Counter();

        class CountingThread extends Thread {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    counter.increment();
                }
            }
        }

        CountingThread t1 = new CountingThread();
        t1.start();
        CountingThread t2 = new CountingThread();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(counter.getCount());
    }
}
