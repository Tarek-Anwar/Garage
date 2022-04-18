package com.HomeGarage.garage.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.models.CityModel;

import java.util.ArrayList;
import java.util.Collection;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder>  implements Filterable {

    ArrayList<CityModel> cityList ;
    ArrayList<CityModel> cityFilter;
    CityListener cityListener;
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<CityModel> filterlist = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filterlist.addAll(cityFilter);
            }else {
                for(CityModel city : cityFilter){
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
            cityList.addAll((Collection<? extends CityModel>) results.values);
            notifyDataSetChanged();
        }
    };
    public CityAdapter(ArrayList<CityModel> cityList , CityListener cityListener){
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
        void onCityListener(CityModel cityModel);
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

        public void BulidUI(CityModel cityModel){
            cityName.setText(cityModel.getCity_name_en());
            if(cityModel.getNumberGarage()==0){
                numOfGarage.setText(itemView.getContext().getText(R.string.coom_soon));
            }else {
                numOfGarage.setText(cityModel.getNumberGarage()+" Garages"); }
            layouCity.setOnClickListener(v -> cityListener.onCityListener(cityModel));
        }
    }
}
