package net.datafans.common.http.aspect;

import javax.servlet.http.HttpServletRequest;

import net.datafans.common.http.annotation.Auth;
import net.datafans.common.http.constant.CommonParameter;
import net.datafans.common.http.exception.AuthFailedException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect {

	@Before("@annotation(auth)")
	public void auth(JoinPoint jp, Auth auth) {

		HttpServletRequest request = null;
		Object[] args = jp.getArgs();

		if (args != null && args.length > 0) {
			for (Object o : args) {
				if (o instanceof HttpServletRequest) {
					request = (HttpServletRequest) o;
					break;
				}
			}
		}

		Object token = request.getParameter(CommonParameter.TOKEN);

		if (token == null) {
			throw new AuthFailedException();
		}
	}
}
