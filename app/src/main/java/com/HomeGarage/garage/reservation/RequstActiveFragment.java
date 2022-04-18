package com.HomeGarage.garage.reservation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.databinding.FragmentRequstActiveBinding;
import com.HomeGarage.garage.dialog.DialogPay;
import com.HomeGarage.garage.home.HomeFragment;
import com.HomeGarage.garage.models.GrageInfoModel;
import com.HomeGarage.garage.models.OpreationModel;
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
    OpreationModel opreationModel;
    GrageInfoModel grageInfoModel;
    FragmentActivity activity;
    DatabaseReference refOperation;
    Date start = null;
    Date end = null;
    Long diff;
    volatile boolean con;
    int countProgress , round ;
    String roundTxt  ;
    SimpleDateFormat formatterLong =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));
    public RequstActiveFragment(OpreationModel opreationModel, FragmentActivity activity) {
        this.opreationModel = opreationModel;
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
        refOperation = FirebaseUtil.referenceOperattion.child(opreationModel.getId());

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

        binding.txtRequstStateHome.setText(FirebaseUtil.stateList.get(Integer.parseInt(opreationModel.getState())-1));
        binding.txtRequstTypeHome.setText(FirebaseUtil.typeList.get(Integer.parseInt(opreationModel.getType())-1));

        setProgressBar();

        getGarageInfo(opreationModel.getTo(), new OnGrageReciveCallback() {
            @Override
            public void OnGrageRecive(GrageInfoModel grageInfoModel) {
                String allAddress  , name;
                if(Locale.getDefault().getLanguage().equals("en")){
                    allAddress = grageInfoModel.getGovernoateEn()+"\n"+ grageInfoModel.getCityEn()+"\n"+ grageInfoModel.getRestOfAddressEN();
                    name = grageInfoModel.getNameEn();
                }else {
                    allAddress = grageInfoModel.getGovernoateAR()+"\n"+ grageInfoModel.getCityAr()+"\n"+ grageInfoModel.getRestOfAddressAr();
                    name = grageInfoModel.getNameAr();
                }
                binding.addressReq.setText(allAddress);
                binding.nameGarageReq.setText(name);

                if(grageInfoModel.getNumOfRatings()!=0) {
                    float ratting = grageInfoModel.getRate() / (2* grageInfoModel.getNumOfRatings());
                    binding.rateReq.setText(String.format("%.2f",ratting));
                    binding.numRateReq.setText( " ( "+ grageInfoModel.getNumOfRatings() +ratings);
                }
               if(opreationModel.getPrice()<0){
                    binding.dues.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onBalaceChange(OpreationModel opreationModel) {
                if(opreationModel.getType().equals("1") && opreationModel.getState().equals("1")){
                    binding.imgTypeReq.setImageDrawable(icRequst);
                    binding.imgStateReq.setBackground(reqWait);
                    binding.totalType.setVisibility(View.GONE);
                    binding.btnPayReser.setText(cancel);
                }else if(opreationModel.getType().equals("2") && opreationModel.getState().equals("2")){
                    binding.imgTypeReq.setImageDrawable(icAccpet);
                    binding.imgStateReq.setBackground(reqActice);
                    binding.totalType.setVisibility(View.GONE);
                    binding.btnPayReser.setText(finsh);
                }else {
                    binding.imgTypeReq.setImageDrawable(ic_done);
                    binding.imgStateReq.setBackground(reqFinsh);
                    binding.btnPayReser.setText(pay);
                }

                if(opreationModel.getPrice()!=0){
                    binding.txtTotalHome.setText(opreationModel.getPrice()*-1 + egPound);
                }

                if(opreationModel.getType().equals("1") || System.currentTimeMillis() < start.getTime() ){
                    if(binding.btnPayReser.getText().equals(cancel)) {
                        binding.btnPayReser.setOnClickListener(v -> {
                            Date date = new Date(System.currentTimeMillis());
                            if (opreationModel.getDataEnd() == null) {
                                opreationModel.setDataEnd(formatterLong.format(date));
                            }
                            opreationModel.setState("3");
                            opreationModel.setType("4");
                            opreationModel.setDataEnd(formatterLong.format(System.currentTimeMillis()));
                            refOperation.setValue(opreationModel);
                            binding.chronometer.stop();

                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                                    grageInfoModel.getId(), "From " + opreationModel.getFromName()
                                    , "sorry " + opreationModel.getToName() + ", i'm can't come in reservation " + opreationModel.getDate(), opreationModel.getId(), getContext());
                            notificationsSender.SendNotifications();

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

                }else if(System.currentTimeMillis() > start.getTime() && opreationModel.getState().equals("2")) {

                    if(binding.btnPayReser.getText().equals(finsh)) {
                        binding.btnPayReser.setOnClickListener(v -> {
                            Date date = new Date(System.currentTimeMillis());
                            if (opreationModel.getDataEnd() == null) {
                                opreationModel.setDataEnd(formatterLong.format(date));
                            }
                            opreationModel.setPrice(-1 * calPriceExpect(grageInfoModel.getPriceForHour(), opreationModel.getDate(), opreationModel.getDataEnd()));
                            opreationModel.setState("3");
                            opreationModel.setType("5");
                            refOperation.setValue(opreationModel);
                            binding.chronometer.stop();

                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                                    grageInfoModel.getId(), "From " + opreationModel.getFromName()
                                    , "reservation to is finshed check pay" + opreationModel.getDate(), opreationModel.getId(), getContext());
                            notificationsSender.SendNotifications();
                            binding.btnPayReser.setText(pay);
                        });
                    }
                }else {
                    if(binding.btnPayReser.getText().equals(pay)) {
                        binding.btnPayReser.setOnClickListener(v -> {
                            DialogPay dialogPay = new DialogPay(grageInfoModel, -1 * opreationModel.getPrice(), opreationModel.getId());
                            dialogPay.show(getParentFragmentManager(), "Pay");
                        });
                    }
                }
            }
        });

        return binding.getRoot();
    }

    private void setProgressBar(){
        try { start = formatterLong.parse(opreationModel.getDate());
        } catch (ParseException e) { e.printStackTrace(); }

        if(opreationModel.getDataEnd()==null) {
            diff = System.currentTimeMillis() - start.getTime();
        }else {
            try { end = formatterLong.parse(opreationModel.getDataEnd());
            } catch (ParseException e) { e.printStackTrace(); }
            diff = end.getTime() - start.getTime();
        }

        if(diff<0){ con = false;countProgress = (int) (-1 * diff / 5000);
        }else { con=true;countProgress = (int) (diff / 5000);
            round = (countProgress/2160) + 1;
            binding.roundTime.setText(roundTxt + round);}

        binding.chronometer.setBase(SystemClock.elapsedRealtime() - diff);
        if(con && (opreationModel.getState().equals("1") || opreationModel.getState().equals("2"))){
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GaragerOnwerInfo").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                grageInfoModel = snapshot.getValue(GrageInfoModel.class);
                callback.OnGrageRecive(grageInfoModel);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        refOperation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OpreationModel opreationModel = snapshot.getValue(OpreationModel.class);
                if(opreationModel !=null) callback.onBalaceChange(opreationModel);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private float calPriceExpect(Float f , String s_time , String e_time){
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = formatterLong.parse(s_time);
            d2 = formatterLong.parse(e_time);
        } catch (ParseException e) { e.printStackTrace(); }
        Long diff = d2.getTime() - d1.getTime();
        Long diffMinets = diff / (60 * 1000) ;
        float total =   diffMinets * f / 60;
        if(total<10) return 10;
        else  return total ;
    }


    private interface OnGrageReciveCallback{
        void OnGrageRecive(GrageInfoModel grageInfoModel);
        void onBalaceChange(OpreationModel opreationModel);
    }


}