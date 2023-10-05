package com.action.application;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.constants.RMT2SystemExceptionConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.util.RMT2Money;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.api.web.Response;
import com.entity.Application;
import com.entity.ApplicationFactory;

/**
 * Action handler provides functionality to respond to requests pertaining to
 * the application list page. The following request types are serviced: list
 * applications, add and application, and edit an application.
 * 
 * @author Roy Terrell
 * 
 */
public class AppSearchAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_LIST = "App.Search.list";

    private static final String COMMAND_EDIT = "App.Search.edit";

    private static final String COMMAND_ADD = "App.Search.add";

    private Logger logger;

    // private ApplicationApi api;

    private Object data;

    private int selectedAppId;

    private boolean selectionRequired;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public AppSearchAction() throws SystemException {
        super();
        logger = Logger.getLogger("AppSearchAction");
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
     * Processes the client's request using request, response, and command. Add,
     * edit, delete, and list application are the commands recognized.
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
        if (command.equalsIgnoreCase(AppSearchAction.COMMAND_LIST)) {
            this.search();
        }
        if (command.equalsIgnoreCase(AppSearchAction.COMMAND_EDIT)) {
            this.selectionRequired = true;
            this.editData();
        }
        if (command.equalsIgnoreCase(AppSearchAction.COMMAND_ADD)) {
            this.addData();
        }
    }

    /**
     * Retrieve all application records from the database.
     * 
     * @throws ActionCommandException
     */
    protected void search() throws ActionCommandException {
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
            this.data = applications;
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Creates a new Application object which is used to add an application
     * entry to the system via the Application Maintenance Edit page.
     * 
     * @throws ActionCommandException
     */
    public void add() throws ActionCommandException {
        this.data = ApplicationFactory.create();
        return;
    }

    /**
     * Obtains the input data from the request and maps the data to an
     * {@link com.bean.Application Application} object. Lastly, the application
     * object is prepared to be forwarded to the next JSP for processing.
     * 
     * @throws ActionCommandException
     */
    public void edit() throws ActionCommandException {
        Application app = ApplicationFactory.create();
        app.setAppId(this.selectedAppId);
        String temp = this.getInputValue("Name", null);
        app.setName(temp);
        temp = this.getInputValue("Description", null);
        app.setDescription(temp);
        temp = this.getInputValue("Status", null);
        if (RMT2Money.isNumeric(temp)) {
            app.setActive(Integer.valueOf(temp));
        }

        this.data = app;
        return;
    }

    /**
     * Obtains the application unique id data from the request which was
     * selected by the user. The unique id is used to obtain the application
     * record from the database and to map the data to its associated object.
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
            String temp = this.getInputValue("AppId", null);
            try {
                this.selectedAppId = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                this.selectedAppId = -1;
            }
        }
    }

    /**
     * Sends an application object to the client via the request object.
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