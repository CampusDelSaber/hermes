package com.isc.hermes.view;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.cardview.widget.CardView;
import com.isc.hermes.R;
import com.isc.hermes.utils.KeyBoardManager;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This class manages the filters view.
 */
public class FiltersView {
    private final Activity activity;
    private EditText latEditText;
    private EditText longEditText;
    private EditText searchInput;
    private CardView filtersContainer;
    private CircleImageView filtersButton;
    private Button startGeocodeButton;
    private Button chooseCityButton;

    /**
     * This constructor initializes the components of the filters view.
     *
     * @param activity The context of the current activity.
     */
    public FiltersView(Activity activity) {
        this.activity = activity;
        initComponents();
    }

    /**
     * This method initializes the components of the filters view.
     */
    private void initComponents() {
        initFilterOptionsButton();
        filtersButtonListener();
        initTextViews();
        initFiltersButtons();
    }

    /**
     * This method initializes the filter options button.
     */
    private void initFilterOptionsButton() {
        filtersButton = activity.findViewById(R.id.filtersButton);
        filtersContainer = activity.findViewById(R.id.filtersContainer);
        filtersContainer.setVisibility(View.INVISIBLE);
    }

    /**
     * This method sets the listener of the filter options button.
     */
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

    /**
     * This method initializes the text views.
     */
    private void initTextViews() {
        latEditText = activity.findViewById(R.id.geocode_latitude_editText);
        longEditText = activity.findViewById(R.id.geocode_longitude_editText);
        searchInput = activity.findViewById(R.id.searchInput);
    }

    /**
     * This method initializes the filters buttons.
     */
    private void initFiltersButtons() {
        startGeocodeButton = activity.findViewById(R.id.start_geocode_button);
        chooseCityButton = activity.findViewById(R.id.searchButton);
    }

    /**
     * This method returns the latitude edit text.
     * @return the latitude edit text
     */
    public EditText getLatEditText() {
        return latEditText;
    }

    /**
     * This method returns the longitude edit text.
     * @return the longitude edit text
     */
    public EditText getLongEditText() {
        return longEditText;
    }

    /**
     * This method returns the search input.
     * @return the search input
     */
    public EditText getSearchInput() {
        return searchInput;
    }

    /**
     * This method returns the filters container.
     * @return the filters container
     */
    public CardView getFiltersContainer() {
        return filtersContainer;
    }

    /**
     * This method returns the filters button.
     * @return the filters button
     */
    public CircleImageView getFiltersButton() {
        return filtersButton;
    }

    /**
     * This method returns the start geocode button.
     * @return the start geocode button
     */
    public Button getStartGeocodeButton() {
        return startGeocodeButton;
    }

    /**
     * This method returns the choose city button.
     * @return the choose city button
     */
    public Button getChooseCityButton() {
        return chooseCityButton;
    }
}
