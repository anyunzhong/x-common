package net.datafans.common.util;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

	private final static ConcurrentHashMap<Class<?>, Logger> map = new ConcurrentHashMap<Class<?>, Logger>();

	public static void info(Class<?> clazz, String str) {

		Logger logger = getLogger(clazz);
		logger.info(str);
	}

	public static void error(Class<?> clazz, String str) {

		Logger logger = getLogger(clazz);
		logger.error(str);
	}

	public static void debug(Class<?> clazz, String str) {
		Logger logger = getLogger(clazz);
		logger.debug(str);
	}

	public static void warn(Class<?> clazz, String str) {

		Logger logger = getLogger(clazz);
		logger.warn(str);
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
