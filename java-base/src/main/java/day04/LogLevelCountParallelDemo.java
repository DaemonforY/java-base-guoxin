package day04;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogLevelCountParallelDemo {
    public static void main(String[] args) throws Exception {
        // 日志文件在 resources 目录
        Path logPath = Paths.get("src/main/resources/app.log");

        // try-with-resources 自动关闭文件流
        try (Stream<String> lines = Files.lines(logPath)) {
            // 并行流处理 + 并发分组统计
            ConcurrentMap<String, Long> logCounts = lines
                    .parallel()  // 并行流
                    .filter(line -> line.contains("[") && line.contains("]"))
                    .map(line -> {
                        int left = line.indexOf('[');
                        int right = line.indexOf(']');
                        if (left != -1 && right != -1 && right > left) {
                            return line.substring(left + 1, right);
                        }
                        return "UNKNOWN";
                    })
                    .collect(Collectors.groupingByConcurrent(
                            level -> level,
                            Collectors.counting()
                    ));

            // 输出统计结果
            logCounts.forEach((level, count) ->
                    System.out.println(level + ": " + count)
            );
        }
    }
}
