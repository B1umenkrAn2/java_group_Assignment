/*****************************************************************c******************o*******v******id********
 * File: ProductPojo.java
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
*
* Description: model for the Product object
*/

@Entity(name="Product")
@Table(name = "PRODUCT")
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name="PRODUCT_ID"))
public class ProductPojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Member variables
     */
    protected String description;
    protected String serialNo;
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
    
    // Additional Mapping for M:N properties
    @JsonInclude(Include.NON_NULL)
    /**
     * Getter for stores selling this product. It has many-to-many relationship with StorePojo
     * 
     * @return current list of stores selling this product
     */
    @ManyToMany(mappedBy = "products",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
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