package net.datafans.common.cache.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

public class RedisCache {

	private static final Logger log = LoggerFactory.getLogger(RedisCache.class);

	private RedisDataSource redisDataSource;

	public void disconnect() {
		Jedis jedis = redisDataSource.getRedisClient();
		jedis.disconnect();
	}

	public RedisCache(int maxActive, int maxIdle, int maxWait, String host, int port, String password) {

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(maxActive);
		config.setMaxIdle(maxIdle);
		config.setMaxWait(maxWait);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);

		JedisPool pool = new JedisPool(config, host, port);

		redisDataSource = new RedisDataSource();
		redisDataSource.setPool(pool);
		redisDataSource.setPassword(password);
	}

	/**
	 * 设置单个值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {
		String result = null;

		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.set(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 获取单个值
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}

		boolean broken = false;
		try {
			result = jedis.get(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Boolean exists(String key) {
		Boolean result = false;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.exists(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String type(String key) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.type(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 在某段时间后实现
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(String key, int seconds) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.expire(key, seconds);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 在某个时间点失效
	 * 
	 * @param key
	 * @param unixTime
	 * @return
	 */
	public Long expireAt(String key, long unixTime) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.expireAt(key, unixTime);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long ttl(String key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.ttl(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public boolean setbit(String key, long offset, boolean value) {

		Jedis jedis = redisDataSource.getRedisClient();
		boolean result = false;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.setbit(key, offset, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public boolean getbit(String key, long offset) {
		Jedis jedis = redisDataSource.getRedisClient();
		boolean result = false;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;

		try {
			result = jedis.getbit(key, offset);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public long setrange(String key, long offset, String value) {
		Jedis jedis = redisDataSource.getRedisClient();
		long result = 0;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.setrange(key, offset, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String getrange(String key, long startOffset, long endOffset) {
		Jedis jedis = redisDataSource.getRedisClient();
		String result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.getrange(key, startOffset, endOffset);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String getSet(String key, String value) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.getSet(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long setnx(String key, String value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.setnx(key, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String setex(String key, int seconds, String value) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.setex(key, seconds, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}


	/**
	 * 模糊匹配
	 *
	 * @param patten
	 * @return
	 */
	public Set<String> keys(String patten) {
		Set<String> result = null;

		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.keys(patten);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long decrBy(String key, long integer) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.decrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long decr(String key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.decr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long incrBy(String key, long integer) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.incrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long incr(String key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.incr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long append(String key, String value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.append(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String substr(String key, int start, int end) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.substr(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hset(String key, String field, String value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hset(key, field, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String hget(String key, String field) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hget(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hsetnx(String key, String field, String value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hsetnx(key, field, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String hmset(String key, Map<String, String> hash) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hmset(key, hash);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public List<String> hmget(String key, String... fields) {
		List<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hmget(key, fields);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hincrBy(String key, String field, long value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hincrBy(key, field, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Boolean hexists(String key, String field) {
		Boolean result = false;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hexists(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long del(String key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.del(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hdel(String key, String field) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hdel(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hlen(String key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hlen(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<String> hkeys(String key) {
		Set<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hkeys(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public List<String> hvals(String key) {
		List<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hvals(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Map<String, String> hgetAll(String key) {
		Map<String, String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.hgetAll(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	// ================list ====== l表示 list或 left, r表示right====================
	public Long rpush(String key, String string) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.rpush(key, string);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long lpush(String key, String string) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lpush(key, string);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long llen(String key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.llen(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public List<String> lrange(String key, long start, long end) {
		List<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lrange(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String ltrim(String key, long start, long end) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.ltrim(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String lindex(String key, long index) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lindex(key, index);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String lset(String key, long index, String value) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lset(key, index, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long lrem(String key, long count, String value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lrem(key, count, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String lpop(String key) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.lpop(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String rpop(String key) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.rpop(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	// return 1 add a not exist value ,
	// return 0 add a exist value
	public Long sadd(String key, String member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sadd(key, member);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<String> smembers(String key) {
		Set<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.smembers(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long srem(String key, String member) {
		Jedis jedis = redisDataSource.getRedisClient();

		Long result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.srem(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String spop(String key) {
		Jedis jedis = redisDataSource.getRedisClient();
		String result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.spop(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long scard(String key) {
		Jedis jedis = redisDataSource.getRedisClient();
		Long result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.scard(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Boolean sismember(String key, String member) {
		Jedis jedis = redisDataSource.getRedisClient();
		Boolean result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.sismember(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String srandmember(String key) {
		Jedis jedis = redisDataSource.getRedisClient();
		String result = null;
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.srandmember(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zadd(String key, double score, String member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zadd(key, score, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<String> zrange(String key, int start, int end) {
		Set<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrange(key, start, end);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zrem(String key, String member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {
			result = jedis.zrem(key, member);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Double zincrby(String key, double score, String member) {
		Double result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zincrby(key, score, member);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zrank(String key, String member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrank(key, member);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zrevrank(String key, String member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrank(key, member);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<String> zrevrange(String key, int start, int end) {
		Set<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrange(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeWithScores(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeWithScores(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zcard(String key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zcard(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Double zscore(String key, String member) {
		Double result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zscore(key, member);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public List<String> sort(String key) {
		List<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sort(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public List<String> sort(String key, SortingParams sortingParameters) {
		List<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sort(key, sortingParameters);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zcount(String key, double min, double max) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zcount(key, min, max);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<String> zrangeByScore(String key, double min, double max) {
		Set<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeByScore(key, min, max);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Set<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeByScore(key, max, min);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		Set<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeByScore(key, min, max, offset, count);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		Set<String> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeByScore(key, max, min, offset, count);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeByScoreWithScores(key, min, max);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeByScoreWithScores(key, max, min);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeByScoreWithScores(key, min, max, offset, count);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zremrangeByRank(String key, int start, int end) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zremrangeByRank(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zremrangeByScore(String key, double start, double end) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zremrangeByScore(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.linsert(key, where, pivot, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String set(byte[] key, byte[] value) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.set(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public byte[] get(byte[] key) {
		byte[] result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.get(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Boolean exists(byte[] key) {
		Boolean result = false;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.exists(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String type(byte[] key) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.type(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long expire(byte[] key, int seconds) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.expire(key, seconds);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long expireAt(byte[] key, long unixTime) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.expireAt(key, unixTime);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long ttl(byte[] key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.ttl(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public byte[] getSet(byte[] key, byte[] value) {
		byte[] result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.getSet(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long setnx(byte[] key, byte[] value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.setnx(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String setex(byte[] key, int seconds, byte[] value) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.setex(key, seconds, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long decrBy(byte[] key, long integer) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.decrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long decr(byte[] key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.decr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long incrBy(byte[] key, long integer) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.incrBy(key, integer);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long incr(byte[] key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.incr(key);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long append(byte[] key, byte[] value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.append(key, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public byte[] substr(byte[] key, int start, int end) {
		byte[] result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.substr(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hset(byte[] key, byte[] field, byte[] value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hset(key, field, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public byte[] hget(byte[] key, byte[] field) {
		byte[] result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hget(key, field);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hsetnx(key, field, value);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hmset(key, hash);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		List<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hmget(key, fields);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hincrBy(byte[] key, byte[] field, long value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hincrBy(key, field, value);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Boolean hexists(byte[] key, byte[] field) {
		Boolean result = false;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hexists(key, field);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hdel(byte[] key, byte[] field) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hdel(key, field);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long hlen(byte[] key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hlen(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<byte[]> hkeys(byte[] key) {
		Set<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hkeys(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Collection<byte[]> hvals(byte[] key) {
		Collection<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hvals(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Map<byte[], byte[]> hgetAll(byte[] key) {
		Map<byte[], byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.hgetAll(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long rpush(byte[] key, byte[] string) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.rpush(key, string);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long lpush(byte[] key, byte[] string) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.lpush(key, string);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long llen(byte[] key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.llen(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public List<byte[]> lrange(byte[] key, int start, int end) {
		List<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.lrange(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String ltrim(byte[] key, int start, int end) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.ltrim(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public byte[] lindex(byte[] key, int index) {
		byte[] result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.lindex(key, index);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public String lset(byte[] key, int index, byte[] value) {
		String result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.lset(key, index, value);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long lrem(byte[] key, int count, byte[] value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.lrem(key, count, value);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public byte[] lpop(byte[] key) {
		byte[] result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.lpop(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public byte[] rpop(byte[] key) {
		byte[] result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.rpop(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long sadd(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sadd(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<byte[]> smembers(byte[] key) {
		Set<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.smembers(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long srem(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.srem(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public byte[] spop(byte[] key) {
		byte[] result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.spop(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long scard(byte[] key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.scard(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Boolean sismember(byte[] key, byte[] member) {
		Boolean result = false;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sismember(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public byte[] srandmember(byte[] key) {
		byte[] result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.srandmember(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zadd(byte[] key, double score, byte[] member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zadd(key, score, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrange(byte[] key, int start, int end) {
		Set<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrange(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zrem(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrem(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Double zincrby(byte[] key, double score, byte[] member) {
		Double result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zincrby(key, score, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zrank(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrank(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zrevrank(byte[] key, byte[] member) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrank(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrevrange(byte[] key, int start, int end) {
		Set<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrange(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeWithScores(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeWithScores(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zcard(byte[] key) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zcard(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Double zscore(byte[] key, byte[] member) {
		Double result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zscore(key, member);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public List<byte[]> sort(byte[] key) {
		List<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sort(key);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		List<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.sort(key, sortingParameters);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zcount(byte[] key, double min, double max) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zcount(key, min, max);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		Set<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeByScore(key, min, max);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
		Set<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeByScore(key, min, max, offset, count);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeByScoreWithScores(key, min, max);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrangeByScoreWithScores(key, min, max, offset, count);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		Set<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeByScore(key, max, min);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
		Set<byte[]> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeByScore(key, max, min, offset, count);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeByScoreWithScores(key, max, min);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
		Set<Tuple> result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zremrangeByRank(byte[] key, int start, int end) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zremrangeByRank(key, start, end);

		} catch (Exception e) {

			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long zremrangeByScore(byte[] key, double start, double end) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.zremrangeByScore(key, start, end);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
		Long result = null;
		Jedis jedis = redisDataSource.getRedisClient();
		if (jedis == null) {
			return result;
		}
		boolean broken = false;
		try {

			result = jedis.linsert(key, where, pivot, value);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			redisDataSource.returnResource(jedis, broken);
		}
		return result;
	}

}
