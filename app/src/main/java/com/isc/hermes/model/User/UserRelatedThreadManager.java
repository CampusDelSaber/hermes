package com.isc.hermes.model.User;

/**
 * Singleton class that manages execution of user-related threads.
 */
public class UserRelatedThreadManager {
    private static UserRelatedThreadManager instance;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private UserRelatedThreadManager() {}

    /**
     * Returns the singleton instance of UserRelatedThreadManager.
     *
     * @return the singleton instance
     */
    public static UserRelatedThreadManager getInstance() {
        if (instance == null)
            instance = new UserRelatedThreadManager();
        return instance;
    }

    /**
     * Executes the provided thread in a new background thread.
     *
     * @param thread the Runnable thread to execute
     */
    public void doActionForThread(Runnable thread) {
        Thread threadToRun = new Thread(thread);
        threadToRun.start();
    }
}
