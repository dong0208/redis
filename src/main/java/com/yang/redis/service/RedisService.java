package com.yang.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: redis
 * @description: redis操作工具类
 * @author: ydd
 * @create: 2019-07-15 16:50
 */
@Component
public class RedisService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     *默认过期时长，单位：秒
    */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24;

    /*
    * 不设置过期时长
    * */
    public static final long NOT_EXPIRE = -1;

    public boolean existsKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * @Description: 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     * @Param: [oldKey, newKey]
     * @return: void
     * @Author: ydd
     * @Date: 2019/7/15
     */
    public void renameKey(String oldKey,String newKey){
        redisTemplate.rename(oldKey,newKey);
    }

    /**
     * @Description: newKey不存在时才重命名
     * @Param: [oldKey, newKey]
     * @return: boolean
     * @Author: ydd
     * @Date: 2019/7/15
     */
    public boolean renameKeyNotExist(String oldKey,String newKey){
        return redisTemplate.renameIfAbsent(oldKey,newKey);
    }

    //删除key
    public void deleteKey(String key){
        redisTemplate.delete(key);
    }

    //删除多个key
    public void deleteKey(String... keys){
        Set<String> kSet = Stream.of(keys).map(k->k).collect(Collectors.toSet());
        redisTemplate.delete(kSet);
    }

    //删除Key的集合
    public void deleteKey(Collection<String> keys){
        Set<String> kSet = keys.stream().map(k -> k).collect(Collectors.toSet());
    }

    //设置key的生命周期
    public void exprieKey(String key, long time, TimeUnit timeUnit){
        redisTemplate.expire(key,time,timeUnit);
    }

    //指定key在指定的日期过期
    public void exprieKeyAt(String key, Date date){
        redisTemplate.expireAt(key,date);
    }

    //查询key的生命周期
    public long getKeyExpire(String key,TimeUnit timeUnit){
        return redisTemplate.getExpire(key,timeUnit);
    }

    //将key设置为永久有效
    public void persisKey(String key){
        redisTemplate.persist(key);
    }
}
