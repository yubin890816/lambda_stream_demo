package com.yubin.lambda.filter.impl;

import com.yubin.lambda.entity.Student;
import com.yubin.lambda.filter.StudentFilter;

/**
 * 通过年龄来过滤
 *
 * @author yubin03
 * @create 2021-08-19
 */
public class StudentAgeFilter implements StudentFilter {
    @Override
    public boolean match(Student student) {
        return student.getAge() > 16;
    }
}
