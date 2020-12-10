/*****************************************************************c******************o*******v******id********
 * File: OrderSystemTestSuite.java
 * Course materials (20F) CST 8277
 * (Original Author) Mike Norman
 *
 * @date 2020 10
 *
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 */
package com.algonquincollege.cst8277;

import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ADDRESS_SUBRESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ADDRESS_RESOURCE_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_ORDER;
import static com.algonquincollege.cst8277.utils.MyConstants.ORDER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.STORE_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.APPLICATION_API_VERSION;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX;
import static com.algonquincollege.cst8277.utils.MyConstants.ACCESS_REQUIRES_AUTHENTICATION;
import static com.algonquincollege.cst8277.utils.MyConstants.PRODUCT_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_DESCRIPTION_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_DESCRIPTION_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.SLASH;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.BillingAddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderLinePk;
import com.algonquincollege.cst8277.models.OrderLinePojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.ShippingAddressPojo;
import com.algonquincollege.cst8277.models.StorePojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class OrderSystemTestSuite {
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);

    static final String APPLICATION_CONTEXT_ROOT = "rest-orderSystem";
    static final String HTTP_SCHEMA = "http";
    static final String HOST = "localhost";
    static final String SECURITY_USER_FOR_NEWCUST = "user0";
    static final String SECURITY_USER_FOR_CUST_A = "user10";
    static final String SECURITY_USER_FOR_CUST_B = "user11";
    
    static final int PORT = 9090;

    // test fixture(s)
    static URI uri;
    static HttpAuthenticationFeature adminAuth;
    static HttpAuthenticationFeature userNew_Auth;
    static HttpAuthenticationFeature userA_Auth;
    static HttpAuthenticationFeature userB_Auth;
    
    static final String CUST1_FIRSTNAME = "Mike";
    static final String CUST1_LASTNAME = "Norman";
    static final String CUST1_EMAIL = "normanm@algonquincollege.com";
    static final String CUST1_PHONE = "613-111-5555";
//    static CustomerPojo newCustomer = new CustomerPojo("Jim", "Carrey");
   // static CustomerPojo newCustomer;
   // static AddressPojo custA_addr =  new AddressPojo("Ottawa", "Canada", "K2T 1B6", "On", "200 Earl Grey Dr");
//    static BillingAddressPojo custAdmin_addr = new BillingAddressPojo("K2T 1B6", "200 Earl Grey Dr");
    //static ShippingAddressPojo custa_addr = new ShippingAddressPojo("K2C 0C6", "1980 Baseline Rd");
    
    private static final String PROD1_DESCRIPTION = "Cadbury";
    private static final String PROD1_SERIALNO = "cad001";
    private static final int TOTAL_NUMBER_OF_PRODUCTS = 8;
    
    private static final int TOTAL_NUMBER_OF_STORES = 3;
    private static final String STORE1_NAME = "Loblaws";
    
    private static final String ORDER1_DESCRIPTION = "ORD_CUSTADMIN";
    
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
        userNew_Auth = HttpAuthenticationFeature.basic(SECURITY_USER_FOR_NEWCUST, DEFAULT_USER_PASSWORD);
        userA_Auth = HttpAuthenticationFeature.basic(SECURITY_USER_FOR_CUST_A, DEFAULT_USER_PASSWORD);
        userB_Auth = HttpAuthenticationFeature.basic(SECURITY_USER_FOR_CUST_B, DEFAULT_USER_PASSWORD);
    }

    protected WebTarget webTarget;
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }
//------------------------------------------------------------------------------------------------------
    @Test
    public void test01_all_customers_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        List<CustomerPojo> custs = response.readEntity(new GenericType<List<CustomerPojo>>(){});
        assertThat(custs, is(not(empty())));
//        assertThat(custs, hasSize(5));
        assertThat(custs.get(0).getFirstName(), is(equalToIgnoringCase(CUST1_FIRSTNAME)));
        assertThat(custs.get(0).getLastName(), is(equalToIgnoringCase(CUST1_LASTNAME)));
        assertThat(custs.get(0).getEmail(), is(equalToIgnoringCase(CUST1_EMAIL)));
        assertThat(custs.get(0).getPhoneNumber(), is(equalToIgnoringCase(CUST1_PHONE)));
    }
    
    @Test
    public void test02_find_all_customers_with_userA() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userA_Auth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .get();
//        assertThat(response.getStatus(), is(403));
        assertThat(response.getStatus(), is(FORBIDDEN.getStatusCode()));       
    }
    
    @Test
    public void test03_find_all_customers_with_userB() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userB_Auth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .get();
//        assertThat(response.getStatus(), is(403));
        assertThat(response.getStatus(), is(FORBIDDEN.getStatusCode()));
        
    }
    
    @Test
    public void test04_find_customerAdmin_by_id_adminrole() throws JsonMappingException, JsonProcessingException {
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
    public void test05_find_customerAdmin_by_id_userA() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userA_Auth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request()
            .get();
        assertThat(response.getStatus(),is(FORBIDDEN.getStatusCode()));
//        assertThat(response.getStatus(), is(403));
    }
    
    @Test
    public void test06_find_customerAdmin_by_id_userB() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userB_Auth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request()
            .get();
        assertThat(response.getStatus(),is(FORBIDDEN.getStatusCode()));
//        assertThat(response.getStatus(), is(403));
    }
    
    @Test
    public void test07_find_customerAdmin_by_id_norole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request()
            .get();
        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
    }
    
    @Test
    public void test08_find_customerA_by_id_adminrole() throws JsonMappingException, JsonProcessingException {
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
    public void test09_find_customerA_by_id_userA() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userA_Auth)
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
    public void test10_find_customerA_by_id_userB() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userB_Auth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)
            .request()
            .get();
        
//        assertThat(response.getStatus(), is(403));
        assertThat(response.getStatus(), is(FORBIDDEN.getStatusCode()));
    }
    
    @Test
    public void test11_find_customerB_by_id_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 3)
            .request()
            .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        CustomerPojo c2 = response.readEntity(new GenericType<CustomerPojo>(){});
        assertThat(c2.getFirstName(), is(equalToIgnoringCase("Matt")));
        assertThat(c2.getLastName(), is(equalToIgnoringCase("Jon")));
        assertThat(c2.getEmail(), is(equalToIgnoringCase("mjon@gmail.com")));
        assertThat(c2.getPhoneNumber(), is(equalToIgnoringCase("613-333-7777")));       
    }
    
    public void test12_find_customerB_by_id_userA() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userA_Auth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 3)
            .request()
            .get();
        
//        assertThat(response.getStatus(), is(403));
        assertThat(response.getStatus(), is(FORBIDDEN.getStatusCode()));
        
    }
    
    @Test
    public void test13_find_customerB_by_id_userB() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userB_Auth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 3)
            .request()
            .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        CustomerPojo c2 = response.readEntity(new GenericType<CustomerPojo>(){});
        assertThat(c2.getFirstName(), is(equalToIgnoringCase("Matt")));
        assertThat(c2.getLastName(), is(equalToIgnoringCase("Jon")));
        assertThat(c2.getEmail(), is(equalToIgnoringCase("mjon@gmail.com")));
        assertThat(c2.getPhoneNumber(), is(equalToIgnoringCase("613-333-7777")));
    }
   
    @Test
    public void test14_update_FirstName_by_adminrole() throws JsonMappingException, JsonProcessingException {
       Response response = webTarget
           .register(adminAuth)
           .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
           .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 3)
           .request()
           .get();
       assertThat(response.getStatus(), is(OK.getStatusCode()));
       CustomerPojo c3 = response.readEntity(new GenericType<CustomerPojo>(){});
       assertThat(c3.getLastName(), is(equalToIgnoringCase("Jon")));
       
       c3.setFirstName("Yan");
       
       Response response2 = webTarget
           .register(adminAuth)
           .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
           .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 3)
           .request()
           .put(Entity.entity(c3, MediaType.APPLICATION_JSON_TYPE));
       assertThat(response2.getStatus(), is(OK.getStatusCode()));
       CustomerPojo customerUpdateWithIdTimestamps = response2.readEntity(new GenericType<CustomerPojo>(){});
     //update the timestamps
       LocalDateTime localTime = LocalDateTime.now();
       LocalDate today = localTime.toLocalDate();
       assertThat(customerUpdateWithIdTimestamps.getCreatedDate().toLocalDate(), is(today));
       c3 = customerUpdateWithIdTimestamps;
       
       assertThat(c3.getFirstName(), is(equalToIgnoringCase("Yan")));
//       assertThat(c6.getLastName(), is(equalToIgnoringCase("Carrey")));
//       assertThat(c6.getBillingAddress().getPostal(), is(equalToIgnoringCase("K2C 0C6")));
//       assertThat(c6.getBillingAddress().getStreet(), is(equalToIgnoringCase("1980 Baseline Rd")));
    }
   
    @Test
    public void test15_update_CustA_FirstName_by_userA() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                // userA can get his own information
                .register(adminAuth).path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2).request().get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        CustomerPojo c2 = response.readEntity(new GenericType<CustomerPojo>() {
        });
        assertThat(c2.getLastName(), is(equalToIgnoringCase("Pitt")));

        c2.setFirstName("Brad");

        Response response2 = webTarget.register(userA_Auth).path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2).request()
                .put(Entity.entity(c2, MediaType.APPLICATION_JSON_TYPE));
        // only Admin and the own user can update his information
        assertThat(response2.getStatus(), is(OK.getStatusCode()));
        CustomerPojo customerUpdateWithIdTimestamps = response2.readEntity(new GenericType<CustomerPojo>() {
        });
        // update the timestamps
        LocalDateTime localTime = LocalDateTime.now();
        LocalDate today = localTime.toLocalDate();
        assertThat(customerUpdateWithIdTimestamps.getCreatedDate().toLocalDate(), is(today));
        c2 = customerUpdateWithIdTimestamps;

        assertThat(c2.getFirstName(), is(equalToIgnoringCase("Brad")));
        assertThat(c2.getLastName(), is(equalToIgnoringCase("Pitt")));
        assertThat(c2.getEmail(), is(equalToIgnoringCase("bpitt@gmail.com")));
        assertThat(c2.getPhoneNumber(), is(equalToIgnoringCase("613-222-6666")));
    }
   
    @Test
    public void test16_update_CustA_FirstName_by_userB() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
                // Only Admin role and UserA can retrieve UserA's information
                .register(adminAuth).path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2).request().get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        CustomerPojo c2 = response.readEntity(new GenericType<CustomerPojo>() {
        });
        assertThat(c2.getFirstName(), is(equalToIgnoringCase("Brad")));
        assertThat(c2.getLastName(), is(equalToIgnoringCase("Pitt")));
        assertThat(c2.getEmail(), is(equalToIgnoringCase("bpitt@gmail.com")));
        assertThat(c2.getPhoneNumber(), is(equalToIgnoringCase("613-222-6666")));

        c2.setFirstName("XOXO");
        // line 139
        Response response2 = webTarget.register(userB_Auth).path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2).request()
                .put(Entity.entity(c2, MediaType.APPLICATION_JSON_TYPE));
        // UserB can not update UserA's information
        assertThat(response2.getStatus(), is(FORBIDDEN.getStatusCode()));

    }
   //####################### OPEN WHEN SHOW DEMO #################################
 /*
   //-------------Add address to customer
   @Test
    public void test17_add_address_to_customer_by_adminrole() throws JsonMappingException, JsonProcessingException {
       // CustomerPojo newCustomer = new CustomerPojo("Jim", "Carrey");
      
       BillingAddressPojo billingAddr = new BillingAddressPojo();
       billingAddr.setStreet("100 Earl Grey Dr");
       billingAddr.setCity("Ottawa");
       billingAddr.setCountry("Canada");
       billingAddr.setPostal("K2T 1B6");
       billingAddr.setState("ON");
       
        Response response = webTarget
           .register(adminAuth)
           .path(CUSTOMER_RESOURCE_NAME + CUSTOMER_ADDRESS_RESOURCE_PATH)
           .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 7)
           .request()
           .put(Entity.entity(billingAddr, MediaType.APPLICATION_JSON_TYPE));
       assertThat(response.getStatus(), is(OK.getStatusCode()));
       CustomerPojo cAdmin = response.readEntity(new GenericType<CustomerPojo>(){});
       assertThat(cAdmin.getBillingAddress().getPostal(), is(equalToIgnoringCase("K2T 1B6")));
       assertThat(cAdmin.getBillingAddress().getStreet(), is(equalToIgnoringCase("100 Earl Grey Dr")));
       assertThat(cAdmin.getBillingAddress().getCity(), is(equalToIgnoringCase("Ottawa")));
       assertThat(cAdmin.getBillingAddress().getCountry(), is(equalToIgnoringCase("Canada")));
       assertThat(cAdmin.getBillingAddress().getPostal(), is(equalToIgnoringCase("K2T 1B6")));
       assertThat(cAdmin.getBillingAddress().getState(), is(equalToIgnoringCase("ON")));
   }
    */
   
   //------------------------------------------------------
   
   @Test
   public void test18_add_address_to_customer_by_UserA() throws JsonMappingException, JsonProcessingException {
      
       BillingAddressPojo billingAddr = new BillingAddressPojo();
       billingAddr.setStreet("0000 AAAA");
       billingAddr.setCity("Ottawa");
       billingAddr.setCountry("Canada");
       billingAddr.setPostal("K1K K1K");
       billingAddr.setState("ON");
       
       //only Admin role is able to add address to customer
       Response response = webTarget
//          .register(userA_Auth)
          .register(adminAuth)
          .path(CUSTOMER_RESOURCE_NAME + CUSTOMER_ADDRESS_RESOURCE_PATH)
          .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 3)
          .request()
          .put(Entity.entity(billingAddr, MediaType.APPLICATION_JSON_TYPE));
       
//       assertThat(response.getStatus(), is(403));
//       assertThat(response.getStatus(), is(FORBIDDEN.getStatusCode()));
  }
      

   // Order and OrderLine
   @Test
   public void test19_get_Customer_All_Orders_by_Admin() throws JsonMappingException, JsonProcessingException {
    
       //only Admin role and the specific user are able to retrieve info
       Response response = webTarget
          .register(adminAuth)
          .path(CUSTOMER_RESOURCE_ORDER)
          //.path(ORDER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
          .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
          .request()
          .get();
       assertThat(response.getStatus(), is(OK.getStatusCode()));
  /*
       OrderPojo customer = response.readEntity(new GenericType<OrderPojo>(){});
       List<OrderPojo> orders = customer.getOrders();
       assertThat(orders, is(not(empty())));
       assertThat(orders.get(0).getDescription(), is(equalToIgnoringCase("ORD_CUSTADMIN")));
 */
 }
   @Test
   public void test20_get_Customer_All_Orders_by_UserA() throws JsonMappingException, JsonProcessingException {
    
       //only Admin role and the specific user are able to retrieve info
       Response response = webTarget
          .register(userA_Auth)
          .path(CUSTOMER_RESOURCE_ORDER)
          //.path(ORDER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
          .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
          .request()
          .get();
       assertThat(response.getStatus(), is(OK.getStatusCode()));
       
   }
   
   @Test
   public void test21_get_Customer_All_Orders_by_norole() throws JsonMappingException, JsonProcessingException {
    
       //only Admin role and the specific user are able to retrieve info
       Response response = webTarget
          .path(CUSTOMER_RESOURCE_NAME + CUSTOMER_RESOURCE_ORDER)
          .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
          .request()
          .get();
       assertThat(response.getStatus(),is(401));
   } 

   
   /*
    @Test
    public void test15_remove_address_from_customer_by_adminrole() throws JsonMappingException, JsonProcessingException {
       // CustomerPojo newCustomer = new CustomerPojo("Jim", "Carrey");
      
        Response response = webTarget
            .register(adminAuth)
           //.path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .path(CUSTOMER_RESOURCE_NAME + CUSTOMER_ADDRESS_RESOURCE_PATH)
           .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 17)
            .request()
            .delete();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    */
   
    
    /*
    @Test
    public void test14_update_new_customer_by_adminrole() throws JsonMappingException, JsonProcessingException {
       // newCustomer = new CustomerPojo("Jim", "Carrey");
        Response response1 = webTarget
            .register(userA_Auth)
            .path(CUSTOMER_RESOURCE_NAME)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request();
            put(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        */
      /* CustomerPojo newCustomer = .setEmail("jcarrey@gmail.com");
        Response response = webTarget
            .register(adminAuth)
           //.path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .path(CUSTOMER_RESOURCE_NAME)
          .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, newCustomer.getId())
            .request()
            .put(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(OK.getStatusCode()));
       */
    
        
        /*
        BillingAddressPojo billingAddr = new BillingAddressPojo();
        billingAddr.setStreet("100 Earl Grey Dr");
        billingAddr.setCity("Ottawa");
        billingAddr.setCountry("Canada");
        billingAddr.setPostal("K2T 1B6");
        billingAddr.setState("ON");
        ShippingAddressPojo shippingAddr = new ShippingAddressPojo();
        shippingAddr.setStreet("980 Baseline Rd");
        shippingAddr.setCity("Ottawa");
        shippingAddr.setCountry("Canada");
        shippingAddr.setPostal("K2C 0C6");
        shippingAddr.setState("ON");
        //-----------------------
        newCustomer.setBillingAddress(billingAddr);
        newCustomer.setShippingAddress(shippingAddr);
        //----------------------
        Response response = webTarget
            .register(adminAuth)
           //.path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .path(CUSTOMER_RESOURCE_NAME)
          // .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, newCustomer.getId())
            .request()
            .put(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(OK.getStatusCode()));
      //  CustomerPojo cAdmin = response.readEntity(new GenericType<CustomerPojo>(){});
      //  assertThat(cAdmin.getBillingAddress().getPostal(), is(equalToIgnoringCase("K2T 1B6")));
      //  assertThat(cAdmin.getBillingAddress().getStreet(), is(equalToIgnoringCase("200 Earl Grey Dr")));
    }
    */
    /*
    @Test
    public void test15_add_address_to_customerAdmin_by_adminrole() throws JsonMappingException, JsonProcessingException {
       Response response = webTarget
            .register(adminAuth)
           
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .request()
            .put(Entity.entity(custA_addr, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(OK.getStatusCode()));
      //  CustomerPojo cAdmin = response.readEntity(new GenericType<CustomerPojo>(){});
      //  assertThat(cAdmin.getBillingAddress().getPostal(), is(equalToIgnoringCase("K2T 1B6")));
      //  assertThat(cAdmin.getBillingAddress().getStreet(), is(equalToIgnoringCase("200 Earl Grey Dr")));
    }
    
    @Test
    public void test15_add_address_to_customerAdmin_by_user0() throws JsonMappingException, JsonProcessingException {
        //Response response = webTarget
         //   .register(user0_Auth)
    }
    @Test
    public void test16_add_address_to_customerAdmin_by_user1() throws JsonMappingException, JsonProcessingException {
      //Response response = webTarget
        //   .register(user1_Auth)
    }
    @Test
    public void test17_add_address_to_customerAdmin_by_norole() throws JsonMappingException, JsonProcessingException {
      //Response response = webTarget
          
    }
    @Test
    public void test18_add_address_to_customer0_by_adminrole() throws JsonMappingException, JsonProcessingException {
       // Response response = webTarget
       //     .register(adminAuth)
       
    }
    @Test
    public void test19_add_address_to_customer0_by_user0() throws JsonMappingException, JsonProcessingException {
        //Response response = webTarget
         //   .register(user0_Auth)
    }
    @Test
    public void test20_add_address_to_customer0_by_user1() throws JsonMappingException, JsonProcessingException {
      //Response response = webTarget
        //   .register(user1_Auth)
    }
    @Test
    public void test21_add_address_to_customer1_by_adminrole() throws JsonMappingException, JsonProcessingException {
       // Response response = webTarget
       //     .register(adminAuth)
          
    }
    @Test
    public void test22_add_address_to_customer1_by_user0() throws JsonMappingException, JsonProcessingException {
        //Response response = webTarget
         //   .register(user0_Auth)
    }
    @Test
    public void test23_add_address_to_customer1_by_user1() throws JsonMappingException, JsonProcessingException {
      //Response response = webTarget
        //   .register(user1_Auth)
    }
    //------------New customer
 
    @Test
    public void test24_create_new_customer_by_userA() {
        Response response = webTarget
            .register(userA_Auth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .post(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        //assertThat(response.getStatus(),is(UNAUTHORIZED.getStatusCode()));
        assertThat(response.getStatus(), is(403));
    }
    
    @Test
    public void test25_create_new_customer_by_userB() {
        Response response = webTarget
            .register(userB_Auth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .post(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(),is(403));
    }
  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /*
    @Test
    public void test26_create_new_customer_by_adminrole() {
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
    */
   //----------------
  /*
    @Test
    public void test27_update_new_customer_by_adminrole() {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, newCustomer.getId())
            .request()
            .put(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(),is(OK.getStatusCode()));
        //// add something
    }
    @Test
    public void test28_update_new_customer_by_userA() {
        Response response = webTarget
            .register(userA_Auth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, newCustomer.getId())
            .request()
            .put(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        //assertThat(response.getStatus(),is(OK.getStatusCode()));
        //// check!
        assertThat(response.getStatus(),is(401));
    }
    
    @Test
    public void test29_update_new_customer_by_that_user() {
        Response response = webTarget
            // a new SecurityUser will be created after a new customer is created,  USER_ROLE is 'user1'
            .register(userNew_Auth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, newCustomer.getId())
            .request()
            .put(Entity.entity(newCustomer, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(),is(OK.getStatusCode()));
    }
    */
    /*
    @Test
    public void test30_remove_new_customer_by_userA() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userA_Auth)
           // .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
           // .resolveTemplate(RESOURCE_PATH_ID_ELEMENT,newCustomer.getId())
            .path("/customer/4")
            .request()
            .delete();
        //assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        assertThat(response.getStatus(),is(403));
        assertThat(newCustomer, is(not(nullValue())));
    }
    @Test
    public void test31_remove_new_customer_by_userB() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userB_Auth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, newCustomer.getId())
            .request()
            .delete();
        assertThat(response.getStatus(), is(403));
     // HttpErrorResponse errorResponse = response.readEntity(new GenericType<HttpErrorResponse>(){});
        // assertThat(errorResponse.getStatusCode(), is(401));
       //  assertThat(errorResponse.getReasonPhrase(), is(ACCESS_REQUIRES_AUTHENTICATION));
         assertThat(response.getStatus(), is(FORBIDDEN.getStatusCode()));
        assertThat(newCustomer, is(not(nullValue())));
    }
    */
    /*
    @Test
    public void test32_remove_new_customer_by_that_user() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userNew_Auth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT,newCustomer.getId())
            .request()
            .delete();
        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
        assertThat(newCustomer.getId(), is(not(nullValue())));
    }
    @Test
    public void test33_remove_new_customer_by_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT,newCustomer.getId())
            .request()
            .delete();
       // assertThat(response.getStatus(),is(NO_CONTENT.getStatusCode()));
        HttpErrorResponse errorResponse = response.readEntity(new GenericType<HttpErrorResponse>(){});
        assertThat(errorResponse.getStatusCode(), is(500));
        assertThat(response.getStatus(), is(FORBIDDEN.getStatusCode()));
        assertThat(newCustomer.getId(), is((nullValue())));
        
    }
    */

    @Test
    public void test33_remove_new_customer_by_adminrole() throws JsonMappingException, JsonProcessingException {
        
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            //.resolveTemplate(RESOURCE_PATH_ID_ELEMENT,newCustomer.getId())
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT,6)
            .request()
            .delete();
      //  assertThat(response.getStatus(),is(OK.getStatusCode()));
     assertThat(response.getStatus(),is(NO_CONTENT.getStatusCode()));
    }
       // assertThat(newCustomer.getId(), is((nullValue())));
     //   assertThat(response.getStatus(), is(500));
    //    HttpErrorResponse errorResponse = response.readEntity(new GenericType<HttpErrorResponse>(){});
   //     assertThat(errorResponse.getReasonPhrase(), is("Resource not found"));
 //   }
    
   /* @Test
    public void test34_remove_no_customer_by_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT,100)
            .request()
            .delete();
        //AdminRole is unable to remove non-existing customer from database
        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
        HttpErrorResponse errorResponse = response.readEntity(new GenericType<HttpErrorResponse>(){});
        assertThat(errorResponse.getReasonPhrase(), is("customer does not found"));
        CustomerPojo cust = response.readEntity(new GenericType<CustomerPojo>(){});
        assertThat(cust, is((nullValue())));
        
    }
    */
    
    @Test
    public void test40_create_new_store_with_new_product() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");

        StorePojo store = new StorePojo();
        store.setStoreName("WALMART-BASELINE");
        
        ProductPojo product = new ProductPojo();

        product.setDescription("Old Navey Baby Cloth");
        product.setSerialNo("6666-121-2323");
        
        Set<ProductPojo> productList = new HashSet<>();
        productList.add(product);
        store.setProducts(productList);
        
        Response response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .post(Entity.entity(store, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test40_create_new_store() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");

        StorePojo store = new StorePojo();
        store.setStoreName("WALMART-KANATA");
        
        Response response = webTarget
                .register(adminAuth)
                .path(STORE_RESOURCE_NAME)
                .request()
                .post(Entity.entity(store, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    
    @Test
    public void test40_create_new_product() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");

        ProductPojo product = new ProductPojo();

        product.setDescription("LG LED HD TV");
        product.setSerialNo("1111-222-3333");

        Response response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .post(Entity.entity(product, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test41_find_all_products() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>(){});
        assertThat(products, is(not(empty())));
        assertThat(products, hasSize(this.TOTAL_NUMBER_OF_PRODUCTS));
        
        ProductPojo firstProduct = products.get(0);
        assertThat(firstProduct.getDescription(), is(equalToIgnoringCase(PROD1_DESCRIPTION)));
        assertThat(firstProduct.getSerialNo(), is(equalToIgnoringCase(PROD1_SERIALNO)));
    }
    
    @Test
    public void test42_find_product_by_id() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        ProductPojo foundProduct = response.readEntity(new GenericType<ProductPojo>(){});
        assertThat(foundProduct.getDescription(), is(equalToIgnoringCase(PROD1_DESCRIPTION)));
        assertThat(foundProduct.getSerialNo(), is(equalToIgnoringCase(PROD1_SERIALNO)));
    }
    
    @Test
    public void test43_find_product_by_wrong_id() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 9999)
                .request()
                .get();

        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
    }
    

    @Test
    public void test44_find_product_by_description() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME + "/parameters")
                .queryParam("description", "milk")
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        List<ProductPojo> foundProducts = response.readEntity(new GenericType<List<ProductPojo>>(){});
        foundProducts.stream().forEach(p -> assertThat(p.getDescription(), containsStringIgnoringCase("milk")));
    }
    
    @Test
    public void test45_update_product_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .get();
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>(){});
        ProductPojo lastProduct = products.get(products.size() - 1);
        
        String origSerialNo = lastProduct.getSerialNo();
        String newSerialNo = origSerialNo + "-007";
        
        lastProduct.setSerialNo(newSerialNo);
        
        response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .post(Entity.entity(lastProduct, MediaType.APPLICATION_JSON_TYPE));
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test46_add_product_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .get();
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>(){});
        ProductPojo lastProduct = products.get(products.size() - 1);
        
        ProductPojo newProduct = new ProductPojo();
        
        newProduct.setId(lastProduct.getId() + 100);
        newProduct.setDescription(lastProduct.getDescription() + " new style");
        newProduct.setSerialNo(lastProduct.getSerialNo() + "649");
        
        response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .post(Entity.entity(newProduct, MediaType.APPLICATION_JSON_TYPE));
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test47_delete_product_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .get();
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>(){});
        ProductPojo lastProduct = products.get(products.size() - 1);
        
        response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, lastProduct.getId())
                .request()
                .delete();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test48_delete_product_with_wrong_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 9999)
                .request()
                .delete();
        
        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
    }
    
    // org.eclipse.persistence.exceptions.OptimisticLockException
    @Test
    public void test49_add_all_stores_to_all_products_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));     
        Set<ProductPojo> allProducts = response.readEntity(new GenericType<Set<ProductPojo>>(){});
        
        response = webTarget
                .path(STORE_RESOURCE_NAME)
                .request()
                .get();        
        assertThat(response.getStatus(), is(OK.getStatusCode()));

        Set<StorePojo> allStores = response.readEntity(new GenericType<Set<StorePojo>>(){});
        
        allProducts.stream().forEach(p -> p.setStores(allStores));       
        for (ProductPojo product : allProducts) {
            response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .put(Entity.entity(product, MediaType.APPLICATION_JSON_TYPE));
            
            assertThat(response.getStatus(), is(OK.getStatusCode()));
        }
        
        response = webTarget
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        Set<ProductPojo> allUpdatedProducts = response.readEntity(new GenericType<Set<ProductPojo>>(){});
        
        // Get all stores' names prepared for checking
        List<String> allStoreNames = new ArrayList<>();
        allStores.stream().forEach(s -> allStoreNames.add(s.getStoreName()));        
        
        for (ProductPojo updatedProduct : allUpdatedProducts) {
            List<String> allProductStoreNames = new ArrayList<>();
            updatedProduct.getStores().stream().forEach(s -> allProductStoreNames.add(s.getStoreName()));
            // Check each product has all stores by names
            assertThat(allProductStoreNames, containsInAnyOrder(allStoreNames));  
        }
    }
    
    // org.eclipse.persistence.exceptions.OptimisticLockException
    @Test
    public void test50_add_last_store_to_last_product_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));     
        Set<ProductPojo> productSet = response.readEntity(new GenericType<Set<ProductPojo>>(){});
        
        List<ProductPojo> productList = new ArrayList<>(productSet);
        ProductPojo lastProduct = productList.get(productList.size() - 1);
        
        response = webTarget
                .path(STORE_RESOURCE_NAME)
                .request()
                .get();        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        Set<StorePojo> storeSet = response.readEntity(new GenericType<Set<StorePojo>>(){});
        
        List<StorePojo> storeList = new ArrayList<>(storeSet);
        StorePojo lastStore = storeList.get(storeList.size() - 1);
        
        Set<StorePojo> productStores = lastProduct.getStores();
        productStores.add(lastStore);
        
        Set<ProductPojo> storeProduct = lastStore.getProducts();
        storeProduct.add(lastProduct);
        
//        lastProduct.setStores(new HashSet<StorePojo>(){{add(lastStore);}});
        lastProduct.setStores(productStores);
        
        response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .put(Entity.entity(lastProduct, MediaType.APPLICATION_JSON_TYPE));
            
       assertThat(response.getStatus(), is(OK.getStatusCode()));
       
       
       response = webTarget
               .register(adminAuth)
               .path(STORE_RESOURCE_NAME)
               .request()
               .put(Entity.entity(lastStore, MediaType.APPLICATION_JSON_TYPE));
           
      assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test50_add_a_store_to_a_products_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        ProductPojo foundProduct = response.readEntity(new GenericType<ProductPojo>(){});
        
        response = webTarget
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        StorePojo foundStore = response.readEntity(new GenericType<StorePojo>(){});
        
        Set<StorePojo> productStores = foundProduct.getStores();
        productStores.add(foundStore);
        
        Set<ProductPojo> storeProduct = foundStore.getProducts();
        storeProduct.add(foundProduct);
        

        foundProduct.setStores(productStores);
        foundStore.setProducts(storeProduct);
        
        response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .put(Entity.entity(foundProduct, MediaType.APPLICATION_JSON_TYPE));
            
       assertThat(response.getStatus(), is(OK.getStatusCode()));
       
       response = webTarget
               .register(adminAuth)
               .path(STORE_RESOURCE_NAME)
               .request()
               .put(Entity.entity(foundStore, MediaType.APPLICATION_JSON_TYPE));
       
       assertThat(response.getStatus(), is(OK.getStatusCode()));
   
    }
    
    @Test
    public void test51_find_all_stores() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(STORE_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));

        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>(){});
        assertThat(stores, is(not(empty())));
        assertThat(stores, hasSize(this.TOTAL_NUMBER_OF_STORES));
        
        StorePojo firstStore = stores.get(0);
        assertThat(firstStore.getStoreName(), is(equalToIgnoringCase(STORE1_NAME)));
    }
    
    @Test
    public void test52_find_store_by_id() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        Response response = webTarget
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        StorePojo foundStore = response.readEntity(new GenericType<StorePojo>(){});
        assertThat(foundStore.getStoreName(), is(equalToIgnoringCase(STORE1_NAME)));
    }
    
    @Test
    public void test53_find_store_by_wrong_id() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        Response response = webTarget
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 9999)
                .request()
                .get();

        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
    }
    
    @Test
    public void test54_find_store_by_name() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        Response response = webTarget
                .path(STORE_RESOURCE_NAME + "/parameters")
                .queryParam("storename", "Cost")
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        List<StorePojo> foundStores = response.readEntity(new GenericType<List<StorePojo>>(){});
        foundStores.stream().forEach(s -> assertThat(s.getStoreName(), containsStringIgnoringCase("cost")));
    }
    
    @Test
    public void test55_update_store_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(STORE_RESOURCE_NAME)
                .request()
                .get();
        
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>(){});
        StorePojo lastStores = stores.get(stores.size() - 1);
        
        String origStoreName = lastStores.getStoreName();
        String newStoreName = origStoreName + " Super Store";
        
        lastStores.setStoreName(newStoreName);
        
        response = webTarget
                .register(adminAuth)
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .post(Entity.entity(lastStores, MediaType.APPLICATION_JSON_TYPE));
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test56_add_store_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(STORE_RESOURCE_NAME)
                .request()
                .get();
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>(){});
        
        StorePojo newStore = new StorePojo();
        
        newStore.setStoreName("Home Depot");
        
        response = webTarget
                .register(adminAuth)
                .path(STORE_RESOURCE_NAME)
                .request()
                .post(Entity.entity(newStore, MediaType.APPLICATION_JSON_TYPE));
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
        
    @Test
    public void test57_delete_store_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(STORE_RESOURCE_NAME)
                .request()
                .get();
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>(){});
        StorePojo lastStore = stores.get(stores.size() - 1);
        
        response = webTarget
                .register(adminAuth)
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, lastStore.getId())
                .request()
                .delete();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test58_delete_store_with_wrong_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .register(adminAuth)
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 9999)
                .request()
                .delete();
        
        assertThat(response.getStatus(), is(NOT_FOUND.getStatusCode()));
    }
    
    // org.eclipse.persistence.exceptions.OptimisticLockException
    @Test
    public void test59_add_a_product_to_a_store_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        ProductPojo foundProduct = response.readEntity(new GenericType<ProductPojo>(){});
        
        response = webTarget
                .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        StorePojo foundStore = response.readEntity(new GenericType<StorePojo>(){});
        
        foundStore.setProducts(new HashSet<ProductPojo>(){{add(foundProduct);}}); 
        
        response = webTarget
                .register(adminAuth)
                .path(STORE_RESOURCE_NAME)
                .request()
                .put(Entity.entity(foundStore, MediaType.APPLICATION_JSON_TYPE));
            
       assertThat(response.getStatus(), is(OK.getStatusCode()));
   
    }
    
    
    @Test
    public void add_last_product_to_last_store_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(STORE_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        List<StorePojo> stores = response.readEntity(new GenericType<List<StorePojo>>(){});
        StorePojo lastStore = stores.get(stores.size() - 1);
        
        response = webTarget
                .path(PRODUCT_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        List<ProductPojo> products = response.readEntity(new GenericType<List<ProductPojo>>(){});
        ProductPojo lastProduct = products.get(products.size() - 1);
                
        Set<ProductPojo> storeProducts = lastStore.getProducts();
        storeProducts.add(lastProduct);
        
        lastStore.setProducts(storeProducts);
        
        response = webTarget
             .register(adminAuth)
             .path(STORE_RESOURCE_NAME)
             .request()
             .put(Entity.entity(lastStore, MediaType.APPLICATION_JSON_TYPE));
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));        
    }
    
    @Test
    public void test60_find_all_orders() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(ORDER_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));

        List<OrderPojo> orders = response.readEntity(new GenericType<List<OrderPojo>>(){});
        assertThat(orders, is(not(empty())));
        
        OrderPojo firstOrder = orders.get(0);
        assertThat(firstOrder.getDescription(), is(equalToIgnoringCase(ORDER1_DESCRIPTION)));
    }
    
    @Test
    public void test61_find_order_by_id() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        int orderId = 1;
        Response response = webTarget
                .path(ORDER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, orderId)
                .request()
                .get();

        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        OrderPojo foundOrder = response.readEntity(new GenericType<OrderPojo>(){});
        assertThat(foundOrder.getDescription(), is(equalToIgnoringCase(ORDER1_DESCRIPTION)));
    }
    
    @Test
    public void test62_create_new_plain_order() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        OrderPojo newOrder = new OrderPojo();
        newOrder.setDescription("Thanksgiving Order");
        
        Response response = webTarget
                .register(adminAuth)
                .path(ORDER_RESOURCE_NAME)
                .request()
                .post(Entity.entity(newOrder, MediaType.APPLICATION_JSON_TYPE));
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    // org.eclipse.persistence.exceptions.OptimisticLockException  
    @Test
    public void test63_update_order_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(ORDER_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));

        List<OrderPojo> orders = response.readEntity(new GenericType<List<OrderPojo>>(){});
        OrderPojo lastOrder = orders.get(orders.size() - 1 );
        
        String origOrderDescription = lastOrder.getDescription();
        String newOrderDescription = "Christmas Order";
        
        lastOrder.setDescription(newOrderDescription);
        
        response = webTarget
                .register(adminAuth)
                .path(ORDER_RESOURCE_NAME)
                .request()
                .put(Entity.entity(lastOrder, MediaType.APPLICATION_JSON_TYPE));
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test64_delete_order_by_id_without_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(ORDER_RESOURCE_NAME)
                .request()
                .get();
        List<OrderPojo> orders = response.readEntity(new GenericType<List<OrderPojo>>(){});
        OrderPojo lastOrder = orders.get(orders.size() - 1);
        
        response = webTarget
                .path(ORDER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, lastOrder.getId())
                .request()
                .delete();
        
        assertThat(response.getStatus(), is(UNAUTHORIZED.getStatusCode()));
    }
  
    @Test
    public void test65_delete_order_by_id_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                .path(ORDER_RESOURCE_NAME)
                .request()
                .get();
        List<OrderPojo> orders = response.readEntity(new GenericType<List<OrderPojo>>(){});
        OrderPojo lastOrder = orders.get(orders.size() - 1);
        
        response = webTarget
                .register(adminAuth)
                .path(ORDER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, lastOrder.getId())
                .request()
                .delete();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test65_create_order_for_last_customer() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        OrderPojo order1 = new OrderPojo();
        order1.setDescription("Walmart order");

        OrderPojo order2 = new OrderPojo();
        order2.setDescription("Costco order");

        Set<OrderPojo> orders = new HashSet<>();
        orders.add(order1);
//        orders.add(order2);
        
        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        List<CustomerPojo> allCustomers = response.readEntity(new GenericType<List<CustomerPojo>>(){});
            
        CustomerPojo lastCustomer = allCustomers.get(allCustomers.size() - 1);
        
        lastCustomer.setOrders(orders);
        
        response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_ORDER)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, lastCustomer.getId())
                .request()
                .put(Entity.entity(lastCustomer, MediaType.APPLICATION_JSON_TYPE));
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
   
    // org.eclipse.persistence.exceptions.OptimisticLockException
    @Test
    public void test70_add_last_order_to_customer() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        List<CustomerPojo> allCustomers = response.readEntity(new GenericType<List<CustomerPojo>>(){});
            
        CustomerPojo firstCustomer = allCustomers.get(0);
         
        response = webTarget
                .path(ORDER_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));

        List<OrderPojo> orders = response.readEntity(new GenericType<List<OrderPojo>>(){});
        OrderPojo lastOrder = orders.get(orders.size() - 1 );
        
        lastOrder.setOwningCustomer(firstCustomer);
        
        Set<OrderPojo> custCustOrders = firstCustomer.getOrders();
        
        custCustOrders.add(lastOrder);
        
        response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_ORDER)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, firstCustomer.getId())
                .request()
                .put(Entity.entity(custCustOrders, MediaType.APPLICATION_JSON_TYPE));
                
        assertThat(response.getStatus(), is(OK.getStatusCode()));    
    }
    
    @Test
    public void test71_add_new_order_to_first_customer() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        Response response = webTarget
                //.register(userAuth)
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME)
                .request()
                .get();
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        List<CustomerPojo> allCustomers = response.readEntity(new GenericType<List<CustomerPojo>>(){});
            
        CustomerPojo firstCustomer = allCustomers.get(0);
         
        response = webTarget
                .path(ORDER_RESOURCE_NAME)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));

        OrderPojo newOrder = new OrderPojo();
        
        newOrder.setDescription("Farm Boy Order");
        newOrder.setOwningCustomer(firstCustomer);
        
        Set<OrderPojo> custCustOrders = firstCustomer.getOrders();
        custCustOrders.add(newOrder);
        
        response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_ORDER)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, firstCustomer.getId())
                .request()
                .put(Entity.entity(custCustOrders, MediaType.APPLICATION_JSON_TYPE));
                
        assertThat(response.getStatus(), is(OK.getStatusCode()));    
    }
    
    
    
    @Test
    public void test80_add_shipping_address_to_first_customer_by_UserA() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
          
        ShippingAddressPojo shippingAddr = new ShippingAddressPojo();
        shippingAddr.setStreet("200 Earl Grey Dr");
        shippingAddr.setCity("Ottawa");
        shippingAddr.setCountry("Canada");
        shippingAddr.setPostal("K2T 1B6");
        shippingAddr.setState("ON");
        
        //only Admin role is able to add address to customer
        Response response = webTarget
           .register(adminAuth)
           .path(CUSTOMER_RESOURCE_NAME + CUSTOMER_ADDRESS_RESOURCE_PATH)
           .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
           .request()
           .put(Entity.entity(shippingAddr, MediaType.APPLICATION_JSON_TYPE));
        

        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test81_add_billing_address_to_first_customer_by_UserA() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
          
        BillingAddressPojo billingAddr = new BillingAddressPojo();
        billingAddr.setStreet("1980 Baseline Rd");
        billingAddr.setCity("Ottawa");
        billingAddr.setCountry("Canada");
        billingAddr.setPostal("K2T 1B6");
        billingAddr.setState("ON");
        billingAddr.setAlsoShipping(false);
        
        //only Admin role is able to add address to customer
        Response response = webTarget
           .register(adminAuth)
           .path(CUSTOMER_RESOURCE_NAME + CUSTOMER_ADDRESS_RESOURCE_PATH)
           .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
           .request()
           .put(Entity.entity(billingAddr, MediaType.APPLICATION_JSON_TYPE));
        

        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
    
    @Test
    public void test82_get_all_addresses_from_first_customer_with_roleadmin() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        BillingAddressPojo billingAddr = new BillingAddressPojo();
        billingAddr.setStreet("1982 Baseline Rd");
        billingAddr.setCity("Ottawa");
        billingAddr.setCountry("Canada");
        billingAddr.setPostal("K2T 1B6");
        billingAddr.setState("ON");
        billingAddr.setAlsoShipping(false);
        
        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME + CUSTOMER_ADDRESS_RESOURCE_PATH)
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .get();
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
        
        List<AddressPojo> allCustAddresses = response.readEntity(new GenericType<List<AddressPojo>>(){});
        
        for (AddressPojo address : allCustAddresses) {
            if (address instanceof BillingAddressPojo) {
                BillingAddressPojo billingAddressFromDB = (BillingAddressPojo) address;
                
                assertThat(billingAddressFromDB.getStreet(), is(equalToIgnoringCase("1980 Baseline Rd")));
                
                assertThat(billingAddressFromDB.getCity(), is(equalToIgnoringCase("Ottawa")));
                assertThat(billingAddressFromDB.getState(), is(equalToIgnoringCase("ON")));
                assertThat(billingAddressFromDB.getCountry(), is(equalToIgnoringCase("Canada")));
                assertThat(billingAddressFromDB.getPostal(), is(equalToIgnoringCase("K2T 1B6")));       
            } else if (address instanceof ShippingAddressPojo){
                ShippingAddressPojo shippingAddrFromDB = (ShippingAddressPojo) address;
                
                assertThat(shippingAddrFromDB.getStreet(), is(equalToIgnoringCase("200 Earl Grey Dr")));   
                assertThat(shippingAddrFromDB.getCity(), is(equalToIgnoringCase("Ottawa")));
                assertThat(shippingAddrFromDB.getState(), is(equalToIgnoringCase("ON")));
                assertThat(shippingAddrFromDB.getCountry(), is(equalToIgnoringCase("Canada")));
                assertThat(shippingAddrFromDB.getPostal(), is(equalToIgnoringCase("K2T 1B6")));
            }
        }      
    }
    
    @Test
    public void test83_update_billing_address_for_first_customer_with_adminrole() throws JsonMappingException, JsonProcessingException {
        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
        
        BillingAddressPojo billingAddr = new BillingAddressPojo();
        billingAddr.setStreet("1882 Baseline Rd");
        billingAddr.setCity("Ottawa");
        billingAddr.setCountry("Canada");
        billingAddr.setPostal("K2T 1B6");
        billingAddr.setState("ON");
        billingAddr.setAlsoShipping(true);
        
        Response response = webTarget
                .register(adminAuth)
                .path(CUSTOMER_RESOURCE_NAME + CUSTOMER_ADDRESS_RESOURCE_PATH + "/billing")
                .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
                .request()
                .put(Entity.entity(billingAddr, MediaType.APPLICATION_JSON_TYPE));
        
        assertThat(response.getStatus(), is(OK.getStatusCode()));
    }
        
    
//    @Test
//    public void test84_shipping_address_from_first_customer_by_UserA() throws JsonMappingException, JsonProcessingException {
//        logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
//
//   }
//    
   @Test
   public void test85_create_new_customer() throws JsonMappingException, JsonProcessingException {
       logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
       
       CustomerPojo customer = new CustomerPojo();
       
       customer.setEmail("zhao0066@algonquinlive.com");
       customer.setFirstName("Angela");
       customer.setLastName("Zhao");
       customer.setPhoneNumber("613-345-8861");
       
       Response response = webTarget
               .register(adminAuth)
               .path(CUSTOMER_RESOURCE_NAME)
               .request()
               .post(Entity.entity(customer, MediaType.APPLICATION_JSON_TYPE));
       
       assertThat(response.getStatus(), is(OK.getStatusCode()));
   }
   
   @Test
   public void test86_delete_customer_billingAddress() throws JsonMappingException, JsonProcessingException {
       logger.info("\n\n[" + Thread.currentThread().getStackTrace()[1].getMethodName() + "] ==>");
           
       Response response = webTarget
               .register(adminAuth)
               .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
               .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
               .request()
               .get();
       
       CustomerPojo firstCustomer = response.readEntity(new GenericType<CustomerPojo>(){});
       int addressId = firstCustomer.getBillingAddress().getId();
       
                   
       response = webTarget
               .register(adminAuth)
               .path(CUSTOMER_RESOURCE_NAME + CUSTOMER_ADDRESS_RESOURCE_PATH + "/{idAddress}")
               .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, firstCustomer.getId())
               .resolveTemplate("idAddress", addressId)
               .request()
               .delete();
       
       assertThat(response.getStatus(), is(OK.getStatusCode()));
   }
}