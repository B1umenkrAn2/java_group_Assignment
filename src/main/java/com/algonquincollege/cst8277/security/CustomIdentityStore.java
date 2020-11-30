/*****************************************************************c******************o*******v******id********
 * File: CustomIdentityStore.java
 * Course materials (20F) CST 8277
 * @author Mike Norman
 *
 * update by :
 * Lai Shan Law (040595733)
 * Siyang Xiong (040938012)
 * Angela Zhao (040529234)
 * 
 * @date 2020-11-21
 *
 * Note: students do NOT need to change anything in this class
 */
package com.algonquincollege.cst8277.security;

import static java.util.Collections.emptySet;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.security.enterprise.credential.CallerOnlyCredential;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import org.glassfish.soteria.WrappingCallerPrincipal;

import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;

@ApplicationScoped
@Default
public class CustomIdentityStore implements IdentityStore {

    @Inject
    protected CustomIdentityStoreJPAHelper jpaHelper;

    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;

    @Override
    public CredentialValidationResult validate(Credential credential) {

        CredentialValidationResult result = INVALID_RESULT;

        if (credential instanceof UsernamePasswordCredential) {
            String callerName = ((UsernamePasswordCredential)credential).getCaller();
            String credentialPassword = ((UsernamePasswordCredential)credential).getPasswordAsString();
            SecurityUser user = jpaHelper.findUserByName(callerName);
            if (user != null) {
                String pwHash = user.getPwHash();
                /*
                 * pwHash is actually a multifield String with ':' as the field separator:
                 *   <algorithm>:<iterations>:<base64(salt)>:<base64(hash)>
                 *
                 *   Pbkdf2PasswordHash.Algorithm (String identifier)
                 *     "PBKDF2WithHmacSHA224" - too small don't use,
                 *     "PBKDF2WithHmacSHA256" - default,
                 *     "PBKDF2WithHmacSHA384" - meh
                 *     "PBKDF2WithHmacSHA512" - better security - more CPU: maybe not watch/phone, tablet Ok
                 *
                 *  Pbkdf2PasswordHash.Iterations (integer)
                 *     1024 - minimum (too small don't use)
                 *     2048 - default
                 *   I have seen 20,000 up to 50,000 in production
                 *
                 */
                try {
                    boolean verified = pbAndjPasswordHash.verify(credentialPassword.toCharArray(), pwHash);
                    if (verified) {
                        Set<String> rolesForUser = jpaHelper.findRoleNamesForUser(callerName);
                        result = new CredentialValidationResult(new WrappingCallerPrincipal(user), rolesForUser);
                    }
                }
                catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        }
        // check if the credential was CallerOnlyCredential
        else if (credential instanceof CallerOnlyCredential) {
            String callerName = ((CallerOnlyCredential)credential).getCaller();
            SecurityUser user = jpaHelper.findUserByName(callerName);
            if (user != null) {
                result = new CredentialValidationResult(callerName);
            }
        }

        return result;
    }

    protected Set<String> getRolesNamesForSecurityRoles(Set<SecurityRole> roles) {
        Set<String> roleNames = emptySet();
        if (!roles.isEmpty()) {
            roleNames = roles
                .stream()
                .map(s -> s.getRoleName())
                .collect(Collectors.toSet());
        }
        return roleNames;
    }

}