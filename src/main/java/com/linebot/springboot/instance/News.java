package com.linebot.springboot.instance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class News {
	private volatile static News instance;
	
	private Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>());
	
	private News(){};
	
	public static News getInstance(){
		if(instance == null) {
			synchronized (News.class) {
				if(instance ==  null){
					instance = new News();
				}
			}
		}
		return instance;
	}
	
	public Map<String, String> get(){
		return map;
	}
	
	public void adjust(Map<String, String> map){
		synchronized (map) {
			this.map.clear();
			this.map.putAll(map);	
		}
	}
	
	public String getNews(){
		StringBuilder sb = new StringBuilder();
		for(Entry<String, String> entry : map.entrySet()){
			sb.append(entry.getValue()).append("\n\n");
		}
		return sb.toString();
	}
}
