package com.HomeGarage.garage.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.LastOperModels;

import java.util.ArrayList;

public class LastOperAdapter extends RecyclerView.Adapter<LastOperAdapter.LastOperViewHolder> {

    ArrayList<LastOperModels> lastOperModelsList;
    Context context;
    LastOperListener lastOperListener;
    private int numViewOper=0;

    public LastOperAdapter(ArrayList<LastOperModels> lastOperModelsList, Context context, LastOperListener lastOperListener, int numViewOper) {
        this.lastOperModelsList = lastOperModelsList;
        this.context = context;
        this.numViewOper = numViewOper;
        this.lastOperListener=lastOperListener;
    }

    @NonNull
    @Override
    public LastOperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_operations_row_layout,parent,false);
        return new LastOperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LastOperViewHolder holder, int position) {
        holder.BulidUI(lastOperModelsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (numViewOper==0) { return lastOperModelsList.size(); }
        else { return numViewOper;}
    }

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

        public void BulidUI(LastOperModels models){

            textWhoToDoOper.setText(models.getTextWhoToDoOper());
            textTypeOper.setText(models.getTextTypeOper());
            textTimeOper.setText(models.getTextTimeOper());
            textWhoDoOper.setText(models.getTextWhoDoOper());
            textPriceOper.setText(models.getTextPriceOper());

            layoutLastOper.setOnClickListener(v ->  {
                lastOperListener.LastOperListener(models);
            });
        }
    }

    public interface LastOperListener{
        void LastOperListener(LastOperModels lastOperModels);

    }
}
