/*****************************************************************c******************o*******v******id********
 * File: OrderLinePk.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.Objects;

/**
*
* JPA helper class: Composite Primary Key class for OrderLine - two columns
* ORDERLINE_NO identifies which orderLine within an Order (i.e. line 1, line 2, line 3)
* OWNING_ORDER_ID identifies which Order this orderLine belongs to
* 
*/

@Embeddable
public class OrderLinePk implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Order id which this order line embedded
     */
    protected int owningOrderId;
    
    /**
     * Order line number
     */
    protected int orderLineNo;

    /**
     * Getter for owningOrderId
     * 
     * @return current value for owningOrderId
     */
    @Column(name = "OWING_ORDER_ID")
    public int getOwningOrderId() {
        return owningOrderId;
    }
    
    /**
     * Setter for owningOrderId
     * 
     * @param owningOrderId new value for owningOrderId
     */
    public void setOwningOrderId(int owningOrderId) {
        this.owningOrderId = owningOrderId;
    }

    /**
     * Getter for orderLineNo
     * 
     * @return current value for orderLineNo
     */
    @Column(name = "ORDERLINE_NO")
    public int getOrderLineNo() {
        return orderLineNo;
    }
    
    /**
     * Setter for orderLineNo
     * 
     * @param orderLineNo new value for orderLineNo
     */
    public void setOrderLineNo(int orderLineNo) {
        this.orderLineNo = orderLineNo;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(orderLineNo, owningOrderId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OrderLinePk)) {
            return false;
        }
        OrderLinePk other = (OrderLinePk) obj;
        return orderLineNo == other.orderLineNo && owningOrderId == other.owningOrderId;
    }

}