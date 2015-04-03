package net.datafans.common.http.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class UrlMatcher {

	private static Pattern pattern = Pattern.compile("\\d+$");

	public static boolean versionParsed(HttpServletRequest request) {
		Matcher matcher = pattern.matcher(request.getRequestURI());
		return matcher.find();
	}

}
