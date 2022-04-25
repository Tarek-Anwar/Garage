package com.HomeGarage.garage;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.HomeGarage.garage.home.HomeActivity;
import com.HomeGarage.garage.navfragment.SettingFragment;
import com.HomeGarage.garage.service.ConnectionReceiver;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import yanzhikai.textpath.AsyncTextPathView;

public class SplashScreenActivity extends AppCompatActivity implements ConnectionReceiver.ReceiverListener {

    AsyncTextPathView textHomeCar;
    ImageView imageSplash;
    SharedPreferences preferences;
    private ConnectionReceiver myReceiver = null;
    private FirebaseUser cruuentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseUtil.getInstence();

        cruuentUser = FirebaseUtil.firebaseAuth.getCurrentUser();

        preferences =  getSharedPreferences(getString(R.string.file_info_user), Context.MODE_PRIVATE);
        String lang = preferences.getString(SettingFragment.LANG_APP,"en");
        if(lang.equals("en")) setLangApp("en");
        else setLangApp("ar");

        imageSplash = findViewById(R.id.image_splash);
        textHomeCar = findViewById(R.id.home_car_text);

        Animation animationInScreen = AnimationUtils.loadAnimation(getApplicationContext() ,R.anim.in_screan);
        Animation animationZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);

        imageSplash.startAnimation(animationInScreen);
        textHomeCar.startAnimation(animationZoomOut);

        textHomeCar.setPathPainter((x, y, paintPath) -> paintPath.addCircle(x,y,6,Path.Direction.CCW));
        textHomeCar.setFillColor(true);
        textHomeCar.startAnimation(0,1);

        myReceiver = new ConnectionReceiver();
        ConnectionReceiver.Listener = this;
        broadcastIntent();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLogin(isFind -> {
            Class activity = isFind ? HomeActivity.class : MainActivity.class;
            startHomeActivity(activity);
        });
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        if(!isConnected) showSnackBar(isConnected ,imageSplash , getApplicationContext() , Snackbar.LENGTH_INDEFINITE);
    }

    public void broadcastIntent() {
        registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void showSnackBar(boolean isConnected ,View view ,Context context , int timeSnackbar) {
        String message;
        int color;
        if (isConnected) {
            message = context.getString(R.string.connected_to_Internet);
            color = Color.GREEN;
        }else {
            message = context.getString(R.string.not_connected_to_internet);
            color = Color.RED; }
        Snackbar snackbar = Snackbar.make(view, message,timeSnackbar);
        View snackbarVeiw = snackbar.getView();
        TextView textView = snackbarVeiw.findViewById(R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    void startHomeActivity(Class activity){
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this,activity);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }, 4500);
    }

    private void setLangApp(String lang){
        Configuration resources = getApplicationContext().getResources().getConfiguration();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        resources.locale = locale;
        getBaseContext().getResources().updateConfiguration(resources,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private interface OnInfoArriveCallback{ void infoArriveCallback(Boolean isFind);}

    private  void checkLogin(OnInfoArriveCallback callback) {
        callback.infoArriveCallback(cruuentUser!=null ? true : false);
    }

}