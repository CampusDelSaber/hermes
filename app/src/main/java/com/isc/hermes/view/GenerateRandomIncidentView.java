package com.isc.hermes.view;

import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.isc.hermes.R;
import com.isc.hermes.generators.Radium;

/**
 * This class is used to initialize the view to select how to generate incidents.
 */
public class GenerateRandomIncidentView {
    private AppCompatActivity activity;
    private Spinner spinner;
    private EditText numberIncidentsEdit;
    private ConstraintLayout layout;

    /**
     * Constructor, Initializes the view components
     *
     * @param activity Receives an AppCompacActivity to get the xml elements and initialize it.
     */
    public GenerateRandomIncidentView(AppCompatActivity activity){
        this.activity = activity;
        layout = activity.findViewById(R.id.GenerateIncidentView);
        layout.setVisibility(View.GONE);
        numberIncidentsEdit = activity.findViewById(R.id.numberIncidentsElement);
        initSpinner();
        initButtons();
    }

    /**
     * Initializes the spinner components
     */
    private void initSpinner(){
        spinner = activity.findViewById(R.id.spinnerRadio);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.spinner_items_incidents_generate  , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Initializes the view buttons
     */
    private void initButtons(){
        ImageButton locationButton = activity.findViewById(R.id.generateIncidentButton);
        Button cancelButton = activity.findViewById(R.id.cancel_generate);
        cancelButton.setOnClickListener(v -> hideOptions());
        locationButton.setOnClickListener(v -> showOptions());
    }

    /**
     * This method displays the incident generation selection menu.
     */
    private void showOptions(){
        layout.setVisibility(View.VISIBLE);
    }

    /**
     * This method hides the incident generation selection menu.
     */
    public void hideOptions(){
        layout.setVisibility(View.GONE);
    }

    /**
     * This method returns the Radium selected to generate incidents.
     *
     * @return Radium type, return the Radium selected.
     */
    public Radium getRadiumSelected(){
        Radium radiumSelected = null;
        String textSelected = String.valueOf(spinner.getSelectedItem());
        switch (textSelected){
            case "50m":
                radiumSelected = Radium.FIFTY_METERS;
                break;
            case "100m":
                radiumSelected = Radium.ONE_HUNDRED_METERS;
            case "500m":
                radiumSelected = Radium.FIVE_HUNDRED_METERS;
                break;
            case "1Km":
                radiumSelected = Radium.ONE_KILOMETER;
                break;
            case "5Km":
                radiumSelected = Radium.FIVE_KILOMETERS;
                break;
            case "10Km":
                radiumSelected = Radium.TEN_KILOMETERS;
                break;
        }
        return radiumSelected;
    }

    /**
     * This method returns the number of incidents to be generated inserted.
     *
     * @return int type, number of incidents to generate.
     */
    public int getNumberIncidentsSelected(){
        String text = numberIncidentsEdit.getText().toString();
        int quantityReturned = 0;
        if(text != null && !text.equals("")){
            quantityReturned =  Integer.parseInt(text);
        }
        return quantityReturned;
    }
}
