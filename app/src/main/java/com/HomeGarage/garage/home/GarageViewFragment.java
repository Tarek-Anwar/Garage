package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.reservation.ConfarmResrerFragment;
import com.HomeGarage.garage.service.FcmNotificationsSender;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class GarageViewFragment extends Fragment {

    GrageInfo grageInfo;
    private TextView nameGarage , totalAddressGarage;
    private RatingBar ratingGarage ;
    Button orderGarage;

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

        FirebaseUser user = FirebaseUtil.currentUser;

        nameGarage.setText(grageInfo.getNameEn());
        totalAddressGarage.setText(grageInfo.getGovernoateEn()+" "+grageInfo.getCityEn()+" "+grageInfo.getRestOfAddressEN());

        orderGarage.setOnClickListener(v -> {

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView , new ConfarmResrerFragment(grageInfo));
            transaction.addToBackStack(null);
            transaction.commit();


        });
        return root;
    }

    void initView (View view){
        nameGarage = view.findViewById(R.id.name_garage_txt);
        totalAddressGarage = view.findViewById(R.id.total_address_garage_txt);
        orderGarage = view.findViewById(R.id.btn_order_garage);

        ratingGarage = view.findViewById(R.id.rating_garage);
    }
}