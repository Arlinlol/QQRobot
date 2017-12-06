package org.cool.qqrobot.entity;
/**
 * 类名称：TaobaoProcessData
 * 类描述：存储过程数据
 * 创建人：arlin	
 * 修改人：arlin
 * 修改时间：2017年10月31日 下午11:16:38
 * 修改备注：
 * @version 1.0.0
 *
 */
public class TaobaoProcessData extends ProcessData {
	private int count = 20;
	private String lgToken;//二维码登录token
	private String adToken;//
	
	public int getCountPlus14() {
		return 14+count;
	}
	public int getCountPlus() {
		return ++count;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getLgToken() {
		return lgToken;
	}
	public void setLgToken(String lgToken) {
		this.lgToken = lgToken;
	}
	
	public String getAdToken() {
		return adToken;
	}
	public void setAdToken(String adToken) {
		this.adToken = adToken;
	}
	
	
	
}
