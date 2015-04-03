package net.datafans.common.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.datafans.common.shutdown.ShutdownListener;
import net.datafans.common.util.LogUtil;
import net.datafans.common.util.PropertiesUtil;
import net.datafans.common.util.ShutdownUtil;
import net.datafans.common.util.UncaughtExceptionUtil;

import org.apache.commons.io.FileUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class ContextServer {

	private String contextPath;

	private boolean isShutdown = false;

	private ClassPathXmlApplicationContext ctx;

	private StartHook hook;

	public ContextServer() {
		contextPath = "applicationContext.xml";
		init();
	}

	public ContextServer(String contextPath) {
		this.contextPath = contextPath;
		init();
	}

	private void init() {
		UncaughtExceptionUtil.sharedInstance().declare();
		ShutdownUtil.sharedInstance().register(shutdownListener);
	}

	public void start() {
		// 加载配置文件
		loadConfig();

		ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext(new String[] { contextPath });
			this.ctx = context;
			context.start();

			if (hook != null) {
				hook.onStart();
			}

			while (true) {

				if (isShutdown) {
					break;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (context != null) {
				context.close();
			}
		}
	}

	public ClassPathXmlApplicationContext getCtx() {
		return ctx;
	}

	public void setCtx(ClassPathXmlApplicationContext ctx) {
		this.ctx = ctx;
	}

	public StartHook getHook() {
		return hook;
	}

	public void setHook(StartHook hook) {
		this.hook = hook;
	}

	private ShutdownListener shutdownListener = new ShutdownListener() {

		@Override
		public void shutdown() {
			isShutdown = true;
		}
	};

	public static interface StartHook {
		void onStart();
	}

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
			LogUtil.error(ContextServer.class, "ERROR_LOAD_CONFIG_FILES " + e);
		}
	}

	protected abstract String[] getConfigFileNames();
}
