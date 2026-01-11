# Flink总结

## 基础篇

### 第01讲：Flink的应用场景和架构模型

**应用场景：**实时数据计算

Flink 同时支持流式及批量分析应用，这就是我们所说的批流一体。Flink 承担了**数据的实时采集、实时计算和下游发送。**

Flink 在实时数仓和实时 ETL 中有天然的优势：

1. 状态管理，实时数仓里面会进行很多的聚合计算，这些都需要对于状态进行访问和管理，Flink 支持强大的状态管理；
2. 丰富的 API，Flink 提供极为丰富的多层次 API，包括 Stream API、Table API 及 Flink SQL；
3. 生态完善，实时数仓的用途广泛，Flink 支持多种存储（HDFS、ES 等）；
4. 批流一体，Flink 已经在将流计算和批计算的 API 进行统一。

**事件驱动型应用**

  事件驱动型应用是一类具有状态的应用，它从一个或多个事件流提取数据，并根据到来的事件触发计算、状态更新或其他外部动作。

在事件驱动应用中数据和计算不会分离，应用只需访问本地内存或磁盘即可获取数据，所以具有更高的吞吐和更低的延迟。

**特性**

**高效的状态管理**：Flink自带的State Backend 可以很好的存储中间状态信息。

**丰富的窗口支持：**Flink 支持包含滚动窗口、滑动窗口及其他窗口；

**多种时间语义：**Flink 支持 Event Time、Processing Time 和 Ingestion Time（摄入时间）；

**不同级别的容错：**Flink 支持 At Least Once 或 Exactly Once 容错级别。

**Flink 的分层模型**

![image-20251108220451993](C:\Users\55422\AppData\Roaming\Typora\typora-user-images\image-20251108220451993.png)

  不需要上图中的最低级别的 Low-level 抽象，而是针对 Core API 编程， 比如 DataStream API（有界/无界流）和 DataSet API （有界数据集）。这些流畅的 API 提供了用于数据处理的通用构建块，比如各种形式用户指定的转换、连接、聚合、窗口、状态等。

Table API 和 SQL 是 Flink 提供的更为高级的 API 操作，Flink SQL 是 Flink 实时计算为简化计算模型，降低用户使用实时计算门槛而设计的一套符合标准 SQL 语义的开发语言。

**Flink 的数据流模型**

  Flink 程序的**基础构建模块**是流（Streams）与转换（Transformations），每一个数据流起始于一个或多个 Source，并终止于一个或多个 Sink。数据流类似于有向无环图（DAG）。

  面对复杂的生产环境，Flink 任务大都是**并行进行和分布**在各个计算节点上。在 Flink 任务执行期间，每一个数据流都会有多个分区，并且每个算子都有多个算子任务并行进行。算子子任务的数量是该特定算子的并行度。

**Flink 中的窗口和时间**

窗口

| 特性       | 滚动窗口（Tumbling Window） | 滑动窗口（Sliding Window）          | 会话窗口（Session Window）     |
| ---------- | --------------------------- | ----------------------------------- | ------------------------------ |
| 窗口大小   | 固定                        | 固定                                | 不固定（由空闲间隔决定）       |
| 窗口重叠性 | 无重叠                      | 可重叠（步长 < 窗口大小）           | 无重叠                         |
| 数据归属   | 唯一窗口                    | 可能多个窗口                        | 唯一窗口                       |
| 触发方式   | 固定时间 / 事件数           | 固定步长（时间 / 事件数）           | 空闲间隔超时或新事件触发       |
| 核心参数   | 窗口大小                    | 窗口大小、滑动步长                  | 空闲间隔                       |
| 典型场景   | 每日 DAU、每小时订单统计    | 实时高频趋势监控（如 5 分钟错误率） | 用户会话分析、单次操作行为聚合 |

时间

| 特性         | 事件时间（Event Time）       | 摄取时间（Ingestion Time）       | 处理时间（Processing Time）  |
| ------------ | ---------------------------- | -------------------------------- | ---------------------------- |
| 时间戳来源   | 事件生产者（如客户端、设备） | 流处理系统的数据源（Source）     | 执行计算的节点（算子）       |
| 时间戳稳定性 | 固定不变（事件生成时确定）   | 固定不变（数据摄入时确定）       | 可变（随处理节点和时间变化） |
| 乱序处理需求 | 需要（依赖水印）             | 较少（仅需处理摄入后的轻微乱序） | 无需（按处理顺序直接计算）   |
| 结果一致性   | 高（反映真实业务时间）       | 中（反映数据进入系统的时间）     | 低（依赖系统处理速度）       |
| 性能开销     | 较高（需水印和乱序处理）     | 中                               | 低                           |
| 典型应用场景 | 金融交易、计费系统           | 中间指标监控、不可靠时间戳场景   | 低精度实时监控、本地测试     |

### 第02讲：Flink 入门程序 WordCount 和 SQL 实现

**DataSet WordCount**（批处理词频统计）

程序主要分为两个部分：一部分是将文字拆分成单词；另一部分是单词进行分组计数并打印输出结果。

```java
    public static void main(String[] args) throws Exception {
      // 1.创建Flink运行的上下文环境
      final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
      // 创建DataSet，这里我们的输入是一行一行的文本
      DataSet<String> text = env.fromElements(
            "Flink Spark Storm",
            "Flink Flink Flink",
            "Spark Spark Spark",
            "Storm Storm Storm"
      );
      // 通过Flink内置的转换函数进行计算
      DataSet<Tuple2<String, Integer>> counts =
            text.flatMap(new LineSplitter())
                  .groupBy(0)
                  .sum(1);
      //结果打印
      counts.printToErr();
   }
   public static final class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
      @Override
      public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
         // 将文本分割
         String[] tokens = value.toLowerCase().split("\\W+");
         for (String token : tokens) {
            if (token.length() > 0) {
               out.collect(new Tuple2<String, Integer>(token, 1));
            }
         }
      }
    }
```

**DataStream WordCount**（流处理词频统计）

```java
public class StreamingJob {
    public static void main(String[] args) throws Exception {
        // 创建Flink的流式计算环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 监听本地9000端口
        DataStream<String> text = env.socketTextStream("127.0.0.1", 9000, "\n");
        // 将接收的数据进行拆分，分组，窗口计算并且进行聚合输出
        DataStream<WordWithCount> windowCounts = text
                .flatMap(new FlatMapFunction<String, WordWithCount>() {
                    @Override
                    public void flatMap(String value, Collector<WordWithCount> out) {
                        for (String word : value.split("\\s")) {
                            out.collect(new WordWithCount(word, 1L));
                        }
                    }
                })
                .keyBy("word")
                .timeWindow(Time.seconds(5), Time.seconds(1))
                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                        return new WordWithCount(a.word, a.count + b.count);
                    }
                });
        // 打印结果
        windowCounts.print().setParallelism(1);
        env.execute("Socket Window WordCount");
    }
    // Data type for words with count
    public static class WordWithCount {
        public String word;
        public long count;
        public WordWithCount() {}
        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }
        @Override
        public String toString() {
            return word + " : " + count;
        }
    }
}
```

**Flink Table & SQL WordCount**（ Flink 实时计算为简化计算模型）

```java
public class WordCountSQL {
    public static void main(String[] args) throws Exception{
        //获取运行环境
        ExecutionEnvironment fbEnv = ExecutionEnvironment.getExecutionEnvironment();
        //创建一个tableEnvironment
        BatchTableEnvironment fbTableEnv = BatchTableEnvironment.create(fbEnv);
        String words = "hello flink hello lagou";
        String[] split = words.split("\\W+");
        ArrayList<WC> list = new ArrayList<>();
        for(String word : split){
            WC wc = new WC(word,1);
            list.add(wc);
        }
        DataSet<WC> input = fbEnv.fromCollection(list);
        //DataSet 转sql, 指定字段名
        Table table = fbTableEnv.fromDataSet(input, "word,frequency");
        table.printSchema();
        //注册为一个表
        fbTableEnv.createTemporaryView("WordCount", table);
        Table table02 = fbTableEnv.sqlQuery("select word as word, sum(frequency) as frequency from WordCount GROUP BY word");
        //将表转换DataSet
        DataSet<WC> ds3  = fbTableEnv.toDataSet(table02, WC.class);
        ds3.printToErr();
    }
    public static class WC {
        public String word;
        public long frequency;
        public WC() {}
        public WC(String word, long frequency) {
            this.word = word;
            this.frequency = frequency;
        }
        @Override
        public String toString() {
            return  word + ", " + frequency;
        }
    }
}

```

### 第03讲：Flink 的编程模型与其他框架比较

**Flink 的核心语义和架构模型**

**核心概念**

Streams（流），流分为有界流和无界流。有界流指的是有固定大小，不随时间增加而增长的数据，比如我们保存在 Hive 中的一个表；而无界流指的是数据随着时间增加而增长，计算状态持续进行，比如我们消费 Kafka 中的消息，消息持续不断，那么计算也会持续进行不会结束。

State（状态），所谓的状态指的是在进行流式计算过程中的信息。一般用作容错恢复和持久化，流式计算在本质上是增量计算，也就是说需要不断地查询过去的状态。状态在 Flink 中有十分重要的作用，例如为了确保 Exactly-once 语义需要将数据写到状态中；此外，状态的持久化存储也是集群出现 Fail-over 的情况下自动重启的前提条件。

Time（时间），Flink 支持了 Event time、Ingestion time、Processing time 等多种时间语义，时间是我们在进行 Flink 程序开发时判断业务状态是否滞后和延迟的重要依据。

API：Flink 自身提供了不同级别的抽象来支持我们开发流式或者批量处理程序，由上而下可分为 SQL / Table API、DataStream API、ProcessFunction 三层，开发者可以根据需要选择不同层级的 API 进行开发。

**Flink 集群模型和角色**

JobManager：它扮演的是集群管理者的角色，负责调度任务、协调 checkpoints、协调故障恢复、收集 Job 的状态信息，并管理 Flink 集群中的从节点 TaskManager。

TaskManager：实际负责执行计算的 Worker，在其上执行 Flink Job 的一组 Task；TaskManager 还是所在节点的管理员，它负责把该节点上的服务器信息比如内存、磁盘、任务运行情况等向 JobManager 汇报。

Client：用户在提交编写好的 Flink 工程时，会先创建一个客户端再进行提交，这个客户端就是 Client，Client 会根据用户传入的参数选择使用 yarn per job 模式、stand-alone 模式还是 yarn-session 模式将 Flink 程序提交到集群。

**Flink 资源和资源组**

1. **Task Slot 概念**：Flink 中 TaskManager 是单个 JVM 进程，通过 Task Slot 划分计算资源（仅隔离内存，不涉及 CPU），同一 TaskManager 内的任务可共享 TCP 连接提升效率，不同算子操作也可置于同一 Slot 实现资源共享。
2. Flink 优势及与其他框架的区别
   - **架构**：Flink 采用主从模式，程序经 Stream Graph、JobGraph 优化为可执行的 ExecutionGraph，分布式部署形成网状拓扑；Storm 为主从模式且强依赖 ZooKeeper，Spark Streaming 基于 Spark 本质是微批处理。
   - **容错**：Flink 基于两阶段提交实现精确一次处理语义；Storm 仅支持至少一次处理语义；Spark Streaming 依赖 checkpoint 恢复数据但会导致重复处理。
   - **反压处理**：Flink 借助分布式阻塞队列的天然阻塞特性实现反压；Storm 处理方式简单粗暴且调优难；Spark Streaming 通过构造速率控制器结合 PID 算法调节数据接收速率。

### 第04讲：Flink 常用的 DataSet 和 DataStream API

  Flink 程序的基础构建模块是流（Streams）和转换（Transformations），每一个数据流起始于一个或多个 Source，并终止于一个或多个 Sink。数据流类似于有向无环图（DAG）。

**map(转换)：**Map 接受一个元素作为输入，并且根据开发者自定义的逻辑处理后输出。

**FlatMap（扁平化处理）**：FlatMap 接受一个元素，返回零到多个元素。FlatMap 和 Map 有些类似，但是当返回值是列表的时候，FlatMap 会将列表“平铺”，也就是以单个元素的形式进行输出。

**Filter（筛选）**：过滤掉不需要的数据，每个元素都会被 filter 函数处理，如果 filter 函数返回 true 则保留，否则丢弃。

KeyBy（**数据分区与分组**）：根据指定的 Key 对数据流进行划分，让相同 Key 的数据被分配到同一个下游任务（Task）中处理，为后续的聚合（如 Sum、Max）、窗口（Window）等基于 Key 的操作提供基础。

**Aggregations（聚合函数）：**常见的聚合函数包括但不限于 sum、max、min 等。Aggregations 也需要指定一个 key 进行聚合。min 和 minBy 的区别在于，min 会返回我们制定字段的最大值，minBy 会返回对应的元素。

**Reduce（数据归约）：**将数据流中相同 Key 的数据（基于 KeyBy 后的 KeyedStream）通过自定义逻辑持续合并，最终输出一个聚合结果，是实现复杂状态化计算的重要工具

### 第05讲：Flink SQL & Table 编程和案例

**动态表**

Flink Table & SQL 在处理流数据时会**时时刻刻处于动态的数据变化中**，所以便有了一个动态表的概念。在查询动态表的时候，SQL 会做连续查询，不会终止。

**常用算子**

**SELECT/AS/WHERE**：SELECT、WHERE 和传统 SQL 用法一样，用于筛选和过滤数据，同时适用于 DataStream 和 DataSet。

```mysql
SELECT name，age FROM Table where name LIKE '%小明%';
SELECT * FROM Table WHERE age = 20;
SELECT name, age
FROM Table
WHERE name IN (SELECT name FROM Table2)

```

**GROUP BY / DISTINCT/HAVING**：GROUP BY 用于进行分组操作，DISTINCT 用于结果去重。
HAVING 和传统 SQL 一样，可以用来在聚合函数之后进行筛选。

```mysql
SELECT DISTINCT name FROM Table;
SELECT name, SUM(score) as TotalScore FROM Table GROUP BY name;
SELECT name, SUM(score) as TotalScore FROM Table GROUP BY name HAVING
SUM(score) > 300;

```

**JOIN**：JOIN 可以用于把来自两个表的数据联合起来形成结果表，目前 Flink 的 Join 只支持等值连接。

```mysql
用 用户表和商品表进行关联
SELECT *
FROM User LEFT JOIN Product ON User.name = Product.buyer

SELECT *
FROM User RIGHT JOIN Product ON User.name = Product.buyer

SELECT *
FROM User FULL OUTER JOIN Product ON User.name = Product.buyer

```

**WINDOW**：

根据窗口数据划分的不同：

- **滚动窗口**，窗口数据有固定的大小，窗口中的数据不会叠加；

  ```mysql
  SELECT 
      [gk],
      [TUMBLE_START(timeCol, size)], 
      [TUMBLE_END(timeCol, size)], 
      agg1(col1), 
      ... 
      aggn(colN)
  FROM Tab1
  GROUP BY [gk], TUMBLE(timeCol, size
  ```

  ```mysql
  计算每个用户每天的订单数量
  SELECT 
  	user, 
  	TUMBLE_START(timeLine, INTERVAL '1' DAY) as winStart, 			SUM(amount) FROM Orders 
  	GROUP BY TUMBLE(timeLine, INTERVAL '1' DAY), user;
  ```

  TUMBLE_START 和 TUMBLE_END 代表窗口的开始时间和窗口的结束时间，TUMBLE (timeLine, INTERVAL '1' DAY) 中的 timeLine 代表时间字段所在的列，INTERVAL '1' DAY 表示时间间隔为一天。

- **滑动窗口**，窗口数据有固定大小，可以通过 slide 参数控制滑动窗口的创建频率,多个滑动窗口可能出现数据重叠；

  ```mysql
  SELECT 
      [gk], 
      [HOP_START(timeCol, slide, size)] ,
      [HOP_END(timeCol, slide, size)],
      agg1(col1), 
      ... 
      aggN(colN) 
  FROM Tab1
  GROUP BY [gk], HOP(timeCol, slide, size);
  ```

  ```mysql
  每间隔一小时计算一次过去 24 小时内每个商品的销量
  SELECT 
  	product, 
  	SUM(amount) FROM Orders 
  	GROUP BY HOP(rowtime, INTERVAL '1' HOUR, INTERVAL '1' DAY), product;
  ```

   INTERVAL '1' HOUR 代表滑动窗口生成的时间间隔。

- **会话窗口**，窗口数据没有固定的大小，根据用户传入的参数进行划分，窗口数据无叠加；

  ```mysql
  SELECT 
      [gk], 
      SESSION_START(timeCol, gap) AS winStart,
      SESSION_END(timeCol, gap) AS winEnd,
      agg1(col1),
       ... 
      aggn(colN)
  FROM Tab1
  GROUP BY [gk], SESSION(timeCol, gap)
  ```

  ```mysql
  SELECT 
  	user, 
  	SESSION_START(rowtime, INTERVAL '1' HOUR) AS sStart, 			SESSION_ROWTIME(rowtime, INTERVAL '1' HOUR) AS sEnd, 			SUM(amount) FROM Orders GROUP BY SESSION(rowtime, INTERVAL '1' HOUR), user;
  
  ```

  

**内置函数**

| 分类               | 函数                                                         | 逻辑描述                                                     |
| ------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **比较函数**       | value1=value2                                                | 如果 value1 等于 value2，则返回 TRUE；如果 value1 或 value2 为 NULL，则返回 UNKNOWN |
| **比较函数**       | value1<>value2                                               | 如果 value1 不等于 value2，则返回 TRUE；如果 value1 或 value2 为 NULL，则返回 UNKNOWN |
| **比较函数**       | value1>value2                                                | 如果 value1 大于 value2，则返回 TRUE；如果 value1 或 value2 为 NULL，则返回 UNKNOWN |
| **比较函数**       | value1 <value2                                               | 如果 value1 小于 value2，则返回 TRUE；如果 value1 或 value2 为 NULL，则返回 UNKNOWN |
| **比较函数**       | value IS NULL                                                | 如果 value 为 NULL，则返回 TRUE                              |
| **比较函数**       | value IS NOT NULL                                            | 如果 value 不为 NULL，则返回 TRUE                            |
| **比较函数**       | value1 IN (value2, value3…)                                  | 如果给定列表中存在 value1（value2，value3，…），则返回 TRUE。当（value2，value3，…）包含 NULL，若能找到数据则返回 TRUE，否则返回 UNKNOWN；如果 value1 为 NULL，则始终返回 UNKNOWN |
| **逻辑函数**       | expr1 AND expr2                                              | 若 expr1 和 expr2 都为 TRUE，则返回 TRUE；若任一为 NULL 或 FALSE 则返回对应结果（含 UNKNOWN 逻辑） |
| **逻辑函数**       | expr1 OR expr2                                               | 若 expr1 或 expr2 为 TRUE，则返回 TRUE；若任一为 NULL 或 FALSE 则返回对应结果（含 UNKNOWN 逻辑） |
| **逻辑函数**       | NOT expr                                                     | 对 expr 取反，若 expr 为 TRUE 则返回 FALSE，为 FALSE 则返回 TRUE，为 NULL 则返回 UNKNOWN |
| **逻辑函数**       | CASE WHEN cond1 THEN res1 [WHEN cond2 THEN res2 ...] [ELSE resN] END | 多分支条件判断，满足 cond1 则返回 res1，满足 cond2 则返回 res2… 都不满足则返回 resN |
| **算术函数**       | value1 + value2                                              | 返回 value1 与 value2 的和                                   |
| **算术函数**       | value1 - value2                                              | 返回 value1 与 value2 的差                                   |
| **算术函数**       | value1 * value2                                              | 返回 value1 与 value2 的积                                   |
| **算术函数**       | value1 / value2                                              | 返回 value1 与 value2 的商（若 value2 为 0 则结果可能异常）  |
| **算术函数**       | value1 % value2                                              | 返回 value1 除以 value2 的余数（取模）                       |
| **算术函数**       | ABS(value)                                                   | 返回 value 的绝对值                                          |
| **算术函数**       | ROUND(value, [scale])                                        | 对 value 四舍五入，scale 为保留小数位数（可选）              |
| **字符串处理函数** | CONCAT(string1, string2, ...)                                | 拼接多个字符串，返回合并后的新字符串                         |
| **字符串处理函数** | SUBSTRING(string, start, [length])                           | 从 string 的 start 位置（从 1 开始）截取长度为 length 的子串（length 可选，默认到末尾） |
| **字符串处理函数** | UPPER(string)                                                | 将 string 转换为大写                                         |
| **字符串处理函数** | LOWER(string)                                                | 将 string 转换为小写                                         |
| **字符串处理函数** | REPLACE(string, search, replace)                             | 用 replace 替换 string 中所有的 search 子串                  |
| **字符串处理函数** | LENGTH(string)                                               | 返回 string 的长度（字符数）                                 |
| **字符串处理函数** | string1 LIKE string2                                         | 如果 string1 匹配模式 string2，则返回 TRUE；如果 string1 或 string2 为 NULL，则返回 UNKNOWN |
| **时间函数**       | YEAR(timestamp/date)                                         | 提取 timestamp 或 date 中的年份                              |
| **时间函数**       | MONTH(timestamp/date)                                        | 提取 timestamp 或 date 中的月份                              |
| **时间函数**       | DAY(timestamp/date)                                          | 提取 timestamp 或 date 中的日期（几号）                      |
| **时间函数**       | HOUR(timestamp)                                              | 提取 timestamp 中的小时数                                    |
| **时间函数**       | TO_TIMESTAMP(string, [format])                               | 将 string 按指定 format 转换为时间戳（format 可选，默认适配常见格式） |
| **时间函数**       | TO_DATE(string, [format])                                    | 将 string 按指定 format 转换为日期（format 可选，默认适配常见格式） |
| **时间函数**       | DATE_ADD(date, INTERVAL n unit)                              | 给 date 增加 n 个 unit（如 DAY、MONTH 等）                   |
| **时间函数**       | DATE_SUB(date, INTERVAL n unit)                              | 给 date 减少 n 个 unit（如 DAY、MONTH 等）                   |
| **时间函数**       | TIMESTAMPDIFF(unit, start, end)                              | 计算 start 到 end 的时间差，单位为 unit（如 SECOND、MINUTE、DAY 等） |

### 第06讲：Flink 集群安装部署和 HA 配置

Local 模式是 Flink 提供的最简单部署模式，一般用来本地测试和演示使用。

```
运行脚本启动 Flink
./bin/start-cluster.sh
尝试提交一个测试任务
./bin/flink run examples/batch/WordCount.jar
```

Standalone 模式是集群模式的一种，但是这种模式一般并不运行在生产环境中.

- Standalone 模式的部署相对简单，可以支持小规模，少量的任务运行；
- Stabdalone 模式缺少系统层面对集群中 Job 的管理，容易遭成资源分配不均匀；
- 资源隔离相对简单，任务之间资源竞争严重

**On Yarn 模式和 HA 配置**

依托 YARN 的资源管理能力实现 Flink 任务的动态资源调度；而 HA（High Availability，高可用）配置则用于解决单点故障问题，保障集群持续稳定运行



## 进阶篇

### 第07讲：Flink 常见核心概念分析

**分布式缓存**

Hadoop 会将一些数据或者文件缓存在 HDFS 上，在分布式环境中让所有的计算节点调用同一个配置文件。

Flink 提供的分布式缓存类型 Hadoop，目的是为了在分布式环境中让每一个 TaskManager 节点保存一份相同的数据或者文件，当前计算节点的 task 就像读取本地文件一样拉取这些配置。

**故障恢复**

Flink 支持了不同级别的故障恢复策略，jobmanager.execution.failover-strategy 的可配置项有两种：full 和 region。

   **恢复策略为 full 时**，集群中的 Task 发生故障，那么该任务的所有 Task 都会发生重启。

事实上，我们可能只是集群上某一个或几个Task发生了故障，只需重启有问题的一部分即可， 下面就会使用到**基于 Region 的局部重启策略**。Flink 会把我们的任务分成不同的 Region，当某一个 Task 发生故障时，Flink 会计算需要故障恢复的最小 Region。

 接下来就需要重启发生故障的Region：

1.发生错误的Task所在的Region需要重启

2.若当前的Region的依赖数据出现损坏或者部分丢失，那么生产数据的Region也需要重启。

3.为了保证数据的一致性，当前的Region的下游也需要重启。

**重启策略**

Flink 提供了多种类型和级别的重启策略，常用的重启策略包括：

1.固定延迟重启策略模式2.失败率重启策略模式3.无重启策略模式

  如果用户配置了 checkpoint，但没有设置重启策略，那么会按照固定延迟重启策略模式进行重启；如果用户没有配置 checkpoint，那么默认不会重启。

**无重启策略模式**

在这种情况下，如果我们的作业发生错误，任务会直接退出。


```java
final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
env.setRestartStrategy(RestartStrategies.noRestart());
```

**固定延迟重启策略模式**

需要指定两个参数，首先 Flink 会根据用户配置的重试次数进行重试，每次重试之间根据配置的时间间隔进行重试

```
env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
        3, // 重启次数
        Time.of(5, TimeUnit.SECONDS) // 时间间隔
));
```

**失败率重启策略模式**

需要指定三个参数，失败率重启策略在 Job 失败后会重启，但是超过失败率后，Job 会最终被认定失败。在两个连续的重启尝试之间，重启策略会等待一个固定的时间

```java
env.setRestartStrategy(RestartStrategies.failureRateRestart(
        3, // 每个时间间隔的最大故障次数
        Time.of(5, TimeUnit.MINUTES), // 测量故障率的时间间隔
        Time.of(5, TimeUnit.SECONDS) //  每次任务失败时间间隔
));
```

**并行度**

在分布式运行环境中我们的一个算子任务被切分成了多个子任务并行执行。多个子任务就是多个并行度。

四种来设置并行度：

1.算子级别：代码中调用setParallelism方法来设置每一个算子的并行度。

```java
DataSet<Tuple2<String, Integer>> counts =
      text.flatMap(new LineSplitter())
            .groupBy(0)
            .sum(1).setParallelism(1);
```

2.执行环境级别：创建 Flink 的上下文时可以显示的调用 env.setParallelism() 方法，来设置当前执行环境的并行度，这个配置会对当前任务的所有算子、Source、Sink 生效。

```java
final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
env.setParallelism(5);
```

3.系统配置级别：parallelism.default，该配置即是在系统层面设置所有执行环境的并行度配置。

整体上讲，这四种级别的配置生效优先级如下：**算子级别 > 执行环境级别 > 提交任务级别 > 系统配置级别**。

### 第08讲：Flink 窗口、时间和水印

**Flink 的窗口和时间**

Flink 窗口的实现，根据窗口数据划分的不同，目前 Flink 支持如下 3 种：

- **滚动窗口**，窗口数据有固定的大小，窗口中的数据不会叠加；
- **滑动窗口**，窗口数据有固定的大小，并且有生成间隔；
- **会话窗口**，窗口数据没有固定的大小，根据用户传入的参数进行划分，窗口数据无叠加。

Flink 中的时间分为三种：

![image-20251115180706298](C:\Users\55422\AppData\Roaming\Typora\typora-user-images\image-20251115180706298.png)

- **事件时间**（Event Time）指的是数据产生的时间，这个时间一般由数据生产方自身携带，比如 Kafka 消息，每个生成的消息中自带一个时间戳代表每条数据的产生时间。Event Time 从消息的产生就诞生了，不会改变，也是我们使用最频繁的时间。

  ```java
  StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
  //设置时间属性为 EventTime
  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
  DataStream<MyEvent> stream = env.addSource(new FlinkKafkaConsumer09<MyEvent>(topic, schema, props));
  stream
      .keyBy( (event) -> event.getUser() )
      .timeWindow(Time.hours(1))
      .reduce( (a, b) -> a.add(b) )
      .addSink(...);
  ```

- **摄入时间**（Ingestion Time），事件进入 Flink 系统的时间，在 Flink 的 Source 中，每个事件会把当前时间作为时间戳，后续做窗口处理都会基于这个时间。理论上 Ingestion Time 处于 Event Time 和 Processing Time之间。

- **处理时间**（Processing Time），事件被处理的时间。 数据被 Flink 框架处理时机器的系统时间，Processing Time 是 Flink 的时间系统中最简单的概念，但是这个时间存在一定的不确定性，比如消息到达处理节点延迟等影响。

**水位线（WaterMark）**

是一个时间戳。**衡量事件时间进展的一个标志**，告诉系统已经处理到哪个时间点的数据了。**水位线是事件时间的时钟。**

为什么需要水位线？

在流式处理中，数据可能乱序到达，比如：一条事件发生在 10 秒时，却在 15 秒时才到；如果系统只根据**处理时间**触发窗口，就会导致数据统计不准确。所以 Flink 引入 **事件时间** 概念，并用 **水位线** 来控制窗口何时触发计算。

系统认为所有时间戳 **小于等于水位线时间** 的事件都已经到齐，可以触发窗口计算。

**水位线作用：**

1.**控制窗口触发**：当水位线 >= 窗口结束时间时，窗口就会被触发计算。

**2.识别延迟数据：**如果某条数据的事件时间 < 当前水位线，就说明他是延迟数据。

**水位线的生成方式：**

**周期性**  系统每隔一段时间（比如 200ms）自动发出一个 watermark：`env.getConfig().setAutoWatermarkInterval(200);`

**断点式 ** 每来一条数据就发出一条 watermark，适合数据源自带时间标记的场景。

**水位线** 是 Flink 用来推进事件时间的机制，用于判断哪些事件已经到齐、哪些还在等待。
 它的核心作用是**触发窗口计算**和**识别延迟数据**，
 计算公式通常是：**当前最大事件时间 - 最大乱序时间**。
 当水位线超过窗口结束时间时，窗口才会被计算

### 第09讲：Flink 状态与容错

状态指的是**流处理过程中那些需要记住的数据**，数据包括业务数据、元数据。  Flink 本身提供了不同的状态管理器来管理状态。

Flink 中的**状态**是算子在处理数据过程中存储的中间结果（如累计计数、窗口数据、历史关联信息），分为：

- **Keyed State**（按键分区）：与 Key 绑定，仅作用于 `KeyedStream`，按 Key 隔离（每个 Key 一个状态实例）；
- **Operator State**：与算子并行任务绑定，作用于普通 `DataStream`，按任务隔离（每个并行任务一个状态实例）；
- **Broadcast State**：特殊的 Operator State，用于广播配置 / 规则到所有并行任务。

**容错核心机制：检查点（CheckPoint）**

检查点机制——**通过周期性快照** 记录所有算子的状态和数据流的位置，故障的时候从最近的检查点恢复，保证计算的**精准一次语义。**

1. 检查点原理

- **触发**：由 JobManager 定期向所有 Source 算子发送检查点触发信号；
- **屏障传播**：Source 算子将检查点屏障（Checkpoint Barrier）注入数据流，随数据向下游传播；
- **状态快照**：当算子收到所有输入流的同一检查点屏障时，将自身状态写入持久化存储（如 HDFS），并将屏障传递给下游；
- **确认完成**：当所有算子完成状态快照并向 JobManager 确认后，检查点正式完成。

**2. 检查点配置**

通过 `StreamExecutionEnvironment` 配置检查点：

```java
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
// 启用检查点，间隔10秒
env.enableCheckpointing(10000);
// 设置模式：EXACTLY_ONCE（默认）/ AT_LEAST_ONCE
env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
// 检查点超时时间
env.getCheckpointConfig().setCheckpointTimeout(60000);
// 最大并发检查点数
env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
// 持久化策略：故障后保留检查点
env.getCheckpointConfig().setExternalizedCheckpointCleanup(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATIO
```

### 第10讲：Flink Side OutPut 分流

旁路分流器：把输入流按照需求进行拆分。



Filter 分流：根据用户输入条件进行筛选。在分流场景中，可以进行多次filter(),把需要的不同数据生成不同的流。

Split分流：将流进行切分。需要在 split 算子中定义 OutputSelector，然后重写其中的 select 方法，将不同类型的数据进行标记，最后对返回的 SplitStream 使用 select 方法将对应的数据选择出来。

**使用 split 算子切分过的流，是不能进行二次切分的**

```java
public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    //获取数据源
    List data = new ArrayList<Tuple3<Integer,Integer,Integer>>();
    data.add(new Tuple3<>(0,1,0));
    data.add(new Tuple3<>(0,1,1));
    data.add(new Tuple3<>(0,2,2));
    data.add(new Tuple3<>(0,1,3));
    data.add(new Tuple3<>(1,2,5));
    data.add(new Tuple3<>(1,2,9));
    data.add(new Tuple3<>(1,2,11));
    data.add(new Tuple3<>(1,2,13));
    DataStreamSource<Tuple3<Integer,Integer,Integer>> items = env.fromCollection(data);
    SplitStream<Tuple3<Integer, Integer, Integer>> splitStream = items.split(new OutputSelector<Tuple3<Integer, Integer, Integer>>() {
        @Override
        public Iterable<String> select(Tuple3<Integer, Integer, Integer> value) {
            List<String> tags = new ArrayList<>();
            if (value.f0 == 0) {
                tags.add("zeroStream");
            } else if (value.f0 == 1) {
                tags.add("oneStream");
            }
            return tags;
        }
    });
    splitStream.select("zeroStream").print();
    splitStream.select("oneStream").printToErr();
    //打印结果
    String jobName = "user defined streaming source";
    env.execute(jobName);
}
```

SideOutPut 分流:SideOutPut 是 Flink 框架为我们提供的最新的也是最为推荐的分流方法，在使用 SideOutPut 时，需要按照以下步骤进行：

- 定义 OutputTag

- 调用特定函数进行数据拆分

  - ProcessFunction

  - KeyedProcessFunction

  - CoProcessFunction

  - KeyedCoProcessFunction

  - ProcessWindowFunction

  - ProcessAllWindowFunction

    SideOutPut 方式拆分流是**可以多次进行拆分**的，无需担心会爆出异常。

   **ProcessFunction 来使用 SideOutPut：**

```java
public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    //获取数据源
    List data = new ArrayList<Tuple3<Integer,Integer,Integer>>();
    data.add(new Tuple3<>(0,1,0));
    DataStreamSource<Tuple3<Integer,Integer,Integer>> items = env.fromCollection(data);
    //定义 OutputTag
    OutputTag<Tuple3<Integer,Integer,Integer>> zeroStream = new OutputTag<Tuple3<Integer,Integer,Integer>>("zeroStream") {};
    OutputTag<Tuple3<Integer,Integer,Integer>> oneStream = new OutputTag<Tuple3<Integer,Integer,Integer>>("oneStream") {};
    //调用ProcessFunction函数
    SingleOutputStreamOperator<Tuple3<Integer, Integer, Integer>> processStream= items.process(new ProcessFunction<Tuple3<Integer, Integer, Integer>, Tuple3<Integer, Integer, Integer>>() {
        @Override
        public void processElement(Tuple3<Integer, Integer, Integer> value, Context ctx, Collector<Tuple3<Integer, Integer, Integer>> out) throws Exception {
            if (value.f0 == 0) {
                ctx.output(zeroStream, value);
            } else if (value.f0 == 1) {
                ctx.output(oneStream, value);
            }
        }
    });
    DataStream<Tuple3<Integer, Integer, Integer>> zeroSideOutput = processStream.getSideOutput(zeroStream);
    DataStream<Tuple3<Integer, Integer, Integer>> oneSideOutput = processStream.getSideOutput(oneStream);
    zeroSideOutput.print();
    oneSideOutput.printToErr();
    //打印结果
    String jobName = "user defined streaming source";
    env.execute(jobName);
}
```

### 第11讲：Flink CEP 复杂事件处理

Complex Event Processing（CEP）

解决问题

需要在大量的订单交易中发现那些虚假交易，在网站的访问日志中寻找那些使用脚本或者工具“爆破”登录的用户，或者在快递运输中发现那些滞留很久没有签收的包裹等。

程序结构：定义模式、匹配结果

Flink CEP 的整个过程是：

从一个 Source 作为输入
经过一个 Pattern 算子转换为 PatternStream
经过 select/process 算子转换为 DataStream

**实战案例**

我们模拟电商网站用户搜索的数据来作为数据的输入源，然后查找其中**重复搜索某一个商品的人，并且发送一条告警消息。**

首先定义一个数据源，模拟了一些用户的搜索数据，然后定义了自己的 Pattern。这个模式的特点就是连续两次搜索商品“帽子”，然后进行匹配，发现匹配后输出一条提示信息，直接打印在控制台上。

```java
public static void main(String[] args) throws Exception{
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);
    DataStreamSource source = env.fromElements(
            //浏览记录
            Tuple3.of("Marry", "外套", 1L),
            Tuple3.of("Marry", "帽子",1L),
    );
    //定义Pattern,寻找连续搜索帽子的用户
    Pattern<Tuple3<String, String, Long>, Tuple3<String, String, Long>> pattern = Pattern
            .<Tuple3<String, String, Long>>begin("start")
            .where(new SimpleCondition<Tuple3<String, String, Long>>() {
                @Override
                public boolean filter(Tuple3<String, String, Long> value) throws Exception {
                    return value.f1.equals("帽子");
                }
            }) //.timesOrMore(3);
            .next("middle")
            .where(new SimpleCondition<Tuple3<String, String, Long>>() {
                @Override
                public boolean filter(Tuple3<String, String, Long> value) throws Exception {
                    return value.f1.equals("帽子");
                }
            });
    KeyedStream keyedStream = source.keyBy(0);
    PatternStream patternStream = CEP.pattern(keyedStream, pattern);
    SingleOutputStreamOperator matchStream = patternStream.select(new PatternSelectFunction<Tuple3<String, String, Long>, String>() {
        @Override
        public String select(Map<String, List<Tuple3<String, String, Long>>> pattern) throws Exception {
            List<Tuple3<String, String, Long>> middle = pattern.get("middle");
            return middle.get(0).f0 + ":" + middle.get(0).f2 + ":" + "连续搜索两次帽子!";
        }
    });
    matchStream.printToErr();
    env.execute("execute cep");
}
```

### 第12讲：Flink 常用的 Source 和 Connector

 Flink 自身实现了多种 Source 和 Connector 方法

Source基于文件方便地从本地文件读取数据.

基于Collections 内存中的集合、对象等创建自己的 Source。一般用来进行本地调试或者验证。

基于 Socket通过监听 Socket 端口,模拟一个实时计算环境。

自定义Source通过实现 Flink 的SourceFunction 或者 ParallelSourceFunction 来实现单个或者多个并行度的 Source。

```java
public class MyStreamingSource implements SourceFunction<Item> {
    private boolean isRunning = true;
    /**
     * 重写run方法产生一个源源不断的数据发送源
     * @param ctx
     * @throws Exception
     */
    public void run(SourceContext<Item> ctx) throws Exception {
        while(isRunning){
            Item item = generateItem();
            ctx.collect(item);
            //每秒产生一条数据
            Thread.sleep(1000);
        }
    }
    @Override
    public void cancel() {
        isRunning = false;
    }
    //随机产生一条商品数据
    private Item generateItem(){
        int i = new Random().nextInt(100);
        ArrayList<String> list = new ArrayList();
        list.add("HAT");
        list.add("TIE");
        list.add("SHOE");
        Item item = new Item();
        item.setName(list.get(new Random().nextInt(3)));
        item.setId(i);
        return item;
    }
}
```



## 生产实践篇

### 第13讲：如何实现生产环境中的 Flink 高可用配置

Standalone 模式下的 HA 配置，Flink 依赖 ZooKeeper 实现。ZooKeeper 集群独立于 Flink 集群之外，主要被用来进行 Leader 选举和轻量级状态一致性存储。

需要对 JobManager 做主备，一般推荐一个**主 JobManager** 和多个**备用的 JobManagers**。当你的主 JobManager 发生故障时，备用的 JobManager 会接管集群，以保证我们的任务正常运行。

**文件配置**

| **IP**        | **hostname** | **备注**         |
| ------------- | ------------ | ---------------- |
| 192.168.2.100 | master       | 主节点、ZK 01    |
| 192.168.2.101 | slave01      | 从节点 01、ZK 02 |
| 192.168.2.102 | slave02      | 从节点 02、ZK 03 |

Yarn 集群高可用配置

Flink on Yarn 的高可用配置只需要一个 JobManager。当 JobManager 发生失败时，Yarn 负责将其重新启动。

### 第14讲：Flink Exactly-once 实现原理解析

- **最多一次（At-most-Once）**：这种语义理解起来很简单，用户的数据只会被处理一次，不管成功还是失败，不会重试也不会重发。
- **至少一次（At-least-Once）**：这种语义下，系统会保证数据或事件至少被处理一次。如果中间发生错误或者丢失，那么会从源头重新发送一条然后进入处理系统，所以同一个事件或者消息会被处理多次。
- **精确一次（Exactly-Once）**：表示每一条数据只会被精确地处理一次，不多也不少。**即使发生故障（如节点崩溃、网络中断），数据也只会被处理一次，不会重复执行、也不会丢失**。

端到端的精准一次，应用从Source端开始到Sink端结束，数据必须经过起始点和结束点。借助Flink提供的**分布式快照和两阶段提交。**

**分布式快照机制**（内部状态一致性）

分布式快照（Flink 中核心体现为 Checkpoint）是对所有算子的状态 + 数据源消费位置的全局一致快照，由 JobManager 协调、所有 Task 协同完成，核心目标是**故障恢复时能回到某个一致的时间点，保证内部处理逻辑不重不漏**。

2. 完整执行流程

**（1）触发阶段**

JobManager 的 `CheckpointCoordinator` 按配置的间隔（如 10s）生成全局唯一的 Checkpoint ID，向所有 Source Task 发送「Checkpoint 触发请求」。

（2）Barrier 注入与传播

- Source Task 收到请求后，先记录当前消费的数据源偏移量（如 Kafka Partition 的 offset），并将该偏移量写入本地状态；
- Source Task 向下游算子发送「Checkpoint Barrier」（屏障是特殊的数据流标记，不影响业务处理），标记该 Checkpoint 的数据边界。

（3）Barrier 对齐（核心）

下游算子（如 Window、KeyBy）收到 Barrier 后，需等待**所有输入流的相同 ID Barrier 都到达**（即「Barrier 对齐」）：

- 未对齐时，算子会缓存「先到达的 Barrier 之后的数据」，避免快照包含未处理的新数据；
- 对齐完成后，算子将自身的状态（如窗口聚合结果、Keyed State）异步刷写到状态后端（如 RocksDB + HDFS）。

（4）确认与持久化

- 每个 Task 完成状态快照后，向 JobManager 发送「Checkpoint 完成确认」；
- 当所有 Task 确认完成，JobManager 将该 Checkpoint 的元数据（如状态存储路径、各 Source 偏移量）持久化到分布式存储（如 HDFS/S3），标记该 Checkpoint 成功。

**两阶段提交（2PC）：端到端 Exactly-once 的核心**

  分布式快照仅保证 Flink「内部状态」的 Exactly-once，但流处理全链路是「Source → Flink → Sink」，若 Sink 写入不具备原子性，仍会出现「处理完成但写入失败」或「重复写入」的问题。两阶段提交是 Flink 为解决「Sink 写入原子性」设计的机制，通过将 Sink 写入与 Checkpoint 绑定，实现端到端 Exactly-once。

1. 核心问题：Sink 写入的原子性挑战

假设仅依赖 Checkpoint 恢复：

- 场景 1：Flink 处理完数据并写入 Sink，但 Checkpoint 未完成，故障恢复后重新处理，导致 Sink 重复写入；
- 场景 2：Flink 处理完数据但写入 Sink 时失败，Checkpoint 未完成，恢复后重新处理，若 Sink 不支持重试，会导致数据丢失。

2. 设计思路

Flink 将「每个 Checkpoint 周期」视为一个「全局事务」，Sink 写入分为「预提交（Prepare）」和「提交（Commit）」两个阶段，且事务的提交 / 回滚与 Checkpoint 的成功 / 失败强绑定。

3. 核心实现：TwoPhaseCommitSinkFunction

Flink 提供抽象类 `TwoPhaseCommitSinkFunction`，所有支持 2PC 的 Sink（如 KafkaSink、JDBCSink）均基于该类实现，核心流程如下（以 Kafka Sink 为例）：

（1）初始化事务

每个 Sink Task 启动时，创建一个新的事务（如 Kafka 的 `Producer` 事务），用于缓存本次 Checkpoint 周期内待写入的数据。0/‘/’0

（2）预提交（Prepare Phase）

- 当 Checkpoint 触发，Flink 先完成所有算子的状态快照（分布式快照流程）；

- ```
  CheckpointCoordinator
  ```

  通知所有 Sink Task 执行「预提交」：

  - Sink Task 将缓存的数据写入 Sink（如 Kafka 的事务性分区），但不提交事务（Kafka 事务处于 `prepare` 状态，外部无法读取该数据）；
  - Sink Task 将事务 ID、预提交状态等信息写入 Checkpoint 快照；
  - Sink Task 向 JobManager 确认预提交完成。

（3）提交（Commit Phase）

- 若 JobManager 确认「所有 Task 的 Checkpoint 均成功」（包括预提交），则向所有 Sink Task 发送「提交指令」；
- Sink Task 提交事务（如 Kafka 的 `commitTransaction()`），数据正式对外可见；
- 提交完成后，创建新的事务，准备下一个 Checkpoint 周期的数据写入。

（4）回滚（Abort Phase）

- 若任意 Task 的 Checkpoint 失败（如预提交失败、状态快照失败），JobManager 通知所有 Sink Task 执行「回滚」；
- Sink Task 放弃当前事务（如 Kafka 的 `abortTransaction()`），缓存的数据被丢弃；
- 故障恢复后，从最近成功的 Checkpoint 重新处理数据，重新触发 2PC 流程。
