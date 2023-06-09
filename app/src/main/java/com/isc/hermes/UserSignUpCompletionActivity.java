package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.isc.hermes.model.User;

public class UserSignUpCompletionActivity extends AppCompatActivity {

    private TextView textNameComplete;
    private AutoCompleteTextView textFieldUserName;
    private AutoCompleteTextView textFieldEmail;
    private ImageView imgUser;
    private User userRegistered;
    private AutoCompleteTextView generateComponentsToComboBox() {
        String[] items = {"Administrator", "General"};
        AutoCompleteTextView autoCompleteText = findViewById(R.id.autoCompleteText);
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.combo_box_item, items);
        autoCompleteText.setAdapter(adapterItems);
        return autoCompleteText;
    }
    private void generateActionToComboBox() {
        generateComponentsToComboBox().setOnItemClickListener((parent, view, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), "Item: " + item,
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void chargeImage(){
        if (userRegistered.getPathImageUser() != null)
            Glide.with(this).load(Uri.parse(
                    userRegistered.getPathImageUser())).into(imgUser);
    }

    private void assignValuesToComponentsView() {
        textNameComplete = findViewById(R.id.textNameComplete);
        textFieldUserName = findViewById(R.id.textFieldUserName);
        textFieldEmail = findViewById(R.id.textFieldEmail);
        imgUser = findViewById(R.id.imgUser);
        textFieldEmail.setHorizontallyScrolling(true);
        textFieldUserName.setHorizontallyScrolling(true);
    }

    private void chargeInformationAboutUserInTextFields(){
        textNameComplete.setText(userRegistered.getFullName());
        textFieldUserName.setText(userRegistered.getUserName());
        textFieldEmail.setText(userRegistered.getEmail());
    }

    private void getUserInformation() {
        Intent intent = getIntent();
        userRegistered = intent.getParcelableExtra("userObtained");
        generateActionToComboBox();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up_completion_view);
        getUserInformation();
        assignValuesToComponentsView();
        chargeImage();
        chargeInformationAboutUserInTextFields();
    }
}