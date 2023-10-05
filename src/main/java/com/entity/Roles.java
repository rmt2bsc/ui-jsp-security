package com.entity;

import com.SystemException;
import com.api.persistence.db.orm.OrmBean;
import com.api.util.assistants.EqualityAssistant;
import com.api.util.assistants.HashCodeAssistant;


/**
 * Peer object that maps to the roles database table/view.
 *
 * @author auto generated.
 */
public class Roles extends OrmBean {




	// Property name constants that belong to respective DataSource, RolesView

/** The property name constant equivalent to property, RoleId, of respective DataSource view. */
  public static final String PROP_ROLEID = "RoleId";
/** The property name constant equivalent to property, Name, of respective DataSource view. */
  public static final String PROP_NAME = "Name";
/** The property name constant equivalent to property, Description, of respective DataSource view. */
  public static final String PROP_DESCRIPTION = "Description";
/** The property name constant equivalent to property, DateCreated, of respective DataSource view. */
  public static final String PROP_DATECREATED = "DateCreated";
/** The property name constant equivalent to property, DateUpdated, of respective DataSource view. */
  public static final String PROP_DATEUPDATED = "DateUpdated";
/** The property name constant equivalent to property, UserId, of respective DataSource view. */
  public static final String PROP_USERID = "UserId";



	/** The javabean property equivalent of database column roles.role_id */
  private int roleId;
/** The javabean property equivalent of database column roles.name */
  private String name;
/** The javabean property equivalent of database column roles.description */
  private String description;
/** The javabean property equivalent of database column roles.date_created */
  private java.util.Date dateCreated;
/** The javabean property equivalent of database column roles.date_updated */
  private java.util.Date dateUpdated;
/** The javabean property equivalent of database column roles.user_id */
  private String userId;



	// Getter/Setter Methods

/**
 * Default constructor.
 */
  public Roles() throws SystemException {
	super();
 }
/**
 * Sets the value of member variable roleId
 */
  public void setRoleId(int value) {
    this.roleId = value;
  }
/**
 * Gets the value of member variable roleId
 */
  public int getRoleId() {
    return this.roleId;
  }
/**
 * Sets the value of member variable name
 */
  public void setName(String value) {
    this.name = value;
  }
/**
 * Gets the value of member variable name
 */
  public String getName() {
    return this.name;
  }
/**
 * Sets the value of member variable description
 */
  public void setDescription(String value) {
    this.description = value;
  }
/**
 * Gets the value of member variable description
 */
  public String getDescription() {
    return this.description;
  }
/**
 * Sets the value of member variable dateCreated
 */
  public void setDateCreated(java.util.Date value) {
    this.dateCreated = value;
  }
/**
 * Gets the value of member variable dateCreated
 */
  public java.util.Date getDateCreated() {
    return this.dateCreated;
  }
/**
 * Sets the value of member variable dateUpdated
 */
  public void setDateUpdated(java.util.Date value) {
    this.dateUpdated = value;
  }
/**
 * Gets the value of member variable dateUpdated
 */
  public java.util.Date getDateUpdated() {
    return this.dateUpdated;
  }
/**
 * Sets the value of member variable userId
 */
  public void setUserId(String value) {
    this.userId = value;
  }
/**
 * Gets the value of member variable userId
 */
  public String getUserId() {
    return this.userId;
  }

@Override
public boolean equals(Object obj) {
   if (this == obj) {
      return true;
   }
   if (obj == null) {
      return false;
   }
   if (getClass() != obj.getClass()) {
      return false;
   }
   final Roles other = (Roles) obj; 
   if (EqualityAssistant.notEqual(this.roleId, other.roleId)) {
      return false;
   }
   if (EqualityAssistant.notEqual(this.name, other.name)) {
      return false;
   }
   if (EqualityAssistant.notEqual(this.description, other.description)) {
      return false;
   }
   return true; 
} 

@Override
public int hashCode() {
   return HashCodeAssistant.combineHashCodes(HashCodeAssistant.hashObject(this.roleId),
               HashCodeAssistant.hashObject(this.name),
               HashCodeAssistant.hashObject(this.description));
} 

@Override
public String toString() {
   return "Roles [roleId=" + roleId + 
          ", name=" + name + 
          ", description=" + description  + "]";
}

/**
 * Stubbed initialization method designed to implemented by developer.

 */
  public void initBean() throws SystemException {}
}