package net.datafans.common.exception;


public class ClientException extends CommonException {

    private static final long serialVersionUID = 1L;

    public ClientException(int code) {
        super(code, "no error msg");
    }

    public ClientException(int code, String msg) {
        super(code, msg);
    }

    public ClientException(ErrorCodeItem item) {
        super(item.getCode(), item.getMsg());
    }

}
