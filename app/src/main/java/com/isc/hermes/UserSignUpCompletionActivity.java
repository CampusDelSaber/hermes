package com.isc.hermes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.isc.hermes.model.User;

public class UserSignUpCompletionActivity extends AppCompatActivity {

    private TextView textNameComplete;
    private AutoCompleteTextView textFieldUserName;
    private AutoCompleteTextView textFieldEmail;
    private AutoCompleteTextView textFieldUserType;
    private Button bttnRegister;
    private ImageView imgUser;
    private User userRegistered;
    private AutoCompleteTextView generateComponentsToComboBox() {
        String[] items = {"Administrator", "General"};
        AutoCompleteTextView autoCompleteText = findViewById(R.id.textFieldUserType);
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.combo_box_item, items);
        autoCompleteText.setAdapter(adapterItems);
        return autoCompleteText;
    }
    private void generateActionToComboBox() {
        generateComponentsToComboBox().setOnItemClickListener((parent, view, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            userRegistered.setTypeUser(item);
            Toast.makeText(getApplicationContext(), "Item: " + item,
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void generateActionToButtonSignUp() {
        bttnRegister.setOnClickListener(v -> {
            //save user to database
            System.out.println(userRegistered);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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
        textFieldEmail.setHorizontallyScrolling(true);
        bttnRegister = findViewById(R.id.bttnRegister);
        imgUser = findViewById(R.id.imgUser);
    }

    private void chargeInformationAboutUserInTextFields(){
        textNameComplete.setText(userRegistered.getFullName());
        textFieldUserName.setText(userRegistered.getUserName());
        textFieldEmail.setText(userRegistered.getEmail());
    }

    private void getUserInformation() {
        Intent intent = getIntent();
        userRegistered = intent.getParcelableExtra("userObtained");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up_completion_view);
        getUserInformation();
        generateActionToComboBox();
        assignValuesToComponentsView();
        generateActionToButtonSignUp();
        chargeImage();
        chargeInformationAboutUserInTextFields();
    }
}