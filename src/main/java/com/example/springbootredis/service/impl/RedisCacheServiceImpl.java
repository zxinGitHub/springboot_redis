package com.example.springbootredis.service.impl;

import com.example.springbootredis.dao.UserInfoRepository;
import com.example.springbootredis.domain.UserInfo;
import com.example.springbootredis.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangxin on 2017/11/7.
 */
@Service
@CacheConfig(cacheNames = {"userinfoCache"})
public class RedisCacheServiceImpl implements RedisCacheService {

    private final static Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveUserInfo(UserInfo userInfo) {
        logger.info("开始保存用户信息啦。。。。");
        userInfoRepository.save(userInfo);
        logger.info("成功保存用户信息啦，完美哦！！！");
    }

    /**
     * 增加了@Cacheable注解后，会优先从缓存中从取数据，
     * 如果缓存中没数据就把从数据库取到的缓存更新到redis中缓存起来
     * 下次再查询时就取缓存中的数据
     * @param id
     * @return
     */
    @Override
    @Cacheable
    public UserInfo getUserInfo(Integer id) {
        logger.info("开始获取用户信息啦。。。。");
        //UserInfo userInfo = userInfoRepository.findOne(id);
        Query query = new Query(Criteria.where("id").is(id));
        //获取查询到第一条数据
        UserInfo userInfo = mongoTemplate.findOne(query,UserInfo.class);
        logger.info("开始获取用户信息啦。。。。");
        return userInfo;
    }

    /**
     * 根据id更新address
     * @param id
     * @param address
     * 注意：更新操作时需要进行清除缓存操作  否则下次取到的还是旧的数据
     */
    @Override
    @CacheEvict(allEntries = true)
    public void updateUserInfo(Integer id, String address) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("address",address);
        //更新查询返回集的第一条
        mongoTemplate.updateFirst(query,update,UserInfo.class);
        //更新查询返回集的所有条
        mongoTemplate.updateMulti(query,update,UserInfo.class);
    }
}
