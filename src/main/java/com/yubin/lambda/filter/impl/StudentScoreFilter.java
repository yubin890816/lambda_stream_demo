package com.yubin.lambda.filter.impl;

import com.yubin.lambda.entity.Student;
import com.yubin.lambda.filter.StudentFilter;

/**
 * 通过分数来过滤
 *
 * @author yubin03
 * @create 2021-08-19
 */
public class StudentScoreFilter implements StudentFilter {
    @Override
    public boolean match(Student student) {
        return student.getScore() > 75;
    }
}
