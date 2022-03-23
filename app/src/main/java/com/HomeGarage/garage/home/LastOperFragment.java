package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.LastOperAdapter;
import com.HomeGarage.garage.home.models.Opreation;

public class LastOperFragment extends Fragment implements LastOperAdapter.LastOperListener {

    RecyclerView  recyclerAllOper;
    LastOperAdapter lastOperAdapter;
    TextView textOper;
    volatile int count;
    boolean aBoolean = false;

    public LastOperFragment(int count) {
       this.count=count;
        lastOperAdapter = new LastOperAdapter(this,count);
        if(count==0){
            aBoolean=true;
        }
    }

    public LastOperFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_last_oper, container, false);
        textOper = root.findViewById(R.id.txt_oper_fragment);

        updateUI(count, count -> {
            recyclerAllOper = root.findViewById(R.id.recycle_all_oper);
            recyclerAllOper.setLayoutManager(new LinearLayoutManager(getContext() ,RecyclerView.VERTICAL,false ));
            recyclerAllOper.setAdapter(lastOperAdapter);
            if(aBoolean){
                textOper.setText(R.string.all_opertions);
            }
        });

        return root;
    }

    @Override
    public void LastOperListener(Opreation opreation) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView,  new OperationsFragment(opreation));
        transaction.addToBackStack(null);
        transaction.commit();
    }
    interface OnCountReciveCallback{
        void countReciveCallback(int count);
    }
    void updateUI(int count,OnCountReciveCallback callback){
        callback.countReciveCallback(count);
    }
}