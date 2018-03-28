package com.linebot.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;

import com.linebot.springboot.model.PudMessage;
import com.linebot.springboot.repository.PudMessageRepository;
import com.linebot.springboot.repository.UserInfoRepository;
import com.linebot.springboot.service.PudThisService;

@SpringBootApplication
@EnableScheduling//스케쥴러 활성화
public class LineBotSpringBootApplication implements CommandLineRunner{
	
	@Autowired
	PudThisService pudthis;
	
	@Autowired
	PudMessageRepository msg;
	
	public static void main(String[] args) {
		SpringApplication.run(LineBotSpringBootApplication.class, args);
	}
	
	/**
	 * 스케쥴 등록
	 * @return
	 */
	@Bean 
	public TaskScheduler taskScheduler() { 
		return new ConcurrentTaskScheduler(); 
	} 

	/**
	 * 구동 시 필요 처리
	 */
	@Override
	public void run(String... args) throws Exception {
		//pudthis.updateNews();
		
	}
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   // Do any additional configuration here
	   return builder.build();
	}
}
