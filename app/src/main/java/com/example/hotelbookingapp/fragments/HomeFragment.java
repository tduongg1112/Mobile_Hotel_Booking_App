package com.example.hotelbookingapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelbookingapp.R;
import com.example.hotelbookingapp.adapters.HomeAdapter;
import com.example.hotelbookingapp.models.Hotel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private EditText etLocation;
    private LinearLayout btnFilter;
    private RecyclerView rvHotels;
    private HomeAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private List<Hotel> originalHotelList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        etLocation = view.findViewById(R.id.etLocation);
        btnFilter = view.findViewById(R.id.btnFilter);
        rvHotels = view.findViewById(R.id.rvHotels);

        setupRecyclerView();
        loadDefaultHotels();

        etLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        btnFilter.setOnClickListener(v -> showFilterDialog());

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
        db.collection("hotels").orderBy("name").get()
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

    private void performSearch() {
        String keyword = etLocation.getText().toString().trim().toLowerCase();
        db.collection("hotels").whereArrayContains("searchKeywords", keyword).get()
                .addOnSuccessListener(snapshots -> {
                    hotelList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Hotel hotel = doc.toObject(Hotel.class);
                        hotel.setId(doc.getId());
                        hotelList.add(hotel);
                    }
                    hotelAdapter.notifyDataSetChanged();
                    if (hotelList.isEmpty() && getContext() != null) {
                        Toast.makeText(getContext(), "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showFilterDialog() {
        if (getContext() == null) return;
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.dialog_filter);

        CheckBox cbWifi = dialog.findViewById(R.id.cbWifi);
        CheckBox cbPool = dialog.findViewById(R.id.cbPool);
        Button btnApply = dialog.findViewById(R.id.btnApplyFilter);

        if (btnApply != null) btnApply.setOnClickListener(v -> {
            List<String> selectedAmenities = new ArrayList<>();
            if (cbWifi.isChecked()) selectedAmenities.add("WIFI");
            if (cbPool.isChecked()) selectedAmenities.add("POOL");
            applyFilter(selectedAmenities);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void applyFilter(List<String> requiredAmenities) {
        if (requiredAmenities.isEmpty()) {
            hotelList.clear();
            hotelList.addAll(originalHotelList);
            hotelAdapter.notifyDataSetChanged();
            return;
        }
        List<Hotel> filtered = new ArrayList<>();
        for (Hotel h : originalHotelList) {
            if (h.getAmenities() != null && h.getAmenities().containsAll(requiredAmenities)) {
                filtered.add(h);
            }
        }
        hotelList.clear();
        hotelList.addAll(filtered);
        hotelAdapter.notifyDataSetChanged();
    }
}