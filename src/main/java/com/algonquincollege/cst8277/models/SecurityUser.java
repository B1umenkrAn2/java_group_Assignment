/*****************************************************************c******************o*******v******id********
 * File: SecurityUser.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 *
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import com.algonquincollege.cst8277.rest.SecurityRoleSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;


import static com.algonquincollege.cst8277.models.SecurityUser.USER_FOR_OWNING_CUST_QUERY;
import static com.algonquincollege.cst8277.models.SecurityUser.SECURITY_USER_BY_NAME_QUERY;

/**
 * User class used for (JSR-375) Java EE Security authorization/authentication
 */
@Entity
@Table(name = "SECURITY_USER")
@Access(AccessType.PROPERTY)
@NamedQueries({
        @NamedQuery(name = SECURITY_USER_BY_NAME_QUERY, query = "SELECT sr FROM SecurityUser sr WHERE sr.username =:param1"),
        @NamedQuery(name = USER_FOR_OWNING_CUST_QUERY,query = "SELECT sr FROM SecurityUser sr WHERE sr.customer.id=:id")
})
public class SecurityUser implements Serializable, Principal {
    /**
     * explicit set serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public static final String USER_FOR_OWNING_CUST_QUERY =
            "userForOwningCust";
    public static final String SECURITY_USER_BY_NAME_QUERY =
            "userByName";

    /**
     * Object id
     */
    protected int id;
    /**
     * User name
     */
    protected String username;
    /**
     * Hashed password
     */
    protected String pwHash;
    /**
     * List of security roles granted to this user
     */
    protected Set<SecurityRole> roles = new HashSet<>();
    /**
     * The customer wrapped by this security user
     */
    protected CustomerPojo cust;

    /**
     * Default constructor
     */
    public SecurityUser() {
        super();
    }

    /**
     * Getter for the id
     * 
     * @return current value for id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
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
     * Getter for username
     * 
     * @return current value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username
     * 
     * @param username new value for username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for hashed password
     * 
     * @return current value of hashed password
     */
    @JsonIgnore
    public String getPwHash() {
        return pwHash;
    }

    /**
     * Setter for hashed password
     * 
     * @param pwHash new value for hashed password
     */
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    /**
     * Getter for granted roles
     * 
     * @return list of granted roles
     */
    @ManyToMany(targetEntity = SecurityRole.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "SECURITY_USER_SECURITY_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID"))
    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = SecurityRoleSerializer.class)
    public Set<SecurityRole> getRoles() {
        return roles;
    }

    /**
     * Setter for granted roles
     * 
     * @param roles new granted roles
     */
    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }
    
    /**
     * Getter for wrappered customer
     * 
     * @return current wrappered customer
     */

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "CUSTOMER_ID")
    public CustomerPojo getCustomer() {
        return cust;
    }

    /**
     * Sett for wrappered customer
     * 
     * @param cust new wrapper customer
     */
    public void setCustomer(CustomerPojo cust) {
        this.cust = cust;
    }

    //Principal
    @JsonIgnore
    @Override
    public String getName() {
        return getUsername();
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
        SecurityUser other = (SecurityUser) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}