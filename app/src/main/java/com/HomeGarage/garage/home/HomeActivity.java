package com.HomeGarage.garage.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.sign.SignUpFragment;
import com.HomeGarage.garage.sign.UserInfoFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView  navigationView;
    private  View v;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView name , address ,email , phone;
    ImageView img_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.main_nave_view);
        v = navigationView.getHeaderView(0);

        intiHeader(v);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.file_info_user),Context.MODE_PRIVATE);
        if (preferences.getString(SignUpFragment.USER_NAME,null)!= null) {
            name.setText(preferences.getString(SignUpFragment.USER_NAME, "New User"));
            address.setText(preferences.getString(SignUpFragment.ADDRESS, "NO Address yet"));
            phone.setText(preferences.getString(SignUpFragment.PHONE, "No Phone yet"));
            email.setText(preferences.getString(SignUpFragment.EMAIL, "No Email yet"));
        }
        if(preferences.getString(UserInfoFragment.IMAGE_PRFILE,null)!= null){
            img_profile.setImageURI(Uri.parse((preferences.getString(UserInfoFragment.IMAGE_PRFILE,null))));
        }
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_SHORT).show();
                UserInfoFragment newFragment = new UserInfoFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this ,drawerLayout,R.string.open_menu,R.string.close_menu);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.log_out_nav:
                        Toast.makeText(getApplicationContext(), "Log Out .... ", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    return true;
                }
            });
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void intiHeader(View v){
        name = v.findViewById(R.id.user_name_nav);
        address = v.findViewById(R.id.user_address_nav);
        email = v.findViewById(R.id.user_email_nav);
        phone = v.findViewById(R.id.user_phone_nav);
        img_profile = v.findViewById(R.id.img_profile);
    }

}