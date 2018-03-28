package com.linebot.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.linebot.springboot.model.PudMessage;
import com.linebot.springboot.repository.PudMessageRepository;

@RestController
public class PudMessageController {
	static final Logger LOG = LoggerFactory.getLogger(PudMessageController.class);
	
	@Autowired
	private PudMessageRepository repository;
	
	@RequestMapping(value = "message/insert")
    @ResponseBody
    public void insert() {
		for(int i = 0 ; i < 10 ; i ++){
			repository.insert(
					new PudMessage(String.valueOf(i), "title"+i, "link"+i,"link"+i ,System.currentTimeMillis()));
		}
    }
	
	@RequestMapping(value = "message/findAll/desc")
    @ResponseBody
    public List<PudMessage> desc() {
		return repository.findAll(new Sort(Sort.Direction.DESC, "time"));
    }
	
	@RequestMapping(value = "message/findAll/asc")
    @ResponseBody
    public List<PudMessage> asc() {
		return repository.findAll(new Sort(Sort.Direction.ASC, "time"));
    }
	
	@RequestMapping(value = "message/findAll/limit")
    @ResponseBody
    public List<PudMessage> limit() {
		int limit = 5;
		Pageable page = new PageRequest(0, limit, new Sort(Direction.DESC, "time"));
		Page<PudMessage>  d = repository.findAll(page);
		return  d.getContent();
	}
	
	@RequestMapping(value = "message/update")
    @ResponseBody
    public List<PudMessage> update() {
		int limit = 5;
		Pageable page = new PageRequest(0, limit, new Sort(Direction.DESC, "time"));
		Page<PudMessage>  d = repository.findAll(page);
		return  d.getContent();
	}
	
	
}
