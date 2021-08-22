package com.yubin.lambda.why1;

import com.yubin.lambda.entity.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 为什么使用Lambda测试类
 *
 * @author yubin03
 * @create 2021-08-19
 */
public class WhyUseLambda1 {

    @Test
    public void testWhyUseLambda() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("张三", 13, 63));
        list.add(new Student("李四", 18, 78));
        list.add(new Student("王五", 14, 70));
        list.add(new Student("小红帽", 16, 89));
        list.add(new Student("小绿", 19, 69));

        // 需求1: 查找年龄大于16的学生
        findByAge(list);
        System.out.println("=======================");
        // 需求2: 查找分数大于75的学生
        findByScore(list);
    }

    // 查找年龄大于16的学生: 不使用Lambda的写法
    private void findByAge(List<Student> list) {
        List<Student> students = new ArrayList<>();
        for (Student student : list) {
            if (student.getAge() > 16) {
                students.add(student);
            }
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }

    // 查找分数大于75的学生: 不使用Lambda的写法
    private void findByScore(List<Student> list) {
        List<Student> students = new ArrayList<>();
        for (Student student : list) {
            if (student.getScore() > 75) {
                students.add(student);
            }
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }
}
