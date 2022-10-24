<%@ taglib uri="/rmt2-taglib" prefix="db" %>
<%@ taglib uri="/rmt2-beantaglib" prefix="beanlib" %>
<%@page import="com.entity.UserLogin"%>
<%@ page import="com.api.constants.GeneralConst" %>
<%@ page import="com.action.user.UserConst" %>

<%
  UserLogin user = request.getAttribute("user") == null ? new UserLogin() : (UserLogin) request.getAttribute("user");
%>
					 
<table width="50%" border="0">
	 <caption align="left"><strong>User</strong></caption>
	 <tr>
		 <th width="15%" align="right">
		   <font size="2">
				<b>Login ID:</b>
			</font>
		 </th>
		 <td width="35%">
			<beanlib:InputControl type="hidden" name="Username" value="#user.Username"/>				
			<beanlib:InputControl type="hidden" name="LoginId" value="#user.LoginId"/>
			<beanlib:InputControl value="#user.Username"/>        
		 </td>
		 <th width="15%" align="right">
			<font size="2">
				<b>Group:</b>
			 </font>
		 </th>
		 <td width="35%"> 
		       <beanlib:InputControl value="#user.GrpName"/>
		 </td>				 				 
	 </tr>
	 <tr>
		 <th align="right">
			 <font size="2">
				<b>First Name:</b>
			 </font>
		 </th>
		 <td>
			 <beanlib:InputControl value="#user.Firstname"/>  
		 </td>
		 <th align="right">
			 <font size="2">
				<b>Last Name:</b>
			 </font>
		 </th>
		 <td>
			 <beanlib:InputControl value="#user.Lastname"/>  
		 </td>
	 </tr>
</table>
