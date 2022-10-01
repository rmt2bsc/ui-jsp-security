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
import org.rmt2.jaxb.UserType;
import org.rmt2.util.HeaderTypeBuilder;
import org.rmt2.util.authentication.UserGroupTypeBuilder;
import org.rmt2.util.authentication.UserTypeBuilder;

import com.AuthConstants;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.security.authentication.web.AuthenticationException;
import com.api.util.RMT2Date;
import com.api.util.RMT2Money;
import com.api.util.assistants.Verifier;
import com.api.util.assistants.VerifyException;
import com.entity.UserLogin;

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
    public static final AuthenticationResponse callSearchUsers(UserCriteria userCriteria) throws AuthenticationException {
        // Retrieve user group from the database using unique id.
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

    private static final UserCriteriaType buildPaylodCriteriaSearchCriteria(UserCriteria reqCriteria) {
        ObjectFactory fact = new ObjectFactory();
        UserCriteriaType jaxbCriteria = fact.createUserCriteriaType();

        // Get Login ID
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_Id());
            if (RMT2Money.isNumeric(reqCriteria.getQry_Id())) {
                jaxbCriteria.setLoginId(Integer.valueOf(reqCriteria.getQry_Id()));
            }
        } catch (VerifyException e) {

        }

        // Get User Name
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_Username());
            jaxbCriteria.setUserName(reqCriteria.getQry_Username());
        } catch (VerifyException e) {

        }

        // Get Group ID
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_GrpId());
            if (RMT2Money.isNumeric(reqCriteria.getQry_GrpId())) {
                jaxbCriteria.setGroupId(Integer.valueOf(reqCriteria.getQry_GrpId()));
            }
        } catch (VerifyException e) {

        }

        // Get Last Name
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_Lastname());
            jaxbCriteria.setLastName(reqCriteria.getQry_Lastname());
        } catch (VerifyException e) {

        }

        // Get First Name
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_Firstname());
            jaxbCriteria.setFirstName(reqCriteria.getQry_Firstname());
        } catch (VerifyException e) {

        }

        // Get Email
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_Email());
            jaxbCriteria.setEmail(reqCriteria.getQry_Email());
        } catch (VerifyException e) {

        }

        // Get SSN
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_Ssn());
            jaxbCriteria.setSsn(reqCriteria.getQry_Ssn());
        } catch (VerifyException e) {

        }

        // Get Date of Birth
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_BirthDate());
            jaxbCriteria.setDob(RMT2Date.toXmlDate(reqCriteria.getQry_BirthDate()));
        } catch (VerifyException e) {

        }

        // Get Start Date
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_StartDate());
            jaxbCriteria.setStartDate(RMT2Date.toXmlDate(reqCriteria.getQry_StartDate()));
        } catch (VerifyException e) {

        }
        
        // Get Termination Date
        try {
            Verifier.verifyNotEmpty(reqCriteria.getQry_TerminationDate());
            jaxbCriteria.setTermDate(RMT2Date.toXmlDate(reqCriteria.getQry_TerminationDate()));
        } catch (VerifyException e) {

        }
        return jaxbCriteria;

    }

    /**
     * SOAP call to add or modify a user login instance.
     * 
     * @param grp
     *            {@link UserLogin}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callUpdateUser(UserLogin usr) throws AuthenticationException {

        // Update user group record
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_USER_UPDATE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();

        UserGroupType ugt = UserGroupTypeBuilder.Builder.create()
                .withGroupId(usr.getGrpId())
                .build();

        UserType ut = UserTypeBuilder.Builder.create()
                .withLoginId(usr.getLoginId())
                .withGroupInfo(ugt)
                .withUsername(usr.getUsername())
                .withFirstname(usr.getFirstname())
                .withLastname(usr.getLastname())
                .withBirthDate(usr.getBirthDate())
                .withSsn(usr.getSsn())
                .withStartDate(usr.getStartDate())
                .withTermDate(usr.getTerminationDate())
                .withDescription(usr.getDescription())
                .withTotalLogins(usr.getTotalLogons())
                .withEmail(usr.getEmail())
                .withActiveFlag(usr.getActive() == 1 ? true : false)
                .build();

        apgt.getUserInfo().add(ut);

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

    /**
     * 
     * @param userLoginId
     * @return
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callDeleteUser(int userLoginId) throws AuthenticationException {

        // Delete user group record
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_USER_DELETE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();
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
