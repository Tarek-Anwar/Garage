package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.LastOperAdapter;

import java.util.ArrayList;
import java.util.List;

public class LastOperFragment extends Fragment implements LastOperAdapter.LastOperListener {

    RecyclerView  recyclerAllOper;
    ArrayList<Opreation> opreations=new ArrayList<>();
    LastOperAdapter lastOperAdapter;

    public LastOperFragment(ArrayList<Opreation> opreations) {
        this.opreations=opreations;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lastOperAdapter=new LastOperAdapter(getContext(),this,0);
        lastOperAdapter.setLastOpereations(opreations);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_last_oper, container, false);

        recyclerAllOper = root.findViewById(R.id.recycle_all_oper);
        recyclerAllOper.setLayoutManager(new LinearLayoutManager(getContext() ,RecyclerView.VERTICAL,false ));
        recyclerAllOper.setAdapter(lastOperAdapter);


        return root;
    }

    @Override
    public void LastOperListener(Opreation opreation) {
        OperationsFragment newFragment = new OperationsFragment(opreation);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}