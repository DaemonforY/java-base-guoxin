package day03;

public class DeadlockExample {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    private static class ThreadA extends Thread {
        @Override
        public void run() {
            synchronized (lock1) {
                System.out.println("Thread A: Holding lock 1...");
                try { Thread.sleep(50); } catch (InterruptedException e) {}

                System.out.println("Thread A: Waiting for lock 2...");
                synchronized (lock2) {
                    System.out.println("Thread A: Holding lock 1 & 2...");
                }
            }
        }
    }

    private static class ThreadB extends Thread {
        @Override
        public void run() {
            synchronized (lock2) {
                System.out.println("Thread B: Holding lock 2...");
                try { Thread.sleep(50); } catch (InterruptedException e) {}

                System.out.println("Thread B: Waiting for lock 1...");
                synchronized (lock1) {
                    System.out.println("Thread B: Holding lock 2 & 1...");
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread threadA = new ThreadA();
        Thread threadB = new ThreadB();
        
        threadA.start();
        threadB.start();
    }
}