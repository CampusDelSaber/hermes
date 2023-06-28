package com.isc.hermes.model.user;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserRolesTest {

    @Test
    public void rolesAreExported(){
        String[] roles = UserRoles.export();

        assertEquals(roles[0], "Administrator");
        assertEquals(roles[1], "General");
    }
}