package com.linebot.springboot.support;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.linebot.springboot.model.ShortURLInfo;

@Component
public class ShortURL {
	
	@Autowired
	private RestTemplate rt;

	private final String SHORT_URL_REQUEST_URL = "https://bitly.com/data/shorten";
	private final List<MediaType> mediaTypeList = new ArrayList<MediaType>(){
		 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			 add(MediaType.TEXT_HTML);
			 add(MediaType.APPLICATION_XHTML_XML);
			 add(MediaType.APPLICATION_XML);
		 }
	 };
	 
	private final HttpHeaders headers = new HttpHeaders(){
		
		private String randomStringGenerator(int bit, int radix){
		   	return new BigInteger(bit, new SecureRandom()).toString(radix);
		}
		 
		private String getXSRFToken(){
			return randomStringGenerator(135, 32);
		}
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String token = getXSRFToken();
			add("cookie", "_xsrf="+token);
			add("X-XSRFToken", token);
			setAccept(mediaTypeList);
		}
	};
		

	public String getShortURL(String url){
		try{
			HttpEntity<String> entity = new HttpEntity<String>(getBodyData(url), headers);
			ResponseEntity<ShortURLInfo> response = 
					rt.exchange(SHORT_URL_REQUEST_URL, HttpMethod.POST , entity, ShortURLInfo.class);
			return (response.getStatusCodeValue() == 200) ? response.getBody().getURL() : url;
		}catch(Exception e){
			 e.printStackTrace();
			 return url;
		}
	 }
	 
	private String getBodyData(String url) throws UnsupportedEncodingException{
		return new StringBuilder("url=").append(java.net.URLEncoder.encode(url,"UTF-8")).toString();
	}
}
