package com.HomeGarage.garage.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.LastOperAdapter;
import com.HomeGarage.garage.home.models.LastOperModels;
import java.util.ArrayList;

public class LastOperFragment extends Fragment implements LastOperAdapter.LastOperListener {

    RecyclerView  recyclerAllOper;
    ArrayList<LastOperModels> lastOperModels;

    public LastOperFragment( ArrayList<LastOperModels> lastOperModels) {
        this.lastOperModels = lastOperModels;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_last_oper, container, false);

        recyclerAllOper = root.findViewById(R.id.recycle_all_oper);

        recyclerAllOper.setLayoutManager(new LinearLayoutManager(getContext() ,RecyclerView.VERTICAL,false ));
        recyclerAllOper.setAdapter(new LastOperAdapter(lastOperModels,getContext(),this,0));

        return root;
    }

    @Override
    public void LastOperListener(LastOperModels lastOperModels) {

        SearchFragment newFragment = new SearchFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}