package net.datafans.common.http.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SuccessResponse extends BaseResponse {

    public static final SuccessResponse OK = new SuccessResponse();

    private Object data;

    public SuccessResponse() {
        setStatus(ResponseStatus.STATUS_SUCCESS);
    }

    public SuccessResponse(Map<String, Object> data) {
        setStatus(ResponseStatus.STATUS_SUCCESS);
        setData(data);

    }

    public static SuccessResponse create(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return new SuccessResponse(map);
    }

    public static SuccessResponse create(Map<String, Object> map) {
        return new SuccessResponse(map);
    }

    public static SuccessResponse create(Object data) {
        SuccessResponse response = new SuccessResponse();
        response.setData(data);
        return response;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
