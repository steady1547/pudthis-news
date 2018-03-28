package com.linebot.springboot.instance;

import com.linebot.springboot.model.PudMessage;

import java.util.Iterator;
import java.util.List;

public class MessageString {
	public static final String NO_RESULT = "검색 결과가 없습니다.";
	public static final String MORE_RESULT = "* 더 많은 결과는 '#url#' 를 참고하세요.";
	public static final String WRONG_INPUT = "잘못 입력하셨습니다.";
	public static final String NEW_INFO = "[새소식]";
	private static final String GUERRILLA_INFO_TITLE = "[게릴라 던전 알림]";
	private static final String GUERRILLA_INFO_POSTFIX = " 게릴라가 시작되었습니다.";

	private static final String DELIMITER = "#";

	public static String replaceString(String message, String value){
		return message.replace(message.substring(message.indexOf(DELIMITER), message.lastIndexOf(DELIMITER)+1), value);
	}
	
	
	public static String generateMessage(String intro, PudMessage pudMessage){
		return new StringBuilder(intro).append("\n").append(generateMessage(pudMessage)).toString();
	}
	
	public static String generateMessage(PudMessage pudMessage){
		return new StringBuilder("* ").append(pudMessage.getTitle())
				.append("\n").append(pudMessage.getShorUrl()).toString();
	}
	
	public static String generateMessage(List<PudMessage> list){
		 StringBuilder sb = new StringBuilder();
		 for(Iterator<PudMessage> itr = list.iterator(); itr.hasNext(); ){
			 sb.append(generateMessage(itr.next()));
			 if(itr.hasNext()){ sb.append("\n\n");}
		 }
		return sb.toString();
	}
	
	public static String generateMessage(String intro, List<PudMessage> list){
		 StringBuilder sb = new StringBuilder(intro).append("\n");
		 sb.append(generateMessage(list));
		return sb.toString();
	}

	public static String guerrillaGenerateMessage(String title){
		return new StringBuilder(GUERRILLA_INFO_TITLE).append("\n").append(title).append(GUERRILLA_INFO_POSTFIX).toString();
	}
}
