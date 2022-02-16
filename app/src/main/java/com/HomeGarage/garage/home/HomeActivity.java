package com.HomeGarage.garage.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.HomeGarage.garage.R;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

   private DrawerLayout drawerLayout;
    private NavigationView  navigationView;

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_home);

            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.main_nave_view);

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
}