package com.yili.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BasicAction extends ActionSupport{


	private static final long serialVersionUID = 1L;

	/** 提示信息专用 */
	public String message;
	
	/** 用于存放错误信息 */
	public Map<String, String> errorMap = new HashMap<String, String>();
	
	public BasicAction(){
	}
	
	/** 获取request对象 */
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) ActionContext.getContext().get(
				ServletActionContext.HTTP_REQUEST);
	}
	
	/** 获取response对象 */
	public HttpServletResponse getResponse() {
		return (HttpServletResponse) ActionContext.getContext().get(
				ServletActionContext.HTTP_RESPONSE);
	}
	
	/** 获取session对象 */
	public Map<String, Object> getSession() {
		return ActionContext.getContext().getSession();
	}
	
	/** 输出String */
	public void writeString(String s) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().print(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	/** 将对象转化成jason串 */
	public void writeJson(Object value) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		
		ObjectMapper mapper = new ObjectMapper();
		DateFormat df =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mapper.setDateFormat(df);
		
		try {
		  	mapper.writeValue(response.getWriter(), value);
		}catch (Exception e) {
		    e.printStackTrace();
		}
    }
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getErrorMap() {
		return errorMap;
	}

	public void setErrorMap(Map<String, String> errorMap) {
		this.errorMap = errorMap;
	}
	

}
 