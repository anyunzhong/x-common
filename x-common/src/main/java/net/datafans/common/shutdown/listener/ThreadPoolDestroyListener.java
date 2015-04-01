package net.datafans.common.shutdown.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import net.datafans.common.shutdown.ShutdownListener;
import net.datafans.common.util.LogUtil;

public class ThreadPoolDestroyListener implements ShutdownListener {

	private List<ExecutorService> poolList = new ArrayList<ExecutorService>();

	private static ThreadPoolDestroyListener listener = new ThreadPoolDestroyListener();

	public static ThreadPoolDestroyListener sharedInstance() {
		return listener;
	}

	public synchronized void addPool(ExecutorService pool) {
		if (poolList.contains(pool)) {
			return;
		}
		poolList.add(pool);
	}

	@Override
	public void shutdown() {
		for (ExecutorService pool : poolList) {
			shutdownGracefully(pool);
		}

	}

	private void shutdownGracefully(ExecutorService pool) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (pool != null)
			pool.shutdown();
		LogUtil.info(this.getClass(), "THREAD_POOL_DESTROYED! " + pool);
	}

}
