package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.ResourceType;
import org.rmt2.jaxb.ResourcesInfoType;
import org.rmt2.jaxb.ResourcesubtypeType;
import org.rmt2.jaxb.ResourcetypeType;
import org.rmt2.jaxb.UserResourceAccessListType;
import org.rmt2.jaxb.UserResourceAccessType;



/**
 * A factory that creates new Resource related instances.
 * 
 * @author roy.terrell
 * 
 */
public class UserResourceFactory {

    /**
     * Create a new instance of a UserResource class.
     * 
     * @return {@link UserResource}
     */
    public static UserResource createUserResource() {
        try {
            return new UserResource();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a UserResource class.
     * 
     * @param item
     *            {@link ResourceType}
     * @return {@link UserResource}
     */
    public static UserResource createUserResource(ResourceType item) {
        try {
            UserResource obj = UserResourceFactory.createUserResource();
            obj.setRsrcId(item.getUid());
            obj.setName(item.getCode());
            obj.setDescription(item.getDescription());
            obj.setRsrcTypeId(item.getTypeInfo().getUid());
            obj.setRsrcSubtypeId(item.getSubtypeInfo().getUid());
            obj.setUrl(item.getUrl());
            obj.setSecured(item.getSecured());
            obj.setHost(item.getHost());
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
     * Create a new instance of a UserResource class.
     * 
     * @param items
     *            {@link UserResourceAccessListType}
     * @return List<{@link UserResource}>}
     */
    public static List<UserResource> createUserResource(UserResourceAccessListType items) {
        try {
            List<UserResource> obj = new ArrayList<>();
            List<UserResourceAccessType> uratList = items.getUserResourceAccess();
            for (UserResourceAccessType item : uratList) {
                obj.add(UserResourceFactory.createUserResource(item.getResourceInfo()));
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
     * @return List<{@link UserResource}>}
     */
    public static List<UserResource> createUserResource(ResourcesInfoType items) {
        try {
            List<ResourceType> list = items.getResource();
            List<UserResource> obj = new ArrayList<>();
            for (ResourceType item : list) {
                obj.add(UserResourceFactory.createUserResource(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a UserResourceAccess class.
     * 
     * @return {@link UserResourceAccess}
     */
    public static UserResourceAccess createUserResourceAccess() {
        try {
            return new UserResourceAccess();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a List of new instances of a UserResourceAccess class.
     * 
     * @param item
     *            {@link UserResourceAccessType}
     * @return {@link UserResourceAccess}
     */
    public static UserResourceAccess createUserResourceAccess(UserResourceAccessType item) {
        try {
            UserResourceAccess obj = UserResourceFactory.createUserResourceAccess();
            obj.setRsrcAccessId(item.getResourceInfo().getUid());
            obj.setLoginId(item.getUserInfo().getLoginId());
            obj.setRsrcId(item.getResourceInfo().getUid());
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a List of new instances of a UserResourceAccess class.
     * 
     * @param items
     *            {@link UserResourceAccessListType}
     * @return List<{@link UserResourceAccess}>}
     */
    public static List<UserResourceAccess> createUserResourceAccess(UserResourceAccessListType items) {
        try {
            List<UserResourceAccess> obj = new ArrayList<>();
            List<UserResourceAccessType> uratList = items.getUserResourceAccess();
            for (UserResourceAccessType item : uratList) {
                obj.add(UserResourceFactory.createUserResourceAccess(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a UserResourceType class.
     * 
     * @return {@link UserResourceType}
     */
    public static UserResourceType createUserResourceType() {
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
    public static UserResourceType createUserResourceType(ResourcetypeType item) {
        try {
            UserResourceType obj = UserResourceFactory.createUserResourceType();
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
     * Create a List of new instances of a UserResourceType class.
     * 
     * @param items
     *            @link ResourcesInfoType}
     * @return {@link List<{@link UserResourceType}>}
     */
    public static List<UserResourceType> createUserResourceType(ResourcesInfoType items) {
        try {
            List<ResourcetypeType> list = items.getResourcetype();
            List<UserResourceType> obj = new ArrayList<>();
            for (ResourcetypeType item : list) {
                obj.add(UserResourceFactory.createUserResourceType(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a UserResourceSubtype class.
     * 
     * @return {@link UserResourceSubtype}
     */
    public static UserResourceSubtype createUserResourceSubtype() {
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
    public static UserResourceSubtype createUserResourceSubtype(ResourcesubtypeType item) {
        try {
            UserResourceSubtype obj = UserResourceFactory.createUserResourceSubtype();
            obj.setRsrcSubtypeId(item.getUid());
            obj.setRsrcTypeId(item.getTypeInfo().getUid());
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
     *            ({@link ResourcesInfoType}
     * @return {@link List<{@link UserResourceSubtype}>}
     */
    public static List<UserResourceSubtype> createUserResourceSubtype(ResourcesInfoType items) {
        try {
            List<ResourcesubtypeType> list = items.getResourcesubtype();
            List<UserResourceSubtype> obj = new ArrayList<>();
            for (ResourcesubtypeType item : list) {
                obj.add(UserResourceFactory.createUserResourceSubtype(item));
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a VwUserResourceAccess class.
     * 
     * @return {@link com.bean.VwUserResourceAccess VwUserResourceAccess}
     */
    public static VwUserResourceAccess createUserAccess() {
        try {
            return new VwUserResourceAccess();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Create a new instance of a VwResourceType class.
     * 
     * @return {@link com.bean.VwResourceType VwResourceType}
     */
    public static VwResourceType createVwResourceType() {
        try {
            return new VwResourceType();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new instance of a VwResource class.
     * 
     * @return {@link com.bean.VwResource VwResource}
     */
    public static VwResource createVwResource() {
        try {
            return new VwResource();
        } catch (Exception e) {
            return null;
        }
    }

}
