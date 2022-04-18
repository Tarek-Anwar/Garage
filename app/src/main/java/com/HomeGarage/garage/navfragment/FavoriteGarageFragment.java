package com.HomeGarage.garage.navfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.HomeGarage.garage.Adapter.GarageInCityAdapter;
import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.GarageViewFragment;
import com.HomeGarage.garage.models.GarageInfoModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FavoriteGarageFragment extends Fragment {

    DatabaseReference referenceFav;
    DatabaseReference referenceGarage;
    ArrayList<String> allFavGarage = null;
    ArrayList<GarageInfoModel> garagresFav = null;
    RecyclerView recyclerGaragres;
    public FavoriteGarageFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite_garage, container, false);
        recyclerGaragres = root.findViewById(R.id.recycler_garagres);
        recyclerGaragres.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        referenceFav = FirebaseUtil.databaseReference.child(FirebaseUtil.firebaseAuth.getUid()).child("favicon");
        referenceGarage = FirebaseUtil.referenceGarage;

        getFavGarage(garagesFav ->
                recyclerGaragres.setAdapter(new GarageInCityAdapter(garagesFav, getContext(), garageInfoModel -> {
                    GarageViewFragment newFragment = new GarageViewFragment(garageInfoModel);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, newFragment).addToBackStack(null).commit();
                    })
                ));

        return root;
    }

    private void getFavGarage(OnGarageFavoriteGetCallBack callBack){
        allFavGarage = new ArrayList<>();

        referenceFav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot item : snapshot.getChildren()){
                        allFavGarage.add(item.getKey());
                    }
                    getGaragesFav(callBack);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }
    private void getGaragesFav(OnGarageFavoriteGetCallBack callBack){
        garagresFav = new ArrayList<>();
        for(String garage : allFavGarage){
            Query query = referenceGarage.orderByChild("id").equalTo(garage);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            GarageInfoModel model = item.getValue(GarageInfoModel.class);
                            garagresFav.add(model);
                        }
                        callBack.onGaragesGet(garagresFav);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private interface OnGarageFavoriteGetCallBack{
        void onGaragesGet(ArrayList<GarageInfoModel> garagesFav);
    }
}