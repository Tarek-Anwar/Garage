package com.HomeGarage.garage.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.HomeGarage.garage.util.DateFormatUtil;
import com.HomeGarage.garage.util.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.PurchaseModule;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class DialogPurchase extends DialogFragment {

    TextInputEditText amount;
    Button pushece;
    TextView balace;

    DatabaseReference referencePurchase = FirebaseUtil.referencePurchase;
    DatabaseReference referenceCar = FirebaseUtil.databaseReference.child(FirebaseUtil.firebaseAuth.getUid());
    DatabaseReference referenceApp = FirebaseDatabase.getInstance().getReference().child("App");

    float currntBalance = -1;
    float balance = -1;
    Toast toast;

    public DialogPurchase() { }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dialog_purchase, container, false);
        initUI(root);

        String poungEg = getActivity().getString(R.string.eg);
        String maxDeposit = getActivity().getString(R.string.max_deposit);
        String negativeMoney = getActivity().getString(R.string.negative_money);

        //custom Toast
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.castom_toast_layout,root.findViewById(R.id.custom_toast_thank_you));
        toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(view);

        getBalance((f, balance) -> {
            balace.setText(String.format("%.2f %s",f , poungEg));
            pushece.setOnClickListener(v -> {
                float amountF = Float.parseFloat(Objects.requireNonNull(amount.getText()).toString());

                if(amountF<0) Toast.makeText(getContext(), negativeMoney, Toast.LENGTH_SHORT).show();
                else if(amountF>10000) Toast.makeText(getContext(), maxDeposit, Toast.LENGTH_SHORT).show();
                else{
                    setPurchase(amountF);

                    referenceCar.child("balance").setValue(amountF + currntBalance);
                    referenceApp.child("Balance").setValue(balance+amountF);

                    toast.show();
                    dismiss();

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStackImmediate();
                }
            });
        });

        return root;
    }

    private void setPurchase(float amountF){
        PurchaseModule opreation = new PurchaseModule();
        Date date = new Date(System.currentTimeMillis());
        String dateOpreation = DateFormatUtil.allDataFormat.format(date);
        opreation.setDate(dateOpreation);
        opreation.setType("2");
        opreation.setFrom(FirebaseUtil.firebaseAuth.getUid());
        opreation.setTo("app");
        opreation.setValue(amountF);
        opreation.setFromName(FirebaseUtil.carInfoModuleLogin.get(0).getName());
        opreation.setToName("app");
        opreation.setId(referencePurchase.push().getKey());
        referencePurchase.child(opreation.getId()).setValue(opreation);
    }
    private void initUI(View root) {
        amount = root.findViewById(R.id.amount_puchase);
        pushece = root.findViewById(R.id.btn_pushase_card);
        balace = root.findViewById(R.id.balance_dialog);
    }

    void getBalance(OnBalaceGetCallBack callBack){
        referenceCar.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists()){
                   currntBalance = snapshot.child("balance").getValue(Float.class);
                   if(balance>=0){
                       callBack.balaceGetCallBack(currntBalance , balance);
                   }
              }
          }
          @Override
          public void onCancelled(@NonNull DatabaseError error) { }
      });
        referenceApp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                     balance  = snapshot.child("Balance").getValue(Float.class);
                    if(currntBalance>=0){
                        callBack.balaceGetCallBack(currntBalance , balance);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public interface OnBalaceGetCallBack{
        void balaceGetCallBack(Float f ,Float balance);
    }
}