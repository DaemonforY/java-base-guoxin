package day02;

import java.util.*;

public class CollectionDemo {
    public static void main(String[] args) {
        /*
         * 主函数，用于演示Java中集合类的使用方法
         * 包括Collection、List、Set、Map等接口的实现类示例
         * 以及如何遍历这些集合
         */

        // Collection示例
        // 创建一个ArrayList实例，展示Collection接口的基本用法
        Collection<String> collection = new ArrayList<>();
        collection.add("Element 1");
        collection.add("Element 2");
        System.out.println("Collection: " + collection);

        // List示例
        // 创建一个ArrayList实例，展示List接口允许重复元素的特性
        List<String> list = new ArrayList<>();
        list.add("Item 1");
        list.add("Item 2");
        list.add("Item 1"); // List允许重复元素
        System.out.println("List: " + list);

        // Set示例
        // 创建一个HashSet实例，展示Set接口不允许重复元素的特性
        Set<String> set = new HashSet<>();
        set.add("Unique 1");
        set.add("Unique 2");
        set.add("Unique 1"); // Set会自动去掉重复元素
        System.out.println("Set: " + set);

        // Map示例
        // 创建一个HashMap实例，展示Map接口键值对存储及键的唯一性
        Map<String, Integer> map = new HashMap<>();
        map.put("Key1", 100);
        map.put("Key2", 200);
        map.put("Key1", 300); // Map会覆盖重复的键
        System.out.println("Map: " + map);

        // 遍历集合
        // 以下代码块展示了如何遍历Collection、List、Set和Map集合
        System.out.println("\n遍历 Collection:");
        for (String item : collection) {
            System.out.println(item);
        }

        System.out.println("\n遍历 List:");
        for (String item : list) {
            System.out.println(item);
        }

        System.out.println("\n遍历 Set:");
        for (String item : set) {
            System.out.println(item);
        }

        System.out.println("\n遍历 Map:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Hashtable和HashMap的实例化，用于展示不同集合类的差异
        // 这里没有具体使用它们的代码，仅作为示例
        Hashtable<Object, Object> objectObjectHashtable = new Hashtable<>();
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
    }

}

