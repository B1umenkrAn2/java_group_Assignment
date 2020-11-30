/*****************************************************************c******************o*******v******id********
 * File: ShippingAddressPojo.java
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

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity(name = "ShippingAddress")
@Table(name = "SHIPPINGADDRESS")
@PrimaryKeyJoinColumn(name = "SHIPPING_ADDR_ID")
@DiscriminatorValue(value = "S")
public class ShippingAddressPojo extends AddressPojo implements Serializable  {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    // JPA requires each @Entity class have a default constructor
    public ShippingAddressPojo() {
    }

}