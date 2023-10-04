package com.action.user;

import org.apache.log4j.Logger;

import com.SystemException;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.jsp.action.ActionHandlerException;
import com.api.security.RMT2TagQueryBean;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.UserLogin;



/**
 * This class provides action handlers that services the requests coming from the User Resource Edit Page.
 * 
 * @author Roy Terrell
 * 
 */
public class UserResourceAccessEditAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "UserResourceAccess.Edit.save";

    private static final String COMMAND_BACK = "UserResourceAccess.Edit.back";
    
    public static final String COMMAND_FIRSTLOAD = "firsttimeload";

    private Logger logger;

//    private UserApi userApi;
//
//    private ResourceApi api;

    private UserLogin user;

    private int typeId;
    
    private int subTypeId;

    private String assignedRsrc[];

    private String revokedRsrc[];

    private Object assignedRsrcObjs;

    private Object revokedRsrcObjs;

    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public UserResourceAccessEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("UserResourceAccessEditAction");
    }

    /**
     * Initializes the UserApi and the ResourceApi associated with this class.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
//        this.userApi = UserFactory.createApi(this.dbConn, this.request);
//        this.api = ResourceFactory.createApi(this.dbConn, this.request);
    }

    /**
     * Processes the client's requests to save the changes made to a user
     * resource entity, the loading of a user resource, and to navigate back to the User Search
     * page.
     * 
     * @param request
     *            The HttpRequest object
     * @param response
     *            The HttpResponse object
     * @param command
     *            Comand issued by the client.
     * @Throws SystemException
     *             when an error needs to be reported.
     */
    public void processRequest(Request request, Response response, String command) throws ActionCommandException {
        super.processRequest(request, response, command);
        this.query = (RMT2TagQueryBean) this.request.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);

        if (command.equalsIgnoreCase(UserResourceAccessEditAction.COMMAND_FIRSTLOAD)) {
            this.doLoad();
        }
        if (command.equalsIgnoreCase(UserResourceAccessEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(UserResourceAccessEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    /**
     * Gather user resource data to be displayed on a first-time basis and sends
     * the results to the caller to be rendered.
     * 
     * @throws ActionCommandException
     */
    protected void doLoad() throws ActionCommandException {
	this.receiveClientData();
        // this.getUserResourceData();
	this.sendClientData();
    }
    
    
    /**
     * Updates a user resource entity by persisting changes to the database.
     * This method is used for adding and modifying user resourcess.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // ResourceApi api = ResourceFactory.createApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // int rows;
        // try {
        // rows = api.maintainUserResourceAccess(user.getUsername(),
        // assignedRsrc, revokedRsrc);
        // // Commit Changes to the database
        // tx.commitUOW();
        // this.msg = rows +
        // " User Resource Access items were added successfully";
        // // Refresh client presentation data
        // this.getUserResourceData();
        // }
        // catch (ResourceException e) {
        // tx.rollbackUOW();
        // throw new ActionHandlerException(e);
        // }
        // finally {
        // api.close();
        // tx.close();
        // api = null;
        // tx = null;
        // }
    }

    /**
     * Navigates the user to the previous page.
     * 
     * @throws ActionHandlerException
     */
    protected void doBack() throws ActionCommandException {
        this.receiveClientData();
        this.sendClientData();
        return;
    }

    /**
     * Gathers data from the user's request.
     * 
     * @throws ActionHandlerException
     */
    protected void receiveClientData() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // UserApi userApi = UserFactory.createApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // // Retrieve values from the request object into the User object.
        // try {
        // this.user = UserFactory.createUserLogin();
        // UserFactory.packageBean(this.request, this.user);
        // // Get all data pertaining to user from the database.
        // this.user = (UserLogin)
        // userApi.findUserByUserName(this.user.getUsername());
        // }
        // catch (Exception e) {
        // logger.log(Level.ERROR, e.getMessage());
        // throw new ActionHandlerException(e.getMessage());
        // }
        // finally {
        // userApi.close();
        // tx.close();
        // userApi = null;
        // tx = null;
        // }
        //
        // // Get resource type id.
        // try {
        // this.typeId =
        // Integer.parseInt(this.request.getParameter("ResourceTypeId"));
        // }
        // catch (NumberFormatException e) {
        // this.typeId = 0;
        // }
        //
        // // Get resource sub type id
        // try {
        // this.subTypeId =
        // Integer.parseInt(this.request.getParameter("ResourceSubtypeId"));
        // }
        // catch (NumberFormatException e) {
        // this.subTypeId = 0;
        // }
        //
        // // Get resoruce selections
        // this.revokedRsrc = this.request.getParameterValues("RevokedRsrcId");
        // this.assignedRsrc =
        // this.request.getParameterValues("AssignedRsrcId");
    }

    /**
     * Sends a data to the client via the request object pertaining to User Login, resource type 
     * id, resource sub type id, a list of assigned resources, a list of revoked resources, and 
     * any messages.
     * 
     * @throws ActionHandlerException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(UserConst.CLIENT_DATA_USER, this.user);
        this.request.setAttribute("typeid", String.valueOf(this.typeId));
        this.request.setAttribute("subtypeid", String.valueOf(this.subTypeId));
        this.request.setAttribute("assignedRsrc", this.assignedRsrcObjs);
        this.request.setAttribute("revokedRsrc", this.revokedRsrcObjs);
        this.request.setAttribute(RMT2ServletConst.REQUEST_MSG_INFO, this.msg);
    }

    
    /**
     * Gathers all data needed to populate User Resource page 
     * as a confirmation to the <i>save</i> request.
     * 
     * @throws ActionHandlerException
     */
    private void getUserResourceData() throws ActionHandlerException {
        if (this.user == null) {
            this.msg = "Unable to identify user or user instance is null or invalid";
            logger.error(this.msg);
            throw new ActionHandlerException(this.msg);
        }
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // ResourceApi api = ResourceFactory.createApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // // Setup serach criteria
        // VwUserResourceAccess criteria = ResourceFactory.createUserAccess();
        // criteria.setUserUid(this.user.getLoginId());
        // criteria.setResrcSecured(1);
        // criteria.setRsrcTypeId(this.typeId);
        // criteria.setRsrcSubtypeId(this.subTypeId);
        // this.assignedRsrcObjs = api.getUserResourceAssigned(criteria);
        // this.revokedRsrcObjs = api.getUserResourceRevoked(criteria);
        // }
        // catch (DatabaseException e) {
        // throw new ActionHandlerException(e);
        // }
        // finally {
        // api.close();
        // tx.close();
        // api = null;
        // tx = null;
        // }
        return;
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