package org.cool.qqrobot.web;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cool.qqrobot.common.Const;
import org.cool.qqrobot.common.RobotCodeEnums;
import org.cool.qqrobot.dto.RobotResult;
import org.cool.qqrobot.entity.ProcessData;
import org.cool.qqrobot.entity.Zhe800ProcessData;
import org.cool.qqrobot.service.Zhe800Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

/**
 * 类名称：Zhe800Controller
 * 类描述：主要负责zhe800帐号的二维码登录功能
 * 创建人：arlin	
 * 修改人：arlin
 * 修改时间：2017年6月30日 下午9:58:01
 * 修改备注：
 * @version 1.0.0
 *
 */
@Controller
@RequestMapping("/zhe800")
public class Zhe800Controller extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Zhe800Service z8s;
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String getCodeToLoginV(HttpSession session, Model model) {
		logger.info("调用获得zhe800登录的登录二维码的功能页面.....");
		Zhe800ProcessData processDataSession = (Zhe800ProcessData) session.getAttribute(Const.Z800_PROCESS_DATA);
		if(null == processDataSession){
			logger.info("首次登录，存储会话...");
			session.setAttribute(Const.Z800_PROCESS_DATA, new Zhe800ProcessData());
		}
		return "/zhe800/zhe800Login";
	}
	@RequestMapping(value="/getCode", method = RequestMethod.GET)
	public void getCodeToFrond(HttpSession session, Model model,HttpServletResponse response){
		logger.info("获取二维码...");
		Zhe800ProcessData processDataSession = (Zhe800ProcessData) session.getAttribute(Const.Z800_PROCESS_DATA);
		Map<String, String> qrcode = z8s.getQrCode(processDataSession);
		super.ResponseJson(response, new RobotResult<Map>(true, RobotCodeEnums.REQUEST_SUCCESS.getCode(), qrcode));
	}
	
	@RequestMapping(value="/checkCode", method = RequestMethod.GET)
	public void checkQrcode(HttpSession session, Model model,HttpServletResponse response){
		Zhe800ProcessData processDataSession = (Zhe800ProcessData) session.getAttribute(Const.Z800_PROCESS_DATA);
		Map<String, String> checkStatus = z8s.checkQrCode(processDataSession);
		super.ResponseJson(response, new RobotResult<Map>(true, RobotCodeEnums.REQUEST_SUCCESS.getCode(), checkStatus));
	}
	
}
