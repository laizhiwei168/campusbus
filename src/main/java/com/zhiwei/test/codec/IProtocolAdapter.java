package com.zhiwei.test.codec;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract interface IProtocolAdapter
 {
  /*public static final String VERSION = "2";
  
	 public default String getVersion()
	  {
	    return "2";
	 }
  
   public abstract String getManufacturerId();
  
   public abstract String getModel();*/
   
   public abstract ObjectNode decode(byte[] paramArrayOfByte)
     throws Exception;
   
   public abstract byte[] encode(ObjectNode paramObjectNode)
     throws Exception;
 }