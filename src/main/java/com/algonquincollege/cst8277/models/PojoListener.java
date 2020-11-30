/*****************************************************************c******************o*******v******id********
 * File: PojoListener.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 ** update by :
 * Lai Shan Law (040595733)
 * Siyang Xiong (040938012)
 * Angela Zhao (040529234)
 * 
 * @date 2020-11-21
 */
package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PojoListener {

    @PrePersist
    public void setCreatedOnDate(PojoBase pojo) {
        LocalDateTime now = LocalDateTime.now();
        pojo.setCreatedDate(now);
        pojo.setUpdatedDate(now);
    }
    
    @PreUpdate
    public void setUpdatedDate(PojoBase pojo) {
        pojo.setUpdatedDate(LocalDateTime.now());
    }
}