package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.SearchAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchAdapter.SearchListener {

    private final String TAG="search tag";


    FirebaseDatabase database;
    DatabaseReference reference;

    RadioGroup group;SearchView searchView;

    RecyclerView recyclerSearch;
    SearchAdapter searchAdapter;
    ArrayList<GrageInfo> grageInfos=new ArrayList<>();
    public SearchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchAdapter=new SearchAdapter(getContext(),this);
       database= FirebaseUtil.firebaseDatabase;
       reference=database.getReference().child("GaragerOnwerInfo");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_search, container, false);

        initViews(root);
        // set adapter
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerSearch.setAdapter(searchAdapter);


        getChecked();
        return root;
    }


    private void search(String s, String searchBy) {
        Query query=reference.orderByChild(searchBy).equalTo(s);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        GrageInfo grage = dataSnapshot.getValue(GrageInfo.class);
                        if (!(grageInfos.contains(grage))) {
                            grageInfos.add(grage);
                        }
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        searchAdapter.setGrageInfos(grageInfos);
    }

    private void initViews(View root)
    {
        searchView=(SearchView) root.findViewById(R.id.searchGrages);
        group=(RadioGroup) root.findViewById(R.id.searchRG);
        recyclerSearch = (RecyclerView) root.findViewById(R.id.recycle_search);
    }
   /* public void insertGrages() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        GrageInfo grage=dataSnapshot.getValue(GrageInfo.class);
                        grageInfos.add(grage);
                    }
                    searchAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                                            }
        });

                searchAdapter.setGrageInfos(grageInfos);
    }*/
    private void getChecked()
    {
        group.clearCheck();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radio=group.findViewById(i);
                switch (i) {
                    case R.id.nameRB: {
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                Log.d(TAG,newText);
                                search(newText,"nameEn");
                                return true;
                            }
                        });
                        break;
                    }

                    case R.id.govRB: {
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                search(newText,"governoateEn");
                                return true;
                            }
                        });
                        break;
                    }
                    case R.id.cityRB: {
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                search(newText,"cityEn");
                                return true;
                            }
                        });
                        break;
                    }
                }
            }
        });
    }
    @Override
    public void SearchListener(GrageInfo grageInfo) {
        GarageViewFragment newFragment = new GarageViewFragment(grageInfo);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}