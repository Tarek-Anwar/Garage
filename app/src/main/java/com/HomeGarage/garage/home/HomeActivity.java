package com.HomeGarage.garage.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.home.reservation.RequstActiveFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity  {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView name ,email , phone , balance;
    private ImageView img_profile;
    ArrayList<CarInfo> carInfoUtil ;
    private FirebaseUser  user;
    ActivityHomeBinding binding;

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
        //find header Navigation
        View v = navigationView.getHeaderView(0);
        intiHeader(v);

        //go to edit User information
        img_profile.setOnClickListener( V->{
                Intent intent = new Intent(HomeActivity.this,UserInfoActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
        });

        // set action Bar to Navigation
        actionBarDrawerToggle = new ActionBarDrawerToggle(this ,drawerLayout,R.string.open_menu,R.string.close_menu);
        actionBarDrawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // set listener to item in navigation
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.log_out_nav) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(user.getUid());
                FirebaseUtil.firebaseAuth.signOut();

                Toast.makeText(getApplicationContext(), "Logging Out .. ", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
                return true;
            });

        checkLogin();
    }

    void intiHeader(View v){
        name = v.findViewById(R.id.user_name_nav);
        email = v.findViewById(R.id.user_email_nav);
        phone = v.findViewById(R.id.user_phone_nav);
        balance = v.findViewById(R.id.user_balance_nav);
        img_profile = v.findViewById(R.id.img_profile);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    void setHeaderNav(CarInfo carInfo){
        if (carInfo != null) {
            name.setText(carInfo.getName());
            phone.setText(carInfo.getPhone());
            email.setText(carInfo.getEmail());
            balance.setText("balance " +carInfo.getBalance() + " EG");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
        FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());
    }

    private  void checkLogin() {
        if (user == null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            carInfoUtil = FirebaseUtil.carInfoLogin;
            DatabaseReference ref = FirebaseUtil.databaseReference.child(user.getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    CarInfo carInfo = snapshot.getValue(CarInfo.class);
                    carInfoUtil.add(carInfo);
                    setHeaderNav(carInfo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
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
                        if (  ((opreation.getState().equals("1") || opreation.getState().equals("2") ) &&
                                (opreation.getType().equals("1") || opreation.getType().equals("2") ))
                                ||
                                opreation.getPrice() < 0
                        ) {
                            callback.onCheckResetvation(opreation);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private interface checkResetvationCallback{
        void onCheckResetvation(Opreation opreation);
    }
}