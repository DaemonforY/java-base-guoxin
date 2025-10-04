/*
 * 4. Map 接口
 * 
 * 创建一个简单的学生信息系统，通过学生学号查找学生的信息。
 * 
 * key: student id, value: name
 */


package day02;

import java.util.*;

public class StudentInfo {
    HashMap<Integer, String> information;

    public StudentInfo() {
        this.information = new HashMap<>();
    }

    // add student
    public void addStudent(int sid, String name) {
        if (name ==null) return;

        if (information.containsKey(sid)) {
            System.out.printf("student %d has existed, if you hope update information, please use StudentInfo.updateStudent!\n", sid);
            return;
        }

        information.put(sid, name);
        System.out.printf("student %d has been added\n", sid);

    }

    // remove student
    public void removeStudent(int sid) {
        if (!information.containsKey(sid)) {
            System.out.printf("student %d does not exist\n", sid);
            return;
        }

        information.remove(sid);
        System.out.printf("student %d has been removed\n", sid);

    }

    // update info 
    public void updateStudent(int sid, String name) {
        if (name ==null) return;

        if (!information.containsKey(sid)) {
            System.out.printf("student %d does not exist\n", sid);
            return;
        }

        information.put(sid, name);
        System.out.printf("student %d's name has been update: %s\n", sid, name);

    }

    // get info 
    public void getStudent(int sid) {
        if (!information.containsKey(sid)) {
            System.out.printf("student %d does not exist\n", sid);
            return;
        }

        
        System.out.printf("student %d's name is: %s\n", sid, information.get(sid));

    }

    public void showAllStudent() {
        if (information.isEmpty()) {
            System.out.println("do not have any info!") ;
        }

        System.out.println("All student info:");
        for (int sid:information.keySet()) {
            System.out.printf("sid: %d, name: %s\n", sid, information.get(sid));
        }

        
    }

    public static void main(String[] args) {
        StudentInfo info = new StudentInfo();

        info.addStudent(1234, "name 1");
        info.addStudent(5678, "name 2");
        info.addStudent(1234, "name 6"); //重复添加
        info.addStudent(9999, "name 3"); 
        info.addStudent(6666, "name 4"); 

        info.removeStudent(123); // 删除不存在的学生
        info.removeStudent(1234); //正常删除

        info.showAllStudent();


    }


}
