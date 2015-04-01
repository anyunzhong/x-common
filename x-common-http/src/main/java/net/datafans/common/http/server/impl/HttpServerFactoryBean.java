package net.datafans.common.http.server.impl;

import net.datafans.common.http.server.HttpServer;

import org.springframework.beans.factory.FactoryBean;

public class HttpServerFactoryBean implements FactoryBean<HttpServer> {
	private int port;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public HttpServer getObject() throws Exception {

		return new DefaultHttpServer(port);
	}

	@Override
	public Class<?> getObjectType() {
		return HttpServer.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
