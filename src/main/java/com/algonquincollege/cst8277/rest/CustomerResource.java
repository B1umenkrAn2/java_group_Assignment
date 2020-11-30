/*****************************************************************c******************o*******v******id********
 * File: CustomerResource.java
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

package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ADDRESS_RESOURCE_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ORDER_RESOURCE_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.ORDER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.STORE_RESOURCE_NAME;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.soteria.WrappingCallerPrincipal;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderLinePojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.SecurityUser;


@Path(CUSTOMER_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    //private static final Status NO_CONTENT = null;

    @EJB
    protected CustomerService customerServiceBean;

    @Inject
    protected ServletContext servletContext;

    @Inject
    protected SecurityContext sc;
    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getCustomers() {
        servletContext.log("retrieving all customers ...");
        List<CustomerPojo> custs = customerServiceBean.getAllCustomers();
        Response response = Response.ok(custs).build();
        return response;
    }
    
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getCustomerById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific customer " + id);
        Response response = null;
        CustomerPojo cust = null;

        if (sc.isCallerInRole(ADMIN_ROLE)) {
            cust = customerServiceBean.getCustomerById(id);
            response = Response.status( cust == null ? NOT_FOUND : OK).entity(cust).build();
        }
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            cust = sUser.getCustomer();
            if (cust != null && cust.getId() == id) {
                response = Response.status(OK).entity(cust).build();
            }
            else {
                throw new ForbiddenException();
            }
        }
        else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }
    
    @POST
    @Transactional
    @RolesAllowed({ADMIN_ROLE})
    public Response addCustomer(CustomerPojo newCustomer) {
      Response response = null;
      CustomerPojo newCustomerWithIdTimestamps = customerServiceBean.persistCustomer(newCustomer);
      //build a SecurityUser linked to the new customer
      customerServiceBean.buildUserForNewCustomer(newCustomerWithIdTimestamps);
      response = Response.ok(newCustomerWithIdTimestamps).build();
      return response;
    }
    //update customer
    @PUT
    //@RolesAllowed({USER_ROLE})
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response updateCustomer(CustomerPojo customerToUpdate) {
        servletContext.log("updateCustomer " + customerToUpdate.getId());
      Response response = null;
      CustomerPojo customerToUpdateWithIdTimestamps = customerServiceBean.updateCustomer(customerToUpdate);
      response = Response.ok(customerToUpdateWithIdTimestamps).build();
      return response;
    }
    
    //remove customer
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response removeCustomerById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("removeCustomerById " + id);
        customerServiceBean.removeCustomer(id);
        customerServiceBean.removeUserFromCustomer(id);
        Response response = Response.status(NO_CONTENT).build();
        return response;
    }
    
    
    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Path(CUSTOMER_ADDRESS_RESOURCE_PATH)
    public Response addAddressForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, AddressPojo newAddress) {
        servletContext.log("addAddress " + newAddress.toString() + "customer id " + id);
        Response response = null;
      CustomerPojo updatedCustomer = customerServiceBean.setAddressFor(id, newAddress);
      response = Response.ok(updatedCustomer).build();
      return response;
    }
    //TODO - endpoints for setting up Orders/OrderLines
    //addOrdersForCustomer
    @PUT
    //@RolesAllowed({ADMIN_ROLE})
    @PermitAll
    @Path(CUSTOMER_ORDER_RESOURCE_PATH)
    public Response addOrdersForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, List<OrderPojo> newOrders) {
      Response response = null;
      CustomerPojo updatedCustomer = customerServiceBean.setOrdersForCustomer(id, newOrders);
      response = Response.ok(updatedCustomer).build();
      return response;
    }
   
    //get All Orders
    @GET
    @RolesAllowed({ADMIN_ROLE})
    @Path(ORDER_RESOURCE_NAME)
    public Response getOrders() {
        servletContext.log("retrieving all orders ...");
        List<OrderPojo> orders = customerServiceBean.getAllOrders();
        Response response = Response.ok(orders).build();
        return response;
    }
    
    //getOrderById
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path("order/{id:}/")
    //@Path(RESOURCE_PATH_ID_PATH)
    public Response getOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific order " + id);
        Response response = null;
        OrderPojo order = null;
        CustomerPojo cust = null;
        
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            order = customerServiceBean.getOrderbyId(id);
            response = Response.status( order == null ? NOT_FOUND : OK).entity(order).build();
        }
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            cust = sUser.getCustomer();
            //verfy customer name
            order = cust.getOrders().get(id);
            if (order != null) {
                //order = customerServiceBean.getOrderbyId(id);
                response = Response.status(OK).entity(order).build();
            }
            else {
                throw new ForbiddenException();
            }
        }
        else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }
    /* **************************
  //add order
    @POST
    @Transactional
    @PermitAll
    public Response addOrder(OrderPojo newOrder) {
      Response response = null;
      OrderPojo newOrderWithIdTimestamps = customerServiceBean.persistOrder(newOrder);
      response = Response.ok(newOrderWithIdTimestamps).build();
      return response;
    }
    */
    /*
    //---removeOrderById-------------------no need ----------------------
    //remove order
    @DELETE
    //@RolesAllowed({ADMIN_ROLE})
    @PermitAll
    @Path(RESOURCE_PATH_ID_PATH)
    public Response removeOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("removeOrderById " + id);
        customerServiceBean.removeOrder(id);
        Response response = Response.status(NO_CONTENT).build();
        return response;
    }
     */
    
    //addOrderLinesToOrder
    @PUT
    //@RolesAllowed({USER_ROLE})
    @PermitAll
    @Path(STORE_RESOURCE_NAME)
    public Response addOrderLinesToOrder(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id,  List<OrderLinePojo> orderLines) {
        servletContext.log("try to retrieve specific order " + id);
        Response response = null;
        OrderPojo order = null;
        
            order = customerServiceBean.addOrderLinesToOrder(id, orderLines);
            response = Response.ok(order).build();
            return response;
  }

    @DELETE
    @Path("/address/{id:}")
    @Transactional
    public Response deleteCustomerAddressByid(@PathParam("id") int id) {
        AddressPojo addressPojo = customerServiceBean.removeAddressByid(id);
        return Response.ok(addressPojo).build();
    }

    @PUT
    @Path("/address/{id:}")
    @Transactional
    public Response updateAddressByid(@PathParam("id") int id, AddressPojo ap) {
        ap.setId(id);
        AddressPojo addressPojo = customerServiceBean.updateAddress(ap);
        return Response.ok(addressPojo).build();
    }
//    @GET
//    @Path(CUSTOMER_RESOURCE_ORDER)
//    public Response getCustomerAllOrder(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
//        List<OrderPojo> allOrders = customerServiceBean.getCustomerALLOrders(id);
//        return Response.ok(allOrders).build();
//
//    }

//    @GET
//    @Path("{id:}/order/{id2:}/orderline")
//    public Response getCustomerOrderLineByOrderId(@PathParam("id") int id, @PathParam("id2") int id2) {
//
//        List<OrderLinePojo> allOrderLineById = customerServiceBean.getOneOrderALLOrderLineById(id2);
//        return Response.ok(allOrderLineById).build();
//    }

//    @GET
//    @Path("{id:}/order/{id2:}/")
//    public Response getCustomerAllOrderByOrderId(@PathParam("id") int id, @PathParam("id2") int id2) {
//
//        OrderPojo orderById = customerServiceBean.getOrderById(id2);
//        return Response.ok(orderById).build();
//    }
}