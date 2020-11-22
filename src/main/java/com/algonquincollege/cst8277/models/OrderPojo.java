/*****************************************************************c******************o*******v******id********
 * File: OrderPojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : I. Am. A. Student 040nnnnnnn
 */
package com.algonquincollege.cst8277.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
*
* Description: model for the Order object
*/
@Entity
@Table(name = "ORDER_TBL")
@AttributeOverride(name = "id", column = @Column(name = "ORDER_ID"))
public class OrderPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String description;
    protected Set<OrderLinePojo> orderlines = new HashSet<>();
    protected CustomerPojo owningCustomer;
    
    // JPA requires each @Entity class have a default constructor
	public OrderPojo() {
	}
	@Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @OneToMany(mappedBy= "owningOrder", cascade = CascadeType.ALL,orphanRemoval= true)
	public Set<OrderLinePojo> getOrderlines() {
		return this.orderlines;
	}
	public void setOrderlines(Set<OrderLinePojo> orderlines) {
		this.orderlines = orderlines;
	}
	public OrderLinePojo addOrderline(OrderLinePojo orderline) {
		getOrderlines().add(orderline);
		orderline.setOwningOrder(this);
		return orderline;
	}
	public OrderLinePojo removeOrderline(OrderLinePojo orderline) {
		getOrderlines().remove(orderline);
        orderline.setOwningOrder(null);
		return orderline;
	}
	@ManyToOne
	@JoinColumn(name = "OWNING_CUST_ID")
	public CustomerPojo getOwningCustomer() {
		return this.owningCustomer;
	}
	public void setOwningCustomer(CustomerPojo owner) {
		this.owningCustomer = owner;
	}

}