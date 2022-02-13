package com.HomeGarage.garage.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.OffersModels;
import java.util.ArrayList;

public class OffersAdpter extends RecyclerView.Adapter<OffersAdpter.OffersViewHolder> {

    ArrayList<OffersModels> modelsList;
    Context context ;

    public OffersAdpter(ArrayList<OffersModels> modelsList, Context context) {
        this.modelsList = modelsList;
        this.context = context;
    }

    @NonNull
    @Override
    public OffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_row_layout,parent,false);
        return new OffersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersViewHolder holder, int position) {
        holder.BulidUi(modelsList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelsList.size();
    }

    protected static final class OffersViewHolder extends RecyclerView.ViewHolder {

        private  ImageView imageView;
        public OffersViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.offer_img);
        }

        public void BulidUi(OffersModels offersModels){
            imageView.setImageResource(offersModels.getImg());
        }

    }
}
