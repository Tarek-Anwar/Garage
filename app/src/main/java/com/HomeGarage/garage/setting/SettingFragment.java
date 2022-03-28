package com.HomeGarage.garage.setting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.HomeGarage.garage.R;


public class SettingFragment extends Fragment {

    Switch location;

    public SettingFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        intiUI(root);

        location.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.i("sdfwstr" , isChecked+"");
            Log.i("sdfwstr" , isChecked+"");
        });

        return root;
    }

    private void intiUI(View view){
        location = view.findViewById(R.id.location_automatic);
    }
}