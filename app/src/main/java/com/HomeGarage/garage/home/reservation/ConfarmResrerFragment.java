package com.HomeGarage.garage.home.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.service.FcmNotificationsSender;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ConfarmResrerFragment extends Fragment {

    ImageView startTime , startDate ;
    TextView txtTimeStart;
    Button   btnRecer ,btnRecerNow ;

    String s_time = null;
    String e_time = null;
    Calendar calendar = Calendar.getInstance();

    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm aa" , new Locale("en"));

    DatabaseReference reference;
    GrageInfo grageInfo ;

    String timeResr =null;
    String dateResr = null;
    String allDate= null;
    FragmentActivity activity;

    public ConfarmResrerFragment(GrageInfo grageInfo ,  FragmentActivity activity) {
        this.grageInfo = grageInfo;
        this.activity = activity;
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
                Calendar calendar = Calendar.getInstance();
                calendar.set(0,0,0,hourOfDay,minute);
                timeResr = (String) DateFormat.format("hh:mm aa" , calendar);
                if(dateResr != null) {
                    allDate = dateResr +" " +timeResr;
                    txtTimeStart.setText(allDate);
                }
            },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true
            );
            dialog.show();
        });

        startDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth,0,0);
                dateResr = (String) DateFormat.format("dd/MM/yyyy" , calendar);
                if(timeResr!=null) {
                    allDate = dateResr +" " +timeResr;
                    txtTimeStart.setText(allDate);
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        });

        reference = FirebaseUtil.referenceOperattion;

        btnRecerNow.setOnClickListener(v -> {
            Opreation model = new Opreation();
            Date date = new Date(System.currentTimeMillis());
            String dataModel = formatterLong.format(date);
            model.setDate(dataModel);
            model.setType("1");
            model.setState("1");
            model.setFromName(FirebaseUtil.carInfoLogin.get(0).getName());
            model.setFrom(FirebaseUtil.firebaseAuth.getUid());
            model.setTo(grageInfo.getId());
            model.setToName(grageInfo.getNameEn());
            model.setId(reference.push().getKey());

            reference.child(model.getId()).setValue(model);
            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                    grageInfo.getId(),"From " + FirebaseUtil.firebaseAuth.getCurrentUser().getEmail()
                    ,"I'w Reservation Garage "+ model.getDate() ,model.getId() , getContext());
                notificationsSender.SendNotifications();

            statResetvaion(model);
        });

        btnRecer.setOnClickListener(v -> {

            if(allDate != null) {
                Date d1 = null;
                try { d1 = formatterLong.parse(allDate);
                } catch (ParseException e) { e.printStackTrace(); }
                if (d1.getTime()>System.currentTimeMillis()){
                    Opreation model = new Opreation();
                    model.setDate(allDate);
                    model.setType("1");
                    model.setState("1");
                    model.setFromName(FirebaseUtil.carInfoLogin.get(0).getName());
                    model.setFrom(FirebaseUtil.firebaseAuth.getUid());
                    model.setTo(grageInfo.getId());
                    model.setToName(grageInfo.getNameEn());
                    model.setId(reference.push().getKey());
                    reference.child(model.getId()).setValue(model);

                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                            grageInfo.getId(),"From " + FirebaseUtil.firebaseAuth.getCurrentUser().getEmail()
                            ,"I'w Reservation Garage "+ model.getDate() ,model.getId() , getContext());
                    notificationsSender.SendNotifications();

                    statResetvaion(model);
                }
                else{
                    Toast.makeText(getContext(), "Time in paist", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return root;
    }

    private void initUI(View root) {
        startTime = root.findViewById(R.id.time_start);
        txtTimeStart = root.findViewById(R.id.text_time_start);
        btnRecer = root.findViewById(R.id.btn_reser_time);
        btnRecerNow = root.findViewById(R.id.btn_reser_now);
        startDate = root.findViewById(R.id.start_date);
    }

    private void statResetvaion(Opreation opreation){

       FragmentManager fm = activity.getSupportFragmentManager();
        while (fm.getBackStackEntryCount() != 0) {
            fm.popBackStackImmediate();
        }

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new RequstActiveFragment(opreation,activity));
        transaction.commit();
    }


}