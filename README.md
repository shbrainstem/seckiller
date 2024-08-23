# seckiller

# 利用redis控制高并发时候的库存，在库存扣减完毕后阻挡后续交易；利用交易数据插入数据库时，数据库唯一索引控制交易的唯一性（每一笔交易成功时,已经使用的库存数量+产品ID）
# 优化内容：可以支持基于金额的扣减，将redis的单位对应到“分”，每次请求扣减的金额不一样也可以支持；且如果请求获取库存后，发现余额不足，可以将尝试扣减的金额退回到库存中

## 1.数据库和redis配置文件在application.yml；也可优化Tomcat线程池策略
## 2.建表脚本在ddl.sql，先执行建表操作
## 3.在新的project中，建议先执行TestSecondKill.testFromFirstTime()和 TestSecondKill.testNotFromFristTime() 对照数据库记录看效果
## 4.启动初始化和调用逻辑，参考SecondKill.png 文件
## 5.本地压力测试，首先启动main方法；然后在jmeter打开redisReqeust.jmx文件，可发起压力测试，本机压测值最低580tps，最高1100tps
## 6.优化库存超限后退回库存逻辑
