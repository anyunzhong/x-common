package net.datafans.common.http.demo.handler;

import net.datafans.common.http.handler.TokenHandler;
import org.springframework.stereotype.Component;

@Component
public class DefaultTokenHandler implements TokenHandler {

	@Override
	public int getUserId(String token) {
		return 10086;
	}

}
