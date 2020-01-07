package com.zhiwei.test.utils;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

import org.xsocket.DataConverter;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zhiwei.test.codec.IProtocolAdapter;
import com.zhiwei.test.codec.ProtocolAdapterImpl;

public class ProjectHandle implements IDataHandler, IConnectHandler,  
        IDisconnectHandler {  
    /* 处理连接建立事件 */  
    @Override  
    public boolean onConnect(INonBlockingConnection nbc) throws IOException,  
            BufferUnderflowException, MaxReadSizeExceededException {  
        // TODO Auto-generated method stub  
        System.out.println(nbc.getId() + "is connect!");  
        return true;  
    }  
  
    /* 处理连接断开事件 */  
    @Override  
    public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {  
        // TODO Auto-generated method stub  
        System.out.println(nbc.getId() + "is disconnect!");  
        return true;  
    }  
  
    /* 处理接受数据事件 */  
    @Override  
    public boolean onData(INonBlockingConnection nbc) throws IOException,  
            BufferUnderflowException, ClosedChannelException,  
            MaxReadSizeExceededException { 
       /* String str = nbc.readStringByDelimiter("\r\n", "gbk");  
        System.out.println("data:"+str);  */
        //nbc.write(str);
    	
    	ByteBuffer copyBuffer = ByteBuffer.allocate(20000);  
    	nbc.read(copyBuffer);  
    	copyBuffer.flip();  
    	 byte[] bt = DataConverter.toBytes(copyBuffer);  
        
    	//System.out.println("data:"+str1);
    	//byte[] b= hexToBytes(str1);
    	/*for(byte bt1:bt){
    		System.out.println(Integer.toString(bt1));
    	}
    	System.out.println("LENGTH:"+bt.length);*/
    	IProtocolAdapter protocolAdapter =new ProtocolAdapterImpl();
    	ObjectNode objectNode;
		try {
			objectNode = protocolAdapter.decode(bt);
			String str = objectNode.toString();
	        System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return true;  
    }  
    
    /**
     * 字符串转换为16进制数组
     * @param hexString
     * @return
     */
    public static byte[] hexToBytes(String hexString) {   
    	  if (hexString == null || hexString.equals("")) {   
    	   return null;   
    	  }   
    	  
    	  int length = hexString.length() / 2;   
    	  char[] hexChars = hexString.toCharArray();   
    	  byte[] bytes = new byte[length];   
    	  String hexDigits = "0123456789abcdef";  
    	  for (int i = 0; i < length; i++) {   
    	   int pos = i * 2; // 两个字符对应一个byte  
    	   int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1  
    	   int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2  
    	   if(h == -1 || l == -1) { // 非16进制字符  
    	    return null;  
    	   }  
    	   bytes[i] = (byte) (h | l);   
    	  }   
    	  return bytes;   
    	 }  
}  