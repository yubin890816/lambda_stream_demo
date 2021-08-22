package com.yubin.lambda.functionref;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 方法引用测试类
 *
 * @author yubin03
 * @create 2021-08-22
 */
public class MethodRefTest {

    /**
     * 静态方法引用:如果函数式接口的实现恰好可以通过调用一个静态方法来实现,那么就可以使用静态方法引用。
     */
    @Test
    public void testStaticMethodRef() {
        Supplier<String> supplier = Foo::staticMethod;
        System.out.println(supplier.get());
    }

    /**
     * 实例方法引用:如果函数式接口的实现恰好可以通过调用一个实例方法来实现,那么就可以使用实例方法引用。
     */
    @Test
    public void testInstanceMethodRef() {
        Foo foo = new Foo();
        Supplier<String> supplier = foo::instanceMethod;
        System.out.println(supplier.get());
    }

    /**
     * 对象方法引用案例演示
     *      抽象方法的第一个参数类型,刚好是实例方法的类型, 抽象方法的参数恰好可以当中实例方法的参数
     *      如果函数式接口的实例能由上面说的实例方法调用来实现的话, 那么就可以使用对象方法引用
     */
    @Test
    public void testObjMethodRef() {
        Foo foo1 = new Foo();
        System.out.println(foo1);
        Consumer<Foo> consumer1 = (foo) -> {new Foo().foo();};
        consumer1.accept(foo1);
        Consumer<Foo> consumer3 = Foo::foo;
        consumer3.accept(foo1);
        Consumer<Foo> consumer2 = (foo) -> {new Foo2().foo();};
        //Consumer<Foo> consumer4 = Foo2::foo;

    }

    /**
     * 构造方法引用: 如果函数式接口的实现恰好可以通过调用一个类的构造方法来实现，那么就可以使用构造方法引用
     */
    @Test
    public void testConstructMethodRef() {
        Supplier<Foo2> s1 = () -> new Foo2();
        Supplier<Foo2> s2 = Foo2::new;
        s1.get();
        s2.get();
        Consumer<String> c = Integer::new;
    }

    private static class Foo {

        public static String staticMethod() {
            return "function reference1";
        }

        public String instanceMethod() {
            return "function reference2";
        }

        public void foo() {
            System.out.println("foo：" + this);
        }
    }

    private class Foo2 {

        public void foo() {
            System.out.println("foo");
        }
    }
}
