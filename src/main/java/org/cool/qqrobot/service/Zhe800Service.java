package org.cool.qqrobot.service;

import java.util.Map;

import org.cool.qqrobot.entity.Zhe800ProcessData;

/**
 *
 * 类名称：Zhe800Service
 * 类描述：折800扫码登录服务类
 * 创建人：arlin	
 * 修改人：arlin
 * 修改时间：2017年6月30日 下午11:20:48
 * 修改备注：
 * @version 1.0.0
 *
 */
public interface Zhe800Service {
	//获取二维码内容
	public Map<String, String> getQrCode(Zhe800ProcessData z8pd);
	//检验二维码是否已经被扫描
	public Map<String, String> checkQrCode(Zhe800ProcessData z8pd);
	//检验二维码是否已经登录
}
