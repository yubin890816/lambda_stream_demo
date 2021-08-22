package com.yubin.lambda.why4;

import com.yubin.lambda.entity.Student;
import com.yubin.lambda.filter.StudentFilter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 为什么使用Lambda测试类
 *
 * @author yubin03
 * @create 2021-08-19
 */
public class WhyUseLambda4 {

    @Test
    public void testWhyUseLambda() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("张三", 13, 63));
        list.add(new Student("李四", 18, 78));
        list.add(new Student("王五", 14, 70));
        list.add(new Student("小红帽", 16, 89));
        list.add(new Student("小绿", 19, 69));

        // 需求1: 查找年龄大于16的学生
        findByFilter(list, (student) -> student.getAge() > 16);
        System.out.println("=======================");
        // 需求2: 查找分数大于75的学生
        findByFilter(list, (student)-> student.getScore() > 75);
        System.out.println("=======================");
        // 需求3: 查找名字的长度大于2的学生
        findByFilter(list, (student) -> student.getName().length() > 2);
    }

    private void findByFilter(List<Student> list, StudentFilter filter) {
        List<Student> students = new ArrayList<>();
        for (Student student : list) {
            if (filter.match(student)) {
                students.add(student);
            }
        }
        printStudent(students);
    }

    private void printStudent(List<Student> list) {
        for (Student student : list) {
            System.out.println(student);
        }
    }
}
