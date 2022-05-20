# 开箱即用的雪花漂移算法（yitter-idgenerator-spring-boot-starter）

### 1、介绍
依赖于SnowFlake IdGenerator核心代码，加入springboot自动配置，做到开箱即用的雪花生成算法。在缩短ID长度的同时，具备极高瞬时并发处理能力（50W/0.1s）。支持回拨。

请注意，本包为SnowFlake IdGenerator算法java版本springboot的集成，做到开箱即用。



### 2、技术支持

本包基于SnowFlake IdGenerator核心实现，核心实现和该算法更多细节请参考其项目地址。

[多语言新雪花算法(SnowFlake IdGenerator)](https://gitee.com/yitter/idgenerator)



### 3、算法特点

✔ 整形数字，随时间单调递增（不一定连续），长度更短，用50年都不会超过 js Number类型最大值。（默认配置）

✔ 速度更快，是传统雪花算法的2-5倍，0.1秒可生成50万个（基于8代低压i7）。

✔ 支持时间回拨处理。比如服务器时间回拨1秒，本算法能自动适应生成临界时间的唯一ID。

✔ 支持手工插入新ID。当业务需要在历史时间生成新ID时，用本算法的预留位能生成5000个每秒。

✔ 不依赖任何外部缓存和数据库。（k8s环境下自动注册 WorkerId 的动态库依赖 redis）

✔ 基础功能，开箱即用，无需配置文件、数据库连接等。



### 4、如何使用

1.引入jar包。当然也可以下载源代码。版本号与核心代码包版本保持一致。
``` pom
<dependency>
  <groupId>io.github.lmlx66</groupId>
  <artifactId>yitter-idgenerator-spring-boot-starter</artifactId>
  <version>1.0.7</version>
</dependency>
```



2.静态调用方法即可

``` java
Long id = YitIdGenerator.next();
```

是不是非常简单呢？



3.当然我们也可以对简单配置一下

```yaml
yitter:
  monomer: false # 不开启单机模式
  method: 1 # 1为雪花漂移算法，2为传统算法
  worker-id: 2 # 机器码id
```



### 5、配置详解



#### 5.1、配置文件配置

我们支持在yaml或者properties等配置文件中配置，注意前缀为`yitter`

| 参数名             | 默认值         | 作用                                        |
| ------------------ | -------------- | ------------------------------------------- |
| Method（short）    | 1              | 1表示雪花漂移算法，2表示传统雪花算法        |
| Monomer（Boolean） | true           | true表示单机架构，机器码长为1byte,机器码为1 |
| BaseTime（long）   | 1640966400000L | 基础时间，为2022-01-01 00:00:00             |
| WorkerIdBitLength  | 6              | 机器码位长（能表示机器码的最大值）          |
| WorkerId           | 0              | 机器码（当前系统的机器码）                  |
| SeqBitLength       | 6              | 序列数位长（能表示机器码的最大序列数）      |
| MaxSeqNumber       | 0（不限定）    | 最大序列数（含）                            |
| MinSeqNumber       | 0（不限定）    | 最小序列数（含）                            |
| TopOverCostCount   | 2000           | 最大漂移次数，与计算能力有关                |



#### 5.2、配置类配置

当然，我们也支持配置类配置，返回类型为YitIdGenerator，IdGeneratorProperties是基础配置类实体映射类。

但请注意，如果配置类和配置文件（yaml或properties）同时使用，优先采用配置类配置。

```java
@Configuration
public class IdGeneratorConfig {
    @Bean
    public YitIdGenerator yitIdGenerator() {
        //准备基础配置类，在此可以配置基础信息
        IdGeneratorProperties idGeneratorProperties = new IdGeneratorProperties();
        idGeneratorProperties.setWorkerId((short) 6);
        idGeneratorProperties.setWorkerIdBitLength((byte) 3);
        idGeneratorProperties.setBaseTime(1652943536440L);
        //装载id生成器的配置文件
        return new YitIdGenerator(idGeneratorProperties);
    }
}
```



#### 5.3、参数详解

❄ ***WorkerIdBitLength***，机器码位长，决定 WorkerId 的最大值，**默认值6**，取值范围 [1, 19]，实际上有些语言采用 无符号 ushort (uint16) 类型接收该参数，所以最大值是16，如果是采用 有符号 short (int16)，则最大值为15。

❄ **WorkerId**，机器码，**最重要参数**，无默认值，必须 **全局唯一**（或相同 DataCenterId 内唯一），必须 **程序设定**，缺省条件（WorkerIdBitLength取默认值）时最大值63，理论最大值 2^WorkerIdBitLength-1（不同实现语言可能会限定在 65535 或 32767，原理同 WorkerIdBitLength 规则）。不同机器或不同应用实例 **不能相同**，你可通过应用程序配置该值，也可通过调用外部服务获取值。针对自动注册WorkerId需求，本算法提供默认实现：通过 redis 自动注册 WorkerId 的动态库，详见“Tools\AutoRegisterWorkerId”。

**特别提示**：如果一台服务器部署多个独立服务，需要为每个服务指定不同的 WorkerId。

❄ ***SeqBitLength***，序列数位长，**默认值6**，取值范围 [3, 21]（建议不小于4），决定每毫秒基础生成的ID个数。规则要求：WorkerIdBitLength + SeqBitLength 不超过 22。

❄ ***MinSeqNumber***，最小序列数，默认值5，取值范围 [5, MaxSeqNumber]，每毫秒的前5个序列数对应编号0-4是保留位，其中1-4是时间回拨相应预留位，0是手工新值预留位。

❄ ***MaxSeqNumber***，最大序列数，设置范围 [MinSeqNumber, 2^SeqBitLength-1]，默认值0，真实最大序列数取最大值（2^SeqBitLength-1），不为0时，取其为真实最大序列数，一般无需设置，除非多机共享WorkerId分段生成ID（此时还要正确设置最小序列数）。

❄ ***BaseTime***，基础时间（也称：基点时间、原点时间、纪元时间），有默认值（2020年），是毫秒时间戳（是整数，.NET是DatetTime类型），作用是：用生成ID时的系统时间与基础时间的差值（毫秒数）作为生成ID的时间戳。基础时间一般无需设置，如果觉得默认值太老，你可以重新设置，不过要注意，这个值以后最好不变。



### 6、其他细节



#### 6.1、id组成

- 本算法生成的ID由3部分组成（沿用雪花算法定义）：
- +-------------------------+--------------+----------+
- | 1.相对基础时间的时间差 | 2.WorkerId | 3.序列数 |
- +-------------------------+--------------+----------+



![2](https://cdn.jsdelivr.net/gh/lmlx6688/img1/SnowFlake/2.png)



#### 6.2、集成算法

1️⃣ 用单例模式调用。外部集成方使用更多的实例并行调用本算法，不会增加ID产出效能，因为本算法采用单线程生成ID。

2️⃣ 指定唯一的 WorkerId。必须由外部系统确保 WorkerId 的全局唯一性，并赋值给本算法入口方法。

3️⃣ 单机多实例部署时使用不同 WorkerId。并非所有实现都支持跨进程的并发唯一，保险起见，在同一主机上部署多应用实例时，请确保各 WorkerId 唯一。

4️⃣ 异常处理。算法会抛出所有 Exception，外部系统应 catch 异常并做好应对处理，以免引发更大的系统崩溃。

5️⃣ 认真理解 IdGeneratorOptions 的定义，这对集成和使用本算法有帮助。

6️⃣ 使用雪花漂移算法。虽然代码里包含了传统雪花算法的定义，并且你可以在入口处指定（Method=2）来启用传统算法，但仍建议你使用雪花漂移算法（Method=1，默认的），毕竟它具有更好的伸缩力和更高的性能。

7️⃣ 不要修改核心算法。本算法内部参数较多，逻辑较为复杂，在你尚未掌握核心逻辑时，请勿修改核心代码且用于生产环境，除非通过大量细致、科学的测试验证。

8️⃣ 应用域内配置策略相同。当系统运行一段时间后，项目需要从程序指定 WorkerId 转到自动注册 WorkerId 时，请确保同一应用域内所有在用实例采用一致的配置策略，这不仅仅针对 WorkerId，也包含其他所有配置参数。

9️⃣ 管理好服务器时间。雪花算法依赖系统时间，不要手工大幅度回调操作系统时间。如果一定要调整，切记：确保服务再次启动时的系统时间大于最后一次关闭时的时间。（注：世界级或网络级的时间同步或回拨，引起的系统时间小幅度变化，对本算法没影响）



#### 6.3、配置变更

配置变更指是系统运行一段时间后，再调整运行参数（IdGeneratorOptions 选项值），请注意：

🔴 1.最重要的一条原则是：BaseTime **只能往前**（比老值更小、距离现在更远）赋值，原因是往后赋值极大可能产生相同的时间戳。[**不推荐**在系统运行之后调整 BaseTime]

🔴 2.任何时候增加 WorkerIdBitLength 或 SeqBitLength，都是可以的，但是慎用 “减小”的操作，因为这可能导致在未来某天生成的 ID 与过去老配置时相同。[允许在系统运行之后**增加**任何一个 BitLength 值]

🔴 3.如果必须减小 WorkerIdBitLength 或 SeqBitLength 其中的一项，一定要满足一个条件：新的两个 BitLength 之和要大于 老的值之和。[**不推荐**在运行之后缩小任何一个 BitLength 值]

🔴 4.上述3条规则，并未在本算法内做逻辑控制，集成方应根据上述规则做好影响评估，确认无误后，再实施配置变更。



#### 6.4、最佳实践

1. 机器码请务必唯一，我们可以使用redis自增结合nacos-config或其他方法进行自动配置。也可以使用该算法本身等特性进行k8s集成。
2. 序数位直接决定了本算法一毫秒内能够生成多少id，如果超过此数量，则会阻塞住。请务必测试好你的并发。