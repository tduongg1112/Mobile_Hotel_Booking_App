package com.example.hotelbookingapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.adapters.HomeAdapter;
import com.example.hotelbookingapp.models.Hotel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {
    private EditText etLocation;
    private RecyclerView rvHotels;
    private HomeAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private List<Hotel> originalHotelList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        etLocation = view.findViewById(R.id.etLocation);
        rvHotels = view.findViewById(R.id.rvHotels);

        setupRecyclerView();
        loadDefaultHotels();

        // Search ngay khi người dùng gõ phím
        etLocation.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                performLocalSearch(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void setupRecyclerView() {
        hotelList = new ArrayList<>();
        originalHotelList = new ArrayList<>();
        hotelAdapter = new HomeAdapter(getContext(), hotelList);
        rvHotels.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHotels.setAdapter(hotelAdapter);
    }

    private void loadDefaultHotels() {
        FirebaseFirestore.getInstance().collection("hotels").orderBy("name").get()
                .addOnSuccessListener(snapshots -> {
                    hotelList.clear();
                    originalHotelList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Hotel hotel = doc.toObject(Hotel.class);
                        hotel.setId(doc.getId());
                        hotelList.add(hotel);
                        originalHotelList.add(hotel);
                    }
                    hotelAdapter.notifyDataSetChanged();
                });
    }

    private void performLocalSearch(String text) {
        List<Hotel> filteredList = new ArrayList<>();
        String query = removeAccent(text.toLowerCase().trim());

        if (query.isEmpty()) {
            filteredList.addAll(originalHotelList);
        } else {
            for (Hotel hotel : originalHotelList) {
                String name = (hotel.getName() != null) ? removeAccent(hotel.getName().toLowerCase()) : "";
                String loc = (hotel.getLocation() != null) ? removeAccent(hotel.getLocation().toLowerCase()) : "";
                if (name.contains(query) || loc.contains(query)) {
                    filteredList.add(hotel);
                }
            }
        }
        hotelList.clear();
        hotelList.addAll(filteredList);
        hotelAdapter.notifyDataSetChanged();
    }

    private String removeAccent(String s) {
        if (s == null) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }
}