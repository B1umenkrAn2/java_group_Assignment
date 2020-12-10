/*****************************************************************c******************o*******v******id********
 * File: StorePojo.java
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 * 
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.algonquincollege.cst8277.rest.ProductSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * Description: model for the Store object
 */
@Entity(name = "Store")
@Table(name = "STORES")
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "STORE_ID"))
public class StorePojo extends PojoBase {
    private static final long serialVersionUID = 1L;

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
     * @param storeName
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * Getter for products this store is selling. It has many-to-many relation ship ProductPojo
     * 
     * @return current list of products being sold by this store
     */
    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = ProductSerializer.class)
    @ManyToMany
    @JoinTable(name = "STORES_PRODUCTS", joinColumns=@JoinColumn(name= "STORE_ID", referencedColumnName="STORE_ID"), 
        inverseJoinColumns=@JoinColumn(name="PRODUCT_ID", referencedColumnName="PRODUCT_ID"))
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
