# crowdsouring web

该项目是crowdsouring网页的开发目录

## 准备工作

* nodejs 4.0+
* 全局安装webpack: npm install -g webpack webpack-dev-server
* 全局安装gulp: npm install -g gulp
* 安装依赖：cd path/to/当前目录 && npm install

配置hosts

```
127.0.0.1		dc-store.jd.care o2osellser-care.jd.com
172.22.133.235	plogin.o2o.jd.care
192.168.150.151	erp1.jd.net erp.360buy.net
192.168.146.83	login.o2o.jd.net
```

登录 http://plogin.o2o.jd.care/login，帐号密码找相关人员要

## 开发

```
sudo npm run dev
```

会启动本地服务器，需要80端口权限

总共有以下几个业务：

面向商家中心：
http://dc-store.jd.care/html/check.html
http://dc-store.jd.care/html/crm.html
http://dc-store.jd.care/html/goods.html
http://dc-store.jd.care/html/operation.html
http://dc-store.jd.care/html/crm_admin_8989.html

面向运营平台：
http://o2osellser-care.jd.com/html/yunyin.html

## 编译到java项目下

商家中心

```
gulp
```

运营平台
```
gulp yunyin
```

上面两个步骤针对两个业务不同进行选择，运行完成后提交代码，找java项目人员上预发布或者上线

## 项目目录解释

```
app             项目js相关源代码
css             style目录
html            页面目录
libs            第三方库目录
```

## 注意

由于历史原因，该部分代码放在此目录，对不同业务进行输出即可，后面应该可以提到根目录上最好。

## The End
