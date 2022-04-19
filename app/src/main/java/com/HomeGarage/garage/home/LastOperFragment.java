package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
    TextView textOperation;
    ProgressBar progressBarLastOper;
    boolean aBoolean = false;
    View notOperation , findOperation , seeLastOperation;
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
        if(getActivity()!=null)  textOperation = getActivity().findViewById(R.id.see_all_last_oper);

        if(aBoolean) textOperation.setText(R.string.all_opertions);

        getOperationMe(new OnOperReciveCallback() {
            @Override
            public void countOpereCallback(LinkedList<OpreationModule> lastOpereations) {
                lastOperAdapter = new LastOperAdapter(lastOpereations,count, opreation -> replace(opreation));
                recyclerAllOper.setLayoutManager(new LinearLayoutManager(getContext() ,RecyclerView.VERTICAL,false ));
                recyclerAllOper.setAdapter(lastOperAdapter);
            }
            @Override
            public void isexit(boolean isexit) {
                if(!isexit){
                    findOperation.setVisibility(View.GONE);
                    if(getActivity()!=null) seeLastOperation.setVisibility(View.GONE);
                    notOperation.setVisibility(View.VISIBLE);
                }else {
                    findOperation.setVisibility(View.VISIBLE);
                    notOperation.setVisibility(View.GONE);
                }
            }
        });

        return root;
    }

    private void initUI(View root){
        recyclerAllOper = root.findViewById(R.id.recycle_all_oper);
        textOperation = root.findViewById(R.id.txt_oper_fragment);
        findOperation = root.findViewById(R.id.operaion_latout);
        notOperation = root.findViewById(R.id.not_operation);
        progressBarLastOper = root.findViewById(R.id.progress_bar_last_oper);
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
                    progressBarLastOper.setVisibility(View.VISIBLE);
                    lastOpereations.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        OpreationModule opreationModule = snapshot1.getValue(OpreationModule.class);
                        if (opreationModule.getType().equals("5")
                                || (opreationModule.getType().equals("3") || opreationModule.getType().equals("4"))) {
                            lastOpereations.addFirst(opreationModule);
                        }
                    }
                    progressBarLastOper.setVisibility(View.GONE);
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