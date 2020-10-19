package com.yili.action;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yili.bean.MobileNextActivityVo;
import com.yili.bean.MobileParticipantVo;
import com.yili.bean.MobileRequestVO;
import com.yili.util.CodeUtil;
import com.yili.util.ConfigUtil;
import com.yili.util.DESUtilApp;
import com.yili.util.HttpServiceUtil;
import com.yili.util.ImageSizer;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("app")
public class AppAction extends BasicAction {
	
	private static Logger LOGGER = Logger.getLogger(AppAction.class);
	public static ConfigUtil configUtil = new ConfigUtil();
	

	@Value("${BASE_URL}")
	private String baseUrl="";
	@Value("${upLoad_URL}")
	private String uploadUrl="";

	
	private boolean isUTFEncoding(){
		String encoding = System.getProperty("file.encoding");  
		if("UTF-8".equals(encoding)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 登录
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appLogin")
//	public String appLogin(@RequestBody MobileRequestVO param) throws ClientProtocolException, IOException{
	public String appLogin(@RequestParam(name = "appUserNum", required = false) String appUserNum,
			@RequestParam(name = "appPassword", required = false) String appPassword,
			@RequestParam(name = "mobileId") String mobileId,
			@RequestParam(name = "mobileName", required = false) String mobileName,
			@RequestParam(name = "lastAdress", required = false) String lastAdress,
			@RequestParam(name = "encodeStr", required = false) String encodeStr,
			@RequestParam(name = "isEmessageLogin", required = false) String isEmessageLogin,
			@RequestParam(name = "userId") String userId,
			@RequestParam(name = "ciphertext") String ciphertext,
			@RequestParam(name = "reset", required = false) String reset) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			//appUserNum,appPassword,mobileId,type
//			String appUserNum = param.getAppUserNum();//登录用户编码
//			String appPassword= param.getAppPassword();//密码 
//			String mobileId= param.getMobileId();//手机ID
//			String mobileName= param.getMobileName();//手机名称
//			String lastAdress= param.getLastAdress();//位置信息
//			String reset = param.getReset();//是否解除绑定标志，默认0，1确定绑定新手机
//			String encodeStr = param.getEncodeStr();//二维码时效
//			String isEmessageLogin = param.getIsEmessageLogin();//是否e-message跳转登陆
//			String userId = param.getUserId();//登录用户ID
//			String ciphertext = param.getCiphertext();//加密串
			if(mobileName!=null && !"".equals(mobileName)&& !"null".equals(mobileName)){
				mobileName = URLEncoder.encode(mobileName, "UTF-8");
			}
			LOGGER.info("appLoginServlet REQUEST: " + baseUrl
					+ "appLoginServlet?appUserNum=" + appUserNum + "&appPassword=" + appPassword
					 + "&mobileId=" + mobileId + "&encodeStr=" + encodeStr + "&reset=" + reset + "&mobileName=" + mobileName
					 + "&lastAdress=" + lastAdress+ "&isEmessageLogin=" + isEmessageLogin+ "&userId=" + userId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appLoginServlet?appUserNum=" + appUserNum + "&appPassword=" + appPassword
					 + "&mobileId=" + mobileId + "&encodeStr=" + encodeStr + "&reset=" + reset + "&mobileName=" + mobileName
					 + "&lastAdress=" + lastAdress+ "&isEmessageLogin=" + isEmessageLogin+ "&userId=" + userId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appLoginServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"登录异常\",\"success\":false}";
			LOGGER.info("appLoginServlet :" + str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 我的报账单查询
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetMySubmitedList")
	public String appGetMySubmitedList(@RequestParam(name = "userNum", required = false) String userNum,//当前登录人
									 @RequestParam(name = "claimNo", required = false) String claimNo,//报账单号
									 @RequestParam(name = "itemId", required = false) String itemId,//报账单模板
									 @RequestParam(name = "item2Id", required = false) String item2Id,//业务大类
									 @RequestParam(name = "applyUserNum", required = false) String applyUserNum,//起草人
									 @RequestParam(name = "applyAmountStart", required = false) String applyAmountStart,//报账金额
									 @RequestParam(name = "applyAmountEnd", required = false) String applyAmountEnd,//报账金额
									 @RequestParam(name = "commonQuery", required = false) String commonQuery,//公共查询
									 @RequestParam(name = "applyDateStart", required = false) String applyDateStart,//起草时间开始
									 @RequestParam(name = "applyDateEnd", required = false) String applyDateEnd,//起草时间结束
									 @RequestParam(name = "userId") String userId,//登录用户ID
									 @RequestParam(name = "curPage", required = false) String curPage,//分页
									 @RequestParam(name = "pageSize", required = false) String pageSize,//分页
									 @RequestParam(name = "mobileId") String mobileId,//手机ID
									 @RequestParam(name = "ciphertext") String ciphertext)  throws ClientProtocolException, IOException{//加密串
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			//String resultString ="0";
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========

			LOGGER.info("getMySubmitedList REQUEST: " + baseUrl
					+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "appGetMySubmitedList"
					 + "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
					 + "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&applyDateStart=" + applyDateStart
					 + "&applyDateEnd=" + applyDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "appGetMySubmitedList"
					+ "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
					+ "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
					+ "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&applyDateStart=" + applyDateStart
					+ "&applyDateEnd=" + applyDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("getMySubmitedList :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"我的报账单查询结果出错\",\"success\":false}";
			LOGGER.info("getMySubmitedList :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 我的草稿
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetMyDraftList")
	public String  appGetMyDraftList(@RequestParam(name = "userNum", required = false) String userNum,//当前登录人
								  @RequestParam(name = "claimNo", required = false) String claimNo,//报账单号
								  @RequestParam(name = "itemId", required = false) String itemId,//报账单模板
								  @RequestParam(name = "item2Id", required = false) String item2Id,//业务大类
								  @RequestParam(name = "applyUserNum", required = false) String applyUserNum,//起草人
								  @RequestParam(name = "applyAmountStart", required = false) String applyAmountStart,//报账金额
								  @RequestParam(name = "applyAmountEnd", required = false) String applyAmountEnd,//报账金额
								  @RequestParam(name = "commonQuery", required = false) String commonQuery,//公共查询
								  @RequestParam(name = "applyDateStart", required = false) String applyDateStart,//起草时间开始
								  @RequestParam(name = "applyDateEnd", required = false) String applyDateEnd,//起草时间结束
								  @RequestParam(name = "userId") String userId,//登录用户ID
								  @RequestParam(name = "curPage", required = false) String curPage,//分页
								  @RequestParam(name = "pageSize", required = false) String pageSize,//分页
								  @RequestParam(name = "mobileId") String mobileId,//手机ID
								  @RequestParam(name = "ciphertext") String ciphertext)  throws ClientProtocolException, IOException{//加密串
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

			LOGGER.info("getMyDraftListAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "appGetMyDraftList"
					 + "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
					 + "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&applyDateStart=" + applyDateStart
					 + "&applyDateEnd=" + applyDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "appGetMyDraftList"
					+ "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
					 + "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&applyDateStart=" + applyDateStart
					 + "&applyDateEnd=" + applyDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("getMyDraftListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"我的草稿查询结果出错\",\"success\":false}";
			LOGGER.info("getMyDraftListAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 获取业务大类
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetItem2List")
	public String appGetItem2List(
								  @RequestParam(name = "itemId", required = false) String itemId,//报账单模板
								  @RequestParam(name = "item2Id", required = false) String item2Id,//业务大类
								  @RequestParam(name = "applyUserNum", required = false) String applyUserNum,//起草人
								  @RequestParam(name = "applyAmountStart", required = false) String applyAmountStart,//报账金额
								  @RequestParam(name = "applyAmountEnd", required = false) String applyAmountEnd,//报账金额
								  @RequestParam(name = "commonQuery", required = false) String commonQuery,//公共查询
								  @RequestParam(name = "applyDateStart", required = false) String applyDateStart,//起草时间开始
								  @RequestParam(name = "applyDateEnd", required = false) String applyDateEnd,//起草时间结束
								  @RequestParam(name = "userId") String userId,//登录用户ID
								  @RequestParam(name = "orgId", required = false) String orgId,//ouId
								  @RequestParam(name = "mobileId") String mobileId,//手机ID
								  @RequestParam(name = "ciphertext") String ciphertext)  throws ClientProtocolException, IOException{//加密串
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========


			
			LOGGER.info("getItem2ListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?itemId=" + itemId + "&servletName=" + "appGetItem2List"
					 + "&item2Id=" + item2Id + "&orgId=" + orgId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?itemId=" + itemId + "&servletName=" + "appGetItem2List"
					 + "&item2Id=" + item2Id + "&orgId=" + orgId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("getItem2ListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询业务大类出错\",\"success\":false}";
			LOGGER.info("getItem2ListAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 获取文件
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetFile")
	public String appGetFile(@RequestParam(name = "fileType", required = false) String fileType,//附件类型（影像：eim,附件：att，报账单封皮文件：claim,报账单粘贴单:claimZ）
						   @RequestParam(name = "fileId", required = false) String fileId,//文件所在行id
						   @RequestParam(name = "isSmall", required = false) String isSmall,//是否缩略图（针对影像）
						   @RequestParam(name = "claimId", required = false) String claimId,//报账单id
						   @RequestParam(name = "claimNo", required = false) String claimNo,//报账单id
						   @RequestParam(name = "itemId", required = false) String itemId,//报账单id
						   @RequestParam(name = "userId") String userId,//登录用户ID
						   @RequestParam(name = "orgId", required = false) String orgId,//ouId
						   @RequestParam(name = "mobileId") String mobileId,//手机ID
						   @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		FileInputStream readFile = null;
		BufferedInputStream in = null;
		ServletOutputStream out = null;
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========

			
			LOGGER.info("appGetFile REQUEST: " + baseUrl
					+ "commonComponentAppServlet?fileType=" + fileType + "&servletName=" + "appGetFile"
					 + "&fileId=" + fileId + "&isSmall=" + isSmall+ "&claimNo=" + claimNo+ "&itemId=" + itemId + "&claimId=" + claimId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?fileType=" + fileType + "&servletName=" + "appGetFile"
					 + "&fileId=" + fileId + "&isSmall=" + isSmall+ "&claimNo=" + claimNo+ "&itemId=" + itemId +  "&claimId=" + claimId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetFile :" + URLDecoder.decode(s,"UTF-8"));
			
			JSONObject jsonObject = JSONObject.parseObject(URLDecoder.decode(s,"UTF-8"));
			String code = jsonObject.getString("code");
//			if("1".equals(code)){
//				getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
//			}else{
//				String filePath= jsonObject.getString("filePath");
//				out = getResponse().getOutputStream();
//				File file = new File(filePath);
//				if (file.exists()) {
		            // 写明要下载的文件的大小  
//					getResponse().setContentLength((int) file.length());
//					getResponse().setHeader("Content-Disposition", "attachment;filename="
//		                    + file.getName());// 设置在下载框默认显示的文件名
//					getResponse().setContentType("application/octet-stream");//指明response的返回对象是文件流
//					readFile = new FileInputStream(file);
//					in = new BufferedInputStream(readFile);
//					byte[] temp = new byte[1024 * 64];
//					int size = 0;
//					while ((size = in.read(temp)) != -1) {
//						out.write(temp, 0, size);
//					}
//					in.close();
////					out.flush();
////					out.close();
//					String delete= jsonObject.getString("delete");
//					if(!"".equals(delete) && delete != null && "1".equals(delete)){
//						file.delete();
//					}
//			}
			String path1 = (String) jsonObject.getString("filePath");
			ByteArrayOutputStream outputStream= null;
			BufferedInputStream bis = null;
			HttpURLConnection httpUrl = null;
			URL url = null;
			int BUFFER_SIZE = 1024;
			byte[] buf = new byte[BUFFER_SIZE];
			int size1 = 0;
			try {
				url = new URL(path1);
				httpUrl = (HttpURLConnection) url.openConnection();
				httpUrl.connect();
				bis = new BufferedInputStream(httpUrl.getInputStream());

				while ((size1 = bis.read(buf)) != -1) {
					outputStream.write(buf, 0, size1);
				}
				outputStream.flush();
			} catch (IOException e) {
			} catch (ClassCastException e) {
			} finally {
				try {
					outputStream.close();
					bis.close();
					httpUrl.disconnect();
				} catch (IOException e) {
				}
			}
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询文件出错\",\"success\":false}";
			LOGGER.info("appGetFile :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}finally{
			if(in != null){
				try{
				in.close();
			}catch(Exception e){
				
			}
			}
			if(out != null){
				try{
					out.close();
				}catch(Exception e){
					
				}
				
			}
			if(readFile != null){
				try{
					readFile.close();
				}catch(Exception e){
					
				}
				
			}
		}
	}
	/**
	 * 文件上传
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appUploadFile")
	public String appUploadFile(@RequestParam(name = "claimId", required = false) String claimId,//报账单id
							  @RequestParam(name = "userId") String userId,//登录用户ID
							  @RequestParam(name = "mobileId") String mobileId,//手机ID
							  @RequestParam(name = "ciphertext") String ciphertext,//加密串
								@RequestParam(name = "multiFiles") MultipartFile[] multiFiles) throws ClientProtocolException, IOException{
		try {
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			Map<String,Object> map=new HashMap<String,Object>();
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========

			//封装返回结果
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("claimId",claimId);
			try {

//				LOGGER.info("getUploadFilePath REQUEST: " + baseUrl
//						+ "commonComponentAppServlet?claimId=" + claimId + "&servletName=" + "getUploadFilePath"+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
//
//				String localUploadPath = HttpServiceUtil.sendGet(baseUrl
//						+ "commonComponentAppServlet?claimId=" + claimId + "&servletName=" + "getUploadFilePath"+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
//				localUploadPath = URLDecoder.decode(localUploadPath,"UTF-8");
//				if(localUploadPath==null || "".equals(localUploadPath) || "1".equals(localUploadPath)){
//					String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
//					LOGGER.info("appUploadFile :" + str);
////					getRequest().setAttribute("object", str);
////					writeString(str);
//					return str;
//				}
//				Map<String,Object> mapResult =getMultipartFileList(multipartFile);
//				File[] multiFiles = (File[]) mapResult.get("multiFiles");
//				String[] fileNames =  (String[]) mapResult.get("fileName");
				List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		    	// 上传图片文件
				if (multiFiles != null && multiFiles.length > 0) {
					 for (int i=0;i<multiFiles.length;i++) {
					    // 文件名
		                String fileName = multiFiles[i].getOriginalFilename();
//		                String fileName ="";
		                if (!"".equalsIgnoreCase(fileName)) {
//		                	String newFileName = "";
//		                	java.util.Date dt = new java.util.Date();
//		                	newFileName += "RMB_";
//		                	newFileName += dt.getTime();
//		                	newFileName += "." + getFileExt(fileName);
//		                	String newPath = localUploadPath+"/"+newFileName;
//		                	// 生成文件
//		                	File newFile = new File(newPath);
////		                	if(!newFile.exists()){
////		                		newFile.mkdir();
////							}
//							String[] originalFilename = fileName.split("\\.");
//
////							FileUtils.copyFile();
//		                	multiFiles[i].transferTo(File.createTempFile(originalFilename[0], originalFilename[1]));
//		                	FileUtils.copyFile(File.createTempFile(originalFilename[0], originalFilename[1]),newFile);
////		                	multiFiles[i].
//		    				FileUtils.forceDelete(File.createTempFile(originalFilename[0], originalFilename[1]));
							LOGGER.info("getUploadFilePath REQUEST: " + baseUrl
									+ "commonComponentAppServlet?claimId=" + claimId + "&servletName=" + "getCompId"+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);

							String compId = HttpServiceUtil.sendGet(baseUrl
									+ "commonComponentAppServlet?claimId=" + claimId + "&servletName=" + "getCompId"+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
							compId = URLDecoder.decode(compId,"UTF-8");
							if(compId==null || "".equals(compId)){
								String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
								LOGGER.info("appUploadFile :" + str);
								return str;
							}
							JSONObject jresult = new JSONObject();
							JSONObject jsonparam = new JSONObject();
							jsonparam.put("userId",userId);
							jsonparam.put("companyCode",compId);
							jsonparam.put("filename", UUID.randomUUID().toString());
							jsonparam.put("multipartFile",multiFiles[i]);
//			String url="http://10.8.133.19:7007/invTaxOcrInterfaceController/getInvOcrInfo";
							//文件上传腾讯云
							jresult=httpPost(uploadUrl,jsonparam,false);
							if(jresult.getString("code").equals("1")) {
								String newPath="";
								LOGGER.info("saveAttachment REQUEST: " + baseUrl
										+ "commonComponentAppServlet?servletName=" + "saveAttachment"
										+ "&fileName=" + URLEncoder.encode(fileName, "UTF-8") + "&newPath=" + newPath + "&claimId=" + claimId + "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext + i);

								String s = HttpServiceUtil.sendGet(baseUrl
										+ "commonComponentAppServlet?servletName=" + "saveAttachment"
										+ "&fileName=" + URLEncoder.encode(fileName, "UTF-8") + "&newPath=" + newPath + "&claimId=" + claimId + "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext + i);
								Map<String, Object> mapS = new HashMap<String, Object>();
								mapS.put("fileName", fileName);
								mapS.put("status", URLDecoder.decode(s, "UTF-8"));//0失败；1成功
								mapS.put("path", newPath);
								resultList.add(mapS);
							}else{
								String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
								LOGGER.info("appUploadFile :" + str);
								return str;
							}
		                }
		            }
					 Map<String,Object> mapR = new HashMap<String,Object>();
					 mapR.put("code", "0");
					 mapR.put("result", resultList);
					 mapR.put("message", "文件上传成功");
					 mapR.put("success", "true");
					 String str = JSONObject.toJSONString(mapR,SerializerFeature.WriteMapNullValue);
					// getRequest().setAttribute("object", str);
//					 writeString(str);
					 return str;
				}else{
					String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
					LOGGER.info("appUploadFile :" + str);
//					getRequest().setAttribute("object", str);
//					writeString(str);
					return str;
				}
			} catch (IllegalStateException e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
				LOGGER.info("appUploadFile :" + str);
//				getRequest().setAttribute("object", str);
//				writeString(str);
				e.printStackTrace();
				return str;
			} catch (IOException e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
				LOGGER.info("appUploadFile :" + str);
//				getRequest().setAttribute("object", str);
//				writeString(str);
				e.printStackTrace();
				return str;
			} catch (Exception e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
				LOGGER.info("appUploadFile :" + str);
//				getRequest().setAttribute("object", str);
//				writeString(str);
				e.printStackTrace();
				return str;
			}
		} catch (Exception e){
			String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
			LOGGER.info("appUploadFile :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 影像上传
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
@RequestMapping("appUploadEimFile")
	public String appUploadEimFile(@RequestParam(name = "claimId", required = false) String claimId,//报账单id
								  @RequestParam(name = "userId") String userId,//登录用户ID
								  @RequestParam(name = "userFullName", required = false) String userFullName,//当前登录人
								  @RequestParam(name = "mobileId") String mobileId,//手机ID
								  @RequestParam(name = "ciphertext") String ciphertext ,
								  @RequestParam(name= "multiFiles",required = false) MultipartFile[] multiFiles) throws ClientProtocolException, IOException{//加密串
		try {
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========

			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("claimId",claimId);
			map1.put("userFullName",userFullName);
			String eimScanningType = "报账单";//类型
			//封装返回结果
			Map<String,Object> map = new HashMap<String,Object>();
			
			//生成批次号
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String batchNum = sdf.format(date);
			Random random = new Random();
			batchNum += random.nextInt(9999999);
			
			try {
				//查询权限
				LOGGER.info(
						"isAllowdUploadImage REQUEST: " + baseUrl + "commonComponentAppServlet?claimId="
								+ claimId + "&servletName=" + "isAllowdUploadImage" + "&userId=" + userId + "&mobileId="
								+ mobileId + "&ciphertext=" + ciphertext);

				String isAllowdUploadImage = HttpServiceUtil
						.sendGet(baseUrl + "commonComponentAppServlet?claimId=" + claimId
								+ "&servletName=" + "isAllowdUploadImage" + "&userId=" + userId + "&mobileId=" + mobileId
								+ "&ciphertext=" + ciphertext);
				isAllowdUploadImage = URLDecoder.decode(isAllowdUploadImage,"UTF-8");
				
				if(isAllowdUploadImage.equals("false")){
					String str = "{\"code\":1,\"result\":null,\"message\":\"上传权限已被禁用，请联系管理员\",\"success\":false}";
					LOGGER.info("str :" + str);
					LOGGER.info("str :" + str);
//					getRequest().setAttribute("object", str);
//					writeString(str);
					return str;
				}
				
				String isAllowdUploadImageSSP = HttpServiceUtil
						.sendGet(baseUrl + "commonComponentAppServlet?claimId=" + claimId
								+ "&servletName=" + "isAllowdUploadImageSSP" + "&userId=" + userId + "&mobileId=" + mobileId
								+ "&ciphertext=SSP_" + ciphertext);
				isAllowdUploadImageSSP = URLDecoder.decode(isAllowdUploadImageSSP,"UTF-8");
				
				if(isAllowdUploadImageSSP.equals("false")){
					String str = "{\"code\":1,\"result\":null,\"message\":\"该报账单影像已由支持岗挂接，不允许起草人挂接\",\"success\":false}";
					LOGGER.info("str :" + str);
					LOGGER.info("str :" + str);
//					getRequest().setAttribute("object", str);
//					writeString(str);
					return str;
				}
				
				String errMsg = HttpServiceUtil
						.sendGet(baseUrl + "commonComponentAppServlet?claimId=" + claimId
								+ "&servletName=" + "isAllowdUploadImageOther" + "&userId=" + userId + "&mobileId=" + mobileId
								+ "&ciphertext=OTHER_" + ciphertext);
				errMsg = URLDecoder.decode(errMsg,"UTF-8");
				
				if(!"".equals(errMsg)){
					String str = "{\"code\":1,\"result\":null,\"message\":\"" + errMsg + "\",\"success\":false}";
					LOGGER.info("str :" + str);
					LOGGER.info("str :" + str);
//					getRequest().setAttribute("object", str);
//					writeString(str);
					return str;
				}
				LOGGER.info("getUploadFilePath REQUEST: " + baseUrl
						+ "commonComponentAppServlet?claimId=" + claimId + "&servletName=" + "getCompId"+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);

				String compId = HttpServiceUtil.sendGet(baseUrl
						+ "commonComponentAppServlet?claimId=" + claimId + "&servletName=" + "getCompId"+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
				compId = URLDecoder.decode(compId,"UTF-8");
				if(compId==null || "".equals(compId)){
					String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
					LOGGER.info("appUploadFile :" + str);
					return str;
				}
				
				//获取影像上传路径
				LOGGER.info(
						"appUploadEimFile REQUEST: " + baseUrl + "commonComponentAppServlet?claimId="
								+ claimId + "&servletName=" + "appUploadEimFile" + "&userId=" + userId + "&mobileId="
								+ mobileId + "&ciphertext=" + ciphertext + "11&batchNum=" + batchNum);

				String localUploadPath = HttpServiceUtil
						.sendGet(baseUrl + "commonComponentAppServlet?claimId=" + claimId
								+ "&servletName=" + "appUploadEimFile" + "&userId=" + userId + "&mobileId=" + mobileId
								+ "&ciphertext=" + ciphertext + "11&batchNum=" + batchNum);
				localUploadPath = URLDecoder.decode(localUploadPath,"UTF-8");

				//获取缩略影像上传路径
				LOGGER.info(
						"appUploadEimFile REQUEST: " + baseUrl + "commonComponentAppServlet?claimId="
								+ claimId + "&servletName=" + "appUploadEimSmallFile" + "&userId=" + userId + "&mobileId="
								+ mobileId + "&ciphertext=" + ciphertext + "22&batchNum=" + batchNum);

				String localSmallUploadPath = HttpServiceUtil
						.sendGet(baseUrl + "commonComponentAppServlet?claimId=" + claimId
								+ "&servletName=" + "appUploadEimSmallFile" + "&userId=" + userId + "&mobileId=" + mobileId
								+ "&ciphertext=" + ciphertext + "22&batchNum=" + batchNum);
				localSmallUploadPath = URLDecoder.decode(localSmallUploadPath,"UTF-8");
				if(localUploadPath==null || "".equals(localUploadPath) || "1".equals(localUploadPath) || (localSmallUploadPath==null || "".equals(localSmallUploadPath) || "1".equals(localSmallUploadPath))){
					String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
					LOGGER.info("appUploadEimFile :" + str);
					LOGGER.info("appUploadEimSmallFile :" + str);
					return str;
				}

				List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		    	// 上传图片文件
				if (multiFiles != null && multiFiles.length > 0) {
					 for (int i=0;i<multiFiles.length;i++) {
					    // 文件名
		                String fileName = multiFiles[i].getOriginalFilename();
//		                String fileName ="";
		                if (!"".equalsIgnoreCase(fileName)) {
		                	fileName = fileName.replaceAll("%","");
		                	fileName = fileName.replaceAll("#","");
		                	fileName = fileName.replaceAll("~","");
		                	String newPath = localUploadPath+fileName;
		                	String saveFilePath = localSmallUploadPath+fileName;
		                	String  normalName=UUID.randomUUID().toString();
		                	// 生成影像文件
							File newFile = new File(newPath);
							multiFiles[i].transferTo(newFile);
							JSONObject jresult = new JSONObject();
							JSONObject jsonparam = new JSONObject();
							jsonparam.put("userId",userId);
							jsonparam.put("companyCode",compId);
							jsonparam.put("filename",normalName);
							jsonparam.put("multipartFile",multiFiles[i]);
							//文件上传腾讯云
							jresult=httpPost(uploadUrl,jsonparam,false);
							if(jresult.getString("code").equals("1")){
								//生成缩略影像
								File saveFileSmall = new File(saveFilePath);
								ImageSizer.resize(newFile, saveFileSmall, 300, "jpg");
								FileUtils.forceDelete(newFile);
								InputStream in=new FileInputStream(saveFilePath);
								multiFiles[i]=new MockMultipartFile(saveFileSmall.getName(),in);
								jresult = new JSONObject();
								jsonparam = new JSONObject();
								jsonparam.put("userId",userId);
								jsonparam.put("companyCode",compId);
								jsonparam.put("filename",normalName);
								jsonparam.put("multipartFile",multiFiles[i]);
								//文件上传腾讯云
								jresult=httpPost(uploadUrl,jsonparam,false);
								FileUtils.forceDelete(saveFileSmall);
								if (jresult.getString("code").equals("1")){
									LOGGER.info("saveEimImage REQUEST: " + baseUrl
											+ "commonComponentAppServlet?servletName=" + "saveEimImage" + "&fileName="
											+ URLEncoder.encode(normalName, "UTF-8")  + "&claimId="+ claimId + "&userId="
											+ userId + "&mobileId=" + mobileId + "&ciphertext="+ ciphertext + i
											+ "&batchNum=" + batchNum + "&eimScanningType=" + eimScanningType);
//										+ "&userFullName=" + userFullName+"&localUploadPath="+localUploadPath+"&localSmallUploadPath="+localSmallUploadPath);

									String s = HttpServiceUtil.sendGet(
											baseUrl + "commonComponentAppServlet?servletName=" + "saveEimImage"
													+ "&fileName=" + URLEncoder.encode(normalName, "UTF-8")  + "&claimId=" + claimId
													+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext + i
													+ "&batchNum=" + batchNum + "&eimScanningType=" + eimScanningType + "&userFullName=" + userFullName+"&file="+multiFiles[i]);
									Map<String,Object> result =	JSONObject.parseObject(URLDecoder.decode(s,"UTF-8"),  Map.class);
									resultList.add(result);
								}else{
									String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
									LOGGER.error("appUploadEimFile :" + str);
									return str;
								}

							}else{
								String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
								LOGGER.error("appUploadEimFile :" + str);
								return str;
							}

		                }
		            }
					 Map<String,Object> mapR = new HashMap<String,Object>();
					 mapR.put("code", "0");
					 mapR.put("result", resultList);
					 mapR.put("message", "文件操作成功");
					 mapR.put("success", "true");
					 String str = JSONObject.toJSONString(mapR,SerializerFeature.WriteMapNullValue);
//					 getRequest().setAttribute("object", str);
//					 writeString(str);
					 return  str;
				}else{
					String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
					LOGGER.error("appUploadEimFile :" + str);
//					getRequest().setAttribute("object", str);
//					writeString(str);
					return str;
				}
			} catch (IllegalStateException e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
				LOGGER.error("appUploadEimFile :" + str);
//				getRequest().setAttribute("object", str);
//				writeString(str);
				e.printStackTrace();
				return str;
			} catch (IOException e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
				LOGGER.error("appUploadEimFile :" + str);
//				getRequest().setAttribute("object", str);
//				writeString(str);
				e.printStackTrace();
				return str;
			} catch (Exception e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
				LOGGER.error("appUploadEimFile :" + str);
//				getRequest().setAttribute("object", str);
//				writeString(str);
				e.printStackTrace();
				return str;
			}
		} catch (Exception e){
			String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
			LOGGER.error("appUploadEimFile :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 影像删除
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appDeleteEimFile")
	public String  appDeleteEimFile(@RequestParam(name = "imgFileId", required = false) String imgFileId,//影像id
								 @RequestParam(name = "userId") String userId,//报账单id
								 @RequestParam(name = "mobileId") String mobileId,//报账单id
								 @RequestParam(name = "ciphertext") String ciphertext ) throws ClientProtocolException, IOException{
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

		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			LOGGER.info(
					"appDeleteEimFile REQUEST: " + baseUrl + "commonComponentAppServlet?"+"servletName=" + "appDeleteEimFile" 
			+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext + "&imgFileId=" + imgFileId);

			String s = HttpServiceUtil
					.sendGet(baseUrl + "commonComponentAppServlet?"+"servletName=" + "appDeleteEimFile" 
							+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext + "&imgFileId=" + imgFileId);
			Map<String,Object> result =	JSONObject.parseObject(URLDecoder.decode(s,"UTF-8"),  Map.class);
			resultList.add(result);
			 Map<String,Object> mapR = new HashMap<String,Object>();
			 mapR.put("code", "0");
			 mapR.put("result", resultList);
			 mapR.put("message", "文件操作成功");
			 mapR.put("success", "true");
			 String str = JSONObject.toJSONString(mapR,SerializerFeature.WriteMapNullValue);
//			 getRequest().setAttribute("object", str);
			 return str;
		}catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"影像删除失败\",\"success\":false}";
			LOGGER.error("appDeleteEimFile :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 获取文件后缀
	 * @param name
	 * @return
	 */
	String getFileExt(String name){
		if ( name == null ) return null;
		int pos;
		pos = name.lastIndexOf('.');
		if ( pos >= 0 ) return name.substring(pos+1);
		return "";
	}
	/** 
     * 获取当前请求中的文件列表 
     *  
     * @return
     */  
    protected Map<String,Object> getMultipartFileList(MultipartFile[] files) {
//        List<MultipartFile> files = new ArrayList<MultipartFile>();
//    	File[] files = null;
        try{ 
//        	Map<String,Object> map = new HashMap<String,Object>();
//        	MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) request;
//			MultiPartRequestWrapper multiWrapper = MultipartFile
//			Enumeration fileParameterNames = multiWrapper.getFileParameterNames();

//        	while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
                // get the value of this input tag
//    			for(MultipartFile file : files){
//                     //[得到下一个文件域的name属性值]
//					String inputName = (String) file.getName();
//					// get the content type
//					//[得到这个请求中的mime文件类型数组]
//					String contentType = file.getContentType();
//
//					//[如果这个上下文类型数组非空]
//					if (contentType!=null && !"".equals(contentType)) {
//						// get the name of the file from the input tag
//						//[得到文件名数组]
//						String[] fileName = multiWrapper.getFileNames(inputName);
//						map.put("fileName", fileName);
//						//如果文件名数组是非空的（有文件被选择）
//						if (fileName!=null && !"".equals(fileName)) {
//							// get a File object for the uploaded File
//							//[得到文件数组]
//							files = multiWrapper.getFiles(inputName);
//							map.put("multiFiles", files);
//							return map;
//						}
//					}
//				}
//            }
        } catch (Exception ex) {
        	ex.printStackTrace();
        	return null;
        }
        return null;
    }
	/**
	 * 获取报账单详情
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetRmbsClaim")
	public String appGetRmbsClaim(@RequestParam(name = "userId") String userId,//影像id
								@RequestParam(name = "mobileId") String mobileId,//影像id
								@RequestParam(name = "claimNo", required = false) String claimNo,//影像id
								@RequestParam(name = "ciphertext") String ciphertext,//影像id
								@RequestParam(name = "encodeStr", required = false) String encodeStr,//影像id
								@RequestParam(name = "privilege", required = false) String privilege) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			//String resultString ="0";
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			LOGGER.info("appGetRmbsClaim REQUEST: " + baseUrl
					+ "commonHandleAppServlet?userId=" + userId + "&servletName=" + "appGetRmbsClaim"
					 + "&claimNo=" + claimNo + "&encodeStr=" + encodeStr + "&privilege=" + privilege + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?userId=" + userId + "&servletName=" + "appGetRmbsClaim"
					 + "&claimNo=" + claimNo + "&encodeStr=" + encodeStr + "&privilege=" + privilege + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetRmbsClaim :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询报账单详情出错\",\"success\":false}";
			LOGGER.info("appGetRmbsClaim :" + str);
			//getRequest().setAttribute("object", str);
			//writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 获取报账单详情-草稿
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetDrafRmbsClaim")
	public String appGetDrafRmbsClaim(@RequestParam(name = "userId") String userId,
									  @RequestParam(name = "mobileId") String mobileId,//手机ID
									  @RequestParam(name = "ciphertext") String ciphertext,
									  @RequestParam(name = "claimNo",required = false) String claimNo,
									  @RequestParam(name = "encodeStr",required = false) String encodeStr) throws ClientProtocolException, IOException{
		try{
//			String userId = getRequest().getParameter("userId");//报账单模板
//			String mobileId = getRequest().getParameter("mobileId");//业务大类
//			String claimNo = getRequest().getParameter("claimNo");//ouId
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String encodeStr = getRequest().getParameter("encodeStr");//二维码时效
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
			LOGGER.info("appGetDrafRmbsClaim REQUEST: " + baseUrl
					+ "commonHandleAppServlet?userId=" + userId + "&servletName=" + "appGetDrafRmbsClaim"
					 + "&claimNo=" + claimNo + "&encodeStr=" + encodeStr + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?userId=" + userId + "&servletName=" + "appGetDrafRmbsClaim"
					 + "&claimNo=" + claimNo + "&encodeStr=" + encodeStr + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetDrafRmbsClaim :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询报账单详情出错\",\"success\":false}";
			LOGGER.info("appGetDrafRmbsClaim :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 删除-借款核销
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appDeleteClaimRel")
	public String appDeleteClaimRel(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
									@RequestParam(name = "deleteIds",required = false) String deleteIds,
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
//			String userId = getRequest().getParameter("userId");//报账单模板
//			String mobileId = getRequest().getParameter("mobileId");//业务大类
//			String deleteIds = getRequest().getParameter("deleteIds");//核销行id --逗号分隔
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String claimId = getRequest().getParameter("claimId");//报账单id
			
			LOGGER.info("appDeleteClaimRel REQUEST: " + baseUrl
					+ "commonHandleAppServlet?userId=" + userId + "&servletName=" + "appDeleteClaimRel"
					+ "&deleteIds=" + deleteIds+ "&claimId=" + claimId  + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?userId=" + userId + "&servletName=" + "appDeleteClaimRel"
					+ "&deleteIds=" + deleteIds+ "&claimId=" + claimId  + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appDeleteClaimRel :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"删除借款核销出错\",\"success\":false}";
			LOGGER.info("appDeleteClaimRel :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 删除-计划付款
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appDeletePayList")
	public String appDeletePayList(@RequestParam(name = "userId") String userId,
								   @RequestParam(name = "mobileId") String mobileId,//手机ID
								   @RequestParam(name = "ciphertext") String ciphertext,
								   @RequestParam(name = "deleteIds",required = false) String deleteIds,
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
//			String userId = getRequest().getParameter("userId");//报账单模板
//			String mobileId = getRequest().getParameter("mobileId");//业务大类
//			String deleteIds = getRequest().getParameter("deleteIds");//计划付款行id --逗号分隔
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String claimId = getRequest().getParameter("claimId");//报账单id
			
			LOGGER.info("appDeletePayList REQUEST: " + baseUrl
					+ "commonHandleAppServlet?userId=" + userId + "&servletName=" + "appDeletePayList"
					+ "&deleteIds=" + deleteIds+ "&claimId=" + claimId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?userId=" + userId + "&servletName=" + "appDeletePayList"
					+ "&deleteIds=" + deleteIds+ "&claimId=" + claimId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appDeletePayList :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"删除计划付款出错\",\"success\":false}";
			LOGGER.info("appDeletePayList :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 报账单模板
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetItemList")
	public String  appGetItemList(@RequestParam(name = "userId") String userId,
								  @RequestParam(name = "mobileId") String mobileId,//手机ID
								  @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
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
			LOGGER.info("getItemListAppServlet REQUEST: " + baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetItemList"
					 + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonComponentAppServlet?servletName=" + "appGetItemList"
					 + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("getItemListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
			//return SHOW_ERROR;
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询报账单模板出错\",\"success\":false}";
			LOGGER.info("getItemListAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
			//return SHOW_ERROR;
		}
	}
	/**
	 * 我的待办查询
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetTodoList")
	public String  appGetTodoList(@RequestParam(name = "userNum", required = false) String userNum,//当前登录人
								  @RequestParam(name = "claimNo",required = false) String claimNo,//报账单号
								  @RequestParam(name = "itemId", required = false) String itemId,//报账单模板
								  @RequestParam(name = "item2Id", required = false) String item2Id,//业务大类
								  @RequestParam(name = "applyUserNum", required = false) String applyUserNum,//起草人
								  @RequestParam(name = "applyAmountStart", required = false) String applyAmountStart,//报账金额
								  @RequestParam(name = "applyAmountEnd", required = false) String applyAmountEnd,//报账金额
								  @RequestParam(name = "commonQuery", required = false) String commonQuery,//公共查询
								  @RequestParam(name = "applyDateStart", required = false) String applyDateStart,//起草时间开始
								  @RequestParam(name = "applyDateEnd", required = false) String applyDateEnd,//起草时间结束
								  @RequestParam(name = "userId") String userId,//登录用户ID
								  @RequestParam(name = "curPage", required = false) String curPage,//分页
								  @RequestParam(name = "pageSize", required = false) String pageSize,//分页
								  @RequestParam(name = "curReceiveDateStart", required = false) String curReceiveDateStart,//处理时间开始
								  @RequestParam(name = "curReceiveDateEnd", required = false) String curReceiveDateEnd,//处理时间结束
								  @RequestParam(name = "mobileId") String mobileId,//手机ID
								  @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			//String resultString="0";
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String userNum = getRequest().getParameter("userNum");//当前登录人
//			String claimNo = getRequest().getParameter("claimNo");//报账单号
//			String itemId = getRequest().getParameter("itemId");//报账单模板
//			String item2Id = getRequest().getParameter("item2Id");//业务大类
//			String applyUserNum = getRequest().getParameter("applyUserNum");//起草人
//			String applyAmountStart = getRequest().getParameter("applyAmountStart");//报账金额
//			String applyAmountEnd = getRequest().getParameter("applyAmountEnd");//报账金额
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//			String commonQuery = getRequest().getParameter("commonQuery");//公共查询
//			String applyDateStart = getRequest().getParameter("applyDateStart");//起草时间开始
//			String applyDateEnd = getRequest().getParameter("applyDateEnd");//起草时间结束
			//String curReceiveDateStart = getRequest().getParameter("curReceiveDateStart");//接收时间开始
			//String curReceiveDateEnd = getRequest().getParameter("curReceiveDateEnd");//接收时间结束
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
//			if(!isUTFEncoding()){
//				if(applyUserNum != null && !("").equals(applyUserNum)){
//					applyUserNum = new String(applyUserNum.getBytes("ISO-8859-1"), "UTF-8");
//				}
//			}
			LOGGER.info("getTodoList REQUEST: " + baseUrl
					+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "appGetTodoList"
					 + "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
					 + "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&curReceiveDateStart=" + curReceiveDateStart
					 + "&curReceiveDateEnd=" + curReceiveDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "appGetTodoList"
					 + "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
					 + "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&curReceiveDateStart=" + curReceiveDateStart
					 + "&curReceiveDateEnd=" + curReceiveDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			LOGGER.info("getTodoList :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"我的待办查询结果出错\",\"success\":false}";
			LOGGER.info("getTodoList :" + str);
			//getRequest().setAttribute("object", str);
			//writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 我的已办单查询
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetMyDoneList")
	public String  appGetMyDoneList(@RequestParam(name = "userNum", required = false) String userNum,//当前登录人
									@RequestParam(name = "claimNo", required = false) String claimNo,//报账单号
									@RequestParam(name = "itemId", required = false) String itemId,//报账单模板
									@RequestParam(name = "item2Id", required = false) String item2Id,//业务大类
									@RequestParam(name = "applyUserNum", required = false) String applyUserNum,//起草人
									@RequestParam(name = "applyAmountStart", required = false) String applyAmountStart,//报账金额
									@RequestParam(name = "applyAmountEnd", required = false) String applyAmountEnd,//报账金额
									@RequestParam(name = "commonQuery", required = false) String commonQuery,//公共查询
									@RequestParam(name = "applyDateStart", required = false) String applyDateStart,//起草时间开始
									@RequestParam(name = "applyDateEnd", required = false) String applyDateEnd,//起草时间结束
									@RequestParam(name = "userId") String userId,//登录用户ID
									@RequestParam(name = "curPage", required = false) String curPage,//分页
									@RequestParam(name = "pageSize", required = false) String pageSize,//分页
									@RequestParam(name = "endDateStart", required = false) String endDateStart,//处理时间开始
									@RequestParam(name = "endDateEnd", required = false) String endDateEnd,//处理时间结束
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
//			String userNum = getRequest().getParameter("userNum");//当前登录人
//			String claimNo = getRequest().getParameter("claimNo");//报账单号
//			String itemId = getRequest().getParameter("itemId");//报账单模板
//			String item2Id = getRequest().getParameter("item2Id");//业务大类
//			String applyUserNum = getRequest().getParameter("applyUserNum");//起草人
//			String applyAmountStart = getRequest().getParameter("applyAmountStart");//报账金额
//			String applyAmountEnd = getRequest().getParameter("applyAmountEnd");//报账金额
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//			String commonQuery = getRequest().getParameter("commonQuery");//公共查询
//			String endDateStart = getRequest().getParameter("applyDateStart");//处理时间开始
//			String endDateEnd = getRequest().getParameter("applyDateEnd");//处理时间结束
//			String curReceiveDateStart = getRequest().getParameter("curReceiveDateStart");//接收时间开始
//			String curReceiveDateEnd = getRequest().getParameter("curReceiveDateEnd");//接收时间结束
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			if(!isUTFEncoding()){
				if(applyUserNum != null && !("").equals(applyUserNum)){
					applyUserNum = new String(applyUserNum.getBytes("ISO-8859-1"), "UTF-8");
				}
			}
			LOGGER.info("getMyDoneListAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "appGetMyDoneList"
					 + "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
					 + "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&endDateStart=" + endDateStart
					 + "&endDateEnd=" + endDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "appGetMyDoneList"
					+ "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
					+ "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
					+ "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&endDateStart=" + endDateStart
					+ "&endDateEnd=" + endDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("getMyDoneListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"我的报已办查询结果出错\",\"success\":false}";
			LOGGER.info("getMyDoneListAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 借阅审核列表
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetAamBorrowAudit")
	public String  appGetAamBorrowAudit(@RequestParam(name = "userNum", required = false) String userNum,//当前登录人
										@RequestParam(name = "commonQuery", required = false) String applyUserNum,//当前登录人
										//@RequestParam(name = "userNum") String userNum,//当前登录人
										@RequestParam(name = "odBorrowedFlag", required = false) String odBorrowedFlag,//借阅类型
										@RequestParam(name = "archiveType", required = false) String archiveType,//档案类型
										@RequestParam(name = "userId") String userId,//档案类型
										@RequestParam(name = "curPage", required = false) String curPage,//分页
										@RequestParam(name = "pageSize", required = false) String pageSize,//分页
										@RequestParam(name = "startDate", required = false) String startDate,//处理时间开始
										@RequestParam(name = "endDate", required = false) String endDate,//处理时间结束
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
//			String userNum = getRequest().getParameter("userNum");//当前登录人
//			String applyUserNum = getRequest().getParameter("commonQuery");//applyUserNum起草人
//			String startDate = getRequest().getParameter("startDate");//申请时间
//			String endDate = getRequest().getParameter("endDate");//申请时间
//			String odBorrowedFlag= getRequest().getParameter("odBorrowedFlag");//借阅类型
//			String archiveType= getRequest().getParameter("archiveType");//档案类型
////			String isChecked= getRequest().getParameter("isChecked");//是否审核
//
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
//			if(!isUTFEncoding()){
//				if(applyUserNum != null && !("").equals(applyUserNum)){
//					applyUserNum = new String(applyUserNum.getBytes("ISO-8859-1"), "UTF-8");
//				}
//			}
			
			LOGGER.info("getAamBorrowAuditListAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?applyUserNum=" + applyUserNum + "&startDate=" + startDate + "&servletName=" + "appGetAamBorrowAudit"
					 + "&endDate=" + endDate + "&odBorrowedFlag=" + odBorrowedFlag + "&archiveType=" + archiveType
					 + "&isChecked=" + "unChecked" + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?applyUserNum=" + applyUserNum + "&startDate=" + startDate + "&servletName=" + "appGetAamBorrowAudit"
					 + "&endDate=" + endDate + "&odBorrowedFlag=" + odBorrowedFlag + "&archiveType=" + archiveType
					 + "&isChecked=" + "unChecked" + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("getAamBorrowAuditListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"借阅审核查询结果出错\",\"success\":false}";
			LOGGER.info("getAamBorrowAuditListAppServlet :" + str);
			//getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 我的代付款审核
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetMyTodoPayList")
	public String  appGetMyTodoPayList(@RequestParam(name = "userNum", required = false) String userNum,//当前登录人
									   @RequestParam(name = "commonQuery", required = false) String commonQuery,//当前登录人
									   @RequestParam(name = "payAmountStart", required = false) String payAmountStart,//当前登录人
									   @RequestParam(name = "payAmountEnd", required = false) String payAmountEnd,//当前登录人
									   @RequestParam(name = "claimNo", required = false) String claimNo,//借阅类型
									   @RequestParam(name = "payTypeId", required = false) String payTypeId,//档案类型
									   @RequestParam(name = "userId") String userId,//档案类型
									   @RequestParam(name = "curPage", required = false) String curPage,//分页
									   @RequestParam(name = "pageSize", required = false) String pageSize,//分页
									   @RequestParam(name = "startDate", required = false) String startDate,//处理时间开始
									   @RequestParam(name = "endDate", required = false) String endDate,//处理时间结束
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
//			String userNum = getRequest().getParameter("userNum");//当前登录人
			//提单人provFinamgrId、报账单编号claimNo、付款方式payTypeId、付款日期startDate,endDate、供应商vendorName
//			String commonQuery = getRequest().getParameter("commonQuery");//提单人编码或名称
//			String claimNo= getRequest().getParameter("claimNo");//报账单编号 ,手机端去掉（未更改代码）
//			String payTypeId= getRequest().getParameter("payTypeId");//付款方式,手机端去掉（未更改代码）
//			String startDate = getRequest().getParameter("startDate");//付款日期,手机端去掉（未更改代码）
//			String endDate = getRequest().getParameter("endDate");//付款日期,手机端去掉（未更改代码）
//			String payAmountStart= getRequest().getParameter("payAmountStart");//付款金额
//			String payAmountEnd= getRequest().getParameter("payAmountEnd");//付款金额
//
//
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
//			if(!isUTFEncoding()){
//				if(vendorNameApp != null && !("").equals(vendorNameApp)){
//					vendorNameApp = new String(vendorNameApp.getBytes("ISO-8859-1"), "UTF-8");
//				}
//			}
			
			LOGGER.info("appGetMyTodoPayListAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?commonQuery=" + commonQuery + "&startDate=" + startDate
					 + "&endDate=" + endDate + "&claimNo=" + claimNo + "&payTypeId=" + payTypeId + "&servletName=" + "appGetMyTodoPayList"
					 + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					 + "&payAmountStart=" + payAmountStart + "&payAmountEnd=" + payAmountEnd);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?commonQuery=" + commonQuery + "&startDate=" + startDate
					 + "&endDate=" + endDate + "&claimNo=" + claimNo + "&payTypeId=" + payTypeId + "&servletName=" + "appGetMyTodoPayList"
					 + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					 + "&payAmountStart=" + payAmountStart + "&payAmountEnd=" + payAmountEnd);
			
			LOGGER.info("appGetMyTodoPayListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"我的待付款清单查询结果出错\",\"success\":false}";
			LOGGER.info("appGetMyTodoPayListAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询的代付款详情
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetPayListInfo")
	public String  appGetPayListInfo(@RequestParam(name = "userId") String userId,//手机ID
									 @RequestParam(name = "userNum", required = false) String userNum,//手机ID
									  @RequestParam(name = "mobileId") String mobileId,//手机ID
									 @RequestParam(name = "paylineId", required = false) String paylineId,//手机ID
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
			//提单人provFinamgrId、报账单编号claimNo、付款方式payTypeId、付款日期startDate,endDate、供应商vendorName
//			String paylineId = getRequest().getParameter("paylineId");//付款计划ID
//
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String userNum = getRequest().getParameter("userNum");//当前登录人
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
	
			
			LOGGER.info("appGetPayListInfo REQUEST: " + baseUrl
					+ "commonHandleAppServlet?paylineId=" + paylineId + "&servletName=" + "appGetPayListInfo"
					+ "&userId=" + userId+ "&userNum=" + userNum+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?paylineId=" + paylineId  + "&servletName=" + "appGetPayListInfo"
					+ "&userId=" + userId+ "&userNum=" + userNum+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetPayListInfo :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"我的待付款清单查询结果出错\",\"success\":false}";
			LOGGER.info("appGetMyTodoPayListAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 查询报账单审批下一个流程信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetNextSelect")
	public String  appGetNextSelect(@RequestParam(name = "userId") String userId,
								  @RequestParam(name = "mobileId") String mobileId,//手机ID
								  @RequestParam(name = "ciphertext") String ciphertext,
								  @RequestParam(name = "claimId",required = false) String claimId,
									@RequestParam(name = "nextActivityInstID",required = false) String nextActivityInstID,
									@RequestParam(name = "pendingId",required = false) String pendingId,
									@RequestParam(name = "hrNo",required = false) String hrNo,
									@RequestParam(name = "lockId",required = false) String lockId,
									@RequestParam(name = "lockTime",required = false) String lockTime,
									@RequestParam(name = "processStateEng",required = false) String processStateEng,
									@RequestParam(name = "comment",required = false) String comment) throws ClientProtocolException, IOException{
		
//		String claimId = getRequest().getParameter("claimId");
//		String pendingId = getRequest().getParameter("pendingId");
//		String nextActivityInstID = getRequest().getParameter("nextActivityInstID");
//		String hrNo = getRequest().getParameter("hrNo");
//		String lockId = getRequest().getParameter("lockId");
//		String lockTime = getRequest().getParameter("lockTime");
//		String processStateEng=getRequest().getParameter("processStateEng");//页面当前流程状态
//		String comment = getRequest().getParameter("comment");
		if(comment == null || "".equals(comment)){
			comment = "同意";
		}
//		if(!isUTFEncoding()){
//			comment = new String(comment.getBytes("ISO-8859-1"), "UTF-8");
//		}
		String str ="";
		try{
			//========加密验证 start==========
			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			LOGGER.info("getNextSelectApp REQUEST: " + baseUrl
					+ "mobileQueryNextActivityServlet?claimId=" + claimId + "&pendingId=" + pendingId + "&hrNo=" + hrNo
					+ "&nextActivityInstID=" + nextActivityInstID+"&processStateEng="+processStateEng);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "mobileQueryNextActivityServlet?claimId=" + claimId + "&pendingId=" + pendingId + "&hrNo=" + hrNo
					+ "&nextActivityInstID=" + nextActivityInstID+"&processStateEng="+processStateEng);
			LOGGER.info("getNextSelectApp :" + URLDecoder.decode(s,"UTF-8"));

			if(s.indexOf("error")>0){
				JSONObject jsonObject =JSONObject.parseObject(s);
				str = "{\"code\":1,\"result\":null,\"message\":\""+jsonObject.get("error")+"\",\"success\":false}";
//				writeString(str);
			}else{
				JSONArray jsonObject = JSONArray.parseArray(s);
				List<MobileNextActivityVo> list = new ArrayList<MobileNextActivityVo>();
				for(int i=0; i<jsonObject.size();i++){
					
					JSONObject object = jsonObject.getJSONObject(i);
					JSONArray jsonParticipant = JSONArray.parseArray(JSONObject.toJSONString(object.get("nextParticipant")));
					List<MobileParticipantVo> listParticipant = new ArrayList<MobileParticipantVo>();
					if(jsonParticipant != null){
						for(int j=0; j<jsonParticipant.size();j++){
							JSONObject objectParticipant = jsonParticipant.getJSONObject(j);
							listParticipant.add((MobileParticipantVo)JSONObject.toJavaObject(objectParticipant, MobileParticipantVo.class));
						}
					}
					MobileNextActivityVo mobileNextActivityVo = (MobileNextActivityVo)JSONObject.toJavaObject(object, MobileNextActivityVo.class);
					mobileNextActivityVo.setNextParticipant(listParticipant);
					list.add(mobileNextActivityVo);
				}
				
				//		getRequest().setAttribute("claimId", getRequest().getParameter("claimId"));
				//		getRequest().setAttribute("pendingId", getRequest().getParameter("pendingId"));
				//		getRequest().setAttribute("comment", comment);
				//		getRequest().setAttribute("nextActivityVoList", list);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("claimId", getRequest().getParameter("claimId"));//0：成功
				map.put("pendingId", getRequest().getParameter("pendingId"));//查询结果
				map.put("comment", comment);//信息
				map.put("lockId", lockId);//流程实例锁id
				map.put("lockTime", lockTime);//流程实例锁时间
				map.put("nextActivityVoList", list);//成功标志
				Map<String,Object> resultmap = new HashMap<String,Object>();
				resultmap.put("code", 0);//0：成功
				resultmap.put("result", map);//查询结果
				resultmap.put("message", "查询审核环节成功");//信息
				resultmap.put("success", true);//成功标志
				str = JSONObject.toJSONString(resultmap,SerializerFeature.WriteMapNullValue);
				// 下一环节只有一个审批人
				if(list.size() == 1){
					MobileNextActivityVo mobileNextActivityVo = list.get(0);
					if(!"灵活审批".equals(mobileNextActivityVo.getNextActivityName()) && mobileNextActivityVo.getNextParticipant().size() == 1){
						MobileParticipantVo mobileParticipantVo = mobileNextActivityVo.getNextParticipant().get(0);
						appSingleApprove(pendingId, mobileParticipantVo.getUserId()+"-"+mobileParticipantVo.getGroupId(), claimId, comment, mobileNextActivityVo.getNextActivityId(),lockId,lockTime);
					}
				}
			}

		} catch (Exception e) {
			str = "{\"code\":1,\"result\":null,\"message\":\"获取下一环节出错\",\"success\":false}";
			LOGGER.info("mobileQueryNextActivityServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
		return str;
	}	
	/**
	 * 审批通过
	 * @param pendingId
	 * @param userGroupId
	 * @param claimId
	 * @param comment
	 * @param nextActivityDefId
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appSingleApprove")
	public void  appSingleApprove(String pendingId, String userGroupId, String claimId, String comment, String nextActivityDefId,String lockId, String lockTime) throws ClientProtocolException, IOException{
		try{
			String nextUserId = userGroupId.split("-")[0];
			String nextUserGroupId = userGroupId.split("-")[1];
			
			LOGGER.info("singleApprove REQUEST:" + baseUrl
					+ "mobileProcessApproveServlet?claimId=" + claimId +
					"&pendingId=" + pendingId +
					"&nextActivityDefId=" + nextActivityDefId +
					"&nextUserId=" + nextUserId +
					"&nextUserGroupId=" + nextUserGroupId +
					"&lockId=" + lockId +
					"&lockTime=" + URLEncoder.encode(lockTime, "UTF-8") +
					"&comment=" + URLEncoder.encode(comment, "UTF-8"));
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "mobileProcessApproveServlet?claimId=" + claimId +
					"&pendingId=" + pendingId +
					"&nextActivityDefId=" + nextActivityDefId +
					"&nextUserId=" + nextUserId +
					"&nextUserGroupId=" + nextUserGroupId +
					"&lockId=" + lockId +
					"&lockTime=" + URLEncoder.encode(lockTime, "UTF-8") +
					"&comment=" + URLEncoder.encode(comment, "UTF-8")); 
			Map<String,Object> resultmap = new HashMap<String,Object>();
			if(s.indexOf("error")>0){
				JSONObject jsonObject =JSONObject.parseObject(s);
				String str = "{\"code\":1,\"result\":null,\"message\":\""+jsonObject.get("error")+"\",\"success\":false}";
				writeString(str);
			}else{
				resultmap.put("code", 3);//0：成功
				resultmap.put("result", new HashMap<String,String>());//查询结果
				resultmap.put("message", "发送成功");//信息
				resultmap.put("success", true);//成功标志
				String str = JSONObject.toJSONString(resultmap,SerializerFeature.WriteMapNullValue);
				writeString(str);
				LOGGER.info("singleApprove RESPONSE:" + s);
			}

		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"发送出错\",\"success\":false}";
			LOGGER.info("mobileProcessApproveServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();

		}
		
	}	
	
	public static void main(String args[]){
		List list = new ArrayList();
		Map<String,Object> resultmap = new HashMap<String,Object>();
		Map<String,String> map = new HashMap<String,String>();
		resultmap.put("result", map);
		String str = JSONObject.toJSONString(resultmap);
		System.out.println(str);
	}
	/**
	 * 审批通过
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appApprove")
	public String appApprove(
						   @RequestParam(name = "pendingId",required = false) String pendingId,
						   @RequestParam(name = "nextUserId",required = false) String nextUserId,
						   @RequestParam(name = "nextUserGroupId",required = false) String nextUserGroupId,
						   @RequestParam(name = "claimId",required = false) String claimId,
						   @RequestParam(name = "comment",required = false) String comment,
						   @RequestParam(name = "nextActivityDefId",required = false) String nextActivityDefId,
						   @RequestParam(name = "lockId",required = false) String lockId,
						   @RequestParam(name = "lockTime",required = false) String lockTime) throws ClientProtocolException, IOException{
		String str ="";
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//       param.setIsEmessageLogin(isEmessageLogin);
//			param.setUserId(userId);
//			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String pendingId = getRequest().getParameter("pendingId");
//			String nextUserId = getRequest().getParameter("nextUserId");
//			String nextUserGroupId = getRequest().getParameter("nextUserGroupId");
//			String claimId = getRequest().getParameter("claimId");
//			String comment = getRequest().getParameter("comment");
//			String nextActivityDefId = getRequest().getParameter("nextActivityDefId");
//			String lockId = getRequest().getParameter("lockId");
//			String lockTime = getRequest().getParameter("lockTime");
			if(comment == null || "".equals(comment)){
				comment = "同意";
			}
			LOGGER.info("approve REQUEST:" + baseUrl
					+ "mobileProcessApproveServlet?claimId=" + claimId +
					"&pendingId=" + pendingId +
					"&nextActivityDefId=" + nextActivityDefId +
					"&nextUserId=" + nextUserId +
					"&nextUserGroupId=" + nextUserGroupId +
					"&lockId=" + lockId +
					"&lockTime=" +  URLEncoder.encode(lockTime, "UTF-8") +
					"&comment=" + URLEncoder.encode(comment, "UTF-8"));
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "mobileProcessApproveServlet?claimId=" + claimId +
					"&pendingId=" + pendingId +
					"&nextActivityDefId=" + nextActivityDefId +
					"&nextUserId=" + nextUserId +
					"&nextUserGroupId=" + nextUserGroupId +
					"&lockId=" + lockId +
					"&lockTime=" + URLEncoder.encode(lockTime, "UTF-8") +
					"&comment=" + URLEncoder.encode(comment, "UTF-8")); 
			LOGGER.info("approve RESPONSE:" + s);
			Map<String,Object> resultmap = new HashMap<String,Object>();
			if(s.indexOf("error")>0){
				JSONObject jsonObject =JSONObject.parseObject(s);
				str = "{\"code\":1,\"result\":null,\"message\":\""+jsonObject.get("error")+"\",\"success\":false}";
				writeString(str);
			}else{
				resultmap.put("code", 3);//0：成功
				resultmap.put("result", null);//查询结果
				resultmap.put("message", "发送成功");//信息
				resultmap.put("success", true);//成功标志
				LOGGER.info("singleApprove RESPONSE:" + s);
				str = JSONObject.toJSONString(resultmap,SerializerFeature.WriteMapNullValue);
//				writeString(str);
			}
		} catch (Exception e) {
			str = "{\"code\":1,\"result\":null,\"message\":\""+e.getMessage()+"\",\"success\":false}";
			LOGGER.info("mobileProcessApproveServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
		}
//		JSONObject jsonObject = JSONObject.fromObject(s);
//		
//		if(s.indexOf("success") != -1){
//			result = (String)jsonObject.get("success");
//			return SHOW_TODO;
//		}
//		
//		if(s.indexOf("error") != -1){
//			result = (String)jsonObject.get("success");
//			return SHOW_RESULT;
//		}
//		
//		return SHOW_TODO;
		return str;
	}
	@RequestMapping("appSendback")
	public String appSendback(@RequestParam(name = "userId") String userId,
//							@RequestParam(name = "mobileId") String mobileId,//手机ID
//							@RequestParam(name = "ciphertext") String ciphertext,
							@RequestParam(name = "pendingId",required = false) String pendingId,
							@RequestParam(name = "claimId",required = false) String claimId,
							@RequestParam(name = "comment",required = false) String comment) throws ClientProtocolException, IOException{
		String str ="";
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//       param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
//			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String pendingId = getRequest().getParameter("pendingId");
//			String claimId = getRequest().getParameter("claimId");
//			String comment = getRequest().getParameter("comment");
//			String userId = getRequest().getParameter("userId");//登录用户ID
			
			LOGGER.info("sendback REQUEST:" + baseUrl
					+ "mobileProcessBackServlet?claimId=" + claimId +
					"&pendingId=" + pendingId +
					"&userId=" + userId +
					"&comment=" + URLEncoder.encode(comment, "UTF-8"));
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "mobileProcessBackServlet?claimId=" + claimId +
					"&pendingId=" + pendingId +
					"&userId=" + userId +
					"&comment=" + URLEncoder.encode(comment, "UTF-8"));
			LOGGER.info("sendback RESPONSE:" + s);
			Map<String,Object> resultmap = new HashMap<String,Object>();
			if(s.indexOf("error")>0){
				JSONObject jsonObject =JSONObject.parseObject(s);
				str = "{\"code\":1,\"result\":null,\"message\":\""+jsonObject.get("error")+"\",\"success\":false}";
//				writeString(str);
			}else{
				resultmap.put("code", 0);//0：成功
				resultmap.put("result", null);//查询结果
				resultmap.put("message", "退回成功");//信息
				resultmap.put("success", true);//成功标志
				LOGGER.info("singleApprove RESPONSE:" + s);
				str = JSONObject.toJSONString(resultmap,SerializerFeature.WriteMapNullValue);
//				writeString(str);
			}
		} catch (Exception e) {
			str = "{\"code\":1,\"result\":null,\"message\":\"退回出错\",\"success\":false}";
			LOGGER.info("mobileProcessApproveServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 删除草稿
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appDeleteMyDraft")
	public String appDeleteMyDraft(@RequestParam(name = "userId") String userId,
								   @RequestParam(name = "mobileId") String mobileId,//手机ID
								   @RequestParam(name = "ciphertext") String ciphertext,
								   @RequestParam(name = "appUserNum",required = false) String appUserNum,
								   @RequestParam(name = "itemId",required = false) String itemId,
								   @RequestParam(name = "deleteIds",required = false) String deleteIds,
								   @RequestParam(name = "compId",required = false) String compId,
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
			//requestId="+itemId+"ClaimDel&deleteIds="+claimId+"&compId="+compId+"&claimId="+claimId+"&processStateEng=rootDrafterActivity
//			String appUserNum = getRequest().getParameter("appUserNum");//当前登录人
//			String itemId = getRequest().getParameter("itemId");//报账单模板
//			String deleteIds = getRequest().getParameter("deleteIds");//删除报账单ID
//			String compId= getRequest().getParameter("compId");//登录人公司
//			String claimId= getRequest().getParameter("claimId");//报账单ID
////			String processStateEng = getRequest().getParameter("processStateEng");//流程状态
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appDeleteMyDraftAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?itemId=" + itemId + "&deleteIds=" + deleteIds
					+ "&compId=" + compId + "&claimId=" + claimId + "&processStateEng=" + "rootDrafterActivity"
					+ "&servletName=" + "appDeleteMyDraft" + "&appUserNum=" + appUserNum + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?itemId=" + itemId + "&deleteIds=" + deleteIds
					+ "&compId=" + compId + "&claimId=" + claimId + "&processStateEng=" + "rootDrafterActivity"
					+ "&servletName=" + "appDeleteMyDraft" + "&appUserNum=" + appUserNum + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appDeleteMyDraftAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"删除草稿出错\",\"success\":false}";
			LOGGER.info("appDeleteMyDraftAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 我的信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetMyUserInfo")
	public String  appGetMyUserInfo(@RequestParam(name = "userId") String userId,//当前登录人
									@RequestParam(name = "mobileId") String mobileId,//当前登录人
									@RequestParam(name = "ciphertext") String ciphertext ) throws ClientProtocolException, IOException{
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
//			String userId = getRequest().getParameter("userId");//当前登录人id
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetMyUserInfoAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetMyUserInfo"
					 + "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetMyUserInfo"
					 + "&userId=" + userId + "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetMyUserInfoAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询我的信息出错\",\"success\":false}";
			LOGGER.info("appGetMyUserInfoAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	private boolean securityCheck(){
		String mobileId = getRequest().getParameter("mobileId");
		String ciphertext = getRequest().getParameter("ciphertext");
		String [] stringArr= mobileId.split("_"); 
		try{
			
			String encodeStr = DESUtilApp.encode(DESUtilApp.KEY, stringArr[0]+stringArr[1]+DESUtilApp.KEY);
			if(encodeStr.equals(ciphertext)){
				return true;
			}else {
				return false;
			}
		}catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 审核借阅详情页面
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetAamReviewedInfo")
	public String appGetAamReviewedInfo(@RequestParam(name = "userId") String userId,//当前登录人
									  @RequestParam(name = "mobileId") String mobileId,//当前登录人
									  @RequestParam(name = "ciphertext") String ciphertext,
									  @RequestParam(name = "borrowingReqId", required = false) String borrowingReqId,//当前登录人
									  @RequestParam(name = "processStatus", required = false) String processStatus  ) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String borrowingReqId = getRequest().getParameter("borrowingReqId");//借阅申请ID
//			String processStatus = getRequest().getParameter("processStatus");
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appGetAamReviewedInfoAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetAamReviewedInfo"
					+ "&borrowingReqId=" + borrowingReqId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&processStatus=" + processStatus);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetAamReviewedInfo"
					+ "&borrowingReqId=" + borrowingReqId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&processStatus=" + processStatus);
			
			LOGGER.info("appGetAamReviewedInfoAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"跳转审核借阅详情界面出错\",\"success\":false}";
			LOGGER.info("appGetAamReviewedInfoAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 审核借阅查询审批人
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetAddAuditer")
	public String appGetAddAuditer(@RequestParam(name = "userId") String userId,//当前登录人
								 @RequestParam(name = "mobileId") String mobileId,//当前登录人
								 @RequestParam(name = "ciphertext") String ciphertext,
								 @RequestParam(name = "borrowingReqId", required = false) String borrowingReqId,//当前登录人
								 @RequestParam(name = "userName", required = false) String userName,
								 @RequestParam(name = "fullName", required = false) String fullName  ) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String borrowingReqId = getRequest().getParameter("borrowingReqId");//借阅申请ID
//			String userName = getRequest().getParameter("userName");//查询条件人员编码
//			String fullName = getRequest().getParameter("fullName");//查询条件人员姓名
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appGetAddAuditerAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetAddAuditer"
					+ "&borrowingReqId=" + borrowingReqId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&userName=" + userName + "&fullName=" + fullName);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetAddAuditer"
					+ "&borrowingReqId=" + borrowingReqId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&userName=" + userName + "&fullName=" + fullName);
			
			LOGGER.info("appGetAddAuditerAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询借阅审批人出错\",\"success\":false}";
			LOGGER.info("appGetAddAuditerAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			//writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 审核借阅查询审批人-同意
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appAgreeApproveActivity")
	public void appAgreeApproveActivity() throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			String borrowingReqId = getRequest().getParameter("borrowingReqId");//借阅申请ID
			String wiParticId = getRequest().getParameter("wiParticId");//审批人ID
			String wiParticNo = getRequest().getParameter("wiParticNo");//审批人ID
			String wiParticName = getRequest().getParameter("wiParticName");//审批人名称
			String processState = getRequest().getParameter("processState");//当前流程环节
			String processStateEng = getRequest().getParameter("processStateEng");//当前流程环节英文名
			String odBorrowedFlag = getRequest().getParameter("odBorrowedFlag");//当前流程借阅类型
			String processStatus = getRequest().getParameter("processStatus");//当前流程状态
			String approvaltext = getRequest().getParameter("approvaltext");//审批意见
//			String state = 1  同意；2  退回
			
			String userId = getRequest().getParameter("userId");//登录用户ID
			String mobileId = getRequest().getParameter("mobileId");//手机ID
			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appAgreeApproveActivityAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appAgreeApproveActivity"
					+ "&borrowingReqId=" + borrowingReqId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&wiParticId=" + wiParticId+"&wiParticNo="+wiParticNo + "&wiParticName=" + wiParticName + "&processState=" + processState
					+ "&processStateEng=" + processStateEng+ "&processStatus=" + processStatus+ "&approvaltext=" + approvaltext + "&state=1"
					+ "&odBorrowedFlag="+odBorrowedFlag);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appAgreeApproveActivity"
					+ "&borrowingReqId=" + borrowingReqId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&wiParticId=" + wiParticId+"&wiParticNo="+wiParticNo + "&wiParticName=" + wiParticName + "&processState=" + processState
					+ "&processStateEng=" + processStateEng+ "&processStatus=" + processStatus+ "&approvaltext=" + approvaltext + "&state=1"
					+ "&odBorrowedFlag="+odBorrowedFlag);
			
			LOGGER.info("appAgreeApproveActivityAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			writeString(URLDecoder.decode(s,"UTF-8"));
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"同意审核借阅出错\",\"success\":false}";
			LOGGER.info("appAgreeApproveActivityAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
		}
	}
	
	/**
	 * 审核借阅-退回
	 * @throws ClientProtocolException
	 * @throws IOException
	 */

	@RequestMapping("appBackApproveActivity")
	public void appBackApproveActivity() throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			String borrowingReqId = getRequest().getParameter("borrowingReqId");//借阅申请ID
			String processState = getRequest().getParameter("processState");//当前流程环节
			String processStateEng = getRequest().getParameter("processStateEng");//当前流程环节英文名
			String processStatus = getRequest().getParameter("processStatus");//当前流程状态
			String approvaltext = getRequest().getParameter("approvaltext");//审批意见
//			String state = 1  同意；2  退回
			
			String userId = getRequest().getParameter("userId");//登录用户ID
			String mobileId = getRequest().getParameter("mobileId");//手机ID
			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appBackApproveActivityAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appBackApproveActivity"
					+ "&borrowingReqId=" + borrowingReqId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&approvaltext=" + approvaltext + "&state=2"+ "&processState=" + processState
					+ "&processStateEng=" + processStateEng+ "&processStatus=" + processStatus);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appBackApproveActivity"
					+ "&borrowingReqId=" + borrowingReqId + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&approvaltext=" + approvaltext + "&state=2"+ "&processState=" + processState
					+ "&processStateEng=" + processStateEng+ "&processStatus=" + processStatus);
			
			LOGGER.info("appBackApproveActivityAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			writeString(URLDecoder.decode(s,"UTF-8"));
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"退回审核借阅出错\",\"success\":false}";
			LOGGER.info("appBackApproveActivityAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
		}
	}
	
	/**
	 * 待确认付款(确认支付，确认不支付)
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appConfirmationPay")
	public String  appConfirmationPay() throws ClientProtocolException, IOException{
		try{
			String payLineId = getRequest().getParameter("payLineId");//代付款ID
			String clickType = getRequest().getParameter("clickType");//确认付款confirmationPay，确认不付款confirmationNotPay
			//========加密验证 start==========
			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			String userId = getRequest().getParameter("userId");//登录用户ID
			String mobileId = getRequest().getParameter("mobileId");//手机ID
			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appConfirmationPayAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appConfirmationPay"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&payLineId=" + payLineId + "&clickType=" + clickType);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appConfirmationPay"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&payLineId=" + payLineId + "&clickType=" + clickType);
			
			LOGGER.info("appConfirmationPayAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"付款确认出错\",\"success\":false}";
			LOGGER.info("appConfirmationPayAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 系统消息列表
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetMassageList")
	public String appGetMassageList(@RequestParam(name = "pageSize", required = false) String pageSize,
									@RequestParam(name = "curPage", required = false) String curPage,
									@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,
									@RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			//String resultString ="0";
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串

			LOGGER.info("appGetMassageListAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetMassageList"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&curPage=" + curPage + "&pageSize=" + pageSize);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetMassageList"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&curPage=" + curPage + "&pageSize=" + pageSize);
			
			LOGGER.info("appGetMassageListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取消息列表出错\",\"success\":false}";
			LOGGER.info("appGetMassageListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 计划付款-修改
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appUpdateClaimVendorPay")
	public String  appUpdateClaimVendorPay(@RequestParam(name = "userId") String userId,
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
										   @RequestParam(name = "remark",required = false) String remark,
										   @RequestParam(name = "payLineId",required = false) String payLineId) throws ClientProtocolException, IOException{
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
			if(vendorTypeDisp!=null&&vendorTypeDisp.contains("%")){
				vendorTypeDisp=vendorTypeDisp.replace("%", "%25");
			}
//			String conditionDate = getRequest().getParameter("conditionDate");//条件日期
//			String reserved1 = getRequest().getParameter("reserved1");//账期
//			String planPayDate = getRequest().getParameter("planPayDate");//计划付款日
//			String changePayDate = getRequest().getParameter("changePayDate");//变更后付款日
//			String remark = getRequest().getParameter("remark");//特殊付款说明
//			String payLineId = getRequest().getParameter("payLineId");//支付方式
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appUpdateClaimVendorPayAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appUpdateClaimVendorPay"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&claimNo=" + claimNo + "&payAmount=" + payAmount
					+ "&compId=2&status=0" + "&orgId=" + orgId + "&payTypeId=" + payTypeId + "&payTypeName=" + payTypeName
					+ "&expenseIssuerId=" + expenseIssuerId + "&payLineId=" + payLineId
					+ "&vendorTypeDisp=" + vendorTypeDisp + "&conditionDate=" + conditionDate+ "&reserved1=" + reserved1+ "&planPayDate=" + planPayDate+ "&changePayDate=" + changePayDate+ "&remark=" + remark);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appUpdateClaimVendorPay"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&claimNo=" + claimNo + "&payAmount=" + payAmount
					+ "&compId=2&status=0" + "&orgId=" + orgId + "&payTypeId=" + payTypeId + "&payTypeName=" + payTypeName
					+ "&expenseIssuerId=" + expenseIssuerId + "&payLineId=" + payLineId
					+ "&vendorTypeDisp=" + vendorTypeDisp + "&conditionDate=" + conditionDate+ "&reserved1=" + reserved1+ "&planPayDate=" + planPayDate+ "&changePayDate=" + changePayDate+ "&remark=" + remark);
			
			LOGGER.info("appUpdateClaimVendorPayAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"计划付款修改-保存出错\",\"success\":false}";
			LOGGER.info("appUpdateClaimVendorPayAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 借款核销--修改
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimRelUpdate")
	public String appClaimRelUpdate(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
									@RequestParam(name = "claimId",required = false) String claimId,
									@RequestParam(name = "itemId",required = false) String itemId,
									@RequestParam(name = "updateIds",required = false) String updateIds) throws ClientProtocolException, IOException{
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
//			String updateIds = getRequest().getParameter("updateIds");//核销单拼接字符串
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appClaimRelUpdateAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelUpdate"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&updateIds=" + updateIds);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelUpdate"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&updateIds=" + updateIds);
			
			LOGGER.info("appClaimRelUpdateAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"借款核销修改出错\",\"success\":false}";
			LOGGER.info("appClaimRelUpdateAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 新增明细信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimLineSave")
	public String  appClaimLineSave(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
									@RequestParam(name = "claimId",required = false) String claimId,
									@RequestParam(name = "itemId",required = false) String itemId,
									@RequestParam(name = "costSeg",required = false) String costSeg,
									@RequestParam(name = "costSegCode",required = false) String costSegCode,
									@RequestParam(name = "buSegCode",required = false) String buSegCode,
									@RequestParam(name = "costAssumeDepartment",required = false) String costAssumeDepartment,
									@RequestParam(name = "buSeg",required = false) String buSeg,
									@RequestParam(name = "groupAttributeId",required = false) String groupAttributeId,
									@RequestParam(name = "foreignApplyAmount",required = false) String foreignApplyAmount,
									@RequestParam(name = "claimLineDesc",required = false) String claimLineDesc,
									@RequestParam(name = "apProjectSegCode",required = false) String apProjectSegCode,
									@RequestParam(name = "Vendorsiteid",required = false) String Vendorsiteid,
									@RequestParam(name = "apProjectSeg",required = false) String apProjectSeg,
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
			//查询条件
//			String claimId = getRequest().getParameter("claimId");//报账单ID
//			String itemId = getRequest().getParameter("itemId");//报账单模板
//			String item3Id = getRequest().getParameter("item3Id");//业务小类
//			String costSeg = getRequest().getParameter("costSeg");//费用承担部门
//			String costSegCode = getRequest().getParameter("costSegCode");//费用承担部门
//			String costAssumeDepartment = getRequest().getParameter("costAssumeDepartment");//部门Id
//			String groupAttributeId = getRequest().getParameter("groupAttributeId");//部门属性编码
//			String foreignApplyAmount = getRequest().getParameter("foreignApplyAmount");//费用承担金额
//			String claimLineDesc = getRequest().getParameter("claimLineDesc");//摘要
//			String apProjectSegCode = getRequest().getParameter("apProjectSegCode");//子目段
//			String apProjectSeg = getRequest().getParameter("apProjectSeg");//子目段
//			String Vendorsiteid = getRequest().getParameter("Vendorsiteid");//供应商地点ID
//			String orgId = getRequest().getParameter("orgId");//ouId
//			String buSegCode = getRequest().getParameter("buSegCode");//事业部编码
//			String buSeg = getRequest().getParameter("buSeg");//事业部
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			apProjectSeg = URLDecoder.decode("工伤!@#$%^&*()_+{}|]\[;’:”<>?<>/借款","UTF-8");
			LOGGER.info("appClaimLineSaveAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimLineSave"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&compId=2" + "&item3Id=" + item3Id
					+ "&costSeg=" + costSeg + "&costSegCode=" + costSegCode + "&claimLineDesc=" + claimLineDesc
					+ "&foreignApplyAmount=" + foreignApplyAmount + "&apProjectSegCode=" + apProjectSegCode
					+ "&costAssumeDepartment=" + costAssumeDepartment + "&apProjectSeg=" + URLEncoder.encode(apProjectSeg,"UTF-8")
					+ "&groupAttributeId=" + groupAttributeId + "&Vendorsiteid=" + Vendorsiteid
					+ "&orgId=" + orgId + "&buSegCode=" + buSegCode + "&buSeg=" + buSeg);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimLineSave"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&compId=2" + "&item3Id=" + item3Id
					+ "&costSeg=" + costSeg + "&costSegCode=" + costSegCode + "&claimLineDesc=" + claimLineDesc
					+ "&foreignApplyAmount=" + foreignApplyAmount + "&apProjectSegCode=" + apProjectSegCode
					+ "&costAssumeDepartment=" + costAssumeDepartment + "&apProjectSeg=" + URLEncoder.encode(apProjectSeg,"UTF-8")
					+ "&groupAttributeId=" + groupAttributeId + "&Vendorsiteid=" + Vendorsiteid
					+ "&orgId=" + orgId + "&buSegCode=" + buSegCode + "&buSeg=" + buSeg);
			
			LOGGER.info("appClaimLineSaveAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"新增明细信息出错\",\"success\":false}";
			LOGGER.info("appClaimLineSaveAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 修改明细信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimLineUpdate")
	public String appClaimLineUpdate(@RequestParam(name = "userId") String userId,
									 @RequestParam(name = "mobileId") String mobileId,//手机ID
									 @RequestParam(name = "ciphertext") String ciphertext,
									 @RequestParam(name = "claimId",required = false) String claimId,
									 @RequestParam(name = "itemId",required = false) String itemId,
									 @RequestParam(name = "costSeg",required = false) String costSeg,
									 @RequestParam(name = "costSegCode",required = false) String costSegCode,
									 @RequestParam(name = "buSegCode",required = false) String buSegCode,
									 @RequestParam(name = "costAssumeDepartment",required = false) String costAssumeDepartment,
									 @RequestParam(name = "buSeg",required = false) String buSeg,
									 @RequestParam(name = "groupAttributeId",required = false) String groupAttributeId,
									 @RequestParam(name = "foreignApplyAmount",required = false) String foreignApplyAmount,
									 @RequestParam(name = "claimLineDesc",required = false) String claimLineDesc,
									 @RequestParam(name = "apProjectSegCode",required = false) String apProjectSegCode,
									 @RequestParam(name = "Vendorsiteid",required = false) String Vendorsiteid,
									 @RequestParam(name = "apProjectSeg",required = false) String apProjectSeg,
									 @RequestParam(name = "orgId",required = false) String orgId,
									 @RequestParam(name = "item3Id",required = false) String item3Id,
									 @RequestParam(name = "claimLineId",required = false) String claimLineId) throws ClientProtocolException, IOException{
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
//			String itemId = getRequest().getParameter("itemId");//报账单模板
//			String item3Id = getRequest().getParameter("item3Id");//业务小类
//			String costSeg = getRequest().getParameter("costSeg");//费用承担部门
//			String costSegCode = getRequest().getParameter("costSegCode");//费用承担部门
//			String costAssumeDepartment = getRequest().getParameter("costAssumeDepartment");//部门Id
//			String groupAttributeId = getRequest().getParameter("groupAttributeId");//部门属性编码
//			String foreignApplyAmount = getRequest().getParameter("foreignApplyAmount");//费用承担金额
//			String claimLineDesc = getRequest().getParameter("claimLineDesc");//摘要
//			String apProjectSegCode = getRequest().getParameter("apProjectSegCode");//子目段
//			String apProjectSeg = getRequest().getParameter("apProjectSeg");//子目段
//			String Vendorsiteid = getRequest().getParameter("Vendorsiteid");//供应商地点ID
//			String orgId = getRequest().getParameter("orgId");//ouId
//			String buSegCode = getRequest().getParameter("buSegCode");//事业部编码
//			String buSeg = getRequest().getParameter("buSeg");//事业部
//			String claimLineId = getRequest().getParameter("claimLineId");//明细Id
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串

			LOGGER.info("appClaimLineUpdateAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimLineUpdate"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&compId=2" + "&item3Id=" + item3Id
					+ "&costSeg=" + costSeg + "&costSegCode=" + costSegCode + "&claimLineDesc=" + claimLineDesc
					+ "&foreignApplyAmount=" + foreignApplyAmount + "&apProjectSegCode=" + apProjectSegCode
					+ "&costAssumeDepartment=" + costAssumeDepartment + "&apProjectSeg=" + URLEncoder.encode(apProjectSeg,"UTF-8")
					+ "&groupAttributeId=" + groupAttributeId + "&Vendorsiteid=" + Vendorsiteid
					+ "&orgId=" + orgId + "&buSegCode=" + buSegCode + "&buSeg=" + buSeg + "&claimLineId=" + claimLineId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimLineUpdate"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&compId=2" + "&item3Id=" + item3Id
					+ "&costSeg=" + costSeg + "&costSegCode=" + costSegCode + "&claimLineDesc=" + claimLineDesc
					+ "&foreignApplyAmount=" + foreignApplyAmount + "&apProjectSegCode=" + apProjectSegCode
					+ "&costAssumeDepartment=" + costAssumeDepartment + "&apProjectSeg=" + URLEncoder.encode(apProjectSeg,"UTF-8")
					+ "&groupAttributeId=" + groupAttributeId + "&Vendorsiteid=" + Vendorsiteid
					+ "&orgId=" + orgId + "&buSegCode=" + buSegCode + "&buSeg=" + buSeg + "&claimLineId=" + claimLineId);
			
			LOGGER.info("appClaimLineUpdateAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"修改明细信息出错\",\"success\":false}";
			LOGGER.info("appClaimLineUpdateAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 删除明细信息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimLineDelete")
	public String appClaimLineDelete(@RequestParam(name = "userId") String userId,
									 @RequestParam(name = "mobileId") String mobileId,//手机ID
									 @RequestParam(name = "ciphertext") String ciphertext,
									 @RequestParam(name = "claimId",required = false) String claimId,
									 @RequestParam(name = "itemId",required = false) String itemId,
									 @RequestParam(name = "deleteIds",required = false) String deleteIds) throws ClientProtocolException, IOException{
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
//			String deleteIds = getRequest().getParameter("deleteIds");//删除明细Id
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appClaimLineDeleteAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimLineDelete"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&deleteIds=" + deleteIds);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimLineDelete"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&itemId=" + itemId + "&deleteIds=" + deleteIds);
			
			LOGGER.info("appClaimLineDeleteAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"删除明细信息出错\",\"success\":false}";
			LOGGER.info("appClaimLineDeleteAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 增值税专票-详情(查询发票列表)
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetInvoiceLibraryList")
	public String  appGetInvoiceLibraryList(@RequestParam(name = "userId") String userId,
											@RequestParam(name = "mobileId") String mobileId,//手机ID
											@RequestParam(name = "ciphertext") String ciphertext,
											@RequestParam(name = "curPage",required = false) String curPage,
											@RequestParam(name = "claimId",required = false) String claimId,
											@RequestParam(name = "invoiceNo",required = false) String invoiceNo,
											@RequestParam(name = "pageSize",required = false) String pageSize) throws ClientProtocolException, IOException{
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
			//查询条件user,compId,claimId,item2Id,invoiceNo1
//			String claimId = getRequest().getParameter("claimId");//报账单ID
//			String item2Id = getRequest().getParameter("item2Id");//业务大类
//			String compId = getRequest().getParameter("compId");//
//			String invoiceNo1 = getRequest().getParameter("invoiceNo");//发票号码（查询条件）
			
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetInvoiceLibraryListAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetInvoiceLibraryList"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&invoiceNo1=" + invoiceNo + "&pageSize=" + pageSize
					+ "&curPage=" + curPage);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetInvoiceLibraryList"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&invoiceNo1=" + invoiceNo + "&pageSize=" + pageSize
					+ "&curPage=" + curPage);
			
			LOGGER.info("appGetInvoiceLibraryListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"增值税专票-详情(查询发票列表)出错\",\"success\":false}";
			LOGGER.info("appGetInvoiceLibraryListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 增值税专票-新增
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appSaveInvoiceLibrary")
	public String  appSaveInvoiceLibrary(@RequestParam(name = "userId") String userId,
										 @RequestParam(name = "mobileId") String mobileId,//手机ID
										 @RequestParam(name = "ciphertext") String ciphertext,
										 @RequestParam(name = "claimId",required = false) String claimId,
										 @RequestParam(name = "libraryIds",required = false) String libraryIds) throws ClientProtocolException, IOException{
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
//			String libraryIds = getRequest().getParameter("libraryIds");//增值税发票Id
					
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appSaveInvoiceLibraryAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appSaveInvoiceLibrary"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&libraryIds=" + libraryIds + "&operType=saveInvioceNomal");
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appSaveInvoiceLibrary"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&libraryIds=" + libraryIds + "&operType=saveInvioceNomal");
			
			LOGGER.info("appSaveInvoiceLibraryAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"增值税专票-新增出错\",\"success\":false}";
			LOGGER.info("appSaveInvoiceLibraryAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 增值税专票-删除
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appDeleteInvoiceLibrary")
	public String  appDeleteInvoiceLibrary(@RequestParam(name = "userId") String userId,
										   @RequestParam(name = "mobileId") String mobileId,//手机ID
										   @RequestParam(name = "ciphertext") String ciphertext,
										   @RequestParam(name = "deleteIds",required = false) String deleteIds,
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
			//查询条件
//			String claimId = getRequest().getParameter("claimId");//报账单ID
//			String deleteIds = getRequest().getParameter("deleteIds");//删除明细Id
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appDeleteInvoiceLibraryAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appDeleteInvoiceLibrary"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&deleteIds=" + deleteIds);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appDeleteInvoiceLibrary"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&deleteIds=" + deleteIds);
			
			LOGGER.info("appDeleteInvoiceLibraryAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"删除出错\",\"success\":false}";
			LOGGER.info("appDeleteInvoiceLibraryAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 其他票据,核减金额-保存
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appSaveOtherInvoiceLibrary")
	public String appSaveOtherInvoiceLibrary(@RequestParam(name = "userId") String userId,
											 @RequestParam(name = "mobileId") String mobileId,//手机ID
											 @RequestParam(name = "ciphertext") String ciphertext,
											 @RequestParam(name = "claimId",required = false) String claimId,
											 @RequestParam(name = "summary",required = false) String summary,
											 @RequestParam(name = "subtractionSumAmountVal",required = false) String subtractionSumAmountVal,
											 @RequestParam(name = "objStr",required = false) String objStr) throws ClientProtocolException, IOException{
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
//			String summary = getRequest().getParameter("summary");//备注说明
			if(summary!=null && !"".equals(summary) && !"null".equals(summary)){
				summary=URLEncoder.encode(summary,"UTF-8");
			}
//			String subtractionSumAmountVal = getRequest().getParameter("subtractionSumAmountVal");//核减金额
//			String objStr = getRequest().getParameter("objStr");//其他票据拼接字符串
			if(objStr!=null && !"".equals(objStr) && !"null".equals(objStr)){
				objStr=URLEncoder.encode(objStr,"UTF-8");
			}
			if(subtractionSumAmountVal==null ||"".equals(subtractionSumAmountVal) ||"null".equals(subtractionSumAmountVal)){
				subtractionSumAmountVal = "0";
			}
			//invoiceBusinessType[i].value + "," +invoiceNo[i].value+ "," + invoiceCode[i].value  + "," + taxAmount[i].value + "," + idOther[i].value + "," + pureAmount[i].value +"," + reserved1[i].value + "|";
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appSaveOtherInvoiceLibraryAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appSaveOtherInvoiceLibrary"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&objStr=" + objStr + "&summary=" + summary 
					+ "&subtractionSumAmountVal=" + subtractionSumAmountVal);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appSaveOtherInvoiceLibrary"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId + "&objStr=" + objStr + "&summary=" + summary 
					+ "&subtractionSumAmountVal=" + subtractionSumAmountVal);
			
			LOGGER.info("appSaveOtherInvoiceLibraryAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"保存出错\",\"success\":false}";
			LOGGER.info("appSaveOtherInvoiceLibraryAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 增值税扫描秒发票验证发票正确性
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetVATInvoice")
	public String  appGetVATInvoice(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
									@RequestParam(name = "orgId",required = false) String orgId,
									@RequestParam(name = "orgId",required = false) String invoiceNo,
									@RequestParam(name = "orgId",required = false) String invoiceCode) throws ClientProtocolException, IOException{
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
//			String orgId = getRequest().getParameter("orgId");//报账单ou
//			String invoiceNo = getRequest().getParameter("invoiceNo");//发票号码
//			String invoiceCode = getRequest().getParameter("invoiceCode");//发票代码
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetVATInvoiceAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetVATInvoice"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&orgId=" + orgId + "&invoiceNo=" + invoiceNo + "&invoiceCode=" + invoiceCode);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetVATInvoice"
					+ "&userId=" + userId + "&mobileId=" + mobileId + "&ciphertext=" + ciphertext
					+ "&orgId=" + orgId + "&invoiceNo=" + invoiceNo + "&invoiceCode=" + invoiceCode);
			
			LOGGER.info("appGetVATInvoiceAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"验证扫描增值税发票出错\",\"success\":false}";
			LOGGER.info("appGetVATInvoiceAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 大屏消息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetBigScreenList")
	public String  appGetBigScreenList(@RequestParam(name = "userId") String userId,//当前登录人
									@RequestParam(name = "mobileId") String mobileId,//当前登录人
									@RequestParam(name = "ciphertext") String ciphertext ) throws ClientProtocolException, IOException{
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
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appGetBigScreenListAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetBigScreenList"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetBigScreenList"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appGetBigScreenListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取大屏消息出错\",\"success\":false}";
			LOGGER.info("appGetBigScreenListAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 手机端查看消息详情计数+1
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appSetMessageCount")
	public String appSetMessageCount(@RequestParam(name = "messageId", required = false) String messageId,//当前登录人
								   @RequestParam(name = "userId") String userId,//当前登录人
								   @RequestParam(name = "mobileId") String mobileId,//当前登录人
								   @RequestParam(name = "ciphertext") String ciphertext ) throws ClientProtocolException, IOException{
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
//			String messageId = getRequest().getParameter("messageId");//消息Id
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appSetMessageCountAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appSetMessageCount"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&messageId=" + messageId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appSetMessageCount"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&messageId=" + messageId);
			
			LOGGER.info("appSetMessageCountAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"消息阅读次数+1出错\",\"success\":false}";
			LOGGER.info("appSetMessageCountAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 报账单发送前的附件提示
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetAttachmentInfo")
	public void appGetAttachmentInfo(@RequestParam(name = "userId") String userId,
									 @RequestParam(name = "mobileId") String mobileId,//手机ID
									 @RequestParam(name = "ciphertext") String ciphertext,
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
//			String claimId = getRequest().getParameter("claimId");//明细Id
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appGetAttachmentInfoAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetAttachmentInfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetAttachmentInfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&claimId=" + claimId);
			
			LOGGER.info("appGetAttachmentInfoAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			writeString(URLDecoder.decode(s,"UTF-8"));
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取附件提示出错\",\"success\":false}";
			LOGGER.info("appGetAttachmentInfoAppServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
		}
	}
	
	/**
	 * 报账单起草前提示
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetBeforeClaimfo")
	public String  appGetBeforeClaimfo(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
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
//			String itemId = getRequest().getParameter("itemId");//明细Id
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("appGetBeforeClaimfoAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetBeforeClaimfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&itemId=" + itemId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetBeforeClaimfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&itemId=" + itemId);
			
			LOGGER.info("appGetBeforeClaimfoAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取起草报账单前的系统公告出错\",\"success\":false}";
			LOGGER.info("appGetBeforeClaimfoAppServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 我的办结查询
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetMyFinishList")
	public String appGetMyFinishList(@RequestParam(name = "applyAmountStart", required = false) String applyAmountStart,//报账金额
									 @RequestParam(name = "applyAmountEnd", required = false) String applyAmountEnd,//报账金额
									 @RequestParam(name = "commonQuery", required = false) String commonQuery,//公共查询
									 @RequestParam(name = "applyDateStart", required = false) String applyDateStart,//起草时间开始
									 @RequestParam(name = "applyDateEnd", required = false) String applyDateEnd,//起草时间结束
									 @RequestParam(name = "userId") String userId,//登录用户ID
									 @RequestParam(name = "curPage", required = false) String curPage,//分页
									 @RequestParam(name = "pageSize", required = false) String pageSize,//分页
									 @RequestParam(name = "finishDateStart", required = false) String finishDateStart,//处理时间开始
									 @RequestParam(name = "finishDateEnd", required = false) String finishDateEnd,//处理时间结束
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
//			String commonQuery = getRequest().getParameter("commonQuery");//公共查询
//			String applyAmountStart = getRequest().getParameter("applyAmountStart");//报账金额
//			String applyAmountEnd = getRequest().getParameter("applyAmountEnd");//报账金额
//			String finishDateStart = getRequest().getParameter("applyDateStart");//办结时间开始
//			String finishDateEnd = getRequest().getParameter("applyDateEnd");//办结时间结束
//
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetMyFinishListAppServlet REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetMyFinishList"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&commonQuery=" + commonQuery + "&applyAmountStart=" + applyAmountStart 
					+ "&applyAmountEnd=" + applyAmountEnd + "&finishDateStart=" + finishDateStart 
					+ "&finishDateEnd=" + finishDateEnd + "&pageSize=" + pageSize + "&curPage=" + curPage);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetMyFinishList"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&commonQuery=" + commonQuery + "&applyAmountStart=" + applyAmountStart 
					+ "&applyAmountEnd=" + applyAmountEnd + "&finishDateStart=" + finishDateStart 
					+ "&finishDateEnd=" + finishDateEnd + "&pageSize=" + pageSize + "&curPage=" + curPage);
			
			LOGGER.info("appGetMyFinishListAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"我的办结查询结果出错\",\"success\":false}";
			LOGGER.info("appGetMyFinishListAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	/**
	 * 供应商查询
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetVendorInfo")
	public String appGetVendorInfo(@RequestParam(name = "vendorName", required = false) String vendorName,//报账金额
								 @RequestParam(name = "vendorNumber", required = false) String vendorNumber,//报账金额
								 @RequestParam(name = "orgId", required = false) String orgId,//公共查询
								 @RequestParam(name = "curPage", required = false) String curPage,//分页
								 @RequestParam(name = "pageSize", required = false) String pageSize,//分页
								 @RequestParam(name = "userId") String userId,//处理时间结束
								 @RequestParam(name = "mobileId") String mobileId,//手机ID
								 @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
//			getRequest().setCharacterEncoding("UTF-8");
//			getResponse().setContentType("text/html;charset=UTF-8");
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
		 
//			String vendorName = getRequest().getParameter("vendorName");//供应商名称
			if(vendorName!=null&&!vendorName.equals("")&&!vendorName.equals("null")){
			   vendorName = URLEncoder.encode(vendorName, "UTF-8");
			}
//			String vendorNumber = getRequest().getParameter("vendorNumber");//供应商编号
//			String orgId= getRequest().getParameter("orgId");//OUID
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetVendorInfo REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetVendorInfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&pageSize=" + pageSize + "&curPage=" + curPage+"&vendorName="+vendorName+"&vendorNumber="+vendorNumber+"&orgId="+orgId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetVendorInfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&vendorName=" + vendorName + "&vendorNumber=" + vendorNumber 
					+ "&searchFlag=toPublic" + "&pageSize=" + pageSize + "&curPage=" + curPage+"&orgId="+orgId);
			
			LOGGER.info("appGetVendorInfoAppServlet :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"供应商查询结果出错\",\"success\":false}";
			LOGGER.info("appGetVendorInfoAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			//writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	/**
	 * 供应商地点查询
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appGetVendorSiteInfo")
	public String appGetVendorSiteInfo(@RequestParam(name = "vendorId", required = false) String vendorId,//报账金额
									 @RequestParam(name = "vendorSiteCode", required = false) String vendorSiteCode,//报账金额
									 @RequestParam(name = "orgId", required = false) String orgId,//公共查询
									 @RequestParam(name = "curPage", required = false) String curPage,//分页
									 @RequestParam(name = "pageSize", required = false) String pageSize,//分页
									 @RequestParam(name = "userId") String userId,//处理时间结束
									 @RequestParam(name = "mobileId") String mobileId,//手机ID
									 @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
//			getRequest().setCharacterEncoding("UTF-8");
//			getResponse().setContentType("text/html;charset=UTF-8");
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
		 
//			String vendorId = getRequest().getParameter("vendorId");//供应商ID
//			String orgId= getRequest().getParameter("orgId");//OUID
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
//			String vendorSiteCode=getRequest().getParameter("vendorSiteCode");//地点名称
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appGetVendorSiteInfo REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetVendorSiteInfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&pageSize=" + pageSize + "&curPage=" + curPage+"&vendorId="+vendorId+"&vendorSiteCode="+vendorSiteCode+"&orgId="+orgId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appGetVendorSiteInfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&vendorId=" + vendorId + "&vendorSiteCode=" + vendorSiteCode 
					+ "&pageSize=" + pageSize + "&curPage=" + curPage+"&orgId="+orgId);
			
			LOGGER.info("appGetVendorSiteInfo :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"供应商地点查询结果出错\",\"success\":false}";
			LOGGER.info("appGetVendorSiteInfoAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	/**
	 * 供应商是否存在关联合同查询
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appISExistContract")
	public String appISExistContract(@RequestParam(name = "vendorNumber", required = false) String vendorNumber,
								   @RequestParam(name = "orgId", required = false) String orgId,
								   @RequestParam(name = "userId") String userId,
								   @RequestParam(name = "mobileId") String mobileId,
								   @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
//			getRequest().setCharacterEncoding("UTF-8");
//			getResponse().setContentType("text/html;charset=UTF-8");
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
		 
//			String vendorNumber = getRequest().getParameter("vendorNumber");//供应商ID
//			String orgId= getRequest().getParameter("orgId");//OUID
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appISExistContract REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appISExistContract"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&vendorNumber="+vendorNumber+"&orgId="+orgId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appISExistContract"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&vendorNumber=" + vendorNumber +"&orgId="+orgId);
			
			LOGGER.info("appISExistContract :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"供应商是否存在关联合同查询结果出错\",\"success\":false}";
			LOGGER.info("appISExistContractAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			//writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	
	/**
	 * 合同信息查询
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appContractInfo")
	public String appContractInfo(@RequestParam(name = "vendorNumber", required = false) String vendorNumber,
								@RequestParam(name = "orgId", required = false) String orgId,
								@RequestParam(name = "contractNum", required = false) String contractNum,
								@RequestParam(name = "contractName", required = false) String contractName,
								@RequestParam(name = "userId") String userId,
								@RequestParam(name = "mobileId") String mobileId,
								@RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
		 
//			String vendorNumber = getRequest().getParameter("vendorNumber");//供应商ID
//			String orgId= getRequest().getParameter("orgId");//OUID
//			String contractNum=getRequest().getParameter("contractNum");//合同编码
//			String contractName=getRequest().getParameter("contractName");//合同名称
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appContractInfo REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appContractInfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&vendorNumber="+vendorNumber+"&orgId="+orgId+"&contractNum="+contractNum+"&contractName="+contractName);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appContractInfo"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&vendorNumber=" + vendorNumber +"&orgId="+orgId+"&contractNum="+contractNum+"&contractName="+contractName);
			
			LOGGER.info("appContractInfo :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"合同信息查询结果出错\",\"success\":false}";
			LOGGER.info("appContractInfoAppServlet :" + str);
			//getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	

	/**
	 * 合同详情查询
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appContractDetail")
	public String appContractDetail(@RequestParam(name = "contractId", required = false) String contractId,

								  @RequestParam(name = "userId") String userId,
								  @RequestParam(name = "mobileId") String mobileId,
								  @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
		 
			
//			String contractId=getRequest().getParameter("contractId");//合同id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("appContractDetail REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appContractDetail"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&contractId="+contractId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appContractDetail"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&contractId="+contractId);
			
			LOGGER.info("appContractDetail :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"合同详情查询结果出错\",\"success\":false}";
			LOGGER.info("appContractDetailServlet :" + str);
			//getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	/**
	 * 预付款核销新增查询
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimRelListForAdd")
	public String  appClaimRelListForAdd(@RequestParam(name = "userId") String userId,
										 @RequestParam(name = "mobileId") String mobileId,//手机ID
										 @RequestParam(name = "ciphertext") String ciphertext,
										 @RequestParam(name = "claimId",required = false) String claimId,
										 @RequestParam(name = "vendorNo",required = false) String vendorNo,
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
		 
			
//			String claimId=getRequest().getParameter("claimId");//报账单id
//			String vendorNo=getRequest().getParameter("vendorNo");//供应商编号
//			String orgId=getRequest().getParameter("orgId");//OUID
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String pageSize = getRequest().getParameter("pageSize");//每页条目
//			String curPage = getRequest().getParameter("curPage");//当前页
			
			LOGGER.info("appClaimRelListForAdd REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelListForAdd"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId+"&vendorNo="+vendorNo+"&orgId="+orgId+"&pageSize="+pageSize+"&curPage="+curPage);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelListForAdd"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId+"&vendorNo="+vendorNo+"&orgId="+orgId+"&pageSize="+pageSize+"&curPage="+curPage);
			
			LOGGER.info("appClaimRelListForAdd :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"预付款核销新增查询结果出错\",\"success\":false}";
			LOGGER.info("appClaimRelListForAddServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	/**
	 * 预付款核销新增保存
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimRelInsert")
	public String appClaimRelInsert(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
									@RequestParam(name = "claimId",required = false) String claimId,
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
		 
			/*compId=2, activityInstID=, wfActDefId=updateDraftState, insertIds=3.00,1138993, updUserId=16765, wfstate=, updUserName=00330504,
			 processState=起草人, taskInstID=, budgetAdminActivity=, claimRelType=1, claimId=1184659, status=0, processDefName=, pageSize=10,
			  activityDefID=, wfPendingTaskInstId=, portalgroupid=15060, pendingID=, line_wi=3.00, searchRelFormgotoPage=1,
			   updDate=2018-03-21 15:41:50, processStateEng=rootDrafterActivity, curPage=1, applyUserName=田晓娜, setOfBooks=伊利集团会计帐套, srequestId=T008LoadAll,
			 noWritedAmount=3000, requestId=claimRelInsert, processInstID=, itemId=T008, defaultIndex=2, checkBox=1138993}*/
			
			
//			String claimId=getRequest().getParameter("claimId");//报账单id
			//insertIds=核销金额+","+被核销报账单id   如果选择多条用;分割如3.00,1138993;4.00,1138994
//			String insertIds=getRequest().getParameter("insertIds");
			
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
		
			
			LOGGER.info("appClaimRelInsert REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelInsert"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId+"&insertIds="+insertIds);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelInsert"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId+"&insertIds="+insertIds);
			
			LOGGER.info("appClaimRelInsert :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"预付款核销新增保存出错\",\"success\":false}";
			LOGGER.info("appClaimRelInsertServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	/**
	 * 预付款核销修改
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimRelUpdateT008")
	public String appClaimRelUpdateT008(@RequestParam(name = "userId") String userId,
										@RequestParam(name = "mobileId") String mobileId,//手机ID
										@RequestParam(name = "ciphertext") String ciphertext,
										@RequestParam(name = "claimId",required = false) String claimId,
										@RequestParam(name = "updateIds",required = false) String updateIds) throws ClientProtocolException, IOException{
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
		 
			
			
			
//			String claimId=getRequest().getParameter("claimId");//报账单id
			//updateIds=核销金额+","+预付款核销行id   如果选择多条用;分割如3.00,1138993;4.00,1138994
//			String updateIds=getRequest().getParameter("updateIds");
			
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
		
			
			LOGGER.info("appClaimRelUpdateT008 REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelUpdateT008"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId+"&updateIds="+updateIds);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelUpdateT008"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId+"&updateIds="+updateIds);
			
			LOGGER.info("appClaimRelUpdateT008 :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"预付款核销修改出错\",\"success\":false}";
			LOGGER.info("appClaimRelUpdateT008Servlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	/**
	 * 预付款核销明细行查询
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void appClaimRelList() throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
		 
		
			
			
			String claimId=getRequest().getParameter("claimId");//报账单id
			
			String userId = getRequest().getParameter("userId");//登录用户ID
			String mobileId = getRequest().getParameter("mobileId");//手机ID
			String ciphertext = getRequest().getParameter("ciphertext");//加密串
		
			
			LOGGER.info("appClaimRelList REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelList"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelList"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId);
			
			LOGGER.info("appClaimRelList :" + URLDecoder.decode(s,"UTF-8"));
			
			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			writeString(URLDecoder.decode(s,"UTF-8"));
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"预付款核销明细行查询出错\",\"success\":false}";
			LOGGER.info("appClaimRelListServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 预付款核销删除
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appClaimRelDelete")
	public String appClaimRelDelete(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
									@RequestParam(name = "claimId",required = false) String claimId,
									@RequestParam(name = "deleteIds",required = false) String deleteIds) throws ClientProtocolException, IOException{
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
		 		
//			String claimId=getRequest().getParameter("claimId");//报账单id
			//deleteIds=预付款核销行id +","+预付款核销行id 
//			String deleteIds=getRequest().getParameter("deleteIds");
			
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
		
			
			LOGGER.info("appClaimRelDelete REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelDelete"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId+"&deleteIds="+deleteIds);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "appClaimRelDelete"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+"&claimId="+claimId+"&deleteIds="+deleteIds);
			
			LOGGER.info("appClaimRelDelete :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"预付款核销删除出错\",\"success\":false}";
			LOGGER.info("appClaimRelDeleteServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	/**
	 * 呼叫中心-提问保存
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccSaveQuestion")
	public String ccSaveQuestion(@RequestParam(name = "typeId", required = false) String typeId,//当前登录人
							     @RequestParam(name = "applyUserId", required = false) String applyUserId,//当前登录人
							     @RequestParam(name = "qaDesc", required = false) String qaDesc,//当前登录人
							     @RequestParam(name = "claimNo", required = false) String claimNo,//分页
							     @RequestParam(name = "subTypeId", required = false) String subTypeId,//分页
							     @RequestParam(name = "qaDesp", required = false) String qaDesp,//处理时间开始
							     @RequestParam(name = "fileName", required = false) String fileName,//处理时间结束
							     @RequestParam(name = "filePath", required = false) String filePath,
							     @RequestParam(name = "qaId", required = false) String qaId,
							     @RequestParam(name = "contractTel", required = false) String contractTel,
								 @RequestParam(name = "priority", required = false) String priority,
							     @RequestParam(name = "userId") String userId,
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
//			String applyUserId=getRequest().getParameter("applyUserId");//申请人id
			//String typeId=getRequest().getParameter("typeId");// 问题单类型
//			String claimNo=getRequest().getParameter("claimNo");//报账单号
//			String subTypeId=getRequest().getParameter("subTypeId");//问题分类
//			String priority=getRequest().getParameter("priority");//优先级
			//String qaDesc=getRequest().getParameter("qaDesc");//标题
//			String qaDesp=getRequest().getParameter("qaDesp");//问题详述
//			String fileName=getRequest().getParameter("fileName");//文件信息
//			String filePath=getRequest().getParameter("filePath");//文件信息
//			String qaId=getRequest().getParameter("qaId");//id
//			String contractTel=getRequest().getParameter("contractTel");//id
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
		    if(qaDesp!=null&&!qaDesp.equals("")){
		    	qaDesp= URLEncoder.encode(qaDesp, "UTF-8");
		    }
		   
			
			LOGGER.info("ccSaveQuestion REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccSaveQuestion"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&applyUserId="+applyUserId+"&typeId="+typeId+"&claimNo="+claimNo+"&subTypeId="+subTypeId+"&priority="+priority
					+ "&qaDesc="+qaDesc+"&qaDesp="+qaDesp+"&fileName="+fileName+"&filePath="+filePath+"&qaId="+qaId+"&contractTel="+contractTel);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccSaveQuestion"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&applyUserId="+applyUserId+"&typeId="+typeId+"&claimNo="+claimNo+"&subTypeId="+subTypeId+"&priority="+priority
					+ "&qaDesc="+qaDesc+"&qaDesp="+qaDesp+"&fileName="+fileName+"&filePath="+filePath+"&qaId="+qaId+"&contractTel="+contractTel);
			
			LOGGER.info("ccSaveQuestion :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"提问保存出错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	/**
	 * 呼叫中心-查询列表
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccfindMyQuestion")
	public String ccfindMyQuestion(@RequestParam(name = "typeId", required = false) String typeId,//当前登录人
								   @RequestParam(name = "status", required = false) String status,//当前登录人
								   @RequestParam(name = "qaDesc", required = false) String qaDesc,//当前登录人
								   @RequestParam(name = "curPage", required = false) String curPage,//分页
								   @RequestParam(name = "pageSize", required = false) String pageSize,//分页
								   @RequestParam(name = "startDate", required = false) String startDate,//处理时间开始
								   @RequestParam(name = "endDate", required = false) String endDate,//处理时间结束
								   @RequestParam(name = "userId") String userId,
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
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String typeId=getRequest().getParameter("typeId");//问题单类型
//			String status=getRequest().getParameter("status");//问题单状态
//			String startDate=getRequest().getParameter("startDate");//开始日期
//			String endDate=getRequest().getParameter("endDate");//结束日期
//			String qaDesc=getRequest().getParameter("qaDesc");//标题
//			String pageSize=getRequest().getParameter("pageSize");
//			String curPage=getRequest().getParameter("curPage");
		
			
			LOGGER.info("ccfindMyQuestion REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccfindMyQuestion"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&qaDesc="+qaDesc+"&typeId="+typeId+"&status="+status+"&startDate="+startDate+"&endDate="+endDate
					+"&curPage="+curPage+"&pageSize="+pageSize);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccfindMyQuestion"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&qaDesc="+qaDesc+"&typeId="+typeId+"&status="+status+"&startDate="+startDate+"&endDate="+endDate
					+"&curPage="+curPage+"&pageSize="+pageSize);
			
			LOGGER.info("ccfindMyQuestion :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询列表出错\",\"success\":false}";
			LOGGER.info("ccfindMyQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	

	/**
	 * 呼叫中心-工单详情
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccQuestionDetail")
	public String  ccQuestionDetail(@RequestParam(name = "userId") String userId,
								 @RequestParam(name = "mobileId") String mobileId,//手机ID
								 @RequestParam(name = "ciphertext") String ciphertext,
								 @RequestParam(name = "qaId",required = false) String qaId) throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String qaId=getRequest().getParameter("qaId");//工单id
			LOGGER.info("ccQuestionDetail REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccQuestionDetail"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&qaId="+qaId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccQuestionDetail"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext
					+ "&qaId="+qaId);
			
			LOGGER.info("ccQuestionDetail :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询工单详情出错\",\"success\":false}";
			LOGGER.info("ccQuestionDetailServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	/**
	 * 呼叫中心-字典项
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccDict")
	public String ccDict(@RequestParam(name = "userId") String userId,
						 @RequestParam(name = "mobileId") String mobileId,//手机ID
						 @RequestParam(name = "ciphertext") String ciphertext) throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			LOGGER.info("ccDict REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccDict"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccDict"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("ccDict :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			//writeString(
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询字典项出错\",\"success\":false}";
			LOGGER.info("ccQuestionDetailServlet :" + str);
			//getRequest().setAttribute("object", str);
			//writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	/**
	 * 工单文件上传
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccUploadFile")
	public String  ccUploadFile(@RequestParam(name = "userId") String userId,
								@RequestParam(name = "mobileId") String mobileId,//手机ID
								@RequestParam(name = "ciphertext") String ciphertext,
								@RequestParam(name = "apProjectSegName",required = false) String apProjectSegName,
								@RequestParam(name="multiFiles") MultipartFile[] multiFiles) throws ClientProtocolException, IOException{
		try {
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

//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			//封装返回结果
			Map<String,Object> map = new HashMap<String,Object>();
			try {

//				map.put("apProjectSegName",apProjectSegName);
				String localUploadPath ="/data/server/appfiles_use/attachment_cc/";
				
				
//				Map<String,Object> mapResult =getMultipartFileList(multipartFile);
//				File[] multiFiles = (File[]) mapResult.get("multiFiles");
//				String[] fileNames =  (String[]) mapResult.get("fileName");
				List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		    	// 上传图片文件
				if (multiFiles != null && multiFiles.length > 0) {
					 for (int i=0;i<multiFiles.length;i++) {
					    // 文件名
		                String fileName = multiFiles[i].getOriginalFilename();
//		                String fileName ="";
		                if (!"".equalsIgnoreCase(fileName)) {
		                	String newFileName = "";
		                	java.util.Date dt = new java.util.Date();
		                	newFileName += "upload_";
		                	newFileName += dt.getTime();
		                	newFileName += "." + getFileExt(fileName);
		                	String newPath = localUploadPath+newFileName;
		                	// 生成文件
							File newFile = new File(newPath);
//		                	if(!newFile.exists()){
//		                		newFile.mkdir();
//							}
							String[] originalFilename = fileName.split("\\.");

//							FileUtils.copyFile();
							multiFiles[i].transferTo(File.createTempFile(originalFilename[0], originalFilename[1]));
							FileUtils.copyFile(File.createTempFile(originalFilename[0], originalFilename[1]),newFile);
//		                	multiFiles[i].
							FileUtils.forceDelete(File.createTempFile(originalFilename[0], originalFilename[1]));
		    				Map<String,Object> mapS = new HashMap<String,Object>();
		    				mapS.put("fileName", fileName);
		    				mapS.put("status", "1");//0失败；1成功
		    				mapS.put("filePath", newPath);
		    				resultList.add(mapS);
		                }
		            }
					 Map<String,Object> mapR = new HashMap<String,Object>();
					 mapR.put("code", "0");
					 mapR.put("result", resultList);
					 mapR.put("message", "文件上传成功");
					 mapR.put("success", "true");
					 String str = JSONObject.toJSONString(mapR,SerializerFeature.WriteMapNullValue);
//					 getRequest().setAttribute("object", str);
//					 writeString(str);
					return str;
				}else{
					String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
					LOGGER.info("appUploadFile :" + str);
//					getRequest().setAttribute("object", str);
//					writeString(str);
					return str;
				}
			} catch (IllegalStateException e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
				LOGGER.info("appUploadFile :" + str);
//				getRequest().setAttribute("object", str);
//				writeString(str);
				e.printStackTrace();
				return str;
			} catch (IOException e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
				LOGGER.info("appUploadFile :" + str);
//				getRequest().setAttribute("object", str);
//				writeString(str);
				e.printStackTrace();
				return str;
			} catch (Exception e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
				LOGGER.info("appUploadFile :" + str);
//				getRequest().setAttribute("object", str);
//				writeString(str);
				e.printStackTrace();
				return str;
			}
		} catch (Exception e){
			String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
			LOGGER.info("appUploadFile :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	/**
	 * 呼叫中心-工单发送
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccSend")
	public String ccSend(@RequestParam(name = "userId") String userId,
					   @RequestParam(name = "mobileId") String mobileId,//手机ID
					   @RequestParam(name = "ciphertext") String ciphertext,
					   @RequestParam(name = "qaId",required = false) String qaId) throws ClientProtocolException, IOException{
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
			
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String qaId=getRequest().getParameter("qaId");
		
			
			LOGGER.info("ccSend REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccSend"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&qaId="+qaId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccSend"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&qaId="+qaId);
			
			LOGGER.info("ccSend :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"工单发送出错\",\"success\":false}";
			LOGGER.info("ccSendServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	/**
	 * 呼叫中心-工单删除
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccDeleteQuestion")
	public String ccDeleteQuestion(@RequestParam(name = "userId") String userId,
								 @RequestParam(name = "mobileId") String mobileId,//手机ID
								 @RequestParam(name = "ciphertext") String ciphertext,
								 @RequestParam(name = "qaId",required = false) String qaId) throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String qaId=getRequest().getParameter("qaId");
		
			
			LOGGER.info("ccDeleteQuestion REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccDeleteQuestion"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&qaId="+qaId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccDeleteQuestion"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&qaId="+qaId);
			
			LOGGER.info("ccDeleteQuestion :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"工单删除出错\",\"success\":false}";
			LOGGER.info("ccDeleteQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	/**
	 * 工单获取文件
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccGetFile")
	public String ccGetFile(@RequestParam(name = "userId") String userId,
						  @RequestParam(name = "mobileId") String mobileId,//手机ID
						  @RequestParam(name = "ciphertext") String ciphertext,
						  @RequestParam(name = "filePath",required = false) String filePath) throws ClientProtocolException, IOException{
		FileInputStream readFile = null;
		BufferedInputStream in = null;
		ServletOutputStream out = null;
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
			
//			String filePath = getRequest().getParameter("filePath");//报账单id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
				out = getResponse().getOutputStream();
				File file = new File(filePath);
				if (file.exists()) {  
		            // 写明要下载的文件的大小  
					getResponse().setContentLength((int) file.length());  
					getResponse().setHeader("Content-Disposition", "attachment;filename="  
		                    + file.getName());// 设置在下载框默认显示的文件名  
					getResponse().setContentType("application/octet-stream");//指明response的返回对象是文件流  
					readFile = new FileInputStream(file);
					in = new BufferedInputStream(readFile);
					byte[] temp = new byte[1024 * 64];
					int size = 0;
					while ((size = in.read(temp)) != -1) {
						out.write(temp, 0, size);
					}
					in.close();
					out.flush();
					out.close(); 
				

			}
			
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询文件出错\",\"success\":false}";
			LOGGER.info("ccGetFile :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}finally{
			if(in != null){
				try{
				in.close();
			}catch(Exception e){
				
			}
			}
			if(out != null){
				try{
					out.close();
				}catch(Exception e){
					
				}
				
			}
			if(readFile != null){
				try{
					readFile.close();
				}catch(Exception e){
					
				}
				
			}
		}
		return "";
	}
	
	
	
	/**
	 * 呼叫中心-工单再发送
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccReAsk")
	public String ccReAsk(@RequestParam(name = "userId") String userId,
						@RequestParam(name = "mobileId") String mobileId,//手机ID
						@RequestParam(name = "ciphertext") String ciphertext,
						@RequestParam(name = "qaId",required = false) String qaId,
						@RequestParam(name = "answer",required = false) String answer,
						@RequestParam(name = "curUserId",required = false) String curUserId) throws ClientProtocolException, IOException{
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
			
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String qaId=getRequest().getParameter("qaId");
//			String answer=getRequest().getParameter("answer");
//		    String curUserId=getRequest().getParameter("userId");//登录用户ID
			
			LOGGER.info("ccReAsk REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccReAsk"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&qaId="+qaId+"&answer="+answer+"&curUserId="+curUserId);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccReAsk"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&qaId="+qaId+"&answer="+answer+"&curUserId="+curUserId);
			
			LOGGER.info("ccReAsk :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"工单再发送出错\",\"success\":false}";
			LOGGER.info("ccReAskServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	/**
	 * 呼叫中心-工单再发送
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("ccClose")
	public String ccClose(@RequestParam(name = "userId") String userId,
						@RequestParam(name = "mobileId") String mobileId,//手机ID
						@RequestParam(name = "ciphertext") String ciphertext,
						@RequestParam(name = "curUserId",required = false) String curUserId,
						@RequestParam(name = "qaId",required = false) String qaId) throws ClientProtocolException, IOException{
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
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String qaId=getRequest().getParameter("qaId");
//		    String curUserId=getRequest().getParameter("userId");//登录用户ID
			LOGGER.info("ccClose REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccClose"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&qaId="+qaId+"&curUserId="+curUserId);
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "ccClose"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&qaId="+qaId+"&curUserId="+curUserId);
			LOGGER.info("ccClose :" + URLDecoder.decode(s,"UTF-8"));
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"工单关闭出错\",\"success\":false}";
			LOGGER.info("ccCloseServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	
	
	/**
	 * 报账单撤回
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("recall")
	public String recall(@RequestParam(name = "userId") String userId,
					   @RequestParam(name = "mobileId") String mobileId,//手机ID
					   @RequestParam(name = "ciphertext") String ciphertext,
					   @RequestParam(name = "claimId",required = false) String claimId,
					   @RequestParam(name = "lockId",required = false) String lockId,
					   @RequestParam(name = "lockTime",required = false) String lockTime) throws ClientProtocolException, IOException{
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
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String claimId=getRequest().getParameter("claimId");
//			String lockId = getRequest().getParameter("lockId");
//			String lockTime = getRequest().getParameter("lockTime");
			lockTime = URLEncoder.encode(lockTime, "UTF-8");
		    String type="recall";//登录用户ID
			LOGGER.info("recall REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "recall"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" +ciphertext+"&claimId="+claimId+"&type="+type+"&lockId="+lockId+"&lockTime="+lockTime);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "recall"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&claimId="+claimId+"&type="+type+"&lockId="+lockId+"&lockTime="+lockTime);
			
			LOGGER.info("recall :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"撤回出错\",\"success\":false}";
			LOGGER.info("recallServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 报账单是否已撤回校验
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("isRecall")
	public String isRecall(@RequestParam(name = "userId") String userId,
						 @RequestParam(name = "mobileId") String mobileId,//手机ID
						 @RequestParam(name = "ciphertext") String ciphertext,
						 @RequestParam(name = "claimId",required = false) String claimId,
						 @RequestParam(name = "processStateEng",required = false) String processStateEng) throws ClientProtocolException, IOException{
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
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String claimId=getRequest().getParameter("claimId");
//		    String processStateEng=getRequest().getParameter("processStateEng");
			LOGGER.info("isRecall REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "isRecall"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&claimId="+claimId+"&processStateEng="+processStateEng);
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "isRecall"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&claimId="+claimId+"&processStateEng="+processStateEng);
			LOGGER.info("isRecall :" + URLDecoder.decode(s,"UTF-8"));
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"校验是否已撤回出错\",\"success\":false}";
			LOGGER.info("isRecallServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	/**
	 * 补单列表
	 * @throws Exception
	 */
	@RequestMapping("getClaimNoticeToDoList")
	public String getClaimNoticeToDoList(@RequestParam(name = "userNum", required = false) String userNum,//当前登录人
									   @RequestParam(name = "claimNo", required = false) String claimNo,//报账单号
									   @RequestParam(name = "itemId", required = false) String itemId,//报账单模板
									   @RequestParam(name = "item2Id", required = false) String item2Id,//业务大类
									   @RequestParam(name = "applyUserNum", required = false) String applyUserNum,//起草人
									   @RequestParam(name = "applyAmountStart", required = false) String applyAmountStart,//报账金额
									   @RequestParam(name = "applyAmountEnd", required = false) String applyAmountEnd,//报账金额
									   @RequestParam(name = "commonQuery", required = false) String commonQuery,//公共查询
									   @RequestParam(name = "applyDateStart", required = false) String applyDateStart,//起草时间开始
									   @RequestParam(name = "applyDateEnd", required = false) String applyDateEnd,//起草时间结束
	                                   @RequestParam(name = "userId") String userId,//登录用户ID
										 @RequestParam(name = "curPage", required = false) String curPage,//分页
										 @RequestParam(name = "pageSize", required = false) String pageSize,//分页
									   @RequestParam(name = "mobileId") String mobileId,//手机ID
									   @RequestParam(name = "ciphertext") String ciphertext)  throws ClientProtocolException, IOException{//加密串
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
			
			LOGGER.info("getClaimNoticeToDoList REQUEST: " + baseUrl
			+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "getClaimNoticeToDoList"
			+ "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
			 + "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
			 + "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&applyDateStart=" + applyDateStart
			 + "&applyDateEnd=" + applyDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?claimNo=" + claimNo + "&itemId=" + itemId + "&servletName=" + "getClaimNoticeToDoList"
					+ "&item2Id=" + item2Id + "&applyUserNum=" + applyUserNum + "&applyAmountStart=" + applyAmountStart
					 + "&applyAmountEnd=" + applyAmountEnd + "&userNum=" + userNum+ "&pageSize=" + pageSize
					 + "&curPage=" + curPage+ "&commonQuery=" + commonQuery+ "&applyDateStart=" + applyDateStart
					 + "&applyDateEnd=" + applyDateEnd + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("getClaimNoticeToDoList :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");

		}catch (Exception e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"查询补单列表错误\",\"success\":false}";
				LOGGER.info("getClaimNoticeToDoList :" + str);
				getRequest().setAttribute("object", str);
				writeString(str);
				e.printStackTrace();
			return str;
			}

	}
	
	/**
	 * 补单完成
	 * @throws Exception
	 */
	@RequestMapping("updateClaimNoticeState")
	public String updateClaimNoticeState(@RequestParam(name = "recordId", required = false) String recordId,//补单ID
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

			
			
			LOGGER.info("updateClaimNoticeState REQUEST: " + baseUrl
			+ "commonHandleAppServlet?"+ "servletName=" + "updateClaimNoticeState"
			+ "&recordId=" + recordId  + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?"+ "servletName=" + "updateClaimNoticeState"
					+ "&recordId=" + recordId  + "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("getClaimNoticeToDoList :" + URLDecoder.decode(s,"UTF-8"));
			
			//getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");

		}catch (Exception e) {
				String str = "{\"code\":1,\"result\":null,\"message\":\"补单错误\",\"success\":false}";
				LOGGER.info("getClaimNoticeToDoList :" + str);
				getRequest().setAttribute("object", str);
				writeString(str);
				e.printStackTrace();
			return str;
			}

	}
	
	
	
	
	/**
	 *  二维码扫描登陆
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("appScanningQRCodeLogIn")
	public String appScanningQRCodeLogIn(@RequestParam(name = "isEmessageLogin", required = false) String isEmessageLogin,//补单ID
									   @RequestParam(name = "stamp", required = false) String timeStemp,
									   @RequestParam(name = "loginId") String loginId,
									   @RequestParam(name = "hrNo", required = false) String userName,
									   @RequestParam(name = "token", required = false) String ciphertext,
										 @RequestParam(name = "ciphertext") String ciphertext1,
									   @RequestParam(name = "macNo") String macNo) throws Exception{


		//String isEmessageLogin = getRequest().getParameter("isEmessageLogin");//是否e-message跳转登陆

		//如果不是Emessage访问方式
		if(isEmessageLogin!=null && !"null".equals(isEmessageLogin) && !"".equals(isEmessageLogin) && isEmessageLogin.equals("1")) {
			//========加密验证 start==========
			//timeStemp  = getRequest().getParameter("stamp");//时间戳
			//ciphertext = getRequest().getParameter("token");//加密串
			
			//String loginId = getRequest().getParameter("loginId");//当前登录人账户
			//userName = getRequest().getParameter("hrNo");//当前登录人账户
			//macNo = getRequest().getParameter("macNo");//macNo 电脑的mac地址
			String keyString = CodeUtil.hexSHA1(CodeUtil.emessagekey + loginId + timeStemp);
			if(!ciphertext.equals(keyString)){
				//throw new Exception("加密验证失败！请刷新界面！");
				String str = "{\"code\":\"1\",\"result\":null,\"message\":\"加密验证失败！请刷新界面！\",\"success\":false}";
				LOGGER.info("appScanningQRCodeLogIn :" + str);

				return str;
			}
			//========加密验证 end==========
		}else{
			//普通的FSSC访问方式
//			ciphertext = getRequest().getParameter("ciphertext");//加密串
//			userName = getRequest().getParameter("userName");//当前登录人账户
//			macNo = getRequest().getParameter("macNo");//macNo 电脑的mac地址
			MobileRequestVO param = new MobileRequestVO();
			param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(loginId);
			param.setCiphertext(ciphertext1);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			//========加密验证 start==========
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
		}


		try{

			LOGGER.info("appScanningQRCodeLogInAppServlet REQUEST: " + baseUrl	+ "commonHandleAppServlet?servletName=" + "appScanningQRCodeLogIn"
					+ "&userFullName=" + "0000"  + "&userName=" + userName  + "&macNo=" + macNo  + "&userId=" + "0000" + "&mobileId=" + "0000" + "&ciphertext=" + ciphertext + "&isEmessageLogin="+ isEmessageLogin);

			String s = HttpServiceUtil.sendGet(baseUrl	+ "commonHandleAppServlet?servletName=" + "appScanningQRCodeLogIn"
					+ "&userFullName=" + "0000"  + "&userName=" + userName + "&macNo=" + macNo  + "&userId=" + "0000" + "&mobileId=" + "0000" + "&ciphertext=" + ciphertext + "&isEmessageLogin="+ isEmessageLogin);

			LOGGER.info("appScanningQRCodeLogIn :" + URLDecoder.decode(s,"UTF-8"));

			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":\"1\",\"result\":null,\"message\":\"扫描登陆异常出错\",\"success\":false}";
			LOGGER.info("appScanningQRCodeLogIn :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
			return str;
		}
	}

	/**
	 * 工单-根据问题分类获取问题单类型
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getCCTypeId() throws ClientProtocolException, IOException{
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
			String subTypeId=getRequest().getParameter("subTypeId");

			LOGGER.info("isRecall REQUEST: " + baseUrl
					+ "commonHandleAppServlet?servletName=" + "getCCTypeId"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&subTypeId="+subTypeId);

			String s = HttpServiceUtil.sendGet(baseUrl
					+ "commonHandleAppServlet?servletName=" + "getCCTypeId"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&subTypeId="+subTypeId);

			LOGGER.info("getCCTypeId :" + URLDecoder.decode(s,"UTF-8"));

			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			writeString(URLDecoder.decode(s,"UTF-8"));
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"根据问题分类获取问题单类型出错\",\"success\":false}";
			LOGGER.info("getCCTypeIdServlet :" + str);
			getRequest().setAttribute("object", str);
			writeString(str);
			e.printStackTrace();
		}
	}
	/**
	 * 校验报账单号
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("verifyClaimNo")
	public String  verifyClaimNo(@RequestParam(name = "userId") String userId,
								 @RequestParam(name = "mobileId") String mobileId,//手机ID
								 @RequestParam(name = "ciphertext") String ciphertext,
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


//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String claimNo = getRequest().getParameter("claimNo");// 报账单号

			LOGGER.info("findPendingCount REQUEST: " + configUtil.getBaseUrl()
					+ "appFindPendingListServlet?servletName=verifyClaimNo"
					+ "&claimNo=" + claimNo+ "&userId=" + userId
					+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);

			String s = HttpServiceUtil.sendGet(configUtil.getBaseUrl()
					+ "appFindPendingListServlet?servletName=verifyClaimNo"
					+ "&claimNo=" + claimNo+ "&userId=" + userId
					+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);

			LOGGER.info("getTodoList :" + URLDecoder.decode(s,"UTF-8"));

//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"报账单添加失败，无法获取后台数据，请稍后重试！\",\"success\":false}";
			LOGGER.info("getTodoList :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}


	public static JSONObject httpPost(String url,JSONObject jsonParam,Boolean noNeedResponse){
		//post请求返回结果
		JSONObject jsonResult = new JSONObject();
		DefaultHttpClient httpClient = new DefaultHttpClient();
//		httpClient
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);// 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,30000);// 读取超时
		//System.out.println("httpsUtil 调用地址以及参数：" + url + (jsonParam==null?"":jsonParam.toString()));
		HttpPost method = new HttpPost(url);

		try {
			if (null != jsonParam) {
				//解决中文乱码问题
				//List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
				//pairList.add(new BasicNameValuePair("name", "admin"));
				//pairList.add(new BasicNameValuePair("pass", "123456"));
				//method.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
				ByteArrayEntity entity = new ByteArrayEntity(jsonParam.toString().getBytes("UTF-8"));
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/**请求发送成功，并得到响应**/
			if (result.getStatusLine().getStatusCode() == 200) {
				String str = "";
				try {
					/**读取服务器返回过来的json字符串数据**/
					str = EntityUtils.toString(result.getEntity());
					if (noNeedResponse) {
						return null;
					}
					/**把json字符串转换成json对象**/
					jsonResult = JSONObject.parseObject(str);
				} catch (Exception e) {
					e.printStackTrace();
					jsonResult.put("state", 9999999);
					jsonResult.put("message", "post请求提交失败");
				}
			}
		} catch (IOException e) {
			jsonResult.put("state", 9999999);
			jsonResult.put("message", "post请求提交失败");
		}finally {
			httpClient.getConnectionManager().shutdown();
		}
		return jsonResult;
	}
}