package com.HomeGarage.garage.home.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LastOperAdapter extends RecyclerView.Adapter<LastOperAdapter.LastOperViewHolder> {

    ArrayList<Opreation> lastOpereations;
    LastOperListener lastOperListener;
    private int numViewOper=0;

    public LastOperAdapter(ArrayList<Opreation> lastOpereations, LastOperListener lastOperListener, int numViewOper) {
        this.numViewOper = numViewOper;
        this.lastOperListener=lastOperListener;

      /*  lastOpereations  = FirebaseUtil.opreationEndList;
        DatabaseReference reference = FirebaseUtil.referenceOperattion;
        Query query = reference.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Opreation opreation = snapshot1.getValue(Opreation.class);
                        if (opreation.getState().equals("3") && (opreation.getType().equals("3") || opreation.getType().equals("4"))) {
                            lastOpereations.add(opreation);
                            notifyItemChanged(lastOpereations.size()-1);
                        }
                    }
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
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
        return lastOpereations.size();
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
            textTypeOper.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreation.getState())-1));
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
