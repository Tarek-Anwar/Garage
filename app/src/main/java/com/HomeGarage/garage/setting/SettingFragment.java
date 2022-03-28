package com.HomeGarage.garage.setting;

import android.content.Context;
import android.content.SharedPreferences;
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

    Switch location , city;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    public static final String LOCATIOON_SETTINNG = "LOCATIOON_SETTINNG";
    public static final String CITY_SETTINNG = "CITY_SETTINNG";

    public SettingFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(getString(R.string.file_info_user),Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        intiUI(root);

        location.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean(LOCATIOON_SETTINNG , isChecked);
            editor.apply();
        });

        city.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean(CITY_SETTINNG , isChecked);
            editor.apply();
        });


        boolean blLocation = preferences.getBoolean(LOCATIOON_SETTINNG,false);
        boolean blCity = preferences.getBoolean(CITY_SETTINNG,false);

        location.setChecked(blLocation);
        city.setChecked(blCity);
        return root;
    }

    private void intiUI(View view){
        location = view.findViewById(R.id.location_automatic);
        city = view.findViewById(R.id.city_all_garage);
    }
}