/*****************************************************************c******************o*******v******id********
 * File: CustomerService.java
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
package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.models.SecurityUser.USER_FOR_OWNING_CUST_QUERY;
import static com.algonquincollege.cst8277.models.SecurityRole.ROLE_BY_NAME_QUERY;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_KEY_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_SALT_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX;
import static com.algonquincollege.cst8277.utils.MyConstants.PARAM1;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_KEYSIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_SALTSIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;

import static com.algonquincollege.cst8277.models.CustomerPojo.ALL_CUSTOMERS_QUERY_NAME;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
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
import javax.ws.rs.NotFoundException;

import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.OrderLinePojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;
import com.algonquincollege.cst8277.models.ShippingAddressPojo;
import com.algonquincollege.cst8277.models.StorePojo;

/**
 * Stateless Singleton Session Bean - CustomerService
 * Singleton session beans offer similar functionality to stateless session beans
 * but differ from them in that there is only one singleton session bean per application,
 * as opposed to a pool of stateless session beans, any of which may respond to a client request.
 * Like stateless session beans, singleton session beans can implement web service endpoints.
 */
@Stateless
public class CustomerService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String CUSTOMER_PU = "20f-groupProject-PU";

    @PersistenceContext(name = CUSTOMER_PU)
    protected EntityManager em;

    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;
    
    //TODO
    /*
     * Customer***
     */
    public List<CustomerPojo> getAllCustomers() {
        return em.createNamedQuery(ALL_CUSTOMERS_QUERY_NAME, CustomerPojo.class).getResultList();
    }

    public CustomerPojo getCustomerById(int custPK) {
        return em.find(CustomerPojo.class, custPK);
    }
    
    @Transactional
    public AddressPojo updateAddress(AddressPojo ap) {
        return em.merge(ap);
    }

    //Add new customer***
    @Transactional
    public CustomerPojo persistCustomer(CustomerPojo newCustomer) {
        em.persist(newCustomer);
        return newCustomer;
    }
    
    @Transactional
    public CustomerPojo updateCustomer(CustomerPojo customerToUpdate) {
        em.merge(customerToUpdate);
        return customerToUpdate;
    }
    
    @Transactional
    public AddressPojo removeAddressByid(int id) {
        AddressPojo addressPojo = em.find(AddressPojo.class, id);
        em.remove(addressPojo);
        return addressPojo;
    }


    @Transactional
    public void removeCustomer(int custId) {
        CustomerPojo customer = getCustomerById(custId);
        if (customer == null) {
            throw new NotFoundException("customer does not found");
        }
        else {
            em.refresh(customer);
            em.remove(customer);
        }
    }
  
    /*
     * Security User***
     */
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
    //remove user when Customer is deleted from database
    @Transactional
    public void removeUserFromCustomer(int custId) {
        SecurityUser RemoveUser = em.createNamedQuery(USER_FOR_OWNING_CUST_QUERY,
            SecurityUser.class).setParameter(PARAM1, custId).getSingleResult();
        em.remove(RemoveUser);
    }
    
    
    /*
     * Address***
     */
    @Transactional
    public CustomerPojo setAddressFor(int custId, AddressPojo newAddress) {
        CustomerPojo updatedCustomer = em.find(CustomerPojo.class, custId);
        if (newAddress instanceof ShippingAddressPojo) {
            updatedCustomer.setShippingAddress(newAddress);
        }
        else {
            updatedCustomer.setBillingAddress(newAddress);
        }
        em.merge(updatedCustomer);
        return updatedCustomer;
    }
    /*
     * Add orders to Customer------------------
     */
    @Transactional
    public CustomerPojo setOrdersForCustomer(int custId, List<OrderPojo> newOrders) {
        CustomerPojo updatedCustomer = em.find(CustomerPojo.class, custId);
        if (newOrders == null) {
            throw new NotFoundException("orders do not found");
        }
        else {
            updatedCustomer.setOrders(newOrders);
        }
        em.merge(updatedCustomer);
        return updatedCustomer;
    }
    /*
     * Product***
     */
   public List<ProductPojo> getAllProducts() {
        //example of using JPA Criteria query instead of JPQL
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
            Root<ProductPojo> c = q.from(ProductPojo.class);
            q.select(c);
            TypedQuery<ProductPojo> q2 = em.createQuery(q);
            List<ProductPojo> allProducts = q2.getResultList();
            return allProducts;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public ProductPojo getProductById(int prodId) {
        return em.find(ProductPojo.class, prodId);
    }

    /*
     * Stores***
     */
    public List<StorePojo> getAllStores() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StorePojo> q = cb.createQuery(StorePojo.class);
            Root<StorePojo> c = q.from(StorePojo.class);
            q.select(c);
            TypedQuery<StorePojo> q2 = em.createQuery(q);
            List<StorePojo> allStores = q2.getResultList();
            return allStores;
        }
        catch (Exception e) {
            return null;
        }
 }

    public StorePojo getStoreById(int id) {
        return em.find(StorePojo.class, id);
    }
    
    public List<OrderPojo> getAllOrders() {
        //example of using JPA Criteria query instead of JPQL
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
            Root<OrderPojo> c = q.from(OrderPojo.class);
            q.select(c);
            TypedQuery<OrderPojo> q2 = em.createQuery(q);
            List<OrderPojo> allOrders = q2.getResultList();
            return allOrders;
        }
        catch (Exception e) {
            return null;
        }
    }

    public OrderPojo getOrderbyId(int orderId) {
        return em.find(OrderPojo.class, orderId);
    }
    
   
  //Add new order
    @Transactional
    public OrderPojo persistOrder(OrderPojo newOrder) {
        em.persist(newOrder);
        return newOrder;
    }
    /*
    @Transactional
    public OrderPojo updateOrder(OrderPojo orderToUpdate) {
        em.merge(orderToUpdate);
        return orderToUpdate;
    }
    */
    @Transactional
    public void removeOrder(int orderId) {
        OrderPojo orderToRemove = getOrderbyId(orderId);
        if (orderToRemove == null) {
            throw new NotFoundException("order does not found");
        }
        else {
            em.refresh(orderToRemove);
            em.remove(orderToRemove);
        }
    }
  //update order (addOrderLines)
    @Transactional
    public OrderPojo addOrderLinesToOrder(int orderId, List<OrderLinePojo> orderLines) {
        OrderPojo updatedOrder = getOrderbyId(orderId);
        if (orderLines == null) {
            throw new NotFoundException("orderLines do not found");
        }
        else {
            updatedOrder.setOrderlines(orderLines);
        }
        em.merge(updatedOrder);
        return updatedOrder;
    }
    /*
     * OrderLine
     */
    /*
>>>>>>> d1364d5... Doris's Dec2 work
    @Transactional
    public OrderLinePojo persistOrderL(OrderLinePojo newOrderLine) {
        em.persist(newOrderLine);
        return newOrderLine;
    }
    public List<OrderLinePojo> getAllOrderLines() {
        //example of using JPA Criteria query instead of JPQL
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderLinePojo> q = cb.createQuery(OrderLinePojo.class);
            Root<OrderLinePojo> c = q.from(OrderLinePojo.class);
            q.select(c);
            TypedQuery<OrderLinePojo> q2 = em.createQuery(q);
            List<OrderLinePojo> allOrderLines = q2.getResultList();
            return allOrderLines;
        }
        catch (Exception e) {
            return null;
        }
    }

    public OrderLinePojo getOrderLinebyId(int orderId, int orderLineId) {
        OrderPojo aOrder = getOrderbyId(orderId);
        aOrder.get
        return em.find(OrderLinePojo.class, orderLineId);
    }
    */
}