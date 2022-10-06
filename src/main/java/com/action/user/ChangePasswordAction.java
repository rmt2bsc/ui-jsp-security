package com.action.user;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.action.groups.UserGroupSoapRequests;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.persistence.DatabaseException;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.UserLogin;
import com.entity.UserLoginFactory;

/**
 * This class provides action handlers to respond to User change password
 * related requests.
 * 
 * @author Roy Terrell
 * 
 */
public class ChangePasswordAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "User.ChangePassword.save";
    private static final String COMMAND_BACK = "User.ChangePassword.back";
    private static final Logger logger = Logger.getLogger(ChangePasswordAction.class);

    private UserLogin user;
    private Object grpData;

    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public ChangePasswordAction() throws SystemException {
        super();
    }

    /**
     * Initializes the UserApi associated with this class.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
    }

    /**
     * Processes the client's requests to save the user's password changes and
     * to navigate back to the User Maintenance Edit JSP page page.
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
        this.query = (RMT2TagQueryBean) this.request.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);

        if (command.equalsIgnoreCase(ChangePasswordAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(ChangePasswordAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    /**
     * Updates the user's profile with the new password.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // Apply user password change
        AuthenticationResponse response = UserSoapRequests.callChangePassword(this.user);

        // Check the results of the change password operation.
        ReplyStatusType rst = response.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() != GeneralConst.RC_SUCCESS) {
            this.msg = rst.getExtMessage();
        }
    }

    /**
     * Return to the User Maintenance Edit JSP and provide the UserLogin and the
     * complete list of user groups for the target JSP to render.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        this.receiveClientData();
        // Fetch user profile data
        this.user = UserSoapRequests.getUser(this.user.getUsername());
        // Fetch all user group records
        this.grpData = UserGroupSoapRequests.getUserGroupList();

        this.sendClientData();
    }


    /**
     * Gathers data from the user's request and packages the data into a
     * UserLogin object.
     * 
     * @throws ActionCommandException
     */
    protected void receiveClientData() throws ActionCommandException {
        try {
            // Retrieve values from the request object into the User object.
            this.user = UserLoginFactory.create();
            RMT2WebUtility.packageBean(this.request, this.user);
        } catch (SystemException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Sends data needed for target client JSP to render the page
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(UserConst.CLIENT_DATA_USER, this.user);
        this.request.setAttribute(UserConst.CLIENT_DATA_GROUPS, this.grpData);
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

    @Override
    public void delete() throws ActionCommandException, DatabaseException {
        // TODO Auto-generated method stub

    }

}