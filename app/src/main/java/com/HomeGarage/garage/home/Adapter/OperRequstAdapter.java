package com.HomeGarage.garage.home.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.Opreation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class OperRequstAdapter extends RecyclerView.Adapter<OperRequstAdapter.OperRequstViewHoler> {

    ArrayList<Opreation> opreationList = FirebaseUtil.opreationRequstList;

    public OperRequstAdapter( ArrayList<Opreation> opreationList) {
        //this.opreationList = opreationList;
        /*opreationList = FirebaseUtil.opreationRequstList;
        DatabaseReference reference = FirebaseUtil.referenceOperattion;
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Opreation opreation = snapshot.getValue(Opreation.class);
                if( ( opreation.getState().equals("1") || opreation.getState().equals("2") )
                        && (opreation.getType().equals("1") || opreation.getType().equals("2")))
                {
                    opreationList.add(opreation);
                    notifyItemChanged(opreationList.size()-1);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    @NonNull
    @Override
    public OperRequstViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.opreation_requst_row , parent , false);
        return new OperRequstViewHoler(root);
    }

    @Override
    public void onBindViewHolder(@NonNull OperRequstViewHoler holder, int position) {
        holder.bulidUI(opreationList.get(position));
    }

    @Override
    public int getItemCount() {
        return opreationList.size();
    }

    public class OperRequstViewHoler extends RecyclerView.ViewHolder {

        TextView state , type , name , date;
        public OperRequstViewHoler(@NonNull View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.txt_requst_state_home);
            type = itemView.findViewById(R.id.txt_requst_type_home);
            name = itemView.findViewById(R.id.txt_garage_name);
            date = itemView.findViewById(R.id.txt_date);
        }

        private void bulidUI(Opreation opreation){
            state.setText(FirebaseUtil.stateList.get(Integer.parseInt(opreation.getState())-1));
            type.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreation.getType())-1));
            date.setText(opreation.getDate());
            name.setText(opreation.getToName());
        }
    }
}
