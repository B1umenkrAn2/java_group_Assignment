/*****************************************************************c******************o*******v******id********
 * File: CustomIdentityStoreJPAHelper.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 *
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277.security;

import static com.algonquincollege.cst8277.utils.MyConstants.*;
import static java.util.Collections.emptySet;

import java.util.Set;
import java.util.stream.Collectors;


import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;


import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;

import static com.algonquincollege.cst8277.models.SecurityUser.SECURITY_USER_BY_NAME_QUERY;

/*
 * JPA Helper class for CustomIdentityStore
 * 
 * Stateless Session bean should also be a Singleton
 */

@Singleton
public class CustomIdentityStoreJPAHelper {

    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;

    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    /**
     * Find security user by its username
     * 
     * @param username the username for the scurity user
     * @return found security user
     */
    public SecurityUser findUserByName(String username) {
        SecurityUser user = null;
        try {
            TypedQuery<SecurityUser> query = em.createNamedQuery(SECURITY_USER_BY_NAME_QUERY, SecurityUser.class);
            query.setParameter("param1", username);
            user = query.getSingleResult();
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Find names of security rolls granted to the security user
     * 
     * @param username the username for the security user
     * @return list of roll names
     */
    public Set<String> findRoleNamesForUser(String username) {
        Set<String> roleNames = emptySet();
        SecurityUser securityUser = findUserByName(username);
        if (securityUser != null) {
            roleNames = securityUser.getRoles().stream().map(SecurityRole::getRoleName).collect(Collectors.toSet());
        }
        return roleNames;
    }

    /**
     * Add/save a new security user
     * 
     * @param user the scurity user to be saved
     */
    @Transactional
    public void saveSecurityUser(SecurityUser user) {
        em.persist(user);
    }

    /**
     * Add/save a new security role
     * 
     * @param role the security role to be saved
     */
    @Transactional
    public void saveSecurityRole(SecurityRole role) {
        em.persist(role);
    }
}