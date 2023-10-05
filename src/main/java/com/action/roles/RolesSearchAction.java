package com.action.roles;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.RoleFactory;
import com.entity.Roles;

/**
 * Action handler provides functionality to respond to requests pertaining to
 * the security role list page. The following request types are serviced: list,
 * add, and edit a role.
 * 
 * @author Roy Terrell
 * 
 */
public class RolesSearchAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_LIST = "Roles.Search.list";

    private static final String COMMAND_EDIT = "Roles.Search.edit";

    private static final String COMMAND_ADD = "Roles.Search.add";

    private Logger logger;

    private Object data;

    private int selectedRoleId;

    private boolean selectionRequired;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public RolesSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("RolesSearchAction");
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
     * Processes the client's request using request, response, and command. Add,
     * edit, and list are the roles commands recognized.
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
        this.selectionRequired = false;
        if (command.equalsIgnoreCase(RolesSearchAction.COMMAND_LIST)) {
            this.search();
        }
        if (command.equalsIgnoreCase(RolesSearchAction.COMMAND_EDIT)) {
            this.selectionRequired = true;
            this.editData();
        }
        if (command.equalsIgnoreCase(RolesSearchAction.COMMAND_ADD)) {
            this.addData();
        }
    }

    /**
     * Retrieve all security role records from the database.
     * 
     * @throws ActionCommandException
     */
    protected void search() throws ActionCommandException {
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
     * Creates a new security role object which is used to add an entry to the
     * system via the Roles Maintenance Edit page.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        // this.data = UserFactory.createRole();
        return;
    }

    /**
     * Obtains the input data from the request and maps the data to an
     * {@link Roles} object. Lastly, the application object is prepared to be
     * forwarded to the next JSP for processing.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        Roles obj = RoleFactory.create();
        obj.setRoleId(this.selectedRoleId);
        String temp = this.getInputValue("Name", null);
        obj.setName(temp);
        temp = this.getInputValue("Description", null);
        obj.setDescription(temp);
        this.data = obj;
        return;
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
        if (this.selectionRequired) {
            // Client must select a row to edit.
            if (this.selectedRow < 0) {
                logger.log(Level.ERROR, RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED);
                throw new ActionCommandException(RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED,
                        RMT2SystemExceptionConst.RC_ITEM_NOT_SELECTED);
            }
            String temp = this.getInputValue("RoleId", null);
            try {
                this.selectedRoleId = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                this.selectedRoleId = -1;
            }
        }
    }

    /**
     * Sends a {@link com.bean.Roles Roles} object to the client via the request
     * object.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.data);
    }

    /**
     * No Action
     */
    public void save() throws ActionCommandException {
    }

    /**
     * No Action
     */
    public void delete() throws ActionCommandException {
        return;
    }
}