package day03;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// todo:改进代码
public class DeadlockTryLockSolution {
    private static final Lock lock1 = new ReentrantLock();
    private static final Lock lock2 = new ReentrantLock();

    private static class TryLockThreadA extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    if (lock1.tryLock()) {
                        System.out.println("Thread A: Holding lock 1...");
                        Thread.sleep(50);
                        if (lock2.tryLock()) {
                            try {
                                System.out.println("Thread A: Holding lock 1 & 2...");
                                break; // 成功获得锁，完成工作
                            } finally {
                                lock2.unlock();
                            }
                        }
                        lock1.unlock();  // 释放 lock1，尝试重新获取
                    }
                    Thread.sleep(50);  // 等待一段时间后重试
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class TryLockThreadB extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    if (lock1.tryLock()) {
                        System.out.println("Thread B: Holding lock 1...");
                        Thread.sleep(50);
                        if (lock2.tryLock()) {
                            try {
                                System.out.println("Thread B: Holding lock 1 & 2...");
                                break; // 成功获得锁，完成工作
                            } finally {
                                lock2.unlock();
                            }
                        }
                        lock1.unlock();  // 释放 lock1，尝试重新获取
                    }
                    Thread.sleep(50);  // 等待一段时间后重试
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Thread threadA = new TryLockThreadA();
        Thread threadB = new TryLockThreadB();

        threadA.start();
        threadB.start();
    }
}
