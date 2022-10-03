package com.action.user;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
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
import com.entity.UserGroupFactory;
import com.entity.UserLogin;
import com.entity.UserLoginFactory;

/**
 * This class provides action handlers to respond to User add and edit requests.
 * 
 * @author Roy Terrell
 * 
 */
public class UserEditAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "User.Edit.save";
    private static final String COMMAND_BACK = "User.Edit.back";
    private static final String COMMAND_DELETE = "User.Edit.delete";
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

	if (command.equalsIgnoreCase(UserEditAction.COMMAND_SAVE)) {
	    this.saveData();
	}
	if (command.equalsIgnoreCase(UserEditAction.COMMAND_DELETE)) {
	    this.deleteData();
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
        this.user = UserLoginFactory.create();
        RMT2WebUtility.packageBean(this.request, this.user);

        // Apply user updates
        AuthenticationResponse response = UserSoapRequests.callUpdateUser(this.user);

        // Check the results of the update operation, and, if okay, fetch
        // updated results and refresh display. Otherwise, show error message
        // with the original user edits.
        ReplyStatusType rst = response.getReplyStatus();
        this.msg = rst.getMessage();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_SUCCESS) {
            // Retrieve updated user
            this.user = this.getUser(this.user.getUsername());
        }
        else {
            this.msg = rst.getExtMessage();
        }
        // Retrieve all user group records
        this.grpData = this.getUserGroupList();
    }

    private UserLogin getUser(String userName) {
        UserCriteria criteria = UserCriteria.getInstance();
        criteria.setQry_Username(userName);

        // Fetch user's recent updates to display as confirmation of
        // successful update
        AuthenticationResponse response = UserSoapRequests.callSearchUsers(criteria);
        List list = UserLoginFactory.getUserList(response.getProfile().getUserInfo());
        UserLogin user = (UserLogin) list.get(0);

        // Get message text from reply status
        ReplyStatusType rst = response.getReplyStatus();
        if (rst.getReturnCode().intValue() == GeneralConst.RC_SUCCESS && rst.getRecordCount().intValue() == 1) {
            return user;
        }
        else {
            return null;
        }
    }

    private List getUserGroupList() {
        // Get User Group list
        AuthenticationResponse response2 = UserGroupSoapRequests.callSearchAllUserGroups();

        // Setup user group list on the Request object in order to pass back
        // to JSP client.
        return UserGroupFactory.getUserGroupList(response2.getProfile().getUserGroupInfo());
    }

    /**
     * Deletes a user from the database.
     * 
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // UserApi userApi = UserFactory.createApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // userApi.inActivateUser(this.user);
        // tx.commitUOW();
        // this.msg = "User active status is cancelled";
        // }
        // catch (UserAuthenticationException e) {
        // this.msg = "UserAuthenticationException: " + e.getMessage();
        // logger.log(Level.ERROR, this.msg);
        // tx.rollbackUOW();
        // throw new ActionCommandException(this.msg);
        // }
        // finally {
        // userApi.close();
        // tx.close();
        // userApi = null;
        // tx = null;
        // }
	return;
    }

    /**
     * Restore the search results section of the User Search JSP page to its
     * previous state by using the previous search criteria to render the User
     * Search JSP page.
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
        this.sendClientSearchData();
    }

    /**
     * Gathers all data needed to populate User Application-Role page.
     * 
     * @throws ActionCommandException
     */
    protected void doAppRole() throws ActionCommandException {
	this.receiveClientData();
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // ApplicationApi appApi =
        // UserFactory.createAppApi((DatabaseConnectionBean) tx.getConnector(),
        // this.request);
        // try {
        // this.apps = appApi.getAllApps();
        //
        // // setup user criteria
        // UserLogin userCriteria = UserFactory.createUserLogin();
        // userCriteria.setLoginId(this.user.getLoginId());
        //
        // // Setup application-role criteria
        // AppRole appRoleCriteria = UserFactory.createAppRole();
        // if (this.apps != null) {
        // Application app = (Application) ((List) this.apps).get(0);
        // appRoleCriteria.setAppId(app.getAppId());
        // this.selectedApp = String.valueOf(app.getAppId());
        // }
        // this.assignedRoles = appApi.getAppRoleAssigned(userCriteria,
        // appRoleCriteria);
        // this.revokedRoles = appApi.getAppRoleRevoked(userCriteria,
        // appRoleCriteria);
        // }
        // catch (UserAuthenticationException e) {
        // throw new ActionCommandException(e);
        // }
        // finally {
        // this.sendClientData();
        // appApi.close();
        // tx.close();
        // appApi = null;
        // tx = null;
        // }
	return;
    }
    
    
    protected void doResources() throws ActionCommandException {
        // try {
        // UserResourceAccessEditAction delegate = new
        // UserResourceAccessEditAction();
        // delegate.processRequest(this.request, this.response,
        // UserResourceAccessEditAction.COMMAND_FIRSTLOAD);
        // }
        // catch (SystemException e) {
        // this.logger.log(Level.ERROR, e.getMessage());
        // throw new ActionCommandException(e);
        // }
	
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
	}
	catch (SystemException e) {
	    logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
	}
    }

    /**
     * Sends a UserLogin data object and any server messages to the user via the
     * request object.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
	this.request.setAttribute(UserConst.CLIENT_DATA_USER, this.user);
	this.request.setAttribute(UserConst.CLIENT_DATA_GROUPS, this.grpData);
	this.request.setAttribute("apps", this.apps);
	this.request.setAttribute("selectedApp", this.selectedApp);
	this.request.setAttribute("assignedRoles", this.assignedRoles);
	this.request.setAttribute("revokedRoles", this.revokedRoles);
	this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
    }

    private void sendClientSearchData() throws ActionCommandException {
        this.request.setAttribute(UserConst.CLIENT_DATA_USER, this.searchData);
    }

    // /**
    // * This method is responsible for validating a user's profile. The login
    // id,
    // * first name, last name, birth date, start date, and social security
    // number
    // * are required to have values. Also, the user's login Id is only
    // validated
    // * when the user is new.
    // *
    // * @param user
    // * {@link User} - user's credentials
    // * @throws UserMaintException
    // */
    // protected void validateUser(UserLogin user) throws UserMaintException {
    // // User's login code only needs to be validated if user is new
    // if (user.getLoginId() == 0) {
    // this.msg = "User Maintenance: Login cannot be blank";
    // if (user.getUsername() == null || user.getUsername().length() <= 0) {
    // throw new UserMaintException(this.msg);
    // }
    // }
    // // Validate First Name
    // if (user.getFirstname() == null || user.getFirstname().length() <= 0) {
    // this.msg = "User Maintenance: First Name cannot be blank";
    // throw new UserMaintException(this.msg);
    // }
    //
    // // Validate Last Name
    // if (user.getLastname() == null || user.getLastname().length() <= 0) {
    // this.msg = "User Maintenance: Last Name cannot be blank";
    // throw new UserMaintException(this.msg);
    // }
    //
    // // Validate Title
    // if (user.getSsn() == null || user.getSsn().length() <= 0) {
    // this.msg = "User Maintenance: Social Security Number cannot be blank";
    // throw new UserMaintException(this.msg);
    // }
    //
    // if (user.getBirthDate() == null) {
    // this.msg = "User Maintenance: Birth date cannot be blank";
    // throw new UserMaintException(this.msg);
    // }
    //
    // if (user.getStartDate() == null) {
    // this.msg = "User Maintenance: Start date cannot be blank";
    // throw new UserMaintException(this.msg);
    // }
    // }

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