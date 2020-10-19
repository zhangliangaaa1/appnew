package com.yili.bean;

import java.io.Serializable;

/**
 * Created by ANDY on 2016/12/20.
 */
public class MobileProcessHistoryVo implements Serializable {
	private String user;    //审批人
	private String endDate; //审批时间
	private String currentState;    //当前状态
	private String comment; //意见

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
}
