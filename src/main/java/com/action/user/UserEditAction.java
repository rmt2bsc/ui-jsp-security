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
import com.entity.Application;
import com.entity.ApplicationFactory;
import com.entity.UserResourceFactory;
import com.entity.UserAppRoleFactory;
import com.entity.UserLogin;
import com.entity.UserLoginFactory;

/**
 * This class provides action handlers to respond to User maintenance related
 * requests.
 * 
 * @author Roy Terrell
 * 
 */
public class UserEditAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "User.Edit.save";
    private static final String COMMAND_BACK = "User.Edit.back";
    private static final String COMMAND_DELETE = "User.Edit.delete";
    private static final String COMMAND_CHANGEPASSWORD = "User.Edit.changepassword";
    private static final String COMMAND_APPROLE = "User.Edit.approle";
    private static final String COMMAND_RESOURCE = "User.Edit.resources";
    private static final Logger logger = Logger.getLogger(UserEditAction.class);

    private Object searchData;
    private UserLogin user;
    private Object grpData;
    private Object apps;
    private Object assignedRoles;
    private Object revokedRoles;
    private String selectedApp;
    private Object assignedResources;
    private Object revokedResources;

    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public UserEditAction() throws SystemException {
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
     * Processes various client requests targeted to modify the UserLogin
     * profile.
     * <p>
     * Requests includes saving changes made to a user's profile, delete a
     * user's profile, password change, user roles, user resources, and to
     * navigate back to the User Search page.
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

        if (command.equalsIgnoreCase(UserEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(UserEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(UserEditAction.COMMAND_CHANGEPASSWORD)) {
            this.doChangePassword();
        }
        if (command.equalsIgnoreCase(UserEditAction.COMMAND_BACK)) {
            this.doBack();
        }
        if (command.equalsIgnoreCase(UserEditAction.COMMAND_APPROLE)) {
            this.doAppRole();
        }
        if (command.equalsIgnoreCase(UserEditAction.COMMAND_RESOURCE)) {
            this.doResources();
        }
    }

    /**
     * Applies data changes to the user's profile by persisting changes to the
     * database. This method is used for adding users as well as modifying
     * users.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // Apply user updates
        AuthenticationResponse response = UserSoapRequests.callUpdateUser(this.user);

        // Check the results of the update operation, and, if okay, fetch
        // updated results and refresh display. Otherwise, show error message
        // with the original user edits.
        ReplyStatusType rst = response.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_SUCCESS) {
            // Retrieve updated user
            this.user = UserSoapRequests.getUser(this.user.getUsername());
        }
        else {
            this.msg = rst.getExtMessage();
        }
        // Retrieve all user group records
        this.grpData = UserGroupSoapRequests.getUserGroupList();
    }


    /**
     * Deletes a user from the database.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // Call SOAP web service to delete user from the system
        AuthenticationResponse response = UserSoapRequests.callDeleteUser(this.user.getLoginId());
        ReplyStatusType rst = response.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_SUCCESS) {
            this.msg = rst.getMessage();
        }
        else {
            this.msg = rst.getExtMessage() + ".   Details [" + this.user.getFirstname() + " " + this.user.getLastname() + " "
                    + this.user.getUsername() + "]";
        }

        // Initialize empty user group list to satisfy JSP data requirements
        this.grpData = new ArrayList();
        return;
    }

    /**
     * Invokes the user change password JSP page for selected employee
     * 
     * @throws ActionCommandException
     */
    protected void doChangePassword() throws ActionCommandException {
        // Get search parameters from request and add search parameter
        // object to session
        this.receiveClientData();

        // Send User search results data to client
        this.sendClientData();
    }

    /**
     * Return to the User Search JSP and provide the list of UserLogin data to
     * render the search results section of the target JSP page
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        // Get search parameters from request and add search parameter
        // object to session
        RMT2TagQueryBean query = (RMT2TagQueryBean) this.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);
        UserCriteria criteria = (UserCriteria) query.getCustomObj();

        // Fetch all users for display
        AuthenticationResponse response = UserSoapRequests.callSearchUsers(criteria);
        this.searchData = UserLoginFactory.getUserList(response.getProfile().getUserInfo());

        // Send User search results data to client
        this.sendClientData();
    }

    /**
     * Gathers all data needed to populate User Application-Role page.
     * 
     * @throws ActionCommandException
     */
    protected void doAppRole() throws ActionCommandException {
        this.receiveClientData();
        this.getAppRoleData();
        this.sendClientData();
        return;
    }

    private void getAppRoleData() {
        // Call SOAP web service to get complete list of applications
        AuthenticationResponse appResponse = ApplicationSoapRequests.callGetApplications();
        ReplyStatusType rst = appResponse.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
            this.msg = rst.getMessage();
            return;
        }
        List<Application> applications = ApplicationFactory.create(appResponse.getProfile().getApplicationInfo());
        this.apps = applications;
        this.selectedApp = String.valueOf(applications.get(0).getAppId());

        // Call SOAP web service to get user and permissions for selected
        // application
        AuthenticationResponse response = UserSoapRequests.callGetUserAppPermissions(this.user.getUsername(),
                applications.get(0).getAppId());
        rst = response.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
            this.msg = rst.getMessage();
            return;
        }

        // Sync UserLogin Object
        this.user = UserLoginFactory.getUser(response.getProfile().getUserInfo().get(0));

        // Extract user's permissions
        this.assignedRoles = new ArrayList();
        this.revokedRoles = new ArrayList();
        if (response.getProfile().getUserInfo().get(0).getGrantedAppRoles() != null) {
            this.assignedRoles = UserAppRoleFactory.create(response.getProfile().getUserInfo().get(0).getGrantedAppRoles()
                    .getUserAppRole());
        }
        if (response.getProfile().getUserInfo().get(0).getRevokedAppRoles() != null) {
            this.revokedRoles = UserAppRoleFactory.create(response.getProfile().getUserInfo().get(0).getRevokedAppRoles()
                    .getUserAppRole());
        }
    }

    protected void doResources() throws ActionCommandException {
        this.receiveClientData();
        this.getResourcesData();
        this.sendClientData();
        return;
    }

    private void getResourcesData() {
        // Call SOAP web service to get complete list of applications
        AuthenticationResponse appResponse = ApplicationSoapRequests.callGetApplications();
        ReplyStatusType rst = appResponse.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
            this.msg = rst.getMessage();
            return;
        }
        List<Application> applications = ApplicationFactory.create(appResponse.getProfile().getApplicationInfo());
        this.apps = applications;
        this.selectedApp = String.valueOf(applications.get(0).getAppId());

        // Call SOAP web service to get user and permissions for selected
        // application
        AuthenticationResponse response = UserSoapRequests.callGetUserAppPermissions(this.user.getUsername(),
                applications.get(0).getAppId());
        rst = response.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
            this.msg = rst.getMessage();
            return;
        }

        // Sync UserLogin Object
        this.user = UserLoginFactory.getUser(response.getProfile().getUserInfo().get(0));

        // Extract user's permissions
        this.assignedResources = new ArrayList();
        this.revokedResources = new ArrayList();
        if (response.getProfile().getUserInfo().get(0).getGrantedResources() != null) {
            this.assignedResources = UserResourceFactory.createUserResourceAccess(response.getProfile().getUserInfo().get(0)
                    .getGrantedResources());
        }
        if (response.getProfile().getUserInfo().get(0).getRevokedResources() != null) {
            this.revokedResources = UserResourceFactory.createUserResourceAccess(response.getProfile().getUserInfo().get(0)
                    .getRevokedResources());
        }
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
        this.request.setAttribute(UserConst.CLIENT_DATA_SEARCH, this.searchData);
        this.request.setAttribute(UserConst.CLIENT_DATA_USER, this.user);
        this.request.setAttribute(UserConst.CLIENT_DATA_GROUPS, this.grpData);
        this.request.setAttribute("apps", this.apps);
        this.request.setAttribute("selectedApp", this.selectedApp);
        this.request.setAttribute("assignedRoles", this.assignedRoles);
        this.request.setAttribute("revokedRoles", this.revokedRoles);
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