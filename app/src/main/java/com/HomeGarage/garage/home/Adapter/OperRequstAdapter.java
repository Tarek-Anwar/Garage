package com.HomeGarage.garage.home.Adapter;

import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.Opreation;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OperRequstAdapter extends RecyclerView.Adapter<OperRequstAdapter.OperRequstViewHoler> {

    ArrayList<Opreation> opreationList = FirebaseUtil.opreationRequstList;
    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm aa" , new Locale("en"));
    public OperRequstAdapter() {
    }

    @NonNull
    @Override
    public OperRequstViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.opreation_requst_row , parent , false);
        return new OperRequstViewHoler(root);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull OperRequstViewHoler holder, int position) {
        holder.bulidUI(opreationList.get(position));
    }

    @Override
    public int getItemCount() {
        return opreationList.size();
    }

    public class OperRequstViewHoler extends RecyclerView.ViewHolder {

        TextView state , type , name , date;
        Chronometer chronometer;
        ProgressBar progressBar;

        int countProgress ;
        public OperRequstViewHoler(@NonNull View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.txt_requst_state_home);
            type = itemView.findViewById(R.id.txt_requst_type_home);
            name = itemView.findViewById(R.id.txt_garage_name);
            date = itemView.findViewById(R.id.txt_date);
            chronometer = itemView.findViewById(R.id.progress_bar_txt);
            progressBar = itemView.findViewById(R.id.progress_bar_test);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void bulidUI(Opreation opreation){
            Date start = null;
            try {
                start = formatterLong.parse(opreation.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            state.setText(FirebaseUtil.stateList.get(Integer.parseInt(opreation.getState())-1));
            type.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreation.getType())-1));
            date.setText(opreation.getDate());
            name.setText(opreation.getToName());

            Long diff = System.currentTimeMillis() - start.getTime();

            countProgress = (int) (diff / 10000);
            chronometer.setBase(SystemClock.elapsedRealtime() - diff);
            chronometer.start();

            progressBar.setMin(0);
            progressBar.setMax(1100);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(countProgress<=1100){
                        progressBar.setProgress(countProgress);
                        countProgress++;
                        handler.postDelayed(this,10000);}
                    else {
                        handler.removeCallbacks(this);
                    }
                }
            },1000);


        }
    }
}
