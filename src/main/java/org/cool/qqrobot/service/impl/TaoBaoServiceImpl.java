package org.cool.qqrobot.service.impl;

import java.util.Date;
import java.util.Map;

import org.cool.qqrobot.common.DateTimeUtil;
import org.cool.qqrobot.entity.MyHttpRequest;
import org.cool.qqrobot.entity.MyHttpResponse;
import org.cool.qqrobot.entity.TaobaoProcessData;
import org.cool.qqrobot.service.TaoBaoService;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
/**
 * 类名称：TaoBaoServiceImpl
 * 类描述：
 * 创建人：arlin	
 * 修改人：arlin
 * 修改时间：2017年11月1日 上午12:19:49
 * 修改备注：
 * @version 1.0.0
 *
 */
@Service
public class TaoBaoServiceImpl implements TaoBaoService {
	/**
	 * req===>
	 * https://qrlogin.taobao.com/qrcodelogin/generateQRCode4Login.do?from=tb&_ksTS=1509466468629_29&callback=jsonp30
	 * 																																											  ^这个是时间戳^_计数器_
	 * response<===
	 * (function(){jsonp30({"success":true,"message":"null","url":"//img.alicdn.com/tfscom/TB1iXRpacrI8KJjy0Fhwu2fnpXa.png","lgToken":"df3058e737f9033cf352e0eb9177e6da","adToken":"e4bb648c7071996fdc539a457a2d44bf"});})();
	 * url 二维码地址  lgToken 二维码识别码用户检查是否登录
	 * 
	 */
	@Override
	public Map<String, String> getQrCode(TaobaoProcessData tbpd) {
		MyHttpRequest codeRequest = new MyHttpRequest();
		codeRequest.setUrl("https://qrlogin.taobao.com/qrcodelogin/generateQRCode4Login.do?from=tb&_ksTS="+DateTimeUtil.nowDate().getTime()+"_"+tbpd.getCountPlus()+"&callback=jsonp"+tbpd.getCount());
		MyHttpResponse codeResponse = new MyHttpResponse();
		tbpd.setGetCode(true);
		try {
			codeResponse = tbpd.getMyHttpClient().execute(codeRequest);
		} catch (Exception e) {
			e.printStackTrace();
			tbpd.setGetCode(false);
		}
		String rJson = codeResponse.getTextStr().replaceAll("\\(function\\(\\)\\{jsonp"+tbpd.getCount()+"\\(", "").replaceAll("\\);\\}\\)\\(\\);", "");
		Gson gson = new Gson();
		Map<String, String> map = gson.fromJson(rJson, Map.class);
		tbpd.setLgToken(map.get("lgToken"));
		tbpd.setAdToken(map.get("adToken"));
		return map;
	}
	/**
	 * req===>
	 * https://qrlogin.taobao.com/qrcodelogin/qrcodeLoginCheck.do?lgToken=df3058e737f9033cf352e0eb9177e6da&defaulturl=https%3A%2F%2Fwww.taobao.com%2F&_ksTS=1509463744749_214&callback=jsonp215
	 * response<===
	 * <===未扫描
	 * (function(){jsonp215({"code":"10000","message":"login start state","success":true});})();
	 * code:10004表示需要重新获取二维码
	 * <===手机扫描
	 * (function(){jsonp215({"code":"10001","message":"mobile scan QRCode success","success":true});})();
	 * <===手机点击登录
	 * (function(){jsonp215({"code":"10006","success":true,"url":"https://login.taobao.com/member/loginByIm.do?uid=cntaobaoarlin666364&token=152e55bb1a983ac1de3ea5a6b4f1de58&time=1509465493397&asker=qrcodelogin&ask_version=1.0.0&defaulturl=https%3A%2F%2Fwww.taobao.com%2Fgo%2Fact%2Floginsuccess%2Ftaobao.php&webpas=83dbb6feac1d7fd2de10e107e153d1b61975240123"});})();
	 * url 二维码地址  lgToken 二维码识别码用户检查是否登录
	 * 
	 */
	@Override
	public Map<String, String> checkQrCode(TaobaoProcessData tbpd) {
		MyHttpRequest codeRequest = new MyHttpRequest();
		codeRequest.setUrl("https://qrlogin.taobao.com/qrcodelogin/qrcodeLoginCheck.do?lgToken="+tbpd.getLgToken()+"&defaulturl=defaulturl=https%3A%2F%2Fwww.taobao.com%2Fgo%2Fact%2Floginsuccess%2Ftaobao.php&_ksTS="+DateTimeUtil.nowDate().getTime()+"_"+tbpd.getCountPlus14()+"&callback=jsonp"+tbpd.getCountPlus());
		MyHttpResponse codeResponse = new MyHttpResponse();
		try {
			codeResponse = tbpd.getMyHttpClient().execute(codeRequest);
		} catch (Exception e) {
			e.printStackTrace();
			tbpd.setCodeScanned(false);
		}
		String rJson = codeResponse.getTextStr().replaceAll("\\(function\\(\\)\\{jsonp"+tbpd.getCount()+"\\(", "").replaceAll("\\);\\}\\)\\(\\);", "");
		Map<String, String> map = new Gson().fromJson(rJson, Map.class);
		if (map.containsKey("code")&&map.get("code").equals("10001")) {
			tbpd.setCodeScanned(true);//二维码已经扫描
		}
		if (map.containsKey("code")&&map.get("code").equals("10006")) {
			tbpd.setCodeScanned(true);//二维码已经扫描
			MyHttpRequest login = new MyHttpRequest();
			login.setUrl(map.get("url"));
			this.doLogin(tbpd,login);
		}
		return map;
	}
	/**
	 * req===>
	 * https://login.taobao.com/member/loginByIm.do?uid=cntaobaoarlin666364&token=f81c6e0bcf44df4a96ce06c303064360&time=1509725206146&asker=qrcodelogin&ask_version=1.0.0&defaulturl=https%3A%2F%2Fwww.taobao.com%2Fgo%2Fact%2Floginsuccess%2Ftaobao.php&webpas=7d368d1f0576906808a6b787a9f58f641975240123
	 * response<===
	 * cache-control:no-cache
		cache-control:no-store
		content-language:zh-CN
		content-length:266
		content-type:text/html; charset=gbk
		date:Tue, 31 Oct 2017 15:58:13 GMT
		expires:Thu, 01 Jan 1970 00:00:01 GMT
		location:https://www.taobao.com/go/act/loginsuccess/taobao.php
		p3p:CP=CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR
		p3p:CP='CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR'
		pragma:no-cache
		s:STATUS_NORMAL
		server:Tengine/Aserver
		set-cookie:uc3=nk2=AmPwILrae6GV9%2BA%3D&id2=VyTzwAn87mE%3D&vt3=F8dBzLKHeQbMOcQqU2s%3D&lg2=VFC%2FuZ9ayeYq2g%3D%3D;Domain=.taobao.com;Path=/;Expires=Thu, 30-Nov-2017 15:58:13 GMT;HttpOnly
		set-cookie:existShop=MTUwOTQ2NTQ5Mw%3D%3D; Domain=.taobao.com; Path=/
		set-cookie:lid=arlin666364; Domain=login.taobao.com; Expires=Wed, 31-Oct-2018 15:58:13 GMT; Path=/
		set-cookie:lgc=arlin666364; Domain=.taobao.com; Expires=Thu, 30-Nov-2017 15:58:13 GMT; Path=/
		set-cookie:tracknick=arlin666364; Domain=.taobao.com; Expires=Wed, 31-Oct-2018 15:58:13 GMT; Path=/
		set-cookie:cookie2=24a00e0d7b382cb7985c9db8dba07c7e;Domain=.taobao.com;Path=/;HttpOnly
		set-cookie:sg=453; Domain=.taobao.com; Path=/
		set-cookie:mt=np=&ci=0_0; Domain=.taobao.com; Expires=Tue, 07-Nov-2017 15:58:13 GMT; Path=/
		set-cookie:cookie1=UoKIYc%2Bh18XOLNL%2FUPju0WLoK69hnpdD5b18m9dgI%2FA%3D;Domain=.taobao.com;Path=/;HttpOnly
		set-cookie:tbcp=e=AmPwILrae6GV9%2BAace%2BzJMwUsA%3D%3D&f=UUjZelmGlyLZViMquxX7BEMLG04%3D; Domain=.caipiao.taobao.com; Path=/
		set-cookie:log=lty=UQ%3D%3D;Domain=login.taobao.com;Path=/;Expires=Thu, 30-Nov-2017 15:58:13 GMT;HttpOnly
		set-cookie:uc1=cookie14=UoTcBrynXRnOvg%3D%3D&lng=zh_CN&cookie16=UIHiLt3xCS3yM2h4eKHS9lpEOw%3D%3D&existShop=true&cookie21=UIHiLt3xSilUl20lEYIP%2BQ%3D%3D&tag=8&cookie15=UtASsssmOIJ0bQ%3D%3D&pas=0; Domain=.taobao.com; Path=/
		set-cookie:skt=6810cd145448a22b;Domain=.taobao.com;Path=/;HttpOnly
		set-cookie:t=6ad0115e34e3c001c6a9abd9f0414f66; Domain=.taobao.com; Expires=Mon, 29-Jan-2018 15:58:13 GMT; Path=/
		set-cookie:publishItemObj=Ng%3D%3D; Domain=.taobao.com; Path=/
		set-cookie:_cc_=WqG3DMC9EA%3D%3D; Domain=.taobao.com; Expires=Wed, 31-Oct-2018 15:58:13 GMT; Path=/
		set-cookie:tg=0; Domain=.taobao.com; Expires=Thu, 18-Jul-2019 15:58:13 GMT; Path=/
		set-cookie:_l_g_=Ug%3D%3D; Domain=.taobao.com; Path=/
		set-cookie:_nk_=arlin666364; Domain=.taobao.com; Path=/
		set-cookie:cookie17=VyTzwAn87mE%3D;Domain=.taobao.com;Path=/;HttpOnly
		set-cookie:lc=Vyh%2FMcoi3l0HTEWEu%2FqU7A%3D%3D;Domain=login.taobao.com;Path=/;Expires=Thu, 30-Nov-2017 15:58:13 GMT;HttpOnly
		set-cookie:unb=48010125;Domain=.taobao.com;Path=/;HttpOnly
		status:302
		strict-transport-security:max-age=31536000
		x-category:
	 */
	@Override
	public Map<String, String> doLogin(TaobaoProcessData tbpd,MyHttpRequest login) {
		try {
			tbpd.getMyHttpClient().execute(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//登录成功后执行跳转 https://www.taobao.com/go/act/loginsuccess/taobao.php
		//返回的页面值包括
		/**
		<!DOCTYPE HTML> 
		<html> <head>
		 	<meta charset="gbk"> 
		 	<title>登录成功</title> 
		 	<link rel="stylesheet" href="//assets.alicdn.com/tbra/2.0/tbsp.css?t=20081225.css" type="text/css" media="screen" /> 
		 	<style type="text/css"> html { background-color:#FAFAFA; overflow:hidden; } .msg24 { padding:80px 30px; } </style> </head> <body><script>
				with(document)with(body)with(insertBefore(createElement("script"),firstChild))
				setAttribute("exparams",
					"category=     &userid=48010125     &aplus&yunid=&QT5vO7MUVeeB7LtIi0AD&
					 asid=AY2T3AL0kPxZnvSwGAAAAAD8xc8sA2LWhQ==
					 &sidx=TbS3ATTrwEhe9UdPuNpzahVsVWyaeB0m0/pl9YTBJgftLQY8HecXMEv0xtoU78W2qjE31VBOS1FQdMDHyLWi8x/q3X7Y2jIV4YY7e7SwyfoeWqMSJx2cOVUv7eBpapMAOd1qLJGyHt2sMx54jS5fEJ/wyN4qgOHRedVbkjv6OmYNxWfDg35l/Jo4nclXY6VGRDrjEdnCFDA9Y8Cp4hz+mv67ECX2cl7ndy5V6Hh04bGEFcble6HQbiRWMDK+NctrHnv4INDRtp76JgNyh0Jfp3BlYBMXB9I0rRQ0FNbWwZez2e3Wdj/o0U/w9Shlkxn1Oq4dK2LW79wujbOvot8fZQ=="
					 ,id="tb-beacon-aplus",src=(location>"https"?"//g":"//g")+".alicdn.com/alilog/mlog/aplus_v2.js")
</script> <div class="msg24"><p class="ok naked naked-ok">登录成功, 如果页面没有响应, 请尝试刷新试下.</p></div> <script> 
          (function() { var host = location.host, paths = host.split('.'), postfix = paths.pop(); try { document.domain = paths.pop() + '.' + postfix; } catch(e) {}; })(); </script>
          <input id='J_TbToken' name='_tb_token_' type='hidden' value=QT5vO7MUVeeB7LtIi0AD /> </body></html>
		 */
		//然后又调用了 
		// TODO Auto-generated method stub
		return null;
	}
	

}
