package com.linebot.springboot.support;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

@Component
public class Push {
	static final Logger LOG = LoggerFactory.getLogger(Push.class);

	@Autowired
    private LineMessagingClient lineMessagingClient;
	
	
	public void push(String to, String message){
		
		final PushMessage pushMessage = new PushMessage(to, new TextMessage(message));
		try{
			final BotApiResponse botApiResponse =
		    		lineMessagingClient.pushMessage(pushMessage).get();	
			LOG.info(botApiResponse.getMessage());
		}catch(Exception e){
			LOG.error("push fail : {}", e);
		}
	}
	
	public void pushList(List<String> toList, String message){
		for(String to : toList){
			push(to, message);
		}
	}
	
}
