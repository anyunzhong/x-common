package net.datafans.common.http.aspect;

import javax.servlet.http.HttpServletRequest;

import net.datafans.common.http.annotation.Overload;
import net.datafans.common.http.manager.OverloadManager;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OverloadAspect {

	@Autowired
	private OverloadManager manager;
	
	@Before("@annotation(overload)")
	public void check(JoinPoint jp, Overload overload) {

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

		String path = request.getRequestURI();
		manager.check(path);
		
	}
}
