package com.HomeGarage.garage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.HomeGarage.garage.DB.AppDataBase;
import com.HomeGarage.garage.DB.AppExcutor;
import com.HomeGarage.garage.DB.GrageInfo;
import com.HomeGarage.garage.sign.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}