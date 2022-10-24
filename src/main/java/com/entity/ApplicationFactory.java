package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.ApplicationType;

/**
 * A factory that creates new instances of Application.
 * 
 * @author roy.terrell
 * 
 */
public class ApplicationFactory {

    /**
     * Create a new instance of a Application class.
     * 
     * @return {@link Application}
     */
    public static Application create() {
        try {
            return new Application();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 
     * @param list
     * @return
     */
    public static final List<Application> create(List<ApplicationType> list) {
        if (list == null) {
            return null;
        }
        List<Application> results = new ArrayList<>();
        for (ApplicationType item : list) {
            results.add(ApplicationFactory.create(item));
        }
        return results;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static final Application create(ApplicationType obj) {
        if (obj == null) {
            return null;
        }

        Application results = ApplicationFactory.create();
        results.setAppId(obj.getAppId());
        results.setName(obj.getAppCode());
        results.setDescription(obj.getDescription());
        results.setActive(obj.getActive());
        return results;
    }

}
