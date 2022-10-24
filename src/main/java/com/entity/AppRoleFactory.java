package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.UserAppRoleType;

/**
 * A factory that creates new instances of AppRole.
 * 
 * @author roy.terrell
 * 
 */
public class AppRoleFactory {

    /**
     * Create a new instance of a AppRole class.
     * 
     * @return {@link AppRole}
     */
    public static AppRole create() {
        try {
            return new AppRole();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 
     * @param list
     * @return
     */
    public static final List<AppRole> create(List<UserAppRoleType> list) {
        if (list == null) {
            return null;
        }
        List<AppRole> results = new ArrayList<>();
        for (UserAppRoleType item : list) {
            results.add(AppRoleFactory.create(item));
        }
        return results;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static final AppRole create(UserAppRoleType obj) {
        if (obj == null) {
            return null;
        }

        AppRole results = AppRoleFactory.create();
        if (obj.getAppRoleInfo() != null) {
            results.setAppRoleId(obj.getAppRoleInfo().getAppRoleId());
            results.setCode(obj.getAppRoleInfo().getAppRoleCode());
            results.setName(obj.getAppRoleInfo().getAppRoleName());
            results.setDescription(obj.getAppRoleInfo().getAppRoleDesc());
            if (obj.getAppRoleInfo().getAppInfo() != null) {
                results.setAppId(obj.getAppRoleInfo().getAppInfo().getAppId());

            }
            if (obj.getAppRoleInfo().getRoleInfo() != null) {
                results.setRoleId(obj.getAppRoleInfo().getRoleInfo().getRoleId());
            }
        }
        return results;
    }

}
