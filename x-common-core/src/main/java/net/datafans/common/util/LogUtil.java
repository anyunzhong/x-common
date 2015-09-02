package net.datafans.common.util;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

	private final static ConcurrentHashMap<Class<?>, Logger> map = new ConcurrentHashMap<Class<?>, Logger>();

	public static void info(Class<?> clazz, String content, Throwable e) {

		Logger logger = getLogger(clazz);
		logger.info(content, e);
	}

	public static void error(Class<?> clazz, String content, Throwable e) {

		Logger logger = getLogger(clazz);
		logger.error(content,e);
	}

	public static void debug(Class<?> clazz, String content, Throwable e) {
		Logger logger = getLogger(clazz);
		logger.debug(content, e);
	}

	public static void warn(Class<?> clazz, String content, Throwable e) {

		Logger logger = getLogger(clazz);
		logger.warn(content, e);
	}
	
	
	
	public static void info(Class<?> clazz, String content) {

		Logger logger = getLogger(clazz);
		logger.info(content);
	}

	public static void error(Class<?> clazz, String content) {

		Logger logger = getLogger(clazz);
		logger.error(content);
	}

	public static void debug(Class<?> clazz, String content) {
		Logger logger = getLogger(clazz);
		logger.debug(content);
	}

	public static void warn(Class<?> clazz, String content) {

		Logger logger = getLogger(clazz);
		logger.warn(content);
	}
	
	

	private static Logger getLogger(Class<?> clazz) {
		if (map.containsKey(clazz)) {
			return map.get(clazz);
		}

		Logger logger = LoggerFactory.getLogger(clazz);
		map.put(clazz, logger);
		return logger;
	}
}
