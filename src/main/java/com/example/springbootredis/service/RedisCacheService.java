package com.example.springbootredis.service;

import com.example.springbootredis.domain.UserInfo;

/**
 * Created by zhangxin on 2017/11/7.
 */
public interface RedisCacheService {

    void saveUserInfo(UserInfo userInfo);

    UserInfo getUserInfo(Integer id);

    void updateUserInfo(Integer id, String address);
}
