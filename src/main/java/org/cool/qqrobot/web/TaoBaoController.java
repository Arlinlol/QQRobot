package org.cool.qqrobot.web;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cool.qqrobot.common.Const;
import org.cool.qqrobot.common.RobotCodeEnums;
import org.cool.qqrobot.dto.RobotResult;
import org.cool.qqrobot.entity.TaobaoProcessData;
import org.cool.qqrobot.entity.Zhe800ProcessData;
import org.cool.qqrobot.service.TaoBaoService;
import org.cool.qqrobot.service.Zhe800Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * 类名称：TaoBaoController
 * 类描述：负责淘宝帐号的登录登出
 * 创建人：arlin	
 * 修改人：arlin
 * 修改时间：2017年10月31日 下午11:07:09
 * 修改备注：
 * @version 1.0.0
 *
 */
@Controller
@RequestMapping("/taobao")
public class TaoBaoController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TaoBaoService tbService;
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String getCodeToLoginV(HttpSession session, Model model) {
		logger.info("调用获得淘宝登录的登录二维码的功能页面.....");
		TaobaoProcessData processDataSession = (TaobaoProcessData) session.getAttribute(Const.TAOBAO_PROCESS_DATA);
		if(null == processDataSession){
			logger.info("首次登录，存储会话...");
			session.setAttribute(Const.TAOBAO_PROCESS_DATA, new TaobaoProcessData());
		}
		return "/taobao/taobaoLogin";
	}
	@RequestMapping(value="/getCode", method = RequestMethod.GET)
	public void getCodeToFrond(HttpSession session, Model model,HttpServletResponse response){
		logger.info("获取二维码...");
		TaobaoProcessData processDataSession = (TaobaoProcessData) session.getAttribute(Const.TAOBAO_PROCESS_DATA);
		Map<String, String> qrcode = tbService.getQrCode(processDataSession);
		super.ResponseJson(response, new RobotResult<Map>(true, RobotCodeEnums.REQUEST_SUCCESS.getCode(), qrcode));
	}
	
	@RequestMapping(value="/checkCode", method = RequestMethod.GET)
	public void checkQrcode(HttpSession session, Model model,HttpServletResponse response){
		TaobaoProcessData processDataSession = (TaobaoProcessData) session.getAttribute(Const.TAOBAO_PROCESS_DATA);
		Map<String, String> checkStatus = tbService.checkQrCode(processDataSession);
		super.ResponseJson(response, new RobotResult<Map>(true, RobotCodeEnums.REQUEST_SUCCESS.getCode(), checkStatus));
	}
	
}
