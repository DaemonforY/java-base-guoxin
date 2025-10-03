package day02;

import java.util.HashSet;

/*
 *HashSet 基于 HashMap 来实现的，是一个不允许有重复元素的集合。
 *HashSet 允许有 null 值。
 *HashSet 是无序的，即不会记录插入的顺序。
 *HashSet 不是线程安全的， 如果多个线程尝试同时修改 HashSet，则最终结果是不确定的。
 * */
public class HashSetTest {
    public static void main(String[] args) {
        System.out.println("添加元素");
        HashSet<String> sites = new HashSet<String>();
        sites.add("Google");
        sites.add("Runoob");
        sites.add("Taobao");
        sites.add("Zhihu");
        sites.add("Runoob");  // 重复的元素不会被添加
        System.out.println(sites);
        System.out.println("判断元素是否存在");
        System.out.println(sites.contains("Taobao"));
        System.out.println("删除元素");
        sites.remove("Taobao");  // 删除元素，删除成功返回 true，否则为 false
        System.out.println(sites);
        System.out.println("删除所有元素");
        sites.clear();
        System.out.println(sites);
        System.out.println("计算大小");
        System.out.println(sites.size());
    }

}
