/*****************************************************************c******************o*******v******id********
 * File: StorePojo.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.algonquincollege.cst8277.rest.ProductSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

/**
*
* Description: model for the Store object
*/
@Entity
@Table(name = "STORES")
@AttributeOverride(name = "id", column = @Column(name = "STORE_ID"))
public class StorePojo extends PojoBase {
    
    /**
     * Store name
     */
    protected String storeName;
    /**
     * List of products being sold by this store
     */
    protected Set<ProductPojo> products = new HashSet<>();

    // JPA requires each @Entity class have a default constructor
    public StorePojo() {
    }

    /**
     * Getter for storeName
     * 
     * @return current value for storeName
     */
    public String getStoreName() {
        return storeName;
    }
    
    /**
     * Setter for storeName
     * 
     * @param storeName new value for storeName
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    /**
     * Getter for products this store is selling. It has many-to-many relation ship ProductPojo
     * 
     * @return current list of products being sold by this store
     */
    @JsonSerialize(using = ProductSerializer.class)
      //Discovered what I think is a bug: you should be able to list them in any order,
      //but it turns out, EclipseLink's JPA implementation needs the @JoinColumn StorePojo's PK
      //first, the 'inverse' to ProductPojo's PK second
    @ManyToMany(mappedBy = "stores")
    public Set<ProductPojo> getProducts() {
        return products;
    }
    
    /**
     * Setter for products
     * 
     * @param products new list of products to be sold by this store
     */
    public void setProducts(Set<ProductPojo> products) {
        this.products = products;
    }
}
