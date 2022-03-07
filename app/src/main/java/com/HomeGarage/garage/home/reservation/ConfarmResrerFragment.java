package com.HomeGarage.garage.home.reservation;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.service.FcmNotificationsSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class ConfarmResrerFragment extends Fragment {

    ImageView startTime , endTime;
    TextView txtTimeStart , txtTimeEnd ,txtPriec;
    Button calcPrice , btnRecer ,btnRecerNow ;

    String s_time = null;
    String e_time = null;

    Calendar calendar = Calendar.getInstance();
    final int nowHour = calendar.get(Calendar.HOUR);
    final int nowMinute = calendar.get(Calendar.MINUTE);

    SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm aa");


    DatabaseReference reference;

    GrageInfo grageInfo ;
    public ConfarmResrerFragment(GrageInfo grageInfo ) {
        this.grageInfo = grageInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_confarm_resrer, container, false);
        initUI(root);

        startTime.setOnClickListener(v -> {
            TimePickerDialog dialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
                Date date = new Date(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,hourOfDay,minute);
                String time = (String) DateFormat.format("hh:mm aa" , calendar);
                txtTimeStart.setText(time);
                s_time = formatter.format(date) + " " + time;

            },12,0,false
            );
            dialog.updateTime(nowHour,nowMinute);
            dialog.show();
        });

        endTime.setOnClickListener(v -> {
            TimePickerDialog dialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
                Date date = new Date(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,hourOfDay,minute);

                String time = (String) DateFormat.format("hh:mm aa" , calendar);
                txtTimeEnd.setText(time);

                e_time = formatter.format(date) + " " + time;

            },12,0,false
            );
            dialog.updateTime(nowHour+1,nowMinute);
            dialog.show();
        });

        calcPrice.setOnClickListener(v -> {
            if(s_time != null && e_time != null){ txtPriec.setText(calPriceExpect(grageInfo.getPriceForHour())+" EG"); }

        });

        reference = FirebaseUtil.referenceOperattion;

        btnRecerNow.setOnClickListener(v -> {
            Opreation model = new Opreation();
            Date date = new Date(System.currentTimeMillis());
            String dataModel = formatterLong.format(date);
            model.setDate(dataModel);
            model.setType("Reqest");
            model.setState("Active");
            model.setFrom(FirebaseUtil.firebaseAuth.getUid());
            model.setTo(grageInfo.getId());
            model.setToName(grageInfo.getNameEn());
            model.setId(reference.push().getKey());
            Log.i("SDfsdfsdrwe",grageInfo.getId());
            reference.child(model.getId()).setValue(model);

            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                    grageInfo.getId(),"From " + FirebaseUtil.firebaseAuth.getCurrentUser().getEmail()
                    ,"I'w Reservation Garage "+ model.getDate() ,model.getId() , getContext(),getActivity());
                notificationsSender.SendNotifications();
        });

        btnRecer.setOnClickListener(v -> {
            Date timeNow = new Date();
            Log.i("sdfsdfsdfsdfsdcxv", timeNow.getTime()+"");
        });
        return root;
    }

    private void initUI(View root) {
        startTime = root.findViewById(R.id.time_start);
        endTime = root.findViewById(R.id.time_end);
        txtTimeStart = root.findViewById(R.id.text_time_start);
        txtTimeEnd = root.findViewById(R.id.text_time_end);
        txtPriec = root.findViewById(R.id.text_expected_price);
        calcPrice = root.findViewById(R.id.calc_price);
        btnRecer = root.findViewById(R.id.btn_reser_time);
        btnRecerNow = root.findViewById(R.id.btn_reser_now);
    }

    private float calPriceExpect(Float f){

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatterLong.parse(s_time);
            d2 = formatterLong.parse(e_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long diff = d2.getTime() - d1.getTime();
        Long diffMinets = diff / (60 *1000) ;

        return  diffMinets * f / 60;
    }

}