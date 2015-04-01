package net.datafans.common.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.datafans.common.shutdown.listener.ThreadPoolDestroyListener;
import net.datafans.common.util.ShutdownUtil;

public class ThreadPool {

	private int threadCount;
	private TaskFactory factory;

	private ExecutorService pool;

	public ThreadPool(int threadCount, TaskFactory factory) {
		this.threadCount = threadCount;
		this.factory = factory;
		pool = Executors.newCachedThreadPool();
		init();
	}

	private void init() {
		ThreadPoolDestroyListener listener = ThreadPoolDestroyListener.sharedInstance();
		listener.addPool(pool);
		ShutdownUtil.sharedInstance().register(listener);

	}

	public void start() {
		for (int i = 0; i < threadCount; i++) {
			pool.execute(factory.getTask());
		}
	}
}
