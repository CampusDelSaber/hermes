package com.isc.hermes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EmailVerificationActivity extends AppCompatActivity {

    private EditText[] codeEditTexts;
    private EditText codeTextField1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_verification);

        // Obtener referencias a los EditText
        codeTextField1 = findViewById(R.id.codeTextField1);
        EditText codeTextField2 = findViewById(R.id.codeTextField2);
        EditText codeTextField3 = findViewById(R.id.codeTextField3);
        EditText codeTextField4 = findViewById(R.id.codeTextField4);
        EditText codeTextField5 = findViewById(R.id.codeTextField5);
        EditText codeTextField6 = findViewById(R.id.codeTextField6);

        // Crear un arreglo con los EditText restantes
        codeEditTexts = new EditText[]{codeTextField2, codeTextField3, codeTextField4, codeTextField5, codeTextField6};

        // Configurar TextWatchers y OnFocusChangeListener para los EditText
        for (int i = 0; i < codeEditTexts.length; i++) {
            final EditText currentEditText = codeEditTexts[i];
            final EditText nextEditText = (i < codeEditTexts.length - 1) ? codeEditTexts[i + 1] : null;

            currentEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && nextEditText != null) {
                        nextEditText.requestFocus();
                    }
                }
            });

            currentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        codeTextField1.requestFocus();
                    }
                }
            });
        }
    }
}
