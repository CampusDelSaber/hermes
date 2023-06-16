package com.isc.hermes.model.signup;

import android.content.Context;
import android.content.Intent;

import com.isc.hermes.model.user.UserRoles;

/**
 * This class manages the transitions in the sign up process.
 */
public class SignUpTransitionHandler {

    /**
     * Launch's another activity, based on a role.
     *
     * @param role           UserRole such as Administrator or General
     * @param packageContext the context, so the activity can be launched.
     */
    public void transitionBasedOnRole(UserRoles role, Context packageContext) {
        Intent intent = new Intent(packageContext, RoleTransitionRepository.getInstance().get(role));
        packageContext.startActivity(intent);
    }

}
