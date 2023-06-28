package com.isc.hermes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.isc.hermes.model.signup.SignUpTransitionHandler;
import com.isc.hermes.model.User.UserRepository;

/**
 * This class is used  for completing the user sign-up process.
 * This activity allows the user to enter additional information to complete their registration.
 * It displays the user's name, provides fields for entering a username and email,
 * allows the user to select a user type from a combo box, and displays the user's profile image if available.
 * The completed information can be saved to the database by clicking the "Register" button.
 */
public class UserSignUpCompletionActivity extends AppCompatActivity {

    private TextView textNameComplete;
    private AutoCompleteTextView textFieldUserName;
    private AutoCompleteTextView textFieldEmail;
    private TextInputLayout comboBoxTextField;
    private Button buttonRegister;
    private ImageView imgUser;

    /**
     * Assigns values to the components view.
     * Finds and initializes the TextView, AutoCompleteTextView, Button, and ImageView components from the layout.
     */
    private void assignValuesToComponentsView() {
        textNameComplete = findViewById(R.id.textNameComplete);
        textFieldUserName = findViewById(R.id.textFieldUserName);
        textFieldEmail = findViewById(R.id.textFieldEmail);
        textFieldEmail.setHorizontallyScrolling(true);
        buttonRegister = findViewById(R.id.buttonRegister);
        imgUser = findViewById(R.id.imgUser);
        comboBoxTextField = findViewById(R.id.comboBoxTextField);
    }

    /**
     * Charges information about the user in the text fields.
     * Sets the name, username, and email of the user to the respective TextView and AutoCompleteTextView components.
     */
    private void loadInformationAboutUserInTextFields(){
        textNameComplete.setText(UserRepository.getInstance().getUserContained().getFullName());
        textFieldUserName.setText(UserRepository.getInstance().getUserContained().getUserName());
        textFieldEmail.setText(UserRepository.getInstance().getUserContained().getEmail());
    }

    /**
     * Generates components for the combo box and returns the AutoCompleteTextView.
     * Creates an array of items for the combo box, sets up an ArrayAdapter,
     * and attaches it to the AutoCompleteTextView component to provide suggestions.
     *
     * @return The generated AutoCompleteTextView for the combo box.
     */
    private AutoCompleteTextView generateComponentsToComboBox() {
        String[] items = {"Administrator", "General"};
        AutoCompleteTextView autoCompleteText = findViewById(R.id.textFieldUserType);
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this, R.layout.combo_box_item, items);
        autoCompleteText.setAdapter(adapterItems);
        return autoCompleteText;
    }

    /**
     * Generates an action for the combo box.
     * Sets an item click listener on the AutoCompleteTextView component of the combo box,
     * which captures the selected item and updates the user's type accordingly.
     */
    private void generateActionToComboBox() {
        generateComponentsToComboBox().setOnItemClickListener((parent, view, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            UserRepository.getInstance().getUserContained().setTypeUser(item);
            Toast.makeText(getApplicationContext(), "Item: " + item,
                    Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Generates an action for the sign-up button.
     * Sets a click listener on the "Register" button, which saves the user's information to the database
     * and navigates the user to the main activity.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void generateActionToButtonSignUp() {
        buttonRegister.setOnClickListener(v -> {
            if (UserRepository.getInstance().getUserContained().getTypeUser() != null) {
                UserRepository.getInstance().getUserContained().setRegistered(false);
                new  SignUpTransitionHandler().transitionBasedOnRole(this);
            } else comboBoxTextField.setHelperText("Required");
        });
    }

    /**
     * Charges the user's profile image into the ImageView if available.
     * If the user has a path to their profile image, it uses Glide to load the image into the ImageView.
     */
    private void loadUserImageInView(){
        if (UserRepository.getInstance().getUserContained().getPathImageUser() != null)
            Glide.with(this).load(Uri.parse(
                    UserRepository.getInstance().getUserContained().getPathImageUser())).into(imgUser);
    }

    /**
     * Method for create the view and its components using an user information to do that.
     *
     * @param savedInstanceState the saved state of the instance
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up_completion_view);
        generateActionToComboBox();
        assignValuesToComponentsView();
        generateActionToButtonSignUp();
        loadUserImageInView();
        loadInformationAboutUserInTextFields();
    }
}