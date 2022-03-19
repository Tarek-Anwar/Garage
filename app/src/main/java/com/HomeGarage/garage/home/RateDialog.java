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
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateDialog extends DialogFragment {
    Opreation opreation;
    GrageInfo grageInfo;
    RatingBar ratingBar;
    Button rateBtn;
    float grageRate;
    float rateSum;

    DatabaseReference garageReference,opreationReference ;
    public RateDialog(GrageInfo grageInfo,Opreation opreation,DatabaseReference opreationReference)
    {
        this.grageInfo = grageInfo;
        this.opreation=opreation;
        this.opreationReference=opreationReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.rate_dialog,container,false);
        initViews(view);

        garageReference = FirebaseDatabase.getInstance().getReference().child("GaragerOnwerInfo").child(grageInfo.getId());


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                opreation.setRate(v);
            }
        });

            addRate(new Rate() {
                @Override
                public void getRateAndNum(float rate,int num) {
                    rateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rateSum=rate+opreation.getRate();
                            grageInfo.setRate(rateSum);
                            grageInfo.setNumOfRatings(num+1);
                            garageReference.child("rate").setValue(rateSum);
                            garageReference.child("numOfRatings").setValue(num+1);
                            opreationReference.child("rate").setValue(opreation.getRate());
                            Toast.makeText(getContext(), rateSum+"", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    });
                }
            });




        return view;
    }

    private void initViews(View view) {
        ratingBar=(RatingBar) view.findViewById(R.id.rate);
        rateBtn=(Button) view.findViewById(R.id.addRate);
    }

    void addRate(Rate rateOBJ)
    {
        garageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float rateValue=snapshot.child("rate").getValue(Float.class);
                int num=snapshot.child("numOfRatings").getValue(Integer.class);
                rateOBJ.getRateAndNum(rateValue,num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface Rate
    {
        void getRateAndNum(float rate,int num);
    }

}
