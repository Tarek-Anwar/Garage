package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.R;

public class OperationsFragment extends Fragment {

    private TextView type , to , from , time  , price;
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
        type.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreation.getState())-1));
        to.setText(opreation.getToName());
        from.setText(opreation.getFromName());
        time.setText(opreation.getDate()+"");
        price.setText(opreation.getPrice()+" E.G.");
    }
}