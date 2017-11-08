项目目前里面提交的是redis和springboot项目的初步配置和简单实用，后续会增加spring cache的讲解

以下记录的是项目中未讲解的部分内容：

1、Redis 可以存储键与5种不同数据结构类型之间的映射，这5种数据结构类型分别为String（字符串）、List（列表）、Set（集合）、Hash（散列）和 Zset（有序集合）RedisTemplate中定义了对五种数据结构操作：<br>
  redisTemplate.opsForValue();//操作字符串<br>
  redisTemplate.opsForHash();//操作hash<br>
  redisTemplate.opsForList();//操作list<br>
  redisTemplate.opsForSet();//操作set<br>
  redisTemplate.opsForZSet();//操作有序set<br>

2、Redis持久化（此处借鉴了别人整理的很赞）
提供了多种不同级别的持久化方式:一种是RDB,另一种是AOF.
RDB 持久化可以在指定的时间间隔内生成数据集的时间点快照（point-in-time snapshot）。
AOF 持久化记录服务器执行的所有写操作命令，并在服务器启动时，通过重新执行这些命令来还原数据集。 AOF 文件中的命令全部以 Redis 协议的格式来保存，新命令会被追加到文件的末尾。 Redis 还可以在后台对 AOF 文件进行重写（rewrite），使得 AOF 文件的体积不会超出保存数据集状态所需的实际大小。Redis 还可以同时使用 AOF 持久化和 RDB 持久化。 在这种情况下， 当 Redis 重启时， 它会优先使用 AOF 文件来还原数据集， 因为 AOF 文件保存的数据集通常比 RDB 文件所保存的数据集更完整。你甚至可以关闭持久化功能，让数据只在服务器运行时存在。
了解 RDB 持久化和 AOF 持久化之间的异同是非常重要的， 以下几个小节将详细地介绍这这两种持久化功能， 并对它们的相同和不同之处进行说明。

RDB 快照:
在默认情况下， Redis 将数据库快照保存在名字为 dump.rdb 的二进制文件中。你可以对 Redis 进行设置， 让它在“ N 秒内数据集至少有 M 个改动”这一条件被满足时， 自动保存一次数据集。你也可以通过调用 SAVE 或者 BGSAVE ， 手动让 Redis 进行数据集保存操作。比如说， 以下设置会让 Redis 在满足“ 60 秒内有至少有 1000 个键被改动”这一条件时， 自动保存一次数据集：
save 60 1000
\<br>这种持久化方式被称为快照（snapshot）。


AOF 重写:
因为 AOF 的运作方式是不断地将命令追加到文件的末尾， 所以随着写入命令的不断增加， AOF 文件的体积也会变得越来越大。举个例子， 如果你对一个计数器调用了100 次 INCR ， 那么仅仅是为了保存这个计数器的当前值， AOF 文件就需要使用 100 条记录（entry）。然而在实际上， 只使用一条 SET 命令已经足以保存计数器的当前值了， 其余 99 条记录实际上都是多余的。为了处理这种情况， Redis 支持一种有趣的特性： 可以在不打断服务客户端的情况下， 对 AOF 文件进行重建（rebuild）。执行 BGREWRITEAOF 命令， Redis 将生成一个新的 AOF 文件， 这个文件包含重建当前数据集所需的最少命令。

RDB 的优点:
RDB 是一个非常紧凑（compact）的文件，它保存了 Redis 在某个时间点上的数据集。 这种文件非常适合用于进行备份： 比如说，你可以在最近的 24 小时内，每小时备份一次 RDB 文件，并且在每个月的每一天，也备份一个 RDB 文件。 这样的话，即使遇上问题，也可以随时将数据集还原到不同的版本。RDB 非常适用于灾难恢复（disaster recovery）：它只有一个文件，并且内容都非常紧凑，可以（在加密后）将它传送到别的数据中心，或者亚马逊 S3 中。RDB 可以最大化 Redis 的性能：父进程在保存 RDB 文件时唯一要做的就是 fork 出一个子进程，然后这个子进程就会处理接下来的所有保存工作，父进程无须执行任何磁盘 I/O 操作。RDB 在恢复大数据集时的速度比 AOF 的恢复速度要快。

RDB 的缺点:
如果你需要尽量避免在服务器故障时丢失数据，那么 RDB 不适合你。 虽然 Redis 允许你设置不同的保存点（save point）来控制保存 RDB 文件的频率， 但是， 因为RDB 文件需要保存整个数据集的状态， 所以它并不是一个轻松的操作。 因此你可能会至少 5 分钟才保存一次 RDB 文件。 在这种情况下， 一旦发生故障停机， 你就可能会丢失好几分钟的数据。每次保存 RDB 的时候，Redis 都要 fork() 出一个子进程，并由子进程来进行实际的持久化工作。 在数据集比较庞大时， fork() 可能会非常耗时，造成服务器在某某毫秒内停止处理客户端； 如果数据集非常巨大，并且 CPU 时间非常紧张的话，那么这种停止时间甚至可能会长达整整一秒。 虽然 AOF 重写也需要进行 fork() ，但无论 AOF 重写的执行间隔有多长，数据的耐久性都不会有任何损失。

AOF 的优点:
使用 AOF 持久化会让 Redis 变得非常耐久（much more durable）：你可以设置不同的 fsync 策略，比如无 fsync ，每秒钟一次 fsync ，或者每次执行写入命令时 fsync 。 AOF 的默认策略为每秒钟 fsync 一次，在这种配置下，Redis 仍然可以保持良好的性能，并且就算发生故障停机，也最多只会丢失一秒钟的数据（ fsync 会在后台线程执行，所以主线程可以继续努力地处理命令请求）。AOF 文件是一个只进行追加操作的日志文件（append only log）， 因此对 AOF 文件的写入不需要进行 seek ， 即使日志因为某些原因而包含了未写入完整的命令（比如写入时磁盘已满，写入中途停机，等等）， redis-check-aof 工具也可以轻易地修复这种问题。
Redis 可以在 AOF 文件体积变得过大时，自动地在后台对 AOF 进行重写： 重写后的新 AOF 文件包含了恢复当前数据集所需的最小命令集合。 整个重写操作是绝对安全的，因为 Redis 在创建新 AOF 文件的过程中，会继续将命令追加到现有的 AOF 文件里面，即使重写过程中发生停机，现有的 AOF 文件也不会丢失。 而一旦新 AOF 文件创建完毕，Redis 就会从旧 AOF 文件切换到新 AOF 文件，并开始对新 AOF 文件进行追加操作。AOF 文件有序地保存了对数据库执行的所有写入操作， 这些写入操作以 Redis 协议的格式保存， 因此 AOF 文件的内容非常容易被人读懂， 对文件进行分析（parse）也很轻松。 导出（export） AOF 文件也非常简单： 举个例子， 如果你不小心执行了 FLUSHALL 命令， 但只要 AOF 文件未被重写， 那么只要停止服务器， 移除 AOF 文件末尾的 FLUSHALL 命令， 并重启 Redis ， 就可以将数据集恢复到 FLUSHALL 执行之前的状态。

AOF 的缺点:
对于相同的数据集来说，AOF 文件的体积通常要大于 RDB 文件的体积。根据所使用的 fsync 策略，AOF 的速度可能会慢于 RDB 。 在一般情况下， 每秒 fsync 的性能依然非常高， 而关闭 fsync 可以让 AOF 的速度和 RDB 一样快， 即使在高负荷之下也是如此。 不过在处理巨大的写入载入时，RDB 可以提供更有保证的最大延迟时间（latency）。AOF 在过去曾经发生过这样的 bug ： 因为个别命令的原因，导致 AOF 文件在重新载入时，无法将数据集恢复成保存时的原样。 （举个例子，阻塞命令 BRPOPLPUSH 就曾经引起过这样的 bug 。） 测试套件里为这种情况添加了测试： 它们会自动生成随机的、复杂的数据集， 并通过重新载入这些数据来确保一切正常。 虽然这种 bug 在 AOF 文件中并不常见， 但是对比来说， RDB 几乎是不可能出现这种 bug 的。

3、spring boot 配置logback

spring boot在所有内部日志中使用Commons Logging，但是默认配置也提供了对常用日志的支持，如：Java Util Logging，Log4J，Log4J2 和Logback。每种Logger都可以通过配置使用控制台或者文件输出日志内容。\<br>

Logback 是log4J框架的作者开发的新一代日志框架，它效率更高、能够适应诸多的运行环境，同时天然支持SLF4J。\<br>
默认情况下，Spring boot会用Logback来记录日志，并用INFO级别输出到控制台。在运行应用程序和其他例子是，你应该已经看到很多INFO级别的日志了。\<br>

(1)、日志级别：trace < debug < info < warn < error < fatal，如果低于warn的信息都不会输出
spring boot中默认配置 error 、 warn 和 info 级别的日志输出到控制台。\<br>

(2)、基于applciation.properties的日志配置\<br>

spring boot默认配置指挥输出到控制台，并不会记录到文件中，但是我们通常生产环境使用时都需要以文件方式记录
若要增加文件输出，需要在application.properties中配置logging.file或logging.path属性。\<br>
logging.file 设置文件，可以是绝对路径，也可以是相对路径。\<br>
如：logging.file=my.log\<br>
logging.path 设置目录，会在改目录下创建spring.log文件，并写入日志内容\<br>
如：logging.path=/usr/local/var\<br>
注意：二者不能同时使用，如若同时使用，则只有logging.file生效\<br>
在spring boot 中只需要在application.properties 中进行配置完成日志记录的级别控制\<br>
配置格式：logging.level.*=level\<br>
logging.level: 日志级别控制前缀，*为包名或Logger名
level：选项trace,debug,info,warn,error,fatal,off\<br>
例如：
logging.level.com.exmple=debug : con.exmple包下所有class以debug级别输出\<br>
logging.level.root=warn ： root日志以warn级别输出\<br>
如果你既想完全掌握日志配置，但又不想用logback作为logback配置的名字，可以通过logging.config属性指定自定义的名字：\<br>
logging.config 属性制定自定义的名字。\<br>
logging.config=classpath:logging-logback.xml\<br>

(3)、自定义日志配置\<br>

由于日志服务一般都在ApplicationContext创建前就初始化了，它并不是必须通过Spring的配置文件控制。因此通过系统属性和传统的springboot外部配置文件依然可以很好的支持日志控制和管理。
根据不同的日志系统，你可以按如下规则组织配置文件名，就能被正确加载：\<br>
logback:logback-spring.xml,log-spring.groovy,logback.xml,logback.groovy\<br>
log4j:log4j-spring.properties,log4j-spring.xml,log4j.properties,log4j.xml\<br>
log4j2:log4j2-spring.xml,log4j2.xml\<br>
JDK(Java Util Logging): logging.properties\<br>
spring boot官方推荐优先使用带有 -spring的文件名作为你的日志配置\<br>
