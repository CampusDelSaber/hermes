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
import com.isc.hermes.utils.CreateVerificationCode;
import com.isc.hermes.utils.ValidationPeriod;

public class Validate extends AppCompatActivity {

    private EditText codeInput;
    private TextView result;
    private Button submit;
    private CreateVerificationCode createPass = new CreateVerificationCode();
    private String code = createPass.generateVerificationCode();
    private ValidationPeriod validationPeriod;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);

        System.out.println("=======================\n" +
                "CODE: " + code +
                "\n=======================");
        result = findViewById(R.id.resultMessage);
        codeInput = findViewById(R.id.codeInput);
        submit = findViewById(R.id.submitCode);

        validationPeriod = new ValidationPeriod();
        validationPeriod.createValidationPeriod();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });
    }

    public boolean isCorrect(String code, String usercode) {
        return code.equals(usercode);
    }

    public void verifyCode() {
        String codeuser = String.valueOf(codeInput.getText());

        if (isCorrect(code, codeuser)) {
            result.setText("CORRECT");
            result.setTextColor(getResources().getColor(R.color.green));
        } else {
            result.setText("INCORRECT");
            result.setTextColor(getResources().getColor(R.color.redOriginal));
        }
    }

}