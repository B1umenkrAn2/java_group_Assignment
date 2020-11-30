/*****************************************************************c******************o*******v******id********
 * File: CustomerResource.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 *
 * update by : I. Am. A. Student 040nnnnnnn
 *
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.List;

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

    @GET
    @Path("")
    public Response getCustomers() {
        servletContext.log("retrieving all customers ...");
        List<CustomerPojo> custs = customerServiceBean.getAllCustomers();
        return Response.ok(custs).build();
    }


    @GET
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


    @POST
    @Path("")
    @Transactional
    public Response addCustomer(CustomerPojo newCustomer) {
        servletContext.log("try to add a new customer ");
        CustomerPojo newCustomerWithIdTimestamps = customerServiceBean.persistCustomer(newCustomer);
        //build a SecurityUser linked to the new customer
        customerServiceBean.buildUserForNewCustomer(newCustomerWithIdTimestamps);
        return Response.ok(newCustomerWithIdTimestamps).build();

    }

    @DELETE
    @Path("{id:}")
    @Transactional
    public Response deleteCustomerById(@PathParam("id") int id) {
        servletContext.log("try to delete a specific customer" );
        CustomerPojo customerPojo = customerServiceBean.removeCustomerById(id);
        return Response.ok(customerPojo).build();
    }

    @PUT
    @Path("{id:}")
    @Transactional
    public Response updateCustomerByid(@PathParam("id") int id, CustomerPojo customerPojo) {
        servletContext.log("try to update a specific customer" );
        customerPojo.setId(id);
        CustomerPojo updateCustomer = customerServiceBean.updateCustomer(customerPojo);
        return Response.ok(updateCustomer).build();
    }

    @GET
    @Path("{id:}/address")
    public Response getCustomerAllAddress(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve a specific customer all address" );
        List<AddressPojo> customerAlladdressByCustId = customerServiceBean.getCustomerAlladdressByCustId(id);
        return Response.ok(customerAlladdressByCustId).build();
    }

    @GET
    @Path("{id:}/address/{id2:}")
    public Response getCustomerAllAddress(@PathParam("id") int id, @PathParam("id2") int id2) {
        servletContext.log("try to retrieve a specific customer's a specific address" );
        AddressPojo customerAlladdressByCustId = customerServiceBean.getCustomerAddressByAddressId(id, id2);
        return Response.ok(customerAlladdressByCustId).build();
    }

    @POST
    @Path(RESOURCE_PATH_ID_PATH + SLASH + CUSTOMER_ADDRESS_SUBRESOURCE_NAME)
    @Transactional
    public Response addAddressForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, AddressPojo newAddress) {
        servletContext.log("try to add a address for a specific customer " );
        CustomerPojo updatedCustomer = customerServiceBean.setAddressFor(id, newAddress);
        return Response.ok(updatedCustomer).build();
    }


    @DELETE
    @Path("/address/{id:}")
    @Transactional
    public Response deleteCustomerAddressByid(@PathParam("id") int id) {
        servletContext.log("try to delete a specific address " );
        AddressPojo addressPojo = customerServiceBean.removeAddressByid(id);
        return Response.ok(addressPojo).build();
    }

    @PUT
    @Path("/address/{id:}")
    @Transactional
    public Response updateAddressByid(@PathParam("id") int id, AddressPojo ap) {
        servletContext.log("try to update a specific address " );
        ap.setId(id);
        AddressPojo addressPojo = customerServiceBean.updateAddress(ap);
        return Response.ok(addressPojo).build();
    }

    @GET
    @Path("{id:}/orders")
    public Response getCustomerAllOrder(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific customer all orders " );
        List<OrderPojo> allOrders = customerServiceBean.getCustomerALLOrders(id);
        return Response.ok(allOrders).build();
    }

    @GET
    @Path("/order/{id:}")
    public Response getCustomerOneOrder(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve a specific customer's a specific order" +id );
        OrderPojo orderById = customerServiceBean.getOrderById(id);
        return Response.ok(orderById).build();
    }

    @POST
    @Path("{id:}/order")
    public Response addOrderToCustomerById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, OrderPojo orderPojo) {
        servletContext.log("try to add new OrderLine to a specific order" + id);
        CustomerPojo customerPojo = customerServiceBean.addOrderForCustomer(id, orderPojo);
        return Response.ok(customerPojo).build();
    }

    @PUT
    @Path("/order/{id:}")
    public Response updateOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, OrderPojo orderPojo) {
        servletContext.log("try to update specific OrderLine" + id);
        orderPojo.setId(id);
        OrderPojo updateOrder = customerServiceBean.updateOrder(orderPojo);
        return Response.ok(updateOrder).build();
    }

    @DELETE
    @Path("/order/{id:}")
    public Response deleteOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete specific Order" + id);
        OrderPojo removeOrder = customerServiceBean.removeOrderById(id);
        return Response.ok(removeOrder).build();
    }

    // orderline

    @GET
    @Path("/order/{id:}/orderline")
    public Response getOneOrderAllOrderLine(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve all store " );
        List<OrderLinePojo> oneOrderALLOrderLineById = customerServiceBean.getOneOrderALLOrderLineById(id);
        return Response.ok(oneOrderALLOrderLineById).build();
    }

    @GET
    @Path("/order/orderline/{id:}")
    public Response getOneOrderLine(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific orderLine " + id);
        OrderLinePojo orderLine = customerServiceBean.getOrderLineByOrderLineNo(id);
        return Response.ok(orderLine).build();
    }

    @POST
    @Path("order/{id:}/orderline")
    public Response addOrderLineToOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, OrderLinePojo olp) {
        servletContext.log("try to add new OrderLine to a  specific orderLine" + id);
        OrderPojo orderPojo = customerServiceBean.addOrderLineToOrder(id, olp);
        return Response.ok(orderPojo).build();
    }

    @PUT
    @Path("/order/orderline/{id:}")
    public Response updateOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, OrderLinePojo olp) {
        servletContext.log("try to update specific OrderLine" + id);
        OrderLinePk orderLinePk = new OrderLinePk();
        orderLinePk.setOrderLineNo(id);
        olp.setPk(orderLinePk);
        OrderLinePojo orderLinePojo = customerServiceBean.updateOrderLine(olp);
        return Response.ok(orderLinePojo).build();
    }

    @DELETE
    @Path("/order/orderline/{id:}")
    public Response deleteOrderLineById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete specific OrderLine" + id);
        OrderLinePojo orderLinePojo = customerServiceBean.removeOrderLineByNo(id);
        return Response.ok(orderLinePojo).build();
    }


}