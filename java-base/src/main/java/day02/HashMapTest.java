package day02;

import java.util.HashMap;

/*
 * HashMap 是一个散列表，它存储的内容是键值对(key-value)映射。
 * HashMap 实现了 Map 接口，根据键的 HashCode 值存储数据，
 * 具有很快的访问速度，最多允许一条记录的键为 null，不支持线程同步。
 * HashMap 是无序的，即不会记录插入的顺序。
 * HashMap 的 key 与 value 类型可以相同也可以不同.
 * */
public class HashMapTest {
    public static void main(String[] args) {
        System.out.println("添加元素");
        // 创建 HashMap 对象 Sites
        HashMap<Integer, String> Sites1 = new HashMap<Integer, String>();
        // 添加键值对
        Sites1.put(1, "Google");
        Sites1.put(2, "Runoob");
        Sites1.put(3, "Taobao");
        Sites1.put(4, "Zhihu");
        System.out.println(Sites1);
        // 创建 HashMap 对象 Sites
        HashMap<String, String> Sites2 = new HashMap<String, String>();
        // 添加键值对
        Sites2.put("one", "Google");
        Sites2.put("two", "Runoob");
        Sites2.put("three", "Taobao");
        Sites2.put("four", "Zhihu");
        System.out.println(Sites2);
        System.out.println("访问元素");
        System.out.println(Sites1.get(3));//就是第三个，不是第四个
        System.out.println("删除元素");
        Sites1.remove(4);
        System.out.println(Sites1);
        System.out.println("清空所有元素");
        Sites1.clear();
        System.out.println(Sites1);

    }

}

