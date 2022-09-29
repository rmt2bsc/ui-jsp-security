package com.action.groups;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.UserGroupType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.UserGroupFactory;

/**
 * Action handler provides functionality to respond to requests pertaining to
 * the user group list page. The following request types are serviced: list user
 * groups, add user group, and edit user group.
 * 
 * @author Roy Terrell
 * 
 */
public class UserGroupSearchAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_LIST = "Group.Search.list";

    private static final String COMMAND_EDIT = "Group.Search.edit";

    private static final String COMMAND_ADD = "Group.Search.add";

    private Logger logger;

    // private UserApi api;

    private Object data;

    private int selectedGrpId;

    private boolean selectionRequired;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public UserGroupSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("UserGroupSearchAction");
    }

    /**
     * Performs post initialization and sets up an User Api reference.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
        // this.api = UserFactory.createApi(this.dbConn);
    }

    /**
     * Processes the client's request using request, response, and command. Add,
     * edit, delete, and list are the user group commands recognized.
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
        if (command.equalsIgnoreCase(UserGroupSearchAction.COMMAND_LIST)) {
            this.search();
        }
        if (command.equalsIgnoreCase(UserGroupSearchAction.COMMAND_EDIT)) {
            this.selectionRequired = true;
            this.editData();
        }
        if (command.equalsIgnoreCase(UserGroupSearchAction.COMMAND_ADD)) {
            this.addData();
        }
    }

    /**
     * Retrieve all user group records from the database.
     * 
     * @throws ActionHandlerException
     */
    protected void search() throws ActionCommandException {
        try {
            // Retrieve all user group records from the database
            AuthenticationResponse response = UserGroupSoapRequests.callSearchAllUserGroups();

            // Setup user group list on the Request object in order to pass back
            // to JSP client.
            this.data = UserGroupFactory.getUserGroupList(response.getProfile().getUserGroupInfo());
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e);
        } finally {

        }
    }

    /**
     * Creates a new user group object which is used to add an user group entry
     * to the system via the User Group Maintenance Edit page.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        this.data = UserGroupFactory.createUserGroup();
        return;
    }

    /**
     * Obtains the input data from the request and maps the data to an user
     * group instance.
     * 
     * {@link com.bean.Application Application} object.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        try {
            // Retrieve user group from the database using unique id.
            AuthenticationResponse response = UserGroupSoapRequests.callSearchUserGroup(this.selectedGrpId);

            // Setup fetched user group object to be passed to caller
            this.data = null;
            List<UserGroupType> results = UserGroupFactory.getUserGroupList(response.getProfile().getUserGroupInfo());
            if (results != null && results.size() == 1) {
                this.data = results.get(0);
            }
            return;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e);
        } finally {
        }
    }

    /**
     * Obtains the user group's unique id data from the request which was
     * selected by the user. The unique id is used to obtain the user group
     * record from the database and to map the data to its associated object.
     * 
     * @throws ActionHandlerException
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

            String temp = this.getInputValue("GrpId", null);
            try {
                this.selectedGrpId = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                this.selectedGrpId = -1;
            }
        }
    }

    /**
     * Sends an user group object to the client via the request object.
     * 
     * @throws ActionHandlerException
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