package com.isc.hermes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.database.VerificationCodesManager;
import com.isc.hermes.model.Validator;
import com.isc.hermes.model.User.UserRepository;

/**
 * This class manages the email verification when the user declares themself as a Administrator.
 */
public class EmailVerificationActivity extends AppCompatActivity {

    private EditText[] codeEditTexts;
    private Validator validator;

    /**
     * This method initiates the window whe its called.
     * @param savedInstanceState The saved instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_verification);
        validator = new Validator(UserRepository.getInstance().getUserContained());
        initComponents();
    }

    /**
     * Initializes the components and UI elements of the activity.
     */
    private void initComponents() {
        initCodeEditTexts();
        initContinueButton();
        configureEditTexts();
    }

    /**
     * Initializes the code EditText fields.
     */
    private void initCodeEditTexts() {
        codeEditTexts = new EditText[]{findViewById(R.id.codeTextField1), findViewById(R.id.codeTextField2),
                findViewById(R.id.codeTextField3), findViewById(R.id.codeTextField4),
                findViewById(R.id.codeTextField5), findViewById(R.id.codeTextField6)
        };
    }

    /**
     * Initializes the Continue button and its click listener.
     */
    private void initContinueButton() {
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(EmailVerificationActivity.this, MainActivity.class);
            String code = getCodeUser();
            if (validator.isCorrect(code)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    new AccountInfoManager().addUser(UserRepository.getInstance().getUserContained().getEmail(),
                            UserRepository.getInstance().getUserContained().getFullName(),
                            UserRepository.getInstance().getUserContained().getUserName(),
                            UserRepository.getInstance().getUserContained().getTypeUser(),
                            UserRepository.getInstance().getUserContained().getPathImageUser());
                VerificationCodesManager verificationCodesManager = new VerificationCodesManager();
                verificationCodesManager.updateVerificationCode(validator.getId(), false);
                startActivity(intent);
            } else changeColorCodeUser();
        });
    }

    /**
     * This method separates every codeTextField and calls configureTextWatcher method.
     */
    private void configureEditTexts() {
        for (int i = 0; i < codeEditTexts.length; i++) {
            final EditText currentEditText = codeEditTexts[i];
            final EditText nextEditText = (i < codeEditTexts.length - 1) ? codeEditTexts[i + 1] : null;
            configureTextWatcher(currentEditText, nextEditText);
        }
    }

    /**
     * Configures the TextWatcher for a specific EditText field.
     *
     * @param currentEditText The current EditText field.
     * @param nextEditText    The next EditText field.
     */
    private void configureTextWatcher(final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleTextChanged(s, currentEditText, nextEditText);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void handleTextChanged(CharSequence s, EditText currentEditText, EditText nextEditText) {
        if (s.length() == 2 && nextEditText != null) handleTwoDigitsEntered(s, currentEditText, nextEditText);
        else if (s.length() == 0 && currentEditText != codeEditTexts[0]) handleEmptyInput(currentEditText);
    }

    private void handleTwoDigitsEntered(CharSequence s, EditText currentEditText, EditText nextEditText) {
        String lastCharacter = s.toString().substring(1);
        updateEditText(currentEditText, s.subSequence(0, 1), false);
        updateEditText(nextEditText, lastCharacter, true);
    }

    private void handleEmptyInput(EditText currentEditText) {
        EditText previousEditText = getPreviousEditText(currentEditText);
        assert previousEditText != null;
        updateEditText(previousEditText, previousEditText.getText(), true);
    }

    private void updateEditText(EditText editText, CharSequence text, boolean requestFocus) {
        editText.setFocusableInTouchMode(true);
        editText.setText(text);
        if (requestFocus) {
            editText.requestFocus();
            editText.setSelection(editText.getText().length());
        } editText.setFocusableInTouchMode(false);
    }

    /**
     * This method gets the previous editText from de currentEditText
     * @param currentEditText takes the previous from the currentEditText
     * @return return the previous editText.
     */
    private EditText getPreviousEditText(EditText currentEditText) {
        for (int i = 1; i < codeEditTexts.length; i++) {
            if (codeEditTexts[i] == currentEditText) return codeEditTexts[i - 1];
        } return null;
    }

    private String getCodeUser() {
        StringBuilder code = new StringBuilder();
        for (EditText codeEditText : codeEditTexts) code.append(codeEditText.getText());
        return code.toString();
    }

    private void changeColorCodeUser() {
        for (EditText codeEditText : codeEditTexts)
            codeEditText.setTextColor(getResources().getColor(R.color.redOriginal));
    }
}
