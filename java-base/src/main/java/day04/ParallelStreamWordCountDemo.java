package day04;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ParallelStreamWordCountDemo {
    public static void main(String[] args) {

        // 1. 生成伪数据：10万个单词，词库10个单词
        String[] wordPool = {"java", "stream", "lambda", "parallel", "thread", "safe", "map", "reduce", "code", "test"};
        Random random = new Random();
        List<String> words = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            words.add(wordPool[random.nextInt(wordPool.length)]);
        }

        // 2. 错误用法：非线程安全的HashMap
        Map<String, Integer> wordCount = new HashMap<>();
        try {
            words.parallelStream().forEach(word -> {
                wordCount.merge(word, 1, Integer::sum); // 非线程安全
                // todo: 用你之前了解的方法重新实现下
            });
            System.out.println("错误用法结果（线程不安全，结果可能不对）:");
            System.out.println(wordCount);
        } catch (Exception e) {
            System.out.println("错误用法出现异常：" + e.getMessage());
        }

        // 3. 正确用法1：使用ConcurrentHashMap
        ConcurrentMap<String, Integer> safeWordCount = new ConcurrentHashMap<>();
        words.parallelStream().forEach(word -> {
            safeWordCount.merge(word, 1, Integer::sum); // 线程安全
        });
        System.out.println("正确用法1（ConcurrentHashMap）:");
        System.out.println(safeWordCount);

        // 4. 正确用法2：使用Collectors.groupingByConcurrent
        Map<String, Long> wordCount2 = words.parallelStream()
                .collect(Collectors.groupingByConcurrent(w -> w, Collectors.counting()));
        System.out.println("正确用法2（groupingByConcurrent）:");
        System.out.println(wordCount2);
    }
}
