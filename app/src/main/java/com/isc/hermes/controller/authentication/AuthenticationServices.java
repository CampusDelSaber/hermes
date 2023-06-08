package com.isc.hermes.controller.authentication;

import com.isc.hermes.R;

/**
 *This enum class contains the types of identification used for registration in the application.
 */
public enum AuthenticationServices {
    GOOGLE(R.id.googleButton);
    private final int id;
    AuthenticationServices(int id){
        this.id = id;
    }

    /**
     * This method returns the id of the authentication service.
     *
     * @return id authentication service
     */
    public int getID(){
        return id;
    }
}
