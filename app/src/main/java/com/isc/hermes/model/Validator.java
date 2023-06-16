package com.isc.hermes.model;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.isc.hermes.R;
import com.isc.hermes.database.VerificationCodesManager;

/**
 * The Validator class is responsible for validating a verification code entered by the user.
 */
public class Validator extends AppCompatActivity {

    private EditText codeInput;
    private TextView result;
    private Button submit;
    private String code;
    private Boolean valid;
    private VerificationCodesManager verificationCodesManager;
    private VerificationCode verificationCode;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState The saved instance state.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator);

        result = findViewById(R.id.resultMessage);
        codeInput = findViewById(R.id.codeInput);
        submit = findViewById(R.id.submitCode);

        verificationCodesManager = new VerificationCodesManager();
        verificationCodesManager.addVerificationCode("hermes.map.app@gmail.com");
        try {
            verificationCode = verificationCodesManager.getLastVerificationCode("hermes.map.app@gmail.com");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        code = verificationCode.getVerificationCode();
        valid = verificationCode.getValid();
        String id = verificationCode.getId();

        System.out.println("=======================\n" +
                "CODE: " + code +
                "\n=======================");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
                verificationCodesManager.updateVerificationCode(id, false);
            }
        });
    }

    /**
     * Checks if the entered code matches the actual code.
     *
     * @param code     The actual verification code.
     * @param userCode The code entered by the user.
     * @return True if the codes match, false otherwise.
     */
    public boolean isCorrect(String code, String userCode) {
        return code.equals(userCode);
    }

    /**
     * Verifies the entered verification code.
     */
    public void verifyCode() {
        String userCode = String.valueOf(codeInput.getText());

        if (isCorrect(code, userCode) && valid) {
            result.setText("CORRECT");
            result.setTextColor(getResources().getColor(R.color.green));
        } else {
            result.setText("INCORRECT");
            result.setTextColor(getResources().getColor(R.color.redOriginal));
        }
    }
}