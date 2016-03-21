package net.datafans.common.http.demo.handler;

import net.datafans.common.http.handler.TokenHandler;
import org.springframework.stereotype.Component;

@Component
public class DefaultTokenHandler implements TokenHandler {

	@Override
	public int getUserId(String token, String platform, String path) {
		return 10086;
	}

}
