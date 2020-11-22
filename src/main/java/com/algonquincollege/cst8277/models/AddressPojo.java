/*****************************************************************c******************o*******v******id********
 * File: AddressPojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : I. Am. A. Student 040nnnnnnn
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
@MappedSuperclass
@Entity
@Table(name = "CUST_ADDR")
@AttributeOverride(name = "id", column = @Column(name = "ADDR_ID"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ADDR_TYPE", length = 1)
public abstract class AddressPojo extends PojoBase implements Serializable {

    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    protected String street;
    protected String city;
    protected String country;
    protected String postal;
    protected String state;
    protected Boolean alsoShipping;


    /**
     * JPA requires each @Entity class have a default constructor
     */
    public AddressPojo() {
        super();
    }
    @Column(name = "CITY")
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    @Column(name = "COUNTRY")
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    @Column(name = "POSTAL_CODE")
    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }
    @Column(name = "STATE")
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    @Column(name = "STREET")
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    @Column(name = "ALSOSHIPPING")
    public Boolean getAlsoShipping() {
        return alsoShipping;
    }

    public void setAlsoShipping(Boolean alsoShipping) {
        this.alsoShipping = alsoShipping;
    }
}