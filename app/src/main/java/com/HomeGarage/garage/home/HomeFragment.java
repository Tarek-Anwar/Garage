package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.HomeGarage.garage.home.navfragment.OnSwipeTouchListener;
import com.HomeGarage.garage.home.search.SearchFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements GovernorateAdapter.GoverListener  {

    DrawerLayout drawerLayout;
    RecyclerView   recyclerGover ;
    LinearLayout  layoutlast;
    View seeAllOper , locationGet;
    GovernorateAdapter governorateAdapter;
    public static LatLng curentLocation = null;
    public static String curentGover = null;
    ArrayList <GrageInfo> grageInfos = null;

    MapsFragment mapsFragment;

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

        mapsFragment = new MapsFragment();
        FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
        transaction2.replace(R.id.fragmentContainerMap,mapsFragment);
        transaction2.commit();

        FragmentTransaction transaction3 = getActivity().getSupportFragmentManager().beginTransaction();
        transaction3.replace(R.id.fragmentContainerLastOper,new LastOperFragment(3));
        transaction3.commit();

        root.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            public void onSwipeRight() {
                drawerLayout.open();
            }
        });

        getAllGarage(grages -> {
            if(curentLocation!=null){
                mapsFragment.setLocationMe(curentLocation);
            }
            if(grageInfos!=null){
               mapsFragment.setMarkers(grageInfos);
            }
        });

        governorateAdapter = new GovernorateAdapter(this::onGoverListener);
        recyclerGover.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        recyclerGover.setAdapter(governorateAdapter);

        seeAllOper.setOnClickListener(v -> replaceFragment(new LastOperFragment(0)));

        if(curentLocation!=null){
           locationGet.setVisibility(View.GONE);
        }
        locationGet.setOnClickListener(v -> replaceFragment(new LocationGetFragment()));

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
        seeAllOper = v.findViewById(R.id.see_all_last_oper);
        layoutlast = v.findViewById(R.id.layout_last);
        recyclerGover = v.findViewById(R.id.recycle_gover);
        locationGet = v.findViewById(R.id.get_location);
    }

    public interface OnDataReceiveCallback { void onDataReceived(ArrayList<GrageInfo> grageInfos);}

    private void getAllGarage(OnDataReceiveCallback callback) {
        grageInfos = new ArrayList<>();
        DatabaseReference ref = FirebaseUtil.referenceGarage;
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