package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.MainActivity;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.SplashScreenActivity;
import com.HomeGarage.garage.databinding.ActivityHomeBinding;
import com.HomeGarage.garage.models.CarInfo;
import com.HomeGarage.garage.models.Opreation;
import com.HomeGarage.garage.navfragment.BalanceFragment;
import com.HomeGarage.garage.navfragment.PayFragment;
import com.HomeGarage.garage.reservation.RequstActiveFragment;
import com.HomeGarage.garage.setting.SettingFragment;
import com.HomeGarage.garage.utils.ConnectionReceiver;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity  implements ConnectionReceiver.ReceiverListener {

    ArrayList<CarInfo> carInfoUtil ;
    ActivityHomeBinding binding;
    float currnetBalance;
    SharedPreferences preferences;
    Toast toast;
    private ConnectionReceiver myReceiver = null;
    private DrawerLayout drawerLayout;
    private TextView textName , textEmail , textPhone , textBalance ;
    private ImageView imageProfile , imageLogout , imageSetting;
    private LinearLayout  layoutPayment , layoutInfoBalance;
    private FirebaseUser  cruuentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cruuentUser = FirebaseUtil.firebaseAuth.getCurrentUser();

        toast = Toast.makeText(this, "Please , chec time", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);

        //Check Connection Internet
        myReceiver = new ConnectionReceiver();
        ConnectionReceiver.Listener = this;
        broadcastIntent();
        //Check Time
        getTime(offset -> {
            if(offset > 1000 || offset < -1000) {
                startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                toast.show();
            }
        });
        // Check Login
        checkLogin(carInfo -> {
            setHeaderNav(carInfo);
            showImage(carInfo.getImageUrl());
        });
        //Check Operation
        checkResetvation(opreation -> {
            if(!getSupportFragmentManager().isDestroyed()){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new RequstActiveFragment(opreation ,HomeActivity.this));
            transaction.commit();}
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.main_nave_view);
        View v = navigationView.getHeaderView(0);
        intiHeader(v);

        imageProfile.setOnClickListener( V->{
            Intent intent = new Intent(HomeActivity.this,UserInfoActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        imageLogout.setOnClickListener(v12 -> {
            //clear Setting
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear(); editor.apply();
            // clear subscribe notifation
            FirebaseMessaging.getInstance().unsubscribeFromTopic(cruuentUser.getUid());
            FirebaseUtil.firebaseAuth.signOut();
            Toast.makeText(getApplicationContext(), getString(R.string.goodbye), Toast.LENGTH_LONG).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        layoutPayment.setOnClickListener(v1 -> replaceFragement(new PayFragment()));
        imageSetting.setOnClickListener(v14 -> replaceFragement(new SettingFragment()));
        layoutInfoBalance.setOnClickListener(v13 -> replaceFragement(new BalanceFragment(currnetBalance)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin(carInfo -> {
            setHeaderNav(carInfo);
            showImage(carInfo.getImageUrl()); });
        FirebaseMessaging.getInstance().subscribeToTopic(cruuentUser.getUid());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getTime(offset -> {
            if(offset > 1000 || offset < -1000) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
        getTime(offset -> { });
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        if(!isConnected)
            SplashScreenActivity.showSnackBar(isConnected,findViewById(R.id.fragmentContainerView),getApplicationContext(),Snackbar.LENGTH_SHORT);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    void setHeaderNav(CarInfo carInfo){
        if (carInfo != null) {
            textName.setText(carInfo.getName());
            textPhone.setText(carInfo.getPhone());
            textPhone.setText(carInfo.getEmail());
            textBalance.setText(String.format("%.2f",carInfo.getBalance()) + " "+getString(R.string.eg));
        }
    }

    public void broadcastIntent() {
        registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    void intiHeader(View v){
        textName = v.findViewById(R.id.user_name_nav);
        textEmail = v.findViewById(R.id.user_email_nav);
        textPhone = v.findViewById(R.id.user_phone_nav);
        textBalance = v.findViewById(R.id.user_balance_nav);
        imageProfile = v.findViewById(R.id.img_profile);
        imageLogout = v.findViewById(R.id.img_logout);
        layoutPayment = v.findViewById(R.id.layout_payment_head);
        layoutInfoBalance = v.findViewById(R.id.layout_balance_head);
        imageSetting = v.findViewById(R.id.setting_app);
    }

    private void replaceFragement(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private  void checkLogin(OnInfoArriveCallback callback) {
        if (cruuentUser == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else {
            carInfoUtil = FirebaseUtil.carInfoLogin;
            DatabaseReference ref = FirebaseUtil.databaseReference.child(cruuentUser.getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    CarInfo carInfo = snapshot.getValue(CarInfo.class);
                    carInfoUtil.add(carInfo);
                    currnetBalance = carInfo.getBalance();
                    callback.infoArriveCallback(carInfo);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }

    private void checkResetvation(CheckResetvationCallback callback) {
        DatabaseReference reference = FirebaseUtil.referenceOperattion;
        Query query = reference.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Opreation opreation = snapshot1.getValue(Opreation.class);
                        assert opreation != null;
                        if (((opreation.getState().equals("1") || opreation.getState().equals("2") ) &&
                                (opreation.getType().equals("1") || opreation.getType().equals("2") ))
                                || opreation.getPrice() < 0) {
                            callback.onCheckResetvation(opreation);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width, width).centerCrop().into(imageProfile); } }

    private void getTime(CheckCurrerntTimeCallback callback){
        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               long offset = snapshot.getValue(Long.class);
                callback.onOffsetGet(offset);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private interface CheckResetvationCallback{ void onCheckResetvation(Opreation opreation);}

    private interface OnInfoArriveCallback{ void infoArriveCallback(CarInfo carInfo);}

    private interface CheckCurrerntTimeCallback{ void onOffsetGet(long offset);}

}