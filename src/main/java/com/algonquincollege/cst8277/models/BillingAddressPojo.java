/*****************************************************************c******************o*******v******id********
 * File: BillingAddressPojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 *
 * update by :
 * Lai Shan Law (040595733)
 * Siyang Xiong (040938012)
 * Angela Zhao (040529234)
 *
 * @date 2020-11-21
 */
package com.algonquincollege.cst8277.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Description: model for the BillingAddress object
 */
@Entity(name = "BillingAddress")
@DiscriminatorValue(value = "B")
public class BillingAddressPojo extends AddressPojo implements Serializable {
    /**
     * explicit set serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    protected boolean isAlsoShipping;

    // JPA requires each @Entity class have a default constructor
    public BillingAddressPojo() {
    }

    public boolean isAlsoShipping() {
        return isAlsoShipping;
    }
    public void setAlsoShipping(boolean isAlsoShipping) {
        this.isAlsoShipping = isAlsoShipping;
    }
    
}