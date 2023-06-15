package com.isc.hermes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.model.user.UserRoles;

public class SignUpTransition extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void transitionBasedOnRole(UserRoles role, Context packageContext) {
        Intent intent = new Intent(packageContext, TransitionHandler.getInstance().cash(role));
        startActivity(intent);
    }
}
