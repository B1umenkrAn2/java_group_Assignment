/*****************************************************************c******************o*******v******id********
 * File: SecurityRole.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 *
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 *             
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;


/**
 * Role class used for (JSR-375) Java EE Security authorization/authentication
 */
@Entity
@Table(name = "SECURITY_ROLE")
@Access(AccessType.PROPERTY)
@NamedQueries({
        @NamedQuery(name = "roleByName", query = "select sr from SecurityRole sr where sr.roleName =:param1 ")}
)
public class SecurityRole implements Serializable {
    /**
     * explicit set serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public static final String ROLE_BY_NAME_QUERY = "roleByName";

    /**
     * Object id
     */
    protected int id;
    /**
     * Role name
     */
    protected String roleName;
    /**
     * List of security user granted with this security role
     */
    protected Set<SecurityUser> users;

    /**
     * Default constructor
     */
    public SecurityRole() {
        super();
    }

    /**
     * Getter for id
     * 
     * @return current value of id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    public int getId() {
        return id;
    }

    /**
     * Setter for id
     * 
     * @param id new value for id
     */
    public void setId(int id) {
        this.id = id;
    }

    
    /**
     * Getter for roleName
     * 
     * @return current value of roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Setter for roleName
     * 
     * @param roleName new value for roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Getter for users
     * 
     * @return list of security users
     */
    @ManyToMany(mappedBy = "roles")
    @JsonInclude(Include.NON_NULL)
    public Set<SecurityUser> getUsers() {
        return users;
    }

    /**
     * Setter for users
     * 
     * @param users new list of security users
     */
    public void setUsers(Set<SecurityUser> users) {
        this.users = users;
    }

    /**
     * Add a security user to this role
     * 
     * @param user the security user to be added
     */
    public void addUserToRole(SecurityUser user) {
        getUsers().add(user);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
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
        SecurityRole other = (SecurityRole) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}