package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.LastOperAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements  LastOperAdapter.LastOperListener {
    public static   final String TAG="rrr";

    ArrayList<Opreation> opreationArrayList=new ArrayList<>();
    RecyclerView  recyclerLast;
    LinearLayout layoutNearFind , layoutAllFind,layoutlast;
    View seeAllOper;
    LastOperAdapter lastOperAdapter;
    ImageView notFind;
    private boolean check = false;

    ArrayList<GrageInfo> grageInfos = FirebaseUtil.allGarage;
    public HomeFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastOperAdapter=new LastOperAdapter(getContext(),this,3);
        insertLastOpreationData();
        if(savedInstanceState==null){
            getAllGarage();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        //find element
        initViews(root);

        recyclerLast.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerLast.setAdapter(lastOperAdapter);

        //put LinearLayoutManager to recyclerFind

        seeAllOper.setOnClickListener(v -> {
            Log.i("tttt",opreationArrayList.size()+"");
                LastOperFragment newFragment = new LastOperFragment(lastOperAdapter.getLastOpereations());
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });

        layoutAllFind.setOnClickListener(v -> {
            SearchFragment newFragment = new SearchFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        layoutNearFind.setOnClickListener(v ->{
            SearchFragment newFragment = new SearchFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        if(check==true){
            if(lastOperAdapter.getLastOpereations().isEmpty()){
                notFind.setVisibility(View.VISIBLE);
                layoutlast.setVisibility(View.GONE);
            }else {
                layoutlast.setVisibility(View.VISIBLE);
                notFind.setVisibility(View.GONE);
            }
        }
        return root;
    }

    private void initViews(View v){
        recyclerLast = v.findViewById(R.id.recycler_last);
        layoutNearFind = v.findViewById(R.id.layout_near_find);
        layoutAllFind = v.findViewById(R.id.layout_all_find);
        seeAllOper = v.findViewById(R.id.see_all_last_oper);
        layoutlast = v.findViewById(R.id.layout_last);
        notFind = v.findViewById(R.id.not_find_img);
    }

    @Override
    public void LastOperListener(Opreation opreation) {
            OperationsFragment newFragment = new OperationsFragment(opreation);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
    }

    public void insertLastOpreationData() {

        Opreation opreation=new Opreation("accept","grage owner","client","15/10/2022 5:30 PM", 3.00f);
        for (int i=0;i<15;i++)
        {
            opreationArrayList.add(opreation);
        }
        lastOperAdapter.setLastOpereations(opreationArrayList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"onDestroyView");
        check=true;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void getAllGarage() {
        grageInfos = FirebaseUtil.allGarage;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GaragerOnwerInfo");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        GrageInfo info = item.getValue(GrageInfo.class);
                        grageInfos.add(info);
                    }
                    MapsFragment mapsFragment =new MapsFragment();
                    FragmentTransaction newTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    newTransaction.add(R.id.fragmentContainerMap,mapsFragment,"newFragmnet");
                    newTransaction.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getGarags(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GaragerOnwerInfo");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("sdfsdfd",snapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}