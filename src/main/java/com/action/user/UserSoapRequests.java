package com.action.user;

import java.util.Date;

import org.apache.log4j.Logger;
import org.rmt2.constants.ApiHeaderNames;
import org.rmt2.constants.ApiTransactionCodes;
import org.rmt2.jaxb.AuthCriteriaGroupType;
import org.rmt2.jaxb.AuthProfileGroupType;
import org.rmt2.jaxb.AuthenticationRequest;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.HeaderType;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.jaxb.ReplyStatusType;
import org.rmt2.jaxb.UserCriteriaType;
import org.rmt2.jaxb.UserGroupType;
import org.rmt2.util.HeaderTypeBuilder;
import org.rmt2.util.authentication.UserGroupTypeBuilder;

import com.AuthConstants;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.security.authentication.web.AuthenticationException;
import com.api.util.RMT2Money;
import com.entity.UserGroup;

/**
 * Help class for constructing and invoking SOAP calls pertaining to the User
 * Group entity.
 * 
 * @author appdev
 *
 */
public class UserSoapRequests {
    private static final Logger logger = Logger.getLogger(UserSoapRequests.class);
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging";

    /**
     * SOAP call to fetch all users.
     * 
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callSearchAllUsers() throws AuthenticationException {
        // Retrieve all user group records from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_USER_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthCriteriaGroupType apgt = fact.createAuthCriteriaGroupType();
        UserCriteriaType criteria = fact.createUserCriteriaType();
        apgt.setUserCriteria(criteria);
        req.setCriteria(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == -1) {
                String errMsg = rst.getMessage();
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(UserSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to fetch a single user group.
     * 
     * @param userCriteria
     *            instance of {@link UserCriteria}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callSearchUser(UserCriteria userCriteria) throws AuthenticationException {
        // Retrieve user group from the database using unique id.
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_USER_GROUP_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthCriteriaGroupType apgt = fact.createAuthCriteriaGroupType();

        UserCriteriaType criteria = UserSoapRequests.buildPaylodCriteriaSearchCriteria(userCriteria);

        apgt.setUserCriteria(criteria);
        req.setCriteria(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == -1) {
                String errMsg = rst.getMessage();
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(UserSoapRequests.MSG, e);
        }
    }

    private static final UserCriteriaType buildPaylodCriteriaSearchCriteria(UserCriteria r) {
        ObjectFactory fact = new ObjectFactory();
        UserCriteriaType c = fact.createUserCriteriaType();

        if (r.getQry_Id() != null && RMT2Money.isNumeric(r.getQry_Id())) {
            c.setLoginId(Integer.valueOf(r.getQry_Id()));
        }
        if (r.getQry_Username() != null) {
            c.setUserName(r.getQry_Username());
        }
        if (r.getQry_GrpId() != null && RMT2Money.isNumeric(r.getQry_GrpId())) {
            c.setGroupId(Integer.valueOf(r.getQry_GrpId()));
        }

        return c;

    }

    /**
     * SOAP call to add or modify a user group instance.
     * 
     * @param grp
     *            {@link UserGroup}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callUpdateUserGroup(UserGroup grp) throws AuthenticationException {

        // Update user group record
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_USER_GROUP_UPDATE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();
        UserGroupType ug = UserGroupTypeBuilder.Builder.create()
                .withGroupId(grp.getGrpId())
                .withDescription(grp.getDescription())
                .build();

        apgt.getUserGroupInfo().add(ug);
        req.setProfile(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == -1) {
                String errMsg = rst.getMessage();
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(UserSoapRequests.MSG, e);
        }
    }

    public static final AuthenticationResponse callDeleteUserGroup(int grpId) throws AuthenticationException {

        // Update user group record
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_USER_GROUP_DELETE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();
        UserGroupType ug = UserGroupTypeBuilder.Builder.create()
                .withGroupId(grpId)
                .build();

        apgt.getUserGroupInfo().add(ug);
        req.setProfile(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == -1) {
                String errMsg = rst.getMessage();
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(UserSoapRequests.MSG, e);
        }
    }
}
