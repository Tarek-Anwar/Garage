package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.MainActivity;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.databinding.ActivityHomeBinding;
import com.HomeGarage.garage.home.models.CarInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.home.navfragment.BalanceFragment;
import com.HomeGarage.garage.home.navfragment.OnSwipeTouchListener;
import com.HomeGarage.garage.home.navfragment.PayFragment;
import com.HomeGarage.garage.home.reservation.RequstActiveFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity  {

    private DrawerLayout drawerLayout;
    private TextView name ,email , phone , balance;
    private ImageView img_profile , logout , info;
    private LinearLayout payment , infoBalance;
    ArrayList<CarInfo> carInfoUtil ;
    private FirebaseUser  user;
    ActivityHomeBinding binding;
    float currnetBalance;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //find element
        FirebaseUtil.getInstence("CarInfo" , "Operation","GaragerOnwerInfo");
        user = FirebaseUtil.firebaseAuth.getCurrentUser();

        checkResetvation(opreation -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new RequstActiveFragment(opreation ,this));
            transaction.commit();
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.main_nave_view);
        View v = navigationView.getHeaderView(0);
        intiHeader(v);

        img_profile.setOnClickListener( V->{
                Intent intent = new Intent(HomeActivity.this,UserInfoActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
        });

        payment.setOnClickListener(v1 -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new PayFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            drawerLayout.closeDrawer(GravityCompat.START);

        });

        infoBalance.setOnClickListener(v13 -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new BalanceFragment(currnetBalance));
            transaction.addToBackStack(null);
            transaction.commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        logout.setOnClickListener(v12 -> {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(user.getUid());
            FirebaseUtil.firebaseAuth.signOut();
            Toast.makeText(getApplicationContext(), "Logging Out .. ", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        checkLogin(carInfo -> {
            setHeaderNav(carInfo);
            showImage(carInfo.getImageUrl()); });
    }

    void intiHeader(View v){
        name = v.findViewById(R.id.user_name_nav);
        email = v.findViewById(R.id.user_email_nav);
        phone = v.findViewById(R.id.user_phone_nav);
        balance = v.findViewById(R.id.user_balance_nav);
        img_profile = v.findViewById(R.id.img_profile);
        logout = v.findViewById(R.id.img_logout);
        info = v.findViewById(R.id.img_info);
        payment = v.findViewById(R.id.layout_payment_head);
        infoBalance = v.findViewById(R.id.layout_balance_head);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    void setHeaderNav(CarInfo carInfo){
        if (carInfo != null) {
            name.setText(carInfo.getName());
            phone.setText(carInfo.getPhone());
            email.setText(carInfo.getEmail());
            balance.setText(String.format("%.2f",carInfo.getBalance()) + " E.G.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin(carInfo -> {
            setHeaderNav(carInfo);
            showImage(carInfo.getImageUrl()); });
        FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());
    }

    private  void checkLogin(OnInfoArriveCallbacl callback) {
        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else {
            carInfoUtil = FirebaseUtil.carInfoLogin;
            DatabaseReference ref = FirebaseUtil.databaseReference.child(user.getUid());
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

    private void checkResetvation(checkResetvationCallback callback) {
        DatabaseReference reference = FirebaseUtil.referenceOperattion;
        Query query = reference.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Opreation opreation = snapshot1.getValue(Opreation.class);
                        assert opreation != null;
                        if (  ((opreation.getState().equals("1") || opreation.getState().equals("2") ) &&
                                (opreation.getType().equals("1") || opreation.getType().equals("2") ))
                                || opreation.getPrice() < 0) {
                            callback.onCheckResetvation(opreation); } } } }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }


    private interface checkResetvationCallback{ void onCheckResetvation(Opreation opreation);}

    private interface OnInfoArriveCallbacl{ void infoArriveCallback(CarInfo carInfo);}

    private void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width, width).centerCrop().into(img_profile); } }
}