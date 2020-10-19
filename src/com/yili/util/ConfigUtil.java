package com.yili.util;

import java.io.IOException;

public class ConfigUtil {
	
	private String BASE_URL;
	private String YILI_OA_ADDRESS;

	public ConfigUtil(){
		init();
	}
	
	private void init(){
//		try {
//			BASE_URL = ConfigLoader.getInstance().getProperty("BASE_URL");
//			YILI_OA_ADDRESS = ConfigLoader.getInstance().getProperty("YILI_OA_ADDRESS");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public String getBaseUrl(){
		return BASE_URL;
	}
	
	public String getYiliOAUrl(){
		return YILI_OA_ADDRESS;
	}
	
	public static void main(String[] args) {
		ConfigUtil util = new ConfigUtil();
		System.out.println(util.getBaseUrl()); ;
		System.out.println(util.getYiliOAUrl()); ;
	}
}
