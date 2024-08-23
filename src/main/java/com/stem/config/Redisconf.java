package com.stem.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;

@Configuration
public class Redisconf {

    /**
     * 重写后对，RedisTemplate的increment和decrement方法支持Long类型的处理
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String,Long> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Long> tempalte = new RedisTemplate<>();
        tempalte.setConnectionFactory(factory);

        //设置 key的 序列化方式
        tempalte.setKeySerializer(new StringRedisSerializer());
        //设置 value的序列化方式
        tempalte.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        //设置 hash key 的序列化方式
        tempalte.setHashKeySerializer(new StringRedisSerializer());
        // 设置 hash value的序列化方式
        tempalte.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        tempalte.afterPropertiesSet();
        return tempalte;
    }

    /*
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }*/

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        StringCodec stringCodec = new StringCodec(Charset.forName("UTF-8"));
        config.setCodec(stringCodec);
        //config.setCodec(new JsonJacksonCodec()); // 使用Jackson序列化
        //config.useSingleServer().setAddress("redis://21.100.11.15:6379"); // 设置为你的Redis服务器地址和端口

        //Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setPassword(redisPassword);

        return Redisson.create(config);
    }
}
