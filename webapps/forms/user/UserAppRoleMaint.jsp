<%@ taglib uri="/rmt2-generaltaglib" prefix="gen" %>
<%@ page import="com.api.security.authentication.web.AuthenticationConst" %>
<%@ page import="com.api.util.RMT2Utility" %>


<gen:InitAppRoot id="APP_ROOT"/>

<html>
  <head>
    <title>User Application-Role Maintenance</title>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">  
    <link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2Table.css">
	  <link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2General.css">
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2General.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2Menu.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/dropdown.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/AjaxApi.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/AjaxRequestConfig.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/AjaxXmlRenderer.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/xpath.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2DateValidation.js"></script>
    <script>
        function doSoapCall() {
        	 var xmlhttp = new XMLHttpRequest();
             xmlhttp.open('POST', 'http://localhost:8080/server-external-api/services/soap', true);
             
        	 var userName = document.DataForm.Username.value;
             var appId = getSelectedRadio(document.DataForm.ApplicationId);
             var payload = '<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/>';
               payload += '<SOAP-ENV:Body>';
               payload += '<AuthenticationRequest><header><routing>JMS: rmt2.queue.authentication</routing><application>authentication</application><module>admin</module>';
               payload += '<transaction>GET_USER_PERMISSIONS</transaction><delivery_mode/><message_mode>REQUEST</message_mode>'; 
          	   payload += '<delivery_date>'  + getCurrentDateTime() + '</delivery_date></header><criteria>';
          	   payload += '<user_app_roles_criteria>';
       		   payload += '<user_name>' + userName + '</user_name>';
       		   payload += '<app_id>' + appId + '</app_id>';
       		   payload += '</user_app_roles_criteria>';
       		   payload += '</criteria></AuthenticationRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>';
         		   
       		   xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    if (xmlhttp.status == 200) {
                        changeRolesCallback(xmlhttp.responseText);
                    }
                }
            }
            // Send the POST request
            xmlhttp.setRequestHeader('Content-Type', 'text/xml');
            xmlhttp.send(payload);
        }
        
       
	   function changeRolesCallback(xmlData) {
	        // Get XML based on entire document
	        var renderer = new AjaxXmlRenderer(null, xmlData);
	   		renderer.buildSelectOptions(document.DataForm.AssignedRoleId, "/SOAP-ENV:Envelope/SOAP-ENV:Body/AuthenticationResponse/profile/user_info/granted_app_roles/user_app_role/app_role_info/app_role_code", "/SOAP-ENV:Envelope/SOAP-ENV:Body/AuthenticationResponse/profile/user_info/granted_app_roles/user_app_role/app_role_info/app_role_name");
	   		renderer.buildSelectOptions(document.DataForm.RevokedRoleId,  "/SOAP-ENV:Envelope/SOAP-ENV:Body/AuthenticationResponse/profile/user_info/revoked_app_roles/user_app_role/app_role_info/app_role_code", "/SOAP-ENV:Envelope/SOAP-ENV:Body/AuthenticationResponse/profile/user_info/revoked_app_roles/user_app_role/app_role_info/app_role_name");
	   } 
	   
    </script>   
  </head>

   <%
      String pageTitle = "User Application-Role Maintenance";
   %>
	
  <body bgcolor="#FFFFFF" text="#000000">
	 <form name="DataForm" method="POST" action="<%=APP_ROOT%>/unsecureRequestProcessor/UserAppRole.Edit">
	    <h3><strong><%=pageTitle%></strong></h3>
	    <%@include file="UserHeader.jsp"%>  
	    <br>
		<table width="40%" border="0">
		   <caption align="left"><strong>Applications</strong></caption>
			<tr> 
				<td align="right" bgcolor="#FFCC00"> 
   				   <beanlib:InputControl dataSource="apps"
										 type="radiolookup"
										 name="ApplicationId"
										 codeProperty="AppId"
										 displayProperty="Name"
										 selectedValue="#selectedApp"
										 cols="3"
										 border="yes"
										 onClick="doSoapCall()"/>
				</td>
			</tr>    
		</table>
		<br>
		<table width="60%" border="0">
			<tr> 
				<td align="left" width="40%" >
				   <strong>Revoked Roles</strong>
			  </td>
				<td width="20%">&nbsp;</td>
				<td align="left" width="40%">
			     <strong>Assigned Roles</strong>
			  </td>
			</tr>
			<tr> 
				<td> 
					 <beanlib:InputControl dataSource="revokedRoles"
											type="select"
											name="RevokedRoleId"
											codeProperty="AppRoleId"
											displayProperty="Name"
											multiSelect="Yes"
											size="25"
											style="width:400"/>
				</td>
				<td width="20%" align="center" valign="middle">
				   <table width="50%" align="center" border="0">
				      <tr>
					     <td align="center">
                  <input type="button" name="assigned_cb" value="Assign =&gt;"  style="width:90px" onclick="moveSelected(DataForm.RevokedRoleId, DataForm.AssignedRoleId, true)">
						   </td>
					  </tr>
					  <tr><td>&nbsp;</td></tr>
					  <tr>
					     <td align="center">
						    <input type="button" name="revoke_cb" value="&lt;= Revoke" style="width:90px" onclick="moveSelected(DataForm.AssignedRoleId, DataForm.RevokedRoleId, true)">
						 </td>
					  </tr>
				   </table>  
				</td>
				<td> 
					 <beanlib:InputControl dataSource="assignedRoles"
											type="select"
											name="AssignedRoleId"
											codeProperty="AppRoleId"
											displayProperty="Name"
											multiSelect="Yes"
											size="25"
											style="width:400"/>				
				</td>
			</tr>			
		</table>
		<br>
	  
	   <!-- Display command buttons -->
	  <table width="100%" cellpadding="0" cellspacing="0">
		  <tr>
			<td colspan="2">
			    <input name="<%=GeneralConst.REQ_SAVE%>" type="button" value="Save" style="width:90" onClick="selectAllOpts(document.DataForm.AssignedRoleId); handleAction('_self', document.DataForm, this.name)">
			    <input name="<%=GeneralConst.REQ_BACK%>" type="button" value="Back" style="width:90" onClick="handleAction('_self', document.DataForm, this.name)">
			</td>
		 </tr>		
	  </table>
	 <input name="clientAction" type="hidden">
   </form>
   <db:Dispose/>
 </body>
</html>
