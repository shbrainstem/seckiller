# seckiller

## 1.数据库和redis配置文件在application.yml；也可优化Tomcat线程池策略
## 2.建表脚本在ddl.sql，先执行建表操作
## 3.在新的project中，建议先执行TestSecondKill.testFromFirstTime()和 TestSecondKill.testNotFromFristTime() 对照数据库记录看效果
## 4.启动初始化和调用逻辑，参考SecondKill.png 文件
## 5.本地压力测试，首先启动main方法；然后在jmeter打开redisReqeust.jmx文件，可发起压力测试，本机压测值最低580tps，最高1100tps
## 6.优化库存超限后退回库存逻辑
