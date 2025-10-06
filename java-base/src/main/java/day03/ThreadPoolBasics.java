/*
 * 6. 线程池
 * 使用线程池与 `Callable` 计算一组任务的并发执行结果
 */


package day03;

import java.util.*;
import java.util.concurrent.*;

public class ThreadPoolBasics {
    static int work(int n) {
        try { Thread.sleep(100 + n % 200); } catch (InterruptedException ignored) {}
        return n * n;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);

        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            final int x = i;
            futures.add(pool.submit(() -> work(x)));
        }

        // 收集结果（带超时）
        for (Future<Integer> f : futures) {
            try {
                System.out.println("result: " + f.get(500, TimeUnit.MILLISECONDS));
            } catch (TimeoutException te) {
                System.out.println("task timed out, canceling...");
                f.cancel(true);
            }
        }

        pool.shutdown();
        if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("force shutdownNow");
            pool.shutdownNow();
        }
    }
}
