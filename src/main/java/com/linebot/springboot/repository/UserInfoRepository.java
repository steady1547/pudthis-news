package com.linebot.springboot.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.linebot.springboot.model.UserInfo;

public interface UserInfoRepository extends MongoRepository<UserInfo, String> {
	public List<UserInfo> findByUserId(String userId);
    public void deleteByUserId(String userId);
}
