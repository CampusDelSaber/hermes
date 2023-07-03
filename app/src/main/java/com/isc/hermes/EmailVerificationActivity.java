package com.isc.hermes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.isc.hermes.controller.PopUp.PopUp;
import com.isc.hermes.controller.PopUp.PopUpContinueLikeGeneralUser;
import com.isc.hermes.controller.PopUp.PopUpWarningIncorrectData;
import com.isc.hermes.database.AccountInfoManager;
import com.isc.hermes.database.VerificationCodesManager;
import com.isc.hermes.model.User.UserRepository;
import com.isc.hermes.model.Validator;

/**
 * This class manages the email verification when the user declares themself as a Administrator.
 */
public class EmailVerificationActivity extends AppCompatActivity {

    private EditText[] codeEditTexts;
    private Validator validator;
    private TextView emailTextView;
    private PopUp popUpToConfirmUser;
    private PopUp warningPopUp;

    /**
     * This method initiates the window whe its called.
     * @param savedInstanceState The saved instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_verification);
        this.validator = new Validator(UserRepository.getInstance().getUserContained());
        this.popUpToConfirmUser = new PopUpContinueLikeGeneralUser(this);
        this.warningPopUp = new PopUpWarningIncorrectData(this);
        initComponents();
        ((AppManager)getApplication()).setLastActivity(this);
    }

    /**
     * Initializes the components and UI elements of the activity.
     */
    private void initComponents() {
        initCodeEditTexts();
        configureEditTexts();
    }

    /**
     * Initializes the code EditText fields.
     */
    private void initCodeEditTexts() {
        emailTextView = findViewById(R.id.emailTextView);
        emailTextView.setText(UserRepository.getInstance().getUserContained().getEmail());
        codeEditTexts = new EditText[]{findViewById(R.id.codeTextField1), findViewById(R.id.codeTextField2),
                findViewById(R.id.codeTextField3), findViewById(R.id.codeTextField4),
                findViewById(R.id.codeTextField5), findViewById(R.id.codeTextField6)
        };
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

    /**
     * Handles the text change event when the user enters or removes text in an EditText.
     *
     * @param s                The entered CharSequence.
     * @param currentEditText  The current EditText that triggered the text change event.
     * @param nextEditText     The next EditText in the sequence, if available.
     */
    private void handleTextChanged(CharSequence s, EditText currentEditText, EditText nextEditText) {
        if (s.length() == 2 && nextEditText != null) handleTwoDigitsEntered(s, currentEditText, nextEditText);
        else if (s.length() == 0 && currentEditText != codeEditTexts[0]) handleEmptyInput(currentEditText);
    }

    /**
     * Handles the scenario when two digits are entered in an EditText.
     *
     * @param s                The entered CharSequence.
     * @param currentEditText  The current EditText that triggered the event.
     * @param nextEditText     The next EditText in the sequence.
     */
    private void handleTwoDigitsEntered(CharSequence s, EditText currentEditText, EditText nextEditText) {
        String lastCharacter = s.toString().substring(1);
        updateEditText(currentEditText, s.subSequence(0, 1), false);
        updateEditText(nextEditText, lastCharacter, true);
    }

    /**
     * Handles the scenario when an EditText becomes empty.
     *
     * @param currentEditText  The current EditText that became empty.
     */
    private void handleEmptyInput(EditText currentEditText) {
        EditText previousEditText = getPreviousEditText(currentEditText);
        assert previousEditText != null;
        updateEditText(previousEditText, previousEditText.getText(), true);
    }

    /**
     * Updates the content of an EditText.
     *
     * @param editText     The EditText to update.
     * @param text         The new text for the EditText.
     * @param requestFocus Whether to request focus for the EditText.
     */
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

    /**
     * Retrieves the code entered by the user from the codeEditTexts.
     *
     * @return The code entered by the user as a String.
     */
    private String getCodeUser() {
        StringBuilder code = new StringBuilder();
        for (EditText codeEditText : codeEditTexts) code.append(codeEditText.getText());
        return code.toString();
    }

    /**
     * Changes the color of the code EditTexts to indicate an error state.
     */
    private void changeColorCodeUser() {
        for (EditText codeEditText : codeEditTexts)
            codeEditText.setTextColor(getResources().getColor(R.color.redOriginal));
    }

    /**
     * Adds an administrator user based on the provided email verification code.
     * If the code is correct, the user is added using the AccountInfoManager class,
     * and the verification code is updated. Otherwise, a warning pop-up is displayed.
     */
    private void addAdministratorUser() {
        Intent intent = new Intent(EmailVerificationActivity.this, MainActivity.class);
        String code = getCodeUser();
        if (validator.isCorrect(code)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                new AccountInfoManager().addUser(UserRepository.getInstance().getUserContained().getEmail(),
                        UserRepository.getInstance().getUserContained().getFullName(), UserRepository.getInstance().getUserContained().getUserName(),
                        UserRepository.getInstance().getUserContained().getTypeUser(), UserRepository.getInstance().getUserContained().getPathImageUser());
            verificationCodeUpdate(intent);
        } else visualizedWarningPop();
    }

    /**
     * Updates the user to an administrator user based on the provided email verification code.
     * If the code is correct, the user is updated using the AccountInfoManager class,
     * and the verification code is updated. Otherwise, a warning pop-up is displayed.
     */
    private void updateToAdministratorUser() {
        Intent intent = new Intent(EmailVerificationActivity.this, AccountInformation.class);
        String code = getCodeUser();
        if (validator.isCorrect(code)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                new AccountInfoManager().editUser(UserRepository.getInstance().getUserContained());
            verificationCodeUpdate(intent);
        } else visualizedWarningPop();
    }

    /**
     * Displays the warning pop-up, including a color code change for the user.
     * The warning pop-up is shown using the warningPopUp object.
     */
    private void visualizedWarningPop(){
        changeColorCodeUser();
        warningPopUp.show();
    }

    /**
     * Performs the verification code update and starts the specified activity.
     *
     * @param intent the intent of the activity to be started
     */
    private void verificationCodeUpdate(Intent intent){
        VerificationCodesManager verificationCodesManager = new VerificationCodesManager();
        verificationCodesManager.updateVerificationCode(validator.getId(), false);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Continues the email verification process and performs the necessary actions if the entered code is correct.
     *
     * @param view The View that triggered the method call.
     */
    public void continueVerification(View view) {
        if (UserRepository.getInstance().getUserContained().isRegistered() && UserRepository.getInstance().getUserContained().getTypeUser().equals("General")){
            UserRepository.getInstance().getUserContained().setTypeUser("Administrator");
            updateToAdministratorUser();
        } else {
            addAdministratorUser();
        }
    }

    /**
     * Continues the process for a general user (non-administrator).
     *
     * @param view The View that triggered the method call.
     */
    public void continueLikeGeneralUser(View view) {
        popUpToConfirmUser.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((AppManager) getApplication()).setLastActivity(null);
    }
}
