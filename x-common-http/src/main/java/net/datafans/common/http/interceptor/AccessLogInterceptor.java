package net.datafans.common.http.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.datafans.common.http.constant.CommonAttribute;
import net.datafans.common.http.constant.CommonParameter;
import net.datafans.common.http.entity.AccessLog;
import net.datafans.common.http.handler.AccessLogHandler;
import net.datafans.common.http.handler.TokenHandler;
import net.datafans.common.util.LogUtil;
import net.datafans.common.util.PropertiesUtil;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

public class AccessLogInterceptor implements HandlerInterceptor {

	@Autowired(required = false)
	private AccessLogHandler logHandler;

	@Autowired(required = false)
	private TokenHandler tokenHandler;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		request.setAttribute(CommonAttribute.REQUEST_START_TIME, System.currentTimeMillis());

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Object loged = request.getAttribute("api_loged");
		if (loged == null || Boolean.parseBoolean(loged.toString()) == false) {
			request.setAttribute("api_loged", true);
			return;
		}

		request.setAttribute("api_loged", false);
		Long startTime = NumberUtils.toLong(request.getAttribute(CommonAttribute.REQUEST_START_TIME).toString());
		Long endTime = System.currentTimeMillis();
		int timeCost = (int) (endTime - startTime);
		LogUtil.info(this.getClass(), "REQUEST_TIME: " + timeCost);

		AccessLog log = new AccessLog();
		log.setLogId(1);
		log.setAccessTime(System.currentTimeMillis());
		log.setClientHost(request.getRemoteHost());

		String token = request.getParameter("token");
		if (tokenHandler != null && token != null) {
			log.setClientUniqueId(tokenHandler.getUserId(token));
		}

		log.setParams(JSON.toJSONString(request.getParameterMap()));
		log.setPath(request.getRequestURI());
		log.setApiVersion(NumberUtils.toInt(request.getParameter(CommonParameter.API_VERSION)));
		log.setDeviceType(NumberUtils.toInt(request.getParameter(CommonParameter.DEVICE_TYPE)));
		log.setServerId(PropertiesUtil.get("server.config", "server.id"));
		log.setTimeCost(timeCost);

		Object errorCode = request.getAttribute(CommonAttribute.RESPONSE_STATUS_CODE);
		if (errorCode != null) {
			log.setErrorCode(NumberUtils.toInt(errorCode.toString()));
		}

		Object errorMsg = request.getAttribute(CommonAttribute.RESPONSE_STATUS_MSG);
		if (errorMsg != null) {
			log.setErrorMsg(errorMsg.toString());
		}

		if (logHandler != null) {
			logHandler.handle(log);
		}

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
