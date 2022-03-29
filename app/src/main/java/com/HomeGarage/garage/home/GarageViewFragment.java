package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.reservation.ConfarmResrerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class GarageViewFragment extends Fragment {

    GrageInfo grageInfo;
    private TextView nameGarage , totalAddressGarage , phoneGarage , priceGarage , rateGarageNum;
    Button orderGarage;
    DatabaseReference garageReference ;
    com.chaek.android.RatingBar ratingBar;


    public GarageViewFragment(GrageInfo grageInfo) {
       this.grageInfo = grageInfo;
    }

    public GarageViewFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_garage_view, container, false);
        initView(root);

        if(savedInstanceState==null){ garageReference = FirebaseUtil.referenceGarage.child(grageInfo.getId());
        }else { garageReference = FirebaseUtil.referenceGarage.child(savedInstanceState.getString("saveBalance")); }

        String pound = " "+ getActivity().getString(R.string.eg);
        String ratings =  " " + getActivity().getString(R.string.ratings) + " )";
        getRate(grageInfo -> {
            String allAddress , name ;
            if(Locale.getDefault().getLanguage().equals("en")){
                allAddress = grageInfo.getGovernoateEn()+"\n"+grageInfo.getCityEn()+"\n"+grageInfo.getRestOfAddressEN();
                name = grageInfo.getNameEn();
            }else {
                allAddress = grageInfo.getGovernoateAR()+"\n"+grageInfo.getCityAr()+"\n"+grageInfo.getRestOfAddressAr();
                name = grageInfo.getNameAr();
            }

            nameGarage.setText(name);
            totalAddressGarage.setText(allAddress);
            phoneGarage.setText(grageInfo.getPhone());
            priceGarage.setText(grageInfo.getPriceForHour()+pound);

            if(grageInfo.getNumOfRatings()!=0) {
                float ratting = grageInfo.getRate() /((float) grageInfo.getNumOfRatings());
                ratingBar.setScore((int) ratting*2);
                rateGarageNum.setText(ratting + " ( "+grageInfo.getNumOfRatings() +ratings);
            } else ratingBar.setScore(0);

        });

        ratingBar.setEnabled(false);

        orderGarage.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView , new ConfarmResrerFragment(grageInfo,getActivity()));
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return root;
    }

    void initView (View view){
        nameGarage = view.findViewById(R.id.name_garage_txt);
        totalAddressGarage = view.findViewById(R.id.address_garage_view);
        orderGarage = view.findViewById(R.id.btn_order_garage);
        ratingBar = view.findViewById(R.id.rate_garage);
        phoneGarage = view.findViewById(R.id.phone_garage_view);
        priceGarage = view.findViewById(R.id.price_garage_view);
        rateGarageNum = view.findViewById(R.id.rate_garage_view);
    }

    void getRate(Rate rate) {
        garageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                grageInfo = snapshot.getValue(GrageInfo.class);
                rate.onGarageGet(grageInfo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    interface Rate { void onGarageGet(GrageInfo grageInfo);}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("saveBalance" , grageInfo.getId());
    }

}