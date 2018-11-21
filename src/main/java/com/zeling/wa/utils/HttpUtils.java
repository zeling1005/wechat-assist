package com.zeling.wa.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		String result = null;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "utf-8");
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static String doPost(String url, Map<String, String> params) {
        	CloseableHttpClient client = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);  
            
            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
            for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();) {
    			String name = iter.next();
    			String value = params.get(name);
    			nvps.add(new BasicNameValuePair(name, value));
    		}
            try {
				request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
            
            CloseableHttpResponse response = null;
            String result = null;
			try {
				response = client.execute(request);
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity, "utf-8");
			} catch (ParseException | IOException e) {
				e.printStackTrace();
			}
            return result;
	}
	
	private HttpUtils() {
		throw new AssertionError(HttpUtils.class.getName() + ": 禁止实例化");
	}
}
