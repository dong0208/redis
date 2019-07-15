package com.yang.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: redis
 * @description: redis相关的bean配置
 * @author: ydd
 * @create: 2019-07-15 15:46
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * @Description:  选择redis作为默认缓存工具
     * @Param: [redisTemplate]
     * @return: org.springframework.cache.CacheManager
     * @Author: ydd
     * @Date: 2019/7/15
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisTemplate){
        RedisCacheManager rcm = RedisCacheManager.builder(redisTemplate).build();
        return rcm;
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        //配置连接工厂
        template.setConnectionFactory(factory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用jdk的序列化方式）
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //指定要序列化的域，field，get和set。以及修饰符号范围。Any是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入的类型。类型必须是fianl修饰，fianl修饰的类，比如String Integer等会抛出异常
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        //值采用json序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        //设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * @Description: 对hash类型的数据操作
     * @Param: [redisTemplate]
     * @return: org.springframework.data.redis.core.HashOperations<java.lang.String,java.lang.String,org.omg.CORBA.Object>
     * @Author: ydd
     * @Date: 2019/7/15
     */
    @Bean
    public HashOperations<String,String,Object> hashOperations(RedisTemplate<String, Object> redisTemplate){
        return redisTemplate.opsForHash();
    }

    @Bean
    /**
    * @Description:  对字符串类型进行操作
    * @Param: [redisTemplate]
    * @return: org.springframework.data.redis.core.ValueOperations<java.lang.String,org.omg.CORBA.Object>
    * @Author: ydd
    * @Date: 2019/7/15
    */
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate){
        return redisTemplate.opsForValue();
    }

    @Bean
    /**
    * @Description:  对连表类型的数据操作
    * @Param: [redisTemplate]
    * @return: org.springframework.data.redis.core.ListOperations<java.lang.String,org.omg.CORBA.Object>
    * @Author: ydd
    * @Date: 2019/7/15
    */
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate){
        return redisTemplate.opsForList();
    }

    @Bean
    /**
    * @Description:  对无序的集合类型进行操作
    * @Param: [redisTemplate]
    * @return: org.springframework.data.redis.core.SetOperations<java.lang.String,org.omg.CORBA.Object>
    * @Author: ydd
    * @Date: 2019/7/15
    */
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate){
        return redisTemplate.opsForSet();
    }

    @Bean
    /**
    * @Description:  对有序的集合类型进行操作
    * @Param: [redisTemplate]
    * @return: org.springframework.data.redis.core.ZSetOperations<java.lang.String,org.omg.CORBA.Object>
    * @Author: ydd
    * @Date: 2019/7/15
    */
    public ZSetOperations<String,Object> zSetOperations(RedisTemplate<String, Object> redisTemplate){
        return redisTemplate.opsForZSet();
    }
}
