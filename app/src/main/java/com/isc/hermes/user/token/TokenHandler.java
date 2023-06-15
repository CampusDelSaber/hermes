package com.isc.hermes.user.token;

public class TokenHandler {
    private int counter;

    public int makeNew(){
        int tmp = counter;
        counter++;
        return tmp;
    }
}
