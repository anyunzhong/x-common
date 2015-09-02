package net.datafans.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	public static String get(String path, String key) {
		Properties ps = new Properties();
		try {
			InputStream input = Class.class.getResourceAsStream("/" + path);
			ps.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ps.getProperty(key);
	}
}
