package com.HomeGarage.garage.home.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class GarageInCityAdapter extends RecyclerView.Adapter<GarageInCityAdapter.GarageViewHolder> {

    ArrayList<GrageInfo> grageInfos  = new ArrayList<>() ;
    GarageLisenter garageLisenter;
    public   GarageInCityAdapter(String s , GarageLisenter garageLisenter){
        this.garageLisenter = garageLisenter;
        Query query = FirebaseUtil.referenceGarage.orderByChild("cityEn").equalTo(s);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        GrageInfo grage = dataSnapshot.getValue(GrageInfo.class);
                        grageInfos.add(grage);
                        notifyItemChanged(grageInfos.size()-1);
                    }
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @NonNull
    @Override
    public GarageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_layout,parent,false);
       return new GarageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull GarageViewHolder holder, int position) {
            holder.bulidUI(grageInfos.get(position));
    }

    @Override
    public int getItemCount() {
        return grageInfos.size();
    }

    public class GarageViewHolder extends RecyclerView.ViewHolder{
        TextView name ,address;
        View viewGarageLisenter;
        public GarageViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name_garage);
            address = itemView.findViewById(R.id.txt_address_garage);
            viewGarageLisenter = itemView.findViewById(R.id.layout_garage_lisenter);
        }

        public void  bulidUI(GrageInfo info){
            if(Locale.getDefault().getLanguage().equals("en")){
                name.setText(info.getNameEn());
                address.setText(info.getRestOfAddressEN());
            }else {
                name.setText(info.getNameAr());
                address.setText(info.getRestOfAddressAr());
            }

            viewGarageLisenter.setOnClickListener(v -> garageLisenter.onGarageLisenter(grageInfos.get(getAdapterPosition())));
        }
    }
    public interface GarageLisenter{
        void  onGarageLisenter(GrageInfo grageInfo);
    }
}
