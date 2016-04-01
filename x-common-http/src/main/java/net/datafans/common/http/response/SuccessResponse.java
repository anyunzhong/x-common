package net.datafans.common.http.response;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.datafans.common.util.LogUtil;

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

        logData(value);

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return new SuccessResponse(map);
    }

    public static SuccessResponse create(Map<String, Object> map) {

        logData(map);

        return new SuccessResponse(map);
    }

    public static SuccessResponse create(Object data) {

        logData(data);

        SuccessResponse response = new SuccessResponse();
        response.setData(data);
        return response;
    }


    private static void logData(Object data) {
        String prettyString = JSON.toJSONString(data, SerializerFeature.PrettyFormat);
        LogUtil.info(SuccessResponse.class, "\n" + prettyString + "\n");
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
