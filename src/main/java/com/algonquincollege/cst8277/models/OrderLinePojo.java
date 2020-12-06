/*****************************************************************c******************o*******v******id********
 * File: OrderLinePojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
*
* Description: model for the OrderLine object
*/
@Entity
@Table(name = "ORDERLINE")
public class OrderLinePojo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Primary key
     */
    protected OrderLinePk primaryKey;
    /**
     * Owning order
     */
    protected OrderPojo owningOrder;
    /**
     * Total amount of this order line
     */
    protected Double amount;
    /*
     * Unit price of this order line
     */
    protected Double price;
    /**
     * Ordered product
     */
    protected ProductPojo product;

    // JPA requires each @Entity class have a default constructor
    public OrderLinePojo() {
    }

    /**
     * Getter for primaryKey, it is a embedded class variable
     * 
     * @return current value of primaryKey
     */
    @EmbeddedId
    public OrderLinePk getPk() {
        return primaryKey;
    }
    
    /**
     * Setter for primaryKey
     * 
     * @param primaryKey new value for primaryKey
     */
    public void setPk(OrderLinePk primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    /**
     * Getter for owningOrder. It has many-to-one relationship with OrderPojo. 
     * 
     * @return current value for owningOrder
     */
    @JsonBackReference
    @MapsId("owningOrderId")
    @ManyToOne
    @JoinColumn(name = "OWNING_ORDER_ID")
    public OrderPojo getOwningOrder() {
        return owningOrder;
    }
    
    /**
     * Setter for owningOrder
     * 
     * @param owningOrder new value for owningOrder
     */
    public void setOwningOrder(OrderPojo owningOrder) {
        this.owningOrder = owningOrder;
    }
      
    /**
     * Getter for amount
     * 
     * @return current value for amount
     */
    public Double getAmount() {
        return amount;
    }
    
    /**
     * Setter for amount
     * 
     * @param amount new value for amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    /**
     * Getter for unit price
     * 
     * @return current value for unit price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Setter for unit price
     * 
     * @param price new value for unit price
     */
    public void setPrice(Double price) {
        this.price = price;
    }
    
    /**
     * Getter for product, it has one-to-one relationship with ProductPojo
     * 
     * @return current value for product
     */
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID")
    public ProductPojo getProduct() {
        return product;
    }
    
    /**
     * Setter for product
     * 
     * @param product new value for product
     */
    public void setProduct(ProductPojo product) {
        this.product = product;
    }
}