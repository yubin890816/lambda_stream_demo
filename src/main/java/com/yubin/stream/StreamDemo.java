package com.yubin.stream;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Stream案例演示
 *
 * @author yubin03
 * @create 2021-08-22
 */
public class StreamDemo {

    /**
     * 方式一: 通过数组生成
     */
    @Test
    public void testBuildStream1() {
        String[] strArr = {"a", "b", "c"};
        //static <T> Stream<T> of(T... values)  返回其元素是指定值的顺序排序流。
        Stream<String> stream1 = Stream.of(strArr);
        stream1.forEach(System.out::println);
        //static <T> Stream<T> of(T t) 返回包含单个元素的顺序 Stream 。
        Stream<String> stream2 = Stream.of("d");
        stream2.forEach(System.out::println);

        Stream<String> stream3 = Arrays.stream(strArr);
        stream3.forEach(System.out::println);
    }

    /**
     * 方式二: 通过集合来生成
     */
    @Test
    public void testBuildStream2() {
        List<String> list = Arrays.asList("a", "b", "c", "d");
        Stream<String> stream1 = list.stream();
        stream1.forEach(System.out::println);

        // parallelStream(): 获取的是并行操作的流
        Stream<String> stream2 = list.parallelStream();
        stream2.forEach(System.out::println);
    }

    /**
     * 方式三: 通过Stream.generate来生成
     */
    @Test
    public void testBuildStream3() {
        // Stream.generate : 返回无限顺序无序流，其中每个元素由提供的 Supplier 。 使用该方法的时候注意要结合limit来使用, 否则Stream中元素的个数是无限的
        Stream<Integer> generate = Stream.generate(() -> 1);
        generate.limit(10).forEach(System.out::println);
    }

    /**
     * 方式四: 通过Stream.iterate来生成
     */
    @Test
    public void testBuildStream4() {
        // Stream.iterate(T seed, UnaryOperator<T> f)  : 返回有序无限连续 Stream由函数的迭代应用产生 f至初始元素 seed ，产生 Stream包括 seed ， f(seed) ， f(f(seed)) ，等
        Stream<Integer> iterate = Stream.iterate(1, x -> x + 1);
        // 注意取值的顺序哦seed、f(seed)、f(f(seed))...
        iterate.limit(10).forEach(System.out::println);
    }

    /**
     * 方式五: 其它方式创建Stream
     */
    @Test
    public void testBuildStream5() {
        String str = "abcdef";
        IntStream stream = str.chars();
        //stream.forEach(System.out::println);
        stream.forEach(x -> System.out.println((char) x));
    }

    // Stream intermediate (中间操作案例演示)
    // Stream中间操作filter
    @Test
    public void testFilter() {
        Arrays.asList(1, 2, 3, 4, 5, 6).stream().filter(x -> {
            System.out.println("filter里面的方法被执行了");
            return x % 2 == 0;
        }).forEach(System.out::println);
    }

    // Stream中间操作distinct 去除Stream中的重复元素 使用的是Object.equals(Object)
    @Test
    public void testDistinct() {
        Arrays.asList(1, 2, 2, 4, 4, 6).stream().distinct().forEach(System.out::println);
    }

    // Stream中间操作sorted 对Stream中的元素进行排序
    // sorted(): 按照自然顺序排序
    // sorted(Comparator<? super T> comparator) : 按照指定的比较器进行排序
    @Test
    public void testSorted() {
        Arrays.asList(6, 2, 5, 1, 4, 3).stream().sorted().forEach(System.out::print);
        System.out.println();
        Arrays.asList(6, 2, 5, 1, 4, 3).stream().sorted((a, b) -> a - b).forEach(System.out::print);
        System.out.println();
        Arrays.asList(6, 2, 5, 1, 4, 3).stream().sorted(Comparator.comparingInt(a -> a)).forEach(System.out::print);
        System.out.println();
        Arrays.asList(6, 2, 5, 1, 4, 3).stream().sorted(Integer::compare).forEach(System.out::print);
    }

    // Stream中间操作concat 合并流
    // concat(Stream<? extends T> a, Stream<? extends T> b)
    // 创建一个懒惰连接的流，其元素是第一个流的所有元素，后跟第二个流的所有元素。
    @Test
    public void testConcat() {
        Stream.concat(Arrays.asList("a","b","c").stream(), Arrays.asList("d","e","f").stream()).forEach(System.out::println);
    }

    // Stream中间操作limit 截取
    // limit(long maxSize) : 返回由此流的元素组成的流，截短长度不能超过 maxSize 。
    @Test
    public void testLimit() {
        // 截取前2个原始
        Arrays.asList("a","b","c").stream().limit(2).forEach(System.out::println);
    }

    // Stream中间操作skip 截取
    // skip(long n) : 在丢弃流的第一个 n元素后，返回由该流的 n元素组成的流。
    @Test
    public void testSkip() {
        // 跳过第一个原始
        Arrays.asList("a","b","c").stream().skip(1).forEach(System.out::println);
    }

    // Stream中间操作map 转换
    // mapToDouble(将Stream中的每个元素转换成Double)
    // mapToInt(将Stream中的每个元素转换成Integer)
    // mapToLong(将Stream中的每个元素转换成Long)
    // map(Function<? super T,? extends R> mapper)
    @Test
    public void testMap() {
        Arrays.asList("a", "b", "c").stream().map(x -> x.toUpperCase()).forEach(System.out::println);

    }

    // Stream中间操作flatMap 转换
    // flatMapToDouble 返回的参数类型是Stream<Double>
    // flatMapToInt 返回的参数类型是Stream<Integer>
    // flatMapToLong 返回的参数类型是Stream<Long>
    // flatMap(Function<? super T,? extends Stream<? extends R>> mapper)
    // flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List 了，都是直接的数字。
    @Test
    public void testFlatMap() {
        Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1, 2, 3), Arrays.asList(4), Arrays.asList(5, 6));
        Stream<Integer> stream = inputStream.flatMap(childList -> childList.stream());
        stream.forEach(System.out::println);
    }

    // Stream中间操作peek 查看
    @Test
    public void testPeek() {
        List<User> list = Arrays.asList(new User("zhangsan", 1), new User("lisi", 2));
        List<User> collect = list.stream().peek(x -> x.setName("lili")).collect(Collectors.toList());
        collect.stream().forEach(System.out::println);
    }

    // Stream终止操作min 求Stream中的最小值
    // min(Comparator<? super T> comparator): 根据提供的 Comparator返回此流的最小元素。
    @Test
    public void testMin() {
        Optional<Integer> min = Arrays.asList(6, 2, 5, 1, 4, 3).stream().min((a, b) -> a - b);
        if (min.isPresent()) {
            System.out.println(min.get());
        }
    }

    // Stream终止操作max 求Stream中的最大值
    // max(Comparator<? super T> comparator) : 根据提供的 Comparator返回此流的最大元素。
    @Test
    public void testMax() {
        Optional<Integer> max = Arrays.asList(6, 2, 5, 1, 4, 3).stream().max((a, b) -> a - b);
        if (max.isPresent()) {
            System.out.println(max.get());
        }
    }

    // Stream终止操作count 求Stream中的元素个数
    // count() : 返回此流中的元素数。
    @Test
    public void testCount() {
        long count = Arrays.asList(6, 2, 5, 1, 4, 3).stream().count();
        System.out.println(count);
    }

    // IntStream终止操作average()  求Stream中的元素的算术平均值
    @Test
    public void testAverage() {
        OptionalDouble average = Arrays.asList("6", "2", "5", "1", "4", "3").stream().mapToInt(Integer::parseInt).average();
        if (average.isPresent()) {
            System.out.println(average.getAsDouble());
        }
    }

    // IntStream终止操作sum()  返回此流中元素的总和。
    @Test
    public void testSum() {
        int sum = Arrays.asList("6", "2", "5", "1", "4", "3").stream().mapToInt(Integer::parseInt).sum();
        System.out.println(sum);
    }

    // Stream终止操作allMatch
    // allMatch(Predicate<? super T> predicate) : 返回此流的所有元素是否与提供的谓词匹配（所有的元素都满足返回true, 否则返回false）。
    @Test
    public void testAllMatch() {
        boolean b = Arrays.asList(6, 2, 5, 1, 4, 3).stream().allMatch(x -> x > 0);
        System.out.println(b);
    }

    // Stream终止操作anyMatch
    // anyMatch(Predicate<? super T> predicate) : 返回此流的任何元素是否与提供的谓词匹配（任何一个元素符合条件则返回true, 否则返回false）。
    @Test
    public void testAnyMatch() {
        boolean b = Arrays.asList(6, 2, 5, 1, 4, 3).stream().anyMatch(x -> x > 5);
        System.out.println(b);
    }

    // Stream终止操作noneMatch
    // noneMatch(Predicate<? super T> predicate) : 返回此流的元素是否与提供的谓词匹配。 （所有元素都不匹配则返回true, 否则返回false）。
    @Test
    public void testNoneMatch() {
        boolean b = Arrays.asList(6, 2, 5, 1, 4, 3).stream().noneMatch(x -> x > 5);
        System.out.println(b);
    }

    // Stream终止操作findFirst
    // findFirst()  : 返回描述此流的第一个元素的Optional如果流为空，则返回一个空的Optional 。
    @Test
    public void testFindFirst() {
        Optional<Integer> first = Arrays.asList(6, 2, 5, 1, 4, 3).stream().findFirst();
        if (first.isPresent()) {
            System.out.println(first.get());
        }
    }

    // Stream终止操作findAny
    // findAny() : 返回描述流的一些元素的Optional如果流为空，则返回一个空的Optional 。
    @Test
    public void testFindAny() {
        Optional<Integer> any = Arrays.asList(6, 2, 5, 1, 4, 3).stream().findAny();
        if (any.isPresent()) {
            System.out.println(any.get());
        }
    }

    // Stream终止操作reduce
    @Test
    public void testReduce() {
        // 字符串连接，concat = "ABCD"
        String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        System.out.println(concat);
        // 求最小值，minValue = -3.0
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        System.out.println(minValue);
        // 求和，sumValue = 10, 有起始值
        int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        System.out.println(sumValue);
        // 求和，sumValue = 10, 无起始值
        sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        System.out.println(sumValue);
        // 过滤，字符串连接，concat = "ace"
        concat = Stream.of("a", "B", "c", "D", "e", "F").filter(x -> x.compareTo("Z") > 0).reduce("", String::concat);
        System.out.println(concat);
    }

    // Stream终止操作toArray、collect
    @Test
    public void testToArrayAndCollect() {
        List<Integer> collect = Arrays.asList(6, 2, 5, 1, 4, 3).stream().collect(Collectors.toList());
        System.out.println(collect);

        Object[] objects = Arrays.asList(6, 2, 5, 1, 4, 3).stream().toArray();
        Arrays.stream(objects).forEach(System.out::println);
    }

}
