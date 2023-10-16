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
import org.rmt2.jaxb.ResourceType;
import org.rmt2.jaxb.ResourcesInfoType;
import org.rmt2.jaxb.ResourcesubtypeType;
import org.rmt2.jaxb.ResourcetypeType;
import org.rmt2.util.HeaderTypeBuilder;
import org.rmt2.util.authentication.ResourceSubtypeTypeBuilder;
import org.rmt2.util.authentication.ResourceTypeBuilder;
import org.rmt2.util.authentication.ResourcesInfoTypeBuilder;
import org.rmt2.util.authentication.ResourcetypeTypeBuilder;

import com.AuthConstants;
import com.api.messaging.webservice.soap.client.SoapJaxbClientWrapper;
import com.api.security.authentication.web.AuthenticationException;
import com.entity.UserResource;
import com.entity.VwResource;

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
     * <p>
     * Check if <i>criteria</i> object is needed. If not, return all rows
     * 
     * @param criteria
     *            {@link VwResource} If null, all rows are returned. Otherwise,
     *            use parameter to build selection criteria. Currently, only
     *            resource id is observed in <i>criteria<i/>.
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callGet(VwResource criteria) throws AuthenticationException {
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
        ResourceCriteriaType rct = fact.createResourceCriteriaType();
        apgt.setResourceCriteria(rct);
        req.setCriteria(apgt);
        req.setHeader(head);

        // Check if criteria object is needed. If not, return all rows
        if (criteria != null) {
            if (criteria.getRsrcId() > 0) {
                rct.setRsrcId(criteria.getRsrcId());
            }
        }

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
     *            {@link UserResource}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callUpdate(UserResource data) throws AuthenticationException {
        // Modify a resource type record
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_RESOURCE_UPDATE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();

        ResourcetypeType rtt = ResourcetypeTypeBuilder.Builder.create()
                .withTypeId(data.getRsrcTypeId())
                .build();

        ResourcesubtypeType rst = ResourceSubtypeTypeBuilder.Builder.create()
                .withSubTypeId(data.getRsrcSubtypeId())
                .build();

        ResourceType rt = ResourceTypeBuilder.Builder.create()
                .withResourceId(data.getRsrcId())
                .withName(data.getName())
                .withDescription(data.getDescription())
                .withType(rtt)
                .withSubType(rst)
                .withUrl(data.getUrl())
                .withHost(data.getHost())
                .withSecuredFlag(data.getSecured())
                .build();

        ResourcesInfoType rit = ResourcesInfoTypeBuilder.Builder.create()
                .addResource(rt)
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
     *            {@link UserResource}
     * @return {@link AuthenticationResponse}
     * @throws AuthenticationException
     */
    public static final AuthenticationResponse callDelete(UserResource data) throws AuthenticationException {
        // Delete resource type record from the database
        ObjectFactory fact = new ObjectFactory();
        AuthenticationRequest req = fact.createAuthenticationRequest();

        HeaderType head = HeaderTypeBuilder.Builder.create()
                .withApplication(ApiHeaderNames.APP_NAME_AUTHENTICATION)
                .withModule(AuthConstants.MODULE_ADMIN)
                .withTransaction(ApiTransactionCodes.AUTH_RESOURCE_DELETE)
                .withMessageMode(ApiHeaderNames.MESSAGE_MODE_REQUEST)
                .withDeliveryDate(new Date())
                .withRouting(ApiTransactionCodes.ROUTE_AUTHENTICATION)
                .withDeliveryMode(ApiHeaderNames.DELIVERY_MODE_SYNC)
                .build();

        AuthProfileGroupType apgt = fact.createAuthProfileGroupType();

        ResourceType rt = ResourceTypeBuilder.Builder.create()
                .withResourceId(data.getRsrcId())
                .build();

        ResourcesInfoType rit = ResourcesInfoTypeBuilder.Builder.create()
                .addResource(rt)
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
