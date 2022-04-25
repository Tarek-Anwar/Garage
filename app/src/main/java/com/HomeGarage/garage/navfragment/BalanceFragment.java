package com.HomeGarage.garage.navfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.adapter.BalanceAdapter;
import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.PurchaseModule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class BalanceFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textBalance;
    private LinearLayout buttonPurchase;
    float balance;
    private ProgressBar progressBarBalance;
    DatabaseReference referencePurchase;
    LinkedList<PurchaseModule> purchaseModules;

    public BalanceFragment(){}
    public BalanceFragment(float balance) {
        this.balance = balance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        referencePurchase = FirebaseUtil.referencePurchase;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_balance, container, false);
        initUI(root);

        if(savedInstanceState==null) textBalance.setText("");
        else balance = savedInstanceState.getFloat("saveBalance");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        getAllPurchase(purchaseModules -> recyclerView.setAdapter(new BalanceAdapter(getContext(),purchaseModules)));

        String poundEg = getString(R.string.eg);
        textBalance.setText(String.format("%.2f %s",balance , poundEg));

        buttonPurchase.setOnClickListener(v -> replaceFragment(new PayFragment()));

        return root;
    }

    private void replaceFragment(PayFragment payFragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView,payFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initUI(View root) {
        recyclerView = root.findViewById(R.id.recycle_pay);
        buttonPurchase =  root.findViewById(R.id.btn_purchase_now);
        progressBarBalance = root.findViewById(R.id.progressBar_balance);
        textBalance = root.findViewById(R.id.currnet_balace_pay);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("saveBalance" , balance);
    }

    private void getAllPurchase(OnPurchaseGetCallBack callBack){
        purchaseModules = new LinkedList<>();
        Query query =  referencePurchase.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    purchaseModules.clear();
                    for (DataSnapshot item : snapshot.getChildren()){
                        progressBarBalance.setVisibility(View.VISIBLE);
                        PurchaseModule model = item.getValue(PurchaseModule.class);
                        purchaseModules.addFirst(model);
                    }
                    callBack.getPurchases(purchaseModules);
                    progressBarBalance.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private interface OnPurchaseGetCallBack{
        void getPurchases(LinkedList<PurchaseModule> purchaseModules);
    }
}