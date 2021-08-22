package com.yubin.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * 第一个Lambda表达式案例
 *
 * @author yubin03
 * @create 2021-08-19
 */
public class LambdaDemo {

    // 使用Thread(Runnable runnable)创建一个线程
    @Test
    public void testCreateThread() {
        // JDK8之前的写法
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("running1......");
            }
        }).start();

        // 使用Lambda表达式的形式
        new Thread(() -> {System.out.println("running2......");}).start();

        // 使用Lambda表达式的形式 如果{}中只有一行代码可以 省略 {;}
        new Thread(() -> System.out.println("running3......")).start();
    }

    // 对集合内的元素进行排序
    @Test
    public void testSortList() {
        // 定义一个集合
        List<String> list = Arrays.asList("java", "C", "python", "scala");

        // JDL8之前的写法
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });
        for (String s : list) {
            System.out.println(s);
        }
        System.out.println("==========================");

        // Lambda表达式的写法 如果{}中只有一行代码的话 {;}可以省略   o1, o2的参数类型自动推断
        Collections.sort(list, (o1, o2) -> o1.length() - o2.length());
        list.forEach(System.out::println);
    }

    @Test
    public void testUseLambda() throws Exception {
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("runnable1...");
            }
        };
        runnable1.run();

        Runnable runnable2 = () -> {
            System.out.println("runnable2...");
        };
        runnable2.run();

        Runnable runnable3 = () -> System.out.println("runnable3...");
        runnable3.run();

        System.out.println("========================================");

        Callable<String> callable1 = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "callable1...";
            }
        };
        System.out.println(callable1.call());

        Callable<String> callable2 = () -> {
            return "callable2...";
        };
        System.out.println(callable2.call());

        Callable<String> callable3 = () -> "callable2...";
        System.out.println(callable3.call());

        System.out.println("========================================");

        Function<Integer, String> function1 = new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return "function" + integer;
            }
        };
        System.out.println(function1.apply(1));

        Function<Integer, String> function2 = (Integer integer) -> {
            return "function" + integer;
        };
        System.out.println(function2.apply(2));

        Function<Integer, String> function3 = (integer) -> {
            return "function" + integer;
        };
        System.out.println(function3.apply(3));

        Function<Integer, String> function4 = (integer) -> {
            return "function" + integer;
        };
        System.out.println(function4.apply(4));

        Function<Integer, String> function5 = integer -> {
            return "function" + integer;
        };
        System.out.println(function5.apply(5));

        Function<Integer, String> function6 = integer -> "function" + integer;
        System.out.println(function6.apply(6));
    }
}
