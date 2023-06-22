package com.isc.hermes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.utils.MapConfigure;
import com.isc.hermes.utils.MarkerManager;
import com.isc.hermes.view.MapDisplay;

import java.util.ArrayList;
import java.util.List;

public class FilterViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LocationCategoryAdapter adapter;
    private List<LocationCategory> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters_view);

        categories = new ArrayList<>();
        categories.add(new LocationCategory(R.drawable.back_arrow, "Restaurant"));
        categories.add(new LocationCategory(R.drawable.ic_go, "Hotel"));
        categories.add(new LocationCategory(R.drawable.backbutton, "Shopping"));
        categories.add(new LocationCategory(R.drawable.baseline_circle_24, "Other"));
        categories.add(new LocationCategory(R.drawable.baseline_circle_24, "Other"));
        categories.add(new LocationCategory(R.drawable.baseline_circle_24, "Other"));
        categories.add(new LocationCategory(R.drawable.baseline_circle_24, "Other"));
        categories.add(new LocationCategory(R.drawable.baseline_circle_24, "Other"));
        categories.add(new LocationCategory(R.drawable.baseline_circle_24, "Other"));
        categories.add(new LocationCategory(R.drawable.baseline_circle_24, "Other"));
        categories.add(new LocationCategory(R.drawable.baseline_circle_24, "Other"));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new LocationCategoryAdapter(categories);
        recyclerView.setAdapter(adapter);
    }
}
