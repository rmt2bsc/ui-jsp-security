package com.action.groups;

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
import com.api.web.util.RMT2WebUtility;
import com.entity.UserGroup;
import com.entity.UserGroupFactory;

/**
 * Action handler provides functionality to respond to requests pertaining to
 * the user group edit page. The following request types are serviced: save user
 * group, delete an user group, and back.
 * 
 * @author Roy Terrell
 * 
 */
public class UserGroupEditAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "Group.Edit.save";

    private static final String COMMAND_DELETE = "Group.Edit.delete";

    private static final String COMMAND_BACK = "Group.Edit.back";

    private Logger logger;

    // private UserApi api;

    private Object data;

    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public UserGroupEditAction() throws SystemException {
        super();
        logger = Logger.getLogger(UserGroupEditAction.class);
    }

    /**
     * Performs post initialization and sets up an User Api reference.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
    }

    /**
     * Processes the client's requests to save changes made to an user group
     * profile, delete an user group profile, and to navigate back to User Group
     * List page.
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
        if (command.equalsIgnoreCase(UserGroupEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(UserGroupEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(UserGroupEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    /**
     * Updates an user group profile by persisting changes to the database. This
     * method is used for adding and modifying user group profiles.
     * 
     * @throws ActionHandlerException
     */
    public void save() throws ActionCommandException {
        try {
            // Get request data
            UserGroup grp = (UserGroup) this.data;

            // Update user group record
            // UI-37: added login id and session id parameters to the callSave
            // method invocation
            AuthenticationResponse response = UserGroupSoapRequests.callUpdateUserGroup(grp, this.loginId,
                    this.session.getId());
            // Get message text from reply status
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e);
        } finally {

        }
    }

    /**
     * Delete an user group from the database using the id of the user group.
     * 
     * @param appId
     *            The id of the user group
     * @return Total number of rows deleted.
     * @throws ActionHandlerException
     */
    public void delete() throws ActionCommandException {
        // Get request data
        UserGroup grp = (UserGroup) this.data;

        // Delete user group record
        AuthenticationResponse response = UserGroupSoapRequests.callDeleteUserGroup(grp.getGrpId());
        // Get message text from reply status
        ReplyStatusType rst = response.getReplyStatus();
        this.msg = rst.getMessage();
        this.sendClientData();
    }

    /**
     * Navigates the user to the User Group List page.
     * 
     * @throws ActionHandlerException
     */
    protected void doBack() throws ActionCommandException {
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
     * Gathers data from the user's request and packages the data into a User
     * Group object.
     * 
     * @throws ActionHandlerException
     */
    protected void receiveClientData() throws ActionCommandException {
        this.data = UserGroupFactory.createUserGroup();
        try {
            // Update user group object with user input.
            RMT2WebUtility.packageBean(this.request, this.data);
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e);
        }
    }

    /**
     * Sends an {@link com.bean.UserGroup UserGroup} data object and any server
     * messages to the user via the request object.
     * 
     * @throws ActionHandlerException
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