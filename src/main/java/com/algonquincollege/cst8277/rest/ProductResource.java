/*****************************************************************c******************o*******v******id********
 * File: ProductResource.java
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
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.ProductPojo;

import static com.algonquincollege.cst8277.utils.MyConstants.*;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.CONFLICT;

@Path(PRODUCT_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @EJB
    protected CustomerService customerServiceBean;

    @Inject
    protected ServletContext servletContext;

    @Inject
    protected SecurityContext sc;

    @GET
    @PermitAll
    public Response getProducts() {
        servletContext.log("retrieving all products ...");
        List<ProductPojo> products = customerServiceBean.getAllProducts();
        servletContext.log("\n\nGot total [" + products.size() + "] products\n\n");
//        Response response = Response.ok(products).build();
        Response response = Response.status(products == null ? NOT_FOUND : OK).entity(products).build();
        return response;
    }

    @GET
    @PermitAll
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getProductById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific product " + id);
        ProductPojo theProduct = customerServiceBean.getProductById(id);
        Response response = Response.status(theProduct == null ? NOT_FOUND : OK).entity(theProduct).build();
        return response;
    }
        
    @GET
    @PermitAll
    @Path("/parameters")
    public Response getProductByDescription(@QueryParam("description") String description) {
        servletContext.log("try to retrieve specific product by description " + description);
        List<ProductPojo> products = customerServiceBean.getProductByDescription(description);
        Response response = Response.status(products.isEmpty() ? NOT_FOUND : OK).entity(products).build();
        return response;
    }
    
    
    @POST
    @RolesAllowed({ADMIN_ROLE})
    @Transactional
    public Response addProduct(ProductPojo newProduct) {
        servletContext.log("try to add a new product " + newProduct.getId());
        ProductPojo addedProduct = customerServiceBean.addNewProduct(newProduct);
        Response response = Response.status(newProduct == null ? CONFLICT : OK).entity(addedProduct).build();
        return response;
    }
    
    @PUT
    @RolesAllowed({ADMIN_ROLE})
    @Transactional
    public Response updateProduct(ProductPojo productToUpdate) {
        servletContext.log("try to update a product " + productToUpdate.getId());
        ProductPojo updatedProduct = customerServiceBean.updateProduct(productToUpdate);
        Response response = Response.status(updatedProduct == null ? NOT_FOUND : OK).entity(updatedProduct).build();
        return response;
    }
    
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    @Transactional
    public Response deleteProductById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int prodId) {
        servletContext.log("try to delete a product " + prodId);
        ProductPojo deletedProduct = customerServiceBean.removeProductById(prodId);
        Response response = Response.status(deletedProduct == null ? NOT_FOUND : OK).entity(deletedProduct).build();
        return response;
    }
}