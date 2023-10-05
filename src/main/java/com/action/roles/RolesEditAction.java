package com.action.roles;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.RoleFactory;
import com.entity.Roles;

/**
 * Action handler provides functionality to respond to requests pertaining 
 * to the Roles edit page.  The following request types are serviced: save 
 * and delete a role, and to navigate back to the role list page.
 * 
 * @author Roy Terrell
 * 
 */
public class RolesEditAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "Roles.Edit.save";

    private static final String COMMAND_DELETE = "Roles.Edit.delete";

    private static final String COMMAND_BACK = "Roles.Edit.back";

    private Logger logger;

    private Object data;

    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public RolesEditAction() throws SystemException {
	super();
	logger = Logger.getLogger("RolesEditAction");
    }

    /**
     * Performs post initialization and sets up a Roles Api reference.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
	super.init(_context, _request);
    }

    /**
     * Processes the client's requests to save and delete changes made to a
     * security role profile, and to navigate back to roles list page.
     * 
     * @param request
     *            The HttpRequest object
     * @param response
     *            The HttpResponse object
     * @param command
     *            Command issued by the client.
     * @Throws SystemException when an error needs to be reported.
     */
    public void processRequest(Request request, Response response, String command) throws ActionCommandException {
	super.processRequest(request, response, command);
	if (command.equalsIgnoreCase(RolesEditAction.COMMAND_SAVE)) {
	    this.saveData();
	}
	if (command.equalsIgnoreCase(RolesEditAction.COMMAND_DELETE)) {
	    this.deleteData();
	}
	if (command.equalsIgnoreCase(RolesEditAction.COMMAND_BACK)) {
	    this.doBack();
	}
    }

    /**
     * This method is used for adding and modifying security role profiles.
     * Security role changes are persisted to the database.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // UserApi api = UserFactory.createApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // Roles role = (Roles) this.data;
        // try {
        // // Update application profile.
        // api.maintainRole(role);
        // // Commit Changes to the database
        // tx.commitUOW();
        // this.msg = "Role configuration saved successfully";
        // }
        // catch (UserAuthenticationException e) {
        // this.msg = e.getMessage();
        // tx.rollbackUOW();
        // throw new ActionCommandException(this.msg);
        // }
        // finally {
        // api.close();
        // tx.close();
        // api = null;
        // tx = null;
        // }
    }

    /**
     * Navigates the user to the Roles List page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        // Call SOAP web service to get complete list of roles
        try {
            AuthenticationResponse appResponse = RoleSoapRequests.callGetRoles();
            ReplyStatusType rst = appResponse.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            List<Roles> obj = RoleFactory.create(appResponse.getProfile().getRoleInfo());
            this.data = obj;
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Obtains the selected role from the user's request. The role is identified
     * by its unique id and is used to obtain the complete record of the
     * security role from the database.
     * 
     * @throws ActionCommandException
     *             When a required selection was not made or an error occurrence
     *             during data retrieval.
     */
    protected void receiveClientData() throws ActionCommandException {
        // try {
        // // Retrieve the role from the database using unique id.
        // this.data = UserFactory.createRole();
        // // Update role object with user input.
        // UserFactory.packageBean(this.request, this.data);
        // }
        // catch (Exception e) {
        // logger.log(Level.ERROR, e.getMessage());
        // throw new ActionCommandException(e.getMessage());
        // }
    }

    /**
     * Delete a role from the database using its unique id.
     * 
     * @param appId
     *            The id of the user group
     * @return Total number of rows deleted.
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // UserApi api = UserFactory.createApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // int rc;
        // Roles role = (Roles) this.data;
        // try {
        // // Update user group profile.
        // rc = api.deleteRole(role.getRoleId());
        // // Commit Changes to the database
        // tx.commitUOW();
        // this.msg = rc +
        // " securiy role configuration(s) deleted successfully";
        // }
        // catch (UserAuthenticationException e) {
        // this.msg = e.getMessage();
        // tx.rollbackUOW();
        // throw new ActionCommandException(this.msg);
        // }
        // finally {
        // api.close();
        // tx.close();
        // api = null;
        // tx = null;
        // }
    }

    /**
     * Sends a {@link com.bean.Roles Roles} and any server messages to the user
     * via the request object.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
	this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.data);
	this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
    }

    /**
     * No Action
     */
    public void add() throws ActionCommandException {
	return;
    }

    /**
     * No Action
     */
    public void edit() throws ActionCommandException {
	return;
    }

}