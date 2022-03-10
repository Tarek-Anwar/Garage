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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Dialog extends DialogFragment {
    TextView balance,cost;
    TextInputLayout hours;
    Button calc,pay;
    GrageInfo grageInfo;
    public Dialog(GrageInfo grageInfo) {
        this.grageInfo = grageInfo;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
         View root=inflater.inflate(R.layout.pay_dialog,container,false);
         initViews(root);
        DatabaseReference reference=FirebaseUtil.databaseReference.child(FirebaseUtil.currentUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long result= (Long) snapshot.child("Balance").getValue();
                String txt=balance.getText().toString();
                balance.setText(txt+""+result);
                String costTxt=cost.getText().toString();
                calc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int hoursIN=6;
                        int priceForHour=20;
                        int costIN=hoursIN*priceForHour;
                        if(costIN<result)
                        {
                            cost.setVisibility(View.VISIBLE);
                            pay.setVisibility(View.VISIBLE);
                            cost.setText(costTxt+""+costIN);
                            pay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    reference.child("Balance").setValue(result-costIN);
                                    balance.setText(txt+""+(result-costIN));
                                    Toast.makeText(getContext(),"thanks",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                            Toast.makeText(getContext(),"no money ",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


         return root;
    }
    private void initViews(View root)
    {
        balance=root.findViewById(R.id.balanceTV);
        cost=root.findViewById(R.id.cost);
        hours=root.findViewById(R.id.hoursET);
        calc=(Button) root.findViewById(R.id.clacbtn);
        pay=(Button) root.findViewById(R.id.payBtnDialog);
    }

}
