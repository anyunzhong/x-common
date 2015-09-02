package net.datafans.common.exception;

public class CommonException extends Exception {

	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String errorMsg;

	CommonException(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
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

	@Override
	public String toString() {
		return "CommonException [errorCode=" + errorCode + ", errorMsg=" + errorMsg + "]";
	}
	
	

}
