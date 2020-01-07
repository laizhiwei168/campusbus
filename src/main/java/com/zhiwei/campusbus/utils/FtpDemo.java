package com.zhiwei.campusbus.utils;
import java.io.ByteArrayInputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.SocketException;  
  


import org.apache.commons.io.IOUtils;  
import org.apache.commons.net.ftp.FTPClient;  
import org.apache.commons.net.ftp.FTPReply;  
import org.springframework.web.multipart.commons.CommonsMultipartFile;
  
/** 
 * @ClassName FtpDemo 
 * @Description TODO 
 * @author ydc 
 * @date 下午10:03:36 2013-3-18 
 * @version V1.0 
 */  
public class FtpDemo {  
	
	public static void main(String[] args) {
		FtpDemo ftp=new FtpDemo();
		ftp.upload();
		//ftp.download();
	}
	
    public static String FTP_USERNAME = "ftpuser";  
  
    public static String FTP_PASSWORD = "ftpuserpassword";  
  
    public static String FTP_IP = "111.230.149.207";  
    
    public static Integer port = 21 ;
  
    public static String UPLOAD_FILE_NAME = "6.jpg";  
  
    public static String UPLOAD_FILE_PATH = "D:/" + UPLOAD_FILE_NAME;  
  
    public static String DOWNLOAD_FILE_NAME = "abc.jpg";  
  
    public static String DOWNLOAD_FILE_PATH = "D:/" + DOWNLOAD_FILE_NAME;  
    
    public static String FILE_PATH="/home/ftpusr/sanshui.jpg";
    
    
    /**
     * 
     * @param srcFile
     */
    public static String uploadtofile(CommonsMultipartFile srcFile){
    	 FTPClient ftpClient = new FTPClient();  
    	 InputStream fis = null;  
    	  
    	    try {
    	    	ftpClient.setControlEncoding("utf-8");
    	        ftpClient.connect(FTP_IP);  
    	        ftpClient.login(FTP_USERNAME, FTP_PASSWORD);  
    	  
    	        int reply = ftpClient.getReplyCode();  
    	        System.out.println(reply);  
    	        if (!FTPReply.isPositiveCompletion(reply)) {  
    	        ftpClient.disconnect();  
    	        return "";  
    	        }  
    	  
    	        //File srcFile = new File(UPLOAD_FILE_PATH);  
    	        fis = srcFile.getInputStream();  
    	        // 设置上传目录  
    	        ftpClient.changeWorkingDirectory("/opt/job/ftp");  
    	        ftpClient.setBufferSize(1024);  
    	        ftpClient.setControlEncoding("GBK");
    	        // 设置文件类型（二进制）  
    	        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
    	        ftpClient.storeFile(srcFile.getOriginalFilename(), fis);  
    	        ftpClient.logout();  
    	    } catch (IOException e) {  
    	        e.printStackTrace();      	        
    	        throw new RuntimeException("FTP客户端出错！", e);  
    	    } finally {  
    	        IOUtils.closeQuietly(fis);  
    	        try {  
    	        ftpClient.disconnect();  
    	        } catch (IOException e) {  
    	        e.printStackTrace();  
    	        throw new RuntimeException("关闭FTP连接发生异常！", e);  
    	        }
    	    } 
    	    
    	    return srcFile.getOriginalFilename();
    }
    
  
    /** 
     * FTP上传单个文件测试 
     */  
    public void upload() {  
    FTPClient ftpClient = new FTPClient();  
    FileInputStream fis = null;  
  
    try {  ftpClient.setControlEncoding("utf-8");
    ftpClient.connect(FTP_IP,port);
        ftpClient.login(FTP_USERNAME, FTP_PASSWORD);  
  
        int reply = ftpClient.getReplyCode();  
        System.out.println(reply);  
        if (!FTPReply.isPositiveCompletion(reply)) {  
        ftpClient.disconnect();  
        return;  
        }  
  
        File srcFile = new File(UPLOAD_FILE_PATH);  
        fis = new FileInputStream(srcFile);  
        // 设置上传目录  
        ftpClient.changeWorkingDirectory("/data/ftp/pub");  
        ftpClient.setBufferSize(1024);  
        ftpClient.setControlEncoding("GBK");  
        // 设置文件类型（二进制）  
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
        ftpClient.storeFile(UPLOAD_FILE_NAME, fis);  
        ftpClient.logout();  
    } catch (IOException e) {  
        e.printStackTrace();  
        throw new RuntimeException("FTP客户端出错！", e);  
    } finally {  
        IOUtils.closeQuietly(fis);  
        try {  
        ftpClient.disconnect();  
        } catch (IOException e) {  
        e.printStackTrace();  
        throw new RuntimeException("关闭FTP连接发生异常！", e);  
        }  
    }  
    }  
  
    /** 
     * FTP下载单个文件测试 
     */  
    public void download() {  
    FTPClient ftpClient = new FTPClient();  
    FileOutputStream fos = null;  
  
    try {  
        ftpClient.connect(FTP_IP);  
        ftpClient.login(FTP_USERNAME, FTP_PASSWORD);  
  
        fos = new FileOutputStream(DOWNLOAD_FILE_PATH);  
  
        ftpClient.setBufferSize(1024);  
        // 设置文件类型（二进制）  
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
        ftpClient.retrieveFile(FILE_PATH, fos);  
    } catch (IOException e) {  
        e.printStackTrace();  
        throw new RuntimeException("FTP客户端出错！", e);  
    } finally {  
        IOUtils.closeQuietly(fos);  
        try {  
        ftpClient.disconnect();  
        } catch (IOException e) {  
        e.printStackTrace();  
        throw new RuntimeException("关闭FTP连接发生异常！", e);  
        }  
    }  
    }  
  
    /** 
     * 向FTP服务器上的codeList.xml中写一个字符串 
     *  
     */  
    public void write() {  
    try {  
        FTPClient ftpClient = getFtpClient();  
        InputStream in = new ByteArrayInputStream(  
            "append str+汉字追加".getBytes("UTF-8"));  
        boolean result = ftpClient.storeFile("codeList.xml", in);  
        System.out.println("向codeList.xml中写入字符串成功？" + result);  
        in.close();  
        ftpClient.logout();  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
    }  
  
    private FTPClient getFtpClient() {  
    FTPClient ftpClient = new FTPClient();  
    try {  
        ftpClient.connect(FTP_IP);  
        ftpClient.login(FTP_USERNAME, FTP_PASSWORD);  
        int reply = ftpClient.getReplyCode();  
        System.out.println(reply);  
        if (!FTPReply.isPositiveCompletion(reply)) {  
        ftpClient.disconnect();  
        return null;  
        }  
        ftpClient.setBufferSize(1024);  
        ftpClient.setControlEncoding("UTF-8");  
    } catch (SocketException e) {  
        e.printStackTrace();  
    } catch (IOException e) {  
        e.printStackTrace();  
    }  
  
    return ftpClient;  
    }  
  
}  