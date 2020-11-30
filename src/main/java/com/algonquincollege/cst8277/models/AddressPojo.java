/*****************************************************************c******************o*******v******id********
 * File: AddressPojo.java
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

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
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
    @Type(value = BillingAddressPojo.class, name = "B"),
    @Type(value = ShippingAddressPojo.class, name = "S")
})
//@MappedSuperclass
@Entity(name = "Cust_addr")
@Table(name = "CUST_ADDR")
@AttributeOverride(name = "id", column = @Column(name = "ADDR_ID"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ADDR_TYPE", columnDefinition = "VARCHAR", length = 1)
public abstract class AddressPojo extends PojoBase implements Serializable {

    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    protected String street;
    protected String city;
    protected String country;
    protected String postal;
    protected String state;

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public AddressPojo() {
        super();
    }
    /*
     * @return city  a city
     */
    @Column(name = "CITY")
    public String getCity() {
        return city;
    }
    /*
     * @param city  city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
    /*
     * @return street  a street
     */
    @Column(name = "STREET")
    public String getStreet() {
        return street;
    }
    /*
     * @param street  a street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }
    /*
     * @return postal  a postal code
     */
    @Column(name = "POSTAL_CODE")
    public String getPostal() {
        return postal;
    }
    /*
     * @param postal  a postal code to set
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }
    /*
     * @return state  a state
     */
    @Column(name = "STATE")
    public String getState() {
        return state;
    }
    /*
     * @param state  a state to set
     */
    public void setState(String state) {
        this.state = state;
    }
    /*
     * @return country  a country
     */
    @Column(name = "COUNTRY")
    public String getCountry() {
        return country;
    }
    /*
     * @param country  a country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
}