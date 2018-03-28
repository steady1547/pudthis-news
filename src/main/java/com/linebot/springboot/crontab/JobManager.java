package com.linebot.springboot.crontab;

import com.linebot.springboot.repository.UserInfoRepository;
import com.linebot.springboot.service.PudThisService;
import com.linebot.springboot.support.Push;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Job manager.
 */
@Component
public class JobManager {
	/**
	 * The Log.
	 */
	static final Logger LOG = LoggerFactory.getLogger(JobManager.class);
	/**
	 * The Pudthis.
	 */
	@Autowired
	PudThisService pudthis; 
	
	@Autowired
	private UserInfoRepository repository;

	/**
	 * The Push.
	 */
	@Autowired
	Push push;


	/**
	 * 10분에 한번 게릴라 알림 push 설정
	 */
	@Scheduled(initialDelay = 1000, fixedDelay = 600000) public void aJob() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		pudthis.pushGuerrillaInfo(formatter.format(new Date()));
	}

	
	/**
	 * 애플리케이션 시작 후 initialDelay(ms) 후에 첫 실행, 그 후 매 fixedDelay(ms)마다 주기적으로 실행한다. 
	 * 15분
	 */
	@Scheduled(initialDelay = 900000, fixedDelay = 900000)
	public void bJob() {
		/*System.out.println(pudthis.updateNews());
		LOG.info("[JobManager.bJob] isNewUpdate : {} ", pudthis.updateNews());
		if(pudthis.updateNews()){
			News news = News.getInstance();
			List<UserInfo> list = repository.findAll();
			for(UserInfo info : list){
				System.out.println(info.getUserId());
				//push.push(info.getUserId(),"[새/t식]\n"+ news.getNews());
			}
		}*/
		pudthis.updateNewsList();
	}
	
	/*
	 * push test 
	 * 1분
	 */
	/*@Scheduled(initialDelay = 6000000, fixedDelay = 6000000)
	public void cJob() {  
		News news = News.getInstance();
		//push.push("U46f5c0be9aae5c4ff0f6dba8c5dd3c3e", "[push test ]\n"+  news.getNews());	
	}
	*/
}
