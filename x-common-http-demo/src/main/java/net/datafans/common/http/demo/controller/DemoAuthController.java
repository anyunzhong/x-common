package net.datafans.common.http.demo.controller;

import net.datafans.common.http.annotation.Auth;
import net.datafans.common.http.controller.BaseController;
import net.datafans.common.http.response.SuccessResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/auth/")
public class DemoAuthController extends BaseController {

    // 请求地址: http://localhost:8088/auth/?api_version=1&token=456788888&user_id=10086&type=1000
    // 如果传入的user_id和通过DefaultTokenHandler返回的user_id不一致 返回验证错误
    @Auth
    @RequestMapping(value = "1")
    public SuccessResponse auth(HttpServletRequest request, @RequestParam("user_id") int userId, @RequestParam("type") int type) throws Exception {
        SuccessResponse response = new SuccessResponse();
        response.setData("user_id: " + userId + "  type: " + type);
        return response;

    }

}
