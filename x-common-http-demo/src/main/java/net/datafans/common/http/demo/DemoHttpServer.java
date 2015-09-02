package net.datafans.common.http.demo;

import net.datafans.common.http.server.HttpServer;
import net.datafans.common.http.server.impl.AbstractHttpServer;

/**
 * Created by zhonganyun on 15/9/2.
 */
public class DemoHttpServer extends AbstractHttpServer {

    public static void main(String[] args) {
        HttpServer server = new DemoHttpServer();
        server.start();
    }


    @Override
    protected int getPort() {
        return 8088;
    }

    @Override
    protected boolean loadRemoteConfig() {
        return false;
    }

    @Override
    protected String[] getConfigFileNames() {
        return new String[0];
    }
}
