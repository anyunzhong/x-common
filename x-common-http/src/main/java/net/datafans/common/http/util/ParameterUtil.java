package net.datafans.common.http.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;

import net.datafans.common.http.constant.CommonAttribute;

public class ParameterUtil {

	public static int getUserId(HttpServletRequest request) {
		return NumberUtils.toInt(request.getAttribute(CommonAttribute.USER_ID).toString());
	}
}
