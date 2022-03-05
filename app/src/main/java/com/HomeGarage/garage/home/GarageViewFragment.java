package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.service.FcmNotificationsSender;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class GarageViewFragment extends Fragment {

    GrageInfo grageInfo;
    private TextView nameGarage , totalAddressGarage;
    private RatingBar ratingGarage ;
    Button orderGarage , showLocationGarage;
    FirebaseUser user;
    public GarageViewFragment(GrageInfo grageInfo) {
       this.grageInfo = grageInfo;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_garage_view, container, false);

        initView(root);

        ratingGarage.setRating(grageInfo.getPriceForHour());
        ratingGarage.setEnabled(false);

        user = FirebaseUtil.firebaseAuth.getCurrentUser();
        FirebaseMessaging.getInstance().subscribeToTopic(user.getEmail());

        nameGarage.setText(grageInfo.getNameEn());
        totalAddressGarage.setText(grageInfo.getGovernoateEn()+" "+grageInfo.getCityEn()+" "+grageInfo.getRestOfAddressEN());

        orderGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                        "/topics/"+grageInfo.getEmail() ,"From " + user.getEmail()
                        ,"To Garage "+ grageInfo.getNameEn() , getContext(),getActivity());

                notificationsSender.SendNotifications();
            }
        });
        return root;
    }

    void initView (View view){
        nameGarage = view.findViewById(R.id.name_garage_txt);
        totalAddressGarage = view.findViewById(R.id.total_address_garage_txt);
        orderGarage = view.findViewById(R.id.btn_order_garage);
        showLocationGarage = view.findViewById(R.id.btn_show_loca_garage);
        ratingGarage = view.findViewById(R.id.rating_garage);
    }
}