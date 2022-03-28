package com.HomeGarage.garage.home;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.GovernorateAdapter;
import com.HomeGarage.garage.home.location.GoverGarageFragment;
import com.HomeGarage.garage.home.location.LocationGetFragment;
import com.HomeGarage.garage.home.models.CityModel;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.navfragment.OnSwipeTouchListener;
import com.HomeGarage.garage.setting.SettingFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFragment extends Fragment implements GovernorateAdapter.GoverListener  {

    private final int locationRequestCode = 1;
    public static LatLng curentLocation = null;
    public static String curentGover = null;

    private DrawerLayout drawerLayout;
    private RecyclerView   recyclerGover ;
    private LinearLayout  layoutlast;
    private View seeAllOper , locationGet;
    private GovernorateAdapter governorateAdapter;
    private TextView govetLocation;
    private MapsFragment mapsFragment;
    private FragmentContainerView fragmentContainer;
    private String me , here;
    private SharedPreferences preferences ;
    private ActivityResultLauncher<Object> launcher;
    private Geocoder geocoder;


    public HomeFragment(){ }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //define UI
        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        initViews(root);
        drawerLayout = getActivity().findViewById(R.id.drawer_layout);

        //swaip
        root.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight() {
                drawerLayout.open(); } });

        me = getActivity().getString(R.string.me_location);here = getActivity().getString(R.string.i_here);
        mapsFragment = new MapsFragment();
        mapsFragment.setTitle(me,here);

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
                    getCurrantLoaction(latLng -> {
                        if(latLng!=null ) setMapsFragment(mapsFragment);
                    });
                }else { Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show(); }
            });
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            getCurrantLoaction(latLng -> {
                if(latLng!=null) setMapsFragment(mapsFragment);

            });

        }else {
            setMapsFragment(mapsFragment);
            if(curentLocation!=null){
                mapsFragment.setLocationMe(curentLocation);
                mapsFragment.setTitle(me,here);
                govetLocation.setText(curentGover);
                locationGet.setVisibility(View.GONE);
            }else locationGet.setOnClickListener(v -> replaceFragment(new LocationGetFragment()));
        }

        //last Operation
        FragmentTransaction transaction3 = getActivity().getSupportFragmentManager().beginTransaction();
        transaction3.replace(R.id.fragmentContainerLastOper,new LastOperFragment(3));
        transaction3.commit();
        seeAllOper.setOnClickListener(v -> replaceFragment(new LastOperFragment(0)));

        //Gover item
        governorateAdapter = new GovernorateAdapter(this::onGoverListener);
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
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initViews(View v){
        seeAllOper = v.findViewById(R.id.see_all_last_oper);
        layoutlast = v.findViewById(R.id.layout_last);
        recyclerGover = v.findViewById(R.id.recycle_gover);
        locationGet = v.findViewById(R.id.get_location);
        govetLocation = v.findViewById(R.id.txt_govet_location);
        fragmentContainer = v.findViewById(R.id.fragmentContainerMap);
    }

    public interface OnDataReceiveCallback { void onDataReceived(ArrayList<GrageInfo> grageInfos);}


    private void setMapsFragment(MapsFragment mapsFragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerMap,mapsFragment);
        transaction.commit();
    }

    private void enableLoaction(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    private void getCurrantLoaction(MapSetLocation setLocation) {
        LocationRequest locationRequest =  LocationRequest.create();
        locationRequest.setInterval(100000);
        locationRequest.setFastestInterval(3000);
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
                    if(locationResult.getLocations().size() > 0){
                        int i = locationResult.getLocations().size()-1;
                        double longitude = locationResult.getLocations().get(i).getLongitude();
                        double latitude = locationResult.getLocations().get(i).getLatitude();
                        curentLocation = new LatLng(latitude,longitude);
                        setLocation.onMapSetLocation(curentLocation);
                        mapsFragment.setLocationMe(curentLocation);
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                            Address address = addresses.get(0);
                            String [] govers = address.getAdminArea().split(" ");
                            curentGover = govers[0];
                            govetLocation.setText(curentGover);
                        } catch (IOException e) { e.printStackTrace(); }
                    } }}, Looper.getMainLooper());
        }
    }

    interface MapSetLocation{
        void onMapSetLocation(LatLng latLng);
    }
}