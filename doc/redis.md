

# 1、 NoSQL入门和概述

## 1.1 入门概述

### 1.1.1 历史 

互联网时代背景下，为什么要用NoSQL？

90年代的互联网，访问量不大，单个数据库可以应付。

#### 单机架构

![img](https://upload-images.jianshu.io/upload_images/2006966-71554859a540f036.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/690)瓶颈是：

1、数据量的总大小一个机器放不下时

2、数据的索引（B+Tree）一个机器的内存放不下时

3、访问量（混合读写）一个实例不能承受时

#### **Memcached(缓存)+MySQL+垂直拆分**

`Memcached`作为一个独立的分布式的缓存服务器，为多个web服务器提供了一个共享的高性能缓存服务，在Memcached服务器上，又发展了根据`hash`算法来进行多台`Memcached`缓存服务的扩展，然后又出现了**一致性hash**来解决增加或减少缓存服务器导致重新`hash`带来的大量缓存失效的弊端

#### **MySQL主从读写分离**

- #### 由于数据库的写入压力增加，`Memcached`只能缓解数据库的读取压力。读写集中在一个数据库上让数据库不堪重负，大部分网站开始使用主从复制技术来达到读写分离，以提高读写性能和读库的可扩展性。`MySQL`的`master-slave`模式成为这个时候的网站标配了。

  ![img](https://upload-images.jianshu.io/upload_images/2006966-331910a1093ed7b8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/690)

####  **分表分库+水平拆分+mySQL集群**

- 在`Memcached`的高速缓存，`MySQL`的主从复制，读写分离的基础之上，这时`MySQL`主库的写压力开始出现瓶颈，而数据量的持续猛增，由于`MyISAM`使用表锁，在高并发下会出现严重的锁问题，大量的高并发`MySQL`应用开始使用`InnoDB`引擎代替`MyISAM`。

- 同时，开始流行使用分表分库来缓解写压力和数据增长的扩展问题。这个时候，分表分库成了一个热门技术，是面试的热门问题也是业界讨论的热门技术问题。也就在这个时候，`MySQL`推出了还不太稳定的表分区，这也给技术实力一般的公司带来了希望。虽然`MySQL`推出了`MySQL Cluster`集群，但性能也不能很好满足互联网的要求，只是在高可靠性上提供了非常大的保证

  ![img](https://upload-images.jianshu.io/upload_images/2006966-9cdb2abca7d4d616.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/690)

#### **MySQL的扩展性瓶颈**

`MySQL`数据库也经常存储一些大文本字段，导致数据库表非常的大，在做数据库恢复的时候就导致非常的慢，不容易快速恢复数据库。比如`1000`万`4KB`大小的文本就接近`40GB`的大小，如果能把这些数据从`MySQL`省去，`MySQL`将变得非常的小。关系数据库很强大，但是它并不能很好的应付所有的应用场景。`MySQL`的扩展性差（需要复杂的技术来实现），大数据下`IO`压力大，表结构更改困难，正是当前使用`MySQL`的开发人员面临的问题。

####  **今天什么样子** ![img](https://upload-images.jianshu.io/upload_images/2006966-a01ce8562c21f455.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/690)

### 1.1.2 NoSQL

#### **NoSQL是什么？**

 `NoSQL(NoSQL = Not Only SQL )`，意即“不仅仅是SQL”，泛指非关系型的数据库。随着互联网`web2.0`网站的兴起，传统的关系数据库在应付`web2.0`网站，特别是超大规模和高并发的`SNS`类型的`web2.0`纯动态网站已经显得力不从心，暴露了很多难以克服的问题，而非关系型的数据库则由于其本身的特点得到了非常迅速的发展。`NoSQL`数据库的产生就是为了解决大规模数据集合多重数据种类带来的挑战，尤其是大数据应用难题。

#### **NoSQL能做什么？**

 `NoSQL`，有**三大重要的特性**：

- **易扩展**
- **大数据量高性能**
- **多样灵活的数据模型**

**易扩展：**NoSQL数据库种类繁多，但是一个共同的特点都是去掉关系数据库的关系型特性。数据之间无关系，这样就非常容易扩展。也无形之间，在架构的层面上带来了可扩展的能力。

**大数据量高性能：**NoSQL数据库都具有非常高的读写性能，尤其在大数据量下，同样表现优秀。这得益于它的无关系性，数据库的结构简单。一般MySQL使用`Query Cache`，每次表的更新`Cache`就失效，是一种大粒度的`Cache`，在针对`web2.0`的交互频繁的应用，`Cache`性能不高。而`NoSQL`的`Cache`是记录级的，是一种细粒度的`Cache`，所以`NoSQL`在这个层面上来说就要性能高很多了

**多样灵活的数据模型：**NoSQL无需事先为要存储的数据建立字段，随时可以存储自定义的数据格式。而在关系数据库里，增删字段是一件非常麻烦的事情。如果是非常大数据量的表，增加字段简直就是一个噩梦

#### **NoSQL的3V+3高**

写8万/s，读11万/s

**大数据时代的3V：**

- **海量Volume**
- **多样Variety**
- **实时Velocity**

 `Volume、Variety、Velocity`。这`3V`表明大数据的三方面特质：量大、多样、实时。对，不光是数据量大了。对TB、PB数据级的处理，已经成为基本配置。还能处理多样性的数据类型，结构化数据和非结构化数据，能处理Web数据，能处理语音数据甚至是图像、视频数据。实时。以前的决策支持时代，可以用批量处理的方式，隔夜处理数据，等决策者第二天上班，可以看到昨天的经营数据。但现在的互联网时代，业务在24小时不间断运营，决策已经不是第二天上班才做出，而是在客户每次浏览页面，每次下订单的过程中都存在，都会需要对用户进行实时的推荐，决策已经变得实时。

 互联网需求的3高

- **高并发**
- **高可扩**
- **高性能**

 ## 1.2 NoSQL数据模型

### 1.2.1 聚合模型

- KV
- Bson
- 列族
- 图形

### 1.2.2 NoSQL数据库的四大分类

- KV键值：典型介绍
  - 新浪：BerkeleyDB+redis
  - 美团：redis+tair
  - 阿里、百度：memcache+redis
- 文档型数据库（bson格式比较多）：典型介绍
  - MongoDB：是一个基于分布式文件存储的数据库，是一款介于关系型数据库和非关系型数据库之间的产品
  - CouchDB
- 列存储数据库
  - Cassandra，HBase
  - 分布式文件系统
- 图关系数据库
  - 并非放图形，放的是关系，如：朋友圈社交网络、广告推荐系统、社交网络、推荐系统等，专注于构建关系图谱
  - Neo4J，InfoGrid
- 四者对比

 ## 1.3 分布式数据库CAP原理

### 1.3.1 传统的ACID

传统数据库事务的四大属性：

ACID，指数据库事务正确执行的四个基本要素的缩写。包含：原子性（Atomicity）、一致性（Consistency）、隔离性（Isolation）、持久性（Durability）。

### 1.3.2 CAP

- C：Consistency 强一致性
- A：Availability 可用性
- P：Partition tolerance 分区容错性

 只能3选2

### 1.3.3 CAP的3进2

**CAP理论的核心**：

​	一个分布式系统不可能同时很好的满足一致性，可用性和分区容错性这三个需求。最多只能同时较好的满足两个。NoSQL分成了满足CA、CP、AP原则的三大类：

- CA - 单点集群，满足一致性、可用性的系统，在可扩展性上不够强大

  CA 传统Oracle数据库

- CP - 满足一致性、分区容错性的系统，通常性能不是特别高

  CP Redis、Mongodb

- AP - 最重要，满足可用性、分区容错性的系统，通常可能对一致性要求低一些

  AP 大多数网站架构的选择

**数据库的写实时性和读实时性需求** 　　

对关系数据库来说，插入一条数据之后立刻查询，是肯定可以读出来这条数据的，但是对于很多web应用来说，并不要求这么高的实时性，比方说发一条消息之 后，过几秒乃至十几秒之后，我的订阅者才看到这条动态是完全可以接受的。 

### 1.3.4 BASE

BASE就是为了解决关系数据库强一致性引起的问题而引起的可用性降低而提出的解决方案。

BASE其实是下面三个术语的缩写：
    基本可用（Basically Available）
    软状态（Soft state）
    最终一致（Eventually consistent）

它的思想是通过让系统放松对某一时刻数据一致性的要求来换取系统整体伸缩性和性能上改观。为什么这么说呢，缘由就在于大型系统往往由于地域分布和极高性能的要求，不可能采用分布式事务来完成这些指标，要想获得这些指标，我们必须采用另外一种方式来完成，这里BASE就是解决这个问题的办法

 ### 1.3.5 分布式+集群

简单来讲：
1、分布式：不同的多台服务器上面部署**不同的服务模块**（工程），他们之间通过Rpc/Rmi之间通信和调用，对外提供服务和组内协作。


2、集群：不同的多台服务器上面部署**相同的服务模块**，通过分布式调度软件进行统一的调度，对外提供服务和访问。

 # 2、Redis入门

## 2.1 Redis是什么

Redis:REmote DIctionary Server(远程字典服务器)

Redis是完全开源免费的，用C语言编写的，遵守BSD协议， 是一个高性能的(key/value)分布式内存数据库，基于内存运行并支持持久化的NoSQL数据库，是当前最热门的NoSql数据库之一, 也被人们称为数据结构服务器

Redis 与其他 key - value 缓存产品有以下三个特点

- Redis支持数据的**持久化**，可以将内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用
- Redis不仅仅支持简单的key-value类型的数据，同时还提供**list，set，zset，hash等数据结构**的存储
- Redis支持数据的备份，即master-slave模式的数据备份

 ## 2.2 Redis可以做什么？

1、内存存储和持久化：redis支持**异步**将内存中的数据写到**硬盘**上，同时不影响继续服务

2、取最新N个数据的操作，如：可以将最新的10条评论的ID放在Redis的List集合里面

3、模拟类似于HttpSession这种需要设定过期时间的功能

4、发布、订阅消息系统

5、定时器、计数器

## 2.3 Hello World

redis安装并启动`redis-server /path/to/redis.conf`完成后

`redis-cli -p 6379`

此时终端会变成：`127.0.0.1:6379> `

输入:`set k1 "hello world"` 

取值：`get k1`，此时会返回`"hello world"`

## 2.4 Redis启动后杂项基础知识

- 单进程

单进程模型来处理客户端的请求。对读写等事件的响应是通过对epoll函数的包装来做到的。Redis的实际处理速度完全依靠主进程的执行效率

epoll是Linux内核为处理大批量文件描述符而作了改进的epoll，是Linux下多路复用IO接口select/poll的增强版本，它能显著提高程序在大量并发连接中只有少量活跃的情况下的系统CPU利用率。

- 默认16个数据库，类似数组下表从零开始，初始默认使用零号库
- select命令切换数据库
- dbsize查看当前数据库的key的数量
- flushdb：清空当前库
- flushall；通杀全部库
- 统一密码管理，16个库都是同样密码，要么都OK要么一个也连接不上
- Redis索引都是从零开始
- 为什么默认端口是6379

# 3、Redis的数据类型

- string（字符串）

  string是redis最基本的类型，你可以理解成与Memcached一模一样的类型，一个key对应一个value。


  string类型是**二进制安全(Binary-safe)**的。意思是redis的string可以包含任何数据。比如jpg图片或者序列化的对象 。


  string类型是Redis最基本的数据类型，一个redis中字符串value最多可以是512M。

- hash（哈希，类似java里的Map）

  Redis hash 是一个键值对集合。
  Redis hash是一个string类型的field和value的映射表，hash特别适合用于存储对象。


  类似Java里面的Map<String,Object>。

- list（列表）

  Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）。
  它的底层实际是个链表。

- set（集合）

  Redis的Set是string类型的无序集合。它是通过HashTable实现实现的。

- zset(sorted set：有序集合)

  Redis zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。
  不同的是每个元素都会关联一个double类型的分数。
  redis正是通过分数来为集合中的成员进行从小到大的排序。zset的成员是唯一的,但分数(score)却可以重复。

  

哪里去获得redis常见数据类型操作命令: http://redisdoc.com/

## 3.1 key关键字 

 exists key的名字，判断某个key是否存在
 keys *
 move key db   --->当前库就没有了，被移除了
 expire key 秒钟：为给定的key设置过期时间
 ttl key 查看还有多少秒过期，-1表示永不过期，-2表示已过期，ttl即time to leave
 type key 查看你的key是什么类型

## 3.2 String

性质：单值单value 

**基本命令：**

- `set/get/del/append/strlen`
- `Incr/decr/incrby/decrby`,一定要是数字才能进行加减
- getrange/setrange 字符串截取或重新设置值，用法：`getrange k1 0 1`，`setrange k1 0 xxx`
- setex(set with expire)键秒值，用法：`setex k1 10 v1`/setnx(set if not exist) 用法：`setnx k1 v1`
- mset/mget/msetnx : 多个设值、取值，用法：`mset k1 v1 k2 v2`，`mget k1 k2`，`msetnx k1 v1 k2 v2` 注意，msetnx如果有一条不成功，全部不成功
- getset(先get再set)

## 3.3 List

性质：单值多value

**基本命令：**

- lpush/rpush/lrange 用法：`lpush list01 1 2 3 4 5` ，将值存入list01；lrange可以从list01中取值，`lrange list01 0 -1`；`rpush list02 1 2 3 4 5` 与lpush的区别，lpush像栈，rpush像队列
- lindex，按照索引下标获得元素(从上到下)，用法：`lindex list01 1`
- lpop/rpop 推出顶部元素，lpop以索引小的为顶，rpop以索引大的为顶
- llen 获取元素的长度
- lrem key 删N个value，用法：`lrem list03 2 3` 删除list03中的两个3
- ltrim key 开始index 结束index，截取指定范围的值后再赋值给key，用法：`ltrim list01 0 -1` ，截取list01中的内容后再赋值给list01
- rpoplpush 源列表 目的列表，将源屁股处的元素压到目的的头处
- linsert key  before/after 值1 值2，将某个值插入key的前或后
- lset key index value 在指定位置插入值

**总结：**

> 它是一个字符串链表，left、right都可以插入添加；
> 如果键不存在，创建新的链表；
> 如果键已存在，新增内容；
> 如果值全移除，对应的键也就消失了。
> 链表的操作无论是头和尾效率都极高，但假如是对中间元素进行操作，效率就很惨淡了。

## 3.4 Set

性质：单值多value

**基本命令：**

- sadd/smembers/sismember：`sadd set01 1 1 2 2 3 4`，向set01中存值；`smembers set01 ` 列出set01中所有的数据；`sismember x` 判断 `member` 元素是否集合 `key` 的成员 
- srandmember key 某个整数(随机出几个数) `srandmember set01 3`
- spop key 随机出栈，`spop set01`
- scard，获取集合里面的元素个数
- srem key value 删除集合中元素
- smove key1 key2 在key1里某个值   作用是将key1里的某个值赋给key2
- 数学集合类
  - 差集：sdiff，在第一个set中，不在后面任何一个set中 `sdiff  set01 set02`
  - 交集：sinter
  - 并集：sunion

## 3.5 Hash

重要！很重要！非常重要！

性质：KV模式不变，但V是一个键值对

- **hset/hget/hmset/hmget/hgetall/hdel**
- hexists key 在key里面的某个值的key
- hincrby/hincrbyfloat 用法：`hincrby customer age 2`
- hkeys/hvals 列出所有的key或value
- hsetnx
- hlen

## 3.6 Zset(sorted set)

在set基础上，加一个score值。
之前set是`k1 v1 v2 v3`，
现在zset是`k1 score1 v1 score2 v2`

典型应用场景：游戏中的排名

**基本命令：**

- zadd/zrange
  - withscores
- zrevrangebyscore  key 结束score 开始score
- zrevrange
- zrangebyscore key 开始score 结束score  加 (   不包含
  - limit 作用是返回限制 limit 开始下标步 多少步
- zrem key 某score下对应的value值，作用是删除元素
- zrevrank key values值，作用是逆序获得下标值
- zcard/zcount key score区间/zrank key values值，作用是获得下标值/zscore key 对应值,获得分数

# 4、配置文件redis.conf

## 4.1 位置

安装目录下redis.conf

在linux下开发，默认配置文件先不要修改，要先备份

## 4.2 units单位

对Redis来说，k与kb不同

> 1k => 1000 bytes
> 1kb => 1024 bytes
> 1m => 1000000 bytes
> 1mb => 1024*1024 bytes
> 1g => 1000000000 bytes
> 1gb => 102410241024 bytes
> units are case insensitive so 1GB 1Gb 1gB are all the same.

