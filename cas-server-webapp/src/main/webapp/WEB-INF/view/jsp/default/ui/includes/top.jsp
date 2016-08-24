<!DOCTYPE html>

<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">
<head>
  <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
  
    <title>CAS &#8211; Central Authentication Service</title>
  
<spring:theme code="standard.custom.css.login" var="customCssLogin" />
<%-- <link rel="stylesheet" href="<c:url value="${customCssFile}" />" /> --%>
<!-- <link href="/cas/css/cas.css" rel="stylesheet"> -->
<link rel="shortcut icon" href="images/favicon.ico">
<link href="<c:url value="${customCssLogin}" />" rel="stylesheet">
  <!--[if lt IE 9]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.6.1/html5shiv.js" type="text/javascript"></script>
  <![endif]-->
</head>
