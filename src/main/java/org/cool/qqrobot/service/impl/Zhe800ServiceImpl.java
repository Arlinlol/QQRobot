package org.cool.qqrobot.service.impl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.cool.qqrobot.entity.MyHttpRequest;
import org.cool.qqrobot.entity.MyHttpResponse;
import org.cool.qqrobot.entity.Zhe800ProcessData;
import org.cool.qqrobot.service.Zhe800Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class Zhe800ServiceImpl implements Zhe800Service {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private HttpSession session = null;
    /*
     *  {"code":0,"cryptToken":"2fbfed213c728983e3d1215cbf541380ebebad0d50f89e9ad2782680d6dee106","interval":"3000","token":"f5f5171af65949ffb2f872305c2fae0f"}
     * @see org.cool.qqrobot.service.Zhe800Service#getQrCode(org.cool.qqrobot.entity.Zhe800ProcessData)
     */
	@Override
	public Map<String, String> getQrCode(Zhe800ProcessData z8pd) {
		MyHttpRequest codeRequest = new MyHttpRequest();
		codeRequest.setUrl("https://passport.zhe800.com/j/users/qrcode/login/get_token?callback=get_scan_token4");
		MyHttpResponse codeResponse = new MyHttpResponse();
		z8pd.setGetCode(true);
		try {
			codeResponse = z8pd.getMyHttpClient().execute(codeRequest);
		} catch (Exception e) {
			e.printStackTrace();
			z8pd.setGetCode(false);
		}
		String rJson = codeResponse.getTextStr().replaceAll("get_scan_token4\\(", "").replaceAll("\\)", "");
		Gson gson = new Gson();
		Map<String, String> map = gson.fromJson(rJson, Map.class);
		map.put("qrStr", "https://m.zhe800.com/mz/scanlogin?_lu_m_k=ukm809Ax&crypt_token=" + map.get("cryptToken"));
		z8pd.setToken(map.get("token"));
		return map;
	}
	//{"code":1,"errMsg":"cookie不合法","status":"5"}  {"code":0,"errMsg":"未扫描","status":"1"} {"code":0,"errMsg":"已扫描","status":"2"}
	@Override
	public Map<String, String> checkQrCode(Zhe800ProcessData z8pd) {
		MyHttpRequest codeRequest = new MyHttpRequest();
		codeRequest.setUrl("https://passport.zhe800.com/j/users/qrcode/login/check_token?callback=jQueryaaa&token="+z8pd.getToken());
		MyHttpResponse codeResponse = new MyHttpResponse();
		try {
			codeResponse = z8pd.getMyHttpClient().execute(codeRequest);
		} catch (Exception e) {
			e.printStackTrace();
			z8pd.setCodeScanned(false);
		}
		String rJson = codeResponse.getTextStr().replaceAll("jQueryaaa\\(", "").replaceAll("\\)", "");
		Map<String, String> map = new Gson().fromJson(rJson, Map.class);
		return map;
	}

}
