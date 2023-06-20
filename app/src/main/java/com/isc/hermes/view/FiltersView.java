package com.isc.hermes.view;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.cardview.widget.CardView;
import com.isc.hermes.R;
import com.isc.hermes.utils.KeyBoardManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class FiltersView {
    private final Activity activity;
    private EditText latEditText;
    private EditText longEditText;
    private EditText searchInput;
    private CardView filtersContainer;
    private CircleImageView filtersButton;
    private Button startGeocodeButton;
    private Button chooseCityButton;

    public FiltersView(Activity activity) {
        this.activity = activity;
        initComponents();
    }

    private void initComponents() {
        initFilterOptionsButton();
        filtersButtonListener();
        initTextViews();
        initFiltersButtons();
    }

    private void initFilterOptionsButton() {
        filtersButton = activity.findViewById(R.id.filtersButton);
        filtersContainer = activity.findViewById(R.id.filtersContainer);
        filtersContainer.setVisibility(View.INVISIBLE);
    }

    private void filtersButtonListener() {
        filtersButton.setOnClickListener(v -> {
            if (filtersContainer.getVisibility() == View.INVISIBLE)
                filtersContainer.setVisibility(View.VISIBLE);
            else {
                filtersContainer.setVisibility(View.INVISIBLE);
                KeyBoardManager.getInstance().closeKeyBoard(v, activity);
            }
        });
    }

    private void initTextViews() {
        latEditText = activity.findViewById(R.id.geocode_latitude_editText);
        longEditText = activity.findViewById(R.id.geocode_longitude_editText);
        searchInput = activity.findViewById(R.id.searchInput);
    }

    private void initFiltersButtons() {
        startGeocodeButton = activity.findViewById(R.id.start_geocode_button);
        chooseCityButton = activity.findViewById(R.id.searchButton);
    }

    public EditText getLatEditText() {
        return latEditText;
    }

    public EditText getLongEditText() {
        return longEditText;
    }

    public EditText getSearchInput() {
        return searchInput;
    }

    public CardView getFiltersContainer() {
        return filtersContainer;
    }

    public CircleImageView getFiltersButton() {
        return filtersButton;
    }

    public Button getStartGeocodeButton() {
        return startGeocodeButton;
    }

    public Button getChooseCityButton() {
        return chooseCityButton;
    }
}
