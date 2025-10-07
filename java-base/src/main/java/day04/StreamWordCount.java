package day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamWordCount {
    public static void main(String[] args) {
        // 文件路径
        String filePath = "data/input.txt";

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {

            Map<String, Long> wordCount = lines
                    // 1️⃣ 每一行 -> 拆分为单词数组（使用正则分隔）
                    .flatMap(line -> Arrays.stream(line.split("\\W+")))
                    // 2️⃣ 转为小写，去除空单词
                    .filter(word -> !word.isEmpty())
                    .map(String::toLowerCase)
                    // 3️⃣ 按单词分组统计
                    .collect(Collectors.groupingBy(
                            Function.identity(), // key：单词本身
                            Collectors.counting() // value：出现次数
                    ));

            // 4️⃣ 排序输出（按出现次数降序）
            wordCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(entry ->
                            System.out.printf("%-15s -> %d%n", entry.getKey(), entry.getValue())
                    );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
