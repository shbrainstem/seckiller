package com.stem;

import com.stem.controller.SecondKillController;
import com.stem.init.TestInitRedisAndDb;
import com.stem.vo.SecondKillRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单模拟发起交易；
 * 场景一、从库存未消耗开始
 * 场景二、从库存被消耗过，重启开始
 */
@SpringBootTest
@ContextConfiguration
public class TestSecondKill extends TestInitRedisAndDb {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SecondKillController secondKillController;

    /**
     * 场景一、循环测试，清空並重新初始化ProductNo相關信息
     */
    @Test
    void testFromFirstTime(){
        //初始化
        super.initDB(TestInitRedisAndDb.defaultPO.getProductNo());
        //執行調用
        for(int i =0;i<10;i++) {
            consumingInventory(TestInitRedisAndDb.defaultPO.getProductNo(),Thread.currentThread().getName());
        }
    }

    /**
     * 场景二、循环测试Redis中斷以後的初始化
     */
    @Test
    void testNotFromFristTime(){
        //初始化-
        super.initDBAfterIntercept(TestInitRedisAndDb.defaultPO.getProductNo());
        //執行調用
        for(int i =0;i<10;i++) {
            consumingInventory(TestInitRedisAndDb.defaultPO.getProductNo(),Thread.currentThread().getName());
        }
    }

    void testInventoryMulitThread(){
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i =0 ;i<threadCount;i++){
            executor.submit(()->{
                try{
                    consumingInventory(TestInitRedisAndDb.defaultPO.getProductNo(),Thread.currentThread().getName());
                    latch.await();
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    System.out.println("THread interrupt:"+ Thread.currentThread().getName());
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("THread Excption:"+ Thread.currentThread().getName());
                }finally {
                    latch.countDown();
                }
            });
        }
    }

    void  consumingInventory(String productNo,String threadName){
        SecondKillRequest request = new SecondKillRequest();
        {
            request.setRequestSeq("REQ_NO_"+threadName+"_"+System.currentTimeMillis());
            request.setRequestTime(System.currentTimeMillis()+"");
            request.setProductNo(productNo);
            request.setUserId("USER_ID"+System.currentTimeMillis());
            request.setUserName("USER_NAME"+System.currentTimeMillis());
            request.setObtaindQuantity(1L); //默认一次扣减1个库存
        }
        String respond  = secondKillController.secondKill(request);
        System.out.println("Controller Result==="+respond);
    }

}
