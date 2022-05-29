package com.HomeGarage.garage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.HomeGarage.garage.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdpter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context context;

    public CustomInfoWindowAdpter(Context context) {
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);
        this.context = context;
    }

    private void rendowWindowText(Marker marker , View view){
        String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.name_garage_info);
        if(!title.equals("")){
            tvTitle.setText(title);
        }
        String snippet = marker.getSnippet();
        TextView tvSnippet = view.findViewById(R.id.body_garage_info);
        if(snippet!=null){
            tvSnippet.setText(snippet);
        }
    }
    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }
}
