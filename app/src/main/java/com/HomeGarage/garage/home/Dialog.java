package com.HomeGarage.garage.home;

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

        //get current user balance
        DatabaseReference reference = FirebaseUtil.databaseReference.child(FirebaseUtil.currentUser.getUid());
        reference.child("Balance").setValue(5000);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long result = (long) snapshot.child("Balance").getValue();
                balance.setText(txt + "" + result);

                calc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //calc price
                        long hoursIN = 6;
                        long priceForHour = (long) grageInfo.getPriceForHour();
                        long costIN = hoursIN * priceForHour;
                        if (costIN < result) {
                            cost.setVisibility(View.VISIBLE);
                            pay.setVisibility(View.VISIBLE);
                            cost.setText(costTxt + "" + costIN);
                            pay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    //update car owner balance
                                    long newBalance = result - costIN;
                                    reference.child("Balance").setValue(newBalance);
                                    balance.setText(txt + "" + (newBalance));

                                    //calc app and grage balance
                                    long appBalance = (long) (costIN * .1);
                                    long grageBalance = costIN - appBalance;

                                    //update balance for grage owner
                                    grageInfo.setBalance(grageInfo.getBalance() + grageBalance);
                                    DatabaseReference garageReference = FirebaseDatabase.getInstance().getReference().child("GaragerOnwerInfo");
                                    garageReference.child(grageInfo.getId()).child("Balance").setValue(grageBalance);

                                    //update app balance
                                    DatabaseReference appReference = FirebaseDatabase.getInstance().getReference().child("App");
                                    appReference.child("Balance").setValue(appBalance);

                                    //creat opreation and save to last opreation list
                                    Opreation opreation = new Opreation();
                                    Date date = new Date(System.currentTimeMillis());
                                    String dateOpreation = formatterLong.format(date);
                                    opreation.setDate(dateOpreation);
                                    opreation.setState("3");
                                    opreation.setType("4");
                                    opreation.setFrom(FirebaseUtil.firebaseAuth.getUid());
                                    opreation.setTo(grageInfo.getId());
                                    opreation.setFromName(snapshot.child("Full Name").getValue(String.class));
                                    opreation.setToName(grageInfo.getNameEn());
                                    opreation.setId(referenceOperattion.push().getKey());
                                    referenceOperattion.child(opreation.getId()).setValue(opreation);

                                    opreations.add(opreation);


                                    Toast.makeText(getContext(), "thanks", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else
                            Toast.makeText(getContext(), "no money ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return root;
    }

    private void initViews(View root) {
        balance = root.findViewById(R.id.balanceTV);
        cost = root.findViewById(R.id.cost);
        calc = (Button) root.findViewById(R.id.clacbtn);
        pay = (Button) root.findViewById(R.id.payBtnDialog);

    }

}
