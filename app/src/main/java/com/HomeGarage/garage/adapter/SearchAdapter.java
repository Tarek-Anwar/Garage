package com.HomeGarage.garage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.GarageInfoModule;

import java.util.ArrayList;
import java.util.Locale;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    ArrayList<GarageInfoModule> garageInfoModules;
    Context context;
    SearchListener searchListener;

    public SearchAdapter(Context context, SearchListener searchListener) {
        this.context = context;
        this.searchListener = searchListener;
    }

    public void setGrageInfos(ArrayList<GarageInfoModule> garageInfoModules) {
        this.garageInfoModules = garageInfoModules;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_layout,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.BulidUI(garageInfoModules.get(position));
    }

    @Override
    public int getItemCount() {
        if(garageInfoModules ==null) return 0;
        return garageInfoModules.size();
    }

    public interface SearchListener{ void  SearchListener(GarageInfoModule garageInfoModule);}

    protected class SearchViewHolder extends RecyclerView.ViewHolder {

        private TextView textNameGarage , textRate , textNumOfRats , textPriceForHour ;
        private View layouthListener;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textNameGarage = itemView.findViewById(R.id.txt_name_garage);
            textRate = itemView.findViewById(R.id.rate_req_garage);
            textNumOfRats = itemView.findViewById(R.id.num_rate_req_garage);
            textPriceForHour = itemView.findViewById(R.id.price_garage);
            layouthListener = itemView.findViewById(R.id.layout_garage_lisenter);
        }

        public void BulidUI(GarageInfoModule module){

            String ratingsText =  context.getString(R.string.ratings);
            String egPoundText = context.getString(R.string.eg);
            String notRateText = context.getString(R.string.not_rate);
            float ratting = module.getRate() /((float) module.getNumOfRatings());

            textNameGarage.setText(Locale.getDefault().getLanguage().equals("en") ? module.getNameEn() : module.getNameAr());
            textPriceForHour.setText(String.format("%.2f %s",module.getPriceForHour(),egPoundText));

            if(module.getNumOfRatings()!=0) {
                textRate.setText(String.format("%.2f",ratting));
                textNumOfRats.setText(String.format(" %d ( %s ) " ,module.getNumOfRatings(),ratingsText));
            }else{ textRate.setText(notRateText);textNumOfRats.setText(""); }

            layouthListener.setOnClickListener(V-> searchListener.SearchListener(module));
        }
    }
}
