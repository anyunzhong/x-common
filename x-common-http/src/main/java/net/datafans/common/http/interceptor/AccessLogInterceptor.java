package net.datafans.common.http.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.datafans.common.http.constant.CommonAttribute;
import net.datafans.common.http.constant.CommonParameter;
import net.datafans.common.http.entity.AccessLog;
import net.datafans.common.http.handler.AccessLogHandler;
import net.datafans.common.http.handler.ServerConfigHandler;
import net.datafans.common.http.util.UrlMatcher;
import net.datafans.common.util.LogUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.HashMap;
import java.util.Map;

public class AccessLogInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private AccessLogHandler logHandler;


    @Autowired(required = false)
    private ServerConfigHandler serverConfigHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (UrlMatcher.versionParsed(request)) {
            return true;
        }
        request.setAttribute(CommonAttribute.REQUEST_START_TIME, System.currentTimeMillis());
        LogUtil.info(getClass(), "Path: " + request.getRequestURI());

        Map map = new HashMap();
        map.putAll(request.getParameterMap());
        map.remove("token");
        String prettyString = JSON.toJSONString(map, SerializerFeature.PrettyFormat);
        LogUtil.info(getClass(), "Params: " + prettyString);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        if (!UrlMatcher.versionParsed(request)) {
            return;
        }

        Long startTime = NumberUtils.toLong(request.getAttribute(CommonAttribute.REQUEST_START_TIME).toString());
        Long endTime = System.currentTimeMillis();
        int timeCost = (int) (endTime - startTime);
        LogUtil.info(this.getClass(), "REQUEST_TIME: " + timeCost);

        AccessLog log = new AccessLog();
        log.setLogId(1);
        log.setAccessTime(System.currentTimeMillis());
        log.setClientHost(AccessLogInterceptor.getRemoteAddrIp(request));

        Object userId = request.getAttribute(CommonAttribute.USER_ID);
        if (userId != null) {
            log.setClientUniqueId(NumberUtils.toInt(userId.toString()));
        }

        log.setParams(JSON.toJSONString(request.getParameterMap()));
        log.setPath(request.getRequestURI());
        log.setApiVersion(NumberUtils.toInt(request.getParameter(CommonParameter.API_VERSION)));
        log.setPlatform(NumberUtils.toInt(request.getParameter(CommonParameter.PLATFORM)));
        if (serverConfigHandler != null)
            log.setServerId(serverConfigHandler.getServerId());
        else
            log.setServerId("");
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


    public static String getRemoteAddrIp(HttpServletRequest request) {
        String ipFromNginx = getHeader(request, "X-Real-IP");
        return StringUtil.isBlank(ipFromNginx) ? request.getRemoteAddr() : ipFromNginx;
    }

    private static String getHeader(HttpServletRequest request, String headName) {
        String value = request.getHeader(headName);
        return !StringUtils.isBlank(value) && !"unknown".equalsIgnoreCase(value) ? value : "";
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
