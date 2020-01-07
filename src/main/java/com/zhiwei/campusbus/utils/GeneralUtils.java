package com.zhiwei.campusbus.utils;

import java.text.SimpleDateFormat;

public class GeneralUtils 
{

	public static String dateTime2String(java.sql.Timestamp dateTime)
	{
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd hh:mm:ss");
		return formatter.format(new java.util.Date(dateTime.getTime()));
	}
	
	public static String date2String(java.sql.Timestamp dateTime)
	{
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd");
		return formatter.format(new java.util.Date(dateTime.getTime()));
	}
}
