/*****************************************************************c******************o*******v******id********
 * File: ShippingAddressPojo.java
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
 *
 * Description: model for the ShippingAddress object
 */
@Entity(name="ShippingAddress")
@Table(name = "CUST_ADDR")
@Access(AccessType.PROPERTY)
@DiscriminatorValue("SHIPPING_ADDR")
public class ShippingAddressPojo extends AddressPojo implements Serializable  {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    // JPA requires each @Entity class have a default constructor
    public ShippingAddressPojo() {
    }

}