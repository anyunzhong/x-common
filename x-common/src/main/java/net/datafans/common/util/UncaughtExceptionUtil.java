package net.datafans.common.util;

import java.lang.Thread.UncaughtExceptionHandler;

public class UncaughtExceptionUtil {

	private static UncaughtExceptionUtil util = new UncaughtExceptionUtil();

	public static UncaughtExceptionUtil sharedInstance() {
		return util;
	}

	public void declare() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LogUtil.error(UncaughtExceptionUtil.class, "THREAD_TERMINATE " + t, e);
				e.printStackTrace();
			}
		});
	}
}
