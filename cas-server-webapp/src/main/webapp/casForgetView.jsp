<!DOCTYPE html>
<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html >
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />

<title>CAS Service Forget</title>
<link rel="shortcut icon" href="images/favicon.ico">
<link href="css/public.css" rel="stylesheet">
<style>
.regConTable tr td .isSer {
	float: left;
	width: 15px;
	height: 15px;
	margin: 0 5px 0 0;
	position: relative;
	top: 2px;
}

.serFont {
	float: left;
	height: 20px;
	line-height: 20px;
	color: #999;
}

.userSer {
	float: left;
	height: 20px;
	line-height: 20px;
	color: #E61441;
}
.setfont{
    font-size: 18px;
    margin: 5px;
    margin-top: 10px;
}
</style>
<script type="text/javascript" src="js/jquery.min.js"></script>

</head>
<body id="cas" class="loginbg">

	<div class="content">
		<%-- 	<spring:message code="screen.welcome.immotor.registered" /><spring:message code="screen.welcome.immotor" /> --%>
		<h2>
			<spring:message code="screen.welcome.immotor.forget" />
			<spring:message code="screen.welcome.immotor" />
			
		</h2>
		<div id="msg" class="errors" style="margin:10px "></div>
		<form action="" method="post" id="userForm"   >
			<table class="regConTable" border="0" cellpadding="0" cellspacing="0">
				<tbody>
					<tr>
						<td>
							<div id="emailDiv">
								<input name="email" id = "email" type="text"placeholder="<spring:message code="screen.welcome.input.email" />"/>
								<a href="javascript:;" class="userSer setfont"  target="_blank" id="emailA" ><spring:message code="screen.welcome.label.a.reg.phone" /></a>
							</div>
							<div id="phoneDiv" style="display: none;">
								<select name="area_code" id="area_code" style="float: left;height: 38px;width: 80px;border-radius: 5px;margin-right: 5px;"></select>
								<input name="phone" id="phone" type="text"  style="width: 195px" placeholder="<spring:message code="screen.welcome.input.phone" />" />
								<a href="javascript:;" class="userSer setfont"  target="_blank" id="phoneA" ><spring:message code="screen.welcome.label.a.reg.email" /></a>
							</div>
						</td>
					</tr>
					<tr>
						<td><input class="inpXym" id="passcodeInp" type="text" maxlength="6" placeholder="<spring:message code="screen.welcome.input.passcode" />" /> 
							<a id="getYzm"  class="msgXym"  style="vertical-align:top;float: right;" ><spring:message code="screen.welcome.label.a.passcode" /></a>
						</td>
					</tr>
					<tr>
						<td>
							<input class="" id="regPaw" type="password"  name="password" placeholder="<spring:message code="screen.welcome.input.password" />"/>
						</td>
					</tr>
					<tr>
						<td>
							<input class="" id="regArginPaw" type="password" name="regpassword" placeholder="<spring:message code="screen.welcome.input.regpassword" />"/>
						</td>
					</tr>
					<tr>
						<td style="height: 60px;">
							<input type="hidden" name="scene" value="1"/>
							<input type="hidden" name="username" id="username"/>
							<input type="hidden" name="device_type" value="3"/>
							<input type="submit" value="<spring:message code="screen.welcome.input.sub" />" class="regSub" id="regsub"/></td>
					</tr>
					
				</tbody>
			</table>
		</form>
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

<!-- 		<script type="text/javascript" src="js/jquery-3.1.0.min.js"></script> -->
<!-- 		<script src="js/coopReg.js"></script> -->
<!-- 		<script type="text/javascript"> -->
<!-- // 		function passcode(){ -->
<%-- 			window.location.href="<%=this.getServletContext().getContextPath()%>/passcode";  --%>
<!-- // 			} -->
<!-- 		</script> -->
<!-- 		</script> -->
<!-- 	</div> -->
<!-- </body> -->

<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> --%>

<%-- 	<spring:theme code="cas.javascript.file" var="casJavascriptFile"text="" /> --%>
<!-- 	<script type="text/javascript" src="js/jquery.min.js"></script> -->
<!-- 	<script type="text/javascript" src="/cas/js/cas.js"></script> -->
<!-- 	<script type="text/javascript" src="/cas/js/common.js"></script> -->
<!-- 	<script type="text/javascript" src="/cas/js/coopReg.js"></script> -->
<!-- 	<script type="text/javascript" src="/cas/js/Member.js"></script> -->
	<script type="text/javascript">
	$(function() {
		function getUrlParam(name) {
			//构造一个含有目标参数的正则表达式对象  
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			//匹配目标参数  
			var r = window.location.search.substr(1).match(reg);
			//返回参数值  
			if (r != null)
				return unescape(r[2]);
			return null;
		}
		var service = getUrlParam('service');
		localStorage.clear();
		$("#emailA").click(function(){
		  $("#emailDiv").hide();
		  $("#phoneDiv").show();
		  $("#area_code").empty();
		  $.getJSON("js/Countries.json",function(data){
			  for (var i = data.length - 1; i >= 0; i--) {
					var dial_code = data[i].dial_code.substring(1);
					if(dial_code==86){
						$("#area_code").prepend('<option selected="selected" value="' + dial_code + '">' + data[i].code +'  '+data[i].dial_code+ '</option>');
						break;
					}
					$("#area_code").prepend('<option value="' + dial_code + '">' + data[i].code +'  '+data[i].dial_code+ '</option>');
				};
	       }); 
		});
		$("#phoneA").click(function(){
			$("#phoneDiv").hide();
			$("#emailDiv").show();
			$("#area_code").empty();
		});
		$("#getYzm").click(function(){
			$.ajax({
                cache: true,
                type: "POST",
                url:"passcode",
                data:$('#userForm').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    alert("Connection error");
                },
                success: function(data) {
                	if(data.rtcode>0){
                		$("#msg").html(data.rtdesc);
                	}else{
                		$("#msg").html("");
	                	localStorage.setItem("passcodeSever",data.passcode);
						$("#getYzm").attr("disabled","disabled");
						$("#getYzm").css("background-color","#C0C0C0");
                	}
                }
            });
		});
		
		$("#regsub").click(function(){
			var username="";
			if( $("#area_code option:selected").val()== undefined){
				username = $("#email").val();
				if(username==""){
					$("#msg").html("邮箱为空");
					return false;
				}
				if(!username.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/))
				{
					$("#msg").html("格式不正确！请重新输入");
					$("#email1").focus();
					return false;
				}
			}else{
				username = $("#phone").val();
			}
			var authcodeSever = localStorage.getItem("passcodeSever");
			if($("#passcodeInp").val()!=authcodeSever){
				$("#msg").html("验证码错误");
				return false;
			}
			if(!$("#regPaw").val().match(/^\w{6,}$/)){
				$("#msg").html("密码六位以上");
				return false;
			}
			if($("#regPaw").val()!=$("#regArginPaw").val()){
				$("#msg").html("两次密码不一致，请从新输入");
				return false;
			}
			$.ajax({
                cache: true,
                type: "POST",
                url:"mfyUserPass",
                data:$('#userForm').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    alert("Connection error");
                },
                success: function(data) {
                	if(data.rtcode>0){
                		$("#msg").html(data.rtdesc);
                	}else{
                		$("#username").val(username);
                		var regurl = "registerLogin";
                		if(service!=null){
                			regurl+="?service="+service;
                		}
                		$("#userForm").attr("action", regurl).submit();
                	}
                }
            });
		});
	})
		</script>
</body>
</html>



<%-- <jsp:directive.include file="WEB-INF/view/jsp/default/ui/includes/bottom.jsp" /> --%>
