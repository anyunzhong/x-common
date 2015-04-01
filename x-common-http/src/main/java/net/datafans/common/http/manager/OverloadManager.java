package net.datafans.common.http.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import net.datafans.common.http.exception.OverloadSufferException;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

@Component
public class OverloadManager {

	private ConcurrentHashMap<String, Runtime> map = new ConcurrentHashMap<String, OverloadManager.Runtime>();

	public void register(String path, int threshold, TimeUnit unit) {
		Runtime runtime = new Runtime();
		runtime.setThreshold(threshold);
		runtime.setTimeUnit(unit);
		runtime.setLastTimestamp(System.currentTimeMillis());
		runtime.stock = new AtomicInteger(0);
		map.put(path, runtime);
	}

	public void check(String path) throws OverloadSufferException {

		Runtime runtime = map.get(path);
		if (runtime == null) {
			return;
		}

		runtime.stock.addAndGet(1);

		long current = System.currentTimeMillis();
		long last = runtime.getLastTimestamp();
		long interval = 0;
		switch (runtime.getTimeUnit()) {
		case SECONDS:
			interval = DateUtils.MILLIS_PER_SECOND;
			break;
		case MINUTES:
			interval = DateUtils.MILLIS_PER_MINUTE;
			break;
		case HOURS:
			interval = DateUtils.MILLIS_PER_HOUR;
			break;
		default:
			break;
		}

		if ((current - last) < interval && runtime.getStock().get() > runtime.getThreshold()) {
			throw new OverloadSufferException();
		}
		
		if ((current - last) >= interval) {
			runtime.setLastTimestamp(System.currentTimeMillis());
			runtime.getStock().set(0);
		}

	}

	private static class Runtime {
		private int threshold;
		private AtomicInteger stock;
		private TimeUnit timeUnit;
		private long lastTimestamp;

		public int getThreshold() {
			return threshold;
		}

		public void setThreshold(int threshold) {
			this.threshold = threshold;
		}

		public TimeUnit getTimeUnit() {
			return timeUnit;
		}

		public void setTimeUnit(TimeUnit timeUnit) {
			this.timeUnit = timeUnit;
		}

		public AtomicInteger getStock() {
			return stock;
		}

		public long getLastTimestamp() {
			return lastTimestamp;
		}

		public void setLastTimestamp(long lastTimestamp) {
			this.lastTimestamp = lastTimestamp;
		}

	}
}
