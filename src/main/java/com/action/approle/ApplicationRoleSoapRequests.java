package com.action.approle;

import java.util.Date;

import org.apache.log4j.Logger;
import org.rmt2.constants.ApiHeaderNames;
import org.rmt2.constants.ApiTransactionCodes;
import org.rmt2.jaxb.AppRoleType;
import org.rmt2.jaxb.ApplicationType;
import org.rmt2.jaxb.AuthCriteriaGroupType;
import org.rmt2.jaxb.AuthProfileGroupType;
import org.rmt2.jaxb.AuthenticationRequest;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.HeaderType;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.jaxb.RoleType;
import org.rmt2.jaxb.UserAppRolesCriteriaType;
import org.rmt2.util.HeaderTypeBuilder;
import org.rmt2.util.authentication.AppRoleTypeBuilder;
import org.rmt2.util.authentication.ApplicationTypeBuilder;
import org.rmt2.util.authentication.RoleTypeBuilder;

import com.AuthConstants;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.security.authentication.web.AuthenticationException;
import com.api.util.assistants.Verifier;
import com.api.util.assistants.VerifyException;
import com.entity.AppRole;

/**
 * Help class for constructing and invoking SOAP calls pertaining to the
 * Application/Role related security entities.
 * 
 * @author Roy Terrell
 *
 */
public class ApplicationRoleSoapRequests {
    private static final Logger logger = Logger.getLogger(ApplicationRoleSoapRequests.class);
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging";

    /**
     * SOAP call to fetch application/role data.
     * 
     * @param parms
     *            {@link AppRoleCriteria}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callGetApplicationRoles(AppRoleCriteria parms) throws AuthenticationException {
        // Retrieve application/role records from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_APP_ROLE_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthCriteriaGroupType acgt = fact.createAuthCriteriaGroupType();
        UserAppRolesCriteriaType criteria = fact.createUserAppRolesCriteriaType();

        // Verify and obtain parameter values
        if (parms != null) {
            try {
                Verifier.verifyEmpty(parms.getQry_Id());
            } catch (VerifyException e) {
                criteria.setAppRoleId(Integer.valueOf(parms.getQry_Id()));
            }

            try {
                Verifier.verifyEmpty(parms.getQry_ApplicationId());
            } catch (VerifyException e) {
                criteria.setAppId(Integer.valueOf(parms.getQry_ApplicationId()));
            }

            try {
                Verifier.verifyEmpty(parms.getQry_RoleId());
            } catch (VerifyException e) {
                criteria.setRoleId(Integer.valueOf(parms.getQry_RoleId()));
            }

            try {
                Verifier.verifyEmpty(parms.getQry_AppRoleCode());
            } catch (VerifyException e) {
                criteria.setAppRoleCode(parms.getQry_AppRoleCode());
            }

            try {
                Verifier.verifyEmpty(parms.getQry_AppRoleName());
            } catch (VerifyException e) {
                criteria.setAppRoleName(parms.getQry_AppRoleName());
            }

            try {
                Verifier.verifyEmpty(parms.getQry_Description());
            } catch (VerifyException e) {
                criteria.setAppRoleDescription(parms.getQry_Description());
            }
        }


        acgt.setUserAppRolesCriteria(criteria);
        req.setCriteria(acgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(ApplicationRoleSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to update a single application role.
     * 
     * @param data
     *            {@link AppRole}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callUpdateApplicationRole(AppRole data) throws AuthenticationException {
        // Retrieve all application role records from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_APP_ROLE_UPDATE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();
        ApplicationType at = ApplicationTypeBuilder.Builder.create()
                .withAppId(data.getAppId())
                .build();
        RoleType rt = RoleTypeBuilder.Builder.create()
                .withRoleId(data.getRoleId())
                .build();

        AppRoleType art = AppRoleTypeBuilder.Builder.create()
                .withAppRoleId(data.getAppRoleId())
                .withCode(data.getCode())
                .withName(data.getName())
                .withDescription(data.getDescription())
                .withApplication(at)
                .withRole(rt)
                .build();

        apgt.getAppRoleInfo().add(art);
        req.setProfile(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(ApplicationRoleSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to delete a single application role.
     * 
     * @param data
     *            {@link AppRole}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callDeleteApplicationRole(AppRole data) throws AuthenticationException {
        // Retrieve all user group records from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_APP_ROLE_DELETE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();
        AppRoleType art = AppRoleTypeBuilder.Builder.create()
                .withAppRoleId(data.getAppRoleId())
                .build();

        apgt.getAppRoleInfo().add(art);
        req.setProfile(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(ApplicationRoleSoapRequests.MSG, e);
        }
    }
}
