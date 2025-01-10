package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.AdvertisementAdapter;
import com.example.basicmobileprogramingproject.Entity.AdvertisementEntity;
import com.example.basicmobileprogramingproject.Model.AdvertisementModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdvertisementFragment extends Fragment {
    View view;
    private RecyclerView rvAdsList;
    private FloatingActionButton fabAddAd;
    private FloatingActionButton btnDeleteAd;
    AdvertisementEntity advertisementEntity;
    ArrayList<AdvertisementModel> advertisementList;
    AdvertisementAdapter advertisementAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_advertisement, container, false);
        // mapping id
        rvAdsList = view.findViewById(R.id.rvAdsList);
        fabAddAd = view.findViewById(R.id.fabAddAd);
        btnDeleteAd = view.findViewById(R.id.btnDeleteAd);

        advertisementEntity = new AdvertisementEntity(getContext());
        advertisementList = advertisementEntity.getAdvertisementList();
        if (advertisementList.isEmpty()) {
            AlertDialogUtils.showInfoDialog(getContext(), "No advertisement found");
        }
        advertisementAdapter = new AdvertisementAdapter(getContext(), advertisementList);
        uploadDataToRecyclerViewAds();

        return view;
    }

    public void uploadDataToRecyclerViewAds() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        rvAdsList.setLayoutManager(layoutManager);
        rvAdsList.setAdapter(advertisementAdapter);
    }
}
