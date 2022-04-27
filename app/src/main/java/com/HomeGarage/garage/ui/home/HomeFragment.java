package com.HomeGarage.garage.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.adapter.GovernorateAdapter;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.ui.location.GoverGarageFragment;
import com.HomeGarage.garage.ui.location.LocationGetFragment;
import com.HomeGarage.garage.modules.GarageInfoModule;
import com.HomeGarage.garage.ui.navfragment.FavoriteGarageFragment;
import com.HomeGarage.garage.service.OnSwipeTouchListener;
import com.HomeGarage.garage.ui.navfragment.SettingFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements GovernorateAdapter.GoverListener  {

    public static LatLng curentLocation = null;
    public static String curentGover = null;
    private final int locationRequestCode = 1;
    MapsFragment mapsFragment;
    MapSetLocation setLocation;
    private DrawerLayout drawerLayout;
    private RecyclerView   recyclerGover ;
    private View seeAllOper , locationGet , imageNaveDrawer , imageFavGarage;
    private GovernorateAdapter governorateAdapter;
    private TextView govetLocation;
    private String me , here;
    private SharedPreferences preferences ;
    private ActivityResultLauncher<Object> launcher;
    private Geocoder geocoder;
    private ProgressBar progressBarGover;

    public HomeFragment(){ }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        initViews(root);
        drawerLayout = getActivity().findViewById(R.id.drawer_layout);

        if(getActivity()!=null){
            mapsFragment = (MapsFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerMap);
        }
        me = getActivity().getString(R.string.me_location);here = getActivity().getString(R.string.i_here);

        //swaip
        root.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight() { drawerLayout.open();setAnimation(); } });


        imageNaveDrawer.setOnClickListener(v -> {
            drawerLayout.open();
            setAnimation();
        });

        preferences = getActivity().getSharedPreferences(getString(R.string.file_info_user), Context.MODE_PRIVATE);
        boolean locationget = preferences.getBoolean(SettingFragment.LOCATIOON_SETTINNG,false);
        if(locationget){
            locationGet.setVisibility(View.GONE);
            LocationManager manager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            final boolean locationEnable = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!locationEnable){
                Toast.makeText(getContext(), "please, opne Gps to get Location", Toast.LENGTH_SHORT).show();
                enableLoaction();
            }
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
                if(result==locationRequestCode){
                    getCurrantLoaction();
                }else { Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show(); }
            });
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            getCurrantLoaction();

        }else {
            if(curentLocation!=null){
                if(getActivity()!=null){
                    mapsFragment.setTitle(me,here);
                    mapsFragment.setLocationMe(curentLocation);
                }
                govetLocation.setText(curentGover);
                locationGet.setVisibility(View.GONE);
            }else locationGet.setOnClickListener(v -> replaceFragment(new LocationGetFragment()));
        }

        setLocation = latLng -> {
            if(getActivity()!=null){
                mapsFragment.setTitle(me,here);
                mapsFragment.setLocationMe(latLng);
            }
        };

        if(getActivity()!=null){
            LastOperFragment fragmentView = (LastOperFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerLastOper);
            fragmentView.setCount(3);
        }

        seeAllOper.setOnClickListener(v -> replaceFragment(new LastOperFragment(1)));

        imageFavGarage.setOnClickListener(v -> replaceFragment(new FavoriteGarageFragment()));
        //Gover item
        governorateAdapter = new GovernorateAdapter(this::onGoverListener,progressBarGover);
        recyclerGover.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        recyclerGover.setAdapter(governorateAdapter);

        return root;
    }

    @Override
    public void onGoverListener(int pos , String s) {
        GoverGarageFragment fragment = new GoverGarageFragment (pos , s , getContext());
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initViews(View v){
        seeAllOper = v.findViewById(R.id.see_all_last_oper);
        recyclerGover = v.findViewById(R.id.recycle_gover);
        locationGet = v.findViewById(R.id.get_location);
        govetLocation = v.findViewById(R.id.txt_govet_location);
        imageNaveDrawer = v.findViewById(R.id.opne_nave);
        imageFavGarage = v.findViewById(R.id.image_fav_garage);
        progressBarGover = v.findViewById(R.id.progress_bar_gover);
    }

    private void enableLoaction(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private void getCurrantLoaction() {
        LocationRequest locationRequest =  LocationRequest.create();
        locationRequest.setInterval(100000);
        locationRequest.setFastestInterval(100000);
        locationRequest.setSmallestDisplacement(1f);
        locationRequest.setMaxWaitTime(2000);
        locationRequest.setNumUpdates(5);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(requireActivity(), permission, locationRequestCode);
        }
        else {
            LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if(!locationResult.getLocations().isEmpty()){
                        int i = locationResult.getLocations().size()-1;
                        double longitude = locationResult.getLocations().get(i).getLongitude();
                        double latitude = locationResult.getLocations().get(i).getLatitude();
                       // curentLocation = new LatLng(latitude,longitude);
                        setLocation.onMapSetLocation(new LatLng(latitude,longitude));
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                            Address address = addresses.get(0);
                            String [] govers = address.getAdminArea().split(" ");
                            curentGover = govers[0];
                            govetLocation.setText(curentGover);
                        } catch (IOException e) { e.printStackTrace(); }
                    }
                }
                }, Looper.getMainLooper());
        }
    }

    public interface OnDataReceiveCallback { void onDataReceived(ArrayList<GarageInfoModule> garageInfoModules);}

    interface MapSetLocation{
        void onMapSetLocation(LatLng latLng);
    }

    private void setAnimation(){
        Animation animationImage = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        Animation animationView = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        NavigationView navigationView = getActivity().findViewById(R.id.main_nave_view);
        View viewHeaderView = navigationView.getHeaderView(0);
        ImageView imageProfile = viewHeaderView.findViewById(R.id.img_profile);
        View allHeader = navigationView.findViewById(R.id.all_header);
        imageProfile.setAnimation(animationImage);
        allHeader.setAnimation(animationView);

    }
}