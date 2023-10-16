package com.action.resource;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.ReplyStatusType;

import com.SystemException;
import com.api.constants.GeneralConst;
import com.api.jsp.action.AbstractActionHandler;
import com.api.security.authentication.web.AuthenticationException;
import com.api.web.ActionCommandException;
import com.api.web.Context;
import com.api.web.ICommand;
import com.api.web.Request;
import com.entity.ResourceTypeFactory;
import com.entity.UserResourceType;
import com.entity.VwResource;
import com.entity.VwResourceFactory;
import com.entity.VwResourceType;
import com.entity.VwResourceTypeFactory;

/**
 * Resource action handler provides common functionality to respond to client
 * requests.
 * 
 * @author Roy Terrell
 * 
 */
public abstract class ResourceAbstractAction extends AbstractActionHandler implements ICommand {

    private Logger logger;

    protected Object data;

    protected Object typeData;

    protected Object subTypeData;

    protected int selectedResourceId;

    protected boolean selectionRequired;

    /**
     * Default class constructor responsible for initializing the logger.
     * 
     * @throws SystemException
     */
    public ResourceAbstractAction() throws SystemException {
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
     * Calls SOAP web service to get complete list of resource records
     * 
     * @param criteria
     *            {@link VwResource} Contains data items that is used to build
     *            selection criteria. Send a null instance to return all
     *            resources data records.
     * @return List of {@linkplain VwResource}
     * @throws ActionCommandException
     *             when SOAP call fails invocation or when the message handlder
     *             returns a critical processing error.
     */
    protected List<VwResource> doList(VwResource criteria) throws ActionCommandException {
        try {
            AuthenticationResponse response = ResourceSoapRequests.callGet(criteria);
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                throw new ActionCommandException(rst.getMessage());
            }
            List<VwResource> results = VwResourceFactory.create(response.getProfile().getResourcesInfo());
            return results;
        } catch (AuthenticationException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Calls SOAP web service to get complete list of resource types
     * 
     * @return List of {@link UserResourceType}
     * @throws ActionCommandException
     *             when SOAP call fails invocation or when the message handlder
     *             returns a critical processing error.
     */
    protected List<UserResourceType> lookupResourceTypes() throws ActionCommandException {
        try {
            AuthenticationResponse response = ResourceTypeSoapRequests.callGet();
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                throw new ActionCommandException(rst.getMessage());
            }
            List<UserResourceType> results = ResourceTypeFactory.create(response.getProfile().getResourcesInfo());
            return results;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }

    /**
     * Calls SOAP web service to get complete list of resource sub types
     * 
     * @return List of {@linkplain VwResourceType}
     * @throws ActionCommandException
     *             when SOAP call fails invocation or when the message handlder
     *             returns a critical processing error.
     */
    protected List<VwResourceType> lookupResourceSubTypes() throws ActionCommandException {
        try {
            AuthenticationResponse response = ResourceSubTypeSoapRequests.callGet();
            ReplyStatusType rst = response.getReplyStatus();
            this.msg = rst.getMessage();
            if (rst.getReturnCode().intValue() == GeneralConst.RC_FAILURE) {
                throw new ActionCommandException(rst.getMessage());
            }
            List<VwResourceType> results = VwResourceTypeFactory.create(response.getProfile().getResourcesInfo());
            return results;
        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new ActionCommandException(e.getMessage());
        }
    }
}