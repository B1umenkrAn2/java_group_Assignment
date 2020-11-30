/*****************************************************************c******************o*******v******id********
 * File: OrderSystemTestSuite.java
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 *
 * update by :
 * Lai Shan Law (040595733)
 * Siyang Xiong (040938012)
 * Angela Zhao (040529234)
 * 
 * @date 2020-11-21
 */
package com.algonquincollege.cst8277;

import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ADDRESS_RESOURCE_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ORDER_RESOURCE_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.ORDER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.STORE_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.APPLICATION_API_VERSION;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX;
import static com.algonquincollege.cst8277.utils.MyConstants.ACCESS_REQUIRES_AUTHENTICATION;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

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

import com.algonquincollege.cst8277.rest.HttpErrorResponse;


@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class OrderSystemTestSuite {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);

    static final String APPLICATION_CONTEXT_ROOT = "rest-orderSystem";
    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final String SECURITY_USER_FOR_CUSTOMER2 = "user0";
    //TODO - if you changed your Payara's default port (to say for example 9090)
    //       your may need to alter this constant
    static final int PORT = 9090;

    // test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userAuth;
    
    static final String CUST1_FIRSTNAME = "Mike";
    static final String CUST1_LASTNAME = "Norman";
    static final String CUST1_EMAIL = "normanm@algonquincollege.com";
    static final String CUST1_PHONE = "613-111-5555";
    static CustomerPojo newCustomer = new CustomerPojo("Jim", "Carrey");
    
    
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
        //userAuth = HttpAuthenticationFeature.basic(SECURITY_USER_FOR_CUSTOMER2, DEFAULT_USER_PASSWORD);
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
            //.register(userAuth)
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> custs = response.readEntity(new GenericType<List<CustomerPojo>>(){});
        assertThat(custs, is(not(empty())));
        assertThat(custs, hasSize(4));
    }
    
    // TODO - create39 more test-cases that send GET/PUT/POST/DELETE messages
    // to REST'ful endpoints for the OrderSystem entities using the JAX-RS
    // ClientBuilder APIs

    @Test
    public void test02_find_all_customers_with_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .get();
        //assertThat(response.getStatus(), is(401));
        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        
    }
    @Test
    public void test03_find_all_customers_with_norole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .get();
        //assertThat(response.getStatus(), is(401));
        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        
    }
    @Test
    public void test03_find_customer1_by_id_norole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request()
            .get();
        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
    }
    
    @Test
    public void test04_find_customer1_by_id_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request()
            .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        CustomerPojo c1 = response.readEntity(new GenericType<CustomerPojo>(){});
        assertThat(c1.getFirstName(), is(equalToIgnoringCase(CUST1_FIRSTNAME)));
        assertThat(c1.getLastName(), is(equalToIgnoringCase(CUST1_LASTNAME)));
        assertThat(c1.getEmail(), is(equalToIgnoringCase(CUST1_EMAIL)));
        assertThat(c1.getPhoneNumber(), is(equalToIgnoringCase(CUST1_PHONE)));
    }
    
    @Test
    public void test05_find_customer1_by_id_user1() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request()
            .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        CustomerPojo c1 = response.readEntity(new GenericType<CustomerPojo>(){});
        assertThat(c1.getFirstName(), is(equalToIgnoringCase(CUST1_FIRSTNAME)));
        assertThat(c1.getLastName(), is(equalToIgnoringCase(CUST1_LASTNAME)));
        assertThat(c1.getEmail(), is(equalToIgnoringCase(CUST1_EMAIL)));
        assertThat(c1.getPhoneNumber(), is(equalToIgnoringCase(CUST1_PHONE)));
    }
    @Test
    public void test06_find_customer2_by_id_user1() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
            .request()
            .get();
        assertThat(response.getStatus(), is(401));
    }
    @Test
    public void test07_find_customer2_by_id_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
            .request()
            .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        CustomerPojo c2 = response.readEntity(new GenericType<CustomerPojo>(){});
        assertThat(c2.getFirstName(), is(equalToIgnoringCase("Brad")));
        assertThat(c2.getLastName(), is(equalToIgnoringCase("Pitt")));
        assertThat(c2.getEmail(), is(equalToIgnoringCase("bpitt@gmail.com")));
        assertThat(c2.getPhoneNumber(), is(equalToIgnoringCase("613-222-6666")));
    }
    
    @Test
    public void test08_create_customer_with_norole() {
        Response response = webTarget
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .post(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(),is(UNAUTHORIZED.getStatusCode()));
    }
    
    @Test
    public void test09_create_customer_with_userrole() {
        Response response = webTarget
            .register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .post(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(),is(401));
    }
    @Test
    public void test10_create_customer_with_adminrole() {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .post(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(),is(OK.getStatusCode()));
        CustomerPojo newCustomerAndTimestamps = response.readEntity(new GenericType<CustomerPojo>(){});
        LocalDateTime localTime = LocalDateTime.now();
        LocalDate today = localTime.toLocalDate();
        assertThat(newCustomerAndTimestamps.getCreatedDate().toLocalDate(), is(today));
        newCustomer = newCustomerAndTimestamps;
    }
    @Test
    public void test11_update_customer1_with_norole() {
        
    }
    @Test
    public void test12_update_customer_with_user1() {
        Response response = webTarget
            .register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, newCustomer.getId())
            .request()
            .put(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(),is(OK.getStatusCode()));
        //// add something
    }
    
    @Test
    public void test12_update_customer_with_same_customer_user() {
        Response response = webTarget
            // a new SecurityUser will be created after a new customer is created,  USER_ROLE is 'user'
            .register(HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX + "" + newCustomer.getId(), DEFAULT_USER_PASSWORD))
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, newCustomer.getId())
            .request()
            .put(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(),is(OK.getStatusCode()));
     
    }
    
    
    @Test
    public void test13_update_customer1_with_adminrole() {
        
    }
    @Test
    public void test14_remove_customer1_by_id_norole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request()
            .delete();
       // HttpErrorResponse errorResponse = response.readEntity(new GenericType<HttpErrorResponse>(){});
       // assertThat(errorResponse.getReasonPhrase(), is(ACCESS_REQUIRES_AUTHENTICATION));
        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
    }
    @Test
    public void test15_remove_customer1_by_id_userrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request()
            .delete();
        assertThat(response.getStatus(), is(401));
    }
    
}