package com.action.resource;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.AuthConstants;
import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2SystemExceptionConst;
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
public class ResourceSearchAction extends ResourceAbstractAction implements ICommand {
    private static final String COMMAND_LIST = "Resource.Search.list";

    private static final String COMMAND_EDIT = "Resource.Search.edit";

    private static final String COMMAND_ADD = "Resource.Search.add";

    private static final String COMMAND_FETCH_ACCESS = "Resource.Search.fetchresourceaccess";

    private Logger logger;

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
        this.data = this.doList(null);
        this.sendClientData();
    }

    /**
     * Creates a new resource object which is used to add an entry to the system
     * via the Resource Maintenance Edit page.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        this.data = VwResourceFactory.create();

        // Calls SOAP web service to get complete list of resource types
        this.typeData = this.lookupResourceTypes();

        // Calls SOAP web service to get complete list of resource sub types
        this.subTypeData = this.lookupResourceSubTypes(null);
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
        // Call SOAP web service to get a single resource object
        VwResource criteria = VwResourceFactory.create();
        criteria.setRsrcId(this.selectedResourceId);
        List<VwResource> results = this.doList(criteria);
        if (results != null && results.size() > 0) {
            this.data = results.get(0);
        }

        // Calls SOAP web service to get complete list of resource types
        this.typeData = this.lookupResourceTypes();

        // Calls SOAP web service to get complete list of resource sub types
        this.subTypeData = this.lookupResourceSubTypes(null);

        this.sendClientData();
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
            String temp = this.getInputValue("RsrcId", null);
            try {
                this.selectedResourceId = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                this.selectedResourceId = -1;
            }
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
        this.request.setAttribute(AuthConstants.RESOURCE_TYPE_LIST, this.typeData);
        this.request.setAttribute(AuthConstants.RESOURCE_SUBTYPE_LIST, this.subTypeData);

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