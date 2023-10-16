package com.action.resource;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.AuthConstants;
import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.security.authentication.web.AuthenticationException;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.ResourceSubTypeFactory;
import com.entity.UserResource;
import com.entity.UserResourceFactory;
import com.entity.UserResourceSubtype;


/**
 * Action handler provides functionality to respond to requests pertaining to
 * the Resource edit page. The following request types are serviced: save and
 * delete a resource, and to navigate back to the resource list page.
 * 
 * @author Roy Terrell
 * 
 */
public class ResourceEditAction extends ResourceAbstractAction implements ICommand {
    private static final String COMMAND_SAVE = "Resource.Edit.save";

    private static final String COMMAND_DELETE = "Resource.Edit.delete";

    private static final String COMMAND_BACK = "Resource.Edit.back";

    private Logger logger;



    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public ResourceEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("ResourceEditAction");
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
     * Processes the client's requests to save and delete changes made to a
     * resource profile, and to navigate back to resource list page.
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
        if (command.equalsIgnoreCase(ResourceEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(ResourceEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(ResourceEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    /**
     * This method is used for adding and modifying resource profiles. Resource
     * changes are persisted to the database.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // Calls SOAP web service to persist changes
        try {
            AuthenticationResponse response = ResourceSoapRequests.callUpdate((UserResource) this.data);
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                throw new ActionCommandException(rst.getMessage());
            }
        } catch (AuthenticationException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }

        // Calls SOAP web service to get complete list of resource types
        this.typeData = this.lookupResourceTypes();

        // Calls SOAP web service to get complete list of resource sub types and
        // to highlight the selected option
        int selectResourceTypeId = ((UserResource) this.data).getRsrcTypeId();
        UserResourceSubtype criteria = ResourceSubTypeFactory.create();
        criteria.setRsrcTypeId(selectResourceTypeId);
        this.subTypeData = this.lookupResourceSubTypes(criteria);
    }

    /**
     * Delete a resource from the database using its unique id.
     * 
     * @return Total number of rows deleted.
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // Calls SOAP web service to delete resource
        try {
            AuthenticationResponse response = ResourceSoapRequests.callDelete((UserResource) this.data);
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                throw new ActionCommandException(rst.getMessage());
            }
        } catch (AuthenticationException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }

        // Calls SOAP web service to get complete list of resource types
        this.typeData = this.lookupResourceTypes();

        // Calls SOAP web service to get complete list of resource sub types and
        // to highlight the selected option
        int selectResourceTypeId = ((UserResource) this.data).getRsrcTypeId();
        UserResourceSubtype criteria = ResourceSubTypeFactory.create();
        criteria.setRsrcTypeId(selectResourceTypeId);
        this.subTypeData = this.lookupResourceSubTypes(criteria);
    }

    /**
     * Navigates the user to the Resource List page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
        this.data = this.doList(null);
        this.sendClientData();
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
        try {
            // Retrieve resource sub type data from the JSP form.
            this.data = UserResourceFactory.createUserResource();
            // Update application object with user input.
            RMT2WebUtility.packageBean(this.request, this.data);
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Sends a {@link com.bean.UserResource UserResource} and any server
     * messages to the user via the request object.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.data);
        this.request.setAttribute(AuthConstants.RESOURCE_TYPE_LIST, this.typeData);
        this.request.setAttribute(AuthConstants.RESOURCE_SUBTYPE_LIST, this.subTypeData);
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