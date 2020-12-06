/*****************************************************************c******************o*******v******id********
 * File: BillingAddressPojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.models;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

/**
 * Description: model for the BillingAddress object
 */
@Entity(name = "BillingAddress")
@Table(name = "CUST_ADDR")
@Access(AccessType.PROPERTY)
@DiscriminatorValue("BILLING_ADDR")
public class BillingAddressPojo extends AddressPojo implements Serializable {
    /**
     * explicit set serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Boolean value indicating if this billing address also for shipping
     */
    protected boolean isAlsoShipping;

    // JPA requires each @Entity class have a default constructor
    public BillingAddressPojo() {
    }

    /**
     * Getter for isAlsoShipping
     * 
     * @return boolean flag indicating if this billing address also for shipping
     */
    public boolean isAlsoShipping() {
        return isAlsoShipping;
    }
    
    /**
     * Setter for isAlsoShipping
     * 
     * @param isAlsoShipping new value for isAlsoShipping
     */
    public void setAlsoShipping(boolean isAlsoShipping) {
        this.isAlsoShipping = isAlsoShipping;
    }
}