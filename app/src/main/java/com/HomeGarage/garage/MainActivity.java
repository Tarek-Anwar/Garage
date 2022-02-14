package com.HomeGarage.garage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.HomeGarage.garage.DB.AppDataBase;
import com.HomeGarage.garage.DB.AppExcutor;
import com.HomeGarage.garage.DB.GrageInfo;
import com.HomeGarage.garage.sign.LoginFragment;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //adding login fragment to layout
       /* frameLayout = findViewById(R.id.framelayout);
        loginFragment=new LoginFragment();
        getSupportFragmentManager().
                beginTransaction().
                add(frameLayout.getId(),loginFragment)
                .commit();*/


    }

}