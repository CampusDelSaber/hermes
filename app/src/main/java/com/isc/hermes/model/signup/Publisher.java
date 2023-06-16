package com.isc.hermes.model.signup;

import com.isc.hermes.model.user.User;
import com.isc.hermes.model.user.UserRoles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Publisher {
    private HashMap<UserRoles, List<Suscribable>> subscriber;
    private static Publisher getInstance;

    public void subscribe(UserRoles role, Suscribable suscribable){
        List<Suscribable> subscribersList = subscriber.get(role);
        if (subscribersList == null){
            subscribersList = new ArrayList<>();
            subscriber.put(role, subscribersList);
        }

        subscribersList.add(suscribable);
    }

    public void notifySubscribers(User user, UserRoles userRoles){
        List<Suscribable> subscribersList = subscriber.get(userRoles);
        for (int i = 0; i < subscribersList.size(); i++) {
            subscribersList.get(i).notify(user);
        }
    }

    public static Publisher getInstance(){
        if (getInstance == null){
            getInstance = new Publisher();
        }

        return getInstance;
    }
}
