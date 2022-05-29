package com.HomeGarage.garage.ui.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.adapter.CityAdapter;
import com.HomeGarage.garage.modules.CityModule;
import com.HomeGarage.garage.ui.navfragment.SettingFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GoverGarageFragment extends Fragment {

    public final String SAVE_POSATION = "savePosation";
    private final String SAVE_CITY  = "saveCity";
    private final String SAVE_QUERY  = "saveQuery";

    int posationCity;
    String city;
    ArrayList<CityModule> cityList;
    CityAdapter cityAdapter = null;
    SearchView completeText;
    ProgressBar progressBarCity;
    RecyclerView recyclerCity;
    Context context;
    SharedPreferences preferences;
    boolean settingCity;
    String coomSoon;

    public GoverGarageFragment(int posationCity , String city , Context context) {
        this.posationCity = posationCity;
        this.city = city;
        this.context = context;
    }
    public GoverGarageFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_gover_garage, container, false);

        initUI(root);
        coomSoon = context.getString(R.string.coom_soon);
        if(savedInstanceState!=null){
            posationCity = savedInstanceState.getInt(SAVE_POSATION);
            city = savedInstanceState.getString(SAVE_CITY);
        }

        preferences = getActivity().getSharedPreferences(getString(R.string.file_info_user),Context.MODE_PRIVATE);
        settingCity = preferences.getBoolean(SettingFragment.CITY_SETTINNG,true);

        getAllCityInGover(citys -> {
            cityAdapter= new CityAdapter(citys, s -> {
                if(s.getNumberGarage()>0) {
                    replaceFragment(new CityGarageFragment(s.getCity_name_en()));
                }
                else{
                    Toast.makeText(context,context.getString(R.string.coom_soon), Toast.LENGTH_SHORT).show();
                }
            });
            recyclerCity.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
            recyclerCity.setAdapter(cityAdapter);
        });

        completeText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(cityAdapter!=null) cityAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return root;
    }

    private void replaceFragment(CityGarageFragment cityGarageFragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, cityGarageFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void initUI(View root) {
        completeText = root.findViewById(R.id.search_city);
        recyclerCity = root.findViewById(R.id.recycle_city);
        progressBarCity = root.findViewById(R.id.progress_bar_city);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_CITY,city);
        outState.putInt(SAVE_POSATION,posationCity);
    }

    private void getAllCityInGover(AllCityInGoverCallback callback){
        cityList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cities");
        Query query =  reference.orderByChild("governorate_id").equalTo((posationCity+1)+"");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progressBarCity.setVisibility(View.VISIBLE);
                    cityList.clear();
                    for (DataSnapshot item : snapshot.getChildren()){
                       CityModule cityModule = item.getValue(CityModule.class);
                       if(settingCity) {if(cityModule.getNumberGarage()>0) cityList.add(cityModule);}
                       else cityList.add(cityModule);
                    }
                    progressBarCity.setVisibility(View.GONE);
                    callback.onAllCityInGoverCallback(cityList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    interface AllCityInGoverCallback{
        void  onAllCityInGoverCallback(ArrayList<CityModule> citys);
    }
}