package com.zhiwei.campusbus.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;

public class DefaultExceptionHandler implements HandlerExceptionResolver
{
 private static Logger logger = LogManager.getLogger(DefaultExceptionHandler.class.getName());

 @Override
public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) 
 {
  ModelAndView mv = new ModelAndView();
  
  FastJsonJsonView view = new FastJsonJsonView();
  Map<String, Object> object = new HashMap<String, Object>();
  String[] errorCodeMsg = ex.getMessage().split(",");
  object.put("status", new MyStatus("500", errorCodeMsg[0], errorCodeMsg[1]));
  object.put("content", "");
  
  view.setAttributesMap(object);
  mv.setView(view);
  //mv.addObject(object);
  logger.debug("异常: " + ex.getMessage(), ex);
  
  return mv;
 }
}
