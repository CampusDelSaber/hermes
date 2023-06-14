package com.isc.hermes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isc.hermes.model.Place;

import java.util.ArrayList;
import java.util.List;

public class SearchViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        recyclerView = findViewById(R.id.recycle_view_searched);

        places = new ArrayList<>();
        places.add(new Place("Lugar 1", "Ubicación 1"));
        places.add(new Place("Lugar 2", "Ubicación 2"));
        places.add(new Place("Lugar 3", "Ubicación 3"));
        places.add(new Place("Lugar 4", "Ubicación 4"));

        PlacesAdapter adapter = new PlacesAdapter(places);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
