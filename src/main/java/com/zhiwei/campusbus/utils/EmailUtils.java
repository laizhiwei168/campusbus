package com.zhiwei.campusbus.utils;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

public class EmailUtils {
	 public static void Email(String fromAccount ,String pwd,String toAccount,String subject,String content )throws MessagingException, GeneralSecurityException {
		 String status=fromAccount.split("@")[1];
			String smtp="";
			switch (status) {
			case "163.com":
				smtp="smtp.163.com";
				break;
			case "qq.com":
				smtp="smtp.qq.com";
				break;	    
		    }
		 Properties props = new Properties();

	        // 开启debug调试
	        props.setProperty("mail.debug", "true");
	        // 发送服务器需要身份验证
	        props.setProperty("mail.smtp.auth", "true");
	        // 设置邮件服务器主机名
	        props.setProperty("mail.host", smtp);
	        // 发送邮件协议名称
	        props.setProperty("mail.transport.protocol", "smtp");

	        MailSSLSocketFactory sf = new MailSSLSocketFactory();
	        sf.setTrustAllHosts(true);
	        props.put("mail.smtp.ssl.enable", "true");
	        props.put("mail.smtp.ssl.socketFactory", sf);

	        Session session = Session.getInstance(props);

	        Message msg = new MimeMessage(session);
	        msg.setSubject(subject);//"智能集装箱定位信息推送通知"
	       /* StringBuilder builder = new StringBuilder();
	        builder.append("url = " + "http://blog.csdn.net/never_cxb/article/details/50524571");
	        builder.append("\n页面爬虫错误");
	        builder.append("\n时间 " + new Date());*/
	        msg.setText(content);
	        msg.setFrom(new InternetAddress(fromAccount));

	        Transport transport = session.getTransport();
	        transport.connect(smtp, fromAccount, pwd);

	        transport.sendMessage(msg, new Address[] { new InternetAddress(toAccount) });
	        transport.close();
	 }
}
