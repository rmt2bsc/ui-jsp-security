package com.action.resource;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2ServletConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.api.web.util.RMT2WebUtility;
import com.entity.ResourceTypeFactory;
import com.entity.UserResourceType;

/**
 * Action handler provides functionality to respond to requests pertaining to
 * the Resource Type edit page. The following request types are serviced: save
 * and delete a resource type, and to navigate back to the role list page.
 * 
 * @author Roy Terrell
 * 
 */
public class TypeEditAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "ResourceType.Edit.save";

    private static final String COMMAND_DELETE = "ResourceType.Edit.delete";

    private static final String COMMAND_BACK = "ResourceType.Edit.back";

    private Logger logger;

    private Object data;

    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public TypeEditAction() throws SystemException {
        super();
        logger = Logger.getLogger("TypeEditAction");
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
     * resource type profile, and to navigate back to roles list page.
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
        if (command.equalsIgnoreCase(TypeEditAction.COMMAND_SAVE)) {
            this.saveData();
        }
        if (command.equalsIgnoreCase(TypeEditAction.COMMAND_DELETE)) {
            this.deleteData();
        }
        if (command.equalsIgnoreCase(TypeEditAction.COMMAND_BACK)) {
            this.doBack();
        }
    }

    /**
     * This method is used for adding and modifying resource type profiles.
     * Resource type changes are persisted to the database.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // Call SOAP web service to update a resource type
        try {
            AuthenticationResponse response = ResourceTypeSoapRequests.callUpdate((UserResourceType) this.data);
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            List<UserResourceType> results = ResourceTypeFactory.create(response.getProfile().getResourcesInfo());
            this.data = results.get(0);
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Delete a resource type from the database using its unique id.
     * 
     * @return Total number of rows deleted.
     * @throws ActionCommandException
     */
    public void delete() throws ActionCommandException {
        // Call SOAP web service to delete a resource types
        try {
            AuthenticationResponse response = ResourceTypeSoapRequests.callDelete((UserResourceType) this.data);
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Navigates the user to the Resource Type List page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
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
     * Obtains the selected resource type from the user's request. The resource
     * type is identified by its unique id and is used to obtain the complete
     * record of the security role from the database.
     * 
     * @throws ActionCommandException
     *             When a required selection was not made or an error occurrence
     *             during data retrieval.
     */
    protected void receiveClientData() throws ActionCommandException {
        try {
            // Retrieve application from the database using unique id.
            this.data = ResourceTypeFactory.create();
            // Update application object with user input.
            RMT2WebUtility.packageBean(this.request, this.data);
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Sends a {@link com.bean.UserResourceType UserResourceType} and any server
     * messages to the user via the request object.
     * 
     * @throws ActionCommandException
     */
    protected void sendClientData() throws ActionCommandException {
        this.request.setAttribute(GeneralConst.CLIENT_DATA_RECORD, this.data);
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