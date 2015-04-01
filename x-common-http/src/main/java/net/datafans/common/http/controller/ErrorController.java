package net.datafans.common.http.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.datafans.common.http.constant.CommonAttribute;
import net.datafans.common.http.response.ErrorResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class ErrorController {

	@RequestMapping(value = "/404", method = { RequestMethod.POST, RequestMethod.GET })
	public ErrorResponse return404(HttpServletResponse response, HttpServletRequest request) {
		response.setStatus(HttpStatus.OK_200);

		String msg = "url not found";
		request.setAttribute(CommonAttribute.RESPONSE_STATUS_CODE, 404);
		request.setAttribute(CommonAttribute.RESPONSE_STATUS_MSG, msg);
		return new ErrorResponse(404, msg);
	}

}
