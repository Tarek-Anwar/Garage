package com.HomeGarage.garage.home;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.GovernorateAdapter;
import com.HomeGarage.garage.home.Adapter.LastOperAdapter;
import com.HomeGarage.garage.home.location.GoverGarageFragment;
import com.HomeGarage.garage.home.location.LocationGetFragment;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.navfragment.DialogPurchase;
import com.HomeGarage.garage.home.navfragment.OnSwipeTouchListener;
import com.HomeGarage.garage.home.search.SearchFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements GovernorateAdapter.GoverListener  {

    DrawerLayout drawerLayout;
    RecyclerView  recyclerLast , recyclerGover ;
    LinearLayout layoutNearFind , layoutAllFind , layoutlast;
    View seeAllOper;
    ImageView find_location;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LastOperAdapter lastOperAdapter;
    GovernorateAdapter governorateAdapter;
    public static LatLng curentLocation = null;
    public static String curentGover = null;

    MapsFragment mapsFragment;

    public HomeFragment(){ }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState==null){
            mapsFragment = new MapsFragment();
            FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
            transaction2.replace(R.id.fragmentContainerMap,mapsFragment);
            transaction2.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        initViews(root);
        drawerLayout = getActivity().findViewById(R.id.drawer_layout);

        root.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight() {
                drawerLayout.open();
            }
        });

        getAllGarage(grageInfos -> {
            if(curentLocation!=null){
                mapsFragment.setLocationMe(curentLocation);
            }
            mapsFragment.setMarkers(grageInfos);
        });


        lastOperAdapter = new LastOperAdapter(opreation -> {
            OperationsFragment newFragment = new OperationsFragment(opreation);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }, 3);

        recyclerLast.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerLast.setAdapter(lastOperAdapter);

        governorateAdapter = new GovernorateAdapter(this::onGoverListener);
        recyclerGover.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        recyclerGover.setAdapter(governorateAdapter);

        seeAllOper.setOnClickListener(v -> replaceFragment(new LastOperFragment()));

        layoutAllFind.setOnClickListener(v -> replaceFragment(new SearchFragment()));

        layoutNearFind.setOnClickListener(v -> replaceFragment(new SearchFragment()));

        if(curentLocation!=null){
            find_location.setVisibility(View.GONE);
        }
        find_location.setOnClickListener(v -> replaceFragment(new LocationGetFragment()));

        return root;
    }


    @Override
    public void onGoverListener(int pos , String s) {
        GoverGarageFragment fragment = new GoverGarageFragment(pos , s);
        replaceFragment(fragment);
    }


    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void initViews(View v){
        recyclerLast = v.findViewById(R.id.recycler_last);
        layoutNearFind = v.findViewById(R.id.layout_near_find);
        layoutAllFind = v.findViewById(R.id.layout_all_find);
        seeAllOper = v.findViewById(R.id.see_all_last_oper);
        layoutlast = v.findViewById(R.id.layout_last);
        recyclerGover = v.findViewById(R.id.recycle_gover);
        find_location  = v.findViewById(R.id.find_locatin);
    }

    public interface OnDataReceiveCallback {
        void onDataReceived(ArrayList<GrageInfo> grageInfos);
    }

    private void getAllGarage(OnDataReceiveCallback callback) {
        ArrayList <GrageInfo> grageInfos = new ArrayList<>();
        DatabaseReference ref = FirebaseUtil.referenceGarage;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        GrageInfo info = item.getValue(GrageInfo.class);
                        grageInfos.add(info);
                        Log.i("dasdasd", info.getId());
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