package com.HomeGarage.garage.home.reservation;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.HomeGarage.garage.R;
import android.text.format.DateFormat;

import java.util.Calendar;


public class ConfarmResrerFragment extends Fragment {

    ImageView startTime , endTime;
    TextView txtTimeStart , txtTimeEnd;

    int s_hour , s_minute , e_minute , e_hour;
    String s_time , e_time;
    Calendar calendar = Calendar.getInstance();
    final int nowHour = calendar.get(Calendar.HOUR);
    final int nowMinute = calendar.get(Calendar.MINUTE);

    public ConfarmResrerFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_confarm_resrer, container, false);
        initUI(root);

        startTime.setOnClickListener(v -> {
            TimePickerDialog dialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
                s_hour = hourOfDay;
                s_minute = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,s_hour,s_minute);
                s_time = (String) DateFormat.format("hh:mm aa" , calendar);
                txtTimeStart.setText(s_time);
            },12,0,false
            );
            dialog.updateTime(nowHour,nowMinute);
            dialog.show();
        });

        endTime.setOnClickListener(v -> {
            TimePickerDialog dialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
                e_hour = hourOfDay;
                e_minute = minute;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,e_hour,e_minute);
                e_time = (String) DateFormat.format("hh:mm aa" , calendar);
                txtTimeEnd.setText(e_time);
            },12,0,false
            );
            dialog.updateTime(nowHour+1,nowMinute);
            dialog.show();
        });

        return root;
    }

    private void initUI(View root) {
        startTime = root.findViewById(R.id.time_start);
        endTime = root.findViewById(R.id.time_end);
        txtTimeStart = root.findViewById(R.id.text_time_start);
        txtTimeEnd = root.findViewById(R.id.text_time_end);

    }


}