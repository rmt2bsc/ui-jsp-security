package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.RoleType;

/**
 * A factory that creates new instances of Roles.
 * 
 * @author roy.terrell
 * 
 */
public class RoleFactory {

    /**
     * Create a new instance of a Roles class.
     * 
     * @return {@link Roles}
     */
    public static Roles create() {
        try {
            return new Roles();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 
     * @param obj
     * @return
     */
    public static final Roles create(RoleType obj) {
        if (obj == null) {
            return null;
        }

        Roles results = RoleFactory.create();
        results.setRoleId(obj.getRoleId());
        results.setName(obj.getRoleName());
        results.setDescription(obj.getRoleDescription());
        return results;
    }

    /**
     * 
     * @param list
     * @return
     */
    public static final List<Roles> create(List<RoleType> list) {
        if (list == null) {
            return null;
        }
        List<Roles> results = new ArrayList<>();
        for (RoleType item : list) {
            results.add(RoleFactory.create(item));
        }
        return results;
    }


}
