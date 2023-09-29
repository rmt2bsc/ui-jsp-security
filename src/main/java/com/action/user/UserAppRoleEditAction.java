package com.action.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.action.application.ApplicationSoapRequests;
import com.action.groups.UserGroupSoapRequests;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.AppRoleFactory;
import com.entity.Application;
import com.entity.ApplicationFactory;
import com.entity.UserGroupFactory;
import com.entity.UserLogin;
import com.entity.UserLoginFactory;


/**
 * This class provides action handlers to respond to User add and edit requests.
 * 
 * @author Roy Terrell
 * 
 */
public class UserAppRoleEditAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "UserAppRole.Edit.save";

    private static final String COMMAND_BACK = "UserAppRole.Edit.back";
    
    private static final String COMMAND_USER_APP_ROLES = "UserAppRole.Edit.RQ_authentication_user_app_roles";
    
    private Logger logger;

    private UserLogin user;

    private Object apps;

    private int appId;

    private String revokedRoles[];

    private String assignedRoles[];

    private Object assignedRoleObjs;

    private Object revokedRoleObjs;

    private Object grpData;

    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public UserAppRoleEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("UserEditAction");
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
     * Processes the client's requests to save the changes made to a user's
     * profile, delete a user's profile, and to navigate back to the User Search
     * page.
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
        if (command.equalsIgnoreCase(UserAppRoleEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(UserAppRoleEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }
    
 
    /**
     * Updates the user's profile by persisting changes to the database. This
     * method is used for adding users as well as modifying users.
     * <p>
     * Pseudo edit logic to replace existing code.
     * <ol>
     * <li>Get Key values from the http request object for each entity to be
     * used with its respective API.</li>
     * <li>Retrieve data from the database into each entity object based on the
     * key value(s) representing each entity.</li>
     * <li>Set the properties of each object to the corresponding values from
     * the http request object.</li>
     * <li>Apply changes to the database for each object using its own API.</li>
     * <li>Package any data needed to be returned to the client.</li>
     * <li>Return control back to the Servlet.</li>
     * </ol>
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // Call SOAP web service to get user and permissions for selected
        // application
        AuthenticationResponse response = UserSoapRequests.callUpdateUserAppPermissions(this.user.getUsername(),
                this.assignedRoles);

        ReplyStatusType rst = response.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
            this.msg = rst.getMessage();
            return;
        }
        this.getUserRoleData();
    }

    /**
     * Navigates the user to the previous page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        this.receiveClientData();
        this.getUserGroupList();
        this.sendClientData();
        return;
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
        // Retrieve values from the request object into the User object.
        try {
            this.user = UserSoapRequests.getUser(this.user.getUsername());
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }

        // Get application id.
        try {
            this.appId =
                    Integer.parseInt(this.request.getParameter("ApplicationId"));
        } catch (NumberFormatException e) {
            this.appId = 0;
        }

        // Get role selections
        this.revokedRoles = this.request.getParameterValues("RevokedRoleId");
        this.assignedRoles = this.request.getParameterValues("AssignedRoleId");
    }

    /**
     * Sends a UserLogin data object and any server messages to the user via the
     * request object.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(UserConst.CLIENT_DATA_USER, this.user);
        this.request.setAttribute("selectedApp", String.valueOf(this.appId));
        this.request.setAttribute("apps", this.apps);
        this.request.setAttribute("assignedRoles", this.assignedRoleObjs);
        this.request.setAttribute("revokedRoles", this.revokedRoleObjs);
        this.request.setAttribute(UserConst.CLIENT_DATA_GROUPS, this.grpData);
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
    }

    private void getUserGroupList() {
        // Get User Group list
        AuthenticationResponse response2 = UserGroupSoapRequests.callSearchAllUserGroups();

        // Setup user group list on the Request object in order to pass back
        // to JSP client.
        this.grpData = UserGroupFactory.getUserGroupList(response2.getProfile().getUserGroupInfo());
    }

    private void getUserRoleData() throws ActionCommandException {
        // Call SOAP web service to get complete list of applications
        AuthenticationResponse appResponse = ApplicationSoapRequests.callApplications();
        ReplyStatusType rst = appResponse.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
            this.msg = rst.getMessage();
            return;
        }
        List<Application> applications = ApplicationFactory.create(appResponse.getProfile().getApplicationInfo());
        this.apps = applications;

        // Get application id.
        try {
            this.appId = Integer.parseInt(this.request.getParameter("ApplicationId"));
        } catch (NumberFormatException e) {
            this.appId = 0;
        }

        // Call SOAP web service to get user and permissions for selected
        // application
        AuthenticationResponse response = UserSoapRequests.callGetUserAppPermissions(this.user.getUsername(),
                this.appId);
        rst = response.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
            this.msg = rst.getMessage();
            return;
        }

        // Sync UserLogin Object
        this.user = UserLoginFactory.getUser(response.getProfile().getUserInfo().get(0));

        // Extract user's permissions
        this.assignedRoleObjs = new ArrayList();
        this.revokedRoleObjs = new ArrayList();
        if (response.getProfile().getUserInfo().get(0).getGrantedAppRoles() != null) {
            this.assignedRoleObjs = AppRoleFactory.create(response.getProfile().getUserInfo().get(0).getGrantedAppRoles()
                    .getUserAppRole());
        }
        if (response.getProfile().getUserInfo().get(0).getRevokedAppRoles() != null) {
            this.revokedRoleObjs = AppRoleFactory.create(response.getProfile().getUserInfo().get(0).getRevokedAppRoles()
                    .getUserAppRole());
        }
    }
    
    /**
     * No Action
     */
    public void delete() throws ActionCommandException {
        return;
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