package com.isc.hermes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class manages the email verification when the user declares themself as a Administrator.
 */
public class EmailVerificationActivity extends AppCompatActivity {

    private EditText[] codeEditTexts;

    /**
     * This method initiates the window whe its called.
     * @param savedInstanceState The saved instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_verification);
        initComponents();
    }

    /**
     * This method calls the codeTextFields and groups those in a Array.
     */
    private void initComponents(){
        codeEditTexts = new EditText[]{
                findViewById(R.id.codeTextField1),
                findViewById(R.id.codeTextField2),
                findViewById(R.id.codeTextField3),
                findViewById(R.id.codeTextField4),
                findViewById(R.id.codeTextField5),
                findViewById(R.id.codeTextField6)
        };

        configureEditTexts();
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
     * This method adds a TextChangedListener to every codeTextField.
     * Puts an onTextChanged listener that reacts when a couple numbers are written on the editText
     * And set the focus on the next or the previous depending on the number deleted or added.
     * @param currentEditText the current editText.
     * @param nextEditText the next editText to step on.
     */
    private void configureTextWatcher(final EditText currentEditText, final EditText nextEditText) {

        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2 && nextEditText != null){
                    String lastCharacter = s.toString().substring(1);
                    currentEditText.setText(s.subSequence(0,1));
                    nextEditText.setFocusableInTouchMode(true);
                    nextEditText.setText(lastCharacter);
                    nextEditText.requestFocus();
                    nextEditText.setSelection(nextEditText.getText().length());
                    currentEditText.setFocusableInTouchMode(false);
                }
                else if (s.length() == 0 && currentEditText != codeEditTexts[0]){
                    EditText previousEditText =  getPreviousEditText(currentEditText);
                    previousEditText.setFocusableInTouchMode(true);
                    previousEditText.requestFocus();
                    previousEditText.setSelection(previousEditText.getText().length());
                    currentEditText.setFocusableInTouchMode(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * This method gets the previous editText from de currentEditText
     * @param currentEditText takes the previous from the currentEditText
     * @return return the previous editText.
     */
    private EditText getPreviousEditText(EditText currentEditText) {
        for (int i = 1; i < codeEditTexts.length; i++) {
            if (codeEditTexts[i] == currentEditText) {
                return codeEditTexts[i - 1];
            }
        }
        return null;
    }
}
