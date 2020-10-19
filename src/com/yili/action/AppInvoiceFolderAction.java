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

import com.yili.bean.MobileRequestVO;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yili.bean.MobileNextActivityVo;
import com.yili.bean.MobileParticipantVo;
import com.yili.util.CodeUtil;
import com.yili.util.ConfigUtil;
import com.yili.util.DESUtil;
import com.yili.util.DESUtilApp;
import com.yili.util.HttpServiceUtil;
import com.yili.util.ImageSizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("invoiceFolder")
public class AppInvoiceFolderAction extends BasicAction {
	
	private static Logger LOGGER = Logger.getLogger(AppAction.class);
	public static ConfigUtil configUtil = new ConfigUtil();
	private static final String IMAGE_DES="!@%001EW";
	@Value("${BASE_URL}")
	private String baseUrl="";
	@Value("${upLoad_URL}")
	private String uploadUrl="";

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
	

	@RequestMapping("getFolderList")
	public String  getFolderList(@RequestParam(name = "userId") String userId,
								 @RequestParam(name = "mobileId") String mobileId,//手机ID
								 @RequestParam(name = "ciphertext") String ciphertext,
								 @RequestParam(name = "parentId",required = false) String parentId,
								 @RequestParam(name = "keyWord",required = false) String keyWord,
								 @RequestParam(name = "StartDate",required = false) String StartDate,
								 @RequestParam(name = "EndDate",required = false) String EndDate,
								 @RequestParam(name = "minAmount",required = false) String minAmount,
								 @RequestParam(name = "maxAmount",required = false) String maxAmount) throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//          param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			
//			String parentId=getRequest().getParameter("parentId");//申请人id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String keyWord 		= getRequest().getParameter("keyWord");//加密串
			 if(keyWord!=null&&!keyWord.equals("")){
				 keyWord= URLEncoder.encode(keyWord, "UTF-8");
			    	keyWord= URLEncoder.encode(keyWord,"UTF-8");
			   }
			
//			String StartDate = getRequest().getParameter("StartDate");//加密串
//			String EndDate 	= getRequest().getParameter("EndDate");//加密串
//			String minAmount = getRequest().getParameter("minAmount");//加密串
//			String maxAmount = getRequest().getParameter("maxAmount");//加密串
			
			
			
			System.out.println("getFolderList REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getFolderList"
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&keyWord="+keyWord+
			"&StartDate="+StartDate+
			"&EndDate="+EndDate 	+
			"&minAmount="+minAmount+
			"&maxAmount="+maxAmount+"&versioFlag=N");
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getFolderList"+"&parentId="+parentId
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&keyWord="+keyWord+
					"&StartDate="+StartDate+
					"&EndDate="+EndDate 	+
					"&minAmount="+minAmount+
					"&maxAmount="+maxAmount+"&versioFlag=N");
			
			LOGGER.info("getFolderList :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("saveInvoice")
	public String  saveInvoice(@RequestParam(name = "userId",required = false) String userId,
							   @RequestParam(name = "mobileId") String mobileId,//手机ID
							   @RequestParam(name = "ciphertext") String ciphertext,
							   @RequestParam(name = "parentId",required = false) String parentId,
							   @RequestParam(name = "data",required = false) String data)throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//          param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			
//			String parentId=getRequest().getParameter("parentId");//申请人id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String data	= getRequest().getParameter("data");//加密串
			
			String s = HttpServiceUtil.sendPost(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "saveInvoice"+"&parentId="+parentId
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&versioFlag=N",data);
			
			LOGGER.info("getFolderList :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("getImage")
	public String  getImage(@RequestParam(name = "userId",required = false) String userId,
							@RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
							@RequestParam(name = "ciphertext",required = false) String ciphertext,
							@RequestParam(name = "imageId",required = false) String imageIdArr) throws ClientProtocolException, IOException{
		try{
			DESUtil des = new DESUtil(IMAGE_DES);
			//========加密验证 start==========
			/*String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========
//			String imageIdArr = getRequest().getParameter("imageId");//加密串
//			String decrypt = des.decrypt(imageIdArr);
			String[] split = imageIdArr.split("_");
			String size =split[0];
			String imageId = split[1];
			
			
			LOGGER.info("newFolderSave REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getImageFilePath"
			+ "&versioFlag=Y&imageId="+imageId+"&size="+size);
	
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getImageFilePath"
					+ "&versioFlag=Y&imageId="+imageId+"&size="+size);

			
			String decode = URLDecoder.decode(s,"UTF-8");
			JSONObject json = (JSONObject) JSONObject.parse(decode);
			String path1 = (String) json.get("result");
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
			String str = "{\"code\":1,\"result\":null,\"message\":\"提问保存出错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
		
	}
	/**
	 *
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("newFolderSave")
	public String  newFolderSave(@RequestParam(name = "userId") String userId,
								 @RequestParam(name = "mobileId") String mobileId,//手机ID
								 @RequestParam(name = "ciphertext") String ciphertext,
								 @RequestParam(name = "parentId",required = false) String parentId,
								 @RequestParam(name = "folderName",required = false) String folderName) throws ClientProtocolException, IOException{
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
			
		 		
//			String parentId=getRequest().getParameter("parentId");//申请人id
//			String folderName=getRequest().getParameter("folderName");//id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
		    if(folderName!=null&&!folderName.equals("")){
		    	folderName= URLEncoder.encode(folderName, "UTF-8");
		    	folderName=URLEncoder.encode(folderName,"UTF-8");
		    }
		   
			
			LOGGER.info("newFolderSave REQUEST: " + baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "newFolderSave"+"&fileName="+folderName+"&parentId="+parentId
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "newFolderSave"+"&folderName="+folderName+"&parentId="+parentId
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("appInvoiceFolderSave :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
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
	@RequestMapping("moveFolderList")
	public String  moveFolderList(@RequestParam(name = "userId") String userId,
								  @RequestParam(name = "mobileId") String mobileId,//手机ID
								  @RequestParam(name = "ciphertext") String ciphertext,
								  @RequestParam(name = "parentId",required = false) String parentId,
								  @RequestParam(name = "ids",required = false) String ids) throws ClientProtocolException, IOException{
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
			
			
//			String parentId=getRequest().getParameter("parentId");//申请人id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String ids = getRequest().getParameter("ids");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			
			LOGGER.info("moveFolderList REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "moveFolderList"+"&parentId="+parentId+"&ids="+ids
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "moveFolderList"+"&parentId="+parentId+"&ids="+ids
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("moveFolderList :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
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
	@RequestMapping("moveFolderSave")
	public String moveFolderSave(@RequestParam(name = "userId") String userId,
								 @RequestParam(name = "mobileId") String mobileId,//手机ID
								 @RequestParam(name = "ciphertext") String ciphertext,
								 @RequestParam(name = "moveParentId",required = false) String moveParentId,
								 @RequestParam(name = "ids",required = false) String ids) throws ClientProtocolException, IOException{
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
			
//			String moveParentId=getRequest().getParameter("moveParentId");//申请人id
//			String ids=getRequest().getParameter("ids");//申请人id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			LOGGER.info("moveFolderSave REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "moveFolderSave"
			+"&ids="+ids+"&moveParentId="+moveParentId
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "moveFolderSave"
					+"&ids="+ids+"&moveParentId="+moveParentId
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&versioFlag=N");
			
			LOGGER.info("moveFolderSave :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"发票夹移动报错\",\"success\":false}";
			LOGGER.info("moveFolderSave :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("renameFolderSave")
	public String renameFolderSave(@RequestParam(name = "userId") String userId,
								   @RequestParam(name = "mobileId") String mobileId,//手机ID
								   @RequestParam(name = "ciphertext") String ciphertext,
								   @RequestParam(name = "id",required = false) String id,
								   @RequestParam(name = "folderName",required = false) String folderName) throws ClientProtocolException, IOException{
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
			
//			String id=getRequest().getParameter("id");//申请人id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String folderName=getRequest().getParameter("folderName");//id
		    if(folderName!=null&&!folderName.equals("")){
		    	folderName= URLEncoder.encode(folderName, "UTF-8");
		    	folderName=URLEncoder.encode(folderName,"UTF-8");
		    }
			
			
			LOGGER.info("renameFolderSave REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "renameFolderSave"
			+"&id="+id+"&folderName="+folderName
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "renameFolderSave"
					+"&id="+id+"&folderName="+folderName
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("moveFolderSave :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"发票夹重名报错\",\"success\":false}";
			LOGGER.info("renameFolderSave :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("deleteFolderSave")
	public String  deleteFolderSave(@RequestParam(name = "userId") String userId,
									@RequestParam(name = "mobileId") String mobileId,//手机ID
									@RequestParam(name = "ciphertext") String ciphertext,
									@RequestParam(name = "moveId",required = false) String moveId,
									@RequestParam(name = "ids",required = false) String ids,
									@RequestParam(name = "imageId",required = false) String imageId) throws ClientProtocolException, IOException{
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
			
//			String moveId=getRequest().getParameter("moveId");//申请人id
//			String ids=getRequest().getParameter("ids");//申请人id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String imageId = getRequest().getParameter("imageId");//加密串
			
			
			LOGGER.info("deleteFolderSave REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "deleteFolderSave"
			+"&ids="+ids+"&imageId="+imageId
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "deleteFolderSave"
					+"&ids="+ids+"&imageId="+imageId
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
			
			LOGGER.info("moveFolderSave :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"删除发票夹报错\",\"success\":false}";
			LOGGER.info("deleteFolderSave :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("getUploadImgOcrStatus")
	public String  getUploadImgOcrStatus(@RequestParam(name = "userId") String userId,
										 @RequestParam(name = "mobileId") String mobileId,//手机ID
										 @RequestParam(name = "ciphertext") String ciphertext,
										 @RequestParam(name = "parentId",required = false) String parentId,
										 @RequestParam(name = "documentNum",required = false) String documentNum) throws ClientProtocolException, IOException{
		//========加密验证 start==========
		try{
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
//			if("1".equals(resultString)){
//				throw new Exception("加密验证失败！请刷新界面！");
//			}
		//========加密验证 end==========
//			String parentId=getRequest().getParameter("parentId");//申请人id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String ids = getRequest().getParameter("ids");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String documentNum = getRequest().getParameter("documentNum");//加密串
			
			LOGGER.info("newFolderSave REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getUploadImgOcrStatus"+"&parentId="+parentId
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&documentNum="+documentNum+"&versioFlag=N");
	
			String s = HttpServiceUtil.sendGet(baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getUploadImgOcrStatus"+"&parentId="+parentId
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&documentNum="+documentNum+"&versioFlag=N");
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询状态报错\",\"success\":false}";
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("getDetailMode")
	public String  getDetailMode(@RequestParam(name = "userId",required = false) String userId,
								 @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
								 @RequestParam(name = "ciphertext",required = false) String ciphertext,
								 @RequestParam(name = "invoiceId",required = false) String invoiceId,
								 @RequestParam(name = "invoiceType",required = false) String invoiceType) throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
			MobileRequestVO param = new MobileRequestVO();
//       param.setIsEmessageLogin(isEmessageLogin);
			param.setUserId(userId);
			param.setCiphertext(ciphertext);
			String resultString = CodeUtil.setMessageObject(param,baseUrl,"1");			if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}
			//========加密验证 end==========
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串

//			String invoiceId = getRequest().getParameter("invoiceId");//
//			String invoiceType = getRequest().getParameter("invoiceType");//
			




			LOGGER.info("getDetail REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getDetailMode"
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&invoiceId="+invoiceId+"&invoiceType="+invoiceType+"&versioFlag=N");
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getDetailMode"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&invoiceId="+invoiceId+"&invoiceType="+invoiceType+"&versioFlag=N");
			
			LOGGER.info("getDetail :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
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
	@RequestMapping("getDetail")
	public String  getDetail(@RequestParam(name = "userId",required = false) String userId,
							 @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
							 @RequestParam(name = "ciphertext",required = false) String ciphertext,
							 @RequestParam(name = "invoiceId",required = false) String invoiceId,
							 @RequestParam(name = "imageId",required = false) String imageId,
							 @RequestParam(name = "documentNum",required = false) String documentNum) throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
		/*	if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串

//			String imageId = getRequest().getParameter("imageId");//加密串
//			String documentNum = getRequest().getParameter("documentNum");//加密串
//			String invoiceId = getRequest().getParameter("invoiceId");//加密串
			
			
			LOGGER.info("getDetail REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getDetail"
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&imageId="+imageId+"&documentNum="+documentNum+"&invoiceId="+invoiceId+"&versioFlag=N");
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getDetail"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&imageId="+imageId+"&documentNum="+documentNum+"&invoiceId="+invoiceId+"&versioFlag=N");
			
			LOGGER.info("getDetail :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取发票夹详情报错\",\"success\":false}";
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("getCheckSaveAutoTax")
	public String  getCheckSaveAutoTax(@RequestParam(name = "userId",required = false) String userId,
									   @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
									   @RequestParam(name = "ciphertext",required = false) String ciphertext,
									   @RequestParam(name = "claimId",required = false) String claimId)throws ClientProtocolException, IOException{
		try{

			//========加密验证 start==========
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
		/*	if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========

//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串

//			String claimId = getRequest().getParameter("claimId");//情况编号


			LOGGER.info("getDetail REQUEST: " + baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getCheckSaveAutoTax"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&claimId="+claimId+"&versioFlag=N");

			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getCheckSaveAutoTax"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&claimId="+claimId+"&versioFlag=N");

			LOGGER.info("getCheckSaveAutoTax :" + URLDecoder.decode(s,"UTF-8"));

//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取场景模板报错\",\"success\":false}";
			LOGGER.info("getCheckSaveAutoTax :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("getCheckSaveOrSendClaim")
	public String  getCheckSaveOrSendClaim(@RequestParam(name = "userId",required = false) String userId,
										   @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
										   @RequestParam(name = "ciphertext",required = false) String ciphertext,
										   @RequestParam(name = "invoiceIds",required = false) String invoiceIds)throws ClientProtocolException, IOException{
		try{

			//========加密验证 start==========
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
		/*	if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========

//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串

//			String invoiceIds = getRequest().getParameter("invoiceIds");//情况编号


			LOGGER.info("getDetail REQUEST: " + baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getCheckSaveOrSendClaim"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&invoiceIds="+invoiceIds+"&versioFlag=N");

			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getCheckSaveOrSendClaim"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&invoiceIds="+invoiceIds+"&versioFlag=N");

			LOGGER.info("getCheckSaveOrSendClaim :" + URLDecoder.decode(s,"UTF-8"));

//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取场景模板报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("deleteClaim")
	public String  deleteClaim(@RequestParam(name = "userId",required = false) String userId,
							   @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
							   @RequestParam(name = "ciphertext",required = false) String ciphertext,
							   @RequestParam(name = "claimId",required = false) String claimId,
							   @RequestParam(name = "itemId",required = false) String itemId)throws ClientProtocolException, IOException{
		try{

			//========加密验证 start==========
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
		/*	if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========

//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串

//			String claimId = getRequest().getParameter("claimId");//情况编号
//			String itemId = getRequest().getParameter("itemId");//情况编号


			LOGGER.info("getDetail REQUEST: " + baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "deleteClaim"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&claimId="+claimId+"&itemId="+itemId+"&versioFlag=N");

			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "deleteClaim"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&claimId="+claimId+"&itemId="+itemId+"&versioFlag=N");

			LOGGER.info("getDetail :" + URLDecoder.decode(s,"UTF-8"));

//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取场景模板报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("saveAutoTax")
	public String saveAutoTax(@RequestParam(name = "userId",required = false) String userId,
							  @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
							  @RequestParam(name = "ciphertext",required = false) String ciphertext,
							  @RequestParam(name = "claimId",required = false) String claimId,
							  @RequestParam(name = "invoiceIds",required = false) String invoiceIds) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
		/*	if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========

//			String userId = getsgetRequest().getParameter("invoiceIds");//加密串


			LOGGER.info("getDetail REQUEST: " + baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "saveAutoTax"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&invoiceIds="+invoiceIds+"&claimId="+claimId+"&versioFlag=N");

			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "saveAutoTax"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&invoiceIds="+invoiceIds+"&claimId="+claimId+"&versioFlag=N");

			LOGGER.info("getDetail :" + URLDecoder.decode(s,"UTF-8"));

//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取发票夹详情报错\",\"success\":false}";
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("saveAutoBudan")
	public String  saveAutoBudan(@RequestParam(name = "userId",required = false) String userId,
								 @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
								 @RequestParam(name = "ciphertext",required = false) String ciphertext,
								 @RequestParam(name = "claimId",required = false) String claimId,
								 @RequestParam(name = "invoiceIds",required = false) String invoiceIds,
								 @RequestParam(name = "budanFlag",required = false) String budanFlag) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
		/*	if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========

//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//
//			String claimId = getRequest().getParameter("claimId");//加密串
//			String invoiceIds = getRequest().getParameter("invoiceIds");//加密串
//			String budanFlag = getRequest().getParameter("budanFlag") == null?"N":getRequest().getParameter("budanFlag");


			LOGGER.info("getDetail REQUEST: " + baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "saveAutoBudan"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&invoiceIds="+invoiceIds+"&claimId="+claimId+"&budanFlag="+budanFlag+"&versioFlag=N");

			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "saveAutoBudan"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&invoiceIds="+invoiceIds+"&claimId="+claimId+"&budanFlag="+budanFlag+"&versioFlag=N");

			LOGGER.info("getDetail :" + URLDecoder.decode(s,"UTF-8"));

//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取发票夹详情报错\",\"success\":false}";
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("saveOrSendClaim")
	public String saveOrSendClaim(@RequestParam(name = "userId",required = false) String userId,
								  @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
								  @RequestParam(name = "ciphertext",required = false) String ciphertext,
								  @RequestParam(name = "parentId",required = false) String parentId,
								  @RequestParam(name = "data",required = false) String data) throws ClientProtocolException, IOException{
		try{
			//========加密验证 start==========
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
		/*	if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========

//			String parentId=getRequest().getParameter("parentId");//申请人id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String data	= getRequest().getParameter("data");//加密串

			String s = HttpServiceUtil.sendPost(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "saveOrSendClaim"+"&parentId="+parentId
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&versioFlag=N",data);

			LOGGER.info("getFolderList :" + URLDecoder.decode(s,"UTF-8"));

//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("getClaimTemplate")
	public String getClaimTemplate(@RequestParam(name = "userId",required = false) String userId,
								   @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
								   @RequestParam(name = "ciphertext",required = false) String ciphertext,
								   @RequestParam(name = "caseNo",required = false) String caseNo,
								   @RequestParam(name = "invoiceIds",required = false) String invoiceIds) throws ClientProtocolException, IOException{
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

//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//
//			String caseNo = getRequest().getParameter("caseNo");//情况编号
//			String invoiceIds = getRequest().getParameter("invoiceIds");//情况编号


			LOGGER.info("getDetail REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getClaimTemplate"
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&caseNo="+caseNo+"&invoiceIds="+invoiceIds+"&versioFlag=N");

			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getClaimTemplate"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&caseNo="+caseNo+"&invoiceIds="+invoiceIds+"&versioFlag=N");

			LOGGER.info("getDetail :" + URLDecoder.decode(s,"UTF-8"));

//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取场景模板报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("getClaimOptions")
	public String  getClaimOptions(@RequestParam(name = "userId",required = false) String userId,
								   @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
								   @RequestParam(name = "ciphertext",required = false) String ciphertext) throws ClientProtocolException, IOException{
		try{

			//========加密验证 start==========
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
		/*	if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========

//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串


			LOGGER.info("getDetail REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getClaimOptions"
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+ "&fromSystem=APP&versioFlag=Y");

			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getClaimOptions"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+ "&fromSystem=APP&versioFlag=Y");

			LOGGER.info("getDetail :" + URLDecoder.decode(s,"UTF-8"));

//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));

			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"获取场景列表报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("sendRecgOcrReq")
	public String sendRecgOcrReq (@RequestParam(name = "userId",required = false) String userId,
								  @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
								  @RequestParam(name = "ciphertext",required = false) String ciphertext,
								  @RequestParam(name = "id",required = false) String id)throws ClientProtocolException, IOException{
		
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
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String id = getRequest().getParameter("id");//加密串
			
			
			String s = HttpServiceUtil.sendGet(
					baseUrl + "appInvoiceFolderServlet?servletName=" + "saveOcrReqEimImage"
							+ "&id="+id
							+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext+"&versioFlag=N"
							);
			
			LOGGER.info("appInvoiceFolderServlet REQUEST: "+ "appInvoiceFolderServlet?servletName=" + "saveOcrReqEimImage"
					+ "&id="+id
					+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext +"&versioFlag=N"
					
			);
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"发送识别请求报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("sendVeriOcrReq")
	public String  sendVeriOcrReq (@RequestParam(name = "userId",required = false) String userId,
								   @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
								   @RequestParam(name = "ciphertext",required = false) String ciphertext,
								   @RequestParam(name = "invoiceId",required = false) String id)throws ClientProtocolException, IOException{
		
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
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
//			String id = getRequest().getParameter("invoiceId");//加密串
			
			
			String s = HttpServiceUtil.sendGet(
					baseUrl + "appInvoiceFolderServlet?servletName=" + "sendVeriOcrReq"
							+ "&id="+id
							+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext+0+"&versioFlag=N"
					);
			
			LOGGER.info("appInvoiceFolderServlet REQUEST: "+ "appInvoiceFolderServlet?servletName=" + "sendVeriOcrReq"
					+ "&id="+id
					+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext +"&versioFlag=N"
					
					);
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"发送验真请求报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	
	@RequestMapping("getSendImageList")
	public String  getSendImageList(@RequestParam(name = "userId",required = false) String userId,
									@RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
									@RequestParam(name = "ciphertext",required = false) String ciphertext,
									@RequestParam(name = "parentId",required = false) String parentId)throws ClientProtocolException, IOException{

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
			
//			String parentId=getRequest().getParameter("parentId");//申请人id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			
			
			System.out.println("getFolderList REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getSendImageList"+"&parentId="+parentId
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"versioFlag=N");
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getSendImageList"+"&parentId="+parentId
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&versioFlag=N");
			
			LOGGER.info("getFolderList :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询影像列表报错\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}
	@RequestMapping("getInvoiceTypeList")
	public String getInvoiceTypeList(@RequestParam(name = "userId",required = false) String userId,
									 @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
									 @RequestParam(name = "ciphertext",required = false) String ciphertext) throws ClientProtocolException, IOException{
		try{
			
			//========加密验证 start==========
//			String resultString = CodeUtil.setMessageObject(getRequest(),getResponse(),baseUrl,"1");
		/*	if("1".equals(resultString)){
				throw new Exception("加密验证失败！请刷新界面！");
			}*/
			//========加密验证 end==========
			
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串

			
			LOGGER.info("getDetail REQUEST: " + baseUrl
			+ "appInvoiceFolderServlet?servletName=" + "getInvoiceTypeList"
			+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&versioFlag=N");
			
			String s = HttpServiceUtil.sendGet(baseUrl
					+ "appInvoiceFolderServlet?servletName=" + "getInvoiceTypeList"
					+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext+"&versioFlag=N");
			
			LOGGER.info("getDetail :" + URLDecoder.decode(s,"UTF-8"));
			
//			getRequest().setAttribute("object", URLDecoder.decode(s,"UTF-8"));
			
			return URLDecoder.decode(s,"UTF-8");
		} catch (Exception e) {
			String str = "{\"code\":1,\"result\":null,\"message\":\"查询\",\"success\":false}";
			LOGGER.info("ccSaveQuestionServlet :" + str);
//			getRequest().setAttribute("object", str);
//			writeString(str);
			e.printStackTrace();
			return str;
		}
	}

	/**
	 * app 发票夹 影像上传
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("uploadEimFile")
	public String  uploadEimFile(@RequestParam(name = "userId",required = false) String userId,
								 @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
								 @RequestParam(name = "ciphertext",required = false) String ciphertext,
								 @RequestParam(name = "parentId",required = false) String parentId,
								 @RequestParam(name = "multiFiles",required = false) MultipartFile[] multiFiles) throws ClientProtocolException, IOException{
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
//			String parentId = getRequest().getParameter("parentId");//
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			String eimScanningType = "发票夹";//类型
			
			eimScanningType= URLEncoder.encode(eimScanningType, "UTF-8");
			eimScanningType= URLEncoder.encode(eimScanningType,"UTF-8");
			//封装返回结果
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(parentId==null||parentId.equals("null")||parentId.equals("")||parentId.equals("0")){
				parentId="0";
			}
			
			//生成批次号
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String batchNum = sdf.format(date);
			Random random = new Random();
			batchNum += random.nextInt(9999999);
			
			String claimNo="I"+batchNum;
			try {
//				Map<String,Object> mapResult =getMultipartFileList(getRequest());
//				File[] multiFiles = (File[]) mapResult.get("multiFiles");
//				String[] fileNames =  (String[]) mapResult.get("fileName");
//				for (int i = 0; i < multiFiles.length; i++) {
//					File multiFile = multiFiles[i].;
//					String fileName = multiFiles[i].getOriginalFilename();
//					int i1 = fileName.lastIndexOf(".");
//					String suffix = fileName.substring(i1, fileName.length());
//					String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//				}
				String compId = HttpServiceUtil.sendGet(baseUrl
						+ "commonComponentAppServlet?servletName=" + "getCompId"+ "&userId=" + userId+ "&mobileId=" + mobileId+ "&ciphertext=" + ciphertext);
				compId = URLDecoder.decode(compId,"UTF-8");
				if(compId==null || "".equals(compId)){
					String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败\",\"success\":false}";
					LOGGER.info("appUploadFile :" + str);
					return str;
				}
				//获取影像上传路径
				LOGGER.info(
						"appUploadEimFile REQUEST: " + baseUrl + "appInvoiceFolderServlet?"
								+ "&servletName=" + "uploadEimFile" + "&userId=" + userId + "&mobileId="+ mobileId
								+ mobileId + "&ciphertext=" + ciphertext + "11&batchNum=" + batchNum);

				String localUploadPath = HttpServiceUtil
						.sendGet(baseUrl + "appInvoiceFolderServlet?"
								+ "&servletName=" + "uploadEimFile" + "&userId=" + userId + "&mobileId=" + mobileId
								+ "&ciphertext=" + ciphertext + "11&batchNum=" + batchNum);
				localUploadPath = URLDecoder.decode(localUploadPath,"UTF-8");
				// /data/server/appfiles_use/attachment_eim//appInvoiceFolder/20200615152324994188850/74884/2020/06/15/image
				//获取缩略影像上传路径
				LOGGER.info(
						"appUploadEimFile REQUEST: " + baseUrl + "appInvoiceFolderServlet?"
								+ "&servletName=" + "uploadEimSmallFile" + "&userId=" + userId + "&mobileId="
								+ mobileId + "&ciphertext=" + ciphertext + "22&batchNum=" + batchNum);

				String localSmallUploadPath = HttpServiceUtil
						.sendGet(baseUrl + "appInvoiceFolderServlet?"
								+ "&servletName=" + "uploadEimSmallFile" + "&userId=" + userId + "&mobileId=" + mobileId
								+ "&ciphertext=" + ciphertext + "22&batchNum=" + batchNum);
				localSmallUploadPath = URLDecoder.decode(localSmallUploadPath,"UTF-8");
				///data/server/appfiles_use/attachment_eim//appInvoiceFolder/20200615152324994188850/74884/2020/06/15/image/small
				if(localUploadPath==null || "".equals(localUploadPath) || "1".equals(localUploadPath) || (localSmallUploadPath==null || "".equals(localSmallUploadPath) || "1".equals(localSmallUploadPath))){
					String str = "{\"code\":1,\"result\":null,\"message\":\"文件上传失败asdasd287318273612783612\",\"success\":false}";
					LOGGER.info("uploadEimFile :" + str);
//					getRequest().setAttribute("object", str);
//					writeString(str);
					return str;
				}else{
					/*Map<String,Object> result =	JSONObject.parseObject(URLDecoder.decode(localSmallUploadPath,"UTF-8"),  Map.class);
					if(result !=null && result.containsKey("code") && result.get("code") != null ){
						String code = result.get("code").toString();
						if(code.equals("102")){
							getRequest().setAttribute("object", URLDecoder.decode(localSmallUploadPath,"UTF-8"));
							writeString(URLDecoder.decode(localSmallUploadPath,"UTF-8"));
							return;
						}
					}*/
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
		                	// 生成影像文件
		                	File newFile = new File(newPath);
							String normalName=UUID.randomUUID().toString();
//							FileUtils.copyFile();
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
								String localUploadPathIn= URLEncoder.encode(localUploadPath, "UTF-8");
								localUploadPathIn=URLEncoder.encode(localUploadPathIn,"UTF-8");
//								String smallName=UUID.randomUUID().toString();
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
								if(jresult.getString("code").equals("1")){
									String localSmallUploadPathIn= URLEncoder.encode(localSmallUploadPath, "UTF-8");
									localSmallUploadPathIn=URLEncoder.encode(localSmallUploadPathIn,"UTF-8");
									LOGGER.info(
											baseUrl + "appInvoiceFolderServlet?servletName=" + "saveEimImage"
													+ "&fileName=" + URLEncoder.encode(URLEncoder.encode(normalName, "UTF-8"), "UTF-8")  + "&claimNo="+claimNo+ "&parentId="+parentId
													+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext + i
													+ "&batchNum=" + batchNum + "&eimScanningType=" + eimScanningType+"&localUploadPath="+localUploadPathIn+"&localSmallUploadPath="+localSmallUploadPathIn
									);
									String s = HttpServiceUtil.sendGet(
											baseUrl + "appInvoiceFolderServlet?servletName=" + "saveEimImage"
													+ "&fileName=" + URLEncoder.encode(URLEncoder.encode(normalName, "UTF-8"), "UTF-8")  + "&claimNo="+claimNo+ "&parentId="+parentId
													+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext + i
													+ "&batchNum=" + batchNum + "&eimScanningType=" + eimScanningType+"&localUploadPath="+localUploadPathIn+"&localSmallUploadPath="+localSmallUploadPathIn );
									Map<String,Object> result =	JSONObject.parseObject(URLDecoder.decode(s,"UTF-8"),  Map.class);

									LOGGER.info("saveEimImage REQUEST: " + baseUrl
											+ "appInvoiceFolderServlet?servletName=" + "saveEimImage" + "&fileName="
											+ URLEncoder.encode(URLEncoder.encode(normalName, "UTF-8"), "UTF-8")  + "&userId=" + "&claimNo="+claimNo + "&parentId="+parentId
											+ userId + "&mobileId=" + mobileId + "&ciphertext="+ ciphertext + i
											+ "&batchNum=" + batchNum + "&eimScanningType=" + eimScanningType
									);
									String status = (String) result.get("status");
									System.out.println(i+">>>>>>>>"+result.toString());
									if("S".equals(status)){
										Map tAppInvoiceFolderOcrReq = (Map) result.get("tAppInvoiceFolderOcrReq");
										String pid = (String)tAppInvoiceFolderOcrReq.get("id");
										String s2 = HttpServiceUtil.sendGet(
												baseUrl + "appInvoiceFolderServlet?servletName=" + "saveOcrReqEimImage"
														+ "&id="+pid
														+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext + (i+100)
														+ "&batchNum=" + batchNum );
										LOGGER.info("appInvoiceFolderServlet REQUEST: "+ "appInvoiceFolderServlet?servletName=" + "saveOcrReqEimImage"
												+ "&id="+pid
												+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext + (i+100)
												+ "&batchNum=" + batchNum
										);
										Map<String,Object> result2 =JSONObject.parseObject(URLDecoder.decode(s2,"UTF-8"),  Map.class);
										result.put("status",result2.get("status"));
										result.put("message",result2.get("message"));
									}
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
					 JSONObject rsJsonObj=new JSONObject();
					 rsJsonObj.put("documentNum", claimNo);
					 Map<String,Object> mapR = new HashMap<String,Object>();
					 mapR.put("code", "0");
					 mapR.put("result", rsJsonObj);
					 mapR.put("message", "文件操作成功");
					 mapR.put("success", "true");
					 String str = JSONObject.toJSONString(mapR,SerializerFeature.WriteMapNullValue);
//					 getRequest().setAttribute("object", str);
//					 writeString(str);
					return str;
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
	 * app 发票夹 影像删除
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@RequestMapping("deleteEimFile")
	public String  deleteEimFile(@RequestParam(name = "userId",required = false) String userId,
								 @RequestParam(name = "mobileId",required = false) String mobileId,//手机ID
								 @RequestParam(name = "ciphertext",required = false) String ciphertext,
								 @RequestParam(name = "imageId",required = false) String imageId) throws ClientProtocolException, IOException{
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
//		    String imageId = getRequest().getParameter("imageId");//影像id
//			String userId = getRequest().getParameter("userId");//登录用户ID
//			String mobileId = getRequest().getParameter("mobileId");//手机ID
//			String ciphertext = getRequest().getParameter("ciphertext");//加密串
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			LOGGER.info(
					"appDeleteEimFile REQUEST: " + baseUrl + "appInvoiceFolderServlet?"+"servletName=" + "deleteEimFile" 
			+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext + "&imageId=" + imageId);

			String s = HttpServiceUtil
					.sendGet(baseUrl + "appInvoiceFolderServlet?"+"servletName=" + "deleteEimFile" 
							+ "&userId=" + userId + "&mobileId="+ mobileId + "&ciphertext=" + ciphertext + "&imageId=" + imageId);
			Map<String,Object> result =	JSONObject.parseObject(URLDecoder.decode(s,"UTF-8"),  Map.class);
			resultList.add(result);
			 Map<String,Object> mapR = new HashMap<String,Object>();
			 mapR.put("code", "0");
			 mapR.put("result", resultList);
			 mapR.put("message", "文件操作成功");
			 mapR.put("success", "true");
			 String str = JSONObject.toJSONString(mapR,SerializerFeature.WriteMapNullValue);
//			 getRequest().setAttribute("object", str);
//			 writeString(str);
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
     * @param request 
     * @return 
     */  
    protected Map<String,Object> getMultipartFileList(  
            HttpServletRequest request) {  
//        List<MultipartFile> files = new ArrayList<MultipartFile>();  
    	File[] files = null;
        try {  
        	Map<String,Object> map = new HashMap<String,Object>();
        	MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) request;
        	
        	Enumeration fileParameterNames = multiWrapper.getFileParameterNames();
        	
        	while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
                // get the value of this input tag
    			//[得到下一个文件域的name属性值]
                String inputName = (String) fileParameterNames.nextElement();
             // get the content type
    			//[得到这个请求中的mime文件类型数组]
                String[] contentType = multiWrapper.getContentTypes(inputName);
                
    			//[如果这个上下文类型数组非空]
                if (contentType!=null && !"".equals(contentType)) {
                    // get the name of the file from the input tag
    				//[得到文件名数组]
                    String[] fileName = multiWrapper.getFileNames(inputName);
                    map.put("fileName", fileName);	
    				//如果文件名数组是非空的（有文件被选择）
                    if (fileName!=null && !"".equals(fileName)) {
                        // get a File object for the uploaded File
    					//[得到文件数组]
                        files = multiWrapper.getFiles(inputName);
                        map.put("multiFiles", files);
                        return map;
                    }
                }
            }
        } catch (Exception ex) {  
        	ex.printStackTrace();
        	return null;  
        }  
        return null; 
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
