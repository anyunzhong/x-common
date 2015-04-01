package net.datafans.common.cache.redis;

import net.datafans.common.util.LogUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDataSource {

	private JedisPool pool;

	private String password;

	public Jedis getRedisClient() {
		try {
			Jedis jedis = pool.getResource();
			if (password != null && !password.equals("")) {
				jedis.auth(password);
			}
			return jedis;
		} catch (Exception e) {
			LogUtil.error(this.getClass(), "ERROR_GET_JEDIS_CLIENT" + e);
		}
		return null;
	}

	public void returnResource(Jedis jedis) {
		pool.returnResource(jedis);
	}

	public void returnResource(Jedis jedis, boolean broken) {
		if (broken) {
			pool.returnBrokenResource(jedis);
		} else {
			pool.returnResource(jedis);
		}
	}

	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}