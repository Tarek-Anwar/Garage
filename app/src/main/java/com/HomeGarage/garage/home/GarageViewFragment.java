package com.HomeGarage.garage.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.DB.GrageInfo;
import com.HomeGarage.garage.R;

public class GarageViewFragment extends Fragment {

    GrageInfo grageInfo;
    private TextView nameGarage , totalAddressGarage;
    private Button  orderGarage , showLocationGarage;
    private RatingBar ratingGarage ;


    public GarageViewFragment(GrageInfo grageInfo) {
       this.grageInfo = grageInfo;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_garage_view, container, false);

        initView(root);

        ratingGarage.setRating(grageInfo.getViewRate());
        ratingGarage.setEnabled(false);

        nameGarage.setText(grageInfo.getGrageName());
        totalAddressGarage.setText(grageInfo.getGovernoate()+" "+grageInfo.getCity()+" "+grageInfo.getRestOfAddress());

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