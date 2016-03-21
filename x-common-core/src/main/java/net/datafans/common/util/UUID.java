package net.datafans.common.util;

import org.apache.commons.lang.math.RandomUtils;

public class UUID {

	public static long getMsgID() {
		long ts = System.currentTimeMillis();
		int random = RandomUtils.nextInt(999999 - 100000 + 1) + 100000;
		return Long.parseLong(new StringBuilder(String.valueOf(ts).substring(1)).append(random).toString());
	}


}
