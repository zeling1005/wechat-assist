package com.zeling.wa.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
	
	private HttpUtils() {
		throw new AssertionError(HttpUtils.class.getName() + ": 禁止实例化");
	}
}
