package day03;

public class HelloWorld {
    public static void main(String[] args) throws InterruptedException {
        Thread myThread = new Thread() {
            public void run() {
                System.out.println("Hello1 World!");
            }
        };
        Thread myThread2 = new Thread() {
            public void run() {
                System.out.println("Hello2 World!");
            }
        };
        Thread myThread3 = new Thread() {
            public void run() {
                System.out.println("Hello3 World!");
            }
        };

        myThread.start();
        myThread2.start();
        myThread3.start();
//        Thread.yield();
        System.out.println("main thread");
        myThread.join();
    }
}
