<!DOCTYPE html>
<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="zh_CN">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<title>CAS Service login</title>

<spring:theme code="standard.custom.css.file" var="customCssFile" />
<spring:theme code="standard.custom.css.login" var="customCssLogin" />
<%-- <link rel="stylesheet" href="<c:url value="${customCssFile}" />" /> --%>
<!-- <link href="/cas/css/cas.css" rel="stylesheet"> -->
<link href="<c:url value="${customCssLogin}" />" rel="stylesheet">
</head>
<body id="cas" class="loginbg">

	<div class="content">
		<h2><spring:message code="screen.welcome.immotor.registered" /><spring:message code="screen.welcome.immotor" /></h2>
		<table class="regConTable" border="0" cellpadding="0" cellspacing="0">
					<tbody><tr>
				<td width="80"><font max=“11”>手机号</font></td>
				<td>
					<input id="regName" type="text" onchange="checkPhone()">
					<p class="warning c999">请输入您的手机号</p>
				</td>
			</tr>
			<tr>
				<td><font>校证码</font></td>
				<td>
					<input class="inpXym" id="inpXym" type="text" maxlength="4">
					<a class="msgXym" href="javascript:;" id="getYzm">免费获取效验码</a>
					<p class="warning c999">请输入短信中6位数字效验码</p>
				</td>
			</tr>
			<tr>
				<td><font>密码</font></td>
				<td>
					<input class="" id="regPaw" type="password" onkeyup="REG.checkPassword()">
					<p class="warning c999">6-12个字符（区分大小写）</p>
				</td>
			</tr>
			<tr>
				<td><font>确认密码</font></td>
				<td>
					<input class="" id="regArginPaw" type="password" onkeyup="REG.checkPassword2()">
					<p class="warning c999">请再次输入密码</p>
				</td>
			</tr>
			<tr>
				<td style="height:60px;"></td>
				<td style="height:60px;">
					<input type="hidden" id="invit" value="">
					<a href="javascript:;" class="regSub" onclick="REG.postReg()">马上注册</a>
				</td>
			</tr>
			<tr>
				<td style="height:50px;"></td>
				<td style="height:50px;">
					<input class="isSer" type="checkbox" checked="">
					<span class="serFont">我已经阅读并同意</span>
					<a class="userSer" href="/?c=member&amp;a=reg_xy" target="_blank">"用户服务条款"</a>
				</td>
			</tr>
		</tbody></table>
<%--
	<div class="loginBoxWindow" >
		<div class="title"> 
			<a href="javascript:;" class="close fr" onclick="publicLoginBox.hide()">×</a>
			<h3><spring:message code="screen.welcome.immotor.registered" /><spring:message code="screen.welcome.immotor" /></h3>
		</div>
		<form:form method="post" id="fm1" commandName="${commandName}" cssClass="loginBoxMain" htmlEscape="true">

			<form:errors path="*" id="msg" cssClass="errors" element="div" htmlEscape="false" cssStyle="margin:-15px 0  10px 0; " />
			<section class="iBox1">
				<c:choose>
					<c:when test="${not empty sessionScope.openIdLocalId}">
						<strong><c:out value="${sessionScope.openIdLocalId}" /></strong>
						<input type="hidden" id="username" name="username" value="<c:out value="${sessionScope.openIdLocalId}" />" />
					</c:when>
					<c:otherwise>
						<spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
						<form:input cssClass="required" cssErrorClass="error" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" 
							path="username" placeholder="username" autocomplete="off" htmlEscape="true" />
					</c:otherwise>
				</c:choose>
			</section>
			<section class="iBox2">
				<spring:message code="screen.welcome.label.password.accesskey"
					var="passwordAccessKey" />
				<form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password"
					accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" placeholder="password" />
				<span id="capslock-on" style="display: none;">
						<p>img src="images/warning.png" valign="top">
						<spring:message code="screen.capslock.on" />
					</p>
				</span>
			</section>
			<section class="row btn-row">
				<input type="hidden" name="lt" value="${loginTicket}" /> 
				<input type="hidden" name="execution" value="${flowExecutionKey}" /> 
				<input type="hidden" name="_eventId" value="submit" /> 
				<input class="loginA loginBoxMain"  name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />"
					tabindex="6" type="submit" /> 
			</section>
			<a href="register.html" class="regA"><spring:message code="screen.welcome.button.registered" /></a>
			<a href="forget.html" class="regA fr"><spring:message code="screen.welcome.button.ForgotPassword" /></a>
		</form:form>
	</div> --%>

    <script type="text/javascript" src="js/jquery-1.12.3.min.js"></script>
	<script src="js/coopReg.js"></script>
  </script>
</div>
</body>