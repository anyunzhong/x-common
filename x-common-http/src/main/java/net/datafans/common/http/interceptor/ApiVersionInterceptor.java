package net.datafans.common.http.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.datafans.common.http.constant.CommonParameter;
import net.datafans.common.http.exception.VersionPathNotFoundException;
import net.datafans.common.http.manager.VersionManager;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ApiVersionInterceptor implements HandlerInterceptor {

	@Autowired
	private VersionManager manager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		Object parsed = request.getAttribute("api_parsed");
		if (parsed == null || Boolean.parseBoolean(parsed.toString()) == false) {
			Integer apiVersion = NumberUtils.toInt(request.getParameter(CommonParameter.API_VERSION));
			String realpath = null;
			try {
				realpath = manager.getRealPath(request.getRequestURI(), apiVersion);
			} catch (VersionPathNotFoundException e) {
				return true;
			}
			request.setAttribute("api_parsed", true);
			request.getServletContext().getRequestDispatcher(realpath).forward(request, response);
		}
		request.setAttribute("api_parsed", false);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
