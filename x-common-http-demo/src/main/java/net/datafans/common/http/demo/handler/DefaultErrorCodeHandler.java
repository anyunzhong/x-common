package net.datafans.common.http.demo.handler;

import net.datafans.common.http.handler.ErrorCodeHandler;
import net.datafans.common.http.response.ErrorResponse;
import org.springframework.stereotype.Component;

@Component
public class DefaultErrorCodeHandler implements ErrorCodeHandler {

	@Override
	public ErrorResponse onAuthFail() {
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode(20000);
		response.setErrorMsg("验证失败");
		return response;
	}

	@Override
	public ErrorResponse onOverload() {
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode(100000);
		response.setErrorMsg("接口启用过载保护");
		return response;
	}

	@Override
	public ErrorResponse onError(int errorCode) {
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode(errorCode);
		response.setErrorMsg("你的自定义错误消息");
		return response;
	}

}
