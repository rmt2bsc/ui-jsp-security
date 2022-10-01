package com.action.user;

import java.util.ArrayList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.security.RMT2TagQueryBean;
import com.api.util.RMT2Money;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.UserLoginFactory;

/**
 * This class provides action handlers to respond to an associated controller
 * for searching, adding, deleting, and validating User profiles.
 * 
 * @author Roy Terrell
 * 
 */
public class UserSearchAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_NEWSEARCH = "User.Search.newsearch";

    private static final String COMMAND_SEARCH = "User.Search.search";

    private static final String COMMAND_LIST = "User.Search.list";

    private static final String COMMAND_EDIT = "User.Search.edit";

    private static final String COMMAND_ADD = "User.Search.add";

    private Logger logger;

    private Object data;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public UserSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger(UserSearchAction.class);
    }

    /**
     * Initializes this object using _conext and _request. This is needed in the
     * event this object is instantiated using the default constructor.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
    }

    /**
     * Processes the client's request using request, response, and command.
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

        if (command.equalsIgnoreCase(UserSearchAction.COMMAND_NEWSEARCH)) {
            this.doNewSearch();
        }
        if (command.equalsIgnoreCase(UserSearchAction.COMMAND_SEARCH)) {
            this.doSearch();
        }
        if (command.equalsIgnoreCase(UserSearchAction.COMMAND_LIST)) {
            // There is no action handler since JSP uses datasource to retrieve
            // data.
        }
        if (command.equalsIgnoreCase(UserSearchAction.COMMAND_EDIT)) {
            this.editData();
        }
        if (command.equalsIgnoreCase(UserSearchAction.COMMAND_ADD)) {
            this.addData();
        }
    }

    // /**
    // * Returns selection criteria that is sure to return an empty result set
    // * once applied to the sql that pertains to the data source of the
    // customer
    // * search page.
    // */
    // protected String doInitialCriteria(RMT2TagQueryBean _query) throws
    // ActionCommandException {
    // _query.setQuerySource("UserLoginView");
    // return "username = '!@#$%^&*()_+'";
    // }

    /**
     * Creates an instance of UserCriteria so that it may be assigned as
     * RMT2TagQueryBean's custom object property.
     *
     * @return {@link UserCriteria}
     * @throws ActionHandlerException
     *             problem occurs creating the criteria object.
     */
    protected Object doCustomInitialization() throws ActionCommandException {
        UserCriteria criteriaObj = UserCriteria.getInstance();
        return criteriaObj;

        // if (!this.isFirstTime()) {
        // try {
        // DataSourceAdapter.packageBean(this.request, criteriaObj);
        // this.setBaseView("UserLoginView");
        // this.setBaseClass("com.bean.UserLoginView");
        // } catch (SystemException e) {
        // this.msg = "Problem gathering User Search request parameters: " +
        // e.getMessage();
        // logger.log(Level.ERROR, this.msg);
        // throw new ActionHandlerException(this.msg);
        // }
        // }

    }

    /**
     * Handler method that responds to the client's request to display a new
     * User Search page.
     * 
     * @throws ActionCommandException
     */
    protected void doNewSearch() throws ActionCommandException {
        this.setFirstTime(true);
        this.startSearchConsole();
        this.data = new ArrayList<>();
        // Send empty data list to client
        this.sendClientData();
    }

    /**
     * Handler method that responds to the client's request to perform a user
     * search using the selection criteria entered by the user.
     * 
     * @throws ActionHandlerException
     */
    protected void doSearch() throws ActionCommandException {
        this.setFirstTime(false);

        // TODO: Get search parameters from request and add search parameter
        // object to session
        UserCriteria criteria = null;
        if (this.query != null && this.query.getCustomObj() != null && this.query.getCustomObj() instanceof UserCriteria) {

        }
        this.getSession().setAttribute(RMT2ServletConst.QUERY_BEAN, this.query);

        // Fetch all users for display
        AuthenticationResponse response = UserSoapRequests.callSearchAllUsers();
        this.data = UserLoginFactory.getUserList(response.getProfile().getUserInfo());

        // Get message text from reply status
        ReplyStatusType rst = response.getReplyStatus();
        this.msg = rst.getMessage();

        // Send data to client
        this.sendClientData();

        // this.buildSearchCriteria();
        // this.query = (RMT2TagQueryBean)
        // this.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);
        // this.getSession().setAttribute(RMT2ServletConst.QUERY_BEAN,
        // this.query);
    }

    /**
     * Creates a new UserLogin object which is used to add a user to the system
     * via the User Maintenance Edit page.
     * 
     * @throws ActionHandlerException
     */
    public void add() throws ActionCommandException {
        this.data = UserLoginFactory.create();
        return;
    }

    /**
     * Fetches a user login record as preparation for viewing/editing the
     * record.
     * 
     * @throws ActionHandlerException
     */
    public void edit() throws ActionCommandException {
        // DatabaseTransApi tx = DatabaseTransFactory.create();
        // UserApi userApi = UserFactory.createApi((DatabaseConnectionBean)
        // tx.getConnector(), this.request);
        // try {
        // // Get all data pertaining to user from the database.
        // this.user = (UserLogin)
        // userApi.findUserByUserName(this.user.getUsername());
        // // Reset password for security purposes
        // this.user.setPassword(UserConst.PASSWORD_GARBAGE);
        // return;
        // } catch (Exception e) {
        // logger.log(Level.ERROR, e.getMessage());
        // throw new ActionHandlerException(e.getMessage());
        // } finally {
        // userApi.close();
        // tx.close();
        // userApi = null;
        // tx = null;
        // }
    }

    /**
     * Creates a UserLogin data object using key data items selected from a list
     * of users in the request.
     * 
     * @throws ActionHandlerException
     *             When an item is not selected or when a problem occurs
     *             packaging the USerLogin object with request data
     */
    protected void receiveClientData() throws ActionCommandException {
        String rowStr = this.request.getParameter(GeneralConst.CLIENTROW_PROPERTY);
        int rowNdx = 0;

        // Client must select a row to edit.
        if (rowStr == null) {
            logger.log(Level.ERROR, RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED);
            throw new ActionCommandException(RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED,
                    RMT2SystemExceptionConst.RC_ITEM_NOT_SELECTED);
        }

        try {
            // Get index of the row that is to be processed from the
            // HttpServeltRequest object
            rowNdx = RMT2Money.stringToNumber(rowStr).intValue();

            // Retrieve values from the request object into the User object.
            this.data = UserLoginFactory.create();
            RMT2WebUtility.packageBean(this.request, this.data, rowNdx);
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Sends a UserLogin object to the client via the request object.
     * 
     * @throws ActionHandlerException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(UserConst.CLIENT_DATA_USER, this.data);
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