package com.HomeGarage.garage.home;

import android.content.Context;
import android.content.Intent;
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

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.sign.SignUpFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView name , address ,email , phone;
    ImageView img_profile;
    SharedPreferences preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //find element
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.main_nave_view);
        //find header Navigation
        View v = navigationView.getHeaderView(0);
        intiHeader(v);

        // defined  file preferences and mode
        preferences = getSharedPreferences(getString(R.string.file_info_user),Context.MODE_PRIVATE);

        //set usr information if their
        setHeaderNav(preferences);

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
                Toast.makeText(getApplicationContext(), "Log Out .... ", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
                return true;
            });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // check any change date in Header after edit
        setHeaderNav(preferences);
    }

    void intiHeader(View v){
        name = v.findViewById(R.id.user_name_nav);
        address = v.findViewById(R.id.user_address_nav);
        email = v.findViewById(R.id.user_email_nav);
        phone = v.findViewById(R.id.user_phone_nav);
        img_profile = v.findViewById(R.id.img_profile);
    }

    void setHeaderNav(SharedPreferences preferences){
        if (preferences.getString(SignUpFragment.USER_NAME,null)!= null) {
            name.setText(preferences.getString(SignUpFragment.USER_NAME, "New User"));
            address.setText(preferences.getString(SignUpFragment.ADDRESS, "NO Address yet"));
            phone.setText(preferences.getString(SignUpFragment.PHONE, "No Phone yet"));
            email.setText(preferences.getString(SignUpFragment.EMAIL, "No Email yet"));
        }
        if(preferences.getString(UserInfoActivity.IMAGE_PROFILE,null)!= null){
            img_profile.setImageURI(Uri.parse((preferences.getString(UserInfoActivity.IMAGE_PROFILE,null))));
        }
    }

}