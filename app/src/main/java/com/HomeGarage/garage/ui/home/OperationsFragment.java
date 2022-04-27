package com.HomeGarage.garage.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.util.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.OpreationModule;

public class OperationsFragment extends Fragment {

    OpreationModule opreationModule;
    private TextView type , to , from , time  , price ,rate , timeEnd , state;
    public OperationsFragment(OpreationModule opreationModule) {
        this.opreationModule = opreationModule;
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
        setData(opreationModule);

        return  view;
    }

    void intiView(View view){

        type = view.findViewById(R.id.type_oper_txt);
        to = view.findViewById(R.id.to_oper_txt);
        from = view.findViewById(R.id.from_oper_txt);
        time = view.findViewById(R.id.time_oper_txt);
        price = view.findViewById(R.id.price_oper_txt);
        state = view.findViewById(R.id.state_oper_txt);
        rate = view.findViewById(R.id.rate_oper_txt);
        timeEnd = view.findViewById(R.id.time_end_oper_txt);

    }

    @SuppressLint("SetTextI18n")
    void setData(OpreationModule opreationModule){
        type.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreationModule.getType())-1));
        to.setText(opreationModule.getToName());
        from.setText(opreationModule.getFromName());
        time.setText(opreationModule.getDate()+"");
        price.setText(opreationModule.getPrice()+" E.G.");
        state.setText(FirebaseUtil.stateList.get(Integer.parseInt(opreationModule.getState())-1));
        if(opreationModule.getRate()!=0){ rate.setText((opreationModule.getRate()/2)+""); }else { rate.setText("!"); }
        if(opreationModule.getDataEnd()!=null) timeEnd.setText(opreationModule.getDataEnd());
        else timeEnd.setText("!");
    }
}