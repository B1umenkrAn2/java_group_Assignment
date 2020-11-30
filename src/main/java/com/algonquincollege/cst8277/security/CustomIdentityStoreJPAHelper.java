/*****************************************************************c******************o*******v******id********
 * File: CustomIdentityStoreJPAHelper.java
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
package com.algonquincollege.cst8277.security;

import static com.algonquincollege.cst8277.models.SecurityUser.SECURITY_USER_BY_NAME_QUERY;
import static com.algonquincollege.cst8277.utils.MyConstants.PARAM1;
import static com.algonquincollege.cst8277.utils.MyConstants.PU_NAME;
import static java.util.Collections.emptySet;

import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;

/*
 * Stateless Session bean should also be a Singleton
 */
@Singleton
public class CustomIdentityStoreJPAHelper {
    
    public static final String CUSTOMER_PU = PU_NAME;

    @PersistenceContext(name = CUSTOMER_PU)
    protected EntityManager em;

    public SecurityUser findUserByName(String username) {
        SecurityUser user = null;
        try {
            TypedQuery<SecurityUser> query = em.createNamedQuery(SECURITY_USER_BY_NAME_QUERY, SecurityUser.class);
            query.setParameter(PARAM1, username);
            user = query.getSingleResult();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public Set<String> findRoleNamesForUser(String username) {
        Set<String> roleNames = emptySet();
        SecurityUser securityUser = findUserByName(username);
        if (securityUser != null) {
            roleNames = securityUser.getRoles().stream().map(s -> s.getRoleName()).collect(Collectors.toSet());
        }
        return roleNames;
    }

    @Transactional
    public void saveSecurityUser(SecurityUser user) {
        em.persist(user);
    }

    @Transactional
    public void saveSecurityRole(SecurityRole role) {
        em.persist(role);
    }
}