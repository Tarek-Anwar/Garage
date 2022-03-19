package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment {

    ArrayList<GrageInfo> grageInfos = null;
    LatLng locationMe = null;
    String gover = null;
    List<Marker> markers = new ArrayList<>();

    public MapsFragment( ){ }

    private final OnMapReadyCallback callback = googleMap -> {

        if (locationMe!=null) {
            Marker me = googleMap.addMarker(new MarkerOptions().position(locationMe).title("Me"));
            assert me != null;
            me.setTag(-1);
            markers.add(me);
            setMarkersOnMap(googleMap);
        }else if(gover!=null){
            setMarkersOnMap(googleMap);
        }

       googleMap.setOnMarkerClickListener(marker -> {
           if(Integer.parseInt(Objects.requireNonNull(marker.getTag()).toString()) == -1){
               Toast.makeText(getContext(), "ME", Toast.LENGTH_SHORT).show();
           }else {
               GrageInfo grageInfo = grageInfos.get(Integer.parseInt(marker.getTag().toString()));
               if (grageInfo != null) {
                   GarageViewFragment newFragment = new GarageViewFragment(grageInfo);
                   FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                   transaction.replace(R.id.fragmentContainerView, newFragment);
                   transaction.addToBackStack(null);
                   transaction.commit();
               }
           }
           return false;
       });

        if (locationMe != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationMe, 9));

        }else if(gover!=null){
            for(Marker m : markers) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 9));
            }
        }

    };

    public void setMarkers(ArrayList<GrageInfo> grageInfos){
        this.grageInfos=grageInfos;
    }
    public void setLocationMe(LatLng locationMe){
        this.locationMe = locationMe;
    }
    public void setGover(String gover){this.gover=gover;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
             mapFragment.getMapAsync(callback);
        }
    }
    void setMarkersOnMap(GoogleMap googleMap){
        if (grageInfos != null) {
            for (int i = 0; i < grageInfos.size(); i++) {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(grageInfos.get(i).getLatLngGarage())
                        .title(grageInfos.get(i).getNameEn())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                assert marker != null;
                marker.setTag(i);
                markers.add(marker);
            }
        }

    }
}