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
import com.entity.ResourceTypeFactory;
import com.entity.UserResourceType;

/**
 * Action handler provides functionality to respond to requests pertaining to
 * the resource type list page. The following request types are serviced: list,
 * add, and edit a role.
 * 
 * @author Roy Terrell
 * 
 */
public class TypeSearchAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_LIST = "ResourceType.Search.list";

    private static final String COMMAND_EDIT = "ResourceType.Search.edit";

    private static final String COMMAND_ADD = "ResourceType.Search.add";

    private Logger logger;

    private Object data;

    private boolean selectionRequired;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public TypeSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("TypeSearchAction");
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
     * edit, and list are the resource type commands recognized.
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
        if (command.equalsIgnoreCase(TypeSearchAction.COMMAND_LIST)) {
            this.search();
        }
        if (command.equalsIgnoreCase(TypeSearchAction.COMMAND_EDIT)) {
            this.selectionRequired = true;
            this.editData();
        }
        if (command.equalsIgnoreCase(TypeSearchAction.COMMAND_ADD)) {
            this.addData();
        }
    }

    /**
     * Retrieve all resource type records from the database.
     * 
     * @throws ActionCommandException
     */
    protected void search() throws ActionCommandException {
        // Call SOAP web service to get complete list of resource types
        try {
            AuthenticationResponse response = ResourceTypeSoapRequests.callGet();
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            List<UserResourceType> results = ResourceTypeFactory.create(response.getProfile().getResourcesInfo());
            this.data = results;
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }

    }

    /**
     * Creates a new resource type object which is used to add an entry to the
     * system via the Resource Type Maintenance Edit page.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        this.data = ResourceTypeFactory.create();
        return;
    }

    /**
     * Prepares a selected resource type for editing. Obtains the input data
     * from the request and maps the data to a {@link com.bean.UserResourceType
     * UserResourceType} instance.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {

        return;
    }

    /**
     * Obtains the selected resource type from the user's request. The resource
     * type is identified by its unique id and is used to obtain the complete
     * record of the resource type from the database.
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

            UserResourceType obj = ResourceTypeFactory.create();
            int uid;
            String temp = this.getInputValue("RsrcTypeId", null);
            try {
                uid = Integer.parseInt(temp);
                obj.setRsrcTypeId(uid);
            } catch (NumberFormatException e) {
                obj.setRsrcTypeId(0);
            }

            temp = this.getInputValue("Description", null);
            obj.setDescription(temp);
            this.data = obj;
        }
    }

    /**
     * Sends a {@link com.bean.UserResourceType UserResourceType} object to the
     * client via the request object.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.data);
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