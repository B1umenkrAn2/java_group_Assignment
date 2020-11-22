/*****************************************************************c******************o*******v******id********
 * File: SecurityRoleSerializer.java
 * Course materials (20F) CST 8277
 * @author Mike Norman
 *
 * Note: students do NOT need to change anything in this class
 */
package com.algonquincollege.cst8277.rest;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.algonquincollege.cst8277.models.SecurityRole;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class SecurityRoleSerializer extends StdSerializer<Set<SecurityRole>> implements Serializable {
    private static final long serialVersionUID = 1L;

    public SecurityRoleSerializer() {
        this(null);
    }

    public SecurityRoleSerializer(Class<Set<SecurityRole>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<SecurityRole> originalRoles, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        
        Set<SecurityRole> hollowRoles = new HashSet<>();
        for (SecurityRole originalRole : originalRoles) {
            // create a 'hollow' copy of the original Security Roles entity
            SecurityRole hollowP = new SecurityRole();
            hollowP.setId(originalRole.getId());
            hollowP.setRoleName(originalRole.getRoleName());
            hollowP.setUsers(null);
            hollowRoles.add(hollowP);
        }
        generator.writeObject(hollowRoles);
    }
}