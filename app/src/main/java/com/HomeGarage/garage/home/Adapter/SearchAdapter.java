package com.HomeGarage.garage.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.SearchModel;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    ArrayList<SearchModel>  searchModelList;
    Context context;
    SearchListener searchListener;

    public SearchAdapter(ArrayList<SearchModel> searchModelList, Context context, SearchListener searchListener) {
        this.searchModelList = searchModelList;
        this.context = context;
        this.searchListener = searchListener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_layout,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.BulidUI(searchModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return searchModelList.size();
    }

    protected class SearchViewHolder extends RecyclerView.ViewHolder {

        private TextView nameGarage , addressGarage;
        private View layouthListener;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            nameGarage = itemView.findViewById(R.id.txt_name_garage);
            addressGarage = itemView.findViewById(R.id.txt_address_garage);
            layouthListener = itemView.findViewById(R.id.layout_garage_lisenter);
        }

        public void BulidUI(SearchModel model){
            nameGarage.setText(model.getNameGarage());
            addressGarage.setText(model.getAddressGarage());

            layouthListener.setOnClickListener(V-> {
                searchListener.SearchListener(model);
            });
        }
    }
    public interface SearchListener{
        void  SearchListener(SearchModel searchModel);
    }
}
