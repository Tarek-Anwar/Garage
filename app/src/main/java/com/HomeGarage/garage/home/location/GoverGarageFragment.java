package com.HomeGarage.garage.home.location;

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
import android.widget.Toast;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.CityAdapter;
import com.HomeGarage.garage.home.HomeFragment;
import com.HomeGarage.garage.home.MapsFragment;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GoverGarageFragment extends Fragment implements CityAdapter.CityListener {

    int pos;
    String gover;
    public GoverGarageFragment(int pos , String gover) {
        this.pos = pos;
        this.gover = gover;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_gover_garage, container, false);

        getAllGarage(grageInfos -> {
            FragmentTransaction transaction2 = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction2.add(R.id.map_gover,new MapsFragment(grageInfos,null , gover));
            transaction2.commit();
        });


        CityAdapter cityAdapter = new CityAdapter(pos,this::onCityListener);
        RecyclerView recyclerCity = root.findViewById(R.id.recycle_city);
        recyclerCity.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerCity.setAdapter(cityAdapter);

        return root;
    }

    @Override
    public void onCityListener(String s) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new CityGarageFragment(s));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getAllGarage(HomeFragment.OnDataReceiveCallback callback) {
        ArrayList<GrageInfo>grageInfos = new ArrayList<>();
        DatabaseReference ref = FirebaseUtil.referenceGarage;
        Query query = ref.orderByChild("governoateEn").equalTo(gover);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    grageInfos.clear();
                    for (DataSnapshot item : snapshot.getChildren()) {
                        GrageInfo info = item.getValue(GrageInfo.class);
                        grageInfos.add(info);
                    }
                    callback.onDataReceived(grageInfos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}