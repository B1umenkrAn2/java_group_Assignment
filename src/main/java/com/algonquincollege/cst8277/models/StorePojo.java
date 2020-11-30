/*****************************************************************c******************o*******v******id********
 * File: StorePojo.java
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.algonquincollege.cst8277.rest.ProductSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
/**
*
* Description: model for the Store object
*/
@Entity(name = "Stores")
@Table(name = "STORES")
@AttributeOverride(name = "id", column = @Column(name="STORE_ID"))
public class StorePojo extends PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String storeName;
    protected Set<ProductPojo> products = new HashSet<>();

    // JPA requires each @Entity class have a default constructor
    public StorePojo() {
    }
    
    @Column(name = "STORENAME")
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    /*
     * Using hollowProducts in ProductSerializer class to replace getProducts()
     */
    @JsonSerialize(using = ProductSerializer.class)
    @ManyToMany
    @JoinTable(name="STORES_PRODUCTS", joinColumns=@JoinColumn(name="STORE_ID", referencedColumnName="STORE_ID"),
    inverseJoinColumns=@JoinColumn(name="PRODUCT_ID", referencedColumnName="PRODUCT_ID"))
      //Discovered what I think is a bug: you should be able to list them in any order,
      //but it turns out, EclipseLink's JPA implementation needs the @JoinColumn StorePojo's PK
      //first, the 'inverse' to ProductPojo's PK second
    public Set<ProductPojo> getProducts() {
        return products;
    }
    public void setProducts(Set<ProductPojo> products) {
        this.products = products;
    }
    public void addProducts(ProductPojo p) {
        if (p != null) {
            getProducts().add(p);
            p.addStore(this);
        }
    }
}
