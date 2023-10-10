package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.ResourcesInfoType;
import org.rmt2.jaxb.ResourcesubtypeType;



/**
 * A factory that creates new Resource sub type related instances.
 * 
 * @author roy.terrell
 * 
 */
public class ResourceSubTypeFactory {

    /**
     * Create a new instance of a UserResourceType class.
     * 
     * @return {@link UserResourceSubtype}
     */
    public static UserResourceSubtype create() {
        try {
            return new UserResourceSubtype();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a UserResourceSubtype class.
     * 
     * @param item
     *            {@link ResourcesubtypeType}
     * @return {@link UserResourceSubtype}
     */
    public static UserResourceSubtype createUserResource(ResourcesubtypeType item) {
        try {
            UserResourceSubtype obj = ResourceSubTypeFactory.create();
            obj.setRsrcSubtypeId(item.getUid());
            obj.setRsrcTypeId(item.getResourceTypeId());
            obj.setName(item.getCode());
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
     * Create a List of new instances of a UserResourceSubtype class.
     * 
     * @param items
     *            {@link ResourcesInfoType}
     * @return List<{@link UserResourceSubtype}>}
     */
    public static List<UserResourceSubtype> create(ResourcesInfoType items) {
        if (items == null || items.getResourcesubtype() == null) {
            return null;
        }

        try {
            List<ResourcesubtypeType> list = items.getResourcesubtype();
            List<UserResourceSubtype> obj = new ArrayList<>();
            for (ResourcesubtypeType item : list) {
                obj.add(ResourceSubTypeFactory.createUserResource(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

}
