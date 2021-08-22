package com.yubin.lambda.filter;

import com.yubin.lambda.entity.Student;

/**
 * Student过滤类
 *
 * @author yubin03
 * @create 2021-08-19
 */
public interface StudentFilter {

    public boolean match(Student student);
}
