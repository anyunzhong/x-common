package net.datafans.common.http.handler;

public interface TokenHandler {
	int getUserId(String token, String platform, String path);
}
