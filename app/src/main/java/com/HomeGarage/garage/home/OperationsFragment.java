package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.models.OpreationModel;

public class OperationsFragment extends Fragment {

    OpreationModel opreationModel;
    private TextView type , to , from , time  , price ,rate , timeEnd , state;
    public OperationsFragment(OpreationModel opreationModel) {
        this.opreationModel = opreationModel;
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
        setData(opreationModel);

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
    void setData(OpreationModel opreationModel){
        type.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreationModel.getType())-1));
        to.setText(opreationModel.getToName());
        from.setText(opreationModel.getFromName());
        time.setText(opreationModel.getDate()+"");
        price.setText(opreationModel.getPrice()+" E.G.");
        state.setText(FirebaseUtil.stateList.get(Integer.parseInt(opreationModel.getState())-1));
        if(opreationModel.getRate()!=0){ rate.setText((opreationModel.getRate()/2)+""); }else { rate.setText("!"); }
        if(opreationModel.getDataEnd()!=null) timeEnd.setText(opreationModel.getDataEnd());
        else timeEnd.setText("!");
    }
}