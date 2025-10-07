package day04;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransactionSumHeavyDemo {

    static class Transaction {
        private final double amount;
        private final int n; // 计算参数

        public Transaction(double amount, int n) {
            this.amount = amount;
            this.n = n;
        }
        public double getAmount() { return amount; }
        public int getN() { return n; }
    }


    // 递归斐波那契，模拟CPU密集型任务
    static long fib(int n) {
        if (n <= 1) return n;
        return fib(n - 1) + fib(n - 2);
    }
    // 1 , 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229,


    public static void main(String[] args) throws Exception {
        int total = 100;
        Random random = new Random();
        List<Transaction> transactions = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            transactions.add(new Transaction(1 + random.nextDouble() * 999, 35 + random.nextInt(5))); // n=35~39
        }

        // 1. 传统for循环
        long t1 = System.currentTimeMillis();
        double sum1 = 0;
        for (Transaction t : transactions) {
            sum1 += t.getAmount() * fib(t.getN());
        }
        long t2 = System.currentTimeMillis();
        System.out.printf("传统for循环总金额: %.2f, 用时: %d ms%n", sum1, (t2 - t1));

        // 2. Stream并行流
        long t3 = System.currentTimeMillis();
        double sum2 = transactions.parallelStream()
                .mapToDouble(t -> t.getAmount() * fib(t.getN()))
                .sum();
        long t4 = System.currentTimeMillis();
        System.out.printf("并行流总金额: %.2f, 用时: %d ms%n", sum2, (t4 - t3));

        // 3. 多线程（线程池+分片）
        long t5 = System.currentTimeMillis();
        int threadNum = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        List<Future<Double>> futures = new ArrayList<>();
        int chunkSize = total / threadNum;
        for (int i = 0; i < threadNum; i++) {
            int start = i * chunkSize;
            int end = (i == threadNum - 1) ? total : (i + 1) * chunkSize;
            futures.add(executor.submit(() -> {
                double localSum = 0;
                for (int j = start; j < end; j++) {
                    Transaction t = transactions.get(j);
                    localSum += t.getAmount() * fib(t.getN());
                }
                return localSum;
            }));
        }
        double sum3 = 0;
        for (Future<Double> f : futures) {
            sum3 += f.get();
        }
        executor.shutdown();
        long t6 = System.currentTimeMillis();
        System.out.printf("多线程总金额: %.2f, 用时: %d ms%n", sum3, (t6 - t5));
    }

}
