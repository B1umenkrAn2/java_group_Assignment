/*****************************************************************c******************o*******v******id********
 * File: OrderLinePk.java
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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderLinePk implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;

    protected int owningOrderId;
    protected int orderLineNo;

    @Column(name = "OWNING_ORDER_ID")
    public int getOwningOrderId() {
        return owningOrderId;
    }
    public void setOwningOrderId(int owningOrderId) {
        this.owningOrderId = owningOrderId;
    }
    @Column(name = "ORDERLINE_NO")
    public int getOrderLineNo() {
        return orderLineNo;
    }
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