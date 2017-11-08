package com.example.springbootredis.dao;

import com.example.springbootredis.domain.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangxin on 2017/11/7.
 */
@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo,Integer>{
}
