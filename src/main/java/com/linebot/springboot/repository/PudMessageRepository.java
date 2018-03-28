package com.linebot.springboot.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.linebot.springboot.model.PudMessage;


public interface PudMessageRepository  extends MongoRepository<PudMessage, String> {
	public List<PudMessage> findByNo(String no);
	public boolean existsByNo(String no);   
}
