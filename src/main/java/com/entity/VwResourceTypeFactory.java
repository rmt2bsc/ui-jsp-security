package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.ResourcesInfoType;
import org.rmt2.jaxb.ResourcesubtypeType;



/**
 * A factory that creates new VwResourceType related instances.
 * 
 * @author roy.terrell
 * 
 */
public class VwResourceTypeFactory {

    /**
     * Create a new instance of a VwResourceType class.
     * 
     * @return {@link VwResourceType}
     */
    public static VwResourceType create() {
        try {
            return new VwResourceType();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a VwResourceType class.
     * 
     * @param item
     *            {@link ResourcesubtypeType}
     * @return {@link VwResourceType}
     */
    public static VwResourceType create(ResourcesubtypeType item) {
        try {
            VwResourceType obj = VwResourceTypeFactory.create();
            obj.setResrcSubtypeId(item.getUid());
            obj.setResrcTypeId(item.getTypeInfo().getUid());
            obj.setResrcTypeName(item.getTypeInfo().getDescription());
            obj.setResrcSubtypeName(item.getCode());
            obj.setResrcSubtypeDesc(item.getDescription());
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a List of new instances of a VwResourceType class.
     * 
     * @param items
     *            {@link ResourcesInfoType}
     * @return List<{@link VwResourceType}>}
     */
    public static List<VwResourceType> create(ResourcesInfoType items) {
        if (items == null || items.getResourcesubtype() == null) {
            return null;
        }

        try {
            List<ResourcesubtypeType> list = items.getResourcesubtype();
            List<VwResourceType> obj = new ArrayList<>();
            for (ResourcesubtypeType item : list) {
                obj.add(VwResourceTypeFactory.create(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }
}
