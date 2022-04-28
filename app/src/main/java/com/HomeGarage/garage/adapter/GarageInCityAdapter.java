package com.HomeGarage.garage.adapter;

import android.content.Context;
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
import com.HomeGarage.garage.modules.GarageInfoModule;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class GarageInCityAdapter extends RecyclerView.Adapter<GarageInCityAdapter.GarageViewHolder> {

    ArrayList<GarageInfoModule> garageInfoModules;
    GarageLisenter garageLisenter;
    Context context;

    public   GarageInCityAdapter(ArrayList<GarageInfoModule> garageInfoModules, Context context, GarageLisenter garageLisenter ){
        this.garageLisenter = garageLisenter;
        this.context = context;
        this.garageInfoModules = garageInfoModules;
        notifyDataSetChanged();
    }

    @Override
    public GarageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_layout,parent,false);
       return new GarageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull GarageViewHolder holder, int position) {
            holder.bulidUI(garageInfoModules.get(position));
    }

    @Override
    public int getItemCount() {
        return garageInfoModules.size();
    }

    public interface GarageLisenter{ void  onGarageLisenter(GarageInfoModule garageInfoModule);}

    public class GarageViewHolder extends RecyclerView.ViewHolder{
        TextView textName  , textRate , textNumOfRats , textPriceForHour;
        ImageView imageGarage;
        View viewGarageLisenter;
        public GarageViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.txt_name_garage);
            textRate = itemView.findViewById(R.id.rate_req_garage);
            textNumOfRats = itemView.findViewById(R.id.num_rate_req_garage);
            textPriceForHour = itemView.findViewById(R.id.price_garage);
            viewGarageLisenter = itemView.findViewById(R.id.layout_garage_lisenter);
            imageGarage = itemView.findViewById(R.id.image_garage_city);
        }

        public void  bulidUI(GarageInfoModule info){
            String ratingsText = context.getString(R.string.ratings) ;
            String egPoundText = context.getString(R.string.eg);
            String nonRateText = context.getString(R.string.not_rate);
            float ratting = info.getRate() / (2*info.getNumOfRatings());

            textName.setText(Locale.getDefault().getLanguage().equals("en") ? info.getNameEn() : info.getNameAr());
            textNumOfRats.setText(String.format(" %.2f %s" , info.getPriceForHour() , egPoundText));


            if(info.getImageGarage()!=null){
                showImage(info.getImageGarage(),imageGarage);
            }

            if(info.getNumOfRatings()!=0) {
                textRate.setText(String.format("%.2f ", ratting));
                textNumOfRats.setText(String.format(" %d ( %s )",info.getNumOfRatings(),ratingsText));
            }
            else textNumOfRats.setText(nonRateText);
            viewGarageLisenter.setOnClickListener(v -> garageLisenter.onGarageLisenter(garageInfoModules.get(getLayoutPosition())));
        }

        private void showImage(String url , ImageView imageView) {
            if (url != null && !url.isEmpty()) {
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                Picasso.get().load(url).resize(width, width).centerCrop().into(imageView); } }
    }
}
