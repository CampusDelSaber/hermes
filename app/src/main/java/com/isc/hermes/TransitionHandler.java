package com.isc.hermes;

import com.isc.hermes.model.user.UserRoles;

import java.util.HashMap;
import java.util.Map;

public class TransitionHandler {
    private final Map<UserRoles, Class<?>> rolesClassMap;
    private static TransitionHandler self;

    private TransitionHandler(){
        rolesClassMap = new HashMap<>();

        rolesClassMap.put(UserRoles.ADMINISTRATOR, MainActivity.class);
        rolesClassMap.put(UserRoles.GENERAL, MainActivity.class);
    }

    public static TransitionHandler getInstance(){
        if (self == null){
            self = new TransitionHandler();
        }

        return self;
    }

    public Class<?> cash(UserRoles role){
        return rolesClassMap.get(role);
    }
}
