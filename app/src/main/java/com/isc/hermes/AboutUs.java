package com.isc.hermes;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * This class is in charge of controlling the activity of about us.
 */
public class AboutUs extends AppCompatActivity {

    /**
     * This method is used to create the components to start the activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        creationLabelLine();
    }

    /**
     * It is the creation of underlining a label in the view, thus obtaining the text
     * and finding the label.
     */
    private void creationLabelLine(){
        creationLineSpan(R.id.textView, "About US");
        creationLineSpan(R.id.body_text, "Hermes Objective:");
        creationLineSpan(R.id.body_text2, "Main features:");
    }

    /**
     * This method performs the creation of an underline for the text view.
     *
     * @param id value is expected to be an id resource reference.
     * @param title source text.
     */
    private void creationLineSpan(@IdRes int id, String title){
        TextView textView1 = findViewById(id);
        SpannableString textU = new SpannableString(title);
        textU.setSpan(new UnderlineSpan(), 0, textU.length(), 0);
        textView1.setText(textU);
    }

    /**
     *  It is the action of returning to sing In, to get out of the about us.
     *  You will find the button, and by clicking it you can go back.
     */
    public void goToPrincipalView(View view) {
        finish();
    }
}