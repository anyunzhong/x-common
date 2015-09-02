package net.datafans.common.http.demo.handler;

import net.datafans.common.http.entity.AccessLog;
import net.datafans.common.http.handler.AccessLogHandler;
import net.datafans.common.util.LogUtil;
import org.springframework.stereotype.Component;

@Component
public class DefaultAccessLogHandler implements AccessLogHandler {

    @Override
    public void handle(AccessLog log) {
        LogUtil.info(this.getClass(), log.toString());
    }

}
