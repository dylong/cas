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
	<h1>CASDemo退出成功<%=name %></h1>
</body>
</html>