package net.datafans.common.http.demo.controller;

import net.datafans.common.http.controller.BaseController;
import net.datafans.common.http.response.SuccessResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello/")
public class DemoController extends BaseController {


	// 请求地址: http://localhost:8088/hello/?api_version=1
	// 或者：http://localhost:8088/hello/
	@RequestMapping(value = "1")
	public SuccessResponse hello() throws Exception {
		SuccessResponse response = new SuccessResponse();
		response.setData("hello world");
		return response;

	}


	// 请求地址: http://localhost:8088/hello/?api_version=2
	@RequestMapping(value = "2")
	public SuccessResponse hello2() throws Exception {
		SuccessResponse response = new SuccessResponse();
		response.setData("hello world api 2");
		return response;

	}
}
