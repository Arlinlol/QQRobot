package org.cool.qqrobot.dao;

import java.util.Map;

import org.cool.qqrobot.entity.MyHttpRequest;
import org.cool.qqrobot.entity.MyHttpResponse;
import org.cool.qqrobot.http.MyHttpClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mysql.jdbc.log.Log;

public class Zhe800Test {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	public void TestGetQrcodeString() {
		MyHttpRequest codeRequest = new MyHttpRequest();
		codeRequest.setUrl("https://passport.zhe800.com/j/users/qrcode/login/get_token?callback=get_scan_token4");
		MyHttpResponse codeResponse = new MyHttpResponse();
		try {
			codeResponse = MyHttpClient.getNewInstance().execute(codeRequest);
			String rJson = codeResponse.getTextStr().replaceAll("get_scan_token4\\(", "").replaceAll("\\)", "");
			logger.info(rJson);
			Gson gson = new Gson();
			Map<String, String> map = gson.fromJson(rJson, Map.class);
			System.out.println(map);
			System.out.println("https://m.zhe800.com/mz/scanlogin?_lu_m_k=ukm809Ax&crypt_token="+map.get("cryptToken"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestCheckQrcode()
	{
		//https://passport.zhe800.com/j/users/qrcode/login/check_token?callback=jQuery1720011656905485599056_1498873990334&token=0c8fa3e814f64025a4f54f989f95eb2c
		MyHttpRequest codeRequest = new MyHttpRequest();
		codeRequest.setUrl("https://passport.zhe800.com/j/users/qrcode/login/check_token?callback=jQueryaaa&token=0c8fa3e814f64025a4f54f989f95eb2c");
		MyHttpResponse codeResponse = new MyHttpResponse();
		try {
			codeResponse = MyHttpClient.getNewInstance().execute(codeRequest);
			System.out.println(codeResponse.getTextStr());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
