package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.UserGroupType;

/**
 * A factory that creates new instances of UserGroup.
 * 
 * @author roy.terrell
 * 
 */
public class UserGroupFactory {

    /**
     * Create a new instance of a UserGroup class.
     * 
     * @return {@link UserGroup}
     */
    public static UserGroup createUserGroup() {
        try {
            return new UserGroup();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 
     * @param list
     * @return
     */
    public static final List getUserGroupList(List<UserGroupType> list) {
        List<UserGroup> results = new ArrayList<>();
        for (UserGroupType item : list) {
            results.add(UserGroupFactory.getUserGroup(item));
        }
        return results;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static final UserGroup getUserGroup(UserGroupType obj) {
        UserGroup u = UserGroupFactory.createUserGroup();
        u.setGrpId(obj.getGrpId());
        u.setDescription(obj.getDescription());
        if (obj.getTracking() != null) {
            if (obj.getTracking().getDateCreated() != null) {
                u.setDateCreated(obj.getTracking().getDateCreated().toGregorianCalendar().getTime());
            }
            if (obj.getTracking().getDateUpdated() != null) {
                u.setDateUpdated(obj.getTracking().getDateUpdated().toGregorianCalendar().getTime());
            }
            u.setUserId(obj.getTracking().getUserId());
        }
        return u;
    }

}
