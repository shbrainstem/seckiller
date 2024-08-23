package com.stem.redis;

import com.stem.service.InitProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * 测试RedisTamplate的基本功能
 */
@SpringBootTest
@ContextConfiguration
public class TestRedis {

    @Autowired
    RedisTemplate redisTemplate;
    @MockBean
    private InitProductService initInventoryService;

    @Test
    void testDecr(){
        String key = "DEMO_DRC_0001"+System.currentTimeMillis();
        ValueOperations<String,Long> opsForValue = redisTemplate.opsForValue();
        opsForValue.setIfAbsent(key,0L);
        try{
            Long result = opsForValue.increment(key);
        }catch (Exception e ){

        }
    }

    @Test
    void testRedisDefault(){
        String key = "key-XLY-"+System.currentTimeMillis();
        String value = "default_value";
        Long expireTime = 300000L;
        redisAdd(key,value,expireTime);
        Object obj = redisGet(key);
        Assertions.assertEquals(value,(String)obj);
        System.out.println("value="+key);

    }

    void redisAdd(String key , String value,Long expireTime){
        redisTemplate.opsForValue().set(key,value,expireTime, TimeUnit.MILLISECONDS);
    }

    Object redisGet(String key){
       return redisTemplate.opsForValue().get(key);
    }
}
