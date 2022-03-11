package com.HomeGarage.garage.home.reservation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ProgressBar;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.Opreation;


public class RequstActiveFragment extends Fragment {

    Opreation opreation;
    Chronometer chronometer;
    ProgressBar progressBar;
    int countProgress ;

    public RequstActiveFragment(Opreation opreation) {
        this.opreation = opreation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Date start = null;
            try {
                start = formatterLong.parse(opreation.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long diff = System.currentTimeMillis() - start.getTime();

            countProgress = (int) (diff / 10000);
            chronometer.setBase(SystemClock.elapsedRealtime() - diff);
            chronometer.start();
        countProgress = 1;
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        progressBar.setMin(0);
        progressBar.setMax(200);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(countProgress<=200){
                    progressBar.setProgress(countProgress);
                    countProgress++;
                    handler.postDelayed(this,1000);}
                else {
                    handler.removeCallbacks(this);
                }
            }
        },1000);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requst_active, container, false);
    }
}