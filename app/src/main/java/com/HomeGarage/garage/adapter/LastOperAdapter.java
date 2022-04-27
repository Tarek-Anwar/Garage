package com.HomeGarage.garage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.util.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.OpreationModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class LastOperAdapter extends RecyclerView.Adapter<LastOperAdapter.LastOperViewHolder> {

    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));
    SimpleDateFormat formatter =new SimpleDateFormat("dd MMM yyyy",new Locale("en"));
    LinkedList<OpreationModule> lastOpereations ;
    LastOperListener lastOperListener;
    private int numViewOper = 1;


    public LastOperAdapter(LinkedList<OpreationModule> lastOpereations , int numViewOper, LastOperListener lastOperListener) {
        this.numViewOper = numViewOper;
        this.lastOperListener=lastOperListener;
        this.lastOpereations = lastOpereations;
    }

    @NonNull
    @Override
    public LastOperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_operations_row_layout,parent,false);
        return new LastOperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LastOperViewHolder holder, int position) {
        holder.BulidUI(lastOpereations.get(position));
    }

   @Override
    public int getItemCount() {
        if(numViewOper==1){
            return lastOpereations.size();
        }else {
             if(lastOpereations.size() < 3){
                 return lastOpereations.size();
             }
             return 3;
        }
    }

    public interface LastOperListener{
        void LastOperListener(OpreationModule opreationModule);
    }

    protected class LastOperViewHolder extends RecyclerView.ViewHolder{

       private  TextView textTypeOper,textWhoDoOper,textWhoToDoOper,textTimeOper ;
       private View layoutLastOper;

       public LastOperViewHolder(@NonNull View itemView) {
            super(itemView);
            textTimeOper = itemView.findViewById(R.id.text_time_oper);
            textTypeOper = itemView.findViewById(R.id.text_type_oper);
            textWhoDoOper = itemView.findViewById(R.id.text_who_do);
            textWhoToDoOper = itemView.findViewById(R.id.text_who_to_do);
            layoutLastOper = itemView.findViewById(R.id.layout_last_oper);
        }

        public void BulidUI(@NonNull OpreationModule opreationModule){

            textWhoToDoOper.setText(opreationModule.getToName());
            textTypeOper.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreationModule.getType())-1));

            Date d1 = null;
            try { d1 = formatterLong.parse(opreationModule.getDate());
                textTimeOper.setText(formatter.format(d1));
            } catch (ParseException e) { e.printStackTrace(); }
            textWhoDoOper.setText(opreationModule.getFromName());

            layoutLastOper.setOnClickListener(v ->  {
                lastOperListener.LastOperListener(opreationModule);
            });
        }
    }
}
