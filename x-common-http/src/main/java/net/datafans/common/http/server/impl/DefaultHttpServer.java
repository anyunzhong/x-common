package net.datafans.common.http.server.impl;

import net.datafans.common.http.server.AbstractHttpServer;
import net.datafans.common.shutdown.ShutdownListener;
import net.datafans.common.util.LogUtil;
import net.datafans.common.util.ShutdownUtil;
import net.datafans.common.util.UncaughtExceptionUtil;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class DefaultHttpServer extends AbstractHttpServer {

	private Server server;
	private int port;

	public DefaultHttpServer(int port) {
		this.port = port;
		init();
	}

	private void init() {
		UncaughtExceptionUtil.sharedInstance().declare();

		ShutdownUtil.sharedInstance().register(shutdownListener);

		server = new Server(port);

		WebAppContext context = new WebAppContext();

		String classPath = Class.class.getResource("/").getPath();
		context.setContextPath("/");
		context.setResourceBase(classPath);
		context.setDescriptor(classPath + "web.xml");
		server.setHandler(context);

	}

	@Override
	public void start() {

		try {
			server.start();
			server.join();
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "SERVER_START_ERROR " + e);
		}
	}

	private ShutdownListener shutdownListener = new ShutdownListener() {
		@Override
		public void shutdown() {
			if (server != null) {
				try {
					server.stop();
					LogUtil.info(DefaultHttpServer.class, "SERVER_SHUTDOWN_OK");
				} catch (Exception e) {
					LogUtil.error(DefaultHttpServer.class, "SERVER_SHUTDOWN_ERROR " + e);
				}
			}
		}
	};

}
