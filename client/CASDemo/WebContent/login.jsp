<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<%
	AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
	Map attributes = principal.getAttributes();
	String name = (String)attributes.get("name");
%>

<body>
	第二次请求
	<p style="color: red;">
	website单点登录成功，当前登录的用户是：<%=name %>.
</p>
</body>
</html>