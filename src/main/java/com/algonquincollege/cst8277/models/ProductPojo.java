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
import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
*
* Description: model for the Product object
*/
@Entity
@Table(name = "PRODUCT")
@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
public class ProductPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String description;
    protected String serialNo;
    protected Set<StorePojo> stores = new HashSet<>();



    // JPA requires each @Entity class have a default constructor
    public ProductPojo() {
    }
    
    /**
     * @return the value for firstName
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description new value for description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    
    @JsonInclude(Include.NON_NULL)
    @ManyToMany
    @JoinTable(name = "STORES_PRODUCTS",joinColumns = {@JoinColumn(name = "PRODUCT_ID",referencedColumnName = "PRODUCT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "STORE_ID",referencedColumnName = "STORE_ID")})
    public Set<StorePojo> getStores() {
        return stores;
    }

    public void setStores(Set<StorePojo> stores) {
        this.stores = stores;
    }

}