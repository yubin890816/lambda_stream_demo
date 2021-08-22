# Stream API

# 1、Stream简介

Stream作为Java 8的一大亮点，是对集合（Collection）、数组对象功能的增强，它专注于对集合对象进行各种非常便利、高效的聚合操作，或者大批量数据操作。Stream API借助于同样新出现的Lambda表达式，极大的提高编程效率和程序可读性。同时它提供串行和并行两种模式进行汇聚操作，并发模式能够充分利用多核处理器的优势，使用fork/join并行方式来拆分任务和加速处理过程。通常编写并行代码很难且容易出错，但使用Stream API无需编写一行多线程的代码，就可以方便的写出高性能的并发程序。所以说，Java 8中首次出现的java.util.stream是一个函数式语言+多核时代综合影响的产物。

Stream是一组用来处理数组、集合的API

Java 8之所以费这么大功夫引入函数式编程，原因有二：

- 代码简洁：函数式编程写出的代码简洁且意图明确，使用Stream接口让你从此告别for循环。
- 多核友好：Java函数式编程使得编写并行程序从未如此简单，你需要的全部就是调用一个parallel()方法。

# 2、Stream特性

- **不是数据结构，没有内部存储**
- 不支持索引访问
- 延迟计算
- 支持并行
- 很容易生成数组或集合（List，Set）
- 支持过滤，查找，转换，汇总，聚合等操作

# 3、Stream运行机制

Stream分为源Source、中间操作、终止操作。

- 流的源可以是一个数组、一个集合、一个生成器方法、一个I/O通道等等。
- 一个流可以有零个或者多个中间操作，每一个中间操作都会返回一个新的流，供下一个操作使用。
- 一个流只会有一个终止操作。
- Stream只有遇到终止操作，它的源才开始执行遍历操作。

# 4、Stream的创建

- 通过数组

```java
Arrays.stream(T array)

Stream.of(T array)
```

```java
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
```



- 通过集合

```java
Collection.stream()
Collection.parallelStream():获取的是并行操作的流
```

```java
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
```



- 通过Stream.generate方法来创建

```java
/**
 * 方式三: 通过Stream.generate来生成
 */
@Test
public void testBuildStream3() {
    // Stream.generate : 返回无限顺序无序流，其中每个元素由提供的 Supplier 。 使用该方法的时候注意要结合limit来使用, 否则Stream中元素的个数是无限的
    Stream<Integer> generate = Stream.generate(() -> 1);
    generate.limit(10).forEach(System.out::println);
}
```



- 通过Stream.iterate方法来创建

```java
/**
 * 方式四: 通过Stream.iterate来生成
 */
@Test
public void testBuildStream4() {
    // Stream.iterate(T seed, UnaryOperator<T> f)  : 返回有序无限连续 Stream由函数的迭代应用产生 f至初始元素 seed ，产生 Stream包括 seed ， f(seed) ， f(f(seed)) ，等
    Stream<Integer> iterate = Stream.iterate(1, x -> x + 1);
    iterate.limit(10).forEach(System.out::println);
}
```



- 其它API创建

需要注意的是，对于基本数值型，目前有三种对应的包装类型 Stream：IntStream、LongStream、DoubleStream。当然我们也可以用 Stream<Integer>、Stream<Long> >、Stream<Double>，但是 boxing 和 unboxing 会很耗时，所以特别为这三种基本数值型提供了对应的Stream。
Java 8 中还没有提供其它数值型Stream，因为这将导致扩增的内容较多。而常规的数值型聚合运算可以通过上面三种 Stream 进行。

```java
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
```

# 5、Stream常用API

## 5.1、中间操作

​	一个流可以后面跟随零个或多个 intermediate 操作。其目的主要是打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用。这类操作都是惰性化的（lazy），就是说，仅仅调用到这类方法，并没有真正开始流的遍历。

### 过滤：filter

```java
@Test
public void testFilter() {
    Arrays.asList(1, 2, 3, 4, 5, 6).stream().filter(x -> {
        System.out.println("filter里面的方法被执行了");
        return x % 2 == 0;
    }).forEach(System.out::println);
}
```

### 去重：distinct

```java
// Stream中间操作distinct 去除Stream中的重复元素 使用的是Object.equals(Object)
@Test
public void testDistinct() {
    Arrays.asList(1, 2, 2, 4, 4, 6).stream().distinct().forEach(System.out::println);
}
```

### 排序：sorted

```java
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
```

### 合并流：concat

```java
// Stream中间操作concat 合并流
// concat(Stream<? extends T> a, Stream<? extends T> b)
// 创建一个懒惰连接的流，其元素是第一个流的所有元素，后跟第二个流的所有元素。
@Test
public void testConcat() {
    Stream.concat(Arrays.asList("a","b","c").stream(), Arrays.asList("d","e","f").stream()).forEach(System.out::println);
}
```

### 截取：limit、skip

```java
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
```

### 转换：map/flatMap/

```java
// Stream中间操作map 转换
// map(Function<? super T,? extends R> mapper)
@Test
public void testMap() {
    Arrays.asList("a","b","c").stream().map(x -> x.toUpperCase()).forEach(System.out::println);
}

// Stream中间操作flatMap 转换
// flatMap(Function<? super T,? extends Stream<? extends R>> mapper)
// flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List 了，都是直接的数字。
@Test
public void testFlatMap() {
    Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1, 2, 3), Arrays.asList(4), Arrays.asList(5, 6));
    Stream<Integer> stream = inputStream.flatMap(childList -> childList.stream());
    stream.forEach(System.out::println);
}
```

### 其它：peek

```java
// Stream中间操作peek 查看
@Test
public void testPeek() {
    List<User> list = Arrays.asList(new User("zhangsan", 1), new User("lisi", 2));
    List<User> collect = list.stream().peek(x -> x.setName("lili")).collect(Collectors.toList());
    collect.stream().forEach(System.out::println);
}
```

## 5.2、终止操作

​	一个流只能有一个 terminal 操作，当这个操作执行后，流就被使用“光”了，无法再被操作。所以这必定是流的最后一个操作。Terminal 操作的执行，才会真正开始流的遍历，并且会生成一个结果。

### 循环：forEach

### 计算：min、max、count、average、sum

```java
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
```

### 匹配：anyMatch、allMatch、noneMatch、findFirst、findAny

```java
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
```

### 汇聚：reduce

这个方法的主要作用是把 Stream 元素组合起来。它提供一个起始值（种子），然后依照运算规则（BinaryOperator），和前面 Stream 的第一个、第二个、第 n 个元素组合。从这个意义上说，字符串拼接、数值的 sum、min、max、average 都是特殊的 reduce。例如 Stream 的 sum 就相当于Integer sum = integers.reduce(0, (a, b) -> a+b); 或Integer sum = integers.reduce(0, Integer::sum);
也有没有起始值的情况，这时会把Stream的前面两个元素组合起来，返回的是 Optional。

```java
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
```

上面代码例如第一个示例的 reduce()，第一个参数（空白字符）即为起始值，第二个参数（String::concat）为 BinaryOperator。这类有起始值的 reduce() 都返回具体的对象。而对于第四个示例没有起始值的 reduce()，由于可能没有足够的元素，返回的是 Optional，请留意这个区别。

### 收集器：toArray、collect

```java
// Stream终止操作toArray、collect
@Test
public void testToArrayAndCollect() {
    List<Integer> collect = Arrays.asList(6, 2, 5, 1, 4, 3).stream().collect(Collectors.toList());
    System.out.println(collect);

    Object[] objects = Arrays.asList(6, 2, 5, 1, 4, 3).stream().toArray();
    Arrays.stream(objects).forEach(System.out::println);
}
```



​	在对于一个Stream进行多次转换操作 (Intermediate 操作)，每次都对Stream的每个元素进行转换，而且是执行多次，这样时间复杂度就是N（转换次数）个for循环里把所有操作都做掉的总和吗？其实不是这样的，转换操作都是lazy的，多个转换操作只会在 Terminal 操作的时候融合起来，一次循环完成。我们可以这样简单的理解，Stream 里有个操作函数的集合，每次转换操作就是把转换函数放入这个集合中，在 Terminal 操作的时候循环 Stream 对应的集合，然后对每个元素执行所有的函数。
还有一种操作被称为short-circuiting。用以指：
对于一个intermediate操作，如果它接受的是一个无限大（infinite/unbounded）的Stream，但返回一个有限的新Stream。
对于一个 terminal 操作，如果它接受的是一个无限大的 Stream，但能在有限的时间计算出结果。
当操作一个无限大的Stream，而又希望在有限时间内完成操作，则在管道内拥有一个 short-circuiting 操作是必要非充分条件。

