package com.example.springbootredis.service.impl;

import com.example.springbootredis.dao.UserInfoRepository;
import com.example.springbootredis.domain.UserInfo;
import com.example.springbootredis.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by zhangxin on 2017/11/7.
 */
@Service
@CacheConfig(cacheNames = {"userinfoCache"})
public class RedisCacheServiceImpl implements RedisCacheService {

    private final static Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public void saveUserInfo(UserInfo userInfo) {
        logger.info("开始保存用户信息啦。。。。");
        userInfoRepository.save(userInfo);
        logger.info("成功保存用户信息啦，完美哦！！！");
    }

    @Override
    @Cacheable
    public UserInfo getUserInfo(Integer id) {
        logger.info("开始获取用户信息啦。。。。");
        UserInfo userInfo = userInfoRepository.findOne(id);
        logger.info("开始获取用户信息啦。。。。");
        return userInfo;
    }
}
