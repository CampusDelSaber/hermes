package com.isc.hermes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EmailVerificationActivity extends AppCompatActivity {


    private EditText[] codeEditTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_verification);
        initComponents();
    }

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

    private void configureEditTexts() {
        for (int i = 0; i < codeEditTexts.length; i++) {
            final EditText currentEditText = codeEditTexts[i];
            final EditText nextEditText = (i < codeEditTexts.length - 1) ? codeEditTexts[i + 1] : null;

            configureTextWatcher(currentEditText, nextEditText);

        }
    }

    private void configureTextWatcher(final EditText currentEditText, final EditText nextEditText) {

        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private EditText getPreviousEditText(EditText currentEditText) {
        for (int i = 1; i < codeEditTexts.length; i++) {
            if (codeEditTexts[i] == currentEditText) {
                return codeEditTexts[i - 1];
            }
        }
        return null;
    }


}
