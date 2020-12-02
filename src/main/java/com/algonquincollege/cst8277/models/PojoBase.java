/*****************************************************************c******************o*******v******id********
 * File: PojoBase.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

/**
 * Abstract class that is base of (class) hierarchy for all c.a.cst8277.models @Entity classes
 */

@MappedSuperclass
@Access(AccessType.PROPERTY) // NOTE: by using this annotations, any annotation on a field is ignored without warning
@EntityListeners(PojoListener.class)
public abstract class PojoBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Object id
     */
    protected int id;
    /**
     * Object created timestamp
     */
    protected LocalDateTime created;
    /**
     * Object updated timestamp
     */
    protected LocalDateTime updated;
    /**
     * Object tracking version
     */
    protected int version;

    /**
     * Getter of id
     * 
     * @return current value of id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    
    /**
     * Setter of id
     * 
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    
    @JsonIgnore
    @Version
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Getter of "created" member variable
     * 
     * @return
     */
    @JsonIgnore
    @Column(name = "CREATED")
    public LocalDateTime getCreatedDate() {
        return created;
    }

    /**
     * Setter of "created" member variable
     *
     * @param created new value for "created"
     */
    public void setCreatedDate(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Getter of "updated" member variable
     * 
     * @return current value for "updated"
     */
    @JsonIgnore
    @Column(name = "UPDATED")
    public LocalDateTime getUpdatedDate() {
        return updated;
    }

    /**
     * Setter of "updated" member variable
     * 
     * @param updated new value for "updated" member variable
     */
    public void setUpdatedDate(LocalDateTime updated) {
        this.updated = updated;
    }

    // It is a good idea for hashCode() to use the @Id property
    // since it maps to table's PK and that is how Db determine's identity
    // (same for equals()
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