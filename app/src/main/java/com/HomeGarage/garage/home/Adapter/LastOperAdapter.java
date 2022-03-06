package com.HomeGarage.garage.home.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LastOperAdapter extends RecyclerView.Adapter<LastOperAdapter.LastOperViewHolder> {

    public static final String TAG="mmm";

    ArrayList<Opreation> lastOpereations;
    Context context;
    LastOperListener lastOperListener;
    private int numViewOper=0;

    public LastOperAdapter( Context context, LastOperListener lastOperListener, int numViewOper) {
        this.context = context;
        this.numViewOper = numViewOper;
        this.lastOperListener=lastOperListener;
    }

    public void setLastOpereations(ArrayList<Opreation> lastOpereations) {
        this.lastOpereations = lastOpereations;
        notifyDataSetChanged();
    }

    public ArrayList<Opreation> getLastOpereations() {
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
        try {
            holder.BulidUI(lastOpereations.get(position));
            Log.d(TAG, lastOpereations.size() + "");
        }
        catch (IndexOutOfBoundsException e)
        {
            Log.e(TAG,"error");
        }
    }

   @Override
    public int getItemCount() {
        if(lastOpereations==null){
            return 0;}
        else if (numViewOper==0) { return lastOpereations.size(); }
       else { return numViewOper;}
    }
    protected  class LastOperViewHolder extends RecyclerView.ViewHolder{

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

        public void BulidUI(Opreation opreation){

            textWhoToDoOper.setText(opreation.getToName());
            textTypeOper.setText(opreation.getState());
            textTimeOper.setText(opreation.getDate());
            textWhoDoOper.setText(opreation.getFromName());
            layoutLastOper.setOnClickListener(v ->  {
                lastOperListener.LastOperListener(opreation);
            });
        }
    }

    public interface LastOperListener{
        void LastOperListener(Opreation opreation);
    }
}
