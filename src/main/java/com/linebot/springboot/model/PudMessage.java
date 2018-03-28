package com.linebot.springboot.model;

import org.springframework.data.annotation.Id;

public class PudMessage {

	@Id
	private String id;
	private String no;
	private String title;
	private String shorUrl;
	private String longUrl;
	private long time;
	
	public PudMessage(String no, String title, String shorUrl, String longUrl, long time) {
		super();
		this.no = no;
		this.title = title;
		this.shorUrl = shorUrl;
		this.longUrl = longUrl;
		this.time = time;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getShorUrl() {
		return shorUrl;
	}

	public void setShorUrl(String shorUrl) {
		this.shorUrl = shorUrl;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	
}
