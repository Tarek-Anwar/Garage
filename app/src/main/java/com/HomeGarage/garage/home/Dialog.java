package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.CarInfo;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Dialog extends DialogFragment {

    TextView balance, cost;
    Button calc, pay;
    GrageInfo grageInfo;
    DatabaseReference referenceOperattion = FirebaseUtil.referenceOperattion;
    ArrayList<Opreation> opreations = FirebaseUtil.opreationEndList;

    SimpleDateFormat formatterLong = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", new Locale("en"));

    DatabaseReference reference;
    DatabaseReference garageReference = FirebaseDatabase.getInstance().getReference().child("GaragerOnwerInfo");
    DatabaseReference appReference = FirebaseDatabase.getInstance().getReference().child("App");

    float  costIN;
    CarInfo carInfo;


    public Dialog(GrageInfo grageInfo) {
        this.grageInfo = grageInfo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //init views
        View root = inflater.inflate(R.layout.pay_dialog, container, false);
        initViews(root);

        String txt = balance.getText().toString();
        String costTxt = cost.getText().toString();

        getCarInfo(new OnBalanceReciveCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void OnBalanceRecive(CarInfo carInfo) {
                calc.setOnClickListener(v -> {
                    //calc price
                    costIN = 6 * grageInfo.getPriceForHour();

                    if (costIN < carInfo.getBalance()) {
                        balance.setText(txt + "" + carInfo.getBalance());
                        cost.setVisibility(View.VISIBLE);
                        pay.setVisibility(View.VISIBLE);
                        cost.setText(costTxt + " " + costIN);
                    }
                });
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void OnBalanceAppRecive(Float balanceApp) {
                pay.setOnClickListener(v -> {
                    //update car owner balance
                    carInfo.setBalance(carInfo.getBalance() - costIN);

                    reference.child("balance").setValue(carInfo.getBalance());
                    balance.setText(txt + "" + carInfo.getBalance());

                    //calc app and grage balance
                    float appBalance = (float) (costIN * .1);
                    float grageBalance = costIN - appBalance;

                    //update balance for grage owner
                    grageInfo.setBalance(grageInfo.getBalance() + grageBalance);
                    garageReference.child(grageInfo.getId()).child("balance").setValue(grageInfo.getBalance());

                    //update app balance
                    appReference.child("Balance").setValue(appBalance+balanceApp);

                    //creat opreation and save to last opreation list
                    Opreation opreation = new Opreation();
                    Date date = new Date(System.currentTimeMillis());
                    String dateOpreation = formatterLong.format(date);
                    opreation.setDate(dateOpreation);
                    opreation.setState("3");
                    opreation.setType("4");
                    opreation.setFrom(FirebaseUtil.firebaseAuth.getUid());
                    opreation.setTo(grageInfo.getId());
                    opreation.setPrice(costIN);
                    opreation.setFromName(FirebaseUtil.carInfoLogin.get(0).getName());
                    opreation.setToName(grageInfo.getNameEn());
                    opreation.setId(referenceOperattion.push().getKey());
                    referenceOperattion.child(opreation.getId()).setValue(opreation);

                    opreations.add(opreation);
                    Toast.makeText(getContext(), "thanks", Toast.LENGTH_SHORT).show();
                });

            }
        });

        return root;
    }

    private void initViews(View root) {
        balance = root.findViewById(R.id.balanceTV);
        cost = root.findViewById(R.id.cost);
        calc = root.findViewById(R.id.clacbtn);
        pay = root.findViewById(R.id.payBtnDialog);
    }

    void getCarInfo(OnBalanceReciveCallback callback){
        appReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float balance  = snapshot.child("Balance").getValue(Float.class);
                callback.OnBalanceAppRecive(balance);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        reference = FirebaseUtil.databaseReference.child(FirebaseUtil.firebaseAuth.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carInfo = snapshot.getValue(CarInfo.class);
                callback.OnBalanceRecive(carInfo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private interface OnBalanceReciveCallback{
        void OnBalanceRecive(CarInfo carInfo);
        void OnBalanceAppRecive(Float balance);
    }

}
