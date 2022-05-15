# 缤果电商平台

## 一、简介

采用前后端分离的策略开发，前端使用Vue框架，后端采用Spring Cloud微服务的架构。

1. `leyou`目录是服务器端代码，是本项目的核心
2. `leyou-portal`目录是门户网站代码
3. `leyou-mange-web`目录是后台网站代码



## 二、项目详细介绍

### 一、架构图

![img](https://img-blog.csdnimg.cn/20181212215151153.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)


### 二、包含的微服务




#### 2.1 网关微服务

> 架构图

![img](https://img-blog.csdnimg.cn/20181214102311483.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)


不管是来自于客户端（PC或移动端）的请求，还是服务内部调用。一切对服务的请求都会经过Zuul这个网关，然后再由网关来实现 鉴权、动态路由等等操作。Zuul就是我们服务的统一入口。

> 配置

![img](https://img-blog.csdnimg.cn/20181214102758667.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)

服务网关是微服务架构中一个不可或缺的部分。通过服务网关统一向外系统提供REST API的过程中，除了具备服务路由、均衡负载功能之外，它还具备了`权限控制`等功能。为微服务架构提供了前门保护的作用，同时将权限控制这些较重的非业务逻辑内容迁移到服务路由层面，使得服务集群主体能够具备更高的可复用性和可测试性。

> 主要功能

身份认证与安全：识别每个资源的验证要求，并拒绝那些与要求不相符的请求。（对jwt鉴权）

动态路由：动态地将请求路由到不同的后端集群。

负载均衡和熔断

#### 2.2 授权中心微服务

> 结合RSA的鉴权

![img](https://img-blog.csdnimg.cn/20181214104033784.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)

- 首先利用RSA生成公钥和私钥。私钥保存在授权中心，公钥保存在Zuul和各个微服务
- 用户请求登录
- 授权中心校验，通过后用私钥对JWT进行签名加密
- 返回jwt给用户
- 用户携带JWT访问
- Zuul直接通过公钥解密JWT，进行验证，验证通过则放行
- 请求到达微服务，微服务直接用公钥解析JWT，获取用户信息，无需访问授权中心

> 授权中心的主要职责

1. 用户鉴权：接收用户的登录请求，通过用户中心的接口进行校验，通过后生成JWT。使用私钥生成JWT并返回
2. 服务鉴权：微服务间的调用不经过Zuul，会有风险，需要鉴权中心进行认证。原理与用户鉴权类似，但逻辑稍微复杂一些（未实现）。

#### 2.3 购物车微服务

> 功能需求

- 用户可以在登录状态下将商品添加到购物车

放入数据库

放入redis（采用）

- 用户可以在未登录状态下将商品添加到购物车
- 放入localstorage
- 用户可以使用购物车一起结算下单
- 用户可以查询自己的购物车
- 用户可以在购物车中修改购买商品的数量。
- 用户可以在购物车中删除商品。
- 在购物车中展示商品优惠信息
- 提示购物车商品价格变化

> 流程图

![img](https://img-blog.csdnimg.cn/20181214104500266.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)


这幅图主要描述了两个功能：新增商品到购物车、查询购物车。

- 新增商品：

  - 判断是否登录
    - 是：则添加商品到后台Redis中
    - 否：则添加商品到本地的Localstorage

- 无论哪种新增，完成后都需要查询购物车列表：

  - 判断是否登录
    - 否：直接查询localstorage中数据并展示
    - 是：已登录，则需要先看本地是否有数据，
      - 有：需要提交到后台添加到redis，合并数据，而后查询
      - 否：直接去后台查询redis，而后返回





#### 2.4 页面静态化微服务

商品详情浏览量比较大，并发高，所以单独开启一个微服务用来展示商品详情，并且对其进行静态化处理，保存为静态html文件。在用户访问商品详情页面时，让nginx对商品请求进行监听，指向本地静态页面，如果本地没找到，才反向代理到页面详情微服务端口。 

#### 2.5 后台管理微服务

主要是对商品分类、品牌、商品的规格参数以及商品的CRUD，为后台管理提供各种接口。 

#### 2.6 订单微服务

主要接口有：

- 创建订单
- 查询订单
- 更新订单状态
- 根据订单号生成微信付款链接
- 根据订单号查询支付状态

#### 2.7 注册中心

> 基本架构

![img](https://img-blog.csdnimg.cn/2018121514422783.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)


- Eureka：就是服务注册中心（可以是一个集群），对外暴露自己的地址
- 提供者：启动后向Eureka注册自己信息（地址，提供什么服务）
- 消费者：向Eureka订阅服务，Eureka会将对应服务的所有提供者地址列表发送给消费者，并且定期更新
- 心跳(续约)：提供者定期通过http方式向Eureka刷新自己的状态

主要功能就是对各种服务进行管理。

#### 2.8 搜索微服务

主要是对Elasticsearch的应用，将所有商品数据封装好后添加到Elasticsearch的索引库中，然后进行搜索过滤，查询相应的商品信息。 



#### 2.9 短信微服务

因为系统中不止注册一个地方需要短信发送，因此将短信发送抽取为微服务：`leyou-sms-service`，凡是需要的地方都可以使用。

另外，因为短信发送API调用时长的不确定性，为了提高程序的响应速度，短信发送我们都将采用异步发送方式，即：

- 短信服务监听MQ消息，收到消息后发送短信。
- 其它服务要发送短信时，通过MQ通知短信微服务。

#### 2.10 文件上传微服务

使用分布式文件系统FastDFS实现图片上传。

> FastDFS架构

![img](https://img-blog.csdnimg.cn/20181215150632856.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)


FastDFS两个主要的角色：Tracker Server 和 Storage Server 。

- Tracker Server：跟踪服务器，主要负责调度storage节点与client通信，在访问上起负载均衡的作用，和记录storage节点的运行状态，是连接client和storage节点的枢纽。
- Storage Server：存储服务器，保存文件和文件的meta data（元数据），每个storage server会启动一个单独的线程主动向Tracker cluster中每个tracker server报告其状态信息，包括磁盘使用情况，文件同步情况及文件上传下载次数统计等信息
- Group：文件组，多台Storage Server的集群。上传一个文件到同组内的一台机器上后，FastDFS会将该文件即时同步到同组内的其它所有机器上，起到备份的作用。不同组的服务器，保存的数据不同，而且相互独立，不进行通信。
- Tracker Cluster：跟踪服务器的集群，有一组Tracker Server（跟踪服务器）组成。
- Storage Cluster ：存储集群，有多个Group组成。

> 上传流程

![img](https://img-blog.csdnimg.cn/2018121515081720.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)


1. Client通过Tracker server查找可用的Storage server。
2. Tracker server向Client返回一台可用的Storage server的IP地址和端口号。
3. Client直接通过Tracker server返回的IP地址和端口与其中一台Storage server建立连接并进行文件上传。
4. 上传完成，Storage server返回Client一个文件ID，文件上传结束。

> 下载流程

![img](https://img-blog.csdnimg.cn/20181215150912300.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)


1. Client通过Tracker server查找要下载文件所在的的Storage server。
2. Tracker server向Client返回包含指定文件的某个Storage server的IP地址和端口号。
3. Client直接通过Tracker server返回的IP地址和端口与其中一台Storage server建立连接并指定要下载文件。
4. 下载文件成功。



#### 2.11 支付微服务

这里调用微信支付的接口



## 三、启动方法

### Step1

虚拟机启动，版本为Centos6，启动之后，使用secureCRT连接虚拟机，然后切换到leyou用户后，到目录下执行`./elasticsearch`



### Step2

在本机，打开nginx，`start nginx` .注意别忘了关闭本机的SqlServer的report服务，否则会出现端口占用问题。



### Step3

启动后台服务：使用idea打开leyou项目，启动所有微服务



### Step4

启动前端页面-网站门户：使用idea打开leyou-portal项目，执行`live-server --port=9002`



### Step5

启动前端页面-后台：使用idea打开leyou-mange-web项目，执行`npm start`





### 

