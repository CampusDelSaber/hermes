package com.isc.hermes.model.signup;

import com.isc.hermes.EmailVerificationActivity;
import com.isc.hermes.MainActivity;
import com.isc.hermes.model.user.UserRoles;
import java.util.HashMap;
import java.util.Map;

/**
 * The RoleTransitionRepository class holds data about which transition should be done
 * based in a role.
 * <p>
 * It is a Singleton class.
 */
public class RoleTransitionRepository {
    private final Map<UserRoles, Class<?>> rolesClassMap;
    private static RoleTransitionRepository self;

    /**
     * Constructs a RoleTransitionRepository
     */
    private RoleTransitionRepository(){
        rolesClassMap = new HashMap<>();

        rolesClassMap.put(UserRoles.ADMINISTRATOR, EmailVerificationActivity.class);
        rolesClassMap.put(UserRoles.GENERAL, MainActivity.class);
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
        }

        return self;
    }

    /**
     * This method retrieves the appropriate transition based in a given role.
     *
     * @param role The user role to search for.
     * @return A class to make the transition to.
     */
    public Class<?> get(UserRoles role){
        return rolesClassMap.get(role);
    }
}
