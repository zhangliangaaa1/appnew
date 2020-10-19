package com.yili.bean;

public class MobileRequestVO {
	String appUserNum = "";//登录用户编码
	String appPassword= "";//密码 
	String mobileId= "";//手机ID
	String mobileName= "";//手机名称
	String lastAdress= "";//位置信息
	String reset = "";//是否解除绑定标志，默认0，1确定绑定新手机
	String encodeStr = "";//二维码时效
	String isEmessageLogin = "";//是否e-message跳转登陆
	String userId = "";//登录用户ID
	String ciphertext = "";//加密串
	String timeStemp = "";
	
	public String getTimeStemp() {
		return timeStemp;
	}
	public void setTimeStemp(String timeStemp) {
		this.timeStemp = timeStemp;
	}
	public String getAppUserNum() {
		return appUserNum;
	}
	public void setAppUserNum(String appUserNum) {
		this.appUserNum = appUserNum;
	}
	public String getAppPassword() {
		return appPassword;
	}
	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}
	public String getMobileId() {
		return mobileId;
	}
	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}
	public String getMobileName() {
		return mobileName;
	}
	public void setMobileName(String mobileName) {
		this.mobileName = mobileName;
	}
	public String getLastAdress() {
		return lastAdress;
	}
	public void setLastAdress(String lastAdress) {
		this.lastAdress = lastAdress;
	}
	public String getReset() {
		return reset;
	}
	public void setReset(String reset) {
		this.reset = reset;
	}
	public String getEncodeStr() {
		return encodeStr;
	}
	public void setEncodeStr(String encodeStr) {
		this.encodeStr = encodeStr;
	}
	public String getIsEmessageLogin() {
		return isEmessageLogin;
	}
	public void setIsEmessageLogin(String isEmessageLogin) {
		this.isEmessageLogin = isEmessageLogin;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCiphertext() {
		return ciphertext;
	}
	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}
	
	
}
