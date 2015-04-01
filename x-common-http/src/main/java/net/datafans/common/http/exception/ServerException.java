package net.datafans.common.http.exception;

public class ServerException extends CommonException {

	private static final long serialVersionUID = 1L;

	public ServerException(int code) {
		super(code, "no error msg");
	}

	public ServerException(int code, String msg) {
		super(code, msg);
	}
}
