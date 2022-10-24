package com.action.user;

import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.velocity.runtime.resource.ResourceFactory;

import com.SystemException;
import com.api.config.old.ProviderConfig;
import com.api.config.old.ProviderConnectionException;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.messaging.MessageException;
import com.api.messaging.webservice.http.client.HttpClientMessageException;
import com.api.messaging.webservice.http.client.HttpClientResourceFactory;
import com.api.messaging.webservice.http.client.HttpMessageSender;
import com.api.persistence.db.DatabaseConnectionBean;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.UserLogin;


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
        if (command.equalsIgnoreCase(UserAppRoleEditAction.COMMAND_USER_APP_ROLES)) {
            this.getUserRoleData();
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
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // ApplicationApi appApi =
        // UserFactory.createAppApi((DatabaseConnectionBean) tx.getConnector(),
        // this.request);
        // int rows;
        // try {
        // rows = appApi.maintainUserAppRole(this.user.getUsername(),
        // this.appId, this.assignedRoles, this.revokedRoles);
        // // Commit Changes to the database
        // tx.commitUOW();
        // this.msg = rows + "User Application Roles were added successfully";
        // // Refresh client presentation data
        // this.getAllUserAppRoles();
        // }
        // catch (ApplicationException e) {
        // tx.rollbackUOW();
        // throw new ActionCommandException(e);
        // }
        // finally {
        // appApi.close();
        // tx.close();
        // appApi = null;
        // tx = null;
        // }
    }

    /**
     * Navigates the user to the previous page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        this.receiveClientData();
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
	DatabaseTransApi tx = DatabaseTransFactory.create();
	UserApi userApi = UserFactory.createApi((DatabaseConnectionBean) tx.getConnector(), this.request);
        // Retrieve values from the request object into the User object.
        try {
            this.user = UserFactory.createUserLogin();
            UserFactory.packageBean(this.request, this.user);
            // Get all data pertaining to user from the database.
            this.user = (UserLogin) userApi.findUserByUserName(this.user.getUsername());
        }
        catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
        finally {
            userApi.close();
	    tx.close();
	    userApi = null;
	    tx = null;
	}

        // Get application id.
        try {
            this.appId = Integer.parseInt(this.request.getParameter("ApplicationId"));
        }
        catch (NumberFormatException e) {
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
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
    }

    
    /**
     * Gathers all data needed to populate User Application-Role page as a
     * confirmation to the <i>save</i> request.
     * 
     * @throws ActionCommandException
     */
    private void getAllUserAppRoles() throws ActionCommandException {
	DatabaseTransApi tx = DatabaseTransFactory.create();
	ApplicationApi appApi = UserFactory.createAppApi((DatabaseConnectionBean) tx.getConnector(), this.request);
        try {
            this.apps = appApi.getAllApps();

            // setup user criteria
            UserLogin userCriteria = UserFactory.createUserLogin();
            userCriteria.setLoginId(this.user.getLoginId());

            // Setup application-role criteria
            AppRole appRoleCriteria = UserFactory.createAppRole();
    	    // Get object that matches the current selected application            
            if (this.apps != null) {
        	// Initialize app to be the first element in the event 
        	// the select application object is not found.
        	Application app = (Application) ((List <Application>) this.apps).get(0);
        	int ndx = 0;
        	for (Object obj : (List <Application>) this.apps) {
        	    if (((Application) obj).getAppId() == this.appId) {
        		app = (Application) ((List <Application>) this.apps).get(ndx);
        		break;
        	    }
        	    ndx++;
        	}
                appRoleCriteria.setAppId(app.getAppId());
            }
            this.assignedRoleObjs = appApi.getAppRoleAssigned(userCriteria, appRoleCriteria);
            this.revokedRoleObjs = appApi.getAppRoleRevoked(userCriteria, appRoleCriteria);
        }
        catch (UserAuthenticationException e) {
            throw new ActionCommandException(e);
        }
        finally {
            appApi.close();
	    tx.close();
	    appApi = null;
	    tx = null;
	}
        return;
    }

    private void getUserRoleData() throws ActionCommandException {
      HttpMessageSender client = HttpClientResourceFactory.getHttpInstance();
      ProviderConfig config;
      try {
          config = ResourceFactory.getHttpConfigInstance();
          client.connect(config);
          Object result = client.sendMessage(this.request);
          this.request.setAttribute(RMT2ServletConst.RESPONSE_NONJSP_DATA, result);
          return;
      }
       catch (MessagingException e) {
           e.printStackTrace();
            throw new ActionCommandException(e);
       }
       catch (ProviderConnectionException e) {
           e.printStackTrace();
            throw new ActionCommandException(e);
       }
       catch (HttpClientMessageException e) {
           e.printStackTrace();
            throw new ActionCommandException(e);
       }
       catch (MessageException e) {
           e.printStackTrace();
            throw new ActionCommandException(e);
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