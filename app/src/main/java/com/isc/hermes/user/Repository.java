package com.isc.hermes.user;

import com.isc.hermes.user.token.TokenHandler;
import java.util.HashMap;

public class Repository {

    private static Repository self;
    private final HashMap<Integer, User> users;
    private final TokenHandler handler;

    private Repository(){
        users = new HashMap<>();
        handler = new TokenHandler();
    }

    public static Repository getInstance() {
        if (self == null){
            self = new Repository();
        }
        return self;
    }

    public int put(Payload payload){
        int token = handler.makeNew();
        users.put(token, new User(payload));
        return token;
    }

    public User get(int token){
        User user = users.get(token);
        if (user == null) {
            throw new NullPointerException(String.format("The user with the token %d could not be found", token));
        }
        return user;
    }
}
