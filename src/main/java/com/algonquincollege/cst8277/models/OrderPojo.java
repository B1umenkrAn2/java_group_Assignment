/*****************************************************************c******************o*******v******id********
 * File: OrderPojo.java
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
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
*
* Description: model for the Order object
*/
@Entity(name = "Order")
@Table(name = "ORDER_TBL")
@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))
public class OrderPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Order description
     */
    protected String description;
    /**
     * Sub set of orderlines belonged to this order
     */
    protected Set<OrderLinePojo> orderlines = new HashSet<>();
    /**
     * Customer placed this order
     */
    protected CustomerPojo owningCustomer;
    
    // JPA requires each @Entity class have a default constructor
	public OrderPojo() {
	}
		
    /**
     * Getter for description
     * 
     * @return current value for description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Setter for description
     * 
     * @param description new value for description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Getter for orderlines. It has one-to-many relationship with OrderLinePojo
     * 
     * @return current value for orderlines
     */
    @JsonManagedReference
    @OneToMany(mappedBy= "owningOrder", cascade = CascadeType.ALL,orphanRemoval= true)
	public Set<OrderLinePojo> getOrderlines() {
		return this.orderlines;
	}
    
    /**
     * Setter for orderlines
     * 
     * @param orderlines new value for orderlines
     */
	public void setOrderlines(Set<OrderLinePojo> orderlines) {
		this.orderlines = orderlines;
	}
	
    /**
     * Add a specified orderline object
     * 
     * @param orderline the specified orderline object
     * @return upadted list of orderlines owned by this order after adding
     */
	public OrderLinePojo addOrderline(OrderLinePojo orderline) {
		getOrderlines().add(orderline);
		orderline.setOwningOrder(this);
		return orderline;
	}
	
	/**
     * Remove the specified orderline from list of orderlined owned by this order
     * 
     * @param orderline the specified orderline to be removed
     * @return the updated list of orderlined owned by this order after removal
     */
	public OrderLinePojo removeOrderline(OrderLinePojo orderline) {
		getOrderlines().remove(orderline);
        orderline.setOwningOrder(null);
		return orderline;
	}
	
    /**
     * Getter for the owningCustomer. It has many-to-one relationship with CustomerPojo
     * 
     * @return the current value for owningCustomer
     */
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "OWNING_CUST_ID")
	public CustomerPojo getOwningCustomer() {
		return this.owningCustomer;
	}
	
    /**
     * Setter for owningCustomer
     * 
     * @param owner new value for owningCustomer
     */
	public void setOwningCustomer(CustomerPojo owner) {
		this.owningCustomer = owner;
	}

}