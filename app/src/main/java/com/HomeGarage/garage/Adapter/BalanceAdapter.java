package com.HomeGarage.garage.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.models.PurchaseModel;
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
import java.util.Locale;


public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.BalaceViewHolder> {

    ArrayList<PurchaseModel> purchaseModels;
    DatabaseReference reference;
    Context context;
    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));
    SimpleDateFormat formatterData =new SimpleDateFormat("dd/MM/yyyy" , new Locale("en"));
    SimpleDateFormat formattertime =new SimpleDateFormat("hh:mm:ss aa" , new Locale("en"));

    public  BalanceAdapter(Context context ){
        this.context = context;
        purchaseModels = new ArrayList<>();
        reference = FirebaseUtil.referencePurchase;
        Query query =  reference.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for (DataSnapshot item : snapshot.getChildren()){
                        PurchaseModel model = item.getValue(PurchaseModel.class);
                        purchaseModels.add(model);
                    }
                    Collections.reverse(purchaseModels);
                    notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @NonNull
    @Override
    public BalaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_row,parent,false);
        return new BalaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BalaceViewHolder holder, int position) {
        holder.bulidUI(purchaseModels.get(position));
    }

    @Override
    public int getItemCount() {
        return purchaseModels.size();
    }

    public class BalaceViewHolder extends RecyclerView.ViewHolder{
        TextView type , to ,value , date , time;
        Date dateLong ;
        String poundEg = context.getString(R.string.eg);
        public BalaceViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type_pay_txt);
            to = itemView.findViewById(R.id.to_pay_txt);
            value = itemView.findViewById(R.id.value_pay_txt);
            date = itemView.findViewById(R.id.date_pay_txt);
            time = itemView.findViewById(R.id.time_pay_txt);
        }

        public void bulidUI(PurchaseModel model){
            type.setText(FirebaseUtil.paylist.get(Integer.parseInt(model.getType())-1));
            to.setText(model.getToName());
            if(model.getType().equals("2")){
                value.setText(String.format(" %+.2f %s",model.getValue() , poundEg));
            }else {
                model.setValue(model.getValue()*-1);
                value.setText(String.format("%+.2f %s",model.getValue() , poundEg));
            }

            try {
                dateLong = formatterLong.parse(model.getDate());
                date.setText(formatterData.format(dateLong));
                time.setText(formattertime.format(dateLong));
            } catch (ParseException e) { e.printStackTrace(); }



        }
    }
}
