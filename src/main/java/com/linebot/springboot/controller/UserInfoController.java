package com.linebot.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.linebot.springboot.model.UserInfo;
import com.linebot.springboot.repository.UserInfoRepository;
import com.linecorp.bot.model.event.Event;

@RestController

public class UserInfoController {
	
	@Autowired
	private UserInfoRepository repository;
	
	@RequestMapping(value = "/event/list")
    @ResponseBody
    public List<UserInfo> findList() {
        return repository.findAll();
    }
	
	/*
	@RequestMapping(value = "/insert")
    @ResponseBody
    public List<UserInfo> insert() {
		
    }*/
}
