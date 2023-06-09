package com.isc.hermes.database.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UsersCollectionHandlerTest {

    private UserData makeUser(){
        return new UserData("user",
                "email",
                2000,
                true,
                "");
    }

    private UserData makeUser(int userId){
        return new UserData("user",
                "email",
                userId,
                false,
                "");
    }

    @Test
    void saveUser(){
        UsersCollectionHandler handler = new UsersCollectionHandler();
        handler.save(makeUser());
        assertTrue(true);
    }

    @Test
    void userRetrieved(){
        int userId = 2121;
        UsersCollectionHandler handler = new UsersCollectionHandler();
        handler.save(makeUser(userId));
        UserData userData = handler.get(userId);

        assertEquals(userId, userData.getUserID());
    }

    @Test
    void userNotRetrieved(){
        int userId = 2121;
        UsersCollectionHandler handler = new UsersCollectionHandler();
        UserData userData = handler.get(userId);

        assertNull(userData);
    }
}