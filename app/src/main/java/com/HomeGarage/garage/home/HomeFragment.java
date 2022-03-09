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
import com.HomeGarage.garage.home.Adapter.OperRequstAdapter;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.LastOperAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements  LastOperAdapter.LastOperListener {

    ArrayList<Opreation> lastOperList = FirebaseUtil.opreationEndList;
    LastOperAdapter lastOperAdapter;

    RecyclerView  recyclerLast , recyclerReqsut;
    LinearLayout layoutNearFind , layoutAllFind , layoutlast;
    View seeAllOper;
    ImageView notFind;

    private boolean check = false;
    public HomeFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       lastOperAdapter=new LastOperAdapter(lastOperList,this,3);
        if(savedInstanceState == null){
            getRequst();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        //find element
        initViews(root);

        recyclerReqsut.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL,false));
        recyclerReqsut.setAdapter(new OperRequstAdapter());

        recyclerLast.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerLast.setAdapter(lastOperAdapter);

        seeAllOper.setOnClickListener(v -> {
                LastOperFragment newFragment = new LastOperFragment();
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
            if(lastOperList.isEmpty()){
                notFind.setVisibility(View.VISIBLE);
                layoutlast.setVisibility(View.GONE);
            }else {
                layoutlast.setVisibility(View.VISIBLE);
                notFind.setVisibility(View.GONE);
            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        check=true; }

    public  void getRequst(){
        ArrayList<Opreation> opreationsReq = FirebaseUtil.opreationRequstList;
        ArrayList<Opreation> opreationsEnd = FirebaseUtil.opreationEndList;
        DatabaseReference reference = FirebaseUtil.referenceOperattion;
        Query query = reference.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Opreation opreation = snapshot1.getValue(Opreation.class);
                        if (opreation.getState().equals("3") && (opreation.getType().equals("3") || opreation.getType().equals("4"))) {
                            opreationsEnd.add(opreation);
                            Log.i("sryuivxcvxc" , "State 3  :  " + opreation .getId());
                        }
                        else{
                            opreationsReq.add(opreation);
                            Log.i("sryuivxcvxc" , "State 1 , 2  :  " + opreation .getId());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void LastOperListener(Opreation opreation) {
        OperationsFragment newFragment = new OperationsFragment(opreation);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initViews(View v){
        recyclerLast = v.findViewById(R.id.recycler_last);
        layoutNearFind = v.findViewById(R.id.layout_near_find);
        layoutAllFind = v.findViewById(R.id.layout_all_find);
        seeAllOper = v.findViewById(R.id.see_all_last_oper);
        layoutlast = v.findViewById(R.id.layout_last);
        notFind = v.findViewById(R.id.not_find_img);
        recyclerReqsut = v.findViewById(R.id.recycle_requst);
    }

}