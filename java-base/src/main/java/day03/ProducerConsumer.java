/*
 * 
 * 4. 线程通信
 *    实现生产者-消费者问题，使用 `wait()` 和 `notify()` 进行线程通信
 */


package day03;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {
    
    static class boundedBuffer<T> {
    
        private final ArrayDeque<T> q = new ArrayDeque<>();
        private final int cap;
        private boolean closed = false;

        public boundedBuffer(int cap) {
            this.cap = cap;
        }

        public synchronized void put(T x) throws InterruptedException {
            while (q.size() == cap) {
                wait();
                if (closed) throw new IllegalStateException("buffer closed");
            }
            

            q.addLast(x);
            notifyAll();
        }

        public synchronized T take() throws InterruptedException {
            while (q.size() == 0) {
                wait(); 
                if (closed) throw new IllegalStateException("buffer closed");
            }
            

            T v = q.pollFirst();
            notifyAll();
            return v;
        }

        public synchronized void close() {
            closed = true;
            notifyAll(); // 叫醒所有等待者，看见 closed 后退出
        }


    }

    public static void main(String[] args) {
        boundedBuffer<Integer> buf = new boundedBuffer<>(5);

        Thread producer = new Thread(
            () -> {
                for (int i=1; i<=20; i++) {
                    try {buf.put(i); System.out.println("produce " + i); }
                    catch (InterruptedException e) {}
                    
                }; 
                buf.close(); 
            }, "producer"
        );


        Thread consumer1 = new Thread(
            () -> {
                for (int i=1; i<=20; i++) {
                    try {int f = buf.take(); System.out.println("consume " + f); }
                    catch (InterruptedException e) {}
                }
            }, "consumer1"
        );

        Thread consumer2 = new Thread(
            () -> {
                for (int i=1; i<=20; i++) {
                    try {int f = buf.take(); System.out.println("consume " + f); }
                    catch (InterruptedException e) {}
                }
            }, "consumer2"
        );

        Thread consumer3 = new Thread(
            () -> {
                for (int i=1; i<=20; i++) {
                    try {int f = buf.take(); System.out.println("consume " + f); }
                    catch (InterruptedException e) {}
                }
            }, "consumer3"
        );

        producer.start();
        consumer1.start();
        consumer2.start();
        consumer3.start();

        
    }
}
