/*****************************************************************c******************o*******v******id********
 * File: ProductResource.java
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


import java.util.List;

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

import com.algonquincollege.cst8277.ejb.CustomerService;
import com.algonquincollege.cst8277.models.ProductPojo;

import static com.algonquincollege.cst8277.utils.MyConstants.*;

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
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Path(SLASH)
    public Response getProducts() {
        servletContext.log("retrieving all products ...");
        List<ProductPojo> custs = customerServiceBean.getAllProducts();
        return Response.ok(custs).build();
    }

    @GET
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getProductById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific product " + id);
        ProductPojo theProduct = customerServiceBean.getProductById(id);
        return Response.ok(theProduct).build();
    }
    @POST
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Path(SLASH)
    @Transactional
    public Response addNewProduct(ProductPojo productPojo){
        servletContext.log("try to add new product ");
        ProductPojo pojo = customerServiceBean.addPrdocut(productPojo);
        return Response.ok(pojo).build();
    }


    @PUT
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Transactional
    public Response updateProductById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id,ProductPojo productPojo) {
        servletContext.log("try to update specific product " + id);
        productPojo.setId(id);
        ProductPojo updateProduct = customerServiceBean.updateProduct(productPojo);
        return Response.ok(updateProduct).build();

    }

    @DELETE
    @Path(RESOURCE_PATH_ID_PATH)
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Transactional
    public Response deleteProductById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to delete specific product " + id);
        ProductPojo updateProduct = customerServiceBean.removeProductById(id);
        return Response.ok(updateProduct).build();
    }
}