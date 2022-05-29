package com.HomeGarage.garage.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.adapter.LastOperAdapter;
import com.HomeGarage.garage.modules.OpreationModule;
import com.HomeGarage.garage.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class LastOperFragment extends Fragment {

    private RecyclerView  recyclerAllOper;
    private TextView textOperation;
    private ProgressBar progressBarLastOper;
    private View notOperation ;
    private View findOperation ;
    private View seeLastOperation;
    private LinkedList<OpreationModule> lastOpereations = FirebaseUtil.opreationModuleEndList;
    private int count;

    public LastOperFragment(int count) {
       this.count=count;
    }

    public LastOperFragment() { }

    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_last_oper, container, false);
        initUI(root);

        if(checkActivity(requireActivity())) {
            seeLastOperation = requireActivity().findViewById(R.id.see_all_last_oper);
            if(count!= 3) textOperation.setText(R.string.all_opertions);
        }

        getOperationMe(new OnOperReciveCallback() {
            @Override
            public void countOpereCallback(LinkedList<OpreationModule> lastOpereations) {
                LastOperAdapter lastOperAdapter = new LastOperAdapter(lastOpereations,count, LastOperFragment.this::replace);
                recyclerAllOper.setLayoutManager(new LinearLayoutManager(getContext() ,RecyclerView.VERTICAL,false ));
                recyclerAllOper.setAdapter(lastOperAdapter);
            }

            @Override
            public void isExit(boolean isexit) {
                if(!isexit){
                    findOperation.setVisibility(View.GONE);
                    if(checkActivity(requireActivity())) seeLastOperation.setVisibility(View.GONE);
                    notOperation.setVisibility(View.VISIBLE);
                }else{
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
        Query query = FirebaseUtil.referenceOperattion.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.isExit(snapshot.exists());
                if (snapshot.exists()) {
                    progressBarLastOper.setVisibility(View.VISIBLE);
                    lastOpereations.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        OpreationModule opreationModule = snapshot1.getValue(OpreationModule.class);
                        assert opreationModule != null;
                        if (checkOpreation(opreationModule)) {
                            lastOpereations.addFirst(opreationModule);
                        }
                    }
                    progressBarLastOper.setVisibility(View.GONE);
                    callback.countOpereCallback(lastOpereations);
                }
            }
            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw new DatabaseException(error.getMessage());
            }
        });
    }

    private boolean checkOpreation(OpreationModule module){
        return  module.getState().equals("3") ;
    }
    private boolean checkActivity(FragmentActivity activity){
        return !activity.isDestroyed() ;
    }
    interface OnOperReciveCallback{
        void countOpereCallback(LinkedList<OpreationModule> lastOpereations);
        void isExit(boolean isexit);
    }
}