
<form name="DataForm" method="POST" action="<%=APP_ROOT%>/unsecureRequestProcessor/User.Search"> 
   <font size="4" style="color:blue">Search Results</font>
   <div style="border-style:groove; border-color:#999999; background-color:buttonface; width:90%; height:350px; overflow:auto">   
      <table  width="100%" border="0" bgcolor="white" bordercolor="#999999" cellpadding="0" cellspacing="0">
		   <tr bgcolor="#FFCC00">
		     <th align="left" width="2%">&nbsp;</th>
		     <th align="left" width="6%">Login Id</th>
		     <th align="left" width="12%">Last Name</th>
		     <th align="left" width="12%">First Name</th>
		     <th align="left" width="9%">User Name</th>
		     <th align="left" width="7%">Status</th>
		     <th align="left" width="10%">Group</th>
		     <th align="left" width="12%">SSN</th>
		     <th align="left" width="10%">Start Date</th>
		     <th align="left" width="10%">Term Date</th>
		     <th align="center" width="10%">Login Total</th>
		   </tr>
	
	     <beanlib:LoopRows bean="item" list="<%=UserConst.CLIENT_DATA_SEARCH%>">
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
		         <td>
		             <beanlib:InputControl value="#item.Username"/>
		         </td>
		         <td>
		            <gen:Evaluate expression="#item.Active">
				        <gen:When expression="1">
							 <beanlib:InputControl value="Active"/>				        
				        </gen:When>
				        <gen:WhenElse>
						     <beanlib:InputControl value="Inactive"/>
				        </gen:WhenElse>
				     </gen:Evaluate>
		         </td>
		         <td>
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
		         <td align="center">
		             <beanlib:InputControl  value="#item.TotalLogons"/>
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