package com.HomeGarage.garage.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.LastOperModels;
import com.HomeGarage.garage.home.models.SearchModel;

public class OperationsFragment extends Fragment {

    private TextView type , to , from , time , palce , price;
    LastOperModels lastOperModels;
    public OperationsFragment(LastOperModels lastOperModels) {
        this.lastOperModels = lastOperModels;   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View  view =  inflater.inflate(R.layout.fragment_operations, container, false);

        intiView(view);
        setData(lastOperModels);

        return  view;
    }

    void intiView(View view){

        type = view.findViewById(R.id.type_oper_txt);
        to = view.findViewById(R.id.to_oper_txt);
        from = view.findViewById(R.id.from_oper_txt);
        time = view.findViewById(R.id.time_oper_txt);
        price = view.findViewById(R.id.price_oper_txt);
        palce = view.findViewById(R.id.place_oper_txt);


    }

    void setData(LastOperModels models){
        type.setText(models.getTextTimeOper());
        to.setText(models.getTextWhoDoOper());
        from.setText(models.getTextWhoToDoOper());
        time.setText(models.getTextTimeOper());
        price.setText(models.getTextPriceOper());
        palce.setText(models.getTextPlaceOper());

    }
}