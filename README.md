# 缤果电商平台

## 一、简介

采用前后端分离的策略开发，前端使用Vue框架，后端采用Spring微服务的架构。

1. `leyou`目录是服务器端代码，是本项目的核心
2. `leyou-portal`目录是门户网站代码
3. `leyou-mange-web`目录是后台网站代码

## 二、启动方法

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