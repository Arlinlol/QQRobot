package org.cool.qqrobot.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapUtils;
import org.cool.qqrobot.common.CacheMap;
import org.cool.qqrobot.common.Const;
import org.cool.qqrobot.common.RobotCodeEnums;
import org.cool.qqrobot.core.DataEngine;
import org.cool.qqrobot.dto.RobotResult;
import org.cool.qqrobot.entity.AutoReply;
import org.cool.qqrobot.entity.ProcessData;
import org.cool.qqrobot.exception.RobotException;
import org.cool.qqrobot.service.RobotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 主控制器，负责获取二维码登录以及登出等
 * @author zhoukl
 *
 */
@Controller
@RequestMapping("/robot")
public class RobotController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RobotService robotService;
	
	@RequestMapping(value="/getCodeToLogin", method = RequestMethod.GET)
	public String getCodeToLogin(HttpSession session, Model model) {
		// 通过session判断 防止同一用户多次刷新获取二维码
		ProcessData processDataSession = (ProcessData) session.getAttribute(Const.PROCESS_DATA);
		if (null != processDataSession && CacheMap.isOnline(processDataSession)) {
			// 登陆后可以设置是否自动回复，是否自定义回复名单
			AutoReply autoReply = processDataSession.getAutoReply();
			Map<String, Object> friendsViewMap = processDataSession.getFriendsViewMap();
			if (!Const.SUCCESS_CODE.equals(MapUtils.getInteger(friendsViewMap, Const.RET_CODE))) {
				friendsViewMap = robotService.buildFriendsList(processDataSession);
			}
			Map<String, Object> discussesViewMap = processDataSession.getDiscussesViewMap();
			if (!Const.SUCCESS_CODE.equals(MapUtils.getInteger(discussesViewMap, Const.RET_CODE))) {
				discussesViewMap = robotService.buildDiscussesList(processDataSession);
			}
			Map<String, Object> groupsViewMap = processDataSession.getGroupsViewMap();
			if (!Const.SUCCESS_CODE.equals(MapUtils.getInteger(groupsViewMap, Const.RET_CODE))) {
				groupsViewMap = robotService.buildGroupsList(processDataSession);
			}
			model.addAttribute("autoReply", autoReply);
			model.addAttribute("friendsViewMap", friendsViewMap);
			model.addAttribute("discussesViewMap", discussesViewMap);
			model.addAttribute("groupsViewMap", groupsViewMap);
			return "settings";
		} if (null != processDataSession && CacheMap.hasGetCode(processDataSession)) {
			// 防止刷新重复获取二维码
			model.addAttribute("imageCode", processDataSession.getImageCode());
			// TODO 如果已经扫描授权，则不显示二维码，显示登录中（需要定义一个登录中状态）
			return "login";
		} else {
			ProcessData processData = new ProcessData();
			session.setAttribute(Const.PROCESS_DATA, processData);
			String imageCode = robotService.getCode(processData);
			model.addAttribute("imageCode", imageCode);
			// TODO 二维码获取失败，页面要提示
			return "login";
		}
	}
	
	private RobotResult<Map<String, Object>> successResult() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put(Const.INFO, RobotCodeEnums.REQUEST_SUCCESS.getCodeInfo());
		return new RobotResult<Map<String, Object>>(true, RobotCodeEnums.REQUEST_SUCCESS.getCode(), responseMap);
	}
	
	private RobotResult<Map<String, Object>> exceptionResult() {
		return new RobotResult<Map<String, Object>>(true, RobotCodeEnums.REQUEST_FAIL.getCode(), RobotCodeEnums.REQUEST_FAIL.getCodeInfo());
	}
	
	@RequestMapping(value="/submitList", method = RequestMethod.POST)
	@ResponseBody
	public RobotResult<Map<String, Object>> submitList(@RequestBody Map<String, Object> paramMap, HttpSession session) {
		ProcessData processDataSession = (ProcessData) session.getAttribute(Const.PROCESS_DATA);
		if (null == processDataSession) {
			return new RobotResult<Map<String, Object>>(true, RobotCodeEnums.SESSION_EXPIRED.getCode(), RobotCodeEnums.SESSION_EXPIRED.getCodeInfo());
		}
		try {
			robotService.updateReplyNameList(paramMap, processDataSession);
			return successResult();
		} catch (RobotException e) {
			return exceptionResult();
		}
		
	}
	@RequestMapping(value="/setAutoReply", method = RequestMethod.POST)
	@ResponseBody
	public RobotResult<Map<String, Object>> setAutoReply(@RequestBody Map<String, Object> paramMap, HttpSession session) {
		ProcessData processDataSession = (ProcessData) session.getAttribute(Const.PROCESS_DATA);
		if (null == processDataSession) {
			return new RobotResult<Map<String, Object>>(true, RobotCodeEnums.SESSION_EXPIRED.getCode(), RobotCodeEnums.SESSION_EXPIRED.getCodeInfo());
		}
		try {
			robotService.updateIsAutoReply(paramMap, processDataSession);
			return successResult();
		} catch (RobotException e) {
			return exceptionResult();
		}
	}

	@RequestMapping(value="/setReplyAll", method = RequestMethod.POST)
	@ResponseBody
	public RobotResult<Map<String, Object>> setReplyAll(@RequestBody Map<String, Object> paramMap, HttpSession session) {
		ProcessData processDataSession = (ProcessData) session.getAttribute(Const.PROCESS_DATA);
		if (null == processDataSession) {
			return new RobotResult<Map<String, Object>>(true, RobotCodeEnums.SESSION_EXPIRED.getCode(), RobotCodeEnums.SESSION_EXPIRED.getCodeInfo());
		}
		try {
			robotService.updateIsSpecial(paramMap, processDataSession);
			return successResult();
		} catch (RobotException e) {
			return exceptionResult();
		}
	}
	
	@RequestMapping(value="/robotQuit", method = RequestMethod.GET)
	@ResponseBody
	public RobotResult<Map<String, Object>> quit(HttpSession session) {
		ProcessData processDataSession = (ProcessData) session.getAttribute(Const.PROCESS_DATA);
		try {
			robotService.quit(processDataSession);
			processDataSession = null;
			return successResult();
		} catch (RobotException e) {
			return exceptionResult();
		}
	}
}
