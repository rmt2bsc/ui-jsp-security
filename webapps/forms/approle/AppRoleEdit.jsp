<%@ taglib uri="/rmt2-generaltaglib" prefix="gen" %>
<%@ taglib uri="/rmt2-beantaglib" prefix="beanlib" %>
<%@ taglib uri="/rmt2-taglib" prefix="db" %>
<%@ page import="com.api.constants.GeneralConst" %>
<%@ page import="com.entity.VwAppRoles" %>
<%@ page import="com.api.security.authentication.web.AuthenticationConst" %>
<%@ page import="com.api.constants.RMT2ServletConst" %>

<gen:InitAppRoot id="APP_ROOT"/>

  <%
      VwAppRoles ar = request.getAttribute(GeneralConst.CLIENT_DATA_RECORD) == null ? new VwAppRoles() : (VwAppRoles) request.getAttribute(GeneralConst.CLIENT_DATA_RECORD);
      String roleCriteria = "application_id = " + ar.getApplicationId();
  	  String pageTitle = "Application Role Edit";    
	%>
	
<html>
  <head>
    <title><%=pageTitle %></title>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">  
    <link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2Table.css">
	<link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2General.css">
	<script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2General.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2Menu.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/AjaxApi.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/AjaxRequestConfig.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/AjaxRenderer.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/xpath.js"></script>
  </head>
  					  
  <body bgcolor="#FFFFFF" text="#000000">
       <form name="DataForm" method="POST" action="<%=APP_ROOT%>/unsecureRequestProcessor/AppRole.Edit">
		  <h3><strong><%=pageTitle%></strong></h3>
  		<br>
	  
		<table width="50%" border="0" cellspacing="2" cellpadding="0">
				<tr> 
					 <td width="25%" class="clsTableFormHeader">
						   <font size="3"><b>Id</b></font>
					 </td>
					 <td align="left" width="75%" >
					     <beanlib:InputControl type="hidden" name="AppRoleId" value="#record.AppRoleId"/>                                  
					 	   <beanlib:InputControl value="#record.AppRoleId"/>                                  
					 </td>
				</tr>
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Name</b></font>
					 </td>
					 <td align="left">
						<beanlib:InputControl type="text" name="Name" value="#record.AppRoleName" size="35" maxLength="30"/>                                  
					 </td>
				</tr>				
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Code</b></font>
					 </td>
					 <td align="left">
						<beanlib:InputControl type="text" name="Code" value="#record.AppRoleCode" size="15" maxLength="12"/>                                  
					 </td>
				</tr>								
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Application</b></font>
					 </td>
					 <td align="left">
					    <beanlib:InputControl value="#record.AppName"/>
					    <beanlib:InputControl type="hidden" name="AppName" value="#record.AppName"/>
					    <beanlib:InputControl type="hidden" name="AppId" value="#selectedApp.AppId"/>
					 </td>
				</tr>								
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Role</b></font>
					 </td>
					 <td align="left">
 					    <beanlib:InputControl value="#record.RoleName"/>
 					    <beanlib:InputControl type="hidden" name="RoleName" value="#record.RoleName"/>
					    <beanlib:InputControl type="hidden" name="RoleId" value="#selectedRole.RoleId"/>
					 </td>
				</tr>												
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Description</b></font>
					 </td>
					 <td align="left">
						<beanlib:InputControl type="textarea" 
						                      name="Description" 
						                      value="#record.AppRoleDescription" 
						                      cols="50"
						                      rows="3" 
						                      maxLength="150"/>                                  
					 </td>
				</tr>
				
		</table>
		
		 <!-- Display any messgaes -->
		 <table>
			 <tr>
	  		   <td>
			     <font color="red">
				     <gen:ShowPageMessages dataSource="<%=RMT2ServletConst.REQUEST_MSG_MESSAGES%>"/>
			     </font>
		 	   </td>
			 </tr>
		 </table>
		 
        <!-- Display command links -->
        <table width="50%" cellpadding="0" cellspacing="0">
			<tr>
				<td width="10%">
				    <a id="<%=GeneralConst.REQ_SAVE%>" href="javascript:handleAction('_self', document.DataForm, '<%=GeneralConst.REQ_SAVE%>')">
				       <img name="m_add" src="<%=APP_ROOT%>/images/cm-approles-save.gif"  width="48px" height="70px" style="border: none" alt="Save Resource Type">
				    </a>
				</td>
				<td width="10%">
				    <a id="<%=GeneralConst.REQ_DELETE%>" href="javascript:handleAction('_self', document.DataForm, '<%=GeneralConst.REQ_DELETE%>')">
				       <img name="m_delete" src="<%=APP_ROOT%>/images/cm-approles-delete.gif"  width="48px" height="70px" style="border: none" alt="Delete Resource Type">
				    </a>
				</td>								
	      <td width="10%" valign="top" align="center"> 
          <a  href="javascript:handleAction('_self', document.DataForm, '<%=GeneralConst.REQ_LIST%>')"> 
            <img name="m_back" src="<%=APP_ROOT%>/images/cm-back.gif" width="48px" height="70px" style="border: none" alt="Back to Application Role List">
          </a>
      </td>	        				
			<td width="80%">&nbsp;</td>				
			</tr>
        </table>
	    <input name="clientAction" type="hidden">
   </form>
   <db:Dispose/>
 </body>
</html>
