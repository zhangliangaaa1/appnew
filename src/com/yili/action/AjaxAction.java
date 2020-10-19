package com.yili.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.yili.bean.MobileClaimInfoVo;
import com.yili.bean.MobileNextActivityVo;
import com.yili.bean.MobileParticipantVo;
import com.yili.bean.MobileProcessHistoryVo;
import com.yili.util.ConfigUtil;
import com.yili.util.DESUtil;
import com.yili.util.HttpServiceUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("ajax")
public class AjaxAction extends BasicAction {
	
	private static Logger LOGGER = Logger.getLogger(AjaxAction.class);
	public static ConfigUtil configUtil = new ConfigUtil();
	
	private static final String SHOW_ORDER = "show";
	private static final String SHOW_FIXED = "fixed";
//	private static final String SHOW_DYNC = "dync";
	private static final String SHOW_RESULT = "result";
	private static final String SHOW_ERROR = "error";
	private static final String SHOW_TODO = "myTodo";
	
	
	private MobileClaimInfoVo mobileClaimInfoVo = null;
	private String result = null;
	private String error = "解密失败，无效请求";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6440199631731122623L;

	public String getNextSelect() throws ClientProtocolException, IOException{
		String claimId = getRequest().getParameter("claimId");
		String pendingId = getRequest().getParameter("pendingId");
		String hrNo = getRequest().getParameter("hrNo");
		String lockId = getRequest().getParameter("lockId");
		String lockTime = getRequest().getParameter("lockTime");
		
		String comment = getRequest().getParameter("comment");
		
		if(!isUTFEncoding()){
			comment = new String(comment.getBytes("ISO-8859-1"), "UTF-8");
		}
		
		LOGGER.info("getNextSelect REQUEST: " + configUtil.getBaseUrl()
				+ "mobileQueryNextActivityServlet?claimId=" + claimId + "&pendingId=" + pendingId + "&hrNo=" + hrNo);
		
		String s = HttpServiceUtil.sendGet(configUtil.getBaseUrl()
				+ "mobileQueryNextActivityServlet?claimId=" + claimId + "&pendingId=" + pendingId + "&hrNo=" + hrNo);
		
		LOGGER.info("getNextSelect :" + s);
		JSONArray jsonObject = JSONArray.fromObject(s);
		List<MobileNextActivityVo> list = new ArrayList<MobileNextActivityVo>();
		for(int i=0; i<jsonObject.size();i++){
			JSONObject object = jsonObject.getJSONObject(i);
			JSONArray jsonParticipant = JSONArray.fromObject(object.get("nextParticipant"));
			List<MobileParticipantVo> listParticipant = new ArrayList<MobileParticipantVo>();
			for(int j=0; j<jsonParticipant.size();j++){
				JSONObject objectParticipant = jsonParticipant.getJSONObject(j);
				listParticipant.add((MobileParticipantVo)JSONObject.toBean(objectParticipant, MobileParticipantVo.class));
			}
			MobileNextActivityVo mobileNextActivityVo = (MobileNextActivityVo)JSONObject.toBean(object, MobileNextActivityVo.class);
			mobileNextActivityVo.setNextParticipant(listParticipant);
			list.add(mobileNextActivityVo);
		}
		
		getRequest().setAttribute("claimId", getRequest().getParameter("claimId"));
		getRequest().setAttribute("pendingId", getRequest().getParameter("pendingId"));
		getRequest().setAttribute("comment", comment);
		getRequest().setAttribute("nextActivityVoList", list);
		getRequest().setAttribute("lockId", getRequest().getParameter("lockId"));
		getRequest().setAttribute("lockTime", getRequest().getParameter("lockTime"));
		
		// 下一环节只有一个审批人
		if(list.size() == 1){
			MobileNextActivityVo mobileNextActivityVo = list.get(0);
			if(!"灵活审批".equals(mobileNextActivityVo.getNextActivityName()) && mobileNextActivityVo.getNextParticipant().size() == 1){
				MobileParticipantVo mobileParticipantVo = mobileNextActivityVo.getNextParticipant().get(0);
				singleApprove(pendingId, mobileParticipantVo.getUserId()+"-"+mobileParticipantVo.getGroupId(), claimId, comment, mobileNextActivityVo.getNextActivityId(),lockId,lockTime);
				return SHOW_TODO;
			}
		}
		
		return SHOW_FIXED;
	}
	@RequestMapping("main")
	public  String main(){
		return "redirect:index.jsp";
	}


	@RequestMapping("show")
	public ModelAndView show(@RequestParam(name = "pendingCode", required = false) String pendingCode,
							 @RequestParam(name = "claimId", required = false) String claimId) throws ClientProtocolException, IOException{
//		String pendingCode = getRequest().getParameter("pendingCode");
		
		String xmlStr = "<?xml version=\'1.0\' encoding=\'UTF-8\'?>	"
				+ "<root> "
				+ "	<Todo> "
				+ "			<outsysid>fssc</outsysid>"			
				+ "			<todoid>"+pendingCode+"</todoid> "   			
				+ "			<viewtype>-2</viewtype> " 	
				+ "	</Todo>"
					+ "</root> ";
		try {
//			LOGGER.info("xmlStr:"+xmlStr);
//			Service service = new Service();
//			String url= configUtil.getYiliOAUrl();
//			Call call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(url));
//			call.setOperationName("updateViewtype");
//			
//			Object ret = call.invoke(new Object[] { xmlStr });
//			LOGGER.info("OA RESPONSE:" + ret.toString());
//			if(!ret.toString().contains("success")){
//				error = "待办状态更新失败";
//				LOGGER.error("待办状态更新失败");
//				return SHOW_ERROR;
//			}
			
//			if(!securityCheck()){
//				LOGGER.error("解密失败，无效请求");
//				return SHOW_ERROR;
//			}
	
//			String claimId = getRequest().getParameter("claimId");
//			String pendingId = getRequest().getParameter("pendingId");
			
//			LOGGER.info("show REQUEST: " + configUtil.getBaseUrl()
//					+ "mobileLoadAllClaimInfoServlet?claimId=" + claimId + "&pendingId=" + pendingId);
//
//			String s = HttpServiceUtil.sendGet(configUtil.getBaseUrl()
//					+ "mobileLoadAllClaimInfoServlet?claimId=" + claimId + "&pendingId=" + pendingId);
			String s="{\"expenseIssuerName\":fsscadmin}";
			LOGGER.info("show RESPONSE:" + s);
			JSONObject jsonObject = JSONObject.fromObject(s);
			mobileClaimInfoVo = (MobileClaimInfoVo) JSONObject
					.toBean(jsonObject, MobileClaimInfoVo.class);
//
//			JSONArray jsonParticipant = JSONArray.fromObject(jsonObject.get("processHistoryList"));
//			List<MobileProcessHistoryVo> processHistoryList = new ArrayList<MobileProcessHistoryVo>();
//			for(int j=0; j<jsonParticipant.size();j++){
//				JSONObject processHistory = jsonParticipant.getJSONObject(j);
//				processHistoryList.add((MobileProcessHistoryVo)JSONObject.toBean(processHistory, MobileProcessHistoryVo.class));
//			}
//			mobileClaimInfoVo.setProcessHistoryList(processHistoryList);
			
//			getRequest().setAttribute("hrNo", getRequest().getParameter("hrNo"));
//			getRequest().setAttribute("wfstate", mobileClaimInfoVo.getWfstate());
			ModelAndView modelAndView = new ModelAndView("showOrder");
			return modelAndView;
		} catch (Exception e) {
			LOGGER.error(e);
			error = e.getMessage();
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/error");
			return mav;
		}
	}
	
	public String getUser() throws ClientProtocolException, IOException{
		String keyword = getRequest().getParameter("keyword");
		if(!isUTFEncoding()){
			keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8");
		}
		
		LOGGER.info("getUser REQUEST: " + configUtil.getBaseUrl()
				+ "mobileQueryUserServlet?quyUserStr=" + URLEncoder.encode(keyword, "UTF-8") + "");
		
		String s = HttpServiceUtil.sendGet(configUtil.getBaseUrl()
				+ "mobileQueryUserServlet?quyUserStr=" + URLEncoder.encode(keyword, "UTF-8") + "");
		LOGGER.info("getUser RESPONSE:" + s);
		JSONArray jsonObject = JSONArray.fromObject(s);
		
		List<MobileParticipantVo> mobileParticipantVoList = new ArrayList<MobileParticipantVo>();
		for(int j=0; j<jsonObject.size();j++){
			JSONObject objectParticipant = jsonObject.getJSONObject(j);
			mobileParticipantVoList.add((MobileParticipantVo)JSONObject.toBean(objectParticipant, MobileParticipantVo.class));
		}
		
		writeJson(mobileParticipantVoList);
		return "";
	}
	
	private boolean isUTFEncoding(){
		String encoding = System.getProperty("file.encoding");  
		if("UTF-8".equals(encoding)){
			return true;
		}else{
			return false;
		}
	}
	
	
	public String approve() throws ClientProtocolException, IOException{
		String pendingId = getRequest().getParameter("pendingId");
		String userGroupId = getRequest().getParameter("userGroupId");
		String claimId = getRequest().getParameter("claimId");
		String comment = getRequest().getParameter("comment");
		String nextActivityDefId = getRequest().getParameter("nextActivityId");
		String lockId = getRequest().getParameter("lockId");
		String lockTime = getRequest().getParameter("lockTime");
		
		if(!isUTFEncoding()){
			comment = new String(comment.getBytes("ISO-8859-1"), "UTF-8");
		}
		
		String nextUserId = userGroupId.split("-")[0];
		String nextUserGroupId = userGroupId.split("-")[1];
		
		LOGGER.info("approve REQUEST:" + configUtil.getBaseUrl()
				+ "mobileProcessApproveServlet?claimId=" + claimId +
				"&pendingId=" + pendingId +
				"&nextActivityDefId=" + nextActivityDefId +
				"&nextUserId=" + nextUserId +
				"&nextUserGroupId=" + nextUserGroupId +
				"&lockId=" + lockId +
				"&lockTime=" + lockTime +
				"&comment=" + URLEncoder.encode(comment, "UTF-8"));
		
		String s = HttpServiceUtil.sendGet(configUtil.getBaseUrl()
				+ "mobileProcessApproveServlet?claimId=" + claimId +
				"&pendingId=" + pendingId +
				"&nextActivityDefId=" + nextActivityDefId +
				"&nextUserId=" + nextUserId +
				"&nextUserGroupId=" + nextUserGroupId +
				"&lockId=" + lockId +
				"&lockTime=" + URLEncoder.encode(lockTime, "UTF-8") +
				"&comment=" + URLEncoder.encode(comment, "UTF-8")); 
		
		LOGGER.info("approve RESPONSE:" + s);
		JSONObject jsonObject = JSONObject.fromObject(s);
		
		if(s.indexOf("success") != -1){
			result = (String)jsonObject.get("success");
			return SHOW_TODO;
		}
		
		if(s.indexOf("error") != -1){
			result = (String)jsonObject.get("success");
			return SHOW_RESULT;
		}
		
		return SHOW_TODO;
	}
	
	public String sendback() throws ClientProtocolException, IOException{
		String pendingId = getRequest().getParameter("pendingId");
		String claimId = getRequest().getParameter("claimId");
		String comment = getRequest().getParameter("comment");
		if(!isUTFEncoding()){
			comment = new String(comment.getBytes("ISO-8859-1"), "UTF-8");
		}
		
		LOGGER.info("sendback REQUEST:" + configUtil.getBaseUrl()
				+ "mobileProcessBackServlet?claimId=" + claimId +
				"&pendingId=" + pendingId +
				"&comment=" + URLEncoder.encode(comment, "UTF-8"));
		
		String s = HttpServiceUtil.sendGet(configUtil.getBaseUrl()
				+ "mobileProcessBackServlet?claimId=" + claimId +
				"&pendingId=" + pendingId +
				"&comment=" + URLEncoder.encode(comment, "UTF-8"));
		LOGGER.info("sendback RESPONSE:" + s);
		
		JSONObject jsonObject = JSONObject.fromObject(s);
		
		if(s.indexOf("success") != -1){
			result = (String)jsonObject.get("success");
			return SHOW_TODO;
		}
		
		if(s.indexOf("error") != -1){
			result = (String)jsonObject.get("success");
			return SHOW_RESULT;
		}
		
		return SHOW_TODO;
	}
	
	public MobileClaimInfoVo getMobileClaimInfoVo() {
		return mobileClaimInfoVo;
	}

	public void setMobileClaimInfoVo(MobileClaimInfoVo mobileClaimInfoVo) {
		this.mobileClaimInfoVo = mobileClaimInfoVo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	private boolean securityCheck(){
		String ciphertext = getRequest().getParameter("ciphertext");
		String logintime = getRequest().getParameter("logintime");
		String hrcode = getRequest().getParameter("hrcode");
		
		String encodeStr = DESUtil.encode(DESUtil.KEY, hrcode+logintime+DESUtil.KEY);
		if(encodeStr.equals(ciphertext)){
			return true;
		}else {
			return false;
		}
	}
	
	public void singleApprove(String pendingId, String userGroupId, String claimId, String comment, String nextActivityDefId,String lockId, String lockTime) throws ClientProtocolException, IOException{
		
		if(!isUTFEncoding()){
			comment = new String(comment.getBytes("ISO-8859-1"), "UTF-8");
		}
		
		String nextUserId = userGroupId.split("-")[0];
		String nextUserGroupId = userGroupId.split("-")[1];
		
		LOGGER.info("singleApprove REQUEST:" + configUtil.getBaseUrl()
				+ "mobileProcessApproveServlet?claimId=" + claimId +
				"&pendingId=" + pendingId +
				"&nextActivityDefId=" + nextActivityDefId +
				"&nextUserId=" + nextUserId +
				"&nextUserGroupId=" + nextUserGroupId +
				"&lockId=" + lockId +
				"&lockTime=" + lockTime +
				"&comment=" + URLEncoder.encode(comment, "UTF-8"));
		
		String s = HttpServiceUtil.sendGet(configUtil.getBaseUrl()
				+ "mobileProcessApproveServlet?claimId=" + claimId +
				"&pendingId=" + pendingId +
				"&nextActivityDefId=" + nextActivityDefId +
				"&nextUserId=" + nextUserId +
				"&nextUserGroupId=" + nextUserGroupId +
				"&lockId=" + lockId +
				"&lockTime=" + URLEncoder.encode(lockTime, "UTF-8") +
				"&comment=" + URLEncoder.encode(comment, "UTF-8")); 
		
		LOGGER.info("singleApprove RESPONSE:" + s);
		JSONObject jsonObject = JSONObject.fromObject(s);
		
		if(s.indexOf("success") != -1){
			result = (String)jsonObject.get("success");
		}
		
		if(s.indexOf("error") != -1){
			result = (String)jsonObject.get("success");
		}
	}
}
