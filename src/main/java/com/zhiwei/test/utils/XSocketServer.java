package com.zhiwei.test.utils;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

public class XSocketServer extends Thread {  
    
  private static final int PORT=60000;  
    
  public void run() {  
      IServer srv = null;  
      try {  
          //建立handler  
          srv = new Server(PORT, new ProjectHandle());  
      } catch (UnknownHostException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();
      } catch (IOException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      }  
      //服务器运行  
      srv.run();  
      System.out.println("The ProjectServer start on port: "+PORT);
  }  
    
  public static void main(String[] args) {  
	  XSocketServer projectServer = new XSocketServer();  
      projectServer.start();  
  }  
}  
