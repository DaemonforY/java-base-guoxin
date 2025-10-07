package day04;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 4.0 (Java 8 Dynamic Version)
 * @Description:
 * 案例：找出北京地区的前 TopK 名 VIP 用户
 *
 * 三种方案：
 * 1. 传统循环 + 排序（O(n log n)）
 * 2. 小顶堆算法（O(n log k)）
 * 3. Stream API（声明式）
 *
 * 支持：动态输入数据量 N 和 TopK
 * 输出：性能对比表 + 控制台直方图（兼容 Java8）
 * @Author yongbin
 */
public class UserStreamDemo {

    @Getter
    @Setter
    static class User {
        private final String name;
        private final String city;
        private final boolean vip;
        private final int score;

        public User(String name, String city, boolean vip, int score) {
            this.name = name;
            this.city = city;
            this.vip = vip;
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format("User{name='%s', city='%s', vip=%s, score=%d}",
                    name, city, vip, score);
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要生成的用户数量 (例如 100000、500000、2000000)：");
        int totalUsers = scanner.nextInt();
        System.out.println("请输入要选取的 TopK 值 (例如 10、50、100)：");
        int topK = scanner.nextInt();
        System.out.println("正在生成测试数据，请稍候...");

        // 1️⃣ 生成数据
        List<String> cityList = Arrays.asList("Beijing", "Shanghai", "Guangzhou", "Shenzhen", "Chengdu");
        Random random = new Random();
        List<User> users = new ArrayList<>(totalUsers);
        for (int i = 1; i <= totalUsers; i++) {
            String name = "User" + i;
            String city = cityList.get(random.nextInt(cityList.size()));
            boolean vip = random.nextDouble() < 0.2;
            int score = 50 + random.nextInt(51);
            users.add(new User(name, city, vip, score));
        }

        System.out.println("数据准备完成！共生成 " + users.size() + " 条记录。开始性能测试...\n");

        // 2️⃣ 三种方案计时
        long time1 = testTraditional(users, topK);
        long time2 = testHeap(users, topK);
        long time3 = testStream(users, topK);

        // 3️⃣ 打印性能对比表格
        System.out.println("\n================= 性能对比表 =================");
        System.out.printf("%-25s | %-10s | %-10s%n", "方案", "耗时(ms)", "性能条");
        System.out.println("---------------------------------------------");

        long maxTime = Math.max(time1, Math.max(time2, time3));
        printPerf("方案一：传统循环 + 排序", time1, maxTime);
        printPerf("方案二：小顶堆算法", time2, maxTime);
        printPerf("方案三：Stream API", time3, maxTime);
        System.out.println("=============================================\n");

        // 4️⃣ 打印部分结果（小顶堆结果验证）
        System.out.println("北京地区前 " + topK + " 名 VIP 用户（小顶堆算法结果）：");
        List<User> result = getTopKByHeap(users, topK);
        for (User u : result) {
            System.out.println(u);
        }

        scanner.close();
    }

    /** 方案1：传统循环 + 排序 */
    private static long testTraditional(List<User> users, int topK) {
        long start = System.currentTimeMillis();
        List<User> list = new ArrayList<>();
        for (User user : users) {
            if ("Beijing".equals(user.getCity()) && user.isVip()) {
                list.add(user);
            }
        }
        Collections.sort(list, Comparator.comparing(User::getScore).reversed());
        if (list.size() > topK) {
            list = list.subList(0, topK);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    /** 方案2：小顶堆算法 */
    private static long testHeap(List<User> users, int topK) {
        long start = System.currentTimeMillis();
        getTopKByHeap(users, topK); // 调用复用逻辑
        long end = System.currentTimeMillis();
        return end - start;
    }

    /** 小顶堆TopK逻辑复用 */
    private static List<User> getTopKByHeap(List<User> users, int topK) {
        PriorityQueue<User> heap = new PriorityQueue<>(Comparator.comparing(User::getScore));
        for (User user : users) {
            if ("Beijing".equals(user.getCity()) && user.isVip()) {
                heap.offer(user);
                if (heap.size() > topK) {
                    heap.poll();
                }
            }
        }
        List<User> topKList = new ArrayList<>(heap);
        Collections.sort(topKList, Comparator.comparing(User::getScore).reversed());
        return topKList;
    }

    /** 方案3：Stream API */
    private static long testStream(List<User> users, int topK) {
        long start = System.currentTimeMillis();
        users.stream()
                .filter(u -> "Beijing".equals(u.getCity()))
                .filter(User::isVip)
                .sorted(Comparator.comparing(User::getScore).reversed())
                .limit(topK)
                .collect(Collectors.toList());
        long end = System.currentTimeMillis();
        return end - start;
    }

    /** 控制台直方图（Java8写法） */
    private static void printPerf(String label, long time, long maxTime) {
        int barLength = (int) ((50.0 * time) / maxTime);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < barLength; i++) {
            bar.append('█');
        }
        System.out.printf("%-25s | %-10d | %s%n", label, time, bar.toString());
    }
}
