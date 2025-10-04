/*
 * 5.  集合工具类 
 * 编写一个程序读取学生成绩，并按成绩排序输出前5名学生信息。
 */


package day02;

import java.util.*;
import java.lang.StringBuilder;

class Student {
    String name;
    float grade;

    public Student(String name, float grade) {
        this.name = name;
        this.grade = grade;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: ");
        sb.append(name);
        sb.append(", ");
        sb.append("grade: ");
        sb.append(grade);

        return sb.toString();
    }
}

public class StudentGradeTop {
    ArrayList<Student> studentInfo;

    public StudentGradeTop() {
        studentInfo = new ArrayList<>();
    }

    public void addStudent(Student s) {

        if (s == null || s.getClass() != Student.class) {
            System.out.println("invaliad student!");
            return;
        }

        if (studentInfo.contains(s)) {
            System.out.println("The student is existing!");
            return;
        }

        studentInfo.add(s);
        // System.out.println("Student successly added: " + s.toString());

    }


    public void removeStudent(Student s) {

        if (s == null || s.getClass() != Student.class) {
            System.out.println("invaliad student input!");
            return;
        }

        if (studentInfo.contains(s)) {
            studentInfo.remove(s);
            System.out.println("The student has been removed: " + s.toString());
            return;
        }else {
            System.out.println("does not exist student: " + s.toString());

        }
        
    }


    public void showTopFive() {
        if (studentInfo.isEmpty()) {
            return;
        }

        List<Student> top = new ArrayList<>();
        /*
         * 起始位置是0，
         * 如果top列表中当前学生成绩大于候选学生成绩，则候选人插入位置往后一位
         * 直到候选学生成绩大于top列表中当前学生成绩；
         * 每次插入后，检查学生是否超过5个；
         */

        for (Student candidate : studentInfo) {
            int insertAt = 0;
            while (insertAt < top.size() && top.get(insertAt).grade >= candidate.grade) {
                insertAt++;
            }
            top.add(insertAt, candidate);
            if (top.size() > 5) {
                top.remove(5);
            }
        }

        for (Student s : top) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {

        Student s1 = new Student("n1", 79);
        Student s2 = new Student("n2", 59);
        Student s3 = new Student("n3", 63);
        Student s4 = new Student("n4", 68);
        Student s5 = new Student("n5", 82);

        Student s6 = new Student("n6", 99);
        Student s7 = new Student("n7", 88);
        Student s8 = new Student("n9", 66);
        Student s9 = new Student("n9", 70);
        Student s10 = new Student("n10", 69);

        StudentGradeTop stuTop = new StudentGradeTop();
        stuTop.addStudent(s1);
        stuTop.addStudent(s2);
        stuTop.addStudent(s3);
        stuTop.addStudent(s4);
        stuTop.addStudent(s5);

        stuTop.addStudent(s6);
        stuTop.addStudent(s7);
        stuTop.addStudent(s8);
        stuTop.addStudent(s9);
        stuTop.addStudent(s10);

        stuTop.showTopFive();


    }





}


