package day03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageProcessingExample {
    public void processImagesSingleThread() {
        ImageProcessor processor = new ImageProcessor();
        long startTime = System.currentTimeMillis();

        // 单线程逐个处理图像
        for (int i = 0; i < 20; i++) {
            processor.processImage("Image" + i);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("单线程处理时间: " + (endTime - startTime) + " 毫秒");
    }

    public void processImagesMultiThread() {
        ImageProcessor processor = new ImageProcessor();
        long startTime = System.currentTimeMillis();

        // 使用线程池进行多线程处理
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            String imageName = "Image" + i;
            executorService.execute(() -> processor.processImage(imageName));
        }

        // 关闭线程池并等待任务完成
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            // 等待所有任务完成
        }

        long endTime = System.currentTimeMillis();
        System.out.println("多线程处理时间: " + (endTime - startTime) + " 毫秒");
    }
    public static void main(String[] args) {
        ImageProcessingExample example = new ImageProcessingExample();

        System.out.println("开始单线程处理图像:");
        example.processImagesSingleThread();

        System.out.println("\n开始多线程处理图像:");
        example.processImagesMultiThread();
    }

    static class ImageProcessor {
        // 处理图像方法
        public void processImage(String imageName) {
            System.out.println(Thread.currentThread().getName() + " 正在处理: " + imageName);
            try {
                // 模拟图像处理时间
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // 图像缩放和滤波处理，例如调用经典库
            // 具体处理逻辑省略
            System.out.println(Thread.currentThread().getName() + " 处理完成: " + imageName);
        }
    }
}
