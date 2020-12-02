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
@Entity(name="Product")
@Table(name = "PRODUCT")
@AttributeOverride(name = "id", column = @Column(name = "PRODUCT_ID"))
public class ProductPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Product description
     */
    protected String description;
    /**
     * Product serial number
     */
    protected String serialNo;
    /**
     * List of stores which sell this product
     */
    protected Set<StorePojo> stores = new HashSet<>();


    // JPA requires each @Entity class have a default constructor
    public ProductPojo() {
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
     * Getter for serialNo
     * 
     * @return current value for serialNo
     */
    public String getSerialNo() {
        return serialNo;
    }
    
    /**
     * Setter for serialNo
     * 
     * @param serialNo new value for serialNo
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    
    /**
     * Getter for stores selling this product. It has many-to-many relationship with StorePojo
     * 
     * @return current list of stores selling this product
     */
    @JsonInclude(Include.NON_NULL)
    @ManyToMany
    @JoinTable(name = "STORES_PRODUCTS",joinColumns = {@JoinColumn(name = "PRODUCT_ID",referencedColumnName = "PRODUCT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "STORE_ID",referencedColumnName = "STORE_ID")})
    public Set<StorePojo> getStores() {
        return stores;
    }

    /**
     * Sett for stores to sell this product
     * 
     * @param stores new values of list of stores selling this product
     */
    public void setStores(Set<StorePojo> stores) {
        this.stores = stores;
    }
}