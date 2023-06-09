package com.isc.hermes.database.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UsersCollectionHandlerTest {

    private UserData makeUser(){
        return new UserData(
                "user",
                "false_email",
                "2121",
                "Administrator",
                "path/to/image",
                "test user");
    }

    private UserData makeUser(String userId){
        return new UserData(
                "user",
                "false_email",
                userId,
                "Administrator",
                "path/to/image",
                "test user");
    }

    @Test
    void saveUser(){
        UsersCollectionHandler handler = new UsersCollectionHandler();
        handler.save(makeUser());
        assertTrue(true);
    }

    @Test
    void userRetrieved(){
        String userId = "2121";
        UsersCollectionHandler handler = new UsersCollectionHandler();
        handler.save(makeUser(userId));
        UserData userData = handler.get(userId);

        assertEquals(userId, userData.getUserID());
    }

    @Test
    void userNotRetrieved(){
        String userId = "2121";
        UsersCollectionHandler handler = new UsersCollectionHandler();
        UserData userData = handler.get(userId);

        assertNull(userData);
    }
}