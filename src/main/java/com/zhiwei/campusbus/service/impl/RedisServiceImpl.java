package com.zhiwei.campusbus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.zhiwei.campusbus.service.Function;
import com.zhiwei.campusbus.service.RedisService;

@Component("redisService")
public class RedisServiceImpl implements RedisService{

	@Autowired(required = false)
	private ShardedJedisPool shardedJedisPool;

	// 封装通用逻辑
	private <T> T execute(Function<ShardedJedis, T> fun) {
		ShardedJedis shardedJedis = null;
		try {
			// 从连接池中获取到jedis分片对象
			shardedJedis = shardedJedisPool.getResource();
			return fun.callBack(shardedJedis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != shardedJedis) {
				// 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
				shardedJedis.close();
			}
		}
		return null;
	}

	/**
	 * 执行set方法
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public String set(final String key, final String value) {
		return this.execute(new Function<ShardedJedis, String>() {
			@Override
			public String callBack(ShardedJedis shardedJedis) {
				return shardedJedis.set(key, value);
			}
		});
	}

	/**
	 * 执行set方法，并设置生存时间
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            时间，单位是秒
	 * @return
	 */
	@Override
	public String set(final String key, final String value,
			final Integer seconds) {
		return this.execute(new Function<ShardedJedis, String>() {
			@Override
			public String callBack(ShardedJedis shardedJedis) {
				String str = shardedJedis.set(key, value);
				shardedJedis.expire(key, seconds);
				return str;
			}
		});
	}

	/**
	 * 执行get方法
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public String get(final String key) {
		return this.execute(new Function<ShardedJedis, String>() {
			@Override
			public String callBack(ShardedJedis shardedJedis) {
				return shardedJedis.get(key);
			}
		});
	}

	/**
	 * 删除key
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Long del(final String key) {
		return this.execute(new Function<ShardedJedis, Long>() {
			@Override
			public Long callBack(ShardedJedis shardedJedis) {
				return shardedJedis.del(key);
			}
		});
	}

	/**
	 * 设置生存时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	@Override
	public Long expire(final String key, final int seconds) {
		return this.execute(new Function<ShardedJedis, Long>() {
			@Override
			public Long callBack(ShardedJedis shardedJedis) {
				return shardedJedis.expire(key, seconds);
			}
		});
	}
	
}
