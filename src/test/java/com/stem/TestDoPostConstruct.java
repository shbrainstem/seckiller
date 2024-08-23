package com.stem;

import com.stem.controller.SecondKillController;
import com.stem.init.TestInitRedisAndDb;
import com.stem.vo.SecondKillRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

/**
 *  执行时启动被@PostConstruct注释的 InitProductService.initInventory();
 */
@SpringBootTest
@ContextConfiguration
public class TestDoPostConstruct {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SecondKillController secondKillController;

    /**
     * 循环测试，
     */
    @Test
    void testFromFirstTime(){
        //初始化
        //InitProductService 中@PostConstruct 方法执行
        //執行調用
        for(int i =0;i<10;i++) {
            consumingInventory(TestInitRedisAndDb.defaultPO.getProductNo(),Thread.currentThread().getName());
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
