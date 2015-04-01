package net.datafans.common.http.handler;

import net.datafans.common.http.entity.AccessLog;

public interface AccessLogHandler {
	void handle(AccessLog log);
}
