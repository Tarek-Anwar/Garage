package com.HomeGarage.garage.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeActivity;

import java.util.Locale;


public class SettingFragment extends Fragment {

    public static final String LOCATIOON_SETTINNG = "LOCATIOON_SETTINNG";
    public static final String CITY_SETTINNG = "CITY_SETTINNG";
    public static final String LANG_APP = "LANG_APP";
    Switch location , city;
    RadioGroup radioGroup;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    RadioButton arab , eng;

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

       arab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               resterLang("ar");
           }
       });
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resterLang("en");
            }
        });

        boolean blLocation = preferences.getBoolean(LOCATIOON_SETTINNG,false);
        boolean blCity = preferences.getBoolean(CITY_SETTINNG,false);
        String lang = preferences.getString(LANG_APP,"en");

        if(lang.equals("en")) eng.setChecked(true);
        else arab.setChecked(true);

        location.setChecked(blLocation);
        city.setChecked(blCity);
        return root;
    }

    private void intiUI(View view){
        location = view.findViewById(R.id.location_automatic);
        city = view.findViewById(R.id.city_all_garage);
        radioGroup = view.findViewById(R.id.lang_select);
        arab = view.findViewById(R.id.arab_lang);
        eng = view.findViewById(R.id.eng_lang);
    }

    private void resterLang(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config, getActivity().getResources().getDisplayMetrics());
        editor.putString(LANG_APP,lang);
        editor.apply();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}