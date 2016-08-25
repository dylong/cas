<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>cas单点登录系统集成示例</title>
</head>
<body>
<script type="text/javascript">
	function logout(){
// 		var path = ${pageContext.request.scheme}+"://"+window.location.href+"/"+${pageContext.request.contextPath}+"/index.jsp";
// 		alert(window.location.href);
		window.location.href = "https://localhost:8443/cas/logout?service=https://localhost:8443/CASDemo/index.jsp";
	}
</script>
<%
	AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
	Map attributes = principal.getAttributes();
	String name = (String)attributes.get("email");
%>
<p style="color: red;">
	单点登录成功，当前登录的用户是：<%=name %>.
</p>

<p>
<!-- <a href="javascript:logout();">统一注销</a> -->
 <a href="https://localhost:8443/cas/logout?service=http://localhost:8084/CASDemo/success.jsp">退出</a>
</p>
</body>
</html>