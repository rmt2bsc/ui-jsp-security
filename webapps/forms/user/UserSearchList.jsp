
<form name="DataForm" method="POST" action="<%=APP_ROOT%>/unsecureRequestProcessor/User.Search"> 
   <font size="4" style="color:blue">Search Results</font>
   <div style="border-style:groove; border-color:#999999; background-color:buttonface; width:90%; height:350px; overflow:auto">   
      <table  width="100%" border="0" bgcolor="white" bordercolor="#999999" cellpadding="0" cellspacing="0">
		   <tr bgcolor="#FFCC00">
		     <th align="center" width="2%">&nbsp;</th>
		     <th align="left" width="8%">Login</th>
		     <th align="left" width="20%">Last Name</th>
		     <th align="left" width="20%">First Name</th>
		     <th align="center" width="15%">Group</th>
		     <th align="left" width="10%">SSN</th>
		     <th align="left" width="10%">Start Date</th>
		     <th align="left" width="10%">Term Date</th>
		   </tr>
	
	     <beanlib:LoopRows bean="item" list="<%=UserConst.CLIENT_DATA_USER%>">
		       <tr>
		         <td bgcolor="#FFCC00">
		             <beanlib:InputControl	type="radio" name="selCbx" value="rowid"/>
		             <beanlib:InputControl	type="hidden" name="LoginId" value="#item.LoginId"	uniqueName="yes"/>
		         </td>
		         <td>
		             <beanlib:InputControl value="#item.LoginId"/>
		             <beanlib:InputControl	type="hidden"	name="Username" value="#item.Username"	uniqueName="yes"/>
		         </td>		         
		         <td>
		             <beanlib:InputControl value="#item.Lastname"/>
		         </td>
		         <td>
		             <beanlib:InputControl value="#item.Firstname"/>
		         </td>
		         <td align="center">
					<beanlib:InputControl value="#item.GrpName"/>			 		         
		         </td>
		         <td>
		              <beanlib:InputControl value="#item.Ssn"/>
		         </td>
		         <td>
		             <beanlib:InputControl value="#item.StartDate" format="MM-dd-yyyy"/>
		         </td>
		         <td>
		             <beanlib:InputControl  value="#item.TerminationDate" format="MM-dd-yyyy"/>
		         </td>
		       </tr>
		     </beanlib:LoopRows>
		
		     <% if (pageContext.getAttribute("ROW") == null) {
		          out.println("<tr><td colspan=11 align=center>Data Not Found</td></tr>");
		        }
		     %>
		     <input name="clientAction" type="hidden">
		   
		 </table>
    </div>
    <br>
    <table>
	    <tr>
	       <td>
	          <input type="button" name="<%=GeneralConst.REQ_ADD %>" value="Add" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
			      <input type="button" name="<%=GeneralConst.REQ_EDIT %>" value="Edit" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
			      <input type="button" name="<%=GeneralConst.REQ_BACK %>" value="Back" style=width:90 onClick="handleAction('_self', document.DataForm, this.name)">
	       </td>
	    </tr>
	</table>    
</form>		 