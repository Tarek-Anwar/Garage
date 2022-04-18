package com.HomeGarage.garage.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.CityModule;

import java.util.ArrayList;
import java.util.Collection;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder>  implements Filterable {

    ArrayList<CityModule> cityList ;
    ArrayList<CityModule> cityFilter;
    CityListener cityListener;
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<CityModule> filterlist = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filterlist.addAll(cityFilter);
            }else {
                for(CityModule city : cityFilter){
                    if(city.getCity_name_en().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filterlist.add(city);
                    } } }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cityList.clear();
            cityList.addAll((Collection<? extends CityModule>) results.values);
            notifyDataSetChanged();
        }
    };
    public CityAdapter(ArrayList<CityModule> cityList , CityListener cityListener){
        this.cityListener = cityListener;
        this.cityList=cityList;
        cityFilter = new ArrayList<>(cityList);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_in_cover_row,parent,false);
        return new CityViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.BulidUI(cityList.get(position));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public interface CityListener{
        void onCityListener(CityModule cityModule);
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityName , numOfGarage;
        View layouCity;
        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.txt_city_name);
            layouCity = itemView.findViewById(R.id.layout_city_Listener);
            numOfGarage = itemView.findViewById(R.id.txtt_num_of_garage);
        }

        public void BulidUI(CityModule cityModule){
            cityName.setText(cityModule.getCity_name_en());
            if(cityModule.getNumberGarage()==0){
                numOfGarage.setText(itemView.getContext().getText(R.string.coom_soon));
            }else {
                numOfGarage.setText(cityModule.getNumberGarage()+" Garages"); }
            layouCity.setOnClickListener(v -> cityListener.onCityListener(cityModule));
        }
    }
}
