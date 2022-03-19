package com.HomeGarage.garage.home.navfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.BalanceAdapter;


public class BalanceFragment extends Fragment {


    float balance;

    public BalanceFragment(float balance) {
        this.balance = balance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_balance, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycle_pay);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(new BalanceAdapter(getContext()));

        TextView balanceTxt = root.findViewById(R.id.currnet_balace_pay);
        balanceTxt.setText(String.format("%.2f",balance)+" " + getString(R.string.eg));

         root.findViewById(R.id.btn_purchase_now).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                 transaction.replace(R.id.fragmentContainerView,new PayFragment());
                 transaction.addToBackStack(null);
                 transaction.commit();
             }
         });
        return root;
    }
}