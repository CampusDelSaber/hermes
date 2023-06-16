package com.isc.hermes.model.token;

/**
 * The class TokenGenerator generates tokens.
 */
public class TokenGenerator {
    private int counter;

    /**
     * Makes a new token.
     *
     * @return an representation of a token in the form of an int.
     */
    public int generate(){
        int tmp = counter;
        counter++;
        return tmp;
    }
}
