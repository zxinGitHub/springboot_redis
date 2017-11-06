package com.example.springbootredis;

import com.example.springbootredis.domain.UserInfo;
import com.example.springbootredis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin on 2017/11/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedisController {

    @Autowired
    RedisService redisService;


    @Test
    public void updateUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setName("测试用户1");
        userInfo.setSex((byte) 1);
        userInfo.setAddress("安徽黄山");
        redisService.updateUserInfo(userInfo);

    }

    @Test
    public void getUserInfo(){
        redisService.getUserInfo("1");
    }

    @Test
    public void updateString(){
        String key = "1";
        String value = "what";
        redisService.updateString(key, value);
    }

    @Test
    public void getStringValue(){
        redisService.getStringValue("1");
    }

    @Test
    public void updateList(){
        List list = new ArrayList();
        list.add("list1");
        list.add("list2");
        list.add("list3");
        redisService.updateList("list",list);
    }

    @Test
    public void getList(){
        redisService.getList("list");
    }

}
