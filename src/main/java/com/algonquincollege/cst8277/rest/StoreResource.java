/*****************************************************************c******************o*******v******id********
 * File: StoreResource.java
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
import static com.algonquincollege.cst8277.utils.MyConstants.PRODUCT_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.STORE_RESOURCE_NAME;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.StorePojo;

@Path(STORE_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StoreResource {
    @EJB
    protected CustomerService customerServiceBean;

    @Inject
    protected ServletContext servletContext;

    @GET
    @PermitAll
    public Response getStores() {
        servletContext.log("retrieving all stores ...");
        List<StorePojo> stores = customerServiceBean.getAllStores();
        Response response = Response.status(stores.isEmpty() ? NOT_FOUND : OK).entity(stores).build();
        return response;
    }

    @GET
    @PermitAll
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getStoreById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific store " + id);
        StorePojo theStore = customerServiceBean.getStoreById(id);
        Response response = Response.status(theStore == null ? NOT_FOUND : OK).entity(theStore).build();
        return response;
    }
    
    @POST
    @RolesAllowed({ADMIN_ROLE})
    @Transactional
    public Response addStore(StorePojo newStore) {
        servletContext.log("try to add a new Store " + newStore.getId());
        StorePojo addedStore = customerServiceBean.addNewStore(newStore);
        Response response = Response.status(newStore == null ? CONFLICT : OK).entity(addedStore).build();
        return response;
    }
    
    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Transactional
    public Response updateStore(StorePojo StoreToUpdate) {
        servletContext.log("try to updata a Store " + StoreToUpdate.getId());
        StorePojo updatedStore = customerServiceBean.updateStore(StoreToUpdate);
        Response response = Response.status(updatedStore == null ? NOT_FOUND : OK).entity(updatedStore).build();
        return response;
    }
    
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    @Transactional
    public Response deleteStoreById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete a Store " + id);
        StorePojo deletedStore = customerServiceBean.removeStoreById(id);
        Response response = Response.status(deletedStore == null ? NOT_FOUND : OK).entity(deletedStore).build();
        return response;
    }
}