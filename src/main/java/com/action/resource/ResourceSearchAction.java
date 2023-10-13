package com.action.resource;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.VwResource;
import com.entity.VwResourceFactory;

/**
 * Action handler provides functionality to respond to requests pertaining to
 * the resource list page. The following request types are serviced: list, add,
 * and edit a role.
 * 
 * @author Roy Terrell
 * 
 */
public class ResourceSearchAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_LIST = "Resource.Search.list";

    private static final String COMMAND_EDIT = "Resource.Search.edit";

    private static final String COMMAND_ADD = "Resource.Search.add";

    private static final String COMMAND_FETCH_ACCESS = "Resource.Search.fetchresourceaccess";

    private Logger logger;

    private Object data;

    private boolean selectionRequired;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public ResourceSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("ResourceSearchAction");
    }

    /**
     * Performs post initialization and sets up an Resource Api reference.
     * 
     * @throws SystemException
     */
    protected void init(Context _context, Request _request) throws SystemException {
        super.init(_context, _request);
    }

    /**
     * Processes the client's request using request, response, and command. Add,
     * edit, and list are the resource commands recognized.
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
        this.selectionRequired = false;
        if (command.equalsIgnoreCase(ResourceSearchAction.COMMAND_LIST)) {
            this.search();
        }
        if (command.equalsIgnoreCase(ResourceSearchAction.COMMAND_EDIT)) {
            this.selectionRequired = true;
            this.editData();
        }
        if (command.equalsIgnoreCase(ResourceSearchAction.COMMAND_ADD)) {
            this.addData();
        }
        if (command.equalsIgnoreCase(ResourceSearchAction.COMMAND_FETCH_ACCESS)) {
            this.fetchResourceAccess();
        }
    }


    /**
     * Retrieve all resource records from the database.
     * 
     * @throws ActionCommandException
     */
    protected void search() throws ActionCommandException {
        // Call SOAP web service to get complete list of resource objects
        try {
            AuthenticationResponse response = ResourceSoapRequests.callGet();
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            List<VwResource> results = VwResourceFactory.create(response.getProfile().getResourcesInfo());
            this.data = results;
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Creates a new resource object which is used to add an entry to the system
     * via the Resource Maintenance Edit page.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        // this.data = ResourceFactory.createUserResource();
        return;
    }

    /**
     * Prepares a selected resource for editing. Obtains the input data from the
     * request and maps the data to a {@link com.bean.UserResource UserResource}
     * instance.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        // if (this.data == null) {
        // return;
        // }
        //
        // // Since resource api returns resource sub-types in a List
        // collection, isolate single element.
        // if (this.data instanceof List) {
        // List list = (List) this.data;
        // if (list.size() > 1) {
        // this.msg =
        // "More than one occurrence of the selected item was found to be assoicated with the user\'s selection";
        // logger.log(Level.ERROR, this.msg);
        // throw new ActionCommandException(this.msg);
        // }
        // this.data = list.get(0);
        // }
        //
        // // Update object with user's changes.
        // try {
        // ResourceFactory.packageBean(this.request, this.data,
        // this.selectedRow);
        // }
        // catch (Exception e) {
        // logger.log(Level.ERROR, e.getMessage());
        // throw new ActionCommandException(e.getMessage());
        // }
        return;
    }

    /**
     * Obtains the selected resource from the user's request. The resource is
     * identified by its unique id and is used to obtain the complete record of
     * the resource from the database.
     * 
     * @throws ActionCommandException
     *             When a required selection was not made or an error occurrence
     *             during data retrieval.
     */
    protected void receiveClientData() throws ActionCommandException {
        if (this.selectionRequired) {
            // Client must select a row to edit.
            if (this.selectedRow < 0) {
                logger.log(Level.ERROR, RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED);
                throw new ActionCommandException(RMT2SystemExceptionConst.MSG_ITEM_NOT_SELECTED,
                        RMT2SystemExceptionConst.RC_ITEM_NOT_SELECTED);
            }
            int uid;
            String temp = this.getInputValue("RsrcId", null);
            try {
                uid = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                uid = -1;
            }

            // DatabaseTransApi tx = DatabaseTransFactory.create();
            // ResourceApi api =
            // ResourceFactory.createApi((DatabaseConnectionBean)
            // tx.getConnector(), this.request);
            // try {
            // // Retrieve resource from the database using unique id.
            // UserResource res = ResourceFactory.createUserResource();
            // res.setRsrcId(uid);
            // this.data = api.get(res);
            // }
            // catch (Exception e) {
            // logger.log(Level.ERROR, e.getMessage());
            // throw new ActionCommandException(e.getMessage());
            // }
            // finally {
            // api.close();
            // tx.close();
            // api = null;
            // tx = null;
            // }
        }
    }

    /**
     * Sends a {@link com.bean.UserResource UserResource} object to the client
     * via the request object.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.data);
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