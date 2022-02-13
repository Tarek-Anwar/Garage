package com.HomeGarage.garage.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.sign.LoginFragment;

public class HomeActivity extends AppCompatActivity {
    FrameLayout framehome;
    HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*framehome = findViewById(R.id.framehome);
        homeFragment =new HomeFragment();
        getSupportFragmentManager().
                beginTransaction().
                add(framehome.getId(),homeFragment)
                .commit();*/
    }
}