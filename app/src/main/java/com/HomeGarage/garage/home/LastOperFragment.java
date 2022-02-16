package com.HomeGarage.garage.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.HomeGarage.garage.DB.AppDataBase;
import com.HomeGarage.garage.DB.DBViewModel;
import com.HomeGarage.garage.DB.Opreation;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.LastOperAdapter;
import com.HomeGarage.garage.home.models.LastOperModels;
import java.util.ArrayList;
import java.util.List;

public class LastOperFragment extends Fragment implements LastOperAdapter.LastOperListener {

    RecyclerView  recyclerAllOper;
    List<Opreation> opreationList;
    AppDataBase dataBase;
    LastOperAdapter lastOperAdapter;
    public LastOperFragment( List<Opreation> opreations) {
        opreationList=opreations;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBase= AppDataBase.getInstance(getContext());
        lastOperAdapter=new LastOperAdapter(getContext(),this,0);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_last_oper, container, false);

        recyclerAllOper = root.findViewById(R.id.recycle_all_oper);
        recyclerAllOper.setLayoutManager(new LinearLayoutManager(getContext() ,RecyclerView.VERTICAL,false ));
        recyclerAllOper.setAdapter(lastOperAdapter);
        setUpViewModel();
        return root;
    }

    @Override
    public void LastOperListener(Opreation opreation) {

        OperationsFragment newFragment = new OperationsFragment(opreation);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void setUpViewModel()
    {
        DBViewModel viewModel=new ViewModelProvider(this).get(DBViewModel.class);
        final LiveData<List<Opreation>> opreations=viewModel.getOpreations();
        opreations.observe(getViewLifecycleOwner(), new Observer<List<Opreation>>() {
            @Override
            public void onChanged(List<Opreation> opreations) {
                lastOperAdapter.setLastOpereations(opreations);
            }
        });
    }
}