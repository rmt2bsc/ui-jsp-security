package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.ResourcesInfoType;
import org.rmt2.jaxb.ResourcetypeType;



/**
 * A factory that creates new Resource type related instances.
 * 
 * @author roy.terrell
 * 
 */
public class ResourceTypeFactory {

    /**
     * Create a new instance of a UserResourceType class.
     * 
     * @return {@link UserResourceType}
     */
    public static UserResourceType create() {
        try {
            return new UserResourceType();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a UserResourceType class.
     * 
     * @param item
     *            {@link ResourcetypeType}
     * @return {@link UserResourceType}
     */
    public static UserResourceType createUserResource(ResourcetypeType item) {
        try {
            UserResourceType obj = ResourceTypeFactory.create();
            obj.setRsrcTypeId(item.getUid());
            obj.setDescription(item.getDescription());
            if (item.getTracking() != null) {
                obj.setDateCreated(item.getTracking().getDateCreated().toGregorianCalendar().getTime());
                obj.setDateUpdated(item.getTracking().getDateUpdated().toGregorianCalendar().getTime());
                obj.setUserId(item.getTracking().getUserId());
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Create a List of new instances of a UserResource class.
     * 
     * @param items
     *            {@link ResourcesInfoType}
     * @return List<{@link UserResourceType}>}
     */
    public static List<UserResourceType> create(ResourcesInfoType items) {
        if (items == null || items.getResourcetype() == null) {
            return null;
        }

        try {
            List<ResourcetypeType> list = items.getResourcetype();
            List<UserResourceType> obj = new ArrayList<>();
            for (ResourcetypeType item : list) {
                obj.add(ResourceTypeFactory.createUserResource(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

}
