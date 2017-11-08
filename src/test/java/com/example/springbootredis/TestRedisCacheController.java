package com.example.springbootredis;

import com.example.springbootredis.domain.UserInfo;
import com.example.springbootredis.service.RedisCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhangxin on 2017/11/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedisCacheController {

    @Autowired
    private RedisCacheService redisCacheService;

    @Test
    public void getUserInfo(){
        UserInfo userinfo = redisCacheService.getUserInfo(11);
        System.out.println(userinfo.toString());
    }

    @Test
    public void saveUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(12);
        userInfo.setName("wangwu");
        userInfo.setSex((byte) 1);
        userInfo.setAddress("wangjiazhuang");
        redisCacheService.saveUserInfo(userInfo);
    }

    @Test
    public void updateUser(){
        redisCacheService.updateUserInfo(11,"hahahah");
    }

}
