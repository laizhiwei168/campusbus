package com.zhiwei.campusbus.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class NMRoutingDataSource extends AbstractRoutingDataSource
{

	@Override
	protected Object determineCurrentLookupKey() 
	{
		NMRoutingToken token = NMRoutingToken.getCurrentToken();
		if(token != null)
		{
			String dataSourceName = token.getDataSourceName();
			//解除令牌
			NMRoutingToken.unbindToken();
			return dataSourceName;
		}
		
		return null;
	}

}
