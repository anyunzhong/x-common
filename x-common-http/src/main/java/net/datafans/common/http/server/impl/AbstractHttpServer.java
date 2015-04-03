package net.datafans.common.http.server.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.datafans.common.http.server.HttpServer;
import net.datafans.common.shutdown.ShutdownListener;
import net.datafans.common.util.LogUtil;
import net.datafans.common.util.PropertiesUtil;
import net.datafans.common.util.ShutdownUtil;
import net.datafans.common.util.UncaughtExceptionUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public abstract class AbstractHttpServer implements HttpServer {

	private Server server;

	private void init() {
		UncaughtExceptionUtil.sharedInstance().declare();

		ShutdownUtil.sharedInstance().register(shutdownListener);

		server = new Server(getPort());

		WebAppContext context = new WebAppContext();

		String classPath = Class.class.getResource("/").getPath();
		context.setContextPath("/");
		context.setResourceBase(classPath);
		context.setDescriptor(classPath + "web.xml");
		server.setHandler(context);

	}

	@Override
	public void start() {

		loadConfig();

		init();

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
					LogUtil.info(AbstractHttpServer.class, "SERVER_SHUTDOWN_OK");
				} catch (Exception e) {
					LogUtil.error(AbstractHttpServer.class, "SERVER_SHUTDOWN_ERROR " + e);
				}
			}
		}
	};

	private void loadConfig() {
		String config = "server.config";
		String serverId = PropertiesUtil.get(config, "server.id");
		String[] configFiles = getConfigFileNames();
		String configUrl = PropertiesUtil.get(config, "config.url");

		try {
			if (configFiles != null) {
				for (String string : configFiles) {
					String targetPath = Class.class.getResource("/").getPath() + "/" + string + ".properties";
					FileUtils.copyURLToFile(new URL(configUrl + serverId + "/" + string + ".properties"), new File(
							targetPath));
				}
			}
		} catch (IOException e) {
			LogUtil.error(AbstractHttpServer.class, "ERROR_LOAD_CONFIG_FILES " + e);
		}
	}

	protected abstract String[] getConfigFileNames();

	protected int getPort() {
		return Config.getPort();
	}

	public static class Config {
		private final static String path = "server.properties";

		public static int getPort() {
			return NumberUtils.toInt(PropertiesUtil.get(path, "port"));
		}
	}

}
