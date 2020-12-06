/*****************************************************************c******************o*******v******id********
 * File: CustomerService.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 *
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 *
 */
package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.models.SecurityRole.ROLE_BY_NAME_QUERY;
import static com.algonquincollege.cst8277.utils.MyConstants.*;

import java.io.Serializable;
import java.util.*;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
//import javax.transaction.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
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
    private static final String FIND_CUSTOMER_BY_NAME = 
            "select c from Customer c where c.firstName = :param1 and c.lastName = :param2";
    
    private static final String FIND_CUSTOMER_SHIPPING_ADDRESS = 
            "select sa from Customer cust join ShippingAddress sa on cust.shippingAddress = sa.id where cust.id = :param1";
    
    private static final String FIND_CUSTOMER_BILLING_ADDRESS =
            "select sa from Customer cust join BillingAddress ba on cust.billingAddress = ba.id where cust.id = :param1";

    @PersistenceContext(name = PU_NAME)
    protected EntityManager em;

    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;


    /**
     * Get all customers
     * 
     * @return whole list of customer
     */
    public List<CustomerPojo> getAllCustomers() {
        TypedQuery<CustomerPojo> query = em.createNamedQuery(CustomerPojo.ALL_CUSTOMERS_QUERY_NAME,
                CustomerPojo.class);
        
        return query.getResultList();
    }

    /**
     * Retreieve a customer object from database by its id
     * 
     * @param custPK the customer id which serves as the primary key
     * @return the found customer
     */
    public CustomerPojo getCustomerById(int custPK) {
        return em.find(CustomerPojo.class, custPK);
    }

    /**
     * Retreieve a customer object from database by its first and last names
     * 
     * @param firstName 
     * @param lastName
     * @return the found customer
     */
    public CustomerPojo getCustomerByNames(String firstName, String lastName) {     
        TypedQuery<CustomerPojo> query = em.createQuery(FIND_CUSTOMER_BY_NAME, CustomerPojo.class);
        query.setParameter("param1", firstName).setParameter("param2", lastName);
        
        return query.getSingleResult();
    } 
    
    /**
     * Retreieve a customer object from database by its email
     * 
     * @param email
     * @return the found customer
     */
    public CustomerPojo getCustomerByEmail(String email) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<CustomerPojo> criteria = criteriaBuilder.createQuery(CustomerPojo.class);
        Root<CustomerPojo> root = criteria.from(CustomerPojo.class);
        Predicate predicate = criteriaBuilder.like(root.get(CustomerPojo_.email),
                criteriaBuilder.parameter(String.class, "param1"));
        criteria.where(predicate);
        
        TypedQuery<CustomerPojo> query = em.createQuery(criteria).setParameter("param1", email);
        return query.getSingleResult();
    }
    
    /**
     * Retreieve a customer object from database by its phone number
     * 
     * @param phoneNumber
     * @return the found customer
     */
    public CustomerPojo getCustomerByPhone(String phoneNumber) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<CustomerPojo> criteria = criteriaBuilder.createQuery(CustomerPojo.class);
        Root<CustomerPojo> root = criteria.from(CustomerPojo.class);
        Predicate predicate = criteriaBuilder.like(root.get(CustomerPojo_.phoneNumber),
                criteriaBuilder.parameter(String.class, "param1"));
        criteria.where(predicate);
        
        TypedQuery<CustomerPojo> query = em.createQuery(criteria).setParameter("param1", phoneNumber);
        return query.getSingleResult();
    }
    
    /**
     * Get a customer's shipping address
     * 
     * @param custId customer id
     * @return customer shipping address
     */
    public AddressPojo getCustomerShippingAddress(int custId) {
        TypedQuery<ShippingAddressPojo> query = em.createQuery(FIND_CUSTOMER_SHIPPING_ADDRESS, ShippingAddressPojo.class);
        query.setParameter("param1", custId);
        return query.getSingleResult();
    }
    
    /**
     * Get a customer's billing address
     * 
     * @param custId
     * @return customer billing address
     */
    public AddressPojo getCustomerBillingAddress(int custId) {
        TypedQuery<BillingAddressPojo> query = em.createQuery(FIND_CUSTOMER_BILLING_ADDRESS, BillingAddressPojo.class);
        query.setParameter("param1", custId);
        return query.getSingleResult();
    }
    
    
    
    
    public List<AddressPojo> getCustomerAlladdressByCustId(int id) {
        TypedQuery<AddressPojo> query = em.createQuery("select ap from AddressPojo ap join CustomerPojo c on c.billingAddress.id = ap.id or c.shippingAddress.id = ap.id where c.id =:id", AddressPojo.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    public AddressPojo getCustomerAddressByAddressId(int id, int id2) {
        TypedQuery<AddressPojo> query = em.createQuery("select ap from AddressPojo ap join CustomerPojo c on c.billingAddress.id = ap.id or c.shippingAddress.id = ap.id where c.id =:id and ap.id =:id2", AddressPojo.class);
        query.setParameter("id", id);
        query.setParameter("id2", id2);
        return query.getSingleResult();
    }

    @Transactional
    public AddressPojo removeAddressByid(int id) {
        AddressPojo addressPojo = em.find(AddressPojo.class, id);
        em.remove(addressPojo);
        return addressPojo;
    }

    /**
     * Upate an existed address
     * 
     * @param ap new address 
     * @return updated address
     */
    @Transactional
    public AddressPojo updateAddress(AddressPojo newAddress) {
        return em.merge(newAddress);
    }

    /**
     * Add a new customer to database
     * 
     * @param newCustomer
     * @return new added customer
     */ 
    @Transactional
    public CustomerPojo persistCustomer(CustomerPojo newCustomer) {
        em.persist(newCustomer);
        return newCustomer;
    }

    /**
     * Update an existed customer
     * 
     * @param customerToUpdate customer object from EJB
     * @return updated customer object from database
     */
    @Transactional
    public CustomerPojo updateCustomer(CustomerPojo customerToUpdate) {
        return em.merge(customerToUpdate);
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

    /**
     * Set a new address for an existed customer
     * 
     * @param custId customer id
     * @param newAddress the new address
     * @return customer with new address
     */
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

    /**
     * Remove an existed customer
     * 
     * @param custId customer id
     * @return removed customer
     */
    @Transactional
    public CustomerPojo removeCustomerById(int custId) {
        CustomerPojo pojo = em.find(CustomerPojo.class, custId);
        em.remove(pojo);
        return pojo;
    }
    
    /**
     * Set orders for the specified customer
     * 
     * @param custId customer id
     * @param newOrders new orders
     * @return customer with updated orders
     */
    @Transactional
    public CustomerPojo setOrdersForCustomer(int custId, List<OrderPojo> newOrders) {
        CustomerPojo updatedCustomer = em.find(CustomerPojo.class, custId);
        if (newOrders == null) {
            return null;
        }
        else {
            updatedCustomer.setOrders(newOrders);
        }
        em.merge(updatedCustomer);
        return updatedCustomer;
    } 


    /**
     * Get all products
     * 
     * @return all products
     */
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
            return null;
        }
    }

    @Transactional
    public ProductPojo addNewProduct(ProductPojo newProduct) {
        if (em.find(ProductPojo.class, newProduct.getId()) != null) {
            return null;
        }
        em.persist(newProduct);
        
        return newProduct;
    }
        
    /**
     * Retrieve product by its id
     * 
     * @param prodId product id
     * @return found product
     */
    public ProductPojo getProductById(int prodId) {
        return em.find(ProductPojo.class, prodId);
    }
     
    /**
     * Retrieve product(s) by description
     * 
     * @param prodDescription product description
     * @return products matched to the description
     */
    public List<ProductPojo> getProductByDescription(String prodDescription) {
        
        CriteriaBuilder criteriaBuilder  = em.getCriteriaBuilder();
        CriteriaQuery<ProductPojo> criteria  = criteriaBuilder.createQuery(ProductPojo.class);
           
        Root<ProductPojo> root = criteria.from(ProductPojo.class);
        Predicate predicate = criteriaBuilder.like(root.get(ProductPojo_.description), criteriaBuilder.parameter(String.class, "param1"));
        criteria.where(predicate);
        
        TypedQuery<ProductPojo> query = em.createQuery(criteria).setParameter("param1", "%" + prodDescription + "%");  
        List<ProductPojo> foundProducts = query.getResultList();
        
        return foundProducts;
    }

    
    /**
     * Update an existed product
     * 
     * @param product the product to update
     * @return updated product
     */
    @Transactional
    public ProductPojo updateProduct(ProductPojo product) {
        return  em.merge(product);
    }

    /**
     * Remmove a product
     * 
     * @param id the product id for the product to be removed
     * @return the removed product
     */
    @Transactional
    public ProductPojo removeProductById(int id) {
        ProductPojo productToRemove = em.find(ProductPojo.class, id);
        if (productToRemove == null) {
            return null;
        }
            
        em.remove(productToRemove);
        return productToRemove;
    }

    
    public OrderPojo getOrderbyId(int orderId) {
        return em.find(OrderPojo.class, orderId);
    }
    
    /**
     * Get all stores
     * 
     * @return
     */
    public List<StorePojo> getAllStores() {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<StorePojo> criteria = criteriaBuilder.createQuery(StorePojo.class);
        Root<StorePojo> root = criteria.from(StorePojo.class);
        criteria.select(root);
        TypedQuery<StorePojo> query = em.createQuery(criteria);
        return query.getResultList();
    }

    /**
     * Add a new store
     * 
     * @param newStore new store to add
     * @return added new store
     */
    @Transactional
    public StorePojo addNewStore(StorePojo newStore) {
        if (em.find(StorePojo.class, newStore.getId()) != null) {
            return null;
        }
        em.persist(newStore);
        
        return newStore;
    }
    
    /**
     * Get store by id
     * 
     * @param id store id
     * @return found store
     */
    public StorePojo getStoreById(int id) {
        return em.find(StorePojo.class, id);
    }

    /**
     * Update a store
     * 
     * @param storePojo store to be updated
     * @return updated store
     */
    @Transactional
    public StorePojo updateStore(StorePojo storePojo) {
        StorePojo existedStore = em.find(StorePojo.class, storePojo.getId());
        if (existedStore == null) {
            return null;
        }
        
        return  em.merge(storePojo);
    }

    /**
     * Remove a store by id
     * 
     * @param id store id
     * @return removed store
     */
    @Transactional
    public StorePojo removeStoreById(int id) {
        StorePojo storeToRemove = em.find(StorePojo.class, id);
        if (storeToRemove == null) {
            return null;
        }
        em.remove(storeToRemove);
        return storeToRemove;
    }

    public List<OrderPojo> getAllOrders() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
        Root<OrderPojo> o = q.from(OrderPojo.class);
        q.select(o);
        TypedQuery<OrderPojo> q2 = em.createQuery(q);
        return q2.getResultList();

    }

    public List<OrderPojo> getCustomerALLOrders(int custID) {
        CustomerPojo customerPojo = em.find(CustomerPojo.class, custID);
        TypedQuery<OrderPojo> query = em.createQuery("select o from OrderPojo o   where o.owningCustomer.id=:id", OrderPojo.class);
        query.setParameter("id", custID);
        return query.getResultList();

    }

    public OrderPojo getOrderById(int id) {
        return em.find(OrderPojo.class, id);

    }

    @Transactional
    public OrderPojo updateOrder(OrderPojo orderPojo) {
        return em.merge(orderPojo);
    }


    @Transactional
    public OrderPojo removeOrderById(int id) {
        OrderPojo pojo = em.find(OrderPojo.class, id);
        em.remove(pojo);
        return pojo;

    }

    //update order (addOrderLines)
    @Transactional
    public OrderPojo addOrderLinesToOrder(int orderId, Set<OrderLinePojo> orderLines) {
        OrderPojo updatedOrder = getOrderbyId(orderId);
        if (orderLines == null) {
            return null;
        }
        else {
            updatedOrder.setOrderlines(orderLines);
        }
        em.merge(updatedOrder);
        return updatedOrder;
    }
    
    public List<OrderLinePojo> getOneOrderALLOrderLineById(int oId) {
        TypedQuery<OrderLinePojo> query = em.createQuery("select ol from OrderLinePojo ol where ol.pk.owningOrderId=:id", OrderLinePojo.class);
        query.setParameter("id", oId);
        return query.getResultList();

    }

    public OrderLinePojo getOrderLineByOrderLineNo(int no) {
        TypedQuery<OrderLinePojo> query = em.createQuery("select ol from OrderLinePojo ol where ol.pk.orderLineNo=:no", OrderLinePojo.class);
        query.setParameter("no", no);
        return query.getSingleResult();

    }

    @Transactional
    public OrderLinePojo updateOrderLine(OrderLinePojo orderLinePojo) {
        return   em.merge(orderLinePojo);
    }


    @Transactional
    public OrderLinePojo removeOrderLineByNo(int No) {
        OrderLinePojo orderLinePojo = em.find(OrderLinePojo.class, No);
        em.remove(orderLinePojo);
        return orderLinePojo;

    }


}