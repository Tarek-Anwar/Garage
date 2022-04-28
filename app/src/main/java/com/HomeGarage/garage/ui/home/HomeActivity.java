package com.HomeGarage.garage.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.MainActivity;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.SplashScreenActivity;
import com.HomeGarage.garage.databinding.ActivityHomeBinding;
import com.HomeGarage.garage.modules.CarInfoModule;
import com.HomeGarage.garage.modules.OpreationModule;
import com.HomeGarage.garage.service.ConnectionReceiver;
import com.HomeGarage.garage.ui.navfragment.BalanceFragment;
import com.HomeGarage.garage.ui.navfragment.PayFragment;
import com.HomeGarage.garage.ui.navfragment.SettingFragment;
import com.HomeGarage.garage.ui.reservation.RequstActiveFragment;
import com.HomeGarage.garage.util.FirebaseUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity  implements ConnectionReceiver.ReceiverListener {

    ArrayList<CarInfoModule> carInfoModuleUtil;
    ActivityHomeBinding binding;
    float currnetBalance;
    private ConnectionReceiver myReceiver = null;
    private DrawerLayout drawerLayout;
    private TextView textName , textEmail , textPhone , textBalance ;
    private ImageView imageProfile , imageLogout , imageSetting;
    private LinearLayout  layoutPayment , layoutInfoBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseUtil.getInstence();

        //Check Connection Internet
        myReceiver = new ConnectionReceiver();
        ConnectionReceiver.Listener = this;
        broadcastIntent();

        getUserInfo(carInfo -> {
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

        String goodbye =  getString(R.string.goodbye);

        imageLogout.setOnClickListener(v12 -> {
            //clear Setting
            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.file_info_user), Context.MODE_PRIVATE).edit();
            editor.clear(); editor.apply();
            // clear subscribe notifation
            FirebaseMessaging.getInstance().unsubscribeFromTopic(FirebaseUtil.firebaseAuth.getUid());
            FirebaseUtil.firebaseAuth.signOut();
            Toast.makeText(getApplicationContext(), goodbye, Toast.LENGTH_LONG).show();
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

        getUserInfo(carInfo -> {
            setHeaderNav(carInfo);
            showImage(carInfo.getImageUrl());
        });

        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseUtil.firebaseAuth.getUid());
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        if(!isConnected)
            SplashScreenActivity.showSnackBar(isConnected,findViewById(R.id.fragmentContainerView),getApplicationContext(),Snackbar.LENGTH_INDEFINITE);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    void setHeaderNav(CarInfoModule carInfoModule){
        if (carInfoModule != null) {
            textName.setText(carInfoModule.getName());
            textPhone.setText(carInfoModule.getPhone());
            textEmail.setText(carInfoModule.getEmail());
            textBalance.setText(String.format("%.2f %s", carInfoModule.getBalance(),getString(R.string.eg)));
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

    private void getUserInfo(OnUserInfoArriveCallback callback){
        carInfoModuleUtil = FirebaseUtil.carInfoModuleLogin;
        DatabaseReference ref = FirebaseUtil.databaseReference.child(FirebaseUtil.firebaseAuth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CarInfoModule carInfoModule = snapshot.getValue(CarInfoModule.class);
                carInfoModuleUtil.add(carInfoModule);
                currnetBalance = carInfoModule.getBalance();
                callback.infoUser(carInfoModule);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void checkResetvation(CheckResetvationCallback callback) {
        DatabaseReference reference = FirebaseUtil.referenceOperattion;
        Query query = reference.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        OpreationModule opreationModule = snapshot1.getValue(OpreationModule.class);
                        assert opreationModule != null;
                        if (((opreationModule.getState().equals("1") || opreationModule.getState().equals("2") ) &&
                                (opreationModule.getType().equals("1") || opreationModule.getType().equals("2") ))
                                || opreationModule.getPrice() < 0) {
                            callback.onCheckResetvation(opreationModule);
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


    private interface CheckResetvationCallback{ void onCheckResetvation(OpreationModule opreationModule);}

    private interface OnUserInfoArriveCallback{ void infoUser(CarInfoModule carInfoModule);}


}