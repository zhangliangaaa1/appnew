package com.yili.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import com.yili.bean.MobileRequestVO;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.yili.util.CodeUtil;
import com.yili.util.ConfigUtil;
import com.yili.util.HttpServiceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("claim")
public class ClaimAction extends BasicAction {
	
	private static Logger LOGGER = Logger.getLogger(ClaimAction.class);
	public static ConfigUtil configUtil = new ConfigUtil();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6440199631731122623L;

	@Value("${BASE_URL}")
	private String baseUrl="";
	/**
	 *
	 */
	private boolean isUTFEncoding(){
		String encoding = System.getProperty("file.encoding");
		if("UTF-8".equals(encoding)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 保存报账单
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appSaveClaim")
	public String appSaveClaim(@RequestParam(name = "userId") String userId,
							   @RequestParam(name = "mobileId") String mobileId,//手机ID
							   @RequestParam(name = "ciphertext") String ciphertext,
							   @RequestParam(name = "group",required = false) String group,
							   @RequestParam(name = "userNum",required = false) String userNum,
							   @RequestParam(name = "summary",required = false) String summary,
							   @RequestParam(name = "orgId",required = false) String orgId,
							   @RequestParam(name = "buSeg",required = false) String buSeg,
							   @RequestParam(name = "verifInstructions",required = false) String verifInstructions,
							   @RequestParam(name = "requestId",required = false) String requestId,
							   @RequestParam(name = "expenseIssuerId",required = false) String expenseIssuerId,
							   @RequestParam(name = "item2Id",required = false) String item2Id,
							   @RequestParam(name = "projectNum",required = false) String projectNum,
							   @RequestParam(name = "buSegCode",required = false) String buSegCode,
							   @RequestParam(name = "repaymentSerialNo",required = false) String repaymentSerialNo,
							   @RequestParam(name = "paymentBankName",required = false) String paymentBankName,
							   @RequestParam(name = "paymentBankAccountNum",required = false) String paymentBankAccountNum,
							   @RequestParam(name = "drApProjectSegCode",required = false) String drApProjectSegCode,
							   @RequestParam(name = "drApProjectSeg",required = false) String drApProjectSeg,
							   @RequestParam(name = "planonRepaymentDate",required = false) String planonRepaymentDate,
							   @RequestParam(name = "applyAmount",required = false) String applyAmount,
							   @RequestParam(name = "isUsed",required = false) String isUsed,
							   @RequestParam(name = "vendorNo",required = false) String vendorNo,
							   @RequestParam(name = "vendorSiteCode",required = false) String vendorSiteCode,
							   @RequestParam(name = "vendorSiteId",required = false) String vendorSiteId,
							   @RequestParam(name = "contractId",required = false) String contractId,
							   @RequestParam(name = "paymentBankAccountId",required = false) String paymentBankAccountId,
							   @RequestParam(name = "itemId",required = false) String itemId) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//       param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			getRequest().setCharacterEncoding("GBK");
//			String group = getRequest().getParameter("group");//部门编码或部门名称
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId= getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串

			String urls="userNum,summary,orgId,buSeg,requestId,verifInstructions,itemId,expenseIssuerId,item2Id,projectNum,buSegCode," +
					"repaymentSerialNo,paymentBankName,paymentBankAccountNum,drApProjectSegCode," +
					"drApProjectSeg,planonRepaymentDate,applyAmount,isUsed,paymentBankName,paymentBankAccountNum," +
					"vendorNo,vendorSiteCode,vendorSiteId,contractId,paymentBankAccountId";
//			String url = getLoadUrl(urls,param);
			LOGGER.info("claimAppServlet REQUEST: " + baseUrl
					+ "claimAppServlet?servletName=appSaveClaim"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext+"&group=" + group+"&userNum=" + userNum
					+"&summary=" + summary+"&orgId=" + orgId+"&buSeg=" + buSeg+"&requestId=" + requestId+"&verifInstructions=" + verifInstructions
					+"&itemId=" + itemId+"&expenseIssuerId=" + expenseIssuerId+"&item2Id=" + item2Id+"&projectNum=" + projectNum+"&buSegCode=" + buSegCode
					+"&repaymentSerialNo=" + repaymentSerialNo+"&paymentBankName=" + paymentBankName+"&paymentBankAccountNum=" + paymentBankAccountNum+"&drApProjectSegCode=" + drApProjectSegCode+"&drApProjectSeg=" + drApProjectSeg
					+"&planonRepaymentDate=" + planonRepaymentDate+"&applyAmount=" + applyAmount+"&isUsed=" + isUsed+"&vendorNo=" + vendorNo
					+"&vendorSiteCode=" + vendorSiteCode+"&vendorSiteId=" + vendorSiteId+"&contractId=" + contractId+"&paymentBankAccountId=" + paymentBankAccountId);
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "claimAppServlet?servletName=appSaveClaim"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext+"&group=" + group+"&userNum=" + userNum
					+"&summary=" + summary+"&orgId=" + orgId+"&buSeg=" + buSeg+"&requestId=" + requestId+"&verifInstructions=" + verifInstructions
					+"&itemId=" + itemId+"&expenseIssuerId=" + expenseIssuerId+"&item2Id=" + item2Id+"&projectNum=" + projectNum+"&buSegCode=" + buSegCode
					+"&repaymentSerialNo=" + repaymentSerialNo+"&paymentBankName=" + paymentBankName+"&paymentBankAccountNum=" + paymentBankAccountNum+"&drApProjectSegCode=" + drApProjectSegCode+"&drApProjectSeg=" + drApProjectSeg
					+"&planonRepaymentDate=" + planonRepaymentDate+"&applyAmount=" + applyAmount+"&isUsed=" + isUsed+"&vendorNo=" + vendorNo
					+"&vendorSiteCode=" + vendorSiteCode+"&vendorSiteId=" + vendorSiteId+"&contractId=" + contractId+"&paymentBankAccountId=" + paymentBankAccountId);
			
			LOGGER.info("claimAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"保存员工费用报账单出错\",\"success\":false}";
			LOGGER.info("claimAppServlet :" + str);
			//getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	/**
	 * 更新报账单
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appUpdClaim")
	public String appUpdClaim(@RequestParam(name = "userId") String userId,
							@RequestParam(name = "mobileId") String mobileId,//手机ID
							@RequestParam(name = "ciphertext") String ciphertext,
							@RequestParam(name = "group",required = false) String group,
							@RequestParam(name = "userNum",required = false) String userNum,
							@RequestParam(name = "summary",required = false) String summary,
							@RequestParam(name = "orgId",required = false) String orgId,
							@RequestParam(name = "buSeg",required = false) String buSeg,
							@RequestParam(name = "verifInstructions",required = false) String verifInstructions,
							@RequestParam(name = "requestId",required = false) String requestId,
							@RequestParam(name = "expenseIssuerId",required = false) String expenseIssuerId,
							@RequestParam(name = "item2Id",required = false) String item2Id,
							@RequestParam(name = "projectNum",required = false) String projectNum,
							@RequestParam(name = "buSegCode",required = false) String buSegCode,
							@RequestParam(name = "repaymentSerialNo",required = false) String repaymentSerialNo,
							@RequestParam(name = "paymentBankName",required = false) String paymentBankName,
							@RequestParam(name = "paymentBankAccountNum",required = false) String paymentBankAccountNum,
							@RequestParam(name = "drApProjectSegCode",required = false) String drApProjectSegCode,
							@RequestParam(name = "drApProjectSeg",required = false) String drApProjectSeg,
							@RequestParam(name = "planonRepaymentDate",required = false) String planonRepaymentDate,
							@RequestParam(name = "applyAmount",required = false) String applyAmount,
							@RequestParam(name = "isUsed",required = false) String isUsed,
							@RequestParam(name = "vendorNo",required = false) String vendorNo,
							@RequestParam(name = "vendorSiteCode",required = false) String vendorSiteCode,
							@RequestParam(name = "vendorSiteId",required = false) String vendorSiteId,
							@RequestParam(name = "contractId",required = false) String contractId,
							@RequestParam(name = "paymentBankAccountId",required = false) String paymentBankAccountId,
							@RequestParam(name = "itemId",required = false) String itemId,
							  @RequestParam(name = "claimId",required = false) String claimId,
							  @RequestParam(name = "unitName",required = false) String unitName,
							  @RequestParam(name = "taxNumber",required = false) String taxNumber) throws ClientProtocolException, IOException{
		try{
//			getRequest().setCharacterEncoding("GBK");
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//       param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String group = getRequest().getParameter("group");//部门编码或部门名称
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId= getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			String urls="claimId,userNum,summary,orgId,buSeg,requestId,verifInstructions,itemId,expenseIssuerId,item2Id,projectNum,buSegCode," +
					"repaymentSerialNo,paymentBankName,paymentBankAccountNum,drApProjectSegCode,drApProjectSeg,planonRepaymentDate,applyAmount,isUsed,paymentBankName,paymentBankAccountNum,vendorNo,vendorSiteCode,vendorSiteId,contractId,paymentBankAccountId";
//			String url = getLoadUrl(urls,getRequest());
			LOGGER.info("claimAppServlet REQUEST: " + baseUrl
					+ "claimAppServlet?group=" + group + "&servletName=appUpdClaim&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext+"&userNum=" + userNum
					+"&summary=" + summary+"&orgId=" + orgId+"&buSeg=" + buSeg+"&requestId=" + requestId+"&verifInstructions=" + verifInstructions
					+"&itemId=" + itemId+"&expenseIssuerId=" + expenseIssuerId+"&item2Id=" + item2Id+"&projectNum=" + projectNum+"&buSegCode=" + buSegCode
					+"&repaymentSerialNo=" + repaymentSerialNo+"&paymentBankName=" + paymentBankName+"&paymentBankAccountNum=" + paymentBankAccountNum+"&drApProjectSegCode=" + drApProjectSegCode+"&drApProjectSeg=" + drApProjectSeg
					+"&planonRepaymentDate=" + planonRepaymentDate+"&applyAmount=" + applyAmount+"&isUsed=" + isUsed+"&vendorNo=" + vendorNo
					+"&vendorSiteCode=" + vendorSiteCode+"&vendorSiteId=" + vendorSiteId+"&contractId=" + contractId+"&paymentBankAccountId=" + paymentBankAccountId);
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "claimAppServlet?servletName=appUpdClaim"+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext+"&userNum=" + userNum
					+"&summary=" + summary+"&orgId=" + orgId+"&buSeg=" + buSeg+"&requestId=" + requestId+"&verifInstructions=" + verifInstructions
					+"&itemId=" + itemId+"&expenseIssuerId=" + expenseIssuerId+"&item2Id=" + item2Id+"&projectNum=" + projectNum+"&buSegCode=" + buSegCode
					+"&repaymentSerialNo=" + repaymentSerialNo+"&paymentBankName=" + paymentBankName+"&paymentBankAccountNum=" + paymentBankAccountNum+"&drApProjectSegCode=" + drApProjectSegCode+"&drApProjectSeg=" + drApProjectSeg
					+"&planonRepaymentDate=" + planonRepaymentDate+"&applyAmount=" + applyAmount+"&isUsed=" + isUsed+"&vendorNo=" + vendorNo
					+"&vendorSiteCode=" + vendorSiteCode+"&vendorSiteId=" + vendorSiteId+"&contractId=" + contractId+"&paymentBankAccountId=" + paymentBankAccountId+"&claimId="+claimId+"&taxNumber="+taxNumber+"&unitName"+unitName);
			
			LOGGER.info("claimAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"更新报账单出错\",\"success\":false}";
			LOGGER.info("claimAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	
	public static String getLoadUrl(String params,HttpServletRequest getRequest) throws UnsupportedEncodingException{
		String paramStr[]= params.split(",");
		String url ="";
		getRequest.setCharacterEncoding("utf-8");
		for(int i =0 ;i<paramStr.length;i++){
			if(paramStr[i]!=null){
			    url+="&"+paramStr[i]+"="+(getRequest.getParameter(paramStr[i])==null?"":java.net.URLEncoder.encode(getRequest.getParameter(paramStr[i]).toString() , "UTF-8"));//登录用户ID
			}
	
		}
		return url;
	}
	
	/**
	 * 报账单发送
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appSendClaim")
	public String appSendClaim(@RequestParam(name = "userId") String userId,
							 @RequestParam(name = "mobileId") String mobileId,//手机ID
							 @RequestParam(name = "ciphertext") String ciphertext,
							 @RequestParam(name = "claimId",required = false) String claimId,
							 @RequestParam(name = "budgetConfirm",required = false) String budgetConfirm,
							 @RequestParam(name = "claimConfirm",required = false) String claimConfirm) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//       param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String claimId = getRequest().getParameter("claimId");//登录用户ID
//			String budgetConfirm = getRequest().getParameter("budgetConfirm");//
//			String claimConfirm = getRequest().getParameter("claimConfirm");//
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appSendClaimAppServlet REQUEST: " + baseUrl
					+ "appClaimSendServlet?userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&budgetConfirm=" + budgetConfirm + "&claimConfirm=" + claimConfirm);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appClaimSendServlet?userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&budgetConfirm=" + budgetConfirm + "&claimConfirm=" + claimConfirm);
			
			LOGGER.info("appSendClaimAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"报账单发送出错\",\"success\":false}";
			LOGGER.info("appSendClaimAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
}
