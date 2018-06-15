

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

## 4.3 INCLUDES包含

可以通过includes包含，使redis.conf作为总闸，包含其他配置文件

## 4.4 GENERAL通用

- daemonize 是否后台运行

- pidfile 存放pid的文件

- port

- tcp-backlog

  设置tcp的backlog，backlog其实是一个连接队列，backlog队列总和=未完成三次握手队列 + 已经完成三次握手队列。
  在高并发环境下你需要一个高backlog值来避免慢客户端连接问题。注意Linux内核会将这个值减小到/proc/sys/net/core/somaxconn的值，所以需要确认增大somaxconn和tcp_max_syn_backlog两个值，来达到想要的效果

- timeout 超时时间

- bind 端口的绑定

- tcp-keepalive ：单位为秒，如果设置为0，则不会进行Keepalive检测，建议设置成60 

- loglevel  日志级别，类比log4j

  > Specify the server verbosity level.
  >
  > This can be one of:
  >
  > debug (a lot of information, useful for development/testing)
  >
  > verbose (many rarely useful info, but not a mess like the debug level)
  >
  > notice (moderately verbose, what you want in production probably)
  >
  > warning (only very important / critical messages are logged)

- logfile 日志文件位置

- syslog-enabled 是否把日志输出到syslog中，默认关

- syslog-ident 指定syslog里的日志标志

- syslog-facility 指定syslog设备，值可以是USER或LOCAL0-LOCAL7

- databases 默认有16个库

## 4.5 SNAPSHOTTING快照

## 4.6 REPLICATION复制

## 4.7 SECURITY安全

​	访问密码的查看、设置和取消

## 4.8 LIMITS限制

4.0.9版本分为CLIENTS和MEMORY MANAGEMENT等配置

- maxmemory

- maxclients

- maxmemory-policy

  缓存过期策略：

  - volatile-lru -> remove the key with an expire set using an LRU algorithm
  - allkeys-lru -> remove any key according to the LRU algorithm（最近最少使用算法）
  - volatile-random -> remove a random key with an expire set
  - allkeys-random -> remove a random key, any key
  - volatile-ttl -> remove the key with the nearest expire time (minor TTL)
  - noeviction -> don't expire at all, just return an error on write operations

- maxmemory-samples

  设置样本数量，LRU算法和最小TTL算法都并非是精确的算法，而是估算值，所以你可以设置样本的大小，redis默认会检查这么多个key并选择其中LRU的那个

## 4.9 总结：常见配置redis.conf介绍

参数说明
redis.conf 配置项说明如下：

1. Redis默认不是以守护进程的方式运行，可以通过该配置项修改，使用yes启用守护进程
    daemonize no
2. 当Redis以守护进程方式运行时，Redis默认会把pid写入/var/run/redis.pid文件，可以通过pidfile指定
    pidfile /var/run/redis.pid
3. 指定Redis监听端口，默认端口为6379，作者在自己的一篇博文中解释了为什么选用6379作为默认端口，因为6379在手机按键上MERZ对应的号码，而MERZ取自意大利歌女Alessia Merz的名字
    port 6379
4. 绑定的主机地址
    bind 127.0.0.1
5. 当客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能
      timeout 300
6. 指定日志记录级别，Redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose
    loglevel verbose
7. 日志记录方式，默认为标准输出，如果配置Redis为守护进程方式运行，而这里又配置为日志记录方式为标准输出，则日志将会发送给/dev/null
    logfile stdout
8. 设置数据库的数量，默认数据库为0，可以使用SELECT <dbid>命令在连接上指定数据库id
    databases 16
9. 指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合
    save <seconds> <changes>
    Redis默认配置文件中提供了三个条件：
    save 900 1
    save 300 10
    save 60 10000
    分别表示900秒（15分钟）内有1个更改，300秒（5分钟）内有10个更改以及60秒内有10000个更改。
10. 指定存储至本地数据库时是否压缩数据，默认为yes，Redis采用LZF压缩，如果为了节省CPU时间，可以关闭该选项，但会导致数据库文件变的巨大
   rdbcompression yes
11. 指定本地数据库文件名，默认值为dump.rdb
    dbfilename dump.rdb
12. 指定本地数据库存放目录
    dir ./
13. 设置当本机为slav服务时，设置master服务的IP地址及端口，在Redis启动时，它会自动从master进行数据同步
    slaveof <masterip> <masterport>
14. 当master服务设置了密码保护时，slav服务连接master的密码
    masterauth <master-password>
15. 设置Redis连接密码，如果配置了连接密码，客户端在连接Redis时需要通过AUTH <password>命令提供密码，默认关闭
    requirepass foobared
16. 设置同一时间最大客户端连接数，默认无限制，Redis可以同时打开的客户端连接数为Redis进程可以打开的最大文件描述符数，如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，Redis会关闭新的连接并向客户端返回max number of clients reached错误信息
    maxclients 128
17. 指定Redis最大内存限制，Redis在启动时会把数据加载到内存中，达到最大内存后，Redis会先尝试清除已到期或即将到期的Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis新的vm机制，会把Key存放内存，Value会存放在swap区
    maxmemory <bytes>
18. 指定是否在每次更新操作后进行日志记录，Redis在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为 redis本身同步数据文件是按上面save条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认为no
    appendonly no
19. 指定更新日志文件名，默认为appendonly.aof
     appendfilename appendonly.aof
20. 指定更新日志条件，共有3个可选值： 
    no：表示等操作系统进行数据缓存同步到磁盘（快） 
    always：表示每次更新操作后手动调用fsync()将数据写到磁盘（慢，安全） 
    everysec：表示每秒同步一次（折衷，默认值）
    appendfsync everysec
21. 指定是否启用虚拟内存机制，默认值为no，简单的介绍一下，VM机制将数据分页存放，由Redis将访问量较少的页即冷数据swap到磁盘上，访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析Redis的VM机制）
     vm-enabled no
22. 虚拟内存文件路径，默认值为/tmp/redis.swap，不可多个Redis实例共享
     vm-swap-file /tmp/redis.swap
23. 将所有大于vm-max-memory的数据存入虚拟内存,无论vm-max-memory设置多小,所有索引数据都是内存存储的(Redis的索引数据 就是keys),也就是说,当vm-max-memory设置为0的时候,其实是所有value都存在于磁盘。默认值为0
     vm-max-memory 0
24. Redis swap文件分成了很多的page，一个对象可以保存在多个page上面，但一个page上不能被多个对象共享，vm-page-size是要根据存储的 数据大小来设定的，作者建议如果存储很多小对象，page大小最好设置为32或者64bytes；如果存储很大大对象，则可以使用更大的page，如果不 确定，就使用默认值
     vm-page-size 32
25. 设置swap文件中的page数量，由于页表（一种表示页面空闲或使用的bitmap）是在放在内存中的，，在磁盘上每8个pages将消耗1byte的内存。
     vm-pages 134217728
26. 设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的，可能会造成比较长时间的延迟。默认值为4
     vm-max-threads 4
27. 设置在向客户端应答时，是否把较小的包合并为一个包发送，默认为开启
    glueoutputbuf yes
28. 指定在超过一定的数量或者最大的元素超过某一临界值时，采用一种特殊的哈希算法
    hash-max-zipmap-entries 64
    hash-max-zipmap-value 512
29. 指定是否激活重置哈希，默认为开启（后面在介绍Redis的哈希算法时具体介绍）
    activerehashing yes
30. 指定包含其它的配置文件，可以在同一主机上多个Redis实例之间使用同一份配置文件，而同时各个实例又拥有自己的特定配置文件
    include /path/to/local.conf

# 5、Redis持久化

## 5.1 RDB(Redis DataBase)

### 5.1.1 RDB是什么？

在指定的时间间隔内将内存中的数据集快照写入磁盘，也就是行话讲的Snapshot快照，它恢复时是将快照文件直接读到内存里

Redis会单独创建（fork）一个子进程来进行持久化，会先将数据写入到一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件。
整个过程中，主进程是不进行任何IO操作的，这就确保了极高的性能。如果需要进行大规模数据的恢复，且对于数据恢复的完整性不是非常敏感，那RDB方式要比AOF方式更加的高效。

RDB的**缺点**是最后一次持久化后的数据可能丢失。对数据精度要求不高时首推RDB。

### 5.1.2 Fork

fork的作用是复制一个与当前进程一样的进程。新进程的所有数据（变量、环境变量、程序计数器等）数值都和原进程一致，但是是一个全新的进程，并作为原进程的子进程。fork的过程在内存空间紧张时存在隐患。

**快照策略**

> In the example below the behaviour will be to save:
>
> after 900 sec (15 min) if at least 1 key changed
>
> after 300 sec (5 min) if at least 10 keys changed
>
> after 60 sec if at least 10000 keys changed

符合快照策略时，redis会保存快照文件，rdb保存的是dump.rdb文件，可以通过dbfilename进行配置。

### 5.1.3 配置文件中默认的快照配置

- save

  RDB是整个内存的压缩过的Snapshot，RDB的数据结构，可以配置复合的快照触发条件，
  默认：
  是1分钟内改了1万次，
  或5分钟内改了10次，
  或15分钟内改了1次。

  禁用：

  如果想禁用RDB持久化的策略，只要不设置任何save指令，或者给save传入一个空字符串参数也可以

  save命令可以立刻备份

- stop-writes-on-bgsave-error

  如果配置成no，表示你不在乎数据不一致或者有其他的手段发现和控制

- rdbcompression

  对于存储到磁盘中的快照，可以设置是否进行压缩存储。如果是的话，redis会采用LZF算法进行压缩。如果你不想消耗CPU来进行压缩的话，可以设置为关闭此功能。

- rdbchecksum：

  在存储快照后，还可以让redis使用CRC64算法来进行数据校验，但是这样做会增加大约
  10%的性能消耗，如果希望获取到最大的性能提升，可以关闭此功能

- dbfilename 配置rdb快照文件名

- dir

### 5.1.4 如何触发RDB快照 

命令save或者是bgsave

- Save：save时只管保存，其它不管，全部阻塞
- BGSAVE：Redis会在后台异步进行快照操作，
- 执行flushall命令，也会产生dump.rdb文件，但里面是空的，无意义

### 5.1.5 如何恢复

- 将备份文件 (dump.rdb) 移动到 redis 安装目录并启动服务即可
- CONFIG GET dir获取目录

### 5.1.6 优势

- 适合大规模的数据恢复
- 对数据完整性和一致性要求不高

### 5.1.7 劣势

- 在一定间隔时间做一次备份，所以如果redis意外down掉的话，就会丢失最后一次快照后的所有修改

- fork的时候，内存中的数据被克隆了一份，大致2倍的膨胀性需要考虑

### 5.1.8 如何停止

动态所有停止RDB保存规则的方法：`redis-cli config set save ""`

## 5.2 AOF（Append Only File）

### 5.2.1 是什么？

以**日志的形式**来记录每个写操作，将Redis执行过的所有**写指令**记录下来(读操作不记录)，
只许追加文件但不可以改写文件，redis启动之初会读取该文件重新构建数据，换言之，Redis
重启的话就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作

Aof保存的是appendonly.aof文件

### 5.2.2 配置位置APPEND ONLY MODE追加

- appendonly

  默认no

- appendfilename

  appendonly.aof

- appendfsync 同步策略

  - always：同步持久化 每次发生数据变更会被立即记录到磁盘  性能较差但数据完整性比较好
  - everysec：出厂默认推荐，异步操作，每秒记录   如果一秒内宕机，有数据丢失
  - no

- no-appendfsync-on-rewrite：重写时是否可以运用Appendfsync，用默认no即可，保证数据安全性。

- auto-aof-rewrite-percentage：设置重写的基准值

- auto-aof-rewrite-min-size：设置重写最小日志大小

### 5.2.3 启动、修复、恢复 

- 正常恢复

  - 启动：设置Yes

  ​	修改默认的appendonly no，改为yes

  - 将有数据的aof文件复制一份保存到对应目录(config get dir)
  - 恢复：重启redis然后重新加载

- 异常恢复

  - 启动：设置Yes

  ​	修改默认的appendonly no，改为yes

  - 备份被写坏的AOF文件
  - 修复：

  ​	redis-check-aof --fix进行修复

  - 恢复：重启redis然后重新加载

### 5.2.4 rewrite

#### 是什么？

AOF采用文件追加方式，文件会越来越大为避免出现此种情况，新增了重写机制，当AOF文件的大小超过所设定的阈值时，Redis就会启动AOF文件的内容压缩，只保留可以恢复数据的最小指令集.可以使用命令bgrewriteaof

#### 重写原理

AOF文件持续增长而过大时，会fork出一条新进程来将文件重写(也是先写临时文件最后再rename)，遍历新进程的内存中数据，每条记录有一条的Set语句。重写aof文件的操作，并没有读取旧的aof文件，而是将整个内存中的数据库内容用命令的方式重写了一个新的aof文件，这点和快照有点类似

#### 触发机制

Redis会记录上次重写时的AOF大小，默认配置是当AOF文件大小是上次rewrite后大小的一倍且文件大于64M时触发

### 5.2.5 优势

- 每修改同步：appendfsync always   同步持久化 每次发生数据变更会被立即记录到磁盘  性能较差但数据完整性比较好
- 每秒同步：appendfsync everysec    异步操作，每秒记录   如果一秒内宕机，有数据丢失
- 不同步：appendfsync no   从不同步

### 5.2.6 劣势

- 相同数据集的数据而言aof文件要远大于rdb文件，恢复速度慢于rdb
- aof运行效率要慢于rdb,每秒同步策略效率较好，不同步效率和rdb相同

## 5.3 Which one？

- RDB持久化方式能够在指定的时间间隔能对你的数据进行快照存储
- AOF持久化方式记录每次对服务器写的操作,当服务器重启的时候会重新执行这些命令来恢复原始的数据，AOF命令以redis协议追加保存每次写的操作到文件末尾。Redis还能对AOF文件进行后台重写，使得AOF文件的体积不至于过大
- 只做缓存：如果你只希望你的数据在服务器运行的时候存在,你也可以不使用任何持久化方式.
- 同时开启两种持久化方式
  - 在这种情况下,当redis重启的时候会优先载入AOF文件来恢复原始的数据,因为在通常情况下AOF文件保存的数据集要比RDB文件保存的数据集要完整.
  - RDB的数据不实时，同时使用两者时服务器重启也只会找AOF文件。那要不要只使用AOF呢？作者建议不要，因为RDB更适合用于备份数据库(AOF在不断变化不好备份)，
    快速重启，而且不会有AOF可能潜在的bug，留着作为一个万一的手段。

## 5.4 性能建议

因为RDB文件只用作后备用途，建议只在Slave上持久化RDB文件，而且只要15分钟备份一次就够了，只保留**`save 900 1`**这条规则。


如果Enalbe AOF，好处是在最恶劣情况下也只会丢失不超过两秒数据，启动脚本较简单只load自己的AOF文件就可以了。代价一是带来了持续的IO，二是AOF rewrite的最后将rewrite过程中产生的新数据写到新文件造成的阻塞几乎是不可避免的。只要硬盘许可，应该尽量减少AOF rewrite的频率，AOF重写的**基础大小默认值64M太小**了，可以设到5G以上。默认超过原大小100%大小时重写可以改到适当的数值。

如果不Enable AOF ，仅靠Master-Slave Replication 实现高可用性也可以。能省掉一大笔IO也减少了rewrite时带来的系统波动。代价是如果Master/Slave同时倒掉，会丢失十几分钟的数据，启动脚本也要比较两个Master/Slave中的RDB文件，载入较新的那个。新浪微博就选用了这种架构.

# 6、 事务

## 6.1 是什么？能做什么？

**是什么**

可以一次执行多个命令，本质是一组命令的集合。一个事务中的所有命令都会序列化，**按顺序地串行化执行而不会被其它命令插入**，不许加塞。

**能做什么**

一个队列中，一次性、顺序性、排他性的执行一系列命令

## 6.2 怎么玩

### 6.2.1 常用命令

![redis](https://user-images.githubusercontent.com/16509581/41401404-58fb8e94-6ff2-11e8-846e-9339fc6b2093.jpg)

### 6.2.2 不同case

- Case1 正常执行

![image](https://user-images.githubusercontent.com/16509581/41401455-80882e68-6ff2-11e8-898a-3c2527683faa.png)

- Case2 放弃执行

![image](https://user-images.githubusercontent.com/16509581/41401518-a6350d84-6ff2-11e8-862d-ff4c7455477f.png)

- Case3 全体连坐

  ![image](https://user-images.githubusercontent.com/16509581/41401673-046a9f86-6ff3-11e8-96f6-dcbdab4c8c61.png)

  在执行过程中就报错，会连坐

- Case4 冤头债主

![image](https://user-images.githubusercontent.com/16509581/41401721-215a87c8-6ff3-11e8-983a-808797f4f85e.png)

​	执行过程中没有报错，冤有头债有主

### 6.2.3 Case5 watch监控

#### 悲观锁、乐观锁、CAS

- 悲观锁

  悲观锁(Pessimistic Lock), 顾名思义，就是很悲观，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会block直到它拿到锁。传统的关系型数据库里边就用到了很多这种锁机制，比如行锁，表锁等，读锁，写锁等，都是在做操作之前先上锁

  特点：并发性差、一致性好

- 乐观锁

  乐观锁(Optimistic Lock), 顾名思义，就是很乐观，每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，**可以使用版本号等机制**。乐观锁**适用于多读**的应用类型，这样可以提高吞吐量

  乐观锁策略:<u>提交版本必须大于记录当前版本才能执行更新</u>

#### 监控过程

初始化信用卡可用余额和欠额

![image](https://user-images.githubusercontent.com/16509581/41402890-0d8e10d6-6ff6-11e8-8fdf-9ddd560f6bac.png)

无加塞篡改，先监控再开启multi， 保证两笔金额变动在同一个事务内

![image](https://user-images.githubusercontent.com/16509581/41402993-53c9f646-6ff6-11e8-937c-a50f0a5bff47.png)

有加塞篡改

![image](https://user-images.githubusercontent.com/16509581/41403083-93e3d3d2-6ff6-11e8-9866-6fc11e2071b2.png)

监控了key，如果key被修改了，后面一个事务的执行失效。此时需要**unwatch**，重新从缓存中读取数据

![image](https://user-images.githubusercontent.com/16509581/41403197-e8573ada-6ff6-11e8-8e58-03401b4e8736.png)

一旦执行了exec之前加的监控锁都会被取消掉了

## 6.3 小结

Watch指令，类似**乐观锁**，事务提交时，如果Key的值已被别的客户端改变，比如某个list已被别的客户端push/pop过了，整个事务队列都不会被执行

通过WATCH命令在事务执行之前监控了多个Keys，倘若在WATCH之后有任何Key的值发生了变化，EXEC命令执行的事务都将被放弃，同时返回Nullmulti-bulk应答以通知调用者事务执行失败

![image](https://user-images.githubusercontent.com/16509581/41403453-7d9a4dbc-6ff7-11e8-953a-7e5d76b68003.png)

![1528968233674](C:\Users\cab\AppData\Local\Temp\1528968233674.png)

# 7、消息订阅发布

redis的消息中间件功能

进程间的一种消息通信模式：发送者(pub)发送消息，订阅者(sub)接收消息。



## 订阅、发布消息图

![1528968352943](C:\Users\cab\AppData\Local\Temp\1528968352943.png)

## 基本命令

![image](https://user-images.githubusercontent.com/16509581/41403655-fa57d82e-6ff7-11e8-8848-2e7087bb0388.png)

## 案例

先订阅后发布后才能收到消息，
1 可以一次性订阅多个，SUBSCRIBE c1 c2 c3

2 消息发布，PUBLISH c2 hello-redis

3 订阅多个，通配符\*， PSUBSCRIBE new*
4 收取消息， PUBLISH new1 redis2015

# 8、Redis的复制

## 8.1 是什么

行话：也就是我们所说的主从复制，主机数据更新后根据配置和策略， 自动同步到备机的master/slaver机制，Master以写为主，Slave以读为主

## 8.2 能干嘛

读写分离
容灾恢复

## 8.3 怎么玩

1、配从（库）不配主（库）

2、从库配置`slaveof 主库IP 主库端口`

​	每次与master断开之后，都需要重新连接，除非你配置进redis.conf文件
	Info replication

3、修改配置文件细节操作

- 拷贝多个redis.conf文件
- 开启daemonize yes
- Pid文件名字
- 指定端口
- Log文件名字
- Dump.rdb名字

4、常用3招

- 一主二仆

  - init

    ```shell
    info replication
    
    slaveof ip:port
    ```

  ![image](https://user-images.githubusercontent.com/16509581/41409783-f90a58e0-7009-11e8-957d-1c6c00b3c884.png)

  - 一个Master两个Slave

    ![image](https://user-images.githubusercontent.com/16509581/41409931-78597afe-700a-11e8-9f5b-9c82e99ab0fa.png)

  - 主从问题：

    1 切入点问题？slave1、slave2是从头开始复制还是从切入点开始复制?比如从k4进来，那之前的123是否也可以复制

    ​	可以，数据全部复制

    2 从机是否可以写？set可否？

    ​	从机不可以写操作

    3 主机shutdown后情况如何？从机是上位还是原地待命

    ​	从机原地待命

    4 主机又回来了后，主机新增记录，从机还能否顺利复制？

    ​	顺利复制

    5 其中一台从机down后情况如何？依照原有它能跟上大部队吗？

    ​	从机会变成master；想要跟上大部队，`slaveof ip:port`即可

- 薪火相传

  - 上一个Slave可以是下一个slave的Master，Slave同样可以接收其他 slaves的连接和同步请求，那么该slave作为了链条中下一个的master, 可以有效减轻master的写压力
  - 中途变更转向:会清除之前的数据，重新建立拷贝最新的
  - Slaveof 新主库IP 新主库端口

- 反客为主

  `SLAVEOF no one`  使当前数据库停止与其他数据库的同步，转成主数据库

## 8.4 复制原理

- Slave启动成功连接到master后会发送一个sync命令
- Master接到命令启动后台的存盘进程，同时收集所有接收到的用于修改数据集命令， 在后台进程执行完毕之后，master将传送整个数据文件到slave,以完成一次完全同步
- 全量复制：而slave服务在接收到数据库文件数据后，将其存盘并加载到内存中。
- 增量复制：Master继续将新的所有收集到的修改命令依次传给slave,完成同步
- 但是只要是重新连接master,一次完全同步（全量复制)将被自动执行

## 8.5 哨兵模式（sentinel）

### 8.5.1 是什么？

反客为主的自动版，能够后台监控主机是否故障，如果故障了根据投票数自动将从库转换为主库

### 8.5.2 怎么玩（使用步骤）

- 自定义的/myredis目录下新建sentinel.conf文件，名字绝不能错
- 配置哨兵,填写内容
  - `sentinel monitor 被监控数据库名字(自己起名字) 127.0.0.1 6379 1`
  - 上面最后一个数字1，表示主机挂掉后salve投票看让谁接替成为主机，得票数多少后成为主机
- 启动哨兵
  - `Redis-sentinel /myredis/sentinel.conf `
  - 上述目录依照各自的实际情况配置，可能目录不同
  - 如果原有的master挂了，会投票选出新master
- 如果之前的master重启回来，会变成slave

### 8.5.3 多监控 

一组sentinel能同时监控多个Master

## 8.6 复制的缺点

复制延时

由于所有的写操作都是先在Master上操作，然后同步更新到Slave上，所以从Master同步到Slave机器有一定的延迟，当系统很繁忙的时候，延迟问题会更加严重，Slave机器数量的增加也会使这个问题更加严重。



# 9、Jedis

## 9.1 测试连通性

如果redis无法远程连接，修改配置文件

- **注释掉`bind 127.0.0.1`，并加入`bind 0.0.0.0`**
- **修改配置`protected-mode no`** 

```java
Jedis jedis = new Jedis("39.104.114.86", 6379);
System.out.println(jedis.ping());
```

返回pong表示连接成功

## 9.2 常用api

```java
 Jedis jedis = new Jedis("127.0.0.1",6379);
     //key
     Set<String> keys = jedis.keys("*");
     for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
       String key = (String) iterator.next();
       System.out.println(key);
     }
     System.out.println("jedis.exists====>"+jedis.exists("k2"));
     System.out.println(jedis.ttl("k1"));
     //String
     //jedis.append("k1","myreids");
     System.out.println(jedis.get("k1"));
     jedis.set("k4","k4_redis");
     System.out.println("----------------------------------------");
     jedis.mset("str1","v1","str2","v2","str3","v3");
     System.out.println(jedis.mget("str1","str2","str3"));
     //list
     System.out.println("----------------------------------------");
     //jedis.lpush("mylist","v1","v2","v3","v4","v5");
     List<String> list = jedis.lrange("mylist",0,-1);
     for (String element : list) {
       System.out.println(element);
     }
     //set
     jedis.sadd("orders","jd001");
     jedis.sadd("orders","jd002");
     jedis.sadd("orders","jd003");
     Set<String> set1 = jedis.smembers("orders");
     for (Iterator iterator = set1.iterator(); iterator.hasNext();) {
       String string = (String) iterator.next();
       System.out.println(string);
     }
     jedis.srem("orders","jd002");
     System.out.println(jedis.smembers("orders").size());
     //hash
     jedis.hset("hash1","userName","lisi");
     System.out.println(jedis.hget("hash1","userName"));
     Map<String,String> map = new HashMap<String,String>();
     map.put("telphone","13811814763");
     map.put("address","atguigu");
     map.put("email","abc@163.com");
     jedis.hmset("hash2",map);
     List<String> result = jedis.hmget("hash2", "telphone","email");
     for (String element : result) {
       System.out.println(element);
     }
     //zset
     jedis.zadd("zset01",60d,"v1");
     jedis.zadd("zset01",70d,"v2");
     jedis.zadd("zset01",80d,"v3");
     jedis.zadd("zset01",90d,"v4");
     
     Set<String> s1 = jedis.zrange("zset01",0,-1);
     for (Iterator iterator = s1.iterator(); iterator.hasNext();) {
       String string = (String) iterator.next();
       System.out.println(string);

```

## 9.3 事务

### 9.3.1 日常

```java
    Jedis jedis = new Jedis("127.0.0.1",6379);
     
     //监控key，如果该动了事务就被放弃
     /*3
     jedis.watch("serialNum");
     jedis.set("serialNum","s#####################");
     jedis.unwatch();*/
     
     Transaction transaction = jedis.multi();//被当作一个命令进行执行
     Response<String> response = transaction.get("serialNum");
     transaction.set("serialNum","s002");
     response = transaction.get("serialNum");
     transaction.lpush("list3","a");
     transaction.lpush("list3","b");
     transaction.lpush("list3","c");
     
     transaction.exec();
     //2 transaction.discard();
     System.out.println("serialNum***********"+response.get());

```

### 9.3.2 加锁

```java
public class TestTransaction {
 
  public boolean transMethod() {
     Jedis jedis = new Jedis("127.0.0.1", 6379);
     int balance;// 可用余额
     int debt;// 欠额
     int amtToSubtract = 10;// 实刷额度
 
     jedis.watch("balance");
     //jedis.set("balance","5");//此句不该出现，讲课方便。模拟其他程序已经修改了该条目
     balance = Integer.parseInt(jedis.get("balance"));
     if (balance < amtToSubtract) {
       jedis.unwatch();
       System.out.println("modify");
       return false;
     } else {
       System.out.println("***********transaction");
       Transaction transaction = jedis.multi();
       transaction.decrBy("balance", amtToSubtract);
       transaction.incrBy("debt", amtToSubtract);
       transaction.exec();
       balance = Integer.parseInt(jedis.get("balance"));
       debt = Integer.parseInt(jedis.get("debt"));
 
       System.out.println("*******" + balance);
       System.out.println("*******" + debt);
       return true;
     }
  }
 
  /**
   * 通俗点讲，watch命令就是标记一个键，如果标记了一个键， 在提交事务前如果该键被别人修改过，那事务就会失败，这种情况通常可以在程序中
   * 重新再尝试一次。
   * 首先标记了键balance，然后检查余额是否足够，不足就取消标记，并不做扣减； 足够的话，就启动事务进行更新操作，
   * 如果在此期间键balance被其它人修改， 那在提交事务（执行exec）时就会报错， 程序中通常可以捕获这类错误再重新执行一次，直到成功。
   */
  public static void main(String[] args) {
     TestTransaction test = new TestTransaction();
     boolean retValue = test.transMethod();
     System.out.println("main retValue-------: " + retValue);
  }
}
 
```

## 9.4 主从复制

```java
public static void main(String[] args) throws InterruptedException 
  {
     Jedis jedis_M = new Jedis("127.0.0.1",6379);
     Jedis jedis_S = new Jedis("127.0.0.1",6380);
     
     jedis_S.slaveof("127.0.0.1",6379);
     
     jedis_M.set("k6","v6");
     Thread.sleep(500);
     System.out.println(jedis_S.get("k6"));
  }
```

 ## 9.5 JedisPool

Pool要想到单例

- 获取Jedis实例需要从JedisPool中获取
- 用完Jedis实例需要返还给JedisPool
- 如果Jedis在使用过程中出错，则也需要还给JedisPool

JedisPoolUtil

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 
public class JedisPoolUtil {
  
 private static volatile JedisPool jedisPool = null;//被volatile修饰的变量不会被本地线程缓存，对该变量的读写都是直接操作共享内存。
  
  private JedisPoolUtil() {}
  
  public static JedisPool getJedisPoolInstance()
 {
     if(null == jedisPool)
    {
       synchronized (JedisPoolUtil.class)
      {
          if(null == jedisPool)
         {
           JedisPoolConfig poolConfig = new JedisPoolConfig();
           poolConfig.setMaxActive(1000);
           poolConfig.setMaxIdle(32);
           poolConfig.setMaxWait(100*1000);
           poolConfig.setTestOnBorrow(true);
            
            jedisPool = new JedisPool(poolConfig,"127.0.0.1");
         }
      }
    }
     return jedisPool;
 }
  
  public static void release(JedisPool jedisPool,Jedis jedis)
 {
     if(null != jedis)
    {
      jedisPool.returnResourceObject(jedis);
    }
 }
}
 
```

### 配置总结

> JedisPool的配置参数大部分是由JedisPoolConfig的对应项来赋值的。
>
> maxActive：控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted。
> maxIdle：控制一个pool最多有多少个状态为idle(空闲)的jedis实例；
> whenExhaustedAction：表示当pool中的jedis实例都被allocated完时，pool要采取的操作；默认有三种。
>  WHEN_EXHAUSTED_FAIL --> 表示无jedis实例时，直接抛出NoSuchElementException；
>  WHEN_EXHAUSTED_BLOCK --> 则表示阻塞住，或者达到maxWait时抛出JedisConnectionException；
>  WHEN_EXHAUSTED_GROW --> 则表示新建一个jedis实例，也就说设置的maxActive无用；
> maxWait：表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛JedisConnectionException；
> testOnBorrow：获得一个jedis实例的时候是否检查连接可用性（ping()）；如果为true，则得到的jedis实例均是可用的；
>
> testOnReturn：return 一个jedis实例给pool时，是否检查连接可用性（ping()）；
>
> testWhileIdle：如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
>
> timeBetweenEvictionRunsMillis：表示idle object evitor两次扫描之间要sleep的毫秒数；
>
> numTestsPerEvictionRun：表示idle object evitor每次扫描的最多的对象数；
>
> minEvictableIdleTimeMillis：表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
>
> softMinEvictableIdleTimeMillis：在minEvictableIdleTimeMillis基础上，加入了至少minIdle个对象已经在pool里面了。如果为-1，evicted不会根据idle time驱逐任何对象。如果minEvictableIdleTimeMillis>0，则此项设置无意义，且只有在timeBetweenEvictionRunsMillis大于0时才有意义；
>
> lifo：borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；
>
> ==================================================================================================================
> 其中JedisPoolConfig对一些参数的默认设置如下：
> testWhileIdle=true
> minEvictableIdleTimeMills=60000
> timeBetweenEvictionRunsMillis=30000
> numTestsPerEvictionRun=-1