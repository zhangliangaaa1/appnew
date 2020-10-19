package com.yili.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

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
@RequestMapping("control")
public class ControlAction extends BasicAction {
	
	private static Logger LOGGER = Logger.getLogger(ControlAction.class);
	public static ConfigUtil configUtil = new ConfigUtil();
	@Value("${BASE_URL}")
	private String baseUrl="";
	/**
	 * 
	 */
	private static final long serialVersionUID = -6440199631731122623L;
	
	private boolean isUTFEncoding(){
		String encoding = System.getProperty("file.encoding");  
		if("UTF-8".equals(encoding)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 获取部门
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void appGetGroup() throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			getRequest().setCharacterEncoding("GBK");
			String group = getRequest().getParameter("group");//部门编码或部门名称
			String userId = getRequest().getParameter("userId");//登录用户ID
			String mobileId = getRequest().getParameter("mobileId");//手机ID
			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetGroupAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?group=" + group + "&servletName=" + "appGetGroup"
					 + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?group=" + group + "&servletName=" + "appGetGroup"
					 + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetGroupAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			writeString(URLDecoder.decode(s,"UTF-8"));
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询部门出错\",\"success\":false}";
			LOGGER.info("appGetGroupAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
		}
	}
	/**
	 * 查询部门属性
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetGroupAttributeId")
	public String appGetGroupAttributeId(@RequestParam(name = "userId") String userId,
										 @RequestParam(name = "mobileId") String mobileId,//手机ID
										 @RequestParam(name = "ciphertext") String ciphertext,
										 @RequestParam(name = "groupId",required = false) String groupId,
										 @RequestParam(name = "orgId",required = false) String orgId,
										 @RequestParam(name = "ccSegCode",required = false) String ccSegCode,
										 @RequestParam(name = "ccSegName",required = false) String ccSegName) throws ClientProtocolException, IOException{
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
//			String groupId = getRequest().getParameter("groupId");//部门id
//			String orgId = getRequest().getParameter("orgId");//ouid
//			String ccSegCode = getRequest().getParameter("ccSegCode");//默认费用承担部门编码
//			String ccSegName = getRequest().getParameter("ccSegName");//默认费用承担部门名称
			if(ccSegName!=null && !"".equals(ccSegName)&& !"null".equals(ccSegName)){
				ccSegName = URLEncoder.encode(ccSegName, "UTF-8");
			}
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetGroupAttributeId REQUEST: " + baseUrl
					+ "commonComponentAppServlet?groupId=" + groupId + "&servletName=" + "appGetGroupAttributeId"
					+ "&orgId=" + orgId + "&ccSegCode=" + ccSegCode 
					 + "&userId=" + userId + "&ccSegName=" + ccSegName + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?groupId=" + groupId + "&servletName=" + "appGetGroupAttributeId"
					+ "&orgId=" + orgId + "&ccSegCode=" + ccSegCode 
					 + "&userId=" + userId + "&ccSegName=" + ccSegName + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetGroupAttributeId :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询部门属性\",\"success\":false}";
			LOGGER.info("appGetGroupAttributeId :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 查询业务小类
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetItem3List")
	public String appGetItem3List(
								@RequestParam(name = "item3", required = false) String item3,//业务大类
								@RequestParam(name = "deptCostCode", required = false) String deptCostCode,
								@RequestParam(name = "item2Id", required = false) String item2Id,
								@RequestParam(name = "userId") String userId,//登录用户ID
								@RequestParam(name = "orgId", required = false) String orgId,//ouId
								@RequestParam(name = "mobileId") String mobileId,//手机ID
								@RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
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
//			String item3 = getRequest().getParameter("item3");//小类编码或名称
//			String deptCostCode = getRequest().getParameter("deptCostCode");//部门属性
//			String orgId = getRequest().getParameter("orgId");//ouid
//			String item2Id = getRequest().getParameter("item2Id");//大类编码
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetItem3ListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?item3=" + item3 + "&servletName=" + "appGetItem3List"
					+ "&orgId=" + orgId + "&item2Id=" + item2Id
					 + "&userId=" + userId + "&deptCostCode=" + deptCostCode + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?item3=" + item3 + "&servletName=" + "appGetItem3List"
					+ "&orgId=" + orgId + "&item2Id=" + item2Id
					 + "&userId=" + userId + "&deptCostCode=" + deptCostCode + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetItem3ListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询业务小类出错\",\"success\":false}";
			LOGGER.info("appGetItem3ListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询ou
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetOrgList")
	public String  appGetOrgList(@RequestParam(name = "isOnLineOU", required = false) String isOnLineOU,//是否只查询上线ou 1:只查询上线ou  0：查询全部ou
								 @RequestParam(name = "isAllOU", required = false) String isAllOU,//是否查询全部上线ou 1:全部ou  0：根据部门
								 @RequestParam(name = "groupId", required = false) String groupId,//部门ID
								 @RequestParam(name = "ouNoOrName", required = false) String ouNoOrName,//ou编码
								 @RequestParam(name = "userId") String userId,//登录用户ID
								 @RequestParam(name = "mobileId") String mobileId,//手机ID
								 @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String isOnLineOU = getRequest().getParameter("isOnLineOU");//是否只查询上线ou 1:只查询上线ou  0：查询全部ou
//			String isAllOU = getRequest().getParameter("isAllOU");
//			String groupId = getRequest().getParameter("groupId");
//			String ouNoOrName = getRequest().getParameter("ouNoOrName");//ou编码
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetOrgListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetOrgList" + "&isOnLineOU=" + isOnLineOU
					+ "&groupId=" + groupId + "&ouNoOrName=" + ouNoOrName  + "&isAllOU=" + isAllOU
					 + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetOrgList" + "&isOnLineOU=" + isOnLineOU
					+ "&groupId=" + groupId + "&ouNoOrName=" + ouNoOrName + "&isAllOU=" + isAllOU
					 + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetOrgListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询OU出错\",\"success\":false}";
			LOGGER.info("appGetOrgListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询事业部
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetBuSegCodeList")
	public String  appGetBuSegCodeList(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
									@RequestParam(name = "orgId",required = false) String orgId) throws ClientProtocolException, IOException{
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
//			String orgId = getRequest().getParameter("orgId");//orgId
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetBuSegCodeListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetBuSegCodeList" + "&orgId=" + orgId
					 + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetBuSegCodeList" + "&orgId=" + orgId
					 + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetBuSegCodeListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询事业部出错\",\"success\":false}";
			LOGGER.info("appGetBuSegCodeListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询人员
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetPersonList")
	public String  appGetPersonList(@RequestParam(name = "userId") String userId,
								 @RequestParam(name = "mobileId") String mobileId,//手机ID
								 @RequestParam(name = "ciphertext") String ciphertext,
								 @RequestParam(name = "groupId",required = false) String groupId,
								 @RequestParam(name = "userNoOrName",required = false) String userNoOrName,
								 @RequestParam(name = "orgId",required = false) String orgId) throws ClientProtocolException, IOException{
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
//			String groupId = getRequest().getParameter("groupId");//部门ID（该值为空时查询全集，不为空时按照部门查询）
//			String userNoOrName = getRequest().getParameter("userNoOrName");//人员名称或编码
//			String orgId = getRequest().getParameter("orgId");//ouid
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetPersonListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetPersonList" + "&groupId=" + groupId 
					+ "&userNoOrName=" + userNoOrName + "&userId=" + userId+ "&orgId=" + orgId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetPersonList"  + "&groupId=" + groupId
					+ "&userNoOrName=" + userNoOrName + "&userId=" + userId+ "&orgId=" + orgId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetPersonListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询人员出错\",\"success\":false}";
			LOGGER.info("appGetPersonListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询后台时间
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void appGetAPPDate() throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			String userId = getRequest().getParameter("userId");//登录用户ID
			String mobileId = getRequest().getParameter("mobileId");//手机ID
			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appGetAPPDateAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetAPPDate"+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetAPPDate"+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetAPPDateAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			writeString(URLDecoder.decode(s,"UTF-8"));
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询后台时间出错\",\"success\":false}";
			LOGGER.info("appGetAPPDateAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
		}
	}
	
	/**
	 * 借款核销
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimRelSearch")
	public String appClaimRelSearch(@RequestParam(name = "userId") String userId,
								  @RequestParam(name = "mobileId") String mobileId,//手机ID
								  @RequestParam(name = "ciphertext") String ciphertext,
								  @RequestParam(name = "orgId",required = false) String orgId,
								  @RequestParam(name = "claimId",required = false) String claimId,
								  @RequestParam(name = "itemId",required = false) String itemId,
								  @RequestParam(name = "expenseIsuserId",required = false) String expenseIsuserId,
								  @RequestParam(name = "vendorNo",required = false) String vendorNo,
								  @RequestParam(name = "pageSize",required = false) String pageSize,
								  @RequestParam(name = "curPage",required = false) String curPage) throws ClientProtocolException, IOException{
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
			//查询条件
//			String orgId = getRequest().getParameter("orgId");//ouID
//			String claimId = getRequest().getParameter("claimId");//报账单ID
//			String itemId = getRequest().getParameter("itemId");//报账单模板
//			String expenseIsuserId = getRequest().getParameter("expenseIsuserId");//报销人ID
//			String vendorNo = getRequest().getParameter("vendorNo");//供应商
//			String contractNo = getRequest().getParameter("contractNo");//合同
			
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appClaimRelSearchAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appClaimRelSearch"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&orgId=" + orgId + "&claimId=" + claimId + "&itemId=" + itemId 
					+ "&expenseIsuserId=" + expenseIsuserId + "&vendorNo=" + vendorNo
					+ "&pageSize=" + pageSize + "&curPage=" + curPage);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appClaimRelSearch"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&orgId=" + orgId + "&claimId=" + claimId + "&itemId=" + itemId 
					+ "&expenseIsuserId=" + expenseIsuserId + "&vendorNo=" + vendorNo
					+ "&pageSize=" + pageSize + "&curPage=" + curPage);
			
			LOGGER.info("appClaimRelSearchAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询借款核销出错\",\"success\":false}";
			LOGGER.info("appClaimRelSearchAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询费用承担部门
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetCcSegCodeList")
	public String appGetCcSegCodeList(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
									@RequestParam(name = "orgId",required = false) String orgId,
									@RequestParam(name = "groupId",required = false) String groupId,
									@RequestParam(name = "buSegCode",required = false) String buSegCode,
									@RequestParam(name = "ccSegCodeOrName",required = false) String ccSegCodeOrName) throws ClientProtocolException, IOException{
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
			//查询条件
//			String orgId = getRequest().getParameter("orgId");//orgId
//			String groupId = getRequest().getParameter("groupId");//部门ID
//			String buSegCode = getRequest().getParameter("buSegCode");//事业部
//			String ccSegCodeOrName = getRequest().getParameter("ccSegCodeOrName");//查询条件
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetCcSegCodeListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetCcSegCodeList"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&orgId=" + orgId + "&groupId=" + groupId + "&ccSegCodeOrName=" + ccSegCodeOrName
					+ "&buSegCode=" + buSegCode);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetCcSegCodeList"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&orgId=" + orgId + "&groupId=" + groupId + "&ccSegCodeOrName=" + ccSegCodeOrName
					+ "&buSegCode=" + buSegCode);
			
			LOGGER.info("appGetCcSegCodeListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询费用承担部门出错\",\"success\":false}";
			LOGGER.info("appGetCcSegCodeListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 借款核销--保存
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimRelSave")
	public String appClaimRelSave(@RequestParam(name = "userId") String userId,
								  @RequestParam(name = "mobileId") String mobileId,//手机ID
								  @RequestParam(name = "ciphertext") String ciphertext,
								  @RequestParam(name = "claimId",required = false) String claimId,
								  @RequestParam(name = "itemId",required = false) String itemId,
								  @RequestParam(name = "insertIds",required = false) String insertIds) throws ClientProtocolException, IOException{
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
			//查询条件
//			String claimId = getRequest().getParameter("claimId");//报账单ID
//			String itemId = getRequest().getParameter("itemId");//业务大类
//			String insertIds = getRequest().getParameter("insertIds");//核销单拼接字符串
			
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appClaimRelSaveAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appClaimRelSave"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&insertIds=" + insertIds);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appClaimRelSave"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&insertIds=" + insertIds);
			
			LOGGER.info("appClaimRelSaveAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"借款核销保存出错\",\"success\":false}";
			LOGGER.info("appClaimRelSaveAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 获取报账单模板id和流程环节id
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetClaimItemIdAndPro")
	public String  appGetClaimItemIdAndPro(@RequestParam(name = "userId") String userId,
										@RequestParam(name = "mobileId") String mobileId,//手机ID
										@RequestParam(name = "ciphertext") String ciphertext,
										@RequestParam(name = "claimId",required = false) String claimId,
										@RequestParam(name = "claimNo",required = false) String claimNo) throws ClientProtocolException, IOException{
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
			//查询条件
//			String claimId = getRequest().getParameter("claimId");//报账单ID
//			String claimNo = getRequest().getParameter("claimNo");//报账单ID
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetClaimItemIdAndProAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetClaimItemIdAndPro"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&claimNo=" + claimNo);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetClaimItemIdAndPro"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&claimNo=" + claimNo);
			
			LOGGER.info("appGetClaimItemIdAndProAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取报账单环节出错\",\"success\":false}";
			LOGGER.info("appGetClaimItemIdAndPro :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 获取版本号
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetVersioNumber")
	public String appGetVersioNumber(
								   @RequestParam(name = "mobileId",required=false) String mobileId,
								   @RequestParam(name = "userId") String userId,
								   @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			//查询条件
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetVersioNumberAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetVersioNumber"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext+ "&versioFlag=Y"
					);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetVersioNumber"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext+ "&versioFlag=Y"
					); 
			
			LOGGER.info("appGetVersioNumberAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取版本出错\",\"success\":false}";
			LOGGER.info("appGetVersioNumber :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 查询收款银行
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetBankList")
	public String appGetBankList(@RequestParam(name = "userId") String userId,
							   @RequestParam(name = "mobileId") String mobileId,//手机ID
							   @RequestParam(name = "ciphertext") String ciphertext,
							   @RequestParam(name = "bankAccountNameOrNum",required = false) String bankAccountNameOrNum,
							   @RequestParam(name = "orgId",required = false) String orgId,
							   @RequestParam(name = "pageSize",required = false) String pageSize,
							   @RequestParam(name = "curPage",required = false) String curPage) throws ClientProtocolException, IOException{
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
			//查询条件
//			String compId = getRequest().getParameter("compId");//报账单ID
//			String orgId = getRequest().getParameter("orgId");//ou
//
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String bankAccountNameOrNum = getRequest().getParameter("bankAccountNameOrNum");//查询条件
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetBankListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetBankList"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&compId=2" + "&orgId=" + orgId + "&bankAccountNameOrNum=" + bankAccountNameOrNum + "&pageSize=" + pageSize + "&curPage=" + curPage);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetBankList"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&compId=2" + "&orgId=" + orgId + "&bankAccountNameOrNum=" + bankAccountNameOrNum + "&pageSize=" + pageSize + "&curPage=" + curPage);
			
			LOGGER.info("appGetBankListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询收款银行出错\",\"success\":false}";
			LOGGER.info("appGetBankListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询项目详细信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetProjectInfo")
	public String  appGetProjectInfo(@RequestParam(name = "userId") String userId,
									 @RequestParam(name = "mobileId") String mobileId,//手机ID
									 @RequestParam(name = "ciphertext") String ciphertext,
									 @RequestParam(name = "projectId",required = false) String projectId) throws ClientProtocolException, IOException{
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
			//查询条件
//			String projectId = getRequest().getParameter("projectId");//项目ID
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetProjectInfoAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetProjectInfo"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&show=show" + "&id=" + projectId + "&projectId=" + projectId + "&attId=" + projectId
					+ "&attType=pm");
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetProjectInfo"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&show=show" + "&id=" + projectId + "&projectId=" + projectId + "&attId=" + projectId
					+ "&attType=pm");
			
			LOGGER.info("appGetProjectInfoAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询项目详细信息出错\",\"success\":false}";
			LOGGER.info("appGetProjectInfoAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询项目
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetProject")
	public String appGetProject(@RequestParam(name = "userId") String userId,
							  @RequestParam(name = "mobileId") String mobileId,//手机ID
							  @RequestParam(name = "ciphertext") String ciphertext,
							  @RequestParam(name = "coSegCode",required = false) String coSegCode,
							  @RequestParam(name = "buSegCode",required = false) String buSegCode,
							  @RequestParam(name = "projectNoOrName",required = false) String projectNoOrName,
							  @RequestParam(name = "pageSize",required = false) String pageSize,
							  @RequestParam(name = "curPage",required = false) String curPage) throws ClientProtocolException, IOException{
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
			//查询条件
//			String coSegCode = getRequest().getParameter("coSegCode");//ou
//			String buSegCode = getRequest().getParameter("buSegCode");//事业部
//	        String projectNoOrName = getRequest().getParameter("projectNoOrName");//项目编号名称
//
//	        String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetProjectAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetProject"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&coSegCode=" + coSegCode + "&buSegCode=" + buSegCode 
					+ "&projectNoOrName=" + projectNoOrName + "&pageSize=" + pageSize + "&curPage=" + curPage);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetProject"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&coSegCode=" + coSegCode + "&buSegCode=" + buSegCode 
					+ "&projectNoOrName=" + projectNoOrName + "&pageSize=" + pageSize + "&curPage=" + curPage);
			
			LOGGER.info("appGetProjectAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询项目出错\",\"success\":false}";
			LOGGER.info("appGetProjectAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 计划付款新增-保存
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appSaveClaimVendorPay")
	public String  appSaveClaimVendorPay(@RequestParam(name = "userId") String userId,
										 @RequestParam(name = "mobileId") String mobileId,//手机ID
										 @RequestParam(name = "ciphertext") String ciphertext,
										 @RequestParam(name = "claimId",required = false) String claimId,
										 @RequestParam(name = "claimNo",required = false) String claimNo,
										 @RequestParam(name = "payAmount",required = false) String payAmount,
										 @RequestParam(name = "orgId",required = false) String orgId,
										 @RequestParam(name = "payTypeId",required = false) String payTypeId,
										 @RequestParam(name = "expenseIssuerId",required = false) String expenseIssuerId,
										 @RequestParam(name = "payTypeName",required = false) String payTypeName,
										 @RequestParam(name = "vendorTypeDisp",required = false) String vendorTypeDisp,
										 @RequestParam(name = "conditionDate",required = false) String conditionDate,
										 @RequestParam(name = "reserved1",required = false) String reserved1,
										 @RequestParam(name = "planPayDate",required = false) String planPayDate,
										 @RequestParam(name = "changePayDate",required = false) String changePayDate,
										 @RequestParam(name = "remark",required = false) String remark) throws ClientProtocolException, IOException{
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
			//查询条件

//			String claimId = getRequest().getParameter("claimId");//报账单ID
//			String claimNo = getRequest().getParameter("claimNo");//报账单no
//			String payAmount = getRequest().getParameter("payAmount");//付款金额
//			String orgId = getRequest().getParameter("orgId");//
//			String expenseIssuerId = getRequest().getParameter("expenseIssuerId");//报销人Id
//			String payTypeId = getRequest().getParameter("payTypeId");//支付方式ID
//			String payTypeName = getRequest().getParameter("payTypeName");//支付方式
//			String vendorTypeDisp = getRequest().getParameter("vendorTypeDisp");//满足付款条件类型
//			String conditionDate = getRequest().getParameter("conditionDate");//条件日期
//			String reserved1 = getRequest().getParameter("reserved1");//账期
//			String planPayDate = getRequest().getParameter("planPayDate");//计划付款日
//			String changePayDate = getRequest().getParameter("changePayDate");//变更后付款日
//			String remark = getRequest().getParameter("remark");//特殊付款说明
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appSaveClaimVendorPayAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appSaveClaimVendorPay"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&claimNo=" + claimNo + "&payAmount=" + payAmount
					+ "&compId=2" + "&orgId=" + orgId + "&payTypeId=" + payTypeId + "&payTypeName=" + payTypeName
					+ "&expenseIssuerId=" + expenseIssuerId
					+ "&vendorTypeDisp=" + vendorTypeDisp + "&conditionDate=" + conditionDate+ "&reserved1=" + reserved1+ "&planPayDate=" + planPayDate+ "&changePayDate=" + changePayDate+ "&remark=" + remark);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appSaveClaimVendorPay"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&claimNo=" + claimNo + "&payAmount=" + payAmount
					+ "&compId=2" + "&orgId=" + orgId + "&payTypeId=" + payTypeId + "&payTypeName=" + payTypeName
					+ "&expenseIssuerId=" + expenseIssuerId
					+ "&vendorTypeDisp=" + vendorTypeDisp + "&conditionDate=" + conditionDate+ "&reserved1=" + reserved1+ "&planPayDate=" + planPayDate+ "&changePayDate=" + changePayDate+ "&remark=" + remark);
			
			LOGGER.info("appSaveClaimVendorPayAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"计划付款新增-保存出错\",\"success\":false}";
			LOGGER.info("appSaveClaimVendorPayAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 附件删除
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appDeleteFile")
	public String appDeleteFile(@RequestParam(name = "userId") String userId,
								@RequestParam(name = "mobileId") String mobileId,//手机ID
								@RequestParam(name = "ciphertext") String ciphertext,
								@RequestParam(name = "fileId",required = false) String fileId,
								@RequestParam(name = "claimId",required = false) String claimId) throws ClientProtocolException, IOException{
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
//			String fileId = getRequest().getParameter("fileId");//附件Id
//			String claimId = getRequest().getParameter("claimId");//报账单Id
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appDeleteFileAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appDeleteFile"
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&fileId=" + fileId + "&claimId=" + claimId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appDeleteFile"
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&fileId=" + fileId + "&claimId=" + claimId);
			
			LOGGER.info("appDeleteFileAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return  URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"附件删除出错\",\"success\":false}";
			LOGGER.info("appDeleteFileAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询字典表
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetDictList")
	public String  appGetDictList(@RequestParam(name = "userId") String userId,
							   @RequestParam(name = "mobileId") String mobileId,//手机ID
							   @RequestParam(name = "ciphertext") String ciphertext,
							   @RequestParam(name = "dictId",required = false) String dictId) throws ClientProtocolException, IOException{
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
//			String dictId = getRequest().getParameter("dictId");

//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetDictListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetDictList"
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&dictId=" + dictId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetDictList"
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&dictId=" + dictId);
			
			LOGGER.info("appGetDictListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取字典列表出错\",\"success\":false}";
			LOGGER.info("appGetDictListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询支付配置表
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetPaymentList")
	public String  appGetPaymentList(@RequestParam(name = "userId") String userId,
									 @RequestParam(name = "mobileId") String mobileId,//手机ID
									 @RequestParam(name = "ciphertext") String ciphertext,
									 @RequestParam(name = "dictId",required = false) String dictId,
									 @RequestParam(name = "itemId",required = false) String itemId,
									 @RequestParam(name = "item2Id",required = false) String item2Id) throws ClientProtocolException, IOException{
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
//			String dictId = getRequest().getParameter("dictId");

//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String itemId = getRequest().getParameter("itemId");//模板ID
//			String item2Id = getRequest().getParameter("item2Id");//业务大类ID
			
			LOGGER.info("appGetPaymentListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetPaymentList"
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&dictId=" + dictId + "&itemId=" + itemId + "&item2Id=" + item2Id);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetPaymentList"
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&dictId=" + dictId + "&itemId=" + itemId + "&item2Id=" + item2Id);
			
			LOGGER.info("appGetPaymentListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取字典列表出错\",\"success\":false}";
			LOGGER.info("appGetDictListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询业务小类成功联动子目段
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetItem3Success")
	public String appGetItem3Success(@RequestParam(name = "userId") String userId,
								   @RequestParam(name = "mobileId") String mobileId,//手机ID
								   @RequestParam(name = "ciphertext") String ciphertext,
								   @RequestParam(name = "groupAttributeId",required = false) String groupAttributeId,
								   @RequestParam(name = "claimId",required = false) String claimId,
								   @RequestParam(name = "orgId",required = false) String orgId,
								   @RequestParam(name = "item3Id",required = false) String item3Id) throws ClientProtocolException, IOException{
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
//			String groupAttributeId = getRequest().getParameter("groupAttributeId");//部门属性
//			String claimId = getRequest().getParameter("claimId");//报账单id，查询invoiceType
//			String orgId = getRequest().getParameter("orgId");//ouid
//			String item3Id = getRequest().getParameter("item3Id");//小类编码
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetItem3SuccessAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetItem3Success" 
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&orgId=" + orgId + "&groupAttributeId=" + groupAttributeId + "&item3Id=" + item3Id
					+ "&claimId=" + claimId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetItem3Success"
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&orgId=" + orgId + "&groupAttributeId=" + groupAttributeId + "&item3Id=" + item3Id
					+ "&claimId=" + claimId);
			
			LOGGER.info("appGetItem3SuccessAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"根据业务小类联动子目段出错\",\"success\":false}";
			LOGGER.info("appGetItem3SuccessAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询子目段
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetApProjectSeg")
	public String appGetApProjectSeg(@RequestParam(name = "userId") String userId,
								   @RequestParam(name = "mobileId") String mobileId,//手机ID
								   @RequestParam(name = "ciphertext") String ciphertext,
								   @RequestParam(name = "apProjectSegName",required = false) String apProjectSegName,
								   @RequestParam(name = "orgId",required = false) String orgId,
								   @RequestParam(name = "pageSize",required = false) String pageSize,
								   @RequestParam(name = "curPage",required = false) String curPage) throws ClientProtocolException, IOException{
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
//			String apProjectSegName = getRequest().getParameter("apProjectSegName");//查询条件
//			String orgId = getRequest().getParameter("orgId");//ou
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetApProjectSegAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetApProjectSeg" 
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&pageSize=" + pageSize + "&curPage=" + curPage + "&apProjectSegName=" + apProjectSegName
					+ "&orgId=" + orgId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetApProjectSeg"
					+ "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&pageSize=" + pageSize + "&curPage=" + curPage + "&apProjectSegName=" + apProjectSegName
					+ "&orgId=" + orgId);
			
			LOGGER.info("appGetApProjectSegAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询子目段出错\",\"success\":false}";
			LOGGER.info("appGetApProjectSegAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
}
