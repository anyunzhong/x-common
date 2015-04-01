package net.datafans.common.util;

import java.util.ArrayList;
import java.util.List;

import net.datafans.common.shutdown.ShutdownListener;

public class ShutdownUtil {

	private List<ShutdownListener> listeners = new ArrayList<ShutdownListener>();

	private static ShutdownUtil util = new ShutdownUtil();

	public static ShutdownUtil sharedInstance() {
		return util;
	}

	public ShutdownUtil() {
		addShutdownHook();
	}

	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (ShutdownListener listener : listeners) {
					listener.shutdown();
				}

				LogUtil.info(ShutdownUtil.class, "APP_SHUTDOWN_SUCCESSFULLY!");
			}
		});
	}

	public void register(ShutdownListener listener) {
		if (listeners.contains(listener)) {
			return;
		}
		listeners.add(listener);
	}
}
