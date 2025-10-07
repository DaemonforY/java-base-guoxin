package day04;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class LogFileGenerator {
    public static void main(String[] args) throws IOException {
        String[] levels = {"INFO", "WARN", "ERROR"};
        String[] messages = {
                "Start application",
                "Low disk space",
                "NullPointerException",
                "User login",
                "OutOfMemoryError",
                "Connection timeout",
                "File not found",
                "User logout",
                "Permission denied",
                "Service started"
        };

        int lines = 200; // 生成200条日志
        Random random = new Random();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String resourcePath = "src/main/resources/app.log";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resourcePath))) {
            LocalDateTime now = LocalDateTime.now();
            for (int i = 0; i < lines; i++) {
                String level = levels[random.nextInt(levels.length)];
                String msg = messages[random.nextInt(messages.length)];
                String time = now.plusSeconds(i).format(dtf);
                writer.write(time + " [" + level + "]  " + msg);
                writer.newLine();
            }
        }
        System.out.println("app.log 文件已生成！");
    }
}
