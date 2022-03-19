package com.HomeGarage.garage.home.navfragment;

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
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class DialogPurchase extends DialogFragment {

    TextInputEditText amount;
    Button pushece;
    TextView balace;
    DatabaseReference reference;
    float currntBalance;
    Toast toast;
    public DialogPurchase() { }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dialog_purchase, container, false);

        amount = root.findViewById(R.id.amount_puchase);
        pushece = root.findViewById(R.id.btn_pushase_card);
        balace = root.findViewById(R.id.balance_dialog);

        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.castom_toast_layout,root.findViewById(R.id.custom_toast_thank_you));
        toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(view);

        reference = FirebaseUtil.databaseReference.child(Objects.requireNonNull(FirebaseUtil.firebaseAuth.getUid()));

        getBalance(f -> balace.setText(getString(R.string.balance) +" : " +String.format("%.2f",f)  +" E.g"));

        pushece.setOnClickListener(v -> {
            reference.child("balance").setValue(Float.parseFloat(Objects.requireNonNull(amount.getText()).toString())+currntBalance);
            toast.show();
            dismiss();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView,new HomeFragment());
            transaction.commit();

        });
        return root;
    }

    void getBalance(OnBalaceGetCallBack callBack){
      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists()){
                   currntBalance = snapshot.child("balance").getValue(Float.class);
                  callBack.balaceGetCallBack(currntBalance);
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }

    public interface OnBalaceGetCallBack{
        void balaceGetCallBack(Float f);
    }
}