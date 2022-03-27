package com.HomeGarage.garage.home.location;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.CityAdapter;
import com.HomeGarage.garage.home.HomeFragment;
import com.HomeGarage.garage.home.MapsFragment;
import com.HomeGarage.garage.home.models.CityModel;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


public class GoverGarageFragment extends Fragment {

    int pos;
    String gover;
    ArrayAdapter<String> cityAdapterAuto;
    ArrayList<String> listAdapter;
    ArrayList<CityModel> cityList;
    CityAdapter cityAdapter;
    AutoCompleteTextView completeText;
    RecyclerView recyclerCity;
    Context context;

    public GoverGarageFragment(int pos , String gover , Context context) {
        this.pos = pos;
        this.gover = gover;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_gover_garage, container, false);
        completeText = root.findViewById(R.id.search_city);
        recyclerCity = root.findViewById(R.id.recycle_city);

        getAllCityInGover(citys -> {
            cityAdapter= new CityAdapter(citys, s -> {
                if(s.getNumberGarage()>0){
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, new CityGarageFragment(s.getCity_name_en()));
                transaction.addToBackStack(null);
                transaction.commit();}
                else {
                    Toast.makeText(context, "There are no garages", Toast.LENGTH_SHORT).show();
                }
            });
            recyclerCity.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
            recyclerCity.setAdapter(cityAdapter);

            cityAdapterAuto = new ArrayAdapter<>(context,R.layout.item_auto_complet_row,listAdapter);
            cityAdapterAuto.notifyDataSetChanged();
            completeText.setAdapter(cityAdapterAuto);
            completeText.setThreshold(1);
        });

        return root;
    }


    private void getAllCityInGover(AllCityInGoverCallback callback){
        cityList = new ArrayList<>();
        listAdapter = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cities");
        Query query =  reference.orderByChild("governorate_id").equalTo((pos+1)+"");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot item : snapshot.getChildren()){
                       CityModel cityModel = item.getValue(CityModel.class);
                       listAdapter.add(cityModel.getCity_name_en());
                       cityList.add(cityModel);
                    }
                    callback.onAllCityInGoverCallback(cityList);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    interface AllCityInGoverCallback{
        void  onAllCityInGoverCallback(ArrayList<CityModel> citys);

    }
}