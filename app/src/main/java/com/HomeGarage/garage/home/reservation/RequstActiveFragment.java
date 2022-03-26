package com.HomeGarage.garage.home.reservation;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.databinding.FragmentRequstActiveBinding;
import com.HomeGarage.garage.home.HomeFragment;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.service.FcmNotificationsSender;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class RequstActiveFragment extends Fragment {

    FragmentRequstActiveBinding binding;
    Opreation opreation;
    GrageInfo grageInfo;
    FragmentActivity activity;
    DatabaseReference refOperation;
    Date start = null;
    Date end = null;
    Long diff;
    volatile boolean con;
    int countProgress ;

    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm aa" , new Locale("en"));

    public RequstActiveFragment(Opreation opreation , FragmentActivity activity) {
        this.opreation = opreation;
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refOperation = FirebaseUtil.referenceOperattion.child(opreation.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRequstActiveBinding.inflate(getLayoutInflater());

        binding.txtRequstStateHome.setText(FirebaseUtil.stateList.get(Integer.parseInt(opreation.getState())-1));
        binding.txtRequstTypeHome.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreation.getType())-1));

        try { start = formatterLong.parse(opreation.getDate());
        } catch (ParseException e) { e.printStackTrace(); }

        if(opreation.getDataEnd()==null) {
          diff = System.currentTimeMillis() - start.getTime();
        }else {
            try { end = formatterLong.parse(opreation.getDataEnd());
            } catch (ParseException e) { e.printStackTrace(); }
             diff = end.getTime() - start.getTime();
        }

        setProgressBar();

        String ratings =  " " + getActivity().getString(R.string.ratings) + " )";

        getGarageInfo(opreation.getTo(), new OnGrageReciveCallback() {
            @Override
            public void OnGrageRecive(GrageInfo grageInfo) {
                String allAddress  , name;
                if(Locale.getDefault().getLanguage().equals("en")){
                    allAddress = grageInfo.getGovernoateEn()+"\n"+grageInfo.getCityEn()+"\n"+grageInfo.getRestOfAddressEN();
                    name = grageInfo.getNameEn();
                }else {
                    allAddress = grageInfo.getGovernoateAR()+"\n"+grageInfo.getCityAr()+"\n"+grageInfo.getRestOfAddressAr();
                    name = grageInfo.getNameAr();
                }
                binding.addressReq.setText(allAddress);
                binding.nameGarageReq.setText(name);

                if(grageInfo.getNumOfRatings()!=0) {
                    float ratting = grageInfo.getRate() /((float) grageInfo.getNumOfRatings());
                    binding.rateReq.setText(ratting+"");
                    binding.numRateReq.setText(ratting + " ( "+grageInfo.getNumOfRatings() +ratings);
                }
                if(opreation.getPrice()<0){
                    binding.dues.setVisibility(View.VISIBLE);
                    binding.btnFinshedReser.setVisibility(View.GONE);
                    binding.btnCancelReser.setVisibility(View.GONE);
                    binding.btnPayReser.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onBalaceChange(Opreation opreation) {
                if(opreation.getPrice()>0 || opreation.getType().equals("3")){
                    replaceFragment(new HomeFragment());
                }
            }
        });

        if(opreation.getType().equals("1") || System.currentTimeMillis() < start.getTime() ){
            binding.btnCancelReser.setVisibility(View.VISIBLE);
            binding.btnCancelReser.setOnClickListener(v -> {
                Date date = new Date(System.currentTimeMillis());
                if(opreation.getDataEnd()==null){
                    opreation.setDataEnd(formatterLong.format(date));
                }
                opreation.setState("3");
                opreation.setType("4");
                refOperation.setValue(opreation);

                binding.chronometer.stop();

                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                        grageInfo.getId(), "From " + opreation.getFromName()
                        , "sorry " + opreation.getToName() + ", i'm can't come in reservation " + opreation.getDate(), opreation.getId(), getContext());
                notificationsSender.SendNotifications();
                replaceFragment(new HomeFragment());
            });

            binding.btnFinshedReser.setVisibility(View.GONE);
            binding.btnPayReser.setVisibility(View.GONE);

        }else if(System.currentTimeMillis() > start.getTime() && opreation.getState().equals("2")) {
            binding.btnCancelReser.setVisibility(View.GONE);
            binding.btnPayReser.setVisibility(View.GONE);
            binding.btnFinshedReser.setVisibility(View.VISIBLE);
            binding.btnFinshedReser.setOnClickListener(v -> {

                Date date = new Date(System.currentTimeMillis());
                if (opreation.getDataEnd()==null){
                    opreation.setDataEnd(formatterLong.format(date));
                }
                opreation.setPrice(-1 * calPriceExpect(grageInfo.getPriceForHour(), opreation.getDate(), opreation.getDataEnd()));
                opreation.setState("3");
                opreation.setType("5");
                refOperation.setValue(opreation);
                binding.chronometer.stop();

                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                        grageInfo.getId(), "From " + opreation.getFromName()
                        , "reservation to is finshed check pay" + opreation.getDate(), opreation.getId(), getContext());
                notificationsSender.SendNotifications();
                binding.btnFinshedReser.setVisibility(View.GONE);
                binding.btnPayReser.setVisibility(View.VISIBLE);
            });
        }

        binding.btnPayReser.setOnClickListener(v -> {
            DialogPay dialogPay = new DialogPay(grageInfo, -1 * opreation.getPrice(), opreation.getId());
            dialogPay.show(getParentFragmentManager(), "Pay");
        });

        return binding.getRoot();
    }

    private void setProgressBar(){
        if(diff<0){
            con = false;
            countProgress = (int) (-1 * diff / 5000);
        }else { con=true;countProgress = (int) (diff / 5000); }
        binding.chronometer.setBase(SystemClock.elapsedRealtime() - diff);
        if(con && (opreation.getState().equals("1") || opreation.getState().equals("2"))){
            binding.progressBar.setMax(2160);
            binding.chronometer.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(countProgress==2160){
                        binding.progressBar.setProgress(countProgress);
                        countProgress=0;
                        handler.postDelayed(this,5000);
                    }else if(countProgress<2160){
                        binding.progressBar.setProgress(countProgress);
                        countProgress++;
                        handler.postDelayed(this,5000);
                    }else{ handler.removeCallbacks(this); }}
            },5000);
        }else if(con == false) {
            binding.progressBar.setMax(countProgress);
            binding.chronometer.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(countProgress>0){
                        binding.progressBar.setProgress(countProgress);
                        countProgress--;
                        handler.postDelayed(this,5000);
                    }else{ handler.removeCallbacks(this); }}
            },5000);
        }
    }

    private void getGarageInfo(String id , OnGrageReciveCallback callback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GaragerOnwerInfo").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                grageInfo = snapshot.getValue(GrageInfo.class);
                callback.OnGrageRecive(grageInfo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        refOperation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Opreation opreation = snapshot.getValue(Opreation.class);
              callback.onBalaceChange(opreation);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private interface OnGrageReciveCallback{
        void OnGrageRecive(GrageInfo  grageInfo);
        void onBalaceChange(Opreation opreation);
    }

    private float calPriceExpect(Float f , String s_time , String e_time){
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatterLong.parse(s_time);
            d2 = formatterLong.parse(e_time);
        } catch (ParseException e) { e.printStackTrace(); }
        Long diff = d2.getTime() - d1.getTime();
        Long diffMinets = diff / (60 *1000) ;
        return  diffMinets * f / 60;
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.commit();
    }

}