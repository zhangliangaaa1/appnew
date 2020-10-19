package com.yili.util;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.yili.action.AppAction;
import com.yili.bean.MobileRequestVO;

public class CodeUtil {
//	public static String key = "yili8888!.";
	public static String key = "yili9999!.";//生产
//	public static String emessagekey = "5ObEOi";
	public static String emessagekey = "yilihr";//生产
	
	public static String setMessageObject(MobileRequestVO param,String baseUrl,String isLogin){
		String keyString = "";
		String isEmessageLogin = param.getIsEmessageLogin();//是否e-message跳转登陆
		String userId = param.getUserId();//登录用户ID
		String ciphertext = param.getCiphertext();//加密串
		String timeStemp = param.getTimeStemp();
		if("1".equals(isLogin)&&isEmessageLogin !=null && !"null".equals(isEmessageLogin)&&!"".equals(isEmessageLogin)&&"1".equals(isEmessageLogin)){
			keyString=hexSHA1(emessagekey+userId+timeStemp);
		}else{
			keyString=hexSHA1(key+userId+timeStemp);
		}
				
		try{
			if(keyString.equals(ciphertext)){
//				if("0".equals(isLogin)){
//					LOGGER.info("appTokenValidity REQUEST: " + baseUrl
//							+ "commonComponentAppServlet?userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext  + "&servletName=" + "appTokenValidity");
//					String s = HttpServiceUtil.sendGet(baseUrl
//							+ "commonComponentAppServlet?userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext + "&servletName=" + "appTokenValidity");
//					
//					LOGGER.info("appTokenValidity :" + s);
//					return s;
//				}else{
					return "0";
//				}
			}else{
				return "0";
			}
		}catch(Exception e){
			return "0";
		}
	}
	public static String hexSHA1(String value) {
		try {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(value.getBytes("utf-8"));
		byte[] digest = md.digest();
		return byteToHexString(digest);
	} catch (Exception ex) {
		throw new RuntimeException(ex);
	}
	}
	public static String byteToHexString(byte[] bytes) {
	return String.valueOf(Hex.encodeHex(bytes));
	}
	
	public static void main(String[] args) {
		/*Date lastTime = new Date();
		
		long diff = new Date().getTime() - lastTime.getTime();   
	    long hour = diff / (1000 * 60 * 60);
	    if(hour>6){
	    	Calendar c = Calendar.getInstance();
	    	c.setTime(new Date());
	    	c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 6);
	    	lastTime = c.getTime();
	    }
	    System.out.println("加密前的字符：" + lastTime);*/
    	String token  =  hexSHA1(key+"16765"+"12345678999");
    	System.out.println("加密前的字符：" + token);
    	
//        try {
//            String test = "00124424123456";
//            if(args!=null&&args.length>0){
//                test= args[0];
//            }
//            String key = "&7U(*Il"; //&7U(*Il这个是密钥
//            if (args!=null&&args.length > 1) {
//                key = args[1];
//            }
//            DESUtil des = new DESUtil(key);
//            System.out.println("加密前的字符：" + test);
//            System.out.println("加密后的字符：" + DESUtil.encode(key, test+key));
//            System.out.println("解密后的字符：" + des.decrypt("de26aaf7f8f6bc6162da2aecf77d239724e6c381191f21b1"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
	
	public static String setMessageObject(HttpServletRequest request, HttpServletResponse response,String baseUrl,String isLogin){
		Logger LOGGER = Logger.getLogger(AppAction.class);
		String userId = request.getParameter("userId");//登录用户ID
		String mobileId = request.getParameter("mobileId");//手机ID
		String timeStemp = request.getParameter("timeStamp");//时间戳
		String ciphertext = request.getParameter("ciphertext");//加密串
		String isEmessageLogin = request.getParameter("isEmessageLogin");//是否e-message跳转登陆
		String keyString = "";
		if("1".equals(isLogin)&&isEmessageLogin !=null && !"null".equals(isEmessageLogin)&&!"".equals(isEmessageLogin)&&"1".equals(isEmessageLogin)){
			keyString=hexSHA1(emessagekey+userId+timeStemp);
		}else{
			keyString=hexSHA1(key+userId+timeStemp);
		}
				
		try{
			if(keyString.equals(ciphertext)){
//				if("0".equals(isLogin)){
//					LOGGER.info("appTokenValidity REQUEST: " + baseUrl
//							+ "commonComponentAppServlet?userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext  + "&servletName=" + "appTokenValidity");
//					String s = HttpServiceUtil.sendGet(baseUrl
//							+ "commonComponentAppServlet?userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext + "&servletName=" + "appTokenValidity");
//					
//					LOGGER.info("appTokenValidity :" + s);
//					return s;
//				}else{
					return "0";
//				}
			}else{
				return "1";
			}
		}catch(Exception e){
			return "1";
		}
	}
}
