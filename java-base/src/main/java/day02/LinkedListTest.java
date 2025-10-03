package day02;

import java.util.LinkedList;

/*
*链表（Linked list）是一种常见的基础数据结构，
* 是一种线性表，但是并不会按线性的顺序存储数据，
* 而是在每一个节点里存到下一个节点的地址。
*链表可分为单向链表和双向链表。
*Java LinkedList（链表） 类似于 ArrayList，是一种常用的数据容器。
与 ArrayList 相比，LinkedList 的增加和删除的操作效率更高，而查找和修改的操作效率较低。
以下情况使用 ArrayList :
频繁访问列表中的某一个元素。
只需要在列表末尾进行添加和删除元素操作。
以下情况使用 LinkedList :
你需要通过循环迭代来访问列表中的某些元素。
需要频繁的在列表开头、中间、末尾等位置进行添加和删除元素操作。
*
*
* */
public class LinkedListTest {
    public static void main(String[] args) {
        System.out.println("创建一个简单的链表");
        LinkedList<String> sites = new LinkedList<String>();
        sites.add("Google");
        sites.add("Runoob");
        sites.add("Taobao");
        sites.add("Weibo");
        System.out.println(sites);
        System.out.println("在链表头部添加元素");
        // 使用 addFirst() 在头部添加元素
        sites.addFirst("Wiki");
        System.out.println(sites);
        System.out.println("在列表结尾添加元素");
        // 使用 addLast() 在尾部添加元素
        sites.addLast("Wiki");
        System.out.println(sites);
        System.out.println("在列表开头移除元素");
        // 使用 removeFirst() 移除头部元素
        sites.removeFirst();
        System.out.println(sites);
        System.out.println("在列表结尾移除元素");
        // 使用 removeLast() 移除尾部元素
        sites.removeLast();
        System.out.println(sites);
        System.out.println("获取列表开头的元素");
        // 使用 getFirst() 获取头部元素
        System.out.println(sites.getFirst());
        System.out.println("获取列表结尾的元素");
        // 使用 getLast() 获取尾部元素
        System.out.println(sites.getLast());
        System.out.println("迭代列表中的元素");
        //迭代元素
        for (String site : sites) {
            System.out.println(site);
        }
    }
}
