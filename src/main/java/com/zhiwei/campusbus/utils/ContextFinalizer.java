package com.zhiwei.campusbus.utils;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

@WebListener
public class ContextFinalizer implements ServletContextListener 
{
	private static Logger logger = LogManager.getLogger(ContextFinalizer.class.getName());

    @Override
	public void contextInitialized(ServletContextEvent sce) 
    {
    }

    @Override
	public void contextDestroyed(ServletContextEvent sce) 
    {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while (drivers.hasMoreElements()) 
        {
            try {
                d = drivers.nextElement();
                DriverManager.deregisterDriver(d);
                logger.info(String.format("ContextFinalizer:Driver %s deregistered", d));
            } catch (SQLException ex) {
            	logger.error(String.format("ContextFinalizer:Error deregistering driver %s", d) + ":" + ex);
            }
        }
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
        	logger.error("ContextFinalizer:SEVERE problem cleaning up: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
