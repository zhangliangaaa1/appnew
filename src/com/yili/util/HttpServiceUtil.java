package com.yili.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpServiceUtil {
	public static String sendGet(String url) throws ClientProtocolException,
			IOException {
		// 创建HttpClient实例
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		// httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
		// new HttpHost("10.10.101.9", 8080));
		// 创建Get方法实例
		HttpGet httpgets = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpgets);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instreams = entity.getContent();
			String str = convertStreamToString(instreams);
			httpgets.abort();
			return str;
		}
		return null;
	}

	public static String sendPost(String url, String regJson) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);

		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(new StringEntity(regJson,"UTF-8")); //��ֹ��������
		HttpResponse response = httpclient.execute(httpPost);
		System.out.println(response.getStatusLine().getStatusCode() + "\n");
		HttpEntity entity = response.getEntity();
		String responseContent = EntityUtils.toString(entity, "UTF-8");
		System.out.println(responseContent);
		return responseContent;
	}
	public static String convertStreamToString(InputStream is) {
		StringBuilder sb1 = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;

		try {
			while ((size = is.read(bytes)) > 0) {
				// String str = new String(bytes, 0, size, "gbk");
				String str = new String(bytes, 0, size, "UTF-8");
				sb1.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb1.toString();
	}

	public static void main(String args[]) {
		try {
			Properties p = new Properties();
			p.load(HttpServiceUtil.class.getResourceAsStream("WebAPP.properties"));
			String s = HttpServiceUtil.sendGet(p.getProperty(args[0]));
			JSONObject jsonObject = JSONObject.fromObject(s);
			System.out.print(jsonObject.get("access_token"));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
