package com.linebot.springboot;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.linebot.springboot.controller.PudThisController;
import com.linebot.springboot.instance.MessageString;
import com.linebot.springboot.instance.News;
import com.linebot.springboot.model.UserInfo;
import com.linebot.springboot.repository.UserInfoRepository;
import com.linebot.springboot.service.PudThisService;
import com.linebot.springboot.support.Reply;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@Slf4j
@LineMessageHandler
public class LineBotApplication {
	static final Logger LOG = LoggerFactory.getLogger(PudThisController.class);

    @Autowired
    private LineMessagingClient lineMessagingClient;
    
    @Autowired
	private UserInfoRepository repository;
    
    @Autowired
    private PudThisService pudThisService;
    
    @Autowired
    private Reply reply;

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
    	TextMessageContent locationMessage = event.getMessage();
    	
    	/**
    	 * 기존 유저 DB 추가를 위해서 임시 로직 추가
    	 */
    	List<UserInfo> id = repository.findByUserId(event.getSource().getUserId());
    	if(id.size() == 0){
            repository.save( new UserInfo(event.getSource().getUserId(), event.getTimestamp().toString()));
    	}
    	
    	String text = locationMessage.getText();
    	switch (getInputKey(text)) {
        	case '1': {
        		return  new TextMessage(pudThisService.getNews());
         	}
         	case '2':{
         		return new TextMessage(pudThisService.findMonster(getInputValue(text)));
         	}
         	case '3':{
         		try{
             		return new TextMessage(pudThisService.findMonsterByNum(Integer.parseInt(getInputValue(text))));
         		}catch(Exception e){
             		e.printStackTrace();
         			return new TextMessage(MessageString.WRONG_INPUT);
         		}
         	}
         	default :{
     			return new TextMessage(MessageString.WRONG_INPUT);
         	}
     	}
    }
    
    private char getInputKey(String text){
    	return text.charAt(0);
    }
    
    private String getInputValue(String text){
    	return text.substring(1,text.length()).trim();
    }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent locationMessage = event.getMessage();
    	reply.reply(event.getReplyToken(), new LocationMessage(
                locationMessage.getTitle(),
                locationMessage.getAddress(),
                locationMessage.getLatitude(),
                locationMessage.getLongitude()
        ));
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }


    private void handleSticker(String replyToken, StickerMessageContent content) {
    	reply.reply(replyToken, new StickerMessage(
                content.getPackageId(), content.getStickerId())
        );
    }
    
    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        repository.deleteByUserId(event.getSource().getUserId());
    	LOG.info("unfollowed this bot: {}", event);
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        String replyToken = event.getReplyToken();
        repository.save( new UserInfo(event.getSource().getUserId(), event.getTimestamp().toString()));
        reply.replyText(replyToken, "Got followed event");
    }
    
    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        String replyToken = event.getReplyToken();
        reply.replyText(replyToken, "Joined " + event.getSource());
    }
}
