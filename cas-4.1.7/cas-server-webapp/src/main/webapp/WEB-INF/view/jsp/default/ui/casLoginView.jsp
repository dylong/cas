<jsp:directive.include file="includes/top.jsp" />
<body id="cas" class="loginbg">
	<div class="loginBoxWindow" >
		<div class="title"> 
			<a href="javascript:;" class="close fr"
				onclick="publicLoginBox.hide()">◊</a>
			<h3><spring:message code="screen.welcome.immotor" />ÅÅ</h3>
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
						<p><img src="images/warning.png" valign="top">
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
			<a href="/cas/casRegisterView.jsp?service=${service}" class="regA"><spring:message code="screen.welcome.button.registered" /></a>
			<a href="/cas/casForgetView.jsp?service=${service}" class="regA fr"><spring:message code="screen.welcome.button.ForgotPassword" />Å</a>
		</form:form>
	</div>

<jsp:directive.include file="includes/bottom.jsp" />