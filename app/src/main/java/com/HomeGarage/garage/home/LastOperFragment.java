package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.Adapter.LastOperAdapter;
import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.OpreationModule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class LastOperFragment extends Fragment {

    RecyclerView  recyclerAllOper;
    LastOperAdapter lastOperAdapter;
    TextView textOper;
    boolean aBoolean = false;
    View not_oper , find_oper , see_all_last_oper;
    LinkedList<OpreationModule> lastOpereations = FirebaseUtil.opreationModuleEndList;
    private int count;

    public LastOperFragment(int count) {
       this.count=count;
        if(count==1){
            aBoolean=true;
        }
    }

    public LastOperFragment() { }

    public void setCount(int count) {
        this.count = count;
        if(count==1){
            aBoolean=true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_last_oper, container, false);
        initUI(root);
        if(getActivity()!=null)  see_all_last_oper = getActivity().findViewById(R.id.see_all_last_oper);

        if(aBoolean){
            textOper.setText(R.string.all_opertions);
        }

        getOperationMe(new OnOperReciveCallback() {
            @Override
            public void countOpereCallback(LinkedList<OpreationModule> lastOpereations) {
                lastOperAdapter = new LastOperAdapter(lastOpereations,count, opreation -> {
                    replace(opreation);
                });
                recyclerAllOper.setLayoutManager(new LinearLayoutManager(getContext() ,RecyclerView.VERTICAL,false ));
                recyclerAllOper.setAdapter(lastOperAdapter);
            }

            @Override
            public void isexit(boolean isexit) {
                if(!isexit){
                    find_oper.setVisibility(View.GONE);
                    if(getActivity()!=null) see_all_last_oper.setVisibility(View.GONE);
                    not_oper.setVisibility(View.VISIBLE);
                }else {
                    find_oper.setVisibility(View.VISIBLE);
                    not_oper.setVisibility(View.GONE);
                }
            }
        });

        return root;
    }

    private void initUI(View root){
        recyclerAllOper = root.findViewById(R.id.recycle_all_oper);
        textOper = root.findViewById(R.id.txt_oper_fragment);
        find_oper = root.findViewById(R.id.operaion_latout);
        not_oper = root.findViewById(R.id.not_operation);
    }

    private void replace(OpreationModule opreationModule){
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView,  new OperationsFragment(opreationModule));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getOperationMe(OnOperReciveCallback callback){
        DatabaseReference reference =  FirebaseUtil.referenceOperattion;
        Query query = reference.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.isexit(snapshot.exists());
                if (snapshot.exists()) {
                    lastOpereations.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        OpreationModule opreationModule = snapshot1.getValue(OpreationModule.class);
                        if (opreationModule.getType().equals("5")
                                || (opreationModule.getType().equals("3") || opreationModule.getType().equals("4"))) {
                            lastOpereations.addFirst(opreationModule);
                        }
                    }
                    callback.countOpereCallback(lastOpereations);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    interface OnOperReciveCallback{
        void countOpereCallback(LinkedList<OpreationModule> lastOpereations);
        void isexit(boolean isexit);
    }
}