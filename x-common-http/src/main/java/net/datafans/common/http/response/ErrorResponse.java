package net.datafans.common.http.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ErrorResponse extends BaseResponse {
	private int errorCode;
	private String errorMsg;

	public ErrorResponse() {
		setStatus(ResponseStatus.STATUS_ERROR);
	}

	public ErrorResponse(int errorCode, String errorMsg) {
		setStatus(ResponseStatus.STATUS_ERROR);
		setErrorCode(errorCode);
		setErrorMsg(errorMsg);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
