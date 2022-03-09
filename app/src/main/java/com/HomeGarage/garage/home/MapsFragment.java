package com.HomeGarage.garage.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    ArrayList<GrageInfo> grageInfos = FirebaseUtil.allGarage;
    List<Marker> markers = new ArrayList<>();

    public MapsFragment(){ }

    private OnMapReadyCallback callback = googleMap -> {
        if(!grageInfos.isEmpty()) {
            for (int i = 0; i < grageInfos.size(); i++) {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(grageInfos.get(i).getLatLngGarage()).title(grageInfos.get(i).getNameEn()));
                marker.setTag(i);
                markers.add(marker);
            }
        }

       googleMap.setOnMarkerClickListener(marker -> {
           GrageInfo grageInfo = grageInfos.get(Integer.parseInt(marker.getTag().toString()));
           grageInfo.setPriceForHour(4f);
           GarageViewFragment newFragment = new GarageViewFragment(grageInfo);
           FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
           transaction.replace(R.id.fragmentContainerView, newFragment);
           transaction.addToBackStack(null);
           transaction.commit();

           return false;
       });

        for (Marker m : markers){
           /* LatLng latLng = new LatLng(m.getPosition().latitude,m.getPosition().longitude);
            googleMap.addMarker(new MarkerOptions().position(latLng));*/
           googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(),11.5f));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            getAllGarage(grageInfos -> mapFragment.getMapAsync(callback));
        }
    }

    public interface OnDataReceiveCallback {
        void onDataReceived(ArrayList<GrageInfo> grageInfos);}

    private void getAllGarage(OnDataReceiveCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GaragerOnwerInfo");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
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