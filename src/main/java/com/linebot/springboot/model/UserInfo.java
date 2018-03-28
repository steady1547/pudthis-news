package com.linebot.springboot.model;

import org.springframework.data.annotation.Id;

public class UserInfo {
	
	@Id
	private String id;
	private String userId;
	private String timestamp;
	private String pudId;

	public UserInfo (){};
	
	public UserInfo(String userId, String timestamp){
		this.userId = userId;
		this.timestamp = timestamp;
	}

	public String getPudId() {
		return pudId;
	}

	public void setPudId(String pudId) {
		this.pudId = pudId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
    public String toString() {
        return String.format(
                "Event[id=%s, userId='%s', timestamp='%s']", id, userId, timestamp);
    }
}
