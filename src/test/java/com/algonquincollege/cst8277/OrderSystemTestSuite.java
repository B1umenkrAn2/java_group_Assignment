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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.algonquincollege.cst8277.models.OrderPojo;
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

import com.algonquincollege.cst8277.models.CustomerPojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class OrderSystemTestSuite {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);

    static final String APPLICATION_CONTEXT_ROOT = "rest-orderSystem";
    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";

    //TODO - if you changed your Payara's default port (to say for example 9090)
    //       your may need to alter this constant
    static final int PORT = 8080;

    // test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;

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
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;

    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
                new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }

    @Test
    public void test01_all_customers_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                .register(userAuth)
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> custs = response.readEntity(new GenericType<List<CustomerPojo>>() {
        });
        assertThat(custs, is(not(empty())));
        //TODO - depending on what is in your Db when you run this, you may need to change the next line
        assertThat(custs, hasSize(3));
    }

    // TODO - create39 more test-cases that send GET/PUT/POST/DELETE messages
    // to REST'ful endpoints for the OrderSystem entities using the JAX-RS
    // ClientBuilder APIs
    @Test
    public void test02_something() {

        Response response = webTarget
                .register(userAuth)
                .register(adminAuth)
                .path(ORDER_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
        List<OrderPojo> orderlist = response.readEntity(new GenericType<>() {
        });
        assertThat(orderlist, hasSize(20));

    }

    public void test03_something() {
    }

    public void test04_something() {
    }

    public void test05_something() {
    }

    public void test06_something() {
    }

    public void test07_something() {
    }

    public void test08_something() {
    }

    public void test09_something() {
    }

    public void test10_something() {
    }

    public void test11_something() {
    }

    public void test12_something() {
    }

    public void test13_something() {
    }

    public void test14_something() {
    }

    public void test15_something() {
    }

    public void test16_something() {
    }

    public void test17_something() {
    }

    public void test18_something() {
    }

    public void test19_something() {
    }

    public void test20_something() {
    }

    public void test21_something() {
    }

    public void test22_something() {
    }

    public void test23_something() {
    }

    public void test24_something() {
    }

    public void test25_something() {
    }

    public void test26_something() {
    }

    public void test27_something() {
    }

    public void test28_something() {
    }

    public void test29_something() {
    }

    public void test30_something() {
    }

    public void test31_something() {
    }

    public void test32_something() {
    }

    public void test33_something() {
    }

    public void test34_something() {
    }

    public void test35_something() {
    }

    public void test36_something() {
    }

    public void test37_something() {
    }

    public void test38_something() {
    }

    public void test39_something() {
    }

}