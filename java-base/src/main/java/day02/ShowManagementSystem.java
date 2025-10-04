package day02;

import java.util.*;

/**
 * 节目管理系统 - 演示List接口的使用
 * 包含ArrayList和LinkedList两种实现方式
 * ================================
 * 节目管理系统: 实现Hashset
 * ===================================
 */
public class ShowManagementSystem {

    // 使用ArrayList存储节目
    private List<String> showList;
    // 使用LinkedList存储节目
    private LinkedList<String> showLinkedList;

    private HashSet<String> showHashSet;

    /**
     * 构造函数，用于初始化节目管理系统
     */
    public ShowManagementSystem() {
        // 初始化ArrayList
        this.showList = new ArrayList<>();

        // 初始化LinkedList
        this.showLinkedList = new LinkedList<>();

        this.showHashSet = new HashSet<>();
    }


    /**
     * 添加节目到ArrayList
     */
    public void addShow(String showName) {
        if (showName != null && !showName.trim().isEmpty()) {
            showList.add(showName.trim());
            System.out.println("节目 '" + showName.trim() + "' 已添加到节目列表");
        } else {
            System.out.println("节目名称不能为空！");
        }
    }

    /**
     * 添加节目到LinkedList
     */
    public void addShowToLinkedList(String showName) {
        if (showName != null && !showName.trim().isEmpty()) {
            showLinkedList.add(showName.trim());
            System.out.println("节目 '" + showName.trim() + "' 已添加到LinkedList");
        } else {
            System.out.println("节目名称不能为空！");
        }
    }


    /**
     * 添加节目到Hashset
     */

    public void addShowToHashset(String showName) {
        if (showName != null && !showName.trim().isEmpty()) {
            showHashSet.add(showName);
        } else {
            System.out.printf("Argument %s is invalid", showName);
        }

    }

    /**
     * 删除节目（从ArrayList）
     */
    public boolean removeShow(String showName) {
        if (showName == null || showName.trim().isEmpty()) {
            System.out.println("节目名称不能为空！");
            return false;
        }

        boolean removed = showList.remove(showName.trim());
        if (removed) {
            System.out.println("节目 '" + showName.trim() + "' 已从列表中删除");
        } else {
            System.out.println("节目 '" + showName.trim() + "' 不存在于列表中");
        }
        return removed;
    }

    /**
     * 删除节目（从LinkedList）
     */
    public boolean removeShowFromLinkedList(String showName) {
        if (showName == null || showName.trim().isEmpty()) {
            System.out.println("节目名称不能为空！");
            return false;
        }

        boolean removed = showLinkedList.remove(showName.trim());
        if (removed) {
            System.out.println("节目 '" + showName.trim() + "' 已从LinkedList中删除");
        } else {
            System.out.println("节目 '" + showName.trim() + "' 不存在于LinkedList中");
        }
        return removed;
    }

    /**
     * 删除节目（从Hashset）
     */

    public boolean removeShowFromHashSet(String showName) {
        if (showName == null || showName.trim().isEmpty()) {
            System.out.println("节目名称不能为空！");
            return false;
        }

        boolean removed = showHashSet.remove(showName.trim());
        if (removed) {
            System.out.println("节目 '" + showName.trim() + "' 已从HahSet中删除");
        } else {
            System.out.println("节目 '" + showName.trim() + "' 不存在于LinkedList中");
        }

        return removed;


    }

    /**
     * 查找节目是否存在（在ArrayList中）
     */
    public boolean findShow(String showName) {
        if (showName == null || showName.trim().isEmpty()) {
            System.out.println("节目名称不能为空！");
            return false;
        }

        boolean exists = showList.contains(showName.trim());
        if (exists) {
            System.out.println("节目 '" + showName.trim() + "' 存在于列表中");
        } else {
            System.out.println("节目 '" + showName.trim() + "' 不存在于列表中");
        }
        return exists;
    }

    /**
     * 查找节目是否存在（在LinkedList中）
     */
    public boolean findShowInLinkedList(String showName) {
        if (showName == null || showName.trim().isEmpty()) {
            System.out.println("节目名称不能为空！");
            return false;
        }

        boolean exists = showLinkedList.contains(showName.trim());
        if (exists) {
            System.out.println("节目 '" + showName.trim() + "' 存在于LinkedList中");
        } else {
            System.out.println("节目 '" + showName.trim() + "' 不存在于LinkedList中");
        }
        return exists;
    }

    /**
     * 查找节目是否存在（在Hashset中）
     */

    public boolean findShowInHashSet(String showName) {
        if (showName == null || showName.trim().isEmpty()) {
            System.out.println("节目名称不能为空！");
            return false;
        }

        boolean exists = showHashSet.contains(showName.trim());
        if (exists) {
            System.out.println("节目 '" + showName.trim() + "' 存在于LinkedList中");
        } else {
            System.out.println("节目 '" + showName.trim() + "' 不存在于LinkedList中");
        }
        return exists;

    }

    

    /**
     * 展示节目列表（ArrayList）
     */
    public void displayShows() {
        if (showList.isEmpty()) {
            System.out.println("节目列表为空");
            return;
        }

        System.out.println("\n=== 当前节目列表 (ArrayList) ===");
        for (int i = 0; i < showList.size(); i++) {
            System.out.println((i + 1) + ". " + showList.get(i));
        }
        System.out.println("总共有 " + showList.size() + " 个节目");
    }

    /**
     * 展示节目列表（LinkedList）
     */
    public void displayLinkedListShows() {
        if (showLinkedList.isEmpty()) {
            System.out.println("LinkedList节目列表为空");
            return;
        }

        System.out.println("\n=== 当前节目列表 (LinkedList) ===");
        for (int i = 0; i < showLinkedList.size(); i++) {
            System.out.println((i + 1) + ". " + showLinkedList.get(i));
        }
        System.out.println("总共有 " + showLinkedList.size() + " 个节目");
    }


    /**
     * 展示节目列表（Hashset）
     */
    public void displayHashSetShows() {
        if (showHashSet.isEmpty()) {
            System.out.println("LinkedList节目列表为空");
            return;
        }

        System.out.println("\n=== 当前节目列表 (LinkedList) ===");
        for (int i = 0; i < showHashSet.size(); i++) {
            System.out.println((i + 1) + ". " + showLinkedList.get(i));
        }
        System.out.println("总共有 " + showLinkedList.size() + " 个节目");
    }


    /**
     * 使用迭代器遍历ArrayList
     */
    public void iterateWithIterator() {
        if (showList.isEmpty()) {
            System.out.println("节目列表为空，无法迭代");
            return;
        }

        System.out.println("\n=== 使用迭代器遍历ArrayList ===");
        Iterator<String> iterator = showList.iterator();
        int index = 1;
        while (iterator.hasNext()) {
            System.out.println(index++ + ". " + iterator.next());
        }
    }

    /**
     * 使用增强for循环遍历LinkedList
     */
    public void iterateWithForEach() {
        if (showLinkedList.isEmpty()) {
            System.out.println("LinkedList节目列表为空，无法迭代");
            return;
        }

        System.out.println("\n=== 使用增强for循环遍历LinkedList ===");
        int index = 1;
        for (String show : showLinkedList) {
            System.out.println(index++ + ". " + show);
        }
    }


    /**
     * 使用增强for循环遍历Hashset
     */
    public void ForEachHashset() {
        if (showHashSet.isEmpty()) {
            System.out.println("Hashset节目列表为空，无法迭代");
            return;
        }

        System.out.println("\n=== 使用增强for循环遍历LinkedList ===");
        int index = 1;
        for (String show : showLinkedList) {
            System.out.println(index++ + ". " + show);
        }
    }

    /**
     * 使用迭代器遍历Hashset
     */

    public void IteratorWithHashset() {
        if (showHashSet.isEmpty()) {
            System.out.println("Hashset节目列表为空，无法迭代");
            return;
        }

        Iterator<String> it = showHashSet.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
            
        }
    }



    /**
     * 演示List与数组的区别
     */
    public void demonstrateListVsArray() {
        System.out.println("\n=== List与数组的区别演示 ===");

        // 数组操作
        String[] array = new String[3];
        array[0] = "节目1";
        array[1] = "节目2";
        array[2] = "节目3";

        System.out.println("数组操作：");
        System.out.println("- 固定大小：" + array.length);
        System.out.println("- 访问元素：array[0] = " + array[0]);

        // List操作
        List<String> list = new ArrayList<>();
        list.add("节目1");
        list.add("节目2");
        list.add("节目3");

        System.out.println("\nList操作：");
        System.out.println("- 动态大小：" + list.size());
        System.out.println("- 访问元素：list.get(0) = " + list.get(0));
        System.out.println("- 添加元素：list.add(\"新节目\")");
        list.add("新节目");
        System.out.println("- 添加后大小：" + list.size());
        System.out.println("- 删除元素：list.remove(0)");
        list.remove(0);
        System.out.println("- 删除后大小：" + list.size());
    }

    /**
     * 演示ArrayList和LinkedList的性能差异
     */
    public void demonstratePerformanceDifference() {
        System.out.println("\n=== ArrayList vs LinkedList 性能演示 ===");

        // 测试ArrayList的随机访问性能
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            arrayList.add("节目" + i);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            arrayList.get(i * 10); // 随机访问
        }
        long arrayListTime = System.nanoTime() - startTime;

        // 测试LinkedList的随机访问性能
        LinkedList<String> linkedList = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            linkedList.add("节目" + i);
        }

        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            linkedList.get(i * 10); // 随机访问
        }
        long linkedListTime = System.nanoTime() - startTime;

        System.out.println("ArrayList随机访问1000次耗时：" + arrayListTime + " 纳秒");
        System.out.println("LinkedList随机访问1000次耗时：" + linkedListTime + " 纳秒");
        System.out.println("ArrayList在随机访问方面性能更好");
    }

    /**
     * 主方法 - 演示节目管理系统的使用
     */
    public static void main(String[] args) {
        ShowManagementSystem system = new ShowManagementSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 节目管理系统 ===");
        System.out.println("演示List接口的使用（ArrayList和LinkedList）");

        // 添加一些示例节目
        System.out.println("\n1. 添加节目到ArrayList：");
        system.addShow("新闻联播");
        system.addShow("天气预报");
        system.addShow("综艺大观");
        system.addShow("体育新闻");

        System.out.println("\n2. 添加节目到LinkedList：");
        system.addShowToLinkedList("新闻联播");
        system.addShowToLinkedList("天气预报");
        system.addShowToLinkedList("综艺大观");
        system.addShowToLinkedList("体育新闻");

        // 显示节目列表
        system.displayShows();
        system.displayLinkedListShows();

        // 查找节目
        System.out.println("\n3. 查找节目：");
        system.findShow("新闻联播");
        system.findShow("电影频道");

        // 删除节目
        System.out.println("\n4. 删除节目：");
        system.removeShow("天气预报");
        system.displayShows();

        // 迭代演示
        system.iterateWithIterator();
        system.iterateWithForEach();

        // 演示List与数组的区别
        system.demonstrateListVsArray();

        // 演示性能差异
        system.demonstratePerformanceDifference();

        // 交互式操作
        System.out.println("\n=== 交互式操作 ===");
        while (true) {
            System.out.println("\n请选择操作：");
            System.out.println("1. 添加节目");
            System.out.println("2. 删除节目");
            System.out.println("3. 查找节目");
            System.out.println("4. 显示节目列表");
            System.out.println("5. 退出");
            System.out.print("请输入选择 (1-5): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("请输入节目名称: ");
                    String showName = scanner.nextLine();
                    system.addShow(showName);
                    break;
                case "2":
                    System.out.print("请输入要删除的节目名称: ");
                    String removeName = scanner.nextLine();
                    system.removeShow(removeName);
                    break;
                case "3":
                    System.out.print("请输入要查找的节目名称: ");
                    String findName = scanner.nextLine();
                    system.findShow(findName);
                    break;
                case "4":
                    system.displayShows();
                    break;
                case "5":
                    System.out.println("感谢使用节目管理系统！");
                    scanner.close();
                    return;
                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }
    }
}
