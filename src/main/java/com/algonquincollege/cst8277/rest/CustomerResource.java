/*****************************************************************c******************o*******v******id********
 * File: CustomerResource.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 *
 * update by : Lai Shan Law (040595733)
 *             Siyang Xiong (040938012)
 *             Angela Zhao  (040529234)
 *
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.algonquincollege.cst8277.models.*;
import org.glassfish.soteria.WrappingCallerPrincipal;

import com.algonquincollege.cst8277.ejb.CustomerService;

@Path(CUSTOMER_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @EJB
    protected CustomerService customerServiceBean;

    @Inject
    protected ServletContext servletContext;

    @Inject
    protected SecurityContext sc;

    /**
     * Get all customers, only user with ADMIN_ROLE can query
     * 
     * @return JAX-RS response object
     */
    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getCustomers() {
        servletContext.log("retrieving all customers ...");
        List<CustomerPojo> custs = customerServiceBean.getAllCustomers();
        servletContext.log("\n\nGot total [" + custs.size() + "] customers\n\n");
        return Response.status(custs == null ? NOT_FOUND : OK).entity(custs).build();
    }


    /**
     * Get customer by its id. Either user with ADMIN_ROLE can retrieve, but user
     * with USER_ROLE can only retrieve its own record
     * 
     * @param id customer id
     * @return JAX-RS response object
     */
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getCustomerById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific customer " + id);
        Response response = null;
        CustomerPojo cust = null;

        if (sc.isCallerInRole(ADMIN_ROLE)) {
            cust = customerServiceBean.getCustomerById(id);
            response = Response.status(cust == null ? NOT_FOUND : OK).entity(cust).build();
        } else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal) sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser) wCallerPrincipal.getWrapped();
            cust = sUser.getCustomer();
            if (cust != null && cust.getId() == id) {
                response = Response.status(OK).entity(cust).build();
            } else {
                throw new ForbiddenException();
            }
        } else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }

    /**
     * Add a new customer. Only user with ADMIN_ROLE can add customer
     * 
     * @param newCustomer newCustomer to add
     * @return JAX-RS response object
     */
    @POST
    @RolesAllowed({ADMIN_ROLE})
    @Transactional
    public Response addCustomer(CustomerPojo newCustomer) {
        servletContext.log("\n\n\n===> try to add a new customer " + newCustomer.getId());
        CustomerPojo newCustomerWithIdTimestamps = customerServiceBean.persistCustomer(newCustomer);
        //build a SecurityUser linked to the new customer
        customerServiceBean.buildUserForNewCustomer(newCustomerWithIdTimestamps);
        return Response.ok(newCustomerWithIdTimestamps).build();

    }

    /**
     * Delete a customer by its id. Only user with ADMIN_ROLE is allowed to delete
     * @param id
     * @return
     */
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    @Transactional
    public Response deleteCustomerById(@PathParam("id") int id) {
        servletContext.log("try to delete a customer " + id);  
        CustomerPojo customerPojo = customerServiceBean.removeCustomerById(id);
        return Response.ok(customerPojo).build();
    }


    /**
     * Update a customer 
     * @param id
     * @param customerPojo
     * @return
     */
    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    @Transactional
    public Response updateCustomerByid(@PathParam("id") int id, CustomerPojo customerPojo) {
        customerPojo.setId(id);
        CustomerPojo updateCustomer = customerServiceBean.updateCustomer(customerPojo);
        return Response.ok(updateCustomer).build();
    }

    /**
     * Add/update the address for the specified customer
     * 
     * @param id customer id
     * @param newAddress new address to set
     * @return JAX-RS response object
     */
    @PUT
    @RolesAllowed({ ADMIN_ROLE, USER_ROLE })
    @Path(CUSTOMER_ADDRESS_RESOURCE_PATH)
    public Response addAddressForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, AddressPojo newAddress) {
        servletContext.log("addAddress " + newAddress.toString() + "customer id " + id);
        Response response = null;
        CustomerPojo updatedCustomer = customerServiceBean.setAddressFor(id, newAddress);
        response = Response.status(updatedCustomer == null ? NOT_FOUND : OK).entity(updatedCustomer).build();
        return response;
    }
    

//    Duplicated with above addAddressForCustomer
    
//    @PUT
//    @Path("/address/{id:}")
//    @Transactional
//    public Response updateAddressByid(@PathParam("id") int id, AddressPojo ap) {
//        ap.setId(id);
//        AddressPojo addressPojo = customerServiceBean.updateAddress(ap);
//        return Response.ok(addressPojo).build();
//    }
    
    
    /**
     * Get both billing and shipping address from a customer
     * 
     * @param id customer id
     * @return JAX-RS response object
     */
    @GET
    @RolesAllowed({ADMIN_ROLE, USER_ROLE})
    @Path(CUSTOMER_ADDRESS_RESOURCE_PATH)
    public Response getCustomerAllAddress(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        List<AddressPojo> customerAlladdressByCustId = customerServiceBean.getCustomerAlladdressByCustId(id);
        return Response.ok(customerAlladdressByCustId).build();
    }

//    @GET
//    @Path("{id:}/address/{id2:}")
//    public Response getCustomerAllAddress(@PathParam("id") int id, @PathParam("id2") int id2) {
//        AddressPojo customerAlladdressByCustId = customerServiceBean.getCustomerAddressByAddressId(id, id2);
//        return Response.ok(customerAlladdressByCustId).build();
//    }


    /**
     * Add orders for the specified customer
     * 
     * @param id customer id
     * @param newOrders new orders
     * @return
     */
    @PUT
    @PermitAll
    @Path(CUSTOMER_ORDER_RESOURCE_PATH)
    public Response addOrdersForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, Set<OrderPojo> newOrders) {
        Response response = null;
        CustomerPojo updatedCustomer = customerServiceBean.setOrdersForCustomer(id, newOrders);
        response = Response.ok(updatedCustomer).build();
        return response;
    }

    /**
     * Retrieve all orders
     * 
     * @return
     */
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
            if (cust != null && cust.getId() == id) {
                boolean isCustomerOrderId = false;
                for (OrderPojo custOrder : cust.getOrders()) {
                    if (custOrder.getId() == id) {
                        isCustomerOrderId = true;
                        break;
                    }
                }
                // Check if this order id belongs to current user
                if (isCustomerOrderId) {
                    order = customerServiceBean.getOrderbyId(id);
                    response = Response.status( order == null ? NOT_FOUND : OK).entity(order).build();
                } else {
                    response = Response.status(BAD_REQUEST).build();
                }        
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
    
    
  //getOrderById
    @GET
    @RolesAllowed({ADMIN_ROLE})
    @Path(CUSTOMER_ORDER_RESOURCE_PATH)//---("/{id:}/order")
    public Response getCustomerAllOrdersByCustId(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific order " + id);
        Response response = null;
        //List<OrderPojo> orders = null;
        OrderPojo orders = null;
        CustomerPojo cust = null;
        
        if (sc.isCallerInRole(ADMIN_ROLE)) {
            orders = customerServiceBean.getOrderById(id);
            response = Response.status( orders == null ? NOT_FOUND : OK).entity(orders).build();
        }
        
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            cust = sUser.getCustomer();
            //verfy customer name
            orders = customerServiceBean.getOrderById(id);
            if (orders != null && cust.getId() == id) {
                orders = customerServiceBean.getOrderById(id);
                response = Response.status(OK).entity(orders).build();
            }
            else {
                throw new ForbiddenException();
            }
        } else {
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
    
    /**
     * Add/Set order lines to an order
     * 
     * @param id order 
     * @param orderLines order lines
     * @return
     */
    @PUT
    //@RolesAllowed({USER_ROLE})
    @PermitAll
    @Path(ORDER_RESOURCE_NAME)
    public Response addOrderLinesToOrder(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, Set<OrderLinePojo> orderLines) {
        servletContext.log("try to retrieve specific order " + id);
        Response response = null;
        OrderPojo order = null;

        order = customerServiceBean.addOrderLinesToOrder(id, orderLines);
        response = Response.ok(order).build();
        return response;
    }

//    /**
//     * Delete a customer's address
//     * 
//     * @param id
//     * @return
//     */
//    @DELETE
//    @Path("/address/{id}")
//    @Transactional
//    public Response deleteCustomerAddressByid(@PathParam("id") int id) {
//        AddressPojo addressPojo = customerServiceBean.removeAddressByid(id);
//        return Response.ok(addressPojo).build();
//    }
    
    @PUT
    @Path("/address/{id:}")
    @Transactional
    public Response updateAddressByid(@PathParam("id") int id, AddressPojo ap) {
        ap.setId(id);
        AddressPojo addressPojo = customerServiceBean.updateAddress(ap);
        return Response.ok(addressPojo).build();
    }    
    
    @PUT
    @Path(CUSTOMER_ADDRESS_RESOURCE_PATH + "/billing")
    @Transactional
    public Response updateCustomerBillingAddress(@PathParam("id") int id, AddressPojo newAddress) {
        servletContext.log("\n\ntry to update billing address for customer " + id);
        CustomerPojo updatedCustomer = customerServiceBean.updateCustomerBillingAddress(id, newAddress);
        Response response = Response.status(updatedCustomer == null ? NOT_FOUND : OK).entity(updatedCustomer).build();
        return response;
    }
    
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(CUSTOMER_ADDRESS_RESOURCE_PATH + "/{idAddress}")
    @Transactional
    public Response deleteCustomerAddress(@PathParam("id") int custId, @PathParam("idAddress") int addressId) {
        servletContext.log("\n\ntry to delete address for customer " + custId);
        CustomerPojo updatedCustomer = customerServiceBean.deleteCustomerAddressById(custId, addressId);
        Response response = Response.status(updatedCustomer == null ? NOT_FOUND : OK).entity(updatedCustomer).build();
        return response;
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