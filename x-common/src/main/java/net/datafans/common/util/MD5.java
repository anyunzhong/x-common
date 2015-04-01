package net.datafans.common.util;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {
	public static String encode(String str) {
		return DigestUtils.md5Hex(StringUtils.getBytesUtf8(str));
	}
}
