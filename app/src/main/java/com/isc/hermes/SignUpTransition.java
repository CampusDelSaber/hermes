package com.isc.hermes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.isc.hermes.model.user.UserRoles;

public class SignUpTransition extends AppCompatActivity {
    private TransitionHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = TransitionHandler.getInstance();
    }

    private void changeActivity(Intent intent) {
        startActivity(intent);
    }

    public void transitionBasedOnRole(UserRoles role, Context packageContext) {
        Intent intent = new Intent(packageContext, handler.cash(role));
        changeActivity(intent);
    }
}
