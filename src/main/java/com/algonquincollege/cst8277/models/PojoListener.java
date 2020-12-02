/*****************************************************************c******************o*******v******id********
 * File: PojoListener.java
 * Course materials (20F) CST 8277
 *
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 
 * JPA listener class for audit member fields
 *
 */
public class PojoListener {
    
    /**
     * Pre-Persit operation to set "created" and "updated"
     * audit member fields
     *  
     * @param pojoBase the base class which has the two audit fields
     */
    @PrePersist
    public void setCreatedOnDate(PojoBase pojoBase) {
        LocalDateTime now = LocalDateTime.now();
        pojoBase.setCreatedDate(now);
        pojoBase.setUpdatedDate(now);
    }

    /**
     *  Pre-Update operation to set "updated" audtit member field
     *  
     * @param pojoBase the base class which has the two audit fields
     */
    @PreUpdate
    public void setUpdatedDate(PojoBase pojoBase) {
        pojoBase.setUpdatedDate(LocalDateTime.now());
    }
}