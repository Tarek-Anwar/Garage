package com.HomeGarage.garage.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.DB.GrageInfo;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    List<GrageInfo> grageInfos;
    Context context;
    SearchListener searchListener;

    public SearchAdapter(Context context, SearchListener searchListener) {
        this.context = context;
        this.searchListener = searchListener;
    }

    public void setGrageInfos(List<GrageInfo> grageInfos) {
        this.grageInfos= grageInfos;
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
        holder.BulidUI(grageInfos.get(position));
    }

    @Override
    public int getItemCount() {
        if(grageInfos==null)
            return 0;
        return grageInfos.size();
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

        public void BulidUI(GrageInfo grageInfo){
            nameGarage.setText(grageInfo.getGrageName());
            addressGarage.setText(grageInfo.getGovernoate()+" "+grageInfo.getCity()+"\n"+grageInfo.getRestOfAddress());

            layouthListener.setOnClickListener(V-> {
                searchListener.SearchListener(grageInfo);
            });
        }
    }
    public interface SearchListener{
        void  SearchListener(GrageInfo grageInfo);
    }
}
