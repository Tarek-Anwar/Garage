package com.HomeGarage.garage.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.FindModels;

import java.util.ArrayList;

public class FindAdpter extends RecyclerView.Adapter<FindAdpter.FindViewHolder> {

    Context context;
    ArrayList <FindModels>  findModelsList;

    public FindAdpter(Context context, ArrayList<FindModels> findModelsList) {
        this.context = context;
        this.findModelsList = findModelsList;
    }

    @NonNull
    @Override
    public FindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_garage_row_layout,parent,false);
        return new FindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindViewHolder holder, int position) {
        holder.BulidUi(findModelsList.get(position));
    }

    @Override
    public int getItemCount() {
        return findModelsList.size();
    }

    protected static final class FindViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageFind;
        private TextView txtFind;
        public FindViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFind = itemView.findViewById(R.id.img_find);
            txtFind = itemView.findViewById(R.id.txt_find);
        }

        public void BulidUi(FindModels findModels){
            imageFind.setImageResource(findModels.getImgFind());
            txtFind.setText(findModels.getTxtFind());
        }
    }
}
