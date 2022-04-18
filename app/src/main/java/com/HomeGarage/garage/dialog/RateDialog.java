package com.HomeGarage.garage.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RateDialog extends DialogFragment {


    com.chaek.android.RatingBar ratingBar;
    TextView rateText , skip;
    float currnt_rate = -1;
    float rateSum;
    String idGarage , idOperation;
    DatabaseReference garageReference,opreationReference;

    public RateDialog( String idOperation , String idGarage) {
        this.idGarage = idGarage;
        this.idOperation = idOperation;
        opreationReference = FirebaseUtil.referenceOperattion.child(idOperation);
        garageReference = FirebaseUtil.referenceGarage.child(idGarage);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.rate_dialog,container,false);
        initViews(view);

        ratingBar.setRatingBarListener(i -> currnt_rate = i);

        if(currnt_rate<0) {
            addRate((rate, num) -> rateText.setOnClickListener(view1 -> {
                rateSum = rate + currnt_rate;
                garageReference.child("rate").setValue(rateSum);
                garageReference.child("numOfRatings").setValue(num + 1);
                opreationReference.child("rate").setValue(currnt_rate);
                dismiss();
            }));
        }

        skip.setOnClickListener(v -> dismiss());
        return view;
    }

    private void initViews(View view) {
        ratingBar = view.findViewById(R.id.rate);
        rateText = view.findViewById(R.id.addRate);
        skip = view.findViewById(R.id.skip_rate);
    }

    void addRate(Rate rateOBJ) {
        garageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float rateValue=snapshot.child("rate").getValue(Float.class);
                int num=snapshot.child("numOfRatings").getValue(Integer.class);
                rateOBJ.getRateAndNum(rateValue,num);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    public interface Rate { void getRateAndNum(float rate,int num);}
}
