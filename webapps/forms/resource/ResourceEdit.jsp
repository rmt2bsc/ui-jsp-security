<%@ taglib uri="/rmt2-generaltaglib" prefix="gen" %>
<%@ taglib uri="/rmt2-beantaglib" prefix="beanlib" %>
<%@ page import="com.api.constants.GeneralConst" %>
<%@ page import="com.AuthConstants" %>
<%@ page import="com.api.constants.RMT2ServletConst" %>


<gen:InitAppRoot id="APP_ROOT"/>
<gen:InitSoapHost id="SOAP_HOST"/>

<html>
  <head>
    <title>Resource Edit</title>
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">  
    <link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2Table.css">
	<link rel=STYLESHEET type="text/css" href="<%=APP_ROOT%>/css/RMT2General.css">
	<script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2General.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2Menu.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/AjaxApi.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/AjaxRequestConfig.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/AjaxXmlRenderer.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/xpath.js"></script>
    <script Language="JavaScript" src="<%=APP_ROOT%>/js/RMT2DateValidation.js"></script>
    
     <script>
        function doSoapCall() {
        	 var xmlhttp = new XMLHttpRequest();
             xmlhttp.open('POST', '<%=SOAP_HOST%>', true);
             
        	 var resourceId = document.DataForm.RsrcTypeId.value;  
        	 //  .options[document.DataForm.RsrcTypeId.selectedIndex].text;
             var payload = '<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/>';
               payload += '<SOAP-ENV:Body>';
               payload += '<AuthenticationRequest><header><routing>JMS: rmt2.queue.authentication</routing><application>authentication</application><module>admin</module>';
               payload += '<transaction>GET_RESOURCE_SUB_TYPE</transaction><delivery_mode/><message_mode>REQUEST</message_mode>'; 
          	   payload += '<delivery_date>'  + getCurrentDateTime() + '</delivery_date></header>';
          	   payload += '<criteria>';
          	   payload += '<resource_criteria>';
       		   payload += '<rsrc_type_id>' + resourceId + '</rsrc_type_id>';
       		   payload += '</resource_criteria>';
       		   payload += '</criteria>';
       		   payload += '</AuthenticationRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>';
         		   
       		   xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4) {
                    if (xmlhttp.status == 200) {
                        doSoapCallback(xmlhttp.responseText);
                    }
                }
            }
            // Send the POST request
            xmlhttp.setRequestHeader('Content-Type', 'text/xml');
            xmlhttp.send(payload);
        }
        
       
	   function doSoapCallback(xmlData) {
	        // Get XML based on entire document
	        var renderer = new AjaxXmlRenderer(null, xmlData);
	   		renderer.buildSelectOptions(document.DataForm.RsrcSubtypeId, 
	   				"/SOAP-ENV:Envelope/SOAP-ENV:Body/AuthenticationResponse/profile/resources_info/resourcesubtype/uid", 
	   				"/SOAP-ENV:Envelope/SOAP-ENV:Body/AuthenticationResponse/profile/resources_info/resourcesubtype/code");
	   } 
	   
    </script>   

  </head>
  
   <%
//      UserResource res = request.getAttribute(GeneralConst.CLIENT_DATA_RECORD) == null ? new UserResource() : (UserResource) request.getAttribute(GeneralConst.CLIENT_DATA_RECORD);
  //    String subTypeCriteria = "rsrc_type_id = " + res.getRsrcTypeId();
  	  String pageTitle = "Security - Resource Edit";    
	%>
						  
  <body bgcolor="#FFFFFF" text="#000000">
       <form name="DataForm" method="POST" action="<%=APP_ROOT%>/unsecureRequestProcessor/Resource.Edit">
		  <h3><strong><%=pageTitle%></strong></h3>
  		<br>
	  
		<table width="55%" border="0" cellspacing="2" cellpadding="0">
				<tr> 
					 <td width="25%" class="clsTableFormHeader">
						   <font size="3"><b>Resource Id</b></font>
					 </td>
					 <td align="left" width="75%" >
					     <beanlib:InputControl type="hidden" name="RsrcId" value="#record.RsrcId"/>                                  
					 	 <beanlib:InputControl value="#record.RsrcId"/>                                  
					 </td>
				</tr>
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Name</b></font>
					 </td>
					 <td align="left">
						<beanlib:InputControl type="text" name="Name" value="#record.Name" size="85" maxLength="80"/>                                  
					 </td>
				</tr>				
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Resource Type</b></font>
					 </td>
					 <td align="left">
							<beanlib:InputControl dataSource="<%=AuthConstants.RESOURCE_TYPE_LIST%>"
												  type="select"
											      name="RsrcTypeId"
												  codeProperty="RsrcTypeId"
												  displayProperty="Description"
												  selectedValue="#record.RsrcTypeId"
												  onChange="doSoapCall()"/>
					 </td>
				</tr>								
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Resource Sub-Type</b></font>
					 </td>
					 <td align="left">
							<beanlib:InputControl dataSource="<%=AuthConstants.RESOURCE_SUBTYPE_LIST%>"
												  type="select"
												  name="RsrcSubtypeId"
												  codeProperty="ResrcSubtypeId"
											  	  displayProperty="ResrcSubtypeName"
												  selectedValue="#record.RsrcSubtypeId"/>						 
					 </td>
				</tr>												
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Description</b></font>
					 </td>
					 <td align="left">
						<beanlib:InputControl type="textarea" 
						                      name="Description" 
						                      value="#record.Description" 
						                      cols="50"
						                      rows="5" 
						                      maxLength="250"/>                                  
					 </td>
				</tr>
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Url</b></font>
					 </td>
					 <td align="left">
						<beanlib:InputControl type="text" name="Url" value="#record.Url" size="105" maxLength="100"/>                                  
					 </td>
				</tr>
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Host Server</b></font>
					 </td>
					 <td align="left">
						<beanlib:InputControl type="text" name="Host" value="#record.Host" size="60" maxLength="50"/>                                  
					 </td>
				</tr>
				<tr> 
					 <td class="clsTableFormHeader">
						<font size="3"><b>Secured?</b></font>
					 </td>
					 <td align="left">
					     <table width="50%" border="0">
					        <tr>
					            <th>Yes</th>
					            <td>
					                <input type="radio" name="Secured" value="1"
						                <gen:Evaluate  expression="#record.Secured">
											<gen:When expression="1">checked</gen:When>
										</gen:Evaluate> >
					            </td>
											<th>No</th>
					            <td>
					                <input type="radio" name="Secured" value="0" 
  						               <gen:Evaluate  expression="#record.Secured">
											<gen:When expression="0">checked</gen:When>
									   </gen:Evaluate>  >
					            </td>					            
					        </tr>
					     </table>
					 </td>
				</tr>				
		</table>
		
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
		 
        <!-- Display command links -->
        <table width="50%" cellpadding="0" cellspacing="0">
			<tr>
				<td width="10%">
				    <a id="<%=GeneralConst.REQ_SAVE%>" href="javascript:handleAction('_self', document.DataForm, 'save')">
				       <img name="m_add" src="<%=APP_ROOT%>/images/cm-res-save.gif"  width="48px" height="70px" style="border: none" alt="Save Resource Type">
				    </a>
				</td>
				<td width="10%">
				    <a id="<%=GeneralConst.REQ_DELETE%>" href="javascript:handleAction('_self', document.DataForm, 'delete')">
				       <img name="m_delete" src="<%=APP_ROOT%>/images/cm-res-delete.gif"  width="48px" height="70px" style="border: none" alt="Delete Resource Type">
				    </a>
				</td>								
	      <td width="10%" valign="top" align="center"> 
          <a  href="javascript:handleAction('_self', document.DataForm, 'back')"> 
            <img name="m_back" src="<%=APP_ROOT%>/images/cm-back.gif" width="48px" height="70px" style="border: none" alt="Back to Resource Type List">
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
