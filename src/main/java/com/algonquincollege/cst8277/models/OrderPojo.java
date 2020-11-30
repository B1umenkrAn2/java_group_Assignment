/*****************************************************************c******************o*******v******id********
 * File: OrderPojo.java
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
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
*
* Description: model for the Order object
*/
@Entity(name = "Order_tbl")
@Table(name = "ORDER_TBL")
@AttributeOverride(name = "id", column = @Column(name="ORDER_ID"))
public class OrderPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String description;
    protected List<OrderLinePojo> orderlines;
    protected CustomerPojo owningCustomer;
    
    // JPA requires each @Entity class have a default constructor
    public OrderPojo() {
    }
    /*
     * report the order description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /*
     * set a order description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /*
     * 1:M, Order : OrderLine
     * One-to-Many association between the ORDER_TBL and ORDERLINE tables.
     */
    @OneToMany(mappedBy = "owningOrder",cascade = CascadeType.ALL, orphanRemoval = true)
    /*
     * report a list of orderLine in the order
     * @return orderlines   a list of orderLine
     */
    public List<OrderLinePojo> getOrderlines() {
        return this.orderlines;
    }
    /*
     * set a list of orderLine in the order
     * @param orderlines   a list of orderLine
     */
    public void setOrderlines(List<OrderLinePojo> orderlines) {
        this.orderlines = orderlines;
    }
    /*
     * add a orderLine in the list
     * @param orderline  a orderLine
     * @return orderline  a orderLine
     */
    public OrderLinePojo addOrderline(OrderLinePojo orderline) {
        getOrderlines().add(orderline);
        orderline.setOwningOrder(this);
        return orderline;
    }
    /*
     * remove a orderLine from the list
     * @param orderline  a orderLine
     * @return orderline  a orderLine
     */
    public OrderLinePojo removeOrderline(OrderLinePojo orderline) {
        getOrderlines().remove(orderline);
        //orderline.setOwningOrder(null);
        return orderline;
    }
    /*
     * M:1, Order : Customer
     * Many-to-One association between the ORDER_TBL and CUSTOMER tables.
     */
    //@ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "OWNING_CUST_ID")
    /*
     * report a customer
     * @return owningCustomer
     */
    public CustomerPojo getOwningCustomer() {
        return this.owningCustomer;
    }
    /*
     * set a customer
     * @param owner
     */
    public void setOwningCustomer(CustomerPojo owner) {
        this.owningCustomer = owner;
    }
    
    
}