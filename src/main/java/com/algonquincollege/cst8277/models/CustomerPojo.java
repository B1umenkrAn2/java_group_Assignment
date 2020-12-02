/*****************************************************************c******************o*******v******id********
 * File: CustomerPojo.java
 * Course materials (20F) CST 8277
 *
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


import javax.persistence.*;

/**
 * Description: model for the Customer object
 */
@Entity(name = "Customer")
@Table(name = "CUSTOMER")
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "CUSTOMER_ID"))
@NamedQuery(name="allCustomers", query = "select c from Customer c")
public class CustomerPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ALL_CUSTOMERS_QUERY_NAME = "allCustomers";

    /**
     * First name of this customer
     */
    protected String firstName;
    /**
     * Last name of this customer
     */
    protected String lastName;
    /**
     * Email of this customer
     */
    protected String email;
    /**
     * Phone number of this customer
     */
    protected String phoneNumber;


    /**
     * Billing address of this customer. It has 1:1 relationship with Address table
     */
    protected AddressPojo billingAddress;
    /**
     * Shipping address of this customer. It has 1:1 relationship with Address table
     */
    protected AddressPojo shippingAddress;
    /**
     * Orders placed by this customer. It has 1:M relationship with Order table
     */
    protected List<OrderPojo> Orders = new ArrayList<>();;

    // JPA requires each @Entity class have a default constructor
    public CustomerPojo() {
    }

    /**
     * Getter for firstName
     * 
     * @return current value for firstName
     */
    @Column(name = "FNAME")
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for firstName
     * 
     * @param firstName new value for firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for lastName
     * 
     * @return current value for lastName
     */
    @Column(name = "LNAME")
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for lastName
     * 
     * @param lastName new value for lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for email
     * 
     * @return current value for email
     */
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email
     * 
     * @param email new value for email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for phoneNumber
     * 
     * @return current value for phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Setter for phoneNumber 
     * 
     * @param phoneNumber new value for phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    // Additional Mappings for 1:1, 1:M properties
    
    /**
     * Getter for shippingAddress, it has one-to-one relationship with ShippingAddressPojo
     * 
     * @return current value for shippingAddress
     */
    @OneToOne
    @JoinColumn(name = "SHIPPING_ADDR")
    public AddressPojo getShippingAddress() {
        return shippingAddress;
    }
    
    /**
     * Setter for shippingAddress
     * 
     * @param newAddress new value for shippingAddress
     */
    public void setShippingAddress(AddressPojo newAddress) {
        this.shippingAddress = newAddress;
    }
    
    /**
     * Getter for billingAddress, it has one-to-one relationship with BillingAddressPojo
     * 
     * @return current value for billingAddress
     */
    @OneToOne
    @JoinColumn(name = "BILLING_ADDR")
    public AddressPojo getBillingAddress() {
        return billingAddress;
    }
    
    /**
     * Setting for billingAddress
     * 
     * @param newAddress new value for billingAddress
     */
    public void setBillingAddress(AddressPojo newAddress) {
        this.billingAddress = newAddress;
    }
    
    /**
     * Getter for orders, it has one-to-many relationship with OrderPojo
     * 
     * @return current value for orders
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "owningCustomer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    public List<OrderPojo> getOrders() {
        return Orders;
    }

    /**
     * Setter for orders
     * 
     * @param orders new value for orders
     */
    public void setOrders(List<OrderPojo> orders) {
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