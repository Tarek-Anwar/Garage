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
import android.widget.Toast;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {
    ArrayList<GrageInfo> grageInfos = new ArrayList<>();
    List<Marker> markers = new ArrayList<>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            for(int i=0; i<grageInfos.size();i++){
               Marker marker =  googleMap.addMarker(new MarkerOptions().position(grageInfos.get(i).getLatLngGarage()).title(grageInfos.get(i).getNameEn()));
               marker.setTag(i);
               markers.add(marker);
            }

           googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    GrageInfo grageInfo = grageInfos.get(Integer.parseInt(marker.getTag().toString()));
                    grageInfo.setPriceForHour(4f);
                    GarageViewFragment newFragment = new GarageViewFragment(grageInfo);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return false;
                }
            });

            for (Marker m : markers){
               /* LatLng latLng = new LatLng(m.getPosition().latitude,m.getPosition().longitude);
                googleMap.addMarker(new MarkerOptions().position(latLng));*/
               googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(),11.5f));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_maps, container, false);

        grageInfos.add(new GrageInfo("Mahalla 1","Garbia","Mahalla","Str 21" ,"30.973617,31.1700415"));
        grageInfos.add(new GrageInfo("Mahalla 2","Garbia","Mahalla","Str 50" ,"30.9758634,31.1598116"));
        grageInfos.add(new GrageInfo("Mahalla 3","Garbia","Mahalla","Str 21" ,"30.9820509,31.1652127"));
        grageInfos.add(new GrageInfo("Mahalla 4","Garbia","Mahalla","Str 21" ,"30.974715,31.1644506"));



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}