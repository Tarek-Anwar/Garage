package com.HomeGarage.garage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.util.DateFormatUtil;
import com.HomeGarage.garage.util.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.PurchaseModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalaceViewHolder> {

    LinkedList<PurchaseModule> purchaseModules;
    Context context;

    public  BalanceAdapter(Context context , LinkedList<PurchaseModule> purchaseModules ){
        this.purchaseModules=purchaseModules;
        this.context = context;
    }

    @Override
    public BalaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_row,parent,false);
        return new BalaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BalaceViewHolder holder, int position) {
        holder.bulidUI(purchaseModules.get(position));
    }

    @Override
    public int getItemCount() {
        return purchaseModules.size();
    }

    public class BalaceViewHolder extends RecyclerView.ViewHolder{
        private TextView textType ;
        private TextView textToName ;
        private TextView textValue ;
        private TextView textDate;
        private TextView textTime;
        private String poundEg = context.getString(R.string.eg);

        public BalaceViewHolder(@NonNull View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.type_pay_txt);
            textToName = itemView.findViewById(R.id.to_pay_txt);
            textValue = itemView.findViewById(R.id.value_pay_txt);
            textDate = itemView.findViewById(R.id.date_pay_txt);
            textTime = itemView.findViewById(R.id.time_pay_txt);
        }

        public void bulidUI(PurchaseModule model){
            model.setValue(model.getType().equals("1") ? model.getValue()*-1 : model.getValue());

            textType.setText(FirebaseUtil.paylist.get(Integer.parseInt(model.getType())-1));
            textToName.setText(model.getToName());
            textValue.setText(String.format("%+.2f %s",model.getValue(),poundEg));
            Date date ;
            try {
                date = DateFormatUtil.allDataFormat.parse(model.getDate());
                textDate.setText(DateFormatUtil.dayFormat.format(date));
                textTime.setText(DateFormatUtil.timeFormat.format(date));
            } catch (ParseException e) { e.printStackTrace(); }

        }
    }
}
