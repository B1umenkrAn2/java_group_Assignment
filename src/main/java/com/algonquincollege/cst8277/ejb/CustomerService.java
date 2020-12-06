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
        TypedQuery<CustomerPojo> selectCFromCustomerPojo = em.createQuery("select c from CustomerPojo c ", CustomerPojo.class);
        return selectCFromCustomerPojo.getResultList();
    }

    public CustomerPojo getCustomerById(int custPK) {
        return em.find(CustomerPojo.class, custPK);
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

    @Transactional
    public AddressPojo updateAddress(AddressPojo ap) {
        return em.merge(ap);
    }

    @Transactional
    public CustomerPojo persistCustomer(CustomerPojo newCustomer) {
        em.persist(newCustomer);
        return newCustomer;

    }


    @Transactional
    public CustomerPojo updateCustomer(CustomerPojo cp) {
        return em.merge(cp);
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

    @Transactional
    public CustomerPojo removeCustomerById(int custId) {

        CustomerPojo pojo = em.find(CustomerPojo.class, custId);
        em.remove(pojo);
        return pojo;
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
            return null;
        }
    }

    public ProductPojo getProductById(int prodId) {

        return em.find(ProductPojo.class, prodId);

    }

    @Transactional
    public ProductPojo updateProduct(ProductPojo pp) {

        return em.merge(pp);
    }


    @Transactional
    public ProductPojo removeProductById(int id) {
        ProductPojo pojo = em.find(ProductPojo.class, id);
        em.remove(pojo);
        return pojo;
    }


    public List<StorePojo> getAllStores() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StorePojo> q = cb.createQuery(StorePojo.class);
        Root<StorePojo> c = q.from(StorePojo.class);
        q.select(c);
        TypedQuery<StorePojo> q2 = em.createQuery(q);
        return q2.getResultList();


    }


    public StorePojo getStoreById(int id) {

        return em.find(StorePojo.class, id);

    }

    @Transactional
    public StorePojo updateStore(StorePojo storePojo) {
        return em.merge(storePojo);
    }

    @Transactional
    public StorePojo removeStoreById(int id) {
        StorePojo pojo = em.find(StorePojo.class, id);
        em.remove(pojo);
        return pojo;
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
    public CustomerPojo addOrderForCustomer(int custid, OrderPojo orderPojo) {
        CustomerPojo customerPojo = em.find(CustomerPojo.class, custid);
        customerPojo.getOrders().add(orderPojo);
        em.merge(customerPojo);
        return customerPojo;
    }

    @Transactional
    public OrderPojo removeOrderById(int id) {
        OrderPojo pojo = em.find(OrderPojo.class, id);
        em.remove(pojo);
        return pojo;

    }

    @Transactional
    public OrderPojo addOrderLineToOrder(int id,OrderLinePojo orderLinePojo){
        OrderPojo orderPojo = em.find(OrderPojo.class, id);
        orderPojo.getOrderlines().add(orderLinePojo);
        em.merge(orderPojo);
        return orderPojo;
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
        return em.merge(orderLinePojo);


    }


    @Transactional
    public OrderLinePojo removeOrderLineByNo(int No) {
        OrderLinePojo orderLinePojo = em.find(OrderLinePojo.class, No);
        em.remove(orderLinePojo);
        return orderLinePojo;

    }


}