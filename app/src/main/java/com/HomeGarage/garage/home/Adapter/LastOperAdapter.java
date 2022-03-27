package com.HomeGarage.garage.home.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.Opreation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LastOperAdapter extends RecyclerView.Adapter<LastOperAdapter.LastOperViewHolder> {

    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));
    SimpleDateFormat formatter =new SimpleDateFormat("dd MMM yyyy",new Locale("en"));
    ArrayList<Opreation> lastOpereations = FirebaseUtil.opreationEndList;
    LastOperListener lastOperListener;
    DatabaseReference reference =  FirebaseUtil.referenceOperattion;
    Query query ;

    private int numViewOper = 0;

    public LastOperAdapter(LastOperListener lastOperListener, int numViewOper) {
        this.numViewOper = numViewOper;
        this.lastOperListener=lastOperListener;
        query = reference.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    lastOpereations.clear();
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
            public void onCancelled(@NonNull DatabaseError error) { }
        });
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
        if(numViewOper==0){
            return lastOpereations.size();
        }else {
             if(lastOpereations.size() < 3){
                 return lastOpereations.size();
             }
             return 3;
        }
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

        public void BulidUI(@NonNull Opreation opreation){

            textWhoToDoOper.setText(opreation.getToName());
            textTypeOper.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreation.getType())-1));

            Date d1 = null;
            try { d1 = formatterLong.parse(opreation.getDate());
                textTimeOper.setText(formatter.format(d1));
            } catch (ParseException e) { e.printStackTrace(); }
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
