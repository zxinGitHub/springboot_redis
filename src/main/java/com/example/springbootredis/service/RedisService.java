package com.example.springbootredis.service;

import com.example.springbootredis.domain.UserInfo;

import java.util.List;

/**
 * Created by zhangxin on 2017/11/6.
 */
public interface RedisService {

    void updateUserInfo(UserInfo userInfo);

    UserInfo getUserInfo(String id);

    void updateString(String key, String value);

    UserInfo getStringValue(String key);

    void updateList(String key, List list);

    List getList(String key);

}
