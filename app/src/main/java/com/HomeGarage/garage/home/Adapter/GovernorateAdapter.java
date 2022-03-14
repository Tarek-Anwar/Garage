package com.HomeGarage.garage.home.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GovernorateAdapter extends RecyclerView.Adapter<GovernorateAdapter.GovernorateViewHolder> {

    ArrayList<String> listGoverEn = new ArrayList<>();
    GoverListener goverListener;

    public GovernorateAdapter(GoverListener goverListener){
        this.goverListener = goverListener;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Governorate");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot item : snapshot.getChildren()){
                        listGoverEn.add(item.child("governorate_name_en").getValue(String.class));
                        notifyItemChanged(listGoverEn.size()-1);
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
    public GovernorateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_governorate_row,parent,false);
        return new GovernorateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GovernorateViewHolder holder, int position) {
        holder.BulidUI(listGoverEn.get(position));
    }

    @Override
    public int getItemCount() {
        return listGoverEn.size();
    }

    protected class GovernorateViewHolder extends RecyclerView.ViewHolder {
        TextView nameGaver;
        View layouGover;
        public GovernorateViewHolder(@NonNull View itemView) {
            super(itemView);
            nameGaver = itemView.findViewById(R.id.txt_name_gover);
            layouGover = itemView.findViewById(R.id.layout_gover_listener);
        }

        public void BulidUI(String s){
            nameGaver.setText(s);
            layouGover.setOnClickListener(v -> goverListener.onGoverListener(getAdapterPosition()));
        }

    }

    public interface GoverListener{
        void onGoverListener(int pos);
    }
}
