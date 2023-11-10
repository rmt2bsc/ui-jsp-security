package com.action.application;

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
import com.entity.Application;
import com.entity.ApplicationFactory;

/**
 * Action handler provides functionality to respond to requests pertaining 
 * to the application edit page.  The following request types are 
 * serviced: save application, delete an application, and back.
 * 
 * @author Roy Terrell
 * 
 */
public class AppEditAction extends AbstractActionHandler implements ICommand {
    private static final String COMMAND_SAVE = "App.Edit.save";
    
    private static final String COMMAND_DELETE = "App.Edit.delete";

    private static final String COMMAND_BACK = "App.Edit.back";

    private Logger logger;

    private Object data;

    /**
     * Default constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public AppEditAction() throws SystemException {
	super();
	logger = Logger.getLogger("UserEditAction");
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
     * Processes the client's requests to save changes made to an application
     * profile, delete an application profile, and to navigate back to
     * Application List page.
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
	if (command.equalsIgnoreCase(AppEditAction.COMMAND_SAVE)) {
	    this.saveData();
	}
	if (command.equalsIgnoreCase(AppEditAction.COMMAND_DELETE)) {
 	    this.deleteData();
	}
	if (command.equalsIgnoreCase(AppEditAction.COMMAND_BACK)) {
	    this.doBack();
	}
    }

    /**
     * Updates an application profile by persisting changes to the database.
     * This method is used for adding and modifying application profiles.
     * 
     * @throws ActionCommandException
     */
    public void save() throws ActionCommandException {
        // Call SOAP web service to persist changes made to the Application
        // object.
        try {
         // UI-37: added login id and session id parameters to the callSave
            // method invocation
            AuthenticationResponse appResponse = ApplicationSoapRequests.callUpdateApplication((Application) this.data,
                    this.loginId,
                    this.session.getId());
            ReplyStatusType rst = appResponse.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                this.msg = rst.getMessage();
                return;
            }
            List<Application> applications = ApplicationFactory.create(appResponse.getProfile().getApplicationInfo());
            this.data = applications.get(0);
            this.sendClientData();
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Delete an application from the database using the id of the application.
     * 
     * @return Total number of rows deleted.
     * @throws ApplicationException
     */
    public void delete() throws ActionCommandException {
        // Call SOAP web service to persist changes made to the Application
        // object.
        try {
            AuthenticationResponse appResponse = ApplicationSoapRequests.callDeleteApplication((Application) this.data);
            ReplyStatusType rst = appResponse.getReplyStatus();
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
     * Navigates the user to the Application List page.
     * 
     * @throws ActionCommandException
     */
    protected void doBack() throws ActionCommandException {
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
     * Gathers data from the user's request and packages the data into a
     * Application object.
     * 
     * @throws ActionCommandException
     */
    protected void receiveClientData() throws ActionCommandException {
        try {
            // Retrieve application from the database using unique id.
            this.data = ApplicationFactory.create();
            // Update application object with user input.
            RMT2WebUtility.packageBean(this.request, this.data);
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }
    
    /**
     * Sends an {@link com.bean.Application Application} data object and any
     * server messages to the user via the request object.
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