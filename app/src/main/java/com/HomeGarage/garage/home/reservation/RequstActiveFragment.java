package com.HomeGarage.garage.home.reservation;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.databinding.FragmentRequstActiveBinding;
import com.HomeGarage.garage.home.models.Opreation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class RequstActiveFragment extends Fragment {

    FragmentRequstActiveBinding binding;
    Opreation opreation;
    volatile boolean con;

    int countProgress ;
    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm aa" , new Locale("en"));

    public RequstActiveFragment(Opreation opreation) {
        this.opreation = opreation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRequstActiveBinding.inflate(getLayoutInflater());
        binding.txtDate.setText(opreation.getDate());
        binding.txtGarageName.setText(opreation.getToName());
        binding.txtRequstStateHome.setText(FirebaseUtil.stateList.get(Integer.parseInt(opreation.getState())-1));
        binding.txtRequstTypeHome.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreation.getType())-1));

        Date start = null;
            try { start = formatterLong.parse(opreation.getDate());
            } catch (ParseException e) { e.printStackTrace(); }

        Long diff = System.currentTimeMillis() - start.getTime();

        countProgress = (int) (diff / 10000);
        con = true;
        binding.chronometer.setBase(SystemClock.elapsedRealtime() - diff);
        binding.chronometer.start();
        binding.progressBar.setMin(0);
        binding.progressBar.setMax(1100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
           if(countProgress<1100){
                    binding.progressBar.setProgress(countProgress);
                    countProgress++;
                    handler.postDelayed(this,1000);}
                else if(countProgress==1100){
                    if(con){ binding.progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.cricle_tow));con = false; }
                    else { binding.progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.cricle_progress_bar));con = true; }
                    countProgress=0;
                    handler.postDelayed(this,1000);
                }
                else{
                    handler.removeCallbacks(this);
                }
            }
        },10000);

        return binding.getRoot();
    }
}