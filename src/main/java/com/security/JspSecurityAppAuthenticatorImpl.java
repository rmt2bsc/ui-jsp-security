package com.security;

import java.util.Date;

import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.rmt2.constants.ApiHeaderNames;
import org.rmt2.constants.ApiTransactionCodes;
import org.rmt2.jaxb.ApplicationAccessType;
import org.rmt2.jaxb.AuthProfileGroupType;
import org.rmt2.jaxb.AuthenticationRequest;
import org.rmt2.jaxb.AuthenticationResponse;
import org.rmt2.jaxb.HeaderType;
import org.rmt2.jaxb.ObjectFactory;
import org.rmt2.jaxb.ReplyStatusType;
import org.rmt2.jaxb.UserType;
import org.rmt2.util.HeaderTypeBuilder;
import org.rmt2.util.authentication.ApplicationAccessTypeBuilder;
import org.rmt2.util.authentication.CoreUserBuilder;

import com.api.config.ConfigConstants;
import com.api.config.SystemConfigurator;
import com.api.messaging.MessageException;
import com.api.messaging.webservice.soap.client.SoapClientWrapper;
import com.api.security.User;
import com.api.security.authentication.web.AuthenticationException;
import com.api.security.authentication.web.LogoutException;
import com.api.web.Request;
import com.api.web.security.ClientUserAuthenticator;
import com.api.web.security.RMT2SecurityToken;
import com.api.web.security.RMT2SessionBean;
import com.api.web.security.SessionBeanManager;
import com.api.xml.jaxb.JaxbUtil;

/**
 * An JSP implementation of the ClientUserAuthenticator interface that provides
 * authentication services at the JSP client level.
 * 
 * @author appdev
 *
 */
public class JspSecurityAppAuthenticatorImpl implements ClientUserAuthenticator {
    private Logger logger = Logger.getLogger(JspSecurityAppAuthenticatorImpl.class);

    /**
     * 
     */
    public JspSecurityAppAuthenticatorImpl() {
        return;
    }

    /* (non-Javadoc)
     * @see com.api.web.security.ClientUserAuthenticator#authenticate(java.lang.String, java.lang.String)
     */
    @Override
    public RMT2SecurityToken authenticate(String loginId, String password) throws AuthenticationException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.api.web.security.ClientUserAuthenticator#authenticate(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RMT2SecurityToken authenticate(Request request, String loginId, String password, String appCode, String sessionId)
            throws AuthenticationException {
        
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();
        
        HeaderType head =  HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule("auth")
                .withTransaction(ApiTransactionCodes.AUTH_USER_LOGIN)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DUMMY_HEADER_VALUE)
                .build();

        ApplicationAccessType aat = ApplicationAccessTypeBuilder.Builder.create()
                .withUserName(loginId)
                .withPassword(password)
                .build();
        
        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();
        apgt.getApplicationAccessInfo().add(aat);
        req.setProfile(apgt);
        req.setHeader(head);
        
        // Marshall a data object using some JAXB object
        JaxbUtil jaxb = SystemConfigurator.getJaxb(ConfigConstants.JAXB_CONTEXNAME_DEFAULT);
        String payload = jaxb.marshalJsonMessage(req);
        
        // Authenticate user via SOAP request.
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

            SessionBeanManager sm = SessionBeanManager.getInstance(request);
            RMT2SecurityToken token = new RMT2SecurityToken();
            RMT2SessionBean session = sm.buildSessionBean();
            token.setSession(session);

            AuthenticationResponse response = (AuthenticationResponse) jaxb.unMarshalMessage(result);
            ReplyStatusType rst = response.getReplyStatus();
            if (rst.getReturnCode().intValue() == -1) {
                String errMsg = rst.getMessage();
                logger.error(errMsg);
                throw new AuthenticationException(errMsg);
            }

            // If we reached this point, user was authenticated successfully.
            // Begin setting up user's session bean and security token.
            UserType ut = (UserType) response.getProfile().getUserInfo().get(0);
            session.setLoginId(ut.getUserName());
            session.setOrigAppId(appCode);
            session.setFirstName(ut.getFirstName());
            session.setLastName(ut.getLastName());
            if (ut.getGroupInfo() != null && ut.getGroupInfo().getGrpId() != null) {
                session.setGroupId(ut.getGroupInfo().getGrpId().intValue());
            }
            User coreUser = CoreUserBuilder.Builder.create()
                    .withJaxbUser(ut)
                    .build();
            token.update(coreUser);
            token.setResponseData(result);
            return token;
        } catch (MessageException e) {
            String msg = "Accounting Authentication Error regarding server-side messaging";
            throw new AuthenticationException(msg, e);
        }
    }

    /* (non-Javadoc)
     * @see com.api.web.security.ClientUserAuthenticator#checkAuthenticationStatus(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RMT2SecurityToken checkAuthenticationStatus(Request request, String loginId, String appCode, String sessionId)
            throws AuthenticationException {
        // TODO Add specific logic here
        return null;
    }

    /* (non-Javadoc)
     * @see com.api.web.security.ClientUserAuthenticator#logout(java.lang.String, java.lang.String)
     */
    @Override
    public RMT2SecurityToken logout(String loginId, String sessionId) throws LogoutException {
        // TODO Add specific logic here
        return null;
    }

}
