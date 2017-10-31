package org.cool.qqrobot.entity;
/**
 *
 * 类名称：Zhe800ProcessData
 * 类描述：zhe800存放过程数据
 * 创建人：arlin	
 * 修改人：arlin
 * 修改时间：2017年6月30日 下午11:24:23
 * 修改备注：
 * @version 1.0.0
 *
 */
public class Zhe800ProcessData extends ProcessData {
	private String token;//存储zhe800的会话token
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
