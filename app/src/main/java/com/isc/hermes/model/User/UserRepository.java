package com.isc.hermes.model.User;

/**
 * The UserRepository class represents a repository for managing user data.
 * @see User
 */
public class UserRepository {

    private static UserRepository userRepository;
    private User userContained;

    /**
     * Returns the instance of the UserRepository.
     *
     * @return The UserRepository instance.
     */
    public static UserRepository getInstance() {
        if (userRepository == null) userRepository = new UserRepository();
        return userRepository;
    }

    /**
     * This method is used to verify if repository has a user.
     *
     * @return a boolean to compro
     */
    public boolean hasUser() {
        return userContained != null;
    }

    /**
     * Retrieves the user contained in the repository.
     *
     * @return The user contained in the repository.
     */
    public User getUserContained() {
        return userContained;
    }

    /**
     * Sets the user to be contained in the repository.
     *
     * @param userContained The user to be contained in the repository.
     */
    public void setUserContained(User userContained) {
        this.userContained = userContained;
    }
}
