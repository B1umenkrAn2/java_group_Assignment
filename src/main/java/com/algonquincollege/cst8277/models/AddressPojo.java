/*****************************************************************c******************o*******v******id********
 * File: AddressPojo.java
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

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
*
* Description: model for the Address object
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
  @JsonSubTypes({
    @Type(value = BillingAddressPojo.class, name = "BILLING_ADDR"),
    @Type(value = ShippingAddressPojo.class, name = "SHIPPING_ADDR")
})

@Entity(name = "Address")
@Table(name = "CUST_ADDR")
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "ADDR_ID"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ADDR_TYPE")
public abstract class AddressPojo extends PojoBase implements Serializable {

    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Street of Address
     */
    protected String street;
    /**
     * City of Address
     */
    protected String city;
    /**
     * State or Province of Address
     */
    protected String state;
    /**
     * Country of Address
     */
    protected String country;
    /**
     * Postal code of Address
     */
    protected String postal;


    /**
     * JPA requires each @Entity class have a default constructor
     */
    public AddressPojo() {
        super();
    }
    
    /**
     * Getter for city
     * 
     * @return current city value
     */
    @Column(name = "CITY")
    public String getCity() {
        return city;
    }
    
    /**
     * Setter for city
     * 
     * @param city new value for city
     */
    public void setCity(String city) {
        this.city = city;
    }
    
    /**
     * Getter for street
     * 
     * @return current street value
     */
    @Column(name = "STREET")
    public String getStreet() {
        return street;
    }
    
    /**
     * Setter for street
     * 
     * @param street new value for street
     */
    public void setStreet(String street) {
        this.street = street;
    }
    
    /**
     * Getter for state
     * 
     * @return current value for state
     */
    @Column(name = "STATE")
    public String getState() {
        return state;
    }

    /**
     * Setter for state
     * 
     * @param state new value for state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter for postal
     * 
     * @return current value of postal
     */
    @Column(name = "POSTAL_CODE")
    public String getPostal() {
        return postal;
    }
    
    /**
     * Setter for postal
     * 
     * @param postal new value for postal
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }
    
    /**
     * Getter for country
     * 
     * @return current value for country
     */
    @Column(name = "COUNTRY")
    public String getCountry() {
        return country;
    }
    
    /**
     * Setter for country
     * 
     * @param country new value for setter
     */
    public void setCountry(String country) {
        this.country = country;
    }
}