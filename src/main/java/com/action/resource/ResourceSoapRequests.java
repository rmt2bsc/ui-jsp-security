package com.action.resource;

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
import org.rmt2.jaxb.ResourceCriteriaType;
import org.rmt2.jaxb.ResourcesInfoType;
import org.rmt2.jaxb.ResourcesubtypeType;
import org.rmt2.jaxb.ResourcetypeType;
import org.rmt2.util.HeaderTypeBuilder;
import org.rmt2.util.authentication.ResourceSubtypeTypeBuilder;
import org.rmt2.util.authentication.ResourcesInfoTypeBuilder;
import org.rmt2.util.authentication.ResourcetypeTypeBuilder;

import com.AuthConstants;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.security.authentication.web.AuthenticationException;
import com.entity.UserResourceSubtype;

/**
 * Help class for constructing and invoking SOAP calls pertaining to the
 * Resource related security entities.
 * 
 * @author Roy Terrell
 *
 */
public class ResourceSoapRequests {
    private static final Logger logger = Logger.getLogger(ResourceSoapRequests.class);
    private static final String MSG = "SOAP invocation error occurred regarding server-side messaging";

    /**
     * SOAP call to fetch all resource records.
     * 
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callGet() throws AuthenticationException {
        // Retrieve all resource records from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_RESOURCE_GET)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthCriteriaGroupType apgt = fact.createAuthCriteriaGroupType();
        ResourceCriteriaType criteria = fact.createResourceCriteriaType();
        apgt.setResourceCriteria(criteria);
        req.setCriteria(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(ResourceSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to update a single resource object.
     * 
     * @param data
     *            {@link UserResourceSubtype}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callUpdate(UserResourceSubtype data) throws AuthenticationException {
        // Modify a resource type record
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_RESOURCE_SUB_TYPE_UPDATE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();

        ResourcetypeType rt = ResourcetypeTypeBuilder.Builder.create()
                .withTypeId(data.getRsrcTypeId())
                .build();

        ResourcesubtypeType rst = ResourceSubtypeTypeBuilder.Builder.create()
                .withSubTypeId(data.getRsrcSubtypeId())
                .withType(rt)
                .withName(data.getName())
                .withDescription(data.getDescription())
                .build();

        ResourcesInfoType rit = ResourcesInfoTypeBuilder.Builder.create()
                .addResourceSubType(rst)
                .build();

        apgt.setResourcesInfo(rit);
        req.setProfile(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(ResourceSoapRequests.MSG, e);
        }
    }

    /**
     * SOAP call to delete a single resource.
     * 
     * @param data
     *            {@link UserResourceSubtype}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callDelete(UserResourceSubtype data) throws AuthenticationException {
        // Delete resource type record from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_RESOURCE_SUB_TYPE_DELETE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();
        ResourcesubtypeType rt = ResourceSubtypeTypeBuilder.Builder.create()
                .withSubTypeId(data.getRsrcSubtypeId())
                .build();

        ResourcesInfoType rit = ResourcesInfoTypeBuilder.Builder.create()
                .addResourceSubType(rt)
                .build();

        apgt.setResourcesInfo(rit);
        req.setProfile(apgt);
        req.setHeader(head);

        AuthenticationResponse response = null;
        try {
            response = SoapJaxbClientWrapper.callSoapRequest(req);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException(ResourceSoapRequests.MSG, e);
        }
    }
}
