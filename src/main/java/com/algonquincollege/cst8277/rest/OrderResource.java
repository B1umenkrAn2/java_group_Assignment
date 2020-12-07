/*****************************************************************c******************o*******v******id********
 * File: OrderResource.java
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

import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.ORDER_RESOURCE_NAME;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.OrderPojo;

@Path(ORDER_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    @EJB
    protected CustomerService customerServiceBean;

    @Inject
    protected ServletContext servletContext;

    @GET
    @PermitAll
    public Response getOrders() {
        servletContext.log("retrieving all orders ...");
        List<OrderPojo> orders = customerServiceBean.getAllOrders();
        Response response = Response.status(orders.isEmpty() ? NOT_FOUND : OK).entity(orders).build();
        return response;
    }

    @GET
    @PermitAll
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific store " + id);
        OrderPojo theOrder = customerServiceBean.getOrderById(id);
        Response response = Response.status(theOrder == null ? NOT_FOUND : OK).entity(theOrder).build();
        return response;
    }
        
    @POST
    @RolesAllowed({ADMIN_ROLE})
    @Transactional
    public Response addOrder(OrderPojo newOrder) {
        servletContext.log("try to add a new order " + newOrder.getId());
        OrderPojo addedOrder = customerServiceBean.addNewOrder(newOrder);
        Response response = Response.status(newOrder == null ? CONFLICT : OK).entity(addedOrder).build();
        return response;
    }
    
    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Transactional
    public Response updateOrder(OrderPojo OrderToUpdate) {
        servletContext.log("try to updata a Order " + OrderToUpdate.getId());
        OrderPojo updatedOrder = customerServiceBean.updateOrder(OrderToUpdate);
        Response response = Response.status(updatedOrder == null ? NOT_FOUND : OK).entity(updatedOrder).build();
        return response;
    }
    
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    @Transactional
    public Response deleteOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete a Order " + id);
        OrderPojo deletedOrder = customerServiceBean.removeOrderById(id);
        Response response = Response.status(deletedOrder == null ? NOT_FOUND : OK).entity(deletedOrder).build();
        return response;
    }
}