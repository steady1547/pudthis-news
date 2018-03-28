package com.linebot.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.linebot.springboot.instance.News;
import com.linebot.springboot.service.PudThisService;
@RestController
public class PudThisController {
	static final Logger LOG = LoggerFactory.getLogger(PudThisController.class);

	@Autowired
	PudThisService pudthis; 
	
	@RequestMapping(value = "/getNews")
    @ResponseBody
    public String findList() {
		LOG.debug("request /getNews");
		return pudthis.getNews();
    }
	
}
