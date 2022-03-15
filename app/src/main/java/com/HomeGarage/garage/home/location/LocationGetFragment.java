package com.HomeGarage.garage.home.location;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationGetFragment extends Fragment {


    private final int locationRequestCode = 1;
    private double longitude , latitude;
    private  String allLocation = null;
    private ActivityResultLauncher<Object> launcher;
    TextView allLocaion;
    private Geocoder geocoder;
    public LocationGetFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_location_get, container, false);

        ActivityResultContract<Object, Integer> contract = new ActivityResultContract<Object, Integer>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Object input) {
                return null;
            }
            @Override
            public Integer parseResult(int resultCode, @Nullable Intent intent) {
                return resultCode;
            }
        };
        launcher = registerForActivityResult(contract, result -> {
            if(result==locationRequestCode){
                getCurrantLoaction();
            }else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();

            }
        });

        geocoder = new Geocoder(getContext(), Locale.getDefault());



        LocationManager manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        final boolean locationEnable = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!locationEnable){
            Toast.makeText(getContext(), "please, opne Gps to get Location", Toast.LENGTH_SHORT).show();
            enableLoaction();
        }else{
            Toast.makeText(getContext(), "GPS is opne", Toast.LENGTH_SHORT).show();
        }

        allLocaion =  root.findViewById(R.id.txt_show_locaion);

        root.findViewById(R.id.btn_get_location).setOnClickListener(v -> {
            getCurrantLoaction();
        });

        return root;
    }

    private void enableLoaction(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }
    private void getCurrantLoaction() {
        LocationRequest locationRequest =  LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setNumUpdates(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(requireActivity(), permission, locationRequestCode);
        } else {
            LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if(locationResult.getLocations().size()>0){
                        int indx = locationResult.getLocations().size()-1;
                        longitude = locationResult.getLocations().get(indx).getLongitude();
                        latitude =    locationResult.getLocations().get(indx).getLatitude();
                        allLocation  =  longitude + "," + latitude;
                        HomeFragment.curentLocation = new LatLng(latitude,longitude);
                        Toast.makeText(getContext(), allLocation, Toast.LENGTH_SHORT).show();
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                            Address address = addresses.get(0);
                            allLocation = "city  : "+address.getLocality()+
                                    "\nstate : "+address.getAdminArea()+
                                    "\ncountry  : " +address.getCountryName();
                            String [] govers = address.getAdminArea().split(" ");
                            HomeFragment.curentGover = govers[0];
                            allLocaion.setText(allLocation);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, Looper.getMainLooper());
        }

    }

}