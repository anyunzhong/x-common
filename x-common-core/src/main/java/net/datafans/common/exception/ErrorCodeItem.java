package net.datafans.common.exception;

/**
 * Created by zhonganyun on 15/11/14.
 */
public class ErrorCodeItem {
    private int code;
    private String msg;

    public ErrorCodeItem(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
