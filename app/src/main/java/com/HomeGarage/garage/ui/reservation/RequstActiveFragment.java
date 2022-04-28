package com.HomeGarage.garage.ui.reservation;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.util.DateFormatUtil;
import com.HomeGarage.garage.util.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.databinding.FragmentRequstActiveBinding;
import com.HomeGarage.garage.dialog.DialogPay;
import com.HomeGarage.garage.ui.home.HomeFragment;
import com.HomeGarage.garage.modules.GarageInfoModule;
import com.HomeGarage.garage.modules.OpreationModule;
import com.HomeGarage.garage.service.FcmNotificationsSender;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RequstActiveFragment extends Fragment {

    FragmentRequstActiveBinding binding;
    OpreationModule opreationModule;
    GarageInfoModule garageInfoModule;
    FragmentActivity activity;
    DatabaseReference refOperation;
    Date start = null;
    long diff;
    long offset = FirebaseUtil.offsetTime;
    volatile boolean con;
    int countProgress , round ;
    String roundTxt ;
    String title;
    String body;
    public RequstActiveFragment(OpreationModule opreationModule, FragmentActivity activity) {
        this.opreationModule = opreationModule;
        this.activity = activity;
    }

    public RequstActiveFragment (){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRequstActiveBinding.inflate(getLayoutInflater());
        refOperation = FirebaseUtil.referenceOperattion.child(opreationModule.getId());

        String cancel = getActivity().getString(R.string.cancel);
        String finsh= getActivity().getString(R.string.finshed_requst);
        String pay = getActivity().getString(R.string.pay_type);
        String ratings =  " " + getActivity().getString(R.string.ratings) + " )";
        String egPound = " " + getActivity().getString(R.string.eg);
        roundTxt = getActivity().getString(R.string.round_time)+ " : ";
        Drawable  icRequst  =getActivity().getDrawable(R.drawable.ic_requst);
        Drawable reqWait = getActivity().getDrawable(R.drawable.type_req_wait);
        Drawable icAccpet = getActivity().getDrawable(R.drawable.ic_accpet);
        Drawable  reqActice = getActivity().getDrawable(R.drawable.type_req_active);
        Drawable ic_done = getActivity().getDrawable(R.drawable.ic_done_all);
        Drawable reqFinsh = getActivity().getDrawable(R.drawable.type_req_finsh);

        binding.txtRequstStateHome.setText(FirebaseUtil.stateList.get(Integer.parseInt(opreationModule.getState())-1));
        binding.txtRequstTypeHome.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreationModule.getType())-1));

        setProgressBar();

        getGarageInfo(opreationModule.getTo(), new OnGrageReciveCallback() {
            @Override
            public void OnGrageRecive(GarageInfoModule garageInfoModule) {
                String allAddress , name;
                if(Locale.getDefault().getLanguage().equals("en")){
                    allAddress = garageInfoModule.getGovernoateEn()+"\n"+ garageInfoModule.getCityEn()+"\n"+ garageInfoModule.getRestOfAddressEN();
                    name = garageInfoModule.getNameEn();
                }else {
                    allAddress = garageInfoModule.getGovernoateAR()+"\n"+ garageInfoModule.getCityAr()+"\n"+ garageInfoModule.getRestOfAddressAr();
                    name = garageInfoModule.getNameAr();
                }
                binding.addressReq.setText(allAddress);
                binding.nameGarageReq.setText(name);
                if(!garageInfoModule.getImageGarage().isEmpty()){
                    showImage(garageInfoModule.getImageGarage(),binding.garageImageActive);
                }
                if(garageInfoModule.getNumOfRatings()!=0) {
                    float ratting = garageInfoModule.getRate() / (2* garageInfoModule.getNumOfRatings());
                    binding.rateReq.setText(String.format("%.2f",ratting));
                    binding.numRateReq.setText( " ( "+ garageInfoModule.getNumOfRatings() +ratings);
                }
               if(opreationModule.getPrice()<0){
                    binding.dues.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onBalaceChange(OpreationModule opreationModule) {
                if(opreationModule.getType().equals("1") && opreationModule.getState().equals("1")){
                    binding.imgTypeReq.setImageDrawable(icRequst);
                    binding.imgStateReq.setBackground(reqWait);
                    binding.totalType.setVisibility(View.GONE);
                    binding.btnPayReser.setText(cancel);
                }else if(opreationModule.getType().equals("2") && opreationModule.getState().equals("2")){
                    binding.imgTypeReq.setImageDrawable(icAccpet);
                    binding.imgStateReq.setBackground(reqActice);
                    binding.totalType.setVisibility(View.GONE);
                    binding.btnPayReser.setText(finsh);
                }else {
                    binding.imgTypeReq.setImageDrawable(ic_done);
                    binding.imgStateReq.setBackground(reqFinsh);
                    binding.btnPayReser.setText(pay);
                }

                if(opreationModule.getPrice()!=0){
                    binding.txtTotalHome.setText(opreationModule.getPrice()*-1 + egPound);
                }

                if(opreationModule.getType().equals("1") || System.currentTimeMillis() < start.getTime() ){
                    if(binding.btnPayReser.getText().equals(cancel)) {
                        binding.btnPayReser.setOnClickListener(v -> {
                            Date date = new Date(System.currentTimeMillis()+offset);
                            if (opreationModule.getDataEnd() == null) {
                                opreationModule.setDataEnd(DateFormatUtil.allDataFormat.format(date));
                            }
                            opreationModule.setState("3");
                            opreationModule.setType("4");
                            opreationModule.setDataEnd(DateFormatUtil.allDataFormat.format(System.currentTimeMillis()+offset));
                            refOperation.setValue(opreationModule);
                            binding.chronometer.stop();

                            title = String.format("From %s",opreationModule.getFromName());
                            body = String.format("sorry %s ,  i'm can't come in reservation");
                            sendNotifications(title,body,opreationModule.getId());

                            if(getActivity()!=null){
                                FragmentManager fm = activity.getSupportFragmentManager();
                                while (fm.getBackStackEntryCount() != 0) {
                                    FragmentManager.BackStackEntry entry = activity.getSupportFragmentManager().getBackStackEntryAt(0);
                                    activity.getSupportFragmentManager().popBackStack(entry.getId(),
                                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    activity.getSupportFragmentManager().executePendingTransactions();
                                }
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragmentContainerView, new HomeFragment());
                                transaction.commit();
                            }
                        });
                    }

                }else if(System.currentTimeMillis() > start.getTime() && opreationModule.getState().equals("2")) {
                    if(binding.btnPayReser.getText().equals(finsh)) {
                        binding.btnPayReser.setOnClickListener(v -> {
                            Date date = new Date(System.currentTimeMillis()+offset);
                            if (opreationModule.getDataEnd() == null) {
                                opreationModule.setDataEnd(DateFormatUtil.allDataFormat.format(date));
                            }
                            opreationModule.setPrice(-1 * calPriceExpect(garageInfoModule.getPriceForHour(), opreationModule.getDate(), opreationModule.getDataEnd()));
                            opreationModule.setState("3");
                            opreationModule.setType("5");
                            refOperation.setValue(opreationModule);
                            binding.chronometer.stop();

                             title = String.format("From %s",opreationModule.getFromName());
                             body = String.format("Reservation to is finshed check pay in %s",opreationModule.getDate());
                            sendNotifications(title,body,opreationModule.getId());

                            binding.btnPayReser.setText(pay);
                        });
                    }
                }else {
                    if(binding.btnPayReser.getText().equals(pay)) {
                        binding.btnPayReser.setOnClickListener(v -> {
                            DialogPay dialogPay = new DialogPay(garageInfoModule, -1 * opreationModule.getPrice(), opreationModule.getId());
                            dialogPay.show(getParentFragmentManager(), "Pay");
                        });
                    }
                }
            }
        });

        return binding.getRoot();
    }

    private void sendNotifications(String title,String body,String idOperation){
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                garageInfoModule.getId(), title
                , body, idOperation , getContext());
        notificationsSender.SendNotifications();
    }
    private void setProgressBar(){

        start = DateFormatUtil.parseAllDataFormat(opreationModule.getDate());

        if(opreationModule.getDataEnd()==null) diff = System.currentTimeMillis()+offset - start.getTime();
        else diff = DateFormatUtil.differentTime(opreationModule.getDate(),opreationModule.getDataEnd());

        if(diff<0){
            con = false;
            countProgress = (int) (-1 * diff / 5000);
        }else {
            con=true;
            countProgress = (int) (diff / 5000);
            round = (countProgress/2160) + 1;
            binding.roundTime.setText(roundTxt + round);
        }

        binding.chronometer.setBase(SystemClock.elapsedRealtime() - diff);
        if(con && (opreationModule.getState().equals("1") || opreationModule.getState().equals("2"))){
            binding.progressBar.setMax(2160);
            binding.chronometer.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(countProgress==2160){
                        binding.progressBar.setProgress(countProgress);
                        round++;
                        binding.roundTime.setText(round + round);
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
                    if(countProgress==0){
                        con=true;
                        binding.progressBar.setProgress(countProgress);
                        setProgressBar();
                    }
                    if(countProgress>0){
                        binding.progressBar.setProgress(countProgress);
                        countProgress--;
                        handler.postDelayed(this,5000);
                    }else{ handler.removeCallbacks(this); }}
            },5000);
        }
    }

    private void getGarageInfo(String id , OnGrageReciveCallback callback){
        DatabaseReference ref = FirebaseUtil.referenceGarage.child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                garageInfoModule = snapshot.getValue(GarageInfoModule.class);
                callback.OnGrageRecive(garageInfoModule);
            }
            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw new DatabaseException(error.getMessage());
            }
        });

        refOperation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OpreationModule opreationModule = snapshot.getValue(OpreationModule.class);
                if(opreationModule !=null) callback.onBalaceChange(opreationModule);
            }
            @SuppressLint("RestrictedApi")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw new DatabaseException(error.getMessage());
            }
        });
    }

    private float calPriceExpect(float priceForHour ,String sTime ,String eTime){
        long diff = DateFormatUtil.differentTime(sTime,eTime);
        long diffMinets = diff / (60 * 1000) ;
        float total =   diffMinets * priceForHour / 60;
        if(total<10) return 10;
        else  return total ;
    }

    private interface OnGrageReciveCallback{
        void OnGrageRecive(GarageInfoModule garageInfoModule);
        void onBalaceChange(OpreationModule opreationModule);
    }

    private void showImage(String url , ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width, width).centerCrop().into(imageView); } }
}