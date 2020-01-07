package com.zhiwei.campusbus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
	
	//datatime为20170706T140132Z
	 public static String StringToDate(String datatime,int addHour){
		 StringBuffer strbf=new StringBuffer();
		  String str =datatime.replace("T", ""); 
		  strbf.append( str.subSequence(0,4));
			 for(int i=0;i<5;i++){
				 if(i<3){
					 if(i==2){
						 strbf.append(" ");
					 }else{
						 strbf.append("-");
					 }
			 }else{
				 strbf.append(":");
			 }
				 strbf.append(str.subSequence(4+2*i, 6+2*i));
				 
			 } 
			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        long time_long;
		        long hour_long=3600000;
		        String time="";
				try {
					time_long = format.parse(strbf.toString()).getTime();
			        Date date1 = new Date(time_long+hour_long*addHour);
			        time=format.format(date1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
		        
		 return time;		 
	 }
	 
	 public static String captureName(String str) {
		 str = str.substring(0, 1).toUpperCase() + str.substring(1);
	       return  str;
	      
	    }
	 
	 public static boolean strIsNullOrEmpty(String s) {
	        return (null == s || s.trim().length() < 1);
	    }

}
