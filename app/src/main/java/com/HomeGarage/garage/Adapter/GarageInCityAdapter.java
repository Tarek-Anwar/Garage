package com.HomeGarage.garage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.models.GarageInfoModel;

import java.util.ArrayList;
import java.util.Locale;

public class GarageInCityAdapter extends RecyclerView.Adapter<GarageInCityAdapter.GarageViewHolder> {

    ArrayList<GarageInfoModel> garageInfoModels;
    GarageLisenter garageLisenter;
    Context context;
    String ratings;
    String egPound;
    String nonRate;

    public   GarageInCityAdapter(ArrayList<GarageInfoModel> garageInfoModels, Context context, GarageLisenter garageLisenter ){
        this.garageLisenter = garageLisenter;
        this.context = context;
        this.garageInfoModels = garageInfoModels;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GarageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_layout,parent,false);
       return new GarageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull GarageViewHolder holder, int position) {
            holder.bulidUI(garageInfoModels.get(position));
    }

    @Override
    public int getItemCount() {
        return garageInfoModels.size();
    }

    public interface GarageLisenter{
        void  onGarageLisenter(GarageInfoModel garageInfoModel);
    }

    public class GarageViewHolder extends RecyclerView.ViewHolder{
        TextView name  , rate , numOfRats , price;
        View viewGarageLisenter;
        public GarageViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name_garage);
            rate = itemView.findViewById(R.id.rate_req_garage);
            numOfRats = itemView.findViewById(R.id.num_rate_req_garage);
            price = itemView.findViewById(R.id.price_garage);
            viewGarageLisenter = itemView.findViewById(R.id.layout_garage_lisenter);
        }

        public void  bulidUI(GarageInfoModel info){
            ratings =   context.getString(R.string.ratings) ;
            egPound =  context.getString(R.string.eg);
            nonRate = context.getString(R.string.not_rate);
            if(Locale.getDefault().getLanguage().equals("en")){
                name.setText(info.getNameEn());
            }else { name.setText(info.getNameAr()); }
            price.setText(String.format(" %.2f %s" , info.getPriceForHour() , egPound));
            if(info.getNumOfRatings()!=0) {
                float ratting = info.getRate() / (2*info.getNumOfRatings());
                rate.setText(String.format("%.2f ", ratting));
                numOfRats.setText(String.format(" %d ( %s )",info.getNumOfRatings(),ratings));
            }else { numOfRats.setText(nonRate); }
            viewGarageLisenter.setOnClickListener(v -> garageLisenter.onGarageLisenter(garageInfoModels.get(getLayoutPosition())));
        }
    }
}
