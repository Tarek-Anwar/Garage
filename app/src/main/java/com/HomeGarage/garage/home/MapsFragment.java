package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.Adapter.CustomInfoWindowAdpter;
import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.models.GarageInfoModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsFragment extends Fragment {

    SupportMapFragment mapFragment;
    private  ArrayList<GarageInfoModel> garageInfoModels;
    private  LatLng locationMe = null;
    private  String gover;
    private  String title , snippet ;
    private  List<Marker> markers = new ArrayList<>();
    boolean  cityCheck = true;
    private final OnMapReadyCallback callback = googleMap -> {

        if (locationMe!=null && title!=null)  {
            Marker me = googleMap.addMarker(new MarkerOptions().position(locationMe).title(title).snippet(snippet));
            assert me != null;
            me.setTag(-1);
            markers.add(me);
            setMarkersOnMap(googleMap);
        }else if(gover!=null)setMarkersOnMap(googleMap);

        if(cityCheck) {
            getAllGarage(grageInfos -> {
                if (locationMe != null) {
                    setMarkersOnMap(googleMap);
                    this.garageInfoModels = grageInfos;
                }
            });
        }

        if (locationMe != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationMe, 10));
            googleMap.setInfoWindowAdapter(new CustomInfoWindowAdpter(getContext()));
        }else if(gover!=null){
            for(Marker m : markers) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 11.2f));
                googleMap.setInfoWindowAdapter(new CustomInfoWindowAdpter(getContext()));
            }
        }

        googleMap.setOnInfoWindowClickListener(marker -> {
            if(Integer.parseInt(Objects.requireNonNull(marker.getTag()).toString()) == -1){
            }else {
                GarageInfoModel garageInfoModel = garageInfoModels.get(Integer.parseInt(marker.getTag().toString()));
                if (garageInfoModel != null) {
                    GarageViewFragment newFragment = new GarageViewFragment(garageInfoModel);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    };

    public MapsFragment(){ }

    public void setMarkers(ArrayList<GarageInfoModel> garageInfoModels, boolean cityCheck){
        this.garageInfoModels = garageInfoModels;
        this.cityCheck = cityCheck;
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }
    public void setLocationMe(LatLng locationMe){
        this.locationMe = locationMe;
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    public void setGover(String gover){
        this.gover=gover;

    }
    public void setTitle(String title , String snippet){
        this.title = title;
        this.snippet = snippet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
             mapFragment.getMapAsync(callback);
        }
    }

    void setMarkersOnMap(GoogleMap googleMap){
        if (garageInfoModels != null) {
            for (int i = 0; i < garageInfoModels.size(); i++) {
                double distentGarage = 0;
                if(cityCheck){
                    double lat = garageInfoModels.get(i).getLatLngGarage().latitude;
                    double lon = garageInfoModels.get(i).getLatLngGarage().longitude;
                    distentGarage = distance(locationMe.latitude,locationMe.longitude,lon,lat);
                }
                if(distentGarage<13 || cityCheck==false){
                    int numOfRate = garageInfoModels.get(i).getNumOfRatings();
                    String name ;
                    if(Locale.getDefault().getLanguage().equals("en")) name = garageInfoModels.get(i).getNameEn();
                    else name = garageInfoModels.get(i).getNameAr();
                    String snippet = (garageInfoModels.get(i).getRate()/(2*numOfRate)) + " ( " +numOfRate+" "+" rates "+" )";
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(garageInfoModels.get(i).getLatLngGarage())
                            .title(name)
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    assert marker != null;
                    marker.setTag(i);
                    markers.add(marker);
               }
            }
        }
    }

    private void getAllGarage(HomeFragment.OnDataReceiveCallback callback) {
        garageInfoModels = new ArrayList<>();
        DatabaseReference ref = FirebaseUtil.referenceGarage;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        GarageInfoModel info = item.getValue(GarageInfoModel.class);
                        garageInfoModels.add(info);
                    }
                    callback.onDataReceived(garageInfoModels);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
