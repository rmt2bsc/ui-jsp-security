package com.action.approle;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.AuthConstants;
import com.SystemException;
import com.action.application.ApplicationSoapRequests;
import com.action.roles.RoleSoapRequests;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.Application;
import com.entity.ApplicationFactory;
import com.entity.RoleFactory;
import com.entity.Roles;
import com.entity.VwAppRoleFactory;
import com.entity.VwAppRoles;

/**
 * Action handler provides functionality to respond to requests pertaining to
 * the Application Roles edit page. The following request types are serviced:
 * save, delete an application role, and to navigate back to the application
 * role list page.
 * 
 * @author Roy Terrell
 * 
 */
public class AppRoleEditAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "AppRole.Edit.save";

    private static final String COMMAND_DELETE = "AppRole.Edit.delete";

    private static final String COMMAND_LIST = "AppRole.Edit.list";

    private Logger logger;

    // private ApplicationApi api;

    private Object data;

    private List appList;

    private List roleList;

    private Object selectedRole;

    private Object selectedApp;

    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public AppRoleEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("AppRoleEditAction");
    }

    /**
     * Performs post initialization and sets up an Application Api reference.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
    }

    /**
     * Processes the client's requests to save and delete changes made to an
     * application role profile, and to navigate back to application roles list
     * page.
     * 
     * @param request
     *            The HttpRequest object
     * @param response
     *            The HttpResponse object
     * @param command
     *            Comand issued by the client.
     * @Throws SystemException when an error needs to be reported.
     */
    public void processRequest(Request request, Response response, String command) throws ActionCommandException {
        super.processRequest(request, response, command);
        if (command.equalsIgnoreCase(AppRoleEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(AppRoleEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(AppRoleEditAction.COMMAND_LIST)) {
            this.doBack();
        }
    }

    /**
     * Saves an application role to the database.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // ApplicationApi api =
        // UserFactory.createAppApi((DatabaseConnectionBean) tx.getConnector(),
        // this.request);
        // int key;
        // AppRole obj = (AppRole) this.data;
        // try {
        // // Update application profile.
        // key = api.maintainAppRole(obj);
        // // Commit Changes to the database
        // tx.commitUOW();
        // this.msg = "Application Role configuration saved successfully";
        // } catch (ApplicationException e) {
        // this.msg = e.getMessage();
        // tx.rollbackUOW();
        // } finally {
        // api.close();
        // api = null;
        // tx.close();
        // tx = null;
        // }
    }

    /**
     * Navigates the user to the Application Role Search page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        RMT2TagQueryBean query = (RMT2TagQueryBean) this.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);
        AppRoleCriteria criteria = (AppRoleCriteria) query.getCustomObj();

        // Call SOAP web service to get list of application/role data
        try {
            AuthenticationResponse appResponse = ApplicationRoleSoapRequests.callGetApplicationRoles(criteria);
            ReplyStatusType rst = appResponse.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }

            List<VwAppRoles> obj = new ArrayList<>();
            if (appResponse.getProfile() != null) {
                obj = VwAppRoleFactory.create(appResponse.getProfile().getAppRoleInfo());
            }
            this.data = obj;
            this.getLookupData();
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Gather master data lists for Roles and Applications to be used by the
     * client.
     * 
     * @throws ActionCommandException
     */
    private void getLookupData() throws ActionCommandException {
        // Call SOAP web service to get complete list of applications
        try {
            AuthenticationResponse appResponse = ApplicationSoapRequests.callGetApplications();
            ReplyStatusType rst = appResponse.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            List<Application> applications = ApplicationFactory.create(appResponse.getProfile().getApplicationInfo());
            this.appList = applications;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }

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
            this.roleList = obj;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
        return;
    }

    /**
     * Obtains the selected application role from the user's request. The
     * application role is identified by its unique id and is used to obtain the
     * complete record of the application role from the database.
     * 
     * @throws ActionCommandException
     *             When a required selection was not made or an error occurrence
     *             during data retrieval.
     */
    protected void receiveClientData() throws ActionCommandException {
        int uid;
        String temp = this.getInputValue("AppRoleId", null);
        try {
            uid = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            uid = -1;
        }

        // DatabaseTransApi tx = null;
        // ApplicationApi api = null;
        // try {
        // // Retrieve an application role from the database using unique id.
        // tx = DatabaseTransFactory.create();
        // api = UserFactory.createAppApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // this.data = api.getAppRole(uid);
        // if (this.data == null) {
        // this.data = UserFactory.createAppRole();
        // }
        // // Update an application role object with user input.
        // UserFactory.packageBean(this.request, this.data);
        // } catch (Exception e) {
        // logger.log(Level.ERROR, e.getMessage());
        // throw new ActionCommandException(e.getMessage());
        // } finally {
        // api.close();
        // api = null;
        // tx.close();
        // tx = null;
        // }
    }



    /**
     * Delete an application role from the database using its unique id.
     * 
     * @param appId
     *            The id of the user group
     * @return Total number of rows deleted.
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // ApplicationApi api =
        // UserFactory.createAppApi((DatabaseConnectionBean) tx.getConnector(),
        // this.request);
        // int rc;
        // AppRole obj = (AppRole) this.data;
        // try {
        // // Update user group profile.
        // rc = api.deleteAppRole(obj.getAppRoleId());
        // // Commit Changes to the database
        // tx.commitUOW();
        // this.msg = rc +
        // " application role configuration(s) deleted successfully";
        // } catch (ApplicationException e) {
        // this.msg =
        // "Problem occurred deleting application role profile.  Additional information: "
        // + e.getMessage();
        // tx.rollbackUOW();
        // } finally {
        // api.close();
        // api = null;
        // tx.close();
        // tx = null;
        // }
    }

    /**
     * Sends a {@link com.bean.AppRole AppRole} and any server messages to the
     * user via the request object.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.data);
        this.request.setAttribute(GeneralConst.CLIENT_DATA_LIST, this.data);
        this.request.setAttribute(AuthConstants.APP_LIST, this.appList);
        this.request.setAttribute(AuthConstants.ROLE_LIST, this.roleList);
        this.request.setAttribute("selectedApp", this.selectedApp);
        this.request.setAttribute("selectedRole", this.selectedRole);
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