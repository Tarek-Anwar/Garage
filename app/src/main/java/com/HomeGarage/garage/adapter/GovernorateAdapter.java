package com.HomeGarage.garage.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.GovernorateModule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class GovernorateAdapter extends RecyclerView.Adapter<GovernorateAdapter.GovernorateViewHolder> {

    ArrayList<GovernorateModule> listGoverEn = new ArrayList<>();
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
                        GovernorateModule model = item.getValue(GovernorateModule.class);
                        listGoverEn.add(model);
                        notifyItemChanged(listGoverEn.size()-1);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
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
        holder.bulidUI(listGoverEn.get(position));
    }

    @Override
    public int getItemCount() {
        return listGoverEn.size();
    }

    public interface GoverListener{
        void onGoverListener(int pos , String s);
    }

    protected class GovernorateViewHolder extends RecyclerView.ViewHolder {
        TextView nameGaver;
        ImageView imageView;
        View layouGover;
        public GovernorateViewHolder(@NonNull View itemView) {
            super(itemView);
            nameGaver = itemView.findViewById(R.id.txt_name_gover);
            layouGover = itemView.findViewById(R.id.layout_gover_listener);
            imageView  = itemView.findViewById(R.id.image_gover);
        }

        public void bulidUI(GovernorateModule model){
            showImage(model.getImage_url());
            nameGaver.setText(Locale.getDefault().getLanguage().equals("en") ? model.getGovernorate_name_en() : model.getGovernorate_name_ar());
            layouGover.setOnClickListener(v -> goverListener.onGoverListener(getLayoutPosition() , model.getGovernorate_name_en()));

        }

        public void showImage(String url) {
            if (url != null ) {
                Log.i("sdfrweqrqweer" ,url);
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                Picasso.get().load(url).resize(width,width)
                        .centerCrop()
                        .into(imageView);
            }else Log.i("sdfrweqrqweer" ,"not Url");
        }
    }
}
