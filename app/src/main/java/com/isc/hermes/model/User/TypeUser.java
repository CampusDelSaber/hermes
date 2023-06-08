package com.isc.hermes.model.User;

public enum TypeUser {
    PRIVILEGED_USER("Administrator"),
    UNPRIVILEGED_USER("General");

    private final String type;

    private TypeUser(String type) {
        this.type = type;
    }
}
