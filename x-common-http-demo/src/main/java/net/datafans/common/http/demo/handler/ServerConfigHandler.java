package net.datafans.common.http.demo.handler;

import net.datafans.common.http.handler.TokenHandler;
import org.springframework.stereotype.Component;

@Component
public class ServerConfigHandler implements net.datafans.common.http.handler.ServerConfigHandler {

    @Override
    public String getServerId() {
        return "demo-http-server";
    }
}
