package org.cool.qqrobot.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.cool.qqrobot.common.Const;
import org.cool.qqrobot.common.RobotCodeEnums;
import org.cool.qqrobot.dto.RobotResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected RobotResult<Map<String, Object>> successResult() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put(Const.INFO, RobotCodeEnums.REQUEST_SUCCESS.getCodeInfo());
		return new RobotResult<Map<String, Object>>(true, RobotCodeEnums.REQUEST_SUCCESS.getCode(), responseMap);
	}
	
	protected RobotResult<Map<String, Object>> successResult(Map responseMap) {
		return new RobotResult<Map<String, Object>>(true, RobotCodeEnums.REQUEST_SUCCESS.getCode(), responseMap);
	}
	
	protected RobotResult<Map<String, Object>> exceptionResult() {
		return new RobotResult<Map<String, Object>>(true, RobotCodeEnums.REQUEST_FAIL.getCode(), RobotCodeEnums.REQUEST_FAIL.getCodeInfo());
	}
	
	protected void ResponseJson(HttpServletResponse response,RobotResult<Map> rr) {
		response.setContentType(Const.CONTENT_TYPE_JSON);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			Gson gson = new Gson();
			String json = gson.toJson(rr);
			logger.info("ResponseJson:{}",json);
	    	out.print(json);
		} catch (IOException e) {
		}finally{
			if(null!=out)out.close();
		}
	}
}
