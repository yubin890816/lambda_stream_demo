# 前言

我们知道JDK8是一个比较大版本的更新，其给我们带来了很多新特性，其中有一个非常重要的知识点叫做lambda表达式。

# Lambda表达式的介绍

官方解释：Lambda表达式是Java8中最重要的新功能之一。使用Lambda表达式可以替代只有一个抽象函数的接口实现，告别匿名内部类，代码看起来更简洁易懂。Lambda表达式同时还提升了对集合、框架的迭代、遍历、过滤数据的操作。

官方文档地址：https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.27

## 案例1、使用Thread(Runnable runnable)创建一个线程

```java
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
}
```



## 案例2、对集合内的元素进行排序

```java
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

    // Lambda表达式的写法 如果{}中只有一行代码的话 {;}可以省略  o1, o2的参数类型自动推断
    Collections.sort(list, (o1, o2) -> o1.length() - o2.length());
    list.forEach(System.out::println);
}
```

## 为什么使用Lambda表达式

我们先来看一个案例

```java
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
            if (student.getAge().intValue() > 16) {
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
            if (student.getScore().intValue() > 75) {
                students.add(student);
            }
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }
}
```

上面的代码还是比较简单的，但是出现代码重复的现象，现在我们对其进行优化

```java
public interface StudentFilter {

    public boolean match(Student student);
}

public class StudentAgeFilter implements StudentFilter {
    @Override
    public boolean match(Student student) {
        return student.getAge() > 16;
    }
}

public class StudentScoreFilter implements StudentFilter {
    @Override
    public boolean match(Student student) {
        return student.getScore() > 75;
    }
}

public class WhyUseLambda2 {

    @Test
    public void testWhyUseLambda() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("张三", 13, 63));
        list.add(new Student("李四", 18, 78));
        list.add(new Student("王五", 14, 70));
        list.add(new Student("小红帽", 16, 89));
        list.add(new Student("小绿", 19, 69));

        // 需求1: 查找年龄大于16的学生
        findByFilter(list, new StudentAgeFilter());
        System.out.println("=======================");
        // 需求2: 查找分数大于75的学生
        findByFilter(list, new StudentScoreFilter());
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
```

通过这个案例的代码看起来已经没有重复的代码了，但是还是存在一个问题，比如我现在想以名字的长度来过滤的话，就需要再写一个Filter类，这时我们看到这个StudentFilter接口的实现类只被使用一次，因此我们首先想到的就是直接使用匿名内部类就行了

```java
public class WhyUseLambda3 {

    @Test
    public void testWhyUseLambda() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("张三", 13, 63));
        list.add(new Student("李四", 18, 78));
        list.add(new Student("王五", 14, 70));
        list.add(new Student("小红帽", 16, 89));
        list.add(new Student("小绿", 19, 69));

        // 需求1: 查找年龄大于16的学生
        findByFilter(list, new StudentFilter() {
            @Override
            public boolean match(Student student) {
                return student.getAge() > 16;
            }
        });
        System.out.println("=======================");
        // 需求2: 查找分数大于75的学生
        findByFilter(list, new StudentFilter() {
            @Override
            public boolean match(Student student) {
                return student.getScore() > 75;
            }
        });
        System.out.println("=======================");
        // 需求3: 查找名字的长度大于2的学生
        findByFilter(list, new StudentFilter() {
            @Override
            public boolean match(Student student) {
                return student.getName().length() > 2;
            }
        });
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
```

到此我们再思考一下这些代码又臭又长的，能不能优化一下呢？这时我们今天的主角就可以登场了。

```java
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
```





## Lambda表达式的特点

- 函数式编程
- 参数类型自动推断
- 代码量少，简洁

## Lambda表达式的应用场景

任何有**函数式接口**的地方



# Lambda表达式的写法

## Lambda表达式的格式

LambdaParameters -> LambdaBody

## 表达式说明

args -> expr 获取(Object ... args) -> {函数式接口抽象方法实现逻辑}

- ()参数个数，根据函数式接口里面抽象的参数个数来决定，当参数只有一个的时候()可以省略。
- 当expr逻辑非常简单，只有一行代码的时候，{}和return可以省略。


- -> ：Lambda Operator（Lambda操作符）

## 表达式案例

() -> {}

() -> {System.out.println(1);}

() -> System.out.println(1)

() -> {return 100;}

() -> 100

() -> null

(int x) -> {return x+1;}

(int x) -> x+1

(x) -> x+1

x -> x+1

```java
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

    Callable<String> callable3 = () -> "callable3...";
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
```



## JDK中定义的函数式接口

Supplier：代表一个输出

Consumer：代表一个输入

BiConsumer：代表两个输入

Function：代表一个输入，一个输出（一般输入和输出是不同类型的）

UnaryOperator：代表一个输入，一个输出（输入和输出是相同类型的）

BiFunction：代表两个输入，一个输出（一般输入和输出是不同类型的）

BinaryOperator：代表两个输入，一个输出（输入和输出是相同类型的）



# Lambda表达式的使用







# 函数式接口

我们知道C语言是面向过程编程，Java是面向对象编程，除此之外像Python、Scala语言等它们几乎都是函数式编程的一种语言。

在java里面方法是不能作为我们的形式参数的，java里面要求方法参数传递必须是值传递。但是在某些方法里面可以将函数作为参数传递进去，这种方式就叫做函数式编程。



## 函数式接口的定义

只有一个抽象方法（Object类中的方法除外）的接口叫做函数式接口。

示例：

```java
@FunctionalInterface
public interface InterfaceTest {

    public void add();

    //public void sub();
}
```

在这里有一个@FunctionInterface注解，如果你将`public void sub()`方法的注释放开的话，这个注释就会报错。



# Lambda表达式的原理





# 方法的引用

## 方法引用的定义

方法引用是用来直接访问类或者实例的已经存在的方法或者构造方法，方法引用提供了一种引用而不执行方法的方式，如果抽象方法的实现恰好可以使用调用另外一个方法来实现，就有可能可以使用方法的引用。

方法引用也属于Lambda表达式。

## 方法引用的分类

| 类型     | 语法               | 对应的Lambda表达式                            |
| ------ | ---------------- | --------------------------------------- |
| 静态方法引用 | 类名::staticMethod | (args) -> 类名.staticMethod(args)         |
| 实例方法引用 | inst::instMethod | (args) -> inst.instMethod(args)         |
| 对象方法引用 | 类名::instMethod   | (inst,args) -> 类名.instMethod(inst,args) |
| 构造方法引用 | 类名::new          | (args) -> new 类名(args)                  |

- 静态方法引用：如果函数式接口的实现恰好可以通过**调用一个静态方法**来实现，那么就可以使用静态方法引用。
- 实例方法引用：如果函数式接口的实现恰好可以通过调用一个实例的方法来实现，那么就可以使用实例方法引用。
- 对象方法引用：抽象方法的第一个参数类型刚好是实例方法的类型，抽象方法剩余的参数恰好可以当作实例方法的参数。如果函数式接口的实例能由上面说的实例方法调用来实现的话，那么就可以使用对象方法引用。
- 构造方法引用：如果函数式接口的实现恰好可以通过调用一个类的构造方法来实现，那么就可以使用构造方法引用。

## 案例演示

```java
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
```

