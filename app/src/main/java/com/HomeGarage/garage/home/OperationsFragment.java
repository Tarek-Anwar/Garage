package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.R;

public class OperationsFragment extends Fragment {

    private TextView type , to , from , time , palce , price;
    Opreation opreation;
    public OperationsFragment(Opreation opreation) {
        this.opreation=opreation;
    }

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
        setData(opreation);

        return  view;
    }

    void intiView(View view){

        type = view.findViewById(R.id.type_oper_txt);
        to = view.findViewById(R.id.to_oper_txt);
        from = view.findViewById(R.id.from_oper_txt);
        time = view.findViewById(R.id.time_oper_txt);
        price = view.findViewById(R.id.price_oper_txt);

    }

    @SuppressLint("SetTextI18n")
    void setData(Opreation opreation){
        type.setText(opreation.getState());
        to.setText(opreation.getToName());
        from.setText(opreation.getFrom());
        time.setText(opreation.getDate()+"");

    }
}