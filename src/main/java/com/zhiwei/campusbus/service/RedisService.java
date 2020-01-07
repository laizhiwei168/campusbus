package com.zhiwei.campusbus.service;

public interface RedisService {

	//private <T> T execute(Function<ShardedJedis, T> fun);
	public String set(final String key, final String value) ;
	public String set(final String key, final String value,
			final Integer seconds) ;
	public String get(final String key) ;
	public Long del(final String key) ;
	public Long expire(final String key, final int seconds);
}
