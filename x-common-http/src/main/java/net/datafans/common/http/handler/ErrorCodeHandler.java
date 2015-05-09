package net.datafans.common.http.handler;

import net.datafans.common.http.response.ErrorResponse;

public interface ErrorCodeHandler {

	ErrorResponse onAuthFail();

	ErrorResponse onOverload();
	
	ErrorResponse onError(int errorCode);
}
