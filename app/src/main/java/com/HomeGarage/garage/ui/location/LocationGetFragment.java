package com.HomeGarage.garage.ui.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.ui.home.HomeFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationGetFragment extends Fragment {

    private final int locationRequestCode = 1;
    private  String allLocation = null;
    private ActivityResultLauncher<Object> launcher;

    private Geocoder geocoder;
    TextView allLocaion ;
    ImageView getlocation ;
    LinearLayout allSpace, partSapce;

    public LocationGetFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location_get, container, false);
        initUI(root);

        //check gps
        LocationManager manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        final boolean locationEnable = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!locationEnable){
            Toast.makeText(getContext(), "please, opne Gps to get Location", Toast.LENGTH_SHORT).show();
            enableLoaction();
        }else{
            Toast.makeText(getContext(), "GPS is opne", Toast.LENGTH_SHORT).show();
        }

        //Animation
        Animation animationCricle = AnimationUtils.loadAnimation(getContext(), R.anim.blinlk_animation);
        allSpace.setAnimation(animationCricle);
        partSapce.setAnimation(animationCricle);

        Animation animationImage = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_animation);
        getlocation.setAnimation(animationImage);

        //get Result
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
            if(result==locationRequestCode)  getCurrantLoaction();
            else Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        });

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        getlocation.setOnClickListener(v -> getCurrantLoaction());

        return root;
    }

    private void enableLoaction(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private void initUI(View root){
        allSpace = root.findViewById(R.id.all_space);
        getlocation = root.findViewById(R.id.btn_get_location);
        partSapce = root.findViewById(R.id.part_space);
        allLocaion =  root.findViewById(R.id.txt_show_locaion);
    }

    private void getCurrantLoaction() {
        LocationRequest locationRequest = getLocationRequest();

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, locationRequestCode);
         else {
             LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if(locationResult.getLocations().size() > 0){
                        Location location = locationResult.getLastLocation();
                        HomeFragment.curentLocation =  location;
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            Address address = addresses.get(0);
                            allLocation = getString(R.string.City)+ " : "+address.getLocality()
                                    +"\n"+getString(R.string.governorate)+" : "+address.getAdminArea()+
                                    "\n"+ getString(R.string.country)+" : " +address.getCountryName();
                            String [] govers = address.getAdminArea().split(" ");
                            HomeFragment.currentGoverment = govers[0];
                            allLocaion.setText(allLocation);
                            allSpace.clearAnimation();
                            partSapce.clearAnimation();
                        } catch (IOException e) { e.printStackTrace(); }
                    }
                }
            }, Looper.getMainLooper());
        }
    }

    private  LocationRequest getLocationRequest(){
        LocationRequest locationRequest =  LocationRequest.create();
        locationRequest.setInterval(100000);
        locationRequest.setFastestInterval(100000);
        locationRequest.setSmallestDisplacement(500f);
        locationRequest.setMaxWaitTime(2000);
        locationRequest.setNumUpdates(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


}