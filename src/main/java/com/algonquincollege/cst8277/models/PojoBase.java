/*****************************************************************c******************o*******v******id********
 * File: PojoBase.java
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Abstract class that is base of (class) hierarchy for all c.a.cst8277.models @Entity classes
 */
@MappedSuperclass
@Access(AccessType.PROPERTY) // NOTE: by using this annotations, any annotation on a field is ignored without warning
@EntityListeners({PojoListener.class})
public abstract class PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected LocalDateTime created;
    protected LocalDateTime updated;
    protected int version;

    /*
     * @return the value for id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    /*
     * @param id new value for id
     */
    public void setId(int id) {
        this.id = id;
    }

    /*
     * report the version of a customer record
     * @return version
     */
    @Version
    public int getVersion() {
        return version;
    }
    /*
     * set version of a customer record,
     * it is generated and managed by JPA
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /*
     * report Date & Time when create a new record
     * @return LocalDateTime
     */
    @Column(name = "CREATED")
    public LocalDateTime getCreatedDate() {
        return created;
    }
    /*
     * set Date & Time for a new customer record created,
     * it is generated and managed by JPA
     * @param created
     */
    public void setCreatedDate(LocalDateTime created) {
        this.created = created;
    }

    /*
     * report Date & Time when updated the existing record
     * @return LocalDateTime
     */
    @Column(name = "UPDATED")
    public LocalDateTime getUpdatedDate() {
        return updated;
    }
    /*
     * set Date & Time for a existing customer record updated,
     * it is generated and managed by JPA
     * @param created
     */
    public void setUpdatedDate(LocalDateTime updated) {
        this.updated = updated;
    }
    /*
     * For hashCode() to use the @Id property
     * since it maps to table's PK and that is how Db determine's identity
     * (same for equals())
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PojoBase)) {
            return false;
        }
        PojoBase other = (PojoBase)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}