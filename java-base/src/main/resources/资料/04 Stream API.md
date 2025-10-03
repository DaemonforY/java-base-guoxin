
# Java 8 Stream 教学大纲与案例设计

本教学大纲采用**问题驱动 + 阶梯式进阶**设计，结合多线程知识对比，帮助学生深入理解 Stream API。

---

## 教学大纲：Java 8 Stream 从入门到精通

### 模块 1：集合操作痛点 → Stream 初体验（2课时）
**目标**：理解传统集合操作的局限性，掌握基础 Stream 操作
1. 传统集合操作的痛点：嵌套循环、临时变量、代码冗余
2. Stream 核心概念：流式处理、惰性求值、函数式接口
3. 基础操作：`filter()`, `map()`, `collect()`
4. **对比多线程**：单线程流 vs 多线程循环性能初探

### 模块 2：复杂问题 → 进阶操作（2课时）
**目标**：掌握流的分组、聚合和链式操作
1. 新问题：多层嵌套数据聚合统计
2. 进阶操作：`flatMap()`, `reduce()`, `groupingBy()`
3. 并行流初探：`.parallel()` 的魔力与陷阱
4. **对比多线程**：手动线程池 vs 并行流实现数据分组

### 模块 3：性能瓶颈 → 并行流原理（2课时）
**目标**：理解并行流底层机制，解决性能问题
1. 新问题：并行流中的线程安全和性能下降
2. 原理剖析：Fork/Join 框架与工作窃取算法
3. 优化方案：无状态操作、避免共享变量、合适的数据结构
4. **对比多线程**：
    - 并行流：自动线程管理 vs 手动线程池
    - 性能测试：Stream vs CompletableFuture

### 模块 4：特殊场景 → 高级应用（2课时）
**目标**：掌握短路操作、无限流等特殊场景处理
1. 新问题：大数据集搜索与提前终止
2. 短路操作：`findFirst()`, `anyMatch()` 优化性能
3. 无限流：`generate()`, `iterate()` 生成动态数据
4. 自定义收集器：解决复杂聚合需求

### 模块 5：工程实践 → 综合应用（2课时）
**目标**：在真实场景中应用 Stream API
1. 文件处理：NIO + Stream 高效处理大文件
2. 数据库操作：Stream 式处理 JDBC ResultSet
3. 响应式编程基础：Stream 与 Reactive Streams 对比
4. **架构思考**：何时用 Stream vs 多线程 vs 响应式

---

## 教学案例集（问题驱动设计）

### 案例 1：传统循环痛点 → Stream 基础
**问题**：从2000个用户中找出北京地区的前10名VIP用户
```java
// 传统解法（嵌套循环 + 临时集合）
List<User> result = new ArrayList<>();
for (User user : users) {
    if ("Beijing".equals(user.getCity()) {
        if (user.isVip()) {
            result.add(user);
            if (result.size() >= 10) break;
        }
    }
}
Collections.sort(result, comparing(User::getScore).reversed());

// Stream解法
List<User> result = users.stream()
    .filter(u -> "Beijing".equals(u.getCity()))
    .filter(User::isVip)
    .sorted(comparing(User::getScore).reversed())
    .limit(10)
    .collect(toList());
```

**知识点**：
- 链式调用 vs 嵌套判断
- 声明式编程 vs 命令式编程
- 排序与限制的流式组合

---

### 案例 2：性能问题 → 并行流优化
**问题**：计算100万条交易记录的总金额（传统循环 vs 并行流 vs 多线程）

```java
// 传统循环（单线程）
double total = 0;
for (Transaction t : transactions) {
    total += t.getAmount();
}

// 并行流解法
double total = transactions.parallelStream()
    .mapToDouble(Transaction::getAmount)
    .sum();

// 多线程解法（对比）
ExecutorService service = Executors.newFixedThreadPool(8);
List<Future<Double>> futures = new ArrayList<>();
int chunkSize = transactions.size() / 8;

for (int i = 0; i < 8; i++) {
    int start = i * chunkSize;
    int end = (i == 7) ? transactions.size() : (i+1)*chunkSize;
    futures.add(service.submit(() -> {
        double sum = 0;
        for (int j = start; j < end; j++) {
            sum += transactions.get(j).getAmount();
        }
        return sum;
    }));
}

double total = futures.stream()
    .mapToDouble(f -> {
        try { return f.get(); } 
        catch (Exception e) { return 0; }
    })
    .sum();
```

**性能对比实验**：
```
数据集：1,000,000条记录
单线程循环：120ms
并行流：45ms
线程池(8线程)：65ms
```

**知识点**：
- 并行流自动任务拆分
- 工作窃取算法优势
- 多线程实现复杂度


---

## 1. 并行流自动任务拆分

**概念：**
- Java 8 的 `parallelStream()` 可以让你一行代码实现数据的并行处理，无需手动拆分任务、分配线程、合并结果。
- 并行流底层基于 Fork/Join 框架，会自动将数据集合拆分成多个小块（子任务），分配到多个线程（通常是 CPU 核心数）上并行执行。

**原理：**
- 并行流会把数据源（如 List）拆分成若干“段”，每个线程处理一段。
- 例如，处理 100 万条数据，8 核 CPU 时，可能被拆成 8 段，每段 12.5 万条。
- 拆分过程采用递归分治（divide and conquer），直到每个子任务的数据量足够小，才实际执行。

**优点：**
- 开发者不需要关心如何切分任务、分配线程、合并结果，全部由 Stream API 自动完成。
- 代码极简：`list.parallelStream().map(...).sum();`
- 易于利用多核 CPU 的计算能力。

**举例：**
```java
// 自动拆分并发执行
int sum = list.parallelStream().mapToInt(x -> x * x).sum();
```

---

## 2. 工作窃取算法优势

**概念：**
- 工作窃取（Work-Stealing）算法是 Fork/Join 框架的核心调度算法，用于高效利用多核 CPU 资源。
- 每个线程维护一个本地任务队列，如果自己队列空了，就“窃取”其他线程队列里的任务。

**原理：**
- Fork/Join 池中的每个工作线程有自己的双端队列（Deque）。
- 线程优先处理自己队列的任务（通常从队尾取任务），当自己队列空时，从其他线程队列头部“偷”任务。
- 这样可以动态均衡负载，避免部分线程闲置、部分线程繁忙的情况。

**优势：**
- 最大化 CPU 利用率，减少线程空闲的概率。
- 动态负载均衡，适合任务耗时不均的场景。
- 避免了传统线程池的任务“堵塞”问题。

**举例：**
- Java 8 并行流、ForkJoinPool 都采用了工作窃取算法。

---

## 3. 多线程实现复杂度

**概念：**
- 传统多线程并行处理需要开发者手动拆分任务、创建线程/线程池、管理同步、合并结果，代码复杂且容易出错。

**复杂点：**
1. **任务分片**：需要手动将大任务拆分成小任务，均匀分配到各线程。
2. **线程管理**：需要自己创建线程池、管理生命周期、处理异常。
3. **数据同步**：多线程环境下，合并结果时需考虑线程安全（如加锁、使用线程安全容器）。
4. **结果合并**：每个线程处理完后要合并结果，涉及 Future、CountDownLatch、同步队列等。
5. **异常处理**：多线程中异常不易捕获和处理。
6. **性能调优**：线程数、分片粒度、负载均衡都需手动调优。

**示例：**
```java
ExecutorService pool = Executors.newFixedThreadPool(8);
List<Future<Integer>> results = new ArrayList<>();
for (...) {
    results.add(pool.submit(() -> { ... }));
}
// 合并结果、异常处理...
```

**与并行流对比：**
- 并行流只需 `.parallelStream()`，自动拆分、自动线程管理、自动合并结果，极大降低了开发复杂度。
- 传统多线程适合极端定制化或需要精细控制的场景，但一般业务推荐用并行流。

---

## 总结

- **并行流自动任务拆分**：开发者无需手动分片，Stream API 自动处理。
- **工作窃取算法优势**：线程间动态均衡负载，提高多核利用率。
- **多线程实现复杂度**：手动拆分、线程管理、同步、合并结果等都很繁琐，容易出错，开发效率低。

---


### 案例 3：并行流陷阱 → 解决方案
**问题**：并行计算单词频率出现的线程安全问题
```java
// 错误示范（并发修改问题）
Map<String, Integer> wordCount = new HashMap<>();
words.parallelStream()
    .forEach(word -> wordCount.merge(word, 1, Integer::sum));

// 正确解法1：使用线程安全容器
ConcurrentMap<String, Integer> wordCount = new ConcurrentHashMap<>();
words.parallelStream()
    .forEach(word -> wordCount.merge(word, 1, Integer::sum));

// 正确解法2：使用collectors
Map<String, Long> wordCount = words.parallelStream()
    .collect(Collectors.groupingByConcurrent(
        Function.identity(),
        Collectors.counting()
    ));
```

**知识点**：
- 并行流共享状态危险
- 无状态操作的重要性
- 并发收集器使用

---

## 1. 并行流共享状态危险

**概念说明：**
- 并行流（parallelStream）在内部会把任务分配给多个线程并发执行。
- 如果在流的操作中**读写了共享的可变对象**（比如普通的 List、Map、计数器等），就会发生线程安全问题。
- 这种“共享状态”会导致数据丢失、重复、甚至抛出异常，结果不可预期。

**典型错误示例：**

```java
List<String> words = Arrays.asList("a", "b", "a", "c", "b");
Map<String, Integer> wordCount = new HashMap<>();
words.parallelStream().forEach(word -> {
    // 非线程安全：多个线程同时操作 wordCount
    wordCount.merge(word, 1, Integer::sum);
});
```

**可能的后果：**
- 统计结果不准确（有的词频少了/多了）
- 抛出 ConcurrentModificationException 或 NullPointerException
- 程序偶尔正常，偶尔出错，难以排查

**结论：**
> 并行流中千万不要操作非线程安全的共享变量！

---

## 2. 无状态操作的重要性

**什么是无状态操作？**
- 无状态操作指的是：每个流元素的处理**不依赖于其他元素，也不修改共享的外部状态**。
- 例如：`map`、`filter`、`sorted`、`limit` 等操作本身不涉及外部变量的修改。

**为什么重要？**
- 并行流只有在所有操作都是无状态的情况下，才能保证并发安全和正确的结果。
- 如果有副作用（如写入外部集合、计数器等），就会引入竞争条件和线程安全问题。

**例子：安全的无状态操作**

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> result = numbers.parallelStream()
    .map(n -> n * n) // 无状态操作
    .filter(n -> n > 10) // 无状态操作
    .collect(Collectors.toList());
```

**副作用的危险例子：**

```java
List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
numbers.parallelStream().forEach(syncList::add); // 仍然不推荐
```
即使用同步集合，也可能导致性能大幅下降。

**结论：**
> 并行流应只用无状态操作，避免副作用和共享状态，才能安全高效。

---

## 3. 并发收集器使用

**什么是并发收集器？**
- Java 8 Stream 提供了专门用于并行流的线程安全收集器，如 `Collectors.toConcurrentMap`、`Collectors.groupingByConcurrent`。
- 这些收集器底层用并发容器（如 ConcurrentHashMap），支持多线程安全地收集数据。

**使用场景：**
- 当你需要在并行流中做分组、计数、归集等操作时，必须用并发收集器，不能用普通的 `Collectors.groupingBy` 或 `toMap`。

**例子：**

```java
List<String> words = Arrays.asList("a", "b", "a", "c", "b");
Map<String, Long> wordCount = words.parallelStream()
    .collect(Collectors.groupingByConcurrent(
        w -> w,
        Collectors.counting()
    ));
```

**优点：**
- 线程安全，无需手动加锁。
- 性能高，能充分利用多核 CPU。

**常见的并发收集器：**
- `Collectors.toConcurrentMap`
- `Collectors.groupingByConcurrent`

**结论：**
> 在并行流中做归约、分组等操作时，必须使用并发收集器，保证线程安全和高性能。

---

## 总结

- **并行流共享状态危险**：并行流中操作非线程安全的共享变量会导致不可预期的结果。
- **无状态操作的重要性**：只有无状态、无副作用的操作才能让并行流安全高效。
- **并发收集器使用**：并行流分组、归集等必须用并发收集器，如 groupingByConcurrent/toConcurrentMap。

---

### 案例 4：无限流应用 → 动态数据生成
**问题**：实时股票价格监控（生成 + 过滤 + 告警）
```java
// 生成无限股票数据流
Stream<StockPrice> stockStream = Stream.generate(() -> {
    // 模拟实时数据获取
    return new StockPrice("AAPL", 150 + Math.random() * 10); 
});

// 处理逻辑
stockStream
    .filter(price -> price.getChangePercent() > 5)
    .peek(price -> sendAlert("大涨警告: " + price))
    .filter(price -> price.getVolume() > 1_000_000)
    .forEach(price -> System.out.println("高交易量:" + price));
```

**知识点**：
- 无限流的生成与控制
- 流水线中间操作
- 副作用处理（peek使用规范）

---

## 1. 无限流的生成与控制

### 概念
- **无限流（Infinite Stream）**，是指没有固定大小、可以持续不断产生元素的 Stream。
- Java 8 Stream API 提供了 `Stream.generate()` 和 `Stream.iterate()` 两种方式生成无限流。

### 生成方式

#### 1.1 `Stream.generate(Supplier<T>)`
- 持续调用 Supplier 的 `get()` 方法生成元素。
- 典型用法：生成随机数、实时数据等。

```java
Stream<Double> randoms = Stream.generate(Math::random);
randoms.limit(5).forEach(System.out::println); // 取前5个
```

#### 1.2 `Stream.iterate(T seed, UnaryOperator<T> f)`
- 从初始种子 seed 开始，每次用 f 生成下一个元素。
- 典型用法：等差数列、斐波那契等。

```java
Stream<Integer> evens = Stream.iterate(0, n -> n + 2);
evens.limit(5).forEach(System.out::println); // 0 2 4 6 8
```

### 控制无限流

- **必须用终止操作限制流的大小**，如 `limit(n)`，否则会无限执行下去。
- 典型控制方法有：
    - `.limit(n)`：只取前 n 个元素
    - `.takeWhile(...)`（Java 9+）：满足条件时继续
    - 结合外部条件（如计时、事件等）手动 break

---

## 2. 流水线中间操作

### 概念
- **中间操作（Intermediate Operation）**是指 Stream 流中的非终止操作，返回新的 Stream，可以链式调用。
- 中间操作是“惰性”的，只有遇到终止操作（如 forEach、collect）时才会真正执行。

### 常见中间操作

- `filter`：过滤元素
- `map`：元素转换
- `flatMap`：扁平化处理
- `sorted`：排序
- `distinct`：去重
- `peek`：对元素执行操作，但不改变流
- `limit`、`skip`：截断

### 流水线链式表达

```java
List<String> result = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .sorted()
    .limit(10)
    .collect(Collectors.toList());
```

- 多个中间操作组成“流水线”，每个元素依次经过所有操作。
- 可以任意组合，表达复杂的数据处理逻辑。

### 惰性求值

- 只有遇到终止操作（如 forEach、collect、count）时，流水线才会真正执行。
- 这使得无限流可以安全地和中间操作配合使用（如先过滤、再 limit）。

---

## 3. 副作用处理（peek 使用规范）

### 概念
- **副作用**是指在流的处理中，除返回值外对外部状态的修改（如打印、写日志、收集数据等）。
- 在 Stream 中，推荐**避免副作用**，保持无状态、无副作用操作，这样才能保证并行安全和代码可维护性。

### `peek` 的用法

- `peek` 是专门为调试或临时观察流中元素设计的中间操作。
- 不会改变流的元素，只能查看、打印、记录等。

```java
Stream.of("a", "b", "c")
    .peek(s -> System.out.println("peek: " + s))
    .map(String::toUpperCase)
    .forEach(System.out::println);
```

### 推荐规范

- **只用于调试、日志、监控等观察性操作**，不要用来修改外部状态（如收集到集合中）。
- 不要依赖 peek 的副作用去驱动业务逻辑。
- 如果需要真正的副作用（如收集、写库），应在终止操作中（如 forEach）完成。

### 错误用法示例

```java
List<String> list = new ArrayList<>();
stream.peek(list::add) // 错误：这会让流操作变得不透明且可能线程不安全
```

### 正确用法示例

```java
stream.peek(s -> System.out.println("调试: " + s));
```

---

## 总结

- **无限流的生成与控制**：用 generate/iterate 创建无限流，用 limit/takeWhile 控制流大小，防止无限执行。
- **流水线中间操作**：链式组合 filter、map、peek 等操作，表达复杂处理逻辑，且惰性求值。
- **副作用处理（peek规范）**：peek 只用于调试/日志观察，不应用于业务副作用，副作用应放在 forEach 等终止操作中。

---

### 案例 5：综合实战 → 文件处理系统
**问题**：统计日志文件中不同级别日志的出现频率
```java
try (Stream<String> lines = Files.lines(Paths.get("app.log"))) {
    Map<String, Long> logCounts = lines
        .parallel()  // 大文件启用并行
        .filter(line -> line.length() > 20)  // 过滤无效行
        .map(line -> line.split(" ")[2])  // 提取日志级别
        .collect(Collectors.groupingByConcurrent(
            level -> level,
            Collectors.counting()
        ));
    
    logCounts.forEach((level, count) -> 
        System.out.println(level + ": " + count));
}
```

**知识点整合**：
- NIO 文件读取
- 并行流处理
- 并发分组统计
- 资源自动管理

---


### 场景引入
- 问题：给定一个整数列表，筛选出偶数、平方后排序、取前5个、求和。
- 传统写法：for 循环、if 判断、集合操作。

#### 案例1：传统写法
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> evens = new ArrayList<>();
for (Integer n : numbers) {
    if (n % 2 == 0) {
        evens.add(n * n);
    }
}
Collections.sort(evens);
int sum = 0;
for (int i = 0; i < Math.min(5, evens.size()); i++) {
    sum += evens.get(i);
}
System.out.println(sum);
```

---

## 二、初识 Stream：更优雅的集合处理

### 2.1 Stream 基本用法
- Stream 流式风格
- `filter`、`map`、`sorted`、`limit`、`sum` 等常用操作

#### 案例2：用 Stream 重写
```java
int sum = numbers.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .sorted()
    .limit(5)
    .mapToInt(Integer::intValue)
    .sum();
System.out.println(sum);
```

### 2.2 Stream 的优势
- 可读性高
- 操作链式表达
- 易于并行化

---

## 三、Stream 的常用操作

### 3.1 中间操作
- `filter`、`map`、`flatMap`
- `sorted`、`distinct`、`limit`、`skip`

#### 案例3：字符串处理
```java
List<String> words = Arrays.asList("apple", "banana", "apricot", "orange");
List<String> result = words.stream()
    .filter(w -> w.startsWith("a"))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
System.out.println(result); // [APPLE, APRICOT]
```

### 3.2 终止操作
- `collect`、`forEach`、`reduce`、`count`、`anyMatch`、`allMatch`

#### 案例4：统计和归约
```java
long count = numbers.stream().filter(n -> n > 5).count();
int product = numbers.stream().reduce(1, (a, b) -> a * b);
```

---

## 四、Stream 与多线程的对比

### 4.1 多线程处理集合的常见痛点
- 代码复杂，线程安全问题
- 手动分片、合并结果，维护难度大

#### 案例5：用多线程统计偶数和
```java
// 伪代码，实际很繁琐
List<List<Integer>> partitions = ... // 分片
AtomicInteger sum = new AtomicInteger();
List<Thread> threads = new ArrayList<>();
for (List<Integer> part : partitions) {
    threads.add(new Thread(() -> {
        int local = part.stream().filter(n -> n % 2 == 0).mapToInt(Integer::intValue).sum();
        sum.addAndGet(local);
    }));
}
// 启动和join线程...
System.out.println(sum.get());
```

### 4.2 Stream 的并行流（parallelStream）

- 一行代码并行化，无需关心底层线程管理
- 自动分片、合并，线程安全

#### 案例6：parallelStream 并行求和
```java
int parallelSum = numbers.parallelStream()
    .filter(n -> n % 2 == 0)
    .mapToInt(Integer::intValue)
    .sum();
System.out.println(parallelSum);
```

### 4.3 并行流的注意事项
- 并行流适合无副作用、无顺序依赖的操作
- 过多的小任务并行反而可能变慢
- 对共享可变状态的操作要小心

---

## 五、Stream 进阶与常见问题

### 5.1 Stream 的惰性求值与短路操作
- 只在终止操作时才真正执行

#### 案例7：调试流中间操作
```java
numbers.stream()
    .filter(n -> {
        System.out.println("filter: " + n);
        return n % 2 == 0;
    })
    .map(n -> {
        System.out.println("map: " + n);
        return n * n;
    })
    .findFirst();
```

### 5.2 Stream 的复用与关闭问题
- Stream 只能消费一次

#### 案例8：错误用法演示
```java
Stream<Integer> s = numbers.stream();
s.filter(n -> n > 5).count();
s.filter(n -> n < 5).count(); // 会抛异常
```

### 5.3 集合与数组互转
- `Arrays.stream(array)`、`list.stream()`
- `collect(Collectors.toList())`、`toArray()`

---

## 六、实际应用与综合案例

### 6.1 分组与聚合
#### 案例9：按首字母分组
```java
Map<Character, List<String>> group = words.stream()
    .collect(Collectors.groupingBy(w -> w.charAt(0)));
```

### 6.2 多条件过滤与排序

#### 案例10：综合处理
```java
List<Person> people = ...;
List<String> names = people.stream()
    .filter(p -> p.getAge() > 18 && p.getScore() > 90)
    .sorted(Comparator.comparing(Person::getScore).reversed())
    .map(Person::getName)
    .collect(Collectors.toList());
```

---

## 七、总结与最佳实践

- 什么时候用 stream，什么时候用 for/多线程
- 并行流的适用场景和注意事项
- 避免副作用和状态共享
- 代码可读性和性能权衡

---


---

## 常见 Stream 案例

### 案例1：筛选和收集
**描述**：从整数列表中过滤出偶数，收集为新的列表。

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
```

---

### 案例2：映射与转换
**描述**：将字符串列表全部转为大写。

```java
List<String> words = Arrays.asList("java", "stream", "lambda");
List<String> upperWords = words.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

---

### 案例3：排序
**描述**：对自定义对象列表按分数降序排序。

```java
List<Student> students = ...;
List<Student> sorted = students.stream()
    .sorted(Comparator.comparing(Student::getScore).reversed())
    .collect(Collectors.toList());
```

---

### 案例4：去重
**描述**：对列表去重并输出。

```java
List<Integer> nums = Arrays.asList(1, 2, 2, 3, 3, 3, 4);
List<Integer> unique = nums.stream().distinct().collect(Collectors.toList());
```

---

### 案例5：分组
**描述**：按首字母分组字符串。

```java
List<String> words = Arrays.asList("apple", "banana", "apricot", "blueberry");
Map<Character, List<String>> group = words.stream()
    .collect(Collectors.groupingBy(w -> w.charAt(0)));
```

---

### 案例6：统计与归约
**描述**：计算整数列表的总和、最大值、平均值。

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream().mapToInt(Integer::intValue).sum();
int max = numbers.stream().mapToInt(Integer::intValue).max().orElse(0);
double avg = numbers.stream().mapToInt(Integer::intValue).average().orElse(0.0);
```

---

### 案例7：flatMap 扁平化
**描述**：将二维字符串列表扁平化为一维列表。

```java
List<List<String>> lists = Arrays.asList(
    Arrays.asList("a", "b"),
    Arrays.asList("c", "d")
);
List<String> flat = lists.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList());
```

---

### 案例8：并行流
**描述**：对大数据量进行并行统计。

```java
long count = numbers.parallelStream().filter(n -> n % 2 == 0).count();
```
当然可以！以下是一些**Java 8 Stream 常见陷阱案例及练习**，适合课堂讲解和自学练习，涵盖流的生命周期、线程安全、副作用、性能、空指针等方面。

---

# 陷阱题

## 1. 流只能消费一次（复用陷阱）

```java
Stream<String> stream = Stream.of("a", "b", "c");
stream.forEach(System.out::println);
// 错误：流已经被消费
stream.filter(s -> s.equals("a")).count(); // 抛 IllegalStateException
```
**练习**：修复上面的问题，使得可以多次操作集合。

---

## 2. 并行流与共享可变状态（线程安全陷阱）

```java
List<Integer> numbers = IntStream.range(0, 1000).boxed().collect(Collectors.toList());
List<Integer> unsafeList = new ArrayList<>();
numbers.parallelStream().forEach(unsafeList::add); // 线程不安全，结果可能小于1000
System.out.println(unsafeList.size());
```
**练习**：用线程安全的方式收集结果。

---

## 3. peek 用于副作用（副作用陷阱）

```java
List<String> list = Arrays.asList("a", "b", "c");
List<String> result = new ArrayList<>();
list.stream()
    .peek(result::add) // 错误用法：副作用依赖peek
    .map(String::toUpperCase)
    .collect(Collectors.toList());
System.out.println(result); // 可能为空或不全
```
**练习**：用正确方式收集数据。

---

## 4. NullPointerException 陷阱

```java
List<String> list = Arrays.asList("a", null, "b");
list.stream().map(String::toUpperCase).forEach(System.out::println); // NPE
```
**练习**：如何安全处理流中的 null 元素？

---

## 5. 流的惰性求值陷阱

```java
List<String> list = Arrays.asList("a", "b", "c");
list.stream().map(s -> {
    System.out.println("map: " + s);
    return s.toUpperCase();
}); // 没有终止操作，什么都不会输出
```
**练习**：加一句代码让 map 能被执行。

---

## 6. 性能陷阱：小任务并行流反而更慢

```java
List<Integer> nums = IntStream.range(0, 100).boxed().collect(Collectors.toList());
long t1 = System.currentTimeMillis();
nums.parallelStream().map(i -> i + 1).forEach(i -> {});
long t2 = System.currentTimeMillis();
System.out.println("并行流耗时：" + (t2 - t1) + "ms");
```
**练习**：用更重的任务模拟并行流的优势。

---

## 7. flatMap 与 map 混淆陷阱

```java
List<List<Integer>> lists = Arrays.asList(Arrays.asList(1,2), Arrays.asList(3,4));
lists.stream().map(list -> list.stream()).forEach(System.out::println); // 输出的是Stream对象
```
**练习**：如何让输出变成1,2,3,4？

---

## 8. collect 的错误用法

```java
List<String> list = Arrays.asList("a", "b", "c");
Set<String> set = list.stream().collect(Collectors.toList()); // 错误，类型不匹配
```
**练习**：如何将流收集到 Set？

---

## 9. 并行流分组陷阱

```java
List<String> list = Arrays.asList("a", "b", "a");
Map<String, Long> map = list.parallelStream()
    .collect(Collectors.groupingBy(s -> s, Collectors.counting())); // 不是线程安全的
```
**练习**：如何让分组统计线程安全？

---

## 10. 无限流未终止陷阱

```java
Stream<Integer> infinite = Stream.iterate(0, n -> n + 1);
infinite.forEach(System.out::println); // 程序不会结束
```
**练习**：如何让程序只输出前10个数字？

---

## 进阶练习

- 用流实现两个 List 的交集和并集。
- 用流实现分页（跳过前 n 个，取后 m 个）。
- 用流实现多级分组（如按年级和性别分组学生）。
- 用流实现安全的 Optional 链式取值。

---

---

## 作业练习题

1. **筛选字符串长度大于3的单词，并转为大写后去重，按字母顺序输出。**

2. **给定一个学生列表，统计每个年级的学生人数。**

3. **从一组订单中，找出总金额最高的前3个订单的订单号。**

4. **将一个字符串数组中的所有单词合并成一个用逗号分隔的字符串（不重复）。**

5. **统计一个整数列表中所有偶数的平方之和。**

6. **将员工列表按部门分组，并统计每个部门的平均工资。**

7. **判断一个字符串列表中，是否所有元素都以字母"a"开头。**

8. **对一组日期字符串（如"2024-06-20"），找出月份为6月的所有日期。**

9. **给定学生列表，按分数段分组（<60, 60-80, >80），统计每组人数。**

10. **用 Stream 实现：找出两个列表的交集和并集。**

---

### 进阶练习

11. **用并行流对100万个随机整数求和，并与单线程求和做性能对比。**

12. **将包含学生考试成绩的 Map<String, List<Integer>>，转换为 Map<String, Double>，表示每个学生的平均分。**

13. **模拟一个复杂对象的流式处理：员工列表->筛选年龄>30->按部门分组->每组取工资最高的员工。**

