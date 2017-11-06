package com.example.springbootredis.service.impl;

import com.example.springbootredis.domain.UserInfo;
import com.example.springbootredis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangxin on 2017/11/6.
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        redisTemplate.opsForValue().set(userInfo.getId().toString(),userInfo);
    }

    @Override
    public UserInfo getUserInfo(String id) {
        UserInfo userInfo = (UserInfo) redisTemplate.opsForValue().get(id);
        System.out.println(userInfo.toString());
        return null;
    }

    @Override
    public void updateString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public UserInfo getStringValue(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        System.out.println(value);
        return null;
    }

    @Override
    public void updateList(String key, List list) {
        redisTemplate.opsForList().rightPush(key,list);
    }

    @Override
    public List getList(String key) {
        List list = redisTemplate.opsForList().range(key,0,-1);
        System.out.println(list.toArray());
        return null;
    }
}
