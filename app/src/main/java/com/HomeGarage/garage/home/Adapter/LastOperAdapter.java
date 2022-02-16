package com.HomeGarage.garage.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.DB.Opreation;
import com.HomeGarage.garage.R;


import java.util.List;

public class LastOperAdapter extends RecyclerView.Adapter<LastOperAdapter.LastOperViewHolder> {

    List<Opreation> lastOpereations;
    Context context;
    LastOperListener lastOperListener;
    private int numViewOper=0;

    public LastOperAdapter( Context context, LastOperListener lastOperListener, int numViewOper) {
        this.context = context;
        this.numViewOper = numViewOper;
        this.lastOperListener=lastOperListener;
    }

    public void setLastOpereations(List<Opreation> lastOpereations) {
        this.lastOpereations = lastOpereations;
        notifyDataSetChanged();
    }

    public List<Opreation> getLastOpereations() {
        return lastOpereations;
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
        if(lastOpereations==null)
            return 0;
       if (numViewOper==0) { return lastOpereations.size(); }
       else { return numViewOper;}
    }
/*
    @Override
    public int getItemCount() {
        if (numViewOper==0) { return lastOpereations.size(); }
        else { return numViewOper;}
    }
*/
    protected  class LastOperViewHolder extends RecyclerView.ViewHolder{

       private  TextView textTypeOper,textWhoDoOper,textWhoToDoOper,textTimeOper,textPriceOper ;
       private View layoutLastOper;

       public LastOperViewHolder(@NonNull View itemView) {
            super(itemView);

            textTimeOper = itemView.findViewById(R.id.text_time_oper);
            textTypeOper = itemView.findViewById(R.id.text_type_oper);
            textWhoDoOper = itemView.findViewById(R.id.text_who_do);
            textWhoToDoOper = itemView.findViewById(R.id.text_who_to_do);
            layoutLastOper = itemView.findViewById(R.id.layout_last_oper);
            textPriceOper = itemView.findViewById(R.id.text_oper_price);
        }

        public void BulidUI(Opreation opreation){

            textWhoToDoOper.setText(opreation.getDecisionRecipient());
            textTypeOper.setText(opreation.getState());
            textTimeOper.setText(opreation.getDate());
            textWhoDoOper.setText(opreation.getDecisionMaker());
            textPriceOper.setText(opreation.getPrice()+"");

            layoutLastOper.setOnClickListener(v ->  {
                lastOperListener.LastOperListener(opreation);
            });
        }
    }

    public interface LastOperListener{
        void LastOperListener(Opreation opreation);

    }
}
