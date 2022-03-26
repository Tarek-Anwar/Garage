package com.HomeGarage.garage.home.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    ArrayList<String> cityList ;
    CityListener cityListener;

    public CityAdapter(ArrayList<String> cityList , CityListener cityListener){
        this.cityListener = cityListener;
        this.cityList=cityList;
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

    public class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;
        View layouCity;
        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.txt_city_name);
            layouCity = itemView.findViewById(R.id.layout_city_Listener);
        }

        public void BulidUI(String s){
            cityName.setText(s);
            layouCity.setOnClickListener(v -> cityListener.onCityListener(s));
        }
    }

    public interface CityListener{
        void onCityListener(String s);
    }
}
