/*****************************************************************c******************o*******v******id********
 * File: CustomerPojo.java
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

/**
 * Description: model for the Customer object
 */
@Entity
@Table(name = "CUSTOMER")
@AttributeOverride(name = "id", column = @Column(name = "CUSTOMER_ID"))
public class CustomerPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ALL_CUSTOMERS_QUERY_NAME = "allCustomers";

    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;


    //1:1
    protected AddressPojo billingAddress;
    protected AddressPojo shippingAddress;
    //1:M
    protected Set<OrderPojo> Orders;

    // JPA requires each @Entity class have a default constructor
    public CustomerPojo() {
    }

    /**
     * @return the value for firstName
     */
    @Column(name = "FNAME")
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName new value for firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the value for lastName
     */
    @Column(name = "LNAME")
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName new value for lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "PHONENUMBER")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @OneToOne
    @JoinColumn(name = "BILLING_ADDR")
    public AddressPojo getBillingAddress() {
        return this.billingAddress;
    }

    public void setBillingAddress(AddressPojo BILLING_ADDR) {
        this.billingAddress = BILLING_ADDR;
    }

    @OneToOne
    @JoinColumn(name = "SHIPPING_ADDR")
    public AddressPojo getShippingAddress() {
        return this.shippingAddress;
    }

    public void setShippingAddress(AddressPojo SHIPPING_ADDR) {
        this.shippingAddress = SHIPPING_ADDR;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "owningCustomer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    public Set<OrderPojo> getOrders() {
        return Orders;
    }

    public void setOrders(Set<OrderPojo> orders) {
        Orders = orders;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("Customer [id=")
                .append(id)
                .append(", ");
        if (firstName != null) {
            builder
                    .append("firstName=")
                    .append(firstName)
                    .append(", ");
        }
        if (lastName != null) {
            builder
                    .append("lastName=")
                    .append(lastName)
                    .append(", ");
        }
        if (phoneNumber != null) {
            builder
                    .append("phoneNumber=")
                    .append(phoneNumber)
                    .append(", ");
        }
        if (email != null) {
            builder
                    .append("email=")
                    .append(email)
                    .append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

}