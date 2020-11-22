/*****************************************************************c******************o*******v******id********
 * File: MyConstants.java
 * Course materials (20F) CST 8277
 *
 * @author (original) Mike Norman
 * 
 * update by : I. Am. A. Student 040nnnnnnn
 *
 */
package com.algonquincollege.cst8277.utils;

/**
 * <p>
 * This class holds various constants used by this App's artifacts
 * <p>
 * The key idea here is that often an annotation contains String-based parameters that <b><u>must be an exact match</u></b> <br/>
 * to a string used elsewhere. Use of this type of 'Contants' Interface class prevents errors such as:
<blockquote><pre>
{@literal @}GET
{@literal @}Path("{<b><u>emailID</u></b>}/project")  //accidently capitalized <b><u>ID</u></b>, instead of camel-case <b><u>Id</u></b>
public List<Project> getProjects({@literal @}PathParam("<b><u>emailId</u></b>") String emailId) ...  // path parameter does not match Annotation
</pre></blockquote>
 *
 * @author mwnorman
 */
public interface MyConstants {

    // constants on Interfaces are 'public static final' by default,
    // but I leave 'em in case I move a constant to a Class

    //REST constants
    public static final String APPLICATION_API_VERSION = "/api/v1";
    public static final String SLASH = "/";
    public static final String REST_APPLICATION_PATH = SLASH + "api" + SLASH + "v1";
    public static final String APPLICATION_CONTEXT_ROOT = SLASH + "rest-employeeSystem";
    public static final String RESOURCE_PATH_ID_ELEMENT =  "id";
    public static final String RESOURCE_PATH_ID_PATH =  "/{" + RESOURCE_PATH_ID_ELEMENT + "}";
    public static final String CREDENTIAL_RESOURCE_NAME = "credential";
    public static final String CUSTOMER_RESOURCE_NAME =  "customer";
    public static final String ORDER_RESOURCE_NAME =  "order";
    public static final String PRODUCT_RESOURCE_NAME =  "product";
    public static final String STORE_RESOURCE_NAME =  "store";
    public static final String CUSTOMER_ADDRESS_SUBRESOURCE_NAME =  "address";
    public static final String CUSTOMER_ADDRESS_RESOURCE_PATH =
        RESOURCE_PATH_ID_PATH + SLASH + CUSTOMER_ADDRESS_SUBRESOURCE_NAME;

    //Security constants
    public static final String USER_ROLE = "USER_ROLE";
    public static final String ADMIN_ROLE = "ADMIN_ROLE";
    public static final String ACCESS_REQUIRES_AUTHENTICATION =
        "Access requires Authentication";
    public static final String ACCESS_TO_THE_SPECIFIED_RESOURCE_HAS_BEEN_FORBIDDEN =
        "Access to the specified resource has been forbidden";
    //Eclipse MicroProfile Config - externalise configuration: default in META-INF/microprofile-config.properties
    public static final String DEFAULT_ADMIN_USER_PROPNAME = "default-admin-user";
    public static final String DEFAULT_ADMIN_USER = "admin";
    public static final String DEFAULT_ADMIN_USER_PASSWORD_PROPNAME = "default-admin-user-password";
    public static final String DEFAULT_ADMIN_USER_PASSWORD = "admin";
    public static final String DEFAULT_USER_PREFIX = "user";
    public static final String DEFAULT_USER_PASSWORD = "password";

    // the nickname of this Hash algorithm is 'PBandJ' (Peanut-Butter-And-Jam, like the sandwich!)
    // I would like to use the constants from org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl
    // but they are not visible, so type in them all over again :-( Hope there are no typos!
    public static final String PROPERTY_ALGORITHM  = "Pbkdf2PasswordHash.Algorithm";
    public static final String DEFAULT_PROPERTY_ALGORITHM  = "PBKDF2WithHmacSHA256";
    public static final String PROPERTY_ITERATIONS = "Pbkdf2PasswordHash.Iterations";
    public static final String DEFAULT_PROPERTY_ITERATIONS = "2048";
    public static final String PROPERTY_SALTSIZE   = "Pbkdf2PasswordHash.SaltSizeBytes";
    public static final String DEFAULT_SALT_SIZE   = "32";
    public static final String PROPERTY_KEYSIZE    = "Pbkdf2PasswordHash.KeySizeBytes";
    public static final String DEFAULT_KEY_SIZE    = "32";

    //JPA constants
    public static final String PU_NAME = "assignment4-PU";
    public static final String PARAM1 = "param1";

}