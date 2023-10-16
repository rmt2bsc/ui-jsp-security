package com.action.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.action.groups.UserGroupSoapRequests;
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
import com.entity.UserGroupFactory;
import com.entity.UserLogin;
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
    private static final String COMMAND_FETCH_ACCESS = "Resource.Search.fetchresourceaccess";
    private static final Logger logger = Logger.getLogger(UserSearchAction.class);
    private Object search;
    private Object data;
    private Object grpData;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public UserSearchAction() throws SystemException {
        super();
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
        if (command.equalsIgnoreCase(UserSearchAction.COMMAND_FETCH_ACCESS)) {
            this.fetchResourceAccess();
        }
    }

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
        this.search = new ArrayList<>();
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

        // Get search parameters from request and add search parameter
        // object to session
        RMT2TagQueryBean query = (RMT2TagQueryBean) this.getSession().getAttribute(RMT2ServletConst.QUERY_BEAN);
        UserCriteria criteria = (UserCriteria) query.getCustomObj();
        RMT2WebUtility.packageBean(this.request, criteria);

        // Fetch all users for display
        AuthenticationResponse response = UserSoapRequests.callSearchUsers(criteria);
        this.search = UserLoginFactory.getUserList(response.getProfile().getUserInfo());

        // Get message text from reply status
        ReplyStatusType rst = response.getReplyStatus();
        this.msg = rst.getMessage();

        // Send data to client
        this.sendClientData();
    }

    /**
     * Creates a new UserLogin object which is used to add a user to the system
     * via the User Maintenance Edit page.
     * 
     * @throws ActionHandlerException
     */
    public void add() throws ActionCommandException {
        // Retrieve User data
        this.data = UserLoginFactory.create();

        // Retrieve all user group records
        this.grpData = this.getUserGroupList();
        return;
    }

    /**
     * Fetches a user login record as preparation for viewing/editing the
     * record.
     * 
     * @throws ActionHandlerException
     */
    public void edit() throws ActionCommandException {
        UserLogin user = (UserLogin) this.data;
        String userName = user.getUsername();
        UserCriteria criteria = UserCriteria.getInstance();
        criteria.setQry_Username(userName);

        // Fetch all users for display
        AuthenticationResponse response = UserSoapRequests.callSearchUsers(criteria);
        List list = UserLoginFactory.getUserList(response.getProfile().getUserInfo());
        this.data = list.get(0);

        // Get message text from reply status
        ReplyStatusType rst = response.getReplyStatus();
        this.msg = rst.getMessage();

        // Retrieve all user group records
        this.grpData = this.getUserGroupList();
    }

    private List getUserGroupList() {
        // Get User Group list
        AuthenticationResponse response2 = UserGroupSoapRequests.callSearchAllUserGroups();

        // Setup user group list on the Request object in order to pass back
        // to JSP client.
        return UserGroupFactory.getUserGroupList(response2.getProfile().getUserGroupInfo());
    }

    private void fetchResourceAccess() throws ActionCommandException {
        // SoapClientWrapper client = new SoapClientWrapper();
        // ObjectFactory f = new ObjectFactory();
        // try {
        // RMT2SessionBean userSession = (RMT2SessionBean)
        // this.request.getSession().getAttribute(RMT2ServletConst.SESSION_BEAN);
        // RQAuthenticationUserResourceAccess ws =
        // f.createRQAuthenticationUserResourceAccess();
        // HeaderType header =
        // PayloadFactory.createHeader("RQ_authentication_user_resource_access",
        // "SYNC", "REQUEST", userSession.getLoginId());
        // ws.setHeader(header);
        //
        // String loginId = this.request.getParameter("UserRsrcLoginId");
        // String temp = this.request.getParameter("ResourceTypeId");
        // BigInteger rsrcTypeId = (temp == null ? BigInteger.valueOf(0) :
        // BigInteger.valueOf(Integer.parseInt(temp)));
        // temp = this.request.getParameter("ResourceSubtypeId");
        // BigInteger rsrcSubTypeId = (temp == null ? BigInteger.valueOf(0) :
        // BigInteger.valueOf(Integer.parseInt(temp)));
        //
        // ws.setLoginId(loginId);
        // ws.setRsrcTypeId(rsrcTypeId);
        // ws.setRsrcSubtypeId(rsrcSubTypeId);
        // String msg =
        // com.api.messaging.ResourceFactory.getJaxbMessageBinder().marshalMessage(ws);
        // SOAPMessage resp = client.callSoap(msg);
        // if (client.isSoapError(resp)) {
        // String errMsg = client.getSoapErrorMessage(resp);
        // logger.error(errMsg);
        // return;
        // }
        // RSAuthenticationUserResourceAccess soapResp =
        // (RSAuthenticationUserResourceAccess) client.getSoapResponsePayload();
        // String xml =
        // com.api.messaging.ResourceFactory.getJaxbMessageBinder().marshalMessage(soapResp,
        // false);
        //
        // // Setup XML response on the user's request instance so that the
        // controller can send back to the client
        // this.request.setAttribute(RMT2ServletConst.RESPONSE_NONJSP_DATA,
        // xml);
        // logger.info(xml);
        // }
        // catch (MessageException e) {
        // throw new ActionCommandException(e);
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
        this.request.setAttribute(UserConst.CLIENT_DATA_SEARCH, this.search);
        this.request.setAttribute(UserConst.CLIENT_DATA_USER, this.data);
        this.request.setAttribute(UserConst.CLIENT_DATA_GROUPS, this.grpData);
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