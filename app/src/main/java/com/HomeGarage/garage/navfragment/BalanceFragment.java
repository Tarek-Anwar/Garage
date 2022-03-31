package com.HomeGarage.garage.navfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.Adapter.BalanceAdapter;
import com.HomeGarage.garage.R;


public class BalanceFragment extends Fragment {

    RecyclerView recyclerView;
    TextView balanceTxt;
    LinearLayout button;
    float balance;
    public BalanceFragment(){}
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

        recyclerView = root.findViewById(R.id.recycle_pay);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(new BalanceAdapter(getContext()));

        balanceTxt = root.findViewById(R.id.currnet_balace_pay);
        if(savedInstanceState==null){
            balanceTxt.setText("");
        }else {
            balance = savedInstanceState.getFloat("saveBalance");
        }
        balanceTxt.setText(String.format("%.2f",balance)+" " + getString(R.string.eg));

        button =  root.findViewById(R.id.btn_purchase_now);
        button.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView,new PayFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("saveBalance" , balance);
    }
}