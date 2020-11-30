/*****************************************************************c******************o*******v******id********
 * File: OrderLinePojo.java
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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
*
* Description: model for the OrderLine object
*/
@Entity(name = "Orderline")
@Table(name = "ORDERLINE")
@Access(AccessType.PROPERTY)
public class OrderLinePojo implements Serializable {
    private static final long serialVersionUID = 1L;

    protected OrderLinePk primaryKey;
    protected OrderPojo owningOrder;
    protected Double amount;
    protected Double price;
    protected ProductPojo product;
    
    // JPA requires each @Entity class have a default constructor
    public OrderLinePojo() {
    }
    /*
     * @return amount   an amount
     */
    @Column(name = "AMOUNT")
    public Double getAmount() {
        return amount;
    }
    /*
     * @param amount   an amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    /*
     * @return price   a price
     */
    @Column(name = "PRICE")
    public Double getPrice() {
        return price;
    }
    /*
     * @param price   a price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }
    /*
     * report a set of primary key which contains orderLine num and order ID
     * @return primaryKey
     */
    @EmbeddedId
    public OrderLinePk getPk() {
        return primaryKey;
    }
    /*
     * a primary key which contains orderLine num and order ID to set
     * @param primaryKey
     */
    public void setPk(OrderLinePk primaryKey) {
        this.primaryKey = primaryKey;
    }
    /*
     * M:1, ORDERLINE : ORDER_TBL
     * Many-to-One association between the ORDERLINE and ORDER_TBL tables.
     * 
     */
    @JsonBackReference
    @MapsId("owningOrderId")
    @ManyToOne
    //@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "OWNING_ORDER_ID", referencedColumnName="ORDER_ID")
    /*
     * @return owningOrder   an order
     */
    public OrderPojo getOwningOrder() {
        return owningOrder;
    }
    /*
     * @param owningOrder   an order to set
     */
    public void setOwningOrder(OrderPojo owningOrder) {
        this.owningOrder = owningOrder;
    }
    /*
     * 1:1, ORDERLINE : PRODUCT
     * Many-to-One association between the ORDERLINE and PRODUCT tables.
     */
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    /*
     * @return product   a product
     */
    public ProductPojo getProduct() {
        return product;
    }
    /*
     * @param product   a product to set
     */
    public void setProduct(ProductPojo product) {
        this.product = product;
    }

}