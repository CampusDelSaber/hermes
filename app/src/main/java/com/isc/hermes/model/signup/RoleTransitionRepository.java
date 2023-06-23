package com.isc.hermes.model.signup;

import com.isc.hermes.EmailVerificationActivity;
import com.isc.hermes.MainActivity;
import java.util.HashMap;
import java.util.Map;

/**
 * The RoleTransitionRepository class holds data about which transition should be done
 * based in a role.
 * <p>
 * It is a Singleton class.
 */
public class RoleTransitionRepository {
    private final Map<String, Class<?>> rolesClassMap;
    private static RoleTransitionRepository self;

    /**
     * Constructs a RoleTransitionRepository
     */
    private RoleTransitionRepository(){
        rolesClassMap = new HashMap<>();

        rolesClassMap.put("Administrator", EmailVerificationActivity.class);
        rolesClassMap.put("General", MainActivity.class);
    }

    /**
     * To follow the singleton pattern, this method retrieves an unique instance
     * of the RoleTransitionRepository class.
     *
     * @return static RoleTransitionRepository instance.
     */
    public static RoleTransitionRepository getInstance(){
        if (self == null){
            self = new RoleTransitionRepository();
        } return self;
    }

    /**
     * This method retrieves the appropriate transition based in a given role.
     *
     * @param role The user role to search for.
     * @return A class to make the transition to.
     */
    public Class<?> get(String role){
        return rolesClassMap.get(role);
    }
}
