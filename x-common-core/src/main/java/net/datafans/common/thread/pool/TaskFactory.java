package net.datafans.common.thread.pool;

public interface TaskFactory {
	Runnable getTask();
}
