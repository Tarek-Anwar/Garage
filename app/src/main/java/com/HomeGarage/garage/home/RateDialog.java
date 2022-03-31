package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.models.GrageInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RateDialog extends DialogFragment {

    GrageInfo grageInfo;
    RatingBar ratingBar;
    Button rateBtn;
    float currnt_rate;
    float rateSum;

    DatabaseReference garageReference,opreationReference;

    public RateDialog(GrageInfo grageInfo,DatabaseReference opreationReference) {
        this.grageInfo = grageInfo;
        this.opreationReference=opreationReference;
        garageReference = FirebaseUtil.referenceGarage.child(grageInfo.getId());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.rate_dialog,container,false);
        initViews(view);

        ratingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> currnt_rate = v);

        addRate((rate, num) -> rateBtn.setOnClickListener(view1 -> {
            rateSum=rate+currnt_rate;
            grageInfo.setRate(rateSum);
            grageInfo.setNumOfRatings(num+1);
            garageReference.child("rate").setValue(rateSum);
            garageReference.child("numOfRatings").setValue(num+1);
            opreationReference.child("rate").setValue(currnt_rate);
            Toast.makeText(getContext(), rateSum+"", Toast.LENGTH_SHORT).show();
            dismiss();
        }));

        return view;
    }

    private void initViews(View view) {
        ratingBar=(RatingBar) view.findViewById(R.id.rate);
        rateBtn=(Button) view.findViewById(R.id.addRate);
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
