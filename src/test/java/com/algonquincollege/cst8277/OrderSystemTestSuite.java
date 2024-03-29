/*****************************************************************c******************o*******v******id********
 * File: OrderSystemTestSuite.java
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 *
 * @date 2020 10
 *
 * (Modified) @author Student Name
 */
package com.algonquincollege.cst8277;

import static com.algonquincollege.cst8277.utils.MyConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

import com.algonquincollege.cst8277.models.*;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class OrderSystemTestSuite {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);

    static final String APPLICATION_CONTEXT_ROOT = "rest-orderSystem";
    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";

    static final int PORT = 8080;


    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;
    static HttpAuthenticationFeature user0;


    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        uri = UriBuilder
                .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
                .scheme(HTTP_SCHEMA)
                .host(HOST)
                .port(PORT)
                .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX , DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;

    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
                new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

    // create
    @Test
    public void test01_add_one_product_with_adminrole() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setDescription("productdesc_1");
        productPojo.setSerialNo("001");
        productPojo.setId(1);

        Response response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME + SLASH)
                .request()
                .post(Entity.entity(productPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));

    }

    @Test
    public void test02_add_one_product_with_userrole() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setDescription("productdesc_2");
        productPojo.setSerialNo("002");
        productPojo.setId(2);

        Response response = webTarget
                .register(userAuth)
                .path(PRODUCT_RESOURCE_NAME + SLASH)
                .request()
                .post(Entity.entity(productPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test03_add_one_product_with_adminrole() {
        StorePojo storePojo = new StorePojo();
        storePojo.setStoreName("storeName1");
        storePojo.setId(1);


        Response response = webTarget
                .register(adminAuth)
                .path(STORE_RESOURCE_NAME + SLASH)
                .request()
                .post(Entity.entity(storePojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test04_add_one_product_with_userrole() {
        StorePojo storePojo = new StorePojo();
        storePojo.setStoreName("storeName2");
        storePojo.setId(2);

        Response response = webTarget
                .register(userAuth)
                .path(STORE_RESOURCE_NAME + SLASH)
                .request()
                .post(Entity.entity(storePojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test05_add_one_custmoer_with_adminrole() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setPhoneNumber("613-261-2222");
        customerPojo.setLastName("Tom");
        customerPojo.setFirstName("jam");
        customerPojo.setEmail("tom@jam.com");

        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME + SLASH)
                .request()
                .post(Entity.entity(customerPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
        CustomerPojo customer = response.readEntity(new GenericType<>() {
        });
        assertThat("613-261-2222", is(customer.getPhoneNumber()));
        assertThat("Tom", is(customer.getLastName()));
        assertThat("jam", is(customer.getFirstName()));
        assertThat("tom@jam.com", is(customer.getEmail()));
    }

    @Test
    public void test06_add_one_custmoer_with_userrole() {

        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setPhoneNumber("613-261-2223");
        customerPojo.setLastName("Tom1");
        customerPojo.setFirstName("jam1");
        customerPojo.setEmail("tom1@jam.com");

        Response response = webTarget
                .register(userAuth)
                .path(CUSTOMER_RESOURCE_NAME + SLASH)
                .request()
                .post(Entity.entity(customerPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
        CustomerPojo customer = response.readEntity(new GenericType<>() {
        });
        assertThat("613-261-2223", is(customer.getPhoneNumber()));
        assertThat("Tom1", is(customer.getLastName()));
        assertThat("jam1", is(customer.getFirstName()));
        assertThat("tom1@jam.com", is(customer.getEmail()));

    }

    @Test
    public void test07_add_one_order_with_adminrole() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDescription("order1_desc");
        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME + ONE_CUST_ALL_ORDER + SLASH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .post(Entity.entity(orderPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));

    }

    @Test
    public void test08_add_one_order_with_userrole() {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setDescription("order2_desc");
        Response response = webTarget
                .register(userAuth)
                .path(CUSTOMER_RESOURCE_NAME + ONE_CUST_ALL_ORDER + SLASH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .post(Entity.entity(orderPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
    }

    //read

    @Test
    public void test09_all_customers_with_userrole() {

        Response response = webTarget
                .register(userAuth)
                .path(CUSTOMER_RESOURCE_NAME + SLASH)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customerlist = response.readEntity(new GenericType<>() {
        });
        assertThat(customerlist, hasSize(2));

    }

    @Test
    public void test10_get_one_customers_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        CustomerPojo customer = response.readEntity(new GenericType<>() {
        });
        assertThat(1, is(customer.getId()));
    }

    @Test
    public void test11_get_one_customer_with_self_userole(){

        Response response = webTarget
                .register(userAuth)
                .path(CUSTOMER_RESOURCE_NAME + SLASH)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> customerlist = response.readEntity(new GenericType<>() {
        });
        assertThat(customerlist, hasSize(2));
    }

    @Test
    public void test12_get_one_customers_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .get();
        assertThat(response.getStatus(), is(403));
    }

    @Test
    public void test13_get_one_customers_with_no_role(){
        Response response = webTarget
                .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();
        assertThat(response.getStatus(), is(401));
    }
    @Test
    public void test14_get_one_customer_with_selfuserrole(){
            user0 = HttpAuthenticationFeature.basic("user0", DEFAULT_USER_PASSWORD);
        Response response = webTarget
                .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .register(user0)
                .request()
                .get();
        assertThat(response.getStatus(), is(401));

    }

    @Test
    public void test15_get_one_order_all_orderlines_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME + SLASH + ONE_ORDER_ALL_ORDERLINE)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test16_get_one_order_all_orderlines_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(CUSTOMER_RESOURCE_NAME + SLASH + ONE_ORDER_ALL_ORDERLINE)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test17_get_all_products_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME + SLASH)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));

    }

    @Test
    public void test18_get_all_products_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(PRODUCT_RESOURCE_NAME + SLASH)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test19_get_all_products_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(STORE_RESOURCE_NAME + SLASH)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test20_get_all_products_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(STORE_RESOURCE_NAME + SLASH)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    // update
    @Test
    public void test21_update_one_customer_with_adminrole() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setPhoneNumber("613-261-2255");
        customerPojo.setLastName("Tom3");
        customerPojo.setFirstName("jam3");
        customerPojo.setEmail("tom3@jam.com");

        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .put(Entity.entity(customerPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
        CustomerPojo customer = response.readEntity(new GenericType<>() {
        });
        assertThat("613-261-2255", is(customer.getPhoneNumber()));
        assertThat("Tom3", is(customer.getLastName()));
        assertThat("jam3", is(customer.getFirstName()));
        assertThat("tom3@jam.com", is(customer.getEmail()));
    }

    @Test
    public void test22_update_one_customer_with_userrole() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.setPhoneNumber("613-261-2255");
        customerPojo.setLastName("Tom3");
        customerPojo.setFirstName("jam3");
        customerPojo.setEmail("tom3@jam.com");

        Response response = webTarget
                .register(userAuth)
                .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .put(Entity.entity(customerPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
        CustomerPojo customer = response.readEntity(new GenericType<>() {
        });
        assertThat("613-261-2255", is(customer.getPhoneNumber()));
        assertThat("Tom3", is(customer.getLastName()));
        assertThat("jam3", is(customer.getFirstName()));
        assertThat("tom3@jam.com", is(customer.getEmail()));

    }
    @Test
    public void test23_update_one_order_with_adminrole() {
        OrderPojo updateOrder = new OrderPojo();
        updateOrder.setDescription("newOrder1_desc");
        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME +ONE_CUST_ONE_ORDER)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .put(Entity.entity(updateOrder, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
        OrderPojo orderPojo1 = response.readEntity(new GenericType<>() {
        });
        assertThat("newOrder1_desc", is(orderPojo1.getDescription()));
    }

    @Test
    public void test24_update_one_order_with_userrole() {
        OrderPojo updateOrder = new OrderPojo();
        updateOrder.setDescription("newOrder2_desc");
        Response response = webTarget
                .register(userAuth)
                .path(CUSTOMER_RESOURCE_NAME + ONE_CUST_ONE_ORDER)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .put(Entity.entity(updateOrder, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
        OrderPojo orderPojo1 = response.readEntity(new GenericType<>() {
        });
        assertThat("newOrder2_desc", is(orderPojo1.getDescription()));
    }

    @Test
    public void test27_update_one_product_with_adminrole() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setDescription("newProductdesc_1");
        productPojo.setSerialNo("003");

        Response response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .put(Entity.entity(productPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test28_update_one_product_with_userrole() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setDescription("newProductdesc_2");
        productPojo.setSerialNo("004");

        Response response = webTarget
                .register(userAuth)
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .put(Entity.entity(productPojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test29_update_one_product_with_adminrole() {
        StorePojo storePojo = new StorePojo();
        storePojo.setStoreName("newStoreName_1");


        Response response = webTarget
                .register(adminAuth)
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .put(Entity.entity(storePojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test30_update_one_product_with_userrole() {
        StorePojo storePojo = new StorePojo();
        storePojo.setStoreName("newStoreName_2");
        Response response = webTarget
                .register(userAuth)
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .put(Entity.entity(storePojo, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(200));
    }

//    delete

    @Test
    public void test31_delete_one_customer_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test32_delete_one_customer_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test33_delete_one_customer_with_no_role(){
        Response response = webTarget
                .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .delete();
        assertThat(response.getStatus(), is(401));
    }

    @Test
    public void test34_delete_one_product_with_no_role(){
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .delete();
        assertThat(response.getStatus(), is(401));
    }

    @Test
    public void test35_delete_one_store_with_no_role(){
        Response response = webTarget
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .delete();
        assertThat(response.getStatus(), is(401));
    }

    @Test
    public void test36_delete_one_order_with_no_role(){
        Response response = webTarget
                .path(CUSTOMER_RESOURCE_NAME + ONE_CUST_ONE_ORDER)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .resolveTemplate("id2", 1)
                .request()
                .delete();
        assertThat(response.getStatus(), is(401));
    }


    @Test
    public void test37_delete_one_product_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test38_delete_one_product_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test39_delete_one_store_with_adminrole() {
        Response response = webTarget
                .register(adminAuth)
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void test40_delete_one_store_with_userrole() {
        Response response = webTarget
                .register(userAuth)
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
                .request()
                .delete();
        assertThat(response.getStatus(), is(200));
    }

}