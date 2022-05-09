package com.HomeGarage.garage.ui.navfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.adapter.GarageInCityAdapter;
import com.HomeGarage.garage.modules.GarageInfoModule;
import com.HomeGarage.garage.ui.home.GarageViewFragment;
import com.HomeGarage.garage.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FavoriteGarageFragment extends Fragment {

    DatabaseReference referenceFav;
    DatabaseReference referenceGarage = FirebaseUtil.referenceGarage;
    ArrayList<String> allFavGarage = null;
    ArrayList<GarageInfoModule> garagresFav = new ArrayList<>();
    RecyclerView recyclerGaragres;
    ProgressBar progressBarFavorite;

    public FavoriteGarageFragment() {
        referenceFav = FirebaseUtil.databaseReference.child(Objects.requireNonNull(FirebaseUtil.firebaseAuth.getUid())).child("favicon");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite_garage, container, false);
        recyclerGaragres = root.findViewById(R.id.recycler_garagres);
        progressBarFavorite = root.findViewById(R.id.progress_bar_favorite);

        recyclerGaragres.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        getFavGarage(garagesFav ->
                recyclerGaragres.setAdapter(new GarageInCityAdapter(garagesFav, getContext(), garageInfoModel ->
                   replaceFragement(new GarageViewFragment(garageInfoModel)))));

        return root;
    }

    private void replaceFragement(Fragment fragment){
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment).addToBackStack(null).commit();
    }

    private void getFavGarage(OnGarageFavoriteGetCallBack callBack){
        allFavGarage = new ArrayList<>();
        referenceFav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    allFavGarage.clear();
                    for(DataSnapshot item : snapshot.getChildren()){
                        allFavGarage.add(item.getKey());
                    }
                    progressBarFavorite.setVisibility(View.GONE);
                    garagresFav.clear();
                    getGaragesFav(callBack);
                }else  progressBarFavorite.setVisibility(View.GONE);
            }
            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw new DatabaseException(error.getMessage());
            }
        });
    }

    private void getGaragesFav(OnGarageFavoriteGetCallBack callBack){
        for(String garage : allFavGarage){
            Query query = referenceGarage.orderByChild("id").equalTo(garage);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for(DataSnapshot item : snapshot.getChildren()){
                            GarageInfoModule model = item.getValue(GarageInfoModule.class);
                            garagresFav.add(model);
                        }
                        progressBarFavorite.setProgress(View.GONE);
                        callBack.onGaragesGet(garagresFav);
                    }
                }
                @SuppressLint("RestrictedApi")
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw new DatabaseException(error.getMessage());
                }
            });
        }
    }

    private interface OnGarageFavoriteGetCallBack{
        void onGaragesGet(ArrayList<GarageInfoModule> garagesFav);
    }
}