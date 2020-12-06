/*****************************************************************c******************o*******v******id********
 * File: PojoListener.java
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

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class PojoListener {

    /**
     *  use to setup new customer updateDate,createDate and version
     * @param cust new customer
     */
    @PrePersist
    public void setCreatedOnDate(PojoBase cust) {
        LocalDateTime now = LocalDateTime.now();
        cust.setCreated(now);
        //might as well call setUpdatedDate as well
        cust.setUpdated(now);
        cust.setVersion(1);
    }

    /**
     *  use to setup  customer updateDate and version before update
     * @param cust edit customer
     */
    @PreUpdate
    public void setUpdatedDate(PojoBase cust) {
        cust.setUpdated(LocalDateTime.now());
        cust.setVersion(cust.getVersion()+1);
    }
}