package com.entity;

import java.util.ArrayList;
import java.util.List;

import org.rmt2.jaxb.UserType;

/**
 * A factory that creates new instances of UserLogin.
 * 
 * @author roy.terrell
 * 
 */
public class UserLoginFactory {

    /**
     * Create a new instance of a UserLogin class.
     * 
     * @return {@link UserLogin}
     */
    public static UserLogin create() {
        try {
            return new UserLogin();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 
     * @param list
     * @return
     */
    public static final List getUserList(List<UserType> list) {
        List<UserLogin> results = new ArrayList<>();
        for (UserType item : list) {
            results.add(UserLoginFactory.getUser(item));
        }
        return results;
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static final UserLogin getUser(UserType obj) {
        UserLogin u = UserLoginFactory.create();
        u.setLoginId(obj.getLoginId());
        if (obj.getGroupInfo() != null) {
            if (obj.getGroupInfo().getGrpId() != null) {
                u.setGrpId(obj.getGroupInfo().getGrpId());
            }
            u.setGrpName(obj.getGroupInfo().getDescription());
        }
        u.setActive(obj.getActive());
        u.setBirthDate(obj.getDob().toGregorianCalendar().getTime());
        u.setEmail(obj.getEmail());
        u.setFirstname(obj.getFirstName());
        u.setLastname(obj.getLastName());
        u.setDescription(obj.getDescription());
        u.setLoggedIn(obj.getLoggedIn());
        u.setPassword(obj.getPassword());
        u.setSsn(obj.getSsn());
        u.setStartDate(obj.getStartDate().toGregorianCalendar().getTime());
        if (obj.getTermDate() != null) {
            u.setTerminationDate(obj.getTermDate().toGregorianCalendar().getTime());
        }
        u.setTotalLogons(obj.getTotalLogons());
        u.setUsername(obj.getUserName());
        if (obj.getTracking() != null) {
            if (obj.getTracking().getDateCreated() != null) {
                u.setDateCreated(obj.getTracking().getDateCreated().toGregorianCalendar().getTime());
            }
            if (obj.getTracking().getDateUpdated() != null) {
                u.setDateUpdated(obj.getTracking().getDateUpdated().toGregorianCalendar().getTime());
            }
            u.setUserId(obj.getTracking().getUserId());
        }

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
