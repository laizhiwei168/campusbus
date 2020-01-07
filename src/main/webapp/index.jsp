<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=path%>/static/lib/layui/css/layui.css"/>

</head>
<script type="text/javascript">
function login (){
    window.location.href='html/index.html';
}
</script>

<body style="background-color: #d7eee8" >

<div style="margin:160px auto; width:500px; height:300px; border:1px solid #009688">
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
  <legend>智能集装箱管理平台</legend>
</fieldset>
<%-- <form class="layui-form layui-form-pane" action="<%=path%>/LoginController/Login" method="post"> --%>

<div class="layui-form-item">
    <label class="layui-form-label">用户</label>
    <div class="layui-input-inline">
      <input type="text" name="user" lay-verify="required" placeholder="请输入" autocomplete="off" class="layui-input">
    </div>
  </div>

<div class="layui-form-item">
    <label class="layui-form-label">密码</label>
    <div class="layui-input-inline">
      <input type="password" name="password" placeholder="请输入密码" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux">请务必填写用户名</div>
  </div>
  
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit="" lay-filter="demo1">立即登录</button>
      <button type="reset" class="layui-btn layui-btn-primary" onclick="login()">危险按钮</button>
    </div>
    
  </div>
<!-- </form> -->

</div>

</body>
</html>