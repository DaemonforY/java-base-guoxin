/*
 * 2. 创建与启动线程
 * 通过实现 Runnable 接口来创建线程
 */

package day03;

class RunnableDemo implements Runnable {
    private Thread t;
    private String threadName;

    RunnableDemo(String name) {
        threadName = name;
        System.out.println("Creating thread: " + threadName);
    }

    public void run() {
        System.out.println("Running thread: " + threadName);

        try {
            for (int i=4; i>0; i--) {
                System.out.println("Thread: " + threadName + ", " + i);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }

        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start() {
        System.out.println("Starting " +  threadName );

        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

}


public class CreateThreadRunnable {
    
    public static void main(String[] args) {
        RunnableDemo r1 = new RunnableDemo("thread 1");
        r1.start();

        RunnableDemo r2 = new RunnableDemo("thread 2");
        r2.start();
    }
}
