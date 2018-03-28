package com.linebot.springboot.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortURLInfo{

	@JsonProperty("status_code")
	private String statusCode;
	
	
	@JsonProperty("status_txt")
	private String statusTxt;
	
	private Map<String, String> data = new HashMap<String, String>();
	
	public String getURL(){
		return data.get("url");
	}
	
	@JsonAnyGetter
    public Map<String, String> any() {
        return data;
    }
 
    @JsonAnySetter
    public void set(String name, String value) {
    	data.put(name, value);
    }

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("statusCode=").append(statusCode).append(", ");
		sb.append("data=").append(data);
		return sb.toString();
	};
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusTxt() {
		return statusTxt;
	}

	public void setStatusTxt(String statusTxt) {
		this.statusTxt = statusTxt;
	}
	

}
