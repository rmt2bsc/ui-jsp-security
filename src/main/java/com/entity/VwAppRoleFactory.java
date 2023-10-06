package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.AppRoleType;

/**
 * A factory that creates new instances of VwAppRoles.
 * 
 * @author roy.terrell
 * 
 */
public class VwAppRoleFactory {

    /**
     * Create a new instance of a VwVwAppRoless class.
     * 
     * @return {@link VmVwAppRoless}
     */
    public static final VwAppRoles create() {
        try {
            return new VwAppRoles();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static final VwAppRoles create(AppRoleType obj) {
        if (obj == null) {
            return null;
        }

        VwAppRoles results = VwAppRoleFactory.create();
        if (obj != null) {
            results.setAppRoleId(obj.getAppRoleId());
            results.setAppRoleCode(obj.getAppRoleCode());
            results.setAppRoleName(obj.getAppRoleName());
            results.setAppRoleDescription(obj.getAppRoleDesc());
            if (obj.getAppInfo() != null) {
                results.setApplicationId(obj.getAppInfo().getAppId());
                results.setAppName(obj.getAppInfo().getAppCode());
            }
            if (obj.getRoleInfo() != null) {
                results.setRoleId(obj.getRoleInfo().getRoleId());
                results.setRoleName(obj.getRoleInfo().getRoleName());
            }
        }
        return results;
    }

    /**
     * 
     * @param list
     * @return
     */
    public static final List<VwAppRoles> create(List<AppRoleType> list) {
        if (list == null) {
            return null;
        }
        List<VwAppRoles> results = new ArrayList<>();
        for (AppRoleType item : list) {
            results.add(VwAppRoleFactory.create(item));
        }
        return results;
    }


}
