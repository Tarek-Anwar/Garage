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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    ArrayList<GarageInfoModel> garageInfoModels;
    Context context;
    SearchListener searchListener;
    String ratings;
    String egPound;

    public SearchAdapter(Context context, SearchListener searchListener) {
        this.context = context;
        this.searchListener = searchListener;
        ratings =  " " + context.getString(R.string.ratings) + " )";
        egPound = " " + context.getString(R.string.eg);
    }

    public void setGrageInfos(ArrayList<GarageInfoModel> garageInfoModels) {
        this.garageInfoModels = garageInfoModels;
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
        holder.BulidUI(garageInfoModels.get(position));
    }

    @Override
    public int getItemCount() {
        if(garageInfoModels ==null)
            return 0;
        return garageInfoModels.size();
    }

    public interface SearchListener{
        void  SearchListener(GarageInfoModel garageInfoModel);
    }

    protected class SearchViewHolder extends RecyclerView.ViewHolder {

        private TextView nameGarage , rate , numOfRats , price ;
        private View layouthListener;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            nameGarage = itemView.findViewById(R.id.txt_name_garage);
            rate = itemView.findViewById(R.id.rate_req_garage);
            numOfRats = itemView.findViewById(R.id.num_rate_req_garage);
            price = itemView.findViewById(R.id.price_garage);
            layouthListener = itemView.findViewById(R.id.layout_garage_lisenter);
        }

        public void BulidUI(GarageInfoModel garageInfoModel){
            nameGarage.setText(garageInfoModel.getNameEn());
            price.setText(garageInfoModel.getPriceForHour()+egPound);
            if(garageInfoModel.getNumOfRatings()!=0) {
                float ratting = garageInfoModel.getRate() /((float) garageInfoModel.getNumOfRatings());
                rate.setText(String.format("%.2f",ratting));
                numOfRats.setText( " ( "+ garageInfoModel.getNumOfRatings() +ratings);
            }

            layouthListener.setOnClickListener(V-> {
                searchListener.SearchListener(garageInfoModel);
            });
        }
    }
}
