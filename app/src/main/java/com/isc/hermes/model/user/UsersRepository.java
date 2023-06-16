package com.isc.hermes.model.user;

import com.isc.hermes.model.token.TokenGenerator;
import java.util.HashMap;

/**
 * The Repository class manages the user instances, it follows the Singleton pattern
 */
public class UsersRepository {

    private static UsersRepository self;
    private final HashMap<Integer, User> users;
    private final TokenGenerator handler;

    private UsersRepository(){
        users = new HashMap<>();
        handler = new TokenGenerator();
    }

    /**
     * To follow the singleton pattern, this method retrieves an unique instance
     * of the UserRepository class.
     *
     * @return static UsersRepository instance.
     */
    public static UsersRepository getInstance() {
        if (self == null){
            self = new UsersRepository();
        }
        return self;
    }

    /**
     * Saves the payload as an instance of the User object into the HashMap.
     *
     * @param payload the data to be saved.
     * @return a integer token key to the payload.
     */
    public int put(Payload payload){
        int token = handler.generate();
        users.put(token, new User(payload));
        return token;
    }

    /**
     * Retrieves a user from the HashMap by using the given token as a key.
     *
     * @param token The key to look for the user.
     * @return the respective user to the key-pair
     */
    public User get(int token){
        User user = users.get(token);
        if (user == null) {
            throw new NullPointerException(String.format("The user with the token %d could not be found", token));
        }
        return user;
    }
}
