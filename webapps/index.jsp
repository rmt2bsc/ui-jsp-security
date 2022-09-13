<%@ taglib uri="/rmt2-securitytaglib" prefix="auth" %>
<%@ taglib uri="/rmt2-generaltaglib" prefix="gen" %>
<%@ page import="com.api.util.RMT2Utility" %>
<%@ page import="com.api.config.ConfigConstants" %>
<%@ page import="com.api.config.AppPropertyPool" %>
<%@ page import="com.api.security.authentication.web.AuthenticationConst"%>

<gen:InitAppRoot id="APP_ROOT"/>

<%
  // Prevent JSP from caching.
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevent caching at the proxy server
%>

<html>
  <head>
    <title>Security Administration</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
     <%
     out.println("App Code " + request.getServletContext().getServletContextName());
     out.println("User Id" + request.getParameter(AuthenticationConst.AUTH_PROP_USERID));
     %>
     <auth:UserResourceRouter success="/home.jsp" 
                              failure="/login.jsp" 
                              appName="<%=request.getServletContext().getServletContextName()%>"
                              userName="<%=request.getParameter(AuthenticationConst.AUTH_PROP_USERID)%>"/>
  </body>
</html>
