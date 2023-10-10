package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.AppRoleType;

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
    public static final AppRole create() {
        try {
            return new AppRole();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static final AppRole create(AppRoleType obj) {
        if (obj == null) {
            return null;
        }

        AppRole results = AppRoleFactory.create();
        if (obj != null) {
            results.setAppRoleId(obj.getAppRoleId());
            results.setCode(obj.getAppRoleCode());
            results.setName(obj.getAppRoleName());
            results.setDescription(obj.getAppRoleDesc());
            if (obj.getAppInfo() != null) {
                results.setAppId(obj.getAppInfo().getAppId());
            }
            if (obj.getRoleInfo() != null) {
                results.setRoleId(obj.getRoleInfo().getRoleId());
            }
        }
        return results;
    }

    /**
     * 
     * @param list
     * @return
     */
    public static final List<AppRole> create(List<AppRoleType> list) {
        if (list == null) {
            return null;
        }
        List<AppRole> results = new ArrayList<>();
        for (AppRoleType item : list) {
            results.add(AppRoleFactory.create(item));
        }
        return results;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static final AppRole create(VwAppRoles obj) {
        if (obj == null) {
            return null;
        }

        AppRole results = AppRoleFactory.create();
        if (obj != null) {
            results.setAppRoleId(obj.getAppRoleId());
            results.setCode(obj.getAppRoleCode());
            results.setName(obj.getAppRoleName());
            results.setDescription(obj.getAppRoleDescription());
            results.setAppId(obj.getApplicationId());
            results.setRoleId(obj.getRoleId());
        }
        return results;
    }

}
