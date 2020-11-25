/*****************************************************************c******************o*******v******id********
 * File: CustomerService.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 *
 * update by : I. Am. A. Student 040nnnnnnn
 *
 */
package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.models.SecurityRole.ROLE_BY_NAME_QUERY;
import static com.algonquincollege.cst8277.utils.MyConstants.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
//import javax.transaction.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;

import com.algonquincollege.cst8277.models.*;

/**
 * Stateless Singleton Session Bean - CustomerService
 */
@Singleton
public class CustomerService implements Serializable {
    private static final long serialVersionUID = 1L;

//    public static final String CUSTOMER_PU = "acmeCustomers-PU";

    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;

    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;


    public List<CustomerPojo> getAllCustomers() {
        try {
            em.getTransaction().begin();
            TypedQuery<CustomerPojo> selectCFromCustomerPojo = em.createQuery("select c from CustomerPojo c ", CustomerPojo.class);
            List<CustomerPojo> resultList = selectCFromCustomerPojo.getResultList();
            em.getTransaction().commit();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public CustomerPojo getCustomerById(int custPK) {
        try {
            return em.find(CustomerPojo.class, custPK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Transactional
    public CustomerPojo persistCustomer(CustomerPojo newCustomer) {
        try {
            em.getTransaction().begin();
            em.persist(newCustomer);
            em.getTransaction().commit();
            return newCustomer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public CustomerPojo updateCustomerFirstName(int id, String newFName) {
        try {
            CustomerPojo customerPojo = em.find(CustomerPojo.class, id);
            customerPojo.setFirstName(newFName);
            em.merge(customerPojo);
            return customerPojo;
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    @Transactional
    public CustomerPojo updateCustomerLastName(int id, String newLName) {
        try {
            CustomerPojo customerPojo = em.find(CustomerPojo.class, id);
            customerPojo.setLastName(newLName);
            em.merge(customerPojo);
            return customerPojo;
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    @Transactional
    public CustomerPojo updateCustomerEmail(int id, String newEmail) {
        try {
            CustomerPojo customerPojo = em.find(CustomerPojo.class, id);
            customerPojo.setEmail(newEmail);
            em.merge(customerPojo);
            return customerPojo;
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    @Transactional
    public CustomerPojo updateCustomerPhone(int id, String newPhone) {
        try {
            CustomerPojo customerPojo = em.find(CustomerPojo.class, id);
            customerPojo.setPhoneNumber(newPhone);
            em.merge(customerPojo);
            return customerPojo;
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }


    @Transactional
    public void buildUserForNewCustomer(CustomerPojo newCustomerWithIdTimestamps) {
        SecurityUser userForNewCustomer = new SecurityUser();
        userForNewCustomer.setUsername(DEFAULT_USER_PREFIX + "" + newCustomerWithIdTimestamps.getId());
        Map<String, String> pbAndjProperties = new HashMap<>();
        pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
        pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
        pbAndjProperties.put(PROPERTY_SALTSIZE, DEFAULT_SALT_SIZE);
        pbAndjProperties.put(PROPERTY_KEYSIZE, DEFAULT_KEY_SIZE);
        pbAndjPasswordHash.initialize(pbAndjProperties);
        String pwHash = pbAndjPasswordHash.generate(DEFAULT_USER_PASSWORD.toCharArray());
        userForNewCustomer.setPwHash(pwHash);
        userForNewCustomer.setCustomer(newCustomerWithIdTimestamps);
        SecurityRole userRole = em.createNamedQuery(ROLE_BY_NAME_QUERY,
                SecurityRole.class).setParameter(PARAM1, USER_ROLE).getSingleResult();
        userForNewCustomer.getRoles().add(userRole);
        userRole.getUsers().add(userForNewCustomer);
        em.persist(userForNewCustomer);
    }

    @Transactional
    public CustomerPojo setAddressFor(int custId, AddressPojo newAddress) {
        CustomerPojo updatedCustomer = em.find(CustomerPojo.class, custId);
        if (newAddress instanceof ShippingAddressPojo) {
            updatedCustomer.setShippingAddress(newAddress);
        } else {
            updatedCustomer.setBillingAddress(newAddress);
        }
        em.merge(updatedCustomer);
        return updatedCustomer;
    }

    public List<ProductPojo> getAllProducts() {
        //example of using JPA Criteria query instead of JPQL
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
            Root<ProductPojo> c = q.from(ProductPojo.class);
            q.select(c);
            TypedQuery<ProductPojo> q2 = em.createQuery(q);
            return q2.getResultList();
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    public ProductPojo getProductById(int prodId) {
        try {
            return em.find(ProductPojo.class, prodId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public ProductPojo updateProductDesc(int id, String newDesc) {
        try {
            ProductPojo productPojo = em.find(ProductPojo.class, id);
            productPojo.setDescription(newDesc);
            em.merge(productPojo);
            return productPojo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public ProductPojo updateProductSN(int id, String newSN) {
        try {
            ProductPojo productPojo = em.find(ProductPojo.class, id);
            productPojo.setSerialNo(newSN);
            em.merge(productPojo);
            return productPojo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<StorePojo> getAllStores() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StorePojo> q = cb.createQuery(StorePojo.class);
            Root<StorePojo> c = q.from(StorePojo.class);
            q.select(c);
            TypedQuery<StorePojo> q2 = em.createQuery(q);
            return q2.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public StorePojo getStoreById(int id) {
        try {
            return em.find(StorePojo.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public StorePojo updateStoreName(int Id, String storeName) {
        try {
            StorePojo updatedStore = em.find(StorePojo.class, Id);
            updatedStore.setStoreName(storeName);
            em.merge(updatedStore);
            return updatedStore;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public List<OrderPojo> getAllOrders() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
            Root<OrderPojo> o = q.from(OrderPojo.class);
            q.select(o);
            TypedQuery<OrderPojo> q2 = em.createQuery(q);
            return q2.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public OrderPojo getOrderById(int id) {
        try {
            return em.find(OrderPojo.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public OrderPojo updateDescriptionForOrder(int Id, String desc) {
        try {
            OrderPojo updatedOrder = em.find(OrderPojo.class, Id);
            updatedOrder.setDescription(desc);
            em.merge(updatedOrder);
            return updatedOrder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    //TODO orderLine



}