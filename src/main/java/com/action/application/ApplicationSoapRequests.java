package com.action.application;

import java.util.Date;

import org.apache.log4j.Logger;
import org.rmt2.constants.ApiHeaderNames;
import org.rmt2.constants.ApiTransactionCodes;
import org.rmt2.jaxb.ApplicationType;
import org.rmt2.jaxb.AuthCriteriaGroupType;
import org.rmt2.jaxb.AuthProfileGroupType;
import org.rmt2.jaxb.AuthenticationRequest;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.HeaderType;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.jaxb.UserAppRolesCriteriaType;
import org.rmt2.util.HeaderTypeBuilder;

import com.AuthConstants;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.security.authentication.web.AuthenticationException;
import com.entity.Application;

/**
 * Help class for constructing and invoking SOAP calls pertaining to the
 * Application related security entities.
 * 
 * @author Roy Terrell
 *
 */
public class ApplicationSoapRequests {
    private static final Logger logger = Logger.getLogger(ApplicationSoapRequests.class);
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging";

    /**
     * SOAP call to fetch all applications.
     * 
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callApplications() throws AuthenticationException {
        // Retrieve all user group records from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_APPLICATION_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthCriteriaGroupType apgt = fact.createAuthCriteriaGroupType();
        UserAppRolesCriteriaType criteria = fact.createUserAppRolesCriteriaType();
        apgt.setUserAppRolesCriteria(criteria);
        req.setCriteria(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(ApplicationSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to update a single application.
     * 
     * @param data
     *            {@link Application}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callUpdateApplication(Application data) throws AuthenticationException {
        // Retrieve all user group records from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_APPLICATION_UPDATE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();
        ApplicationType app = fact.createApplicationType();
        app.setActive(data.getActive());
        app.setAppCode(data.getName());
        app.setAppId(data.getAppId());
        app.setDescription(data.getDescription());

        apgt.getApplicationInfo().add(app);
        req.setProfile(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(ApplicationSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to delete a single application.
     * 
     * @param data
     *            {@link Application}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callDeleteApplication(Application data) throws AuthenticationException {
        // Retrieve all user group records from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_APPLICATION_DELETE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();
        ApplicationType app = fact.createApplicationType();
        app.setAppId(data.getAppId());

        apgt.getApplicationInfo().add(app);
        req.setProfile(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(ApplicationSoapRequests.MSG, e);
        }
    }
}
