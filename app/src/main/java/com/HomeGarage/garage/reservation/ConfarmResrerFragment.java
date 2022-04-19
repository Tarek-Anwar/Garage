package com.HomeGarage.garage.reservation;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.modules.GarageInfoModule;
import com.HomeGarage.garage.modules.OpreationModule;
import com.HomeGarage.garage.service.FcmNotificationsSender;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ConfarmResrerFragment extends Fragment {

    Button  btnRecer ,btnRecerNow ;
    SingleDateAndTimePicker singleDateAndTimePicker2;
    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));
    DatabaseReference reference;
    GarageInfoModule garageInfoModule;
    String allDate= null;
    FragmentActivity activity;
    long offset = FirebaseUtil.offsetTime;
    public ConfarmResrerFragment(GarageInfoModule garageInfoModule, FragmentActivity activity) {
        this.garageInfoModule = garageInfoModule;
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

        reference = FirebaseUtil.referenceOperattion;
        singleDateAndTimePicker2.setCustomLocale(Locale.getDefault());

        btnRecerNow.setOnClickListener(v -> {
            Date date = new Date(System.currentTimeMillis()+offset);
            String dataModel = formatterLong.format(date);
            singOperation(dataModel);
        });

        btnRecer.setOnClickListener(v -> {
            if(singleDateAndTimePicker2.getDate()!=null){ allDate = formatterLong.format(singleDateAndTimePicker2.getDate()); }
            if(allDate != null) {
                Date d1 = null;
                try { d1 = formatterLong.parse(allDate);
                } catch (ParseException e) { e.printStackTrace(); }
                if (d1.getTime()>System.currentTimeMillis()+offset){
                    singOperation(allDate); }
                else{ Toast.makeText(getContext(), getString(R.string.time_past), Toast.LENGTH_SHORT).show(); }
            }
        });

        return root;
    }

    private void initUI(View root) {
        btnRecer = root.findViewById(R.id.btn_reser_time);
        btnRecerNow = root.findViewById(R.id.btn_reser_now);
        singleDateAndTimePicker2 = root.findViewById(R.id.single_day_picker);
    }

    private void statResetvaion(OpreationModule opreationModule){
       FragmentManager fm = activity.getSupportFragmentManager();
        while (fm.getBackStackEntryCount() != 0) {
            //fm.popBackStackImmediate();
        FragmentManager.BackStackEntry entry = activity.getSupportFragmentManager().getBackStackEntryAt(0);
            activity.getSupportFragmentManager().popBackStack(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
            activity.getSupportFragmentManager().executePendingTransactions();
        }
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new RequstActiveFragment(opreationModule,activity));
        transaction.commit();
    }

    private void singOperation( String data){
        OpreationModule model = new OpreationModule();
        model.setDate(data);
        model.setType("1");
        model.setState("1");
        model.setFromName(FirebaseUtil.carInfoModuleLogin.get(0).getName());
        model.setFrom(FirebaseUtil.firebaseAuth.getUid());
        model.setTo(garageInfoModule.getId());
        model.setToName(garageInfoModule.getNameEn());
        model.setId(reference.push().getKey());
        reference.child(model.getId()).setValue(model);
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                garageInfoModule.getId(),"from " + FirebaseUtil.carInfoModuleLogin.get(0).getName()
                ,"I want to reserve garage "+ model.getDate() ,model.getId() , getContext());
        notificationsSender.SendNotifications();
        statResetvaion(model);
    }

}