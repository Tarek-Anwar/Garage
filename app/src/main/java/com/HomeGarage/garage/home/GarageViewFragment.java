package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.HomeGarage.garage.models.GrageInfoModel;
import com.HomeGarage.garage.reservation.ConfarmResrerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.Locale;

public class GarageViewFragment extends Fragment {

    GrageInfoModel grageInfoModel;
    Button orderGarage;
    DatabaseReference garageReference ;
    com.chaek.android.RatingBar ratingBar;
    View cardPhoneView , cardAddressView;
    LikeButton likeButtonGarage;
    private TextView nameGarage , totalAddressGarage , phoneGarage , priceGarage , rateGarageNum;


    public GarageViewFragment(GrageInfoModel grageInfoModel) {
       this.grageInfoModel = grageInfoModel;
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

        if(savedInstanceState==null){ garageReference = FirebaseUtil.referenceGarage.child(grageInfoModel.getId());
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
                float ratting = grageInfo.getRate() /grageInfo.getNumOfRatings();
                ratingBar.setScore((int) ratting);
                rateGarageNum.setText( (ratting/2) + " ( "+grageInfo.getNumOfRatings() + ratings);
            } else ratingBar.setScore(0);

        });

        ratingBar.setEnabled(false);
        likeButtonGarage.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(likeButton.isEnabled()) {
                    DatabaseReference reference = FirebaseUtil.databaseReference.child(FirebaseUtil.firebaseAuth.getUid()).child("favicon").child(grageInfoModel.getId());
                    reference.setValue(grageInfoModel.getNameEn());
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if(likeButton.isEnabled()){
                    DatabaseReference reference = FirebaseUtil.databaseReference.child(FirebaseUtil.firebaseAuth.getUid()).child("favicon").child(grageInfoModel.getId());
                    reference.removeValue();
                }
            }
        });

        orderGarage.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView , new ConfarmResrerFragment(grageInfoModel,getActivity()));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        cardPhoneView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + grageInfoModel.getPhone()));
            startActivity(intent);
        });

        cardAddressView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getMapsURI(grageInfoModel.getLocation())));
            startActivity(intent);
        });
        return root;
    }

    void initView (View view){
        likeButtonGarage = view.findViewById(R.id.like_button_garage);
        nameGarage = view.findViewById(R.id.name_garage_txt);
        totalAddressGarage = view.findViewById(R.id.address_garage_view);
        orderGarage = view.findViewById(R.id.btn_order_garage);
        ratingBar = view.findViewById(R.id.rate_garage);
        phoneGarage = view.findViewById(R.id.phone_garage_view);
        priceGarage = view.findViewById(R.id.price_garage_view);
        rateGarageNum = view.findViewById(R.id.rate_garage_view);
        cardPhoneView = view.findViewById(R.id.card_phone_view);
        cardAddressView = view.findViewById(R.id.card_address_view);
    }

    void getRate(Rate rate) {
        garageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                grageInfoModel = snapshot.getValue(GrageInfoModel.class);
                rate.onGarageGet(grageInfoModel);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("saveBalance" , grageInfoModel.getId());
    }

    interface Rate { void onGarageGet(GrageInfoModel grageInfoModel);}

    private String getMapsURI(String locationId) {
        String[] latitudeAndLongitude = locationId.split(",");
        String latitude = latitudeAndLongitude[0];
        String longitude = latitudeAndLongitude[1];
        return "geo:" + latitude + "," + longitude
                + "?q=<" + latitude + ">,<" + longitude + ">,("
                + grageInfoModel.getNameEn() + ")";
    }

}