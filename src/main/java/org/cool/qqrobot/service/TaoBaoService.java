package org.cool.qqrobot.service;

import java.util.Map;

import org.cool.qqrobot.entity.MyHttpRequest;
import org.cool.qqrobot.entity.TaobaoProcessData;

public interface TaoBaoService {
		//获取二维码内容
		public Map<String, String> getQrCode(TaobaoProcessData tbpd);
		//检验二维码是否已经被扫描
		public Map<String, String> checkQrCode(TaobaoProcessData tbpd);
		//执行登录
		public Map<String, String> doLogin(TaobaoProcessData tbpd,MyHttpRequest login);
}
