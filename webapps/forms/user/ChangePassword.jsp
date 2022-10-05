<%@page import="com.action.user.UserConst"%>
<%@page import="com.entity.UserLogin"%>
<%@ taglib uri="/rmt2-beantaglib" prefix="beanlib" %>
<%@ taglib uri="/rmt2-generaltaglib" prefix="gen" %>
<%@ page import="com.api.constants.GeneralConst" %>
<%@ page import="com.api.constants.RMT2ServletConst" %>

<gen:InitAppRoot id="APP_ROOT"/>

<html>
  <title>Change User Password</title>
  <head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">
    <link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2Table.css">
    <link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2General.css">   
    <script type='text/javascript' language='javascript' src="<%=APP_ROOT%>/js/datetimepicker.js"></script>   
  	<script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2General.js"></script>
  	<script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2Menu.js"></script>  
  	<script type="text/javascript">
  	  
				function enableSaveButton() {
					
					var pw1 = document.getElementById('newPW').value;
					var pw2 = document.getElementById('newPW2').value;

					if (pw2) {
						if (pw1 == pw2) {
							//alert("passwords equal: " + pw1 + " - " + pw2);
							document.getElementById('SaveButton').disabled = false;
						}
						else {
							//alert("passwords equal: " + pw1 + " - " + pw2);
							document.getElementById('SaveButton').disabled = true;
						}	
					}
					//else {
						//alert("Confirm password invalid");
					//}
					
				}
	</script> 
  </head>
  <body>
       <form name="DataForm" method="POST" action="<%=APP_ROOT%>/unsecureRequestProcessor/User.Edit">
		 <table width="50%" border="0">
			 <caption>
			      <h2>Change User Password</h2>
			 </caption>
			 <tr>
				 <th class="clsTableFormHeader" width="20%">User Name:</th>
				 <td width="30%">
				     <beanlib:InputControl value="#user.Username"/>
				     <beanlib:InputControl type="hidden" name="Username" value="#user.Username"/>
				     <beanlib:InputControl type="hidden" name="LoginId" value="#user.LoginId"/>
				     <beanlib:InputControl type="hidden" name="OldPassword" value="#user.Password"/>
				 </td>
			 </tr>
			 <tr>
				 <th class="clsTableFormHeader">First Name:</th>
				 <td>
				     <beanlib:InputControl name="Firstname" value="#user.Firstname"/>
				 </td>
			 </tr>
			  <tr>
				 <th class="clsTableFormHeader">Last Name:</th>
				 <td>
				     <beanlib:InputControl name="Lastname" value="#user.Lastname"/>
				 </td>
			 </tr>
			 <tr>
				 <th class="clsTableFormHeader" width="20%">New Password:</th>
				 <td width="30%">
				     <beanlib:InputControl id="newPW" 
				                           type="password" 
				                           name="Password" 
				                           onKeydown="enableSaveButton()" 
				                           onKeyup="enableSaveButton()" 
				                           onKeypress="enableSaveButton()" 
				                           onChange="enableSaveButton()"/>	
				 </td>
			 </tr>			 
			 <tr>
				 <th class="clsTableFormHeader" width="20%">New Password Confirmation:</th>
				 <td width="30%">
				     <beanlib:InputControl id="newPW2" 
				                           type="password" 
				                           name="PasswordConfirm" 
				                           onKeydown="enableSaveButton()" 
				                           onKeyup="enableSaveButton()" 
				                           onKeypress="enableSaveButton()" 
				                           onChange="enableSaveButton()"/>	
				 </td>
			 </tr>			 
			 <tr>
				 <th width="30%" colspan="2" class="clsFootnoteText">*** Changes can only be submitted when password inputs are equivalent ***</th>
			 </tr>			 
		 </table>
		 
		 <br>
		 <!-- Display any messages -->
		 <table>
			 <tr>
	  		   <td>
			     <font color="red">
				     <gen:ShowPageMessages dataSource="<%=RMT2ServletConst.REQUEST_MSG_MESSAGES%>"/>
			     </font>
		 	   </td>
			 </tr>
		 </table>
		 
		 <input name="<%=GeneralConst.REQ_CLIENTACTION %>" type="hidden">
		 <br>
		 <input id="SaveButton" name="<%=GeneralConst.REQ_SAVE %>" type="button" value="Save" style=width:90 disabled  onClick="handleAction('_self', document.DataForm, this.name)">
		 <input name="<%=GeneralConst.REQ_BACK %>" type="button" value="Back" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
   </form>
   <db:Dispose/>
  </body>
</html>
