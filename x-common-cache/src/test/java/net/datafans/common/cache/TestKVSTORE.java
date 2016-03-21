package net.datafans.common.cache;

import net.datafans.common.cache.redis.RedisCache;
import net.datafans.common.util.LogUtil;
import redis.clients.jedis.Jedis;

/**
 * Created by zhonganyun on 16/1/28.
 */
public class TestKVSTORE {

    public static void main(String[] args) {
        RedisCache cache = new RedisCache(100, 20, 100, "d121", 4308, "");
        LogUtil.error(TestKVSTORE.class, "NAME:"+cache.get("foo"));

//        Jedis jedis = new Jedis("cd1",4308);
//        jedis.auth("aac96f57");
//        jedis.set("foo", "bar1111");
//        String value = jedis.get("foo");
//        LogUtil.error(TestKVSTORE.class , value);
    }
}
