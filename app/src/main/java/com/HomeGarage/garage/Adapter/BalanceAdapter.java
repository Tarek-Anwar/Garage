package com.HomeGarage.garage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.PurchaseModule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
        TextView textType , TextToName , textValue , TextDate , TextTime;
        Date dateLong ;
        String poundEg = context.getString(R.string.eg);
        SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));
        SimpleDateFormat formatterData =new SimpleDateFormat("dd/MM/yyyy" , new Locale("en"));
        SimpleDateFormat formattertime =new SimpleDateFormat("hh:mm:ss aa" , new Locale("en"));

        public BalaceViewHolder(@NonNull View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.type_pay_txt);
            TextToName = itemView.findViewById(R.id.to_pay_txt);
            textValue = itemView.findViewById(R.id.value_pay_txt);
            TextDate = itemView.findViewById(R.id.date_pay_txt);
            TextTime = itemView.findViewById(R.id.time_pay_txt);
        }

        public void bulidUI(PurchaseModule model){

            model.setValue(model.getType().equals("2") ? model.getValue() : model.getValue() * -1);

            textType.setText(FirebaseUtil.paylist.get(Integer.parseInt(model.getType())-1));
            TextToName.setText(model.getToName());
            textValue.setText(String.format("%+.2f %s",model.getValue() , poundEg));

            try {
                dateLong = formatterLong.parse(model.getDate());
                TextDate.setText(formatterData.format(dateLong));
                TextTime.setText(formattertime.format(dateLong));
            } catch (ParseException e) { e.printStackTrace(); }

        }
    }
}
