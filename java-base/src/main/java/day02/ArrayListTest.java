package day02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/*
 *ArrayList 类是一个可以动态修改的数组，
 *与普通数组的区别就是它是没有固定大小的限制，我们可以添加或删除元素。
 * ArrayList 继承了 AbstractList ，并实现了 List 接口。
 *ArrayList 是一个数组队列，提供了相关的添加、删除、修改、遍历等功能
 *
 */
public class ArrayListTest {
    public static void main(String[] args) {
        System.out.println("======访问元素=====");
        ArrayList<String> sites = new ArrayList<String>();
        sites.add("Google");
        sites.add("Runoob");
        sites.add("Taobao");
        sites.add("Facebook");
        System.out.println(sites.get(1)); //访问第一个元素
        System.out.println("===========修改元素========");
        sites.set(2, "Wiki"); // 第一个参数为索引位置，第二个为要修改的值
        System.out.println(sites);
        System.out.println("==========删除元素==========");
        sites.remove(1); // 删除索引为1的元素
        System.out.println(sites);
        System.out.println("==========集合大小==========");
        System.out.println(sites.size());
        System.out.println("==========迭代数组列表=======");
        for (String site : sites) {
            System.out.println(site);
        }
        System.out.println("ArrayList 存储数字(使用 Integer 类型)");
        ArrayList<Integer> myNumbers = new ArrayList<Integer>();
        myNumbers.add(10);
        myNumbers.add(15);
        myNumbers.add(20);
        myNumbers.add(25);
        for (int i : myNumbers) {
            System.out.println(i);
        }

        System.out.println("========字母排序==========");
        Collections.sort(sites);  // 字母排序
        for (String i : sites) {
            System.out.println(i);
        }
        System.out.println("========数字排序==========");
        for (int i : myNumbers) {
            System.out.println(i);
        }

        Iterator<String> iterator = sites.iterator();

    }
}

