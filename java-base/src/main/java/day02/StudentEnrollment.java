/*
   3. Set 接口
 * 开发一个应用程序来管理学生的注册课程，确保每个学生的选课记录中不会有重复课程。
 */

package day02;

import java.util.*;

public class StudentEnrollment {
    private HashMap<Integer, HashSet<String>> enrollment;

    public StudentEnrollment() {
        this.enrollment = new HashMap<>();
    }

    // add course to a student 
    public void addCourse(int sid, String coursename) {

        if (coursename == null) return;
        if (!enrollment.containsKey(sid)) {
            enrollment.put(sid, new HashSet<>());
            System.out.printf("New student %d enrolment has been created!\n", sid);
        }

        if (enrollment.get(sid).contains(coursename)) {
            System.out.printf("Course %s has been added by student %d in the past!\n", coursename, sid);
            return;
        }

        enrollment.get(sid).add(coursename);
        System.out.printf("Course %s has been enrolled by %d\n", coursename, sid);

    }

    // remove a course 
    public void removeCourse(int sid, String coursename) {

        if (coursename == null) return;
        if (!enrollment.containsKey(sid)) {
            System.out.printf("student %d does not exist\n", sid);
            return;
        }

        if (!enrollment.get(sid).contains(coursename)) {
            System.out.printf("student %d's course %s does not exist\n", sid, coursename);
            return;
        }

        enrollment.get(sid).remove(coursename);
        System.out.printf("Course %s has been removed by %d\n", coursename, sid);

    }


    // get student's enrollment 
    public void getEnrol(int sid) {

        if (!enrollment.containsKey(sid)) {
            System.out.printf("student %d enrolment does not exist\n", sid);
            return;
        }

        System.out.printf("Student %d has enrolled following courses: %s\n", sid, enrollment.get(sid).toString());

    }

    public static void main(String[] args) {

        StudentEnrollment enrolSystem = new StudentEnrollment();

        enrolSystem.addCourse(3333, "course A"); // 正常添加
        enrolSystem.addCourse(3333, "course E");
        enrolSystem.addCourse(3333, "course G");
        enrolSystem.addCourse(3333, "course A"); // 重复添加
        enrolSystem.removeCourse(3333, "course W"); //删除不存在的课程
        enrolSystem.removeCourse(3333, "course E"); // 正常删除
        enrolSystem.getEnrol(3333);


    }


}
