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

import javax.persistence.*;

/**
*
* Description: model for the Store object
*/
@Entity
@Table(name = "STORES")
@AttributeOverride(name = "id", column = @Column(name = "STORE_ID"))
public class StorePojo extends PojoBase {
    
    protected String storeName;
    protected Set<ProductPojo> products = new HashSet<>();

    // JPA requires each @Entity class have a default constructor
    public StorePojo() {
    }

    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    @JsonSerialize(using = ProductSerializer.class)
      //Discovered what I think is a bug: you should be able to list them in any order,
      //but it turns out, EclipseLink's JPA implementation needs the @JoinColumn StorePojo's PK
      //first, the 'inverse' to ProductPojo's PK second
    @ManyToMany(mappedBy = "stores")
    public Set<ProductPojo> getProducts() {
        return products;
    }
    public void setProducts(Set<ProductPojo> products) {
        this.products = products;
    }

}
