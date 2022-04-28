package com.HomeGarage.garage.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.util.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.GarageInfoModule;
import com.HomeGarage.garage.ui.reservation.ConfarmResrerFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class GarageViewFragment extends Fragment {

    GarageInfoModule garageInfoModule;
    Button orderGarage;
    DatabaseReference garageReference ;
    com.chaek.android.RatingBar ratingBar;
    View cardPhoneView , cardAddressView;
    LikeButton likeButtonGarage;
    private TextView nameGarage , totalAddressGarage , phoneGarage , priceGarage , rateGarageNum;
    DatabaseReference referenceFav;
    ImageView imageGarage;

    public GarageViewFragment(GarageInfoModule garageInfoModule) {
       this.garageInfoModule = garageInfoModule;
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

        referenceFav = FirebaseUtil.databaseReference.child(FirebaseUtil.firebaseAuth.getUid()).child("favicon").child(garageInfoModule.getId());

        if(savedInstanceState==null){ garageReference = FirebaseUtil.referenceGarage.child(garageInfoModule.getId());
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

            if(grageInfo.getImageGarage()!=null) showImage(grageInfo.getImageGarage(),imageGarage);
            if(grageInfo.getNumOfRatings()!=0) {
                float ratting = grageInfo.getRate() /grageInfo.getNumOfRatings();
                ratingBar.setScore((int) ratting);
                rateGarageNum.setText( (ratting/2) + " ( "+grageInfo.getNumOfRatings() + ratings);
            } else ratingBar.setScore(0);

        });

        getFaviconGarage(isFavicon -> {
            likeButtonGarage.setLiked(isFavicon);
            likeButtonGarage.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    if(likeButton.isEnabled()) referenceFav.setValue(garageInfoModule.getNameEn());
                }
                @Override
                public void unLiked(LikeButton likeButton) {
                    if(likeButton.isEnabled()) referenceFav.removeValue();
                }
            });
        });

        ratingBar.setEnabled(false);

        orderGarage.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView , new ConfarmResrerFragment(garageInfoModule,getActivity()));
            transaction.addToBackStack(null);
            transaction.commit();
        });

        cardPhoneView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + garageInfoModule.getPhone()));
            startActivity(intent);
        });

        cardAddressView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getMapsURI(garageInfoModule.getLocation())));
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
        imageGarage = view.findViewById(R.id.img_profile_garage);
    }

    void getRate(Rate rate) {
        garageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                garageInfoModule = snapshot.getValue(GarageInfoModule.class);
                rate.onGarageGet(garageInfoModule);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("saveBalance" , garageInfoModule.getId());
    }

    private void getFaviconGarage(OnFaviconGarageGetCallBack callBack){
        referenceFav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) callBack.isFaviconGarage(true);
                else  callBack.isFaviconGarage(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    interface OnFaviconGarageGetCallBack{
        void isFaviconGarage(Boolean isFavicon);
    }
    interface Rate { void onGarageGet(GarageInfoModule garageInfoModule);}

    private String getMapsURI(String locationId) {
        String[] latitudeAndLongitude = locationId.split(",");
        String latitude = latitudeAndLongitude[0];
        String longitude = latitudeAndLongitude[1];
        return "geo:" + latitude + "," + longitude
                + "?q=<" + latitude + ">,<" + longitude + ">,("
                + garageInfoModule.getNameEn() + ")";
    }
    private void showImage(String url , ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width, width).centerCrop().into(imageView); } }
}