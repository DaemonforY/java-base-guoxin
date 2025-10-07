package day04;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransactionSumDemo {
    static class Transaction {
        private final double amount;
        public Transaction(double amount) { this.amount = amount; }
        public double getAmount() { return amount; }
    }

    public static void main(String[] args) throws Exception {
        // 1. 生成100万条伪交易数据
        int total = 1_000_000;
        Random random = new Random();
        List<Transaction> transactions = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            transactions.add(new Transaction(1 + random.nextDouble() * 999)); // 金额1~1000
        }

        // 2. 传统for循环
        long t1 = System.currentTimeMillis();
        double sum1 = 0;
        for (Transaction t : transactions) {
            sum1 += t.getAmount();
        }
        long t2 = System.currentTimeMillis();
        System.out.printf("传统for循环总金额: %.2f, 用时: %d ms%n", sum1, (t2 - t1));

        // 3. Stream并行流
        long t3 = System.currentTimeMillis();
        double sum2 = transactions.parallelStream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        long t4 = System.currentTimeMillis();
        System.out.printf("并行流总金额: %.2f, 用时: %d ms%n", sum2, (t4 - t3));

        // 4. 多线程（线程池+分片）
        long t5 = System.currentTimeMillis();
        int threadNum = 8;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        List<Future<Double>> futures = new ArrayList<>();
        int chunkSize = total / threadNum;
        for (int i = 0; i < threadNum; i++) {
            int start = i * chunkSize;
            int end = (i == threadNum - 1) ? total : (i + 1) * chunkSize;
            futures.add(executor.submit(() -> {
                double localSum = 0;
                for (int j = start; j < end; j++) {
                    localSum += transactions.get(j).getAmount();
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
