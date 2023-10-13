package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.ResourceType;
import org.rmt2.jaxb.ResourcesInfoType;



/**
 * A factory that creates new VwResource related instances.
 * 
 * @author roy.terrell
 * 
 */
public class VwResourceFactory {

    /**
     * Create a new instance of a VwResource class.
     * 
     * @return {@link VwResource}
     */
    public static VwResource create() {
        try {
            return new VwResource();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a VwResource class.
     * 
     * @param item
     *            {@link ResourceType}
     * @return {@link VwResource}
     */
    public static VwResource create(ResourceType item) {
        try {
            VwResource obj = VwResourceFactory.create();
            obj.setRsrcId(item.getUid());
            obj.setName(item.getCode());
            obj.setDescription(item.getDescription());
            obj.setUrl(item.getUrl());
            obj.setHost(item.getHost());
            obj.setSecured(item.getSecured());
            obj.setRsrcTypeId(item.getTypeInfo().getUid());
            obj.setTypeDescr(item.getTypeInfo().getDescription());
            obj.setRsrcSubtypeId(item.getSubtypeInfo().getUid());
            obj.setSubtypeName(item.getSubtypeInfo().getCode());
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a List of new instances of a VwResource class.
     * 
     * @param items
     *            {@link ResourcesInfoType}
     * @return List<{@link VwResource}>}
     */
    public static List<VwResource> create(ResourcesInfoType items) {
        if (items == null || items.getResource() == null) {
            return null;
        }

        try {
            List<ResourceType> list = items.getResource();
            List<VwResource> obj = new ArrayList<>();
            for (ResourceType item : list) {
                obj.add(VwResourceFactory.create(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }
}
