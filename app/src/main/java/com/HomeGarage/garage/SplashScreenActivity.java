package com.HomeGarage.garage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.HomeGarage.garage.home.HomeActivity;

import yanzhikai.textpath.AsyncTextPathView;
import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.calculator.PathCalculator;
import yanzhikai.textpath.painter.ArrowPainter;
import yanzhikai.textpath.painter.AsyncPathPainter;

public class SplashScreenActivity extends AppCompatActivity {

    AsyncTextPathView homeCar;
    ImageView imageSplash;
    LinearLayout layoutSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageSplash = findViewById(R.id.image_splash);
        layoutSplash = findViewById(R.id.layout_splash);
        homeCar = findViewById(R.id.home_car_text);

        Animation animationInScreen = AnimationUtils.loadAnimation(getApplicationContext() ,R.anim.in_screan);
        Animation animationZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);

        imageSplash.startAnimation(animationInScreen);
        homeCar.startAnimation(animationZoomOut);

        homeCar.setPathPainter((x, y, paintPath) -> paintPath.addCircle(x,y,6,Path.Direction.CCW));
        homeCar.setFillColor(true);
        homeCar.startAnimation(0,1);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this , HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }, 4500);
    }

}