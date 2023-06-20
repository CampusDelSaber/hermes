package com.isc.hermes.view;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.cardview.widget.CardView;
import com.isc.hermes.R;
import de.hdodenhof.circleimageview.CircleImageView;
import com.isc.hermes.databinding.ActivityMainBinding;

public class FiltersView {
    private final Activity activity;
    private final ActivityMainBinding binding;

    public FiltersView(Activity activity) {
        this.activity = activity;
        this.binding = ActivityMainBinding.inflate(activity.getLayoutInflater());
        initComponents();
    }

    private void initComponents() {
        initFilterOptionsButton();
        filtersButtonListener();
    }

    private void initFilterOptionsButton() {
        binding.filtersContainer.setVisibility(View.INVISIBLE);
    }

    private void filtersButtonListener() {
        binding.filtersButton.setOnClickListener(v -> {
            if (binding.filtersContainer.getVisibility() == View.INVISIBLE)
                binding.filtersContainer.setVisibility(View.VISIBLE);
            else {
                binding.filtersContainer.setVisibility(View.GONE);
            }
        });
    }

    public EditText getLatEditText() {
        return binding.geocodeLatitudeEditText;
    }

    public EditText getLongEditText() {
        return binding.geocodeLongitudeEditText;
    }

    public EditText getSearchInput() {
        return binding.searchInput;
    }

    public CardView getFiltersContainer() {
        return binding.filtersContainer;
    }

    public CircleImageView getFiltersButton() {
        return binding.filtersButton;
    }

    public Button getStartGeocodeButton() {
        return binding.startGeocodeButton;
    }

    public Button getChooseCityButton() {
        return binding.searchButton;
    }
}
