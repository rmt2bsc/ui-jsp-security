package com.action.groups;

import java.util.Date;

import javax.xml.soap.SOAPMessage;

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
import com.api.config.ConfigConstants;
import com.api.config.SystemConfigurator;
import com.api.messaging.MessageException;
import com.api.messaging.webservice.soap.client.SoapClientWrapper;
import com.api.security.authentication.web.AuthenticationException;
import com.api.xml.jaxb.JaxbUtil;
import com.entity.UserGroup;

/**
 * Help class for constructing and invoking SOAP calls pertaining to the User
 * Group entity.
 * 
 * @author appdev
 *
 */
public class UserGroupSoapRequests {
    private static final Logger logger = Logger.getLogger(UserGroupSoapRequests.class);
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging";

    /**
     * SOAP call to fetch all user groups.
     * 
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callSearchAllUserGroups() throws AuthenticationException {
        // Retrieve all user group records from the database
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
        UserCriteriaType criteria = fact.createUserCriteriaType();
        apgt.setUserCriteria(criteria);
        req.setCriteria(apgt);
        req.setHeader(head);

        // Marshall a data object using some JAXB object
        JaxbUtil jaxb = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        String payload = jaxb.marshalJsonMessage(req);

        // Prepare to make SOAP request.
        SoapClientWrapper client = new SoapClientWrapper();
        try {
            SOAPMessage resp = client.callSoap(payload);
            if (client.isSoapError(resp)) {
                String errMsg = client.getSoapErrorMessage(resp);
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            String result = client.getSoapResponsePayloadString();
            logger.info(result);

            AuthenticationResponse response = (AuthenticationResponse) jaxb.unMarshalMessage(result);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == -1) {
                String errMsg = rst.getMessage();
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            return response;
        } catch (MessageException e) {
            throw new AuthenticationException(UserGroupSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to fetch a single user group.
     * 
     * @param grpId
     *            the unique id of the user group
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callSearchUserGroup(int grpId) throws AuthenticationException {
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
        UserCriteriaType criteria = fact.createUserCriteriaType();
        criteria.setGroupId(grpId);
        apgt.setUserCriteria(criteria);
        req.setCriteria(apgt);
        req.setHeader(head);

        // Marshall a data object using some JAXB object
        JaxbUtil jaxb = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        String payload = jaxb.marshalJsonMessage(req);

        // Prepare to make SOAP request.
        SoapClientWrapper client = new SoapClientWrapper();
        try {
            SOAPMessage resp = client.callSoap(payload);
            if (client.isSoapError(resp)) {
                String errMsg = client.getSoapErrorMessage(resp);
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            String result = client.getSoapResponsePayloadString();
            logger.info(result);

            AuthenticationResponse response = (AuthenticationResponse) jaxb.unMarshalMessage(result);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == -1) {
                String errMsg = rst.getMessage();
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }

            return response;

        } catch (MessageException e) {
            throw new AuthenticationException(UserGroupSoapRequests.MSG, e);
        }
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

        // Marshall a data object using some JAXB object
        JaxbUtil jaxb = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        String payload = jaxb.marshalJsonMessage(req);

        // Prepare to make SOAP request.
        SoapClientWrapper client = new SoapClientWrapper();
        try {
            SOAPMessage resp = client.callSoap(payload);
            if (client.isSoapError(resp)) {
                String errMsg = client.getSoapErrorMessage(resp);
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            String result = client.getSoapResponsePayloadString();
            logger.info(result);

            AuthenticationResponse response = (AuthenticationResponse) jaxb.unMarshalMessage(result);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == -1) {
                String errMsg = rst.getMessage();
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            return response;
        } catch (MessageException e) {
            throw new AuthenticationException(UserGroupSoapRequests.MSG, e);
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

        // Marshall a data object using some JAXB object
        JaxbUtil jaxb = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        String payload = jaxb.marshalJsonMessage(req);

        // Prepare to make SOAP request.
        SoapClientWrapper client = new SoapClientWrapper();
        try {
            SOAPMessage resp = client.callSoap(payload);
            if (client.isSoapError(resp)) {
                String errMsg = client.getSoapErrorMessage(resp);
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            String result = client.getSoapResponsePayloadString();
            logger.info(result);

            AuthenticationResponse response = (AuthenticationResponse) jaxb.unMarshalMessage(result);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == -1) {
                String errMsg = rst.getMessage();
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }
            return response;
        } catch (MessageException e) {
            throw new AuthenticationException(UserGroupSoapRequests.MSG, e);
        }

    }
}
