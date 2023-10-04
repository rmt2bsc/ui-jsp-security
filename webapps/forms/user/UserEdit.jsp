<%@page import="com.action.user.UserConst"%>
<%@page import="com.entity.UserLogin"%>
<%@ taglib uri="/rmt2-beantaglib" prefix="beanlib" %>
<%@ taglib uri="/rmt2-generaltaglib" prefix="gen" %>
<%@ page import="com.api.constants.GeneralConst" %>
<%@ page import="com.api.constants.RMT2ServletConst" %>

<gen:InitAppRoot id="APP_ROOT"/>

<html>
  <title>User Maintenance</title>
  <head>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">
    <link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2Table.css">
    <link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2General.css">   
    <script type='text/javascript' language='javascript' src="<%=APP_ROOT%>/js/datetimepicker.js"></script>   
  	<script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2General.js"></script>
  	<script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2Menu.js"></script>   
  </head>
  <body>
       <form name="DataForm" method="POST" action="<%=APP_ROOT%>/unsecureRequestProcessor/User.Edit">
		 <table width="90%" border="0">
			 <caption>
			      <h2>User Maintenance Edit</h2>
			 </caption>
			 <tr>
				 <th class="clsTableFormHeader" width="20%">User Name:</th>
				 <td width="30%">
				     <gen:Evaluate expression="#user.LoginId">
				        <gen:When expression="0">
							 <beanlib:InputControl type="text" name="Username" value="#user.Username"/>				        
				        </gen:When>
				        <gen:WhenElse>
						     <beanlib:InputControl value="#user.Username"/>
						     <beanlib:InputControl type="hidden" name="Username" value="#user.Username"/>				        
				        </gen:WhenElse>
				     </gen:Evaluate>
				     <beanlib:InputControl type="hidden" name="LoginId" value="#user.LoginId"/>
				 </td>
				 
				 <th class="clsTableFormHeader" width="20%">Password:</th>
				 <td width="30%">
				     <gen:Evaluate expression="#user.LoginId">
				        <gen:When expression="0">
							 <beanlib:InputControl type="password" name="Password" value="#user.Password"/>				        
				        </gen:When>
				        <gen:WhenElse>
						     <beanlib:InputControl value="************"/>
						     <beanlib:InputControl type="hidden" name="Password" value="#user.Password"/>				        
				        </gen:WhenElse>
				     </gen:Evaluate>
				 </td>
			 </tr>
			 <tr>
				 <th class="clsTableFormHeader">First Name:</th>
				 <td>
				     <beanlib:InputControl type="text" name="Firstname" value="#user.Firstname" size="30"/>
				 </td>
				 <th class="clsTableFormHeader">Last Name:</th>
				 <td>
				     <beanlib:InputControl type="text" name="Lastname" value="#user.Lastname" size="30"/>
				 </td>
			 </tr>
			 
			 <tr>
				 <th class="clsTableFormHeader">Birth Date:</th>
				 <td>
				     <beanlib:InputControl id="birthDate" type="text" name="BirthDate" value="#user.BirthDate" format="MM-dd-yyyy" size="15"/>
				     <a href="javascript:NewCal('birthDate','mmddyyyy')"><img src="<%=APP_ROOT%>/images/cal.gif" width="16" height="16" border="0" alt="Pick a date"></a>	
				 </td>
				 <th class="clsTableFormHeader">SSN:</th>
				 <td>
				     <beanlib:InputControl type="text" name="Ssn" value="#user.Ssn" size="30"/>
				 </td>
			 </tr>			 
			 
			 <tr>
				 <th class="clsTableFormHeader">Start Date:</th>
				 <td>
				     <beanlib:InputControl id="startDate" type="text" name="StartDate" value="#user.StartDate" format="MM-dd-yyyy" size="15"/>
				     <a href="javascript:NewCal('startDate','mmddyyyy')"><img src="<%=APP_ROOT%>/images/cal.gif" width="16" height="16" border="0" alt="Pick a date"></a>						     
				 </td>
				 <th class="clsTableFormHeader" width="15%">Termination Date:</th>
				 <td>
				     <beanlib:InputControl id="termDate" type="text" name="TerminationDate" value="#user.TerminationDate" format="MM-dd-yyyy" size="15"/>
				     <a href="javascript:NewCal('termDate','mmddyyyy')"><img src="<%=APP_ROOT%>/images/cal.gif" width="16" height="16" border="0" alt="Pick a date"></a>						     					     
				 </td>
			 </tr>
			 <tr>
				 <th class="clsTableFormHeader">Email:</th>
				 <td>
				    <beanlib:InputControl type="text" name="Email" value="#user.Email" size="40"/>
				 </td>
				 <th class="clsTableFormHeader">Group:</th>
				 <td> 
					 <beanlib:InputControl
					     dataSource="<%=UserConst.CLIENT_DATA_GROUPS %>"
					     name="GrpId"
					     type="select"
					     displayProperty="description"
					     codeProperty="grpId"
					     selectedValue="#user.GrpId"/>
				 </td>				 
			 </tr>
			 <tr>
				 <th class="clsTableFormHeader">Active:</th>
				 <td>
				    <beanlib:InputControl
					     name="Active"
					     type="checkbox"
					     value="#user.Active"
					     checkedValue="1"
					     onChange="{this.value = (this.checked ? 1 : 0)}"/>
				 </td>
				 <th class="clsTableFormHeader"></th>
				 <td></td>				 
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
		 <input name="<%=GeneralConst.REQ_SAVE %>" type="button" value="Save" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
		 <input name="<%=GeneralConst.REQ_DELETE %>" type="button" value="Delete" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
		 <input name="<%=GeneralConst.REQ_CHANGE_PASSWORD %>" type="button" value="Change Password" style=width:140 onClick="handleAction('_self', document.DataForm, this.name)">
		 <input name="approle" type="button" value="Roles" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
		 <input name="resources" disabled type="button" value="Resources" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
		 <input name="<%=GeneralConst.REQ_BACK %>" type="button" value="Back" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
   </form>
   <db:Dispose/>
  </body>
</html>
