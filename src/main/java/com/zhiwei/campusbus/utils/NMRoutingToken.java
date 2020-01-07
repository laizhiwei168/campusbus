package com.zhiwei.campusbus.utils;

public class NMRoutingToken 
{
	private static ThreadLocal<NMRoutingToken> token = new ThreadLocal<NMRoutingToken>();
	private String dataSourceName;
	
	public String getDataSourceName()
	{
		return dataSourceName;
	}
	
	public NMRoutingToken(String dataSourceName)
	{
		super();
		this.dataSourceName = dataSourceName;
		bindToken(this);
	}
	
	//绑定令牌对象到当前线程
	private static void bindToken(NMRoutingToken t)
	{
		token.set(t);
	}
	
	//解除与当前线程绑定的令牌
	public static void unbindToken()
	{
		token.remove();
	}
	
	//获取与当前线程绑定的令牌
	public static NMRoutingToken getCurrentToken()
	{
		return token.get();
	}
}
