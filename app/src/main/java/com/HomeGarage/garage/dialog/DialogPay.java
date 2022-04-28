package com.HomeGarage.garage.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.modules.OpreationModule;
import com.HomeGarage.garage.util.DateFormatUtil;
import com.HomeGarage.garage.util.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.ui.home.HomeFragment;
import com.HomeGarage.garage.modules.CarInfoModule;
import com.HomeGarage.garage.modules.GarageInfoModule;
import com.HomeGarage.garage.modules.BalanceAppModule;
import com.HomeGarage.garage.modules.PurchaseModule;
import com.HomeGarage.garage.ui.navfragment.PayFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DialogPay extends DialogFragment {

    TextView balance, cost ;
    Button pay , pruchese;
    Toast toast;

    float  costIN;
    CarInfoModule carInfoModuleListener;
    GarageInfoModule garageInfoModule;
    String idLastOper;
    BalanceAppModule modelMoney;

    DatabaseReference referenceCar ;
    DatabaseReference garageReference;
    DatabaseReference refPushase = FirebaseUtil.referencePurchase;
    DatabaseReference referenceOperattion = FirebaseUtil.referenceOperattion;
    DatabaseReference appReference = FirebaseDatabase.getInstance().getReference().child("App");

    public DialogPay(GarageInfoModule garageInfoModule, float  costIN , String idLastOper) {
        this.garageInfoModule = garageInfoModule;
        this.costIN = costIN;
        this.idLastOper = idLastOper;
        garageReference = FirebaseUtil.referenceGarage.child(garageInfoModule.getId());
        referenceCar = FirebaseUtil.databaseReference.child(Objects.requireNonNull(FirebaseUtil.firebaseAuth.getUid()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //init views
        View root = inflater.inflate(R.layout.pay_dialog, container, false);
        initViews(root);

        String yourBalance = getString(R.string.your_balance);
        String eg = " " + getString(R.string.eg);

        //custom toast
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.castom_toast_layout,root.findViewById(R.id.custom_toast_thank_you));
        toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(view);

        getCarInfo(new OnBalanceReciveCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void OnBalanceRecive(CarInfoModule carInfoModule) {
                if(costIN>0 && costIN<10 ){ costIN=10; }
                balance.setText(yourBalance +" : " + String.format("%.2f", carInfoModuleListener.getBalance())+ eg);
                cost.setText(String.format("%.2f",costIN) + eg);

                if (costIN < carInfoModuleListener.getBalance()) {
                    pay.setVisibility(View.VISIBLE);
                    pruchese.setVisibility(View.GONE);
                }
                else {
                    pay.setVisibility(View.GONE);
                    pruchese.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void OnBalanceAppRecive(BalanceAppModule modelMoney){
                pay.setOnClickListener(v -> {

                    //calc app and grage balance
                    float appBalance = (float) (costIN * .1);
                    float grageBalance = costIN - appBalance;

                    //update car owner balance
                    carInfoModuleListener.setBalance(carInfoModuleListener.getBalance() - costIN);
                    referenceCar.child("balance").setValue(carInfoModuleListener.getBalance());
                    balance.setText(yourBalance +" : " + String.format("%.2f", carInfoModuleListener.getBalance())+ eg);


                    //update balance for grage owner
                    garageInfoModule.setBalance(garageInfoModule.getBalance() + grageBalance);
                    garageReference.child("Balance").setValue(garageInfoModule.getBalance());
                    modelMoney.setMoneyForGarage(grageBalance + modelMoney.getMoneyForGarage());
                    appReference.child(garageInfoModule.getId()).child("moneyForGarage").setValue(modelMoney.getMoneyForGarage());

                    //update app balance
                    modelMoney.setAppPercent(appBalance + modelMoney.getAppPercent());
                    appReference.child(garageInfoModule.getId()).child("appPercent").setValue(modelMoney.getAppPercent());
                    appReference.child(garageInfoModule.getId()).child("totalBalance").setValue(modelMoney.getMoneyForGarage() - modelMoney.getAppPercent());

                    //creat opreation and save to last opreation list
                    pushPurchaseModule();

                    referenceOperattion.child(idLastOper).child("price").setValue(costIN);

                    showRateDialog();

                    replaceFragmentWithoutBackStack(new HomeFragment());
                    toast.show();
                });

            }
        });

        pruchese.setOnClickListener(v -> replaceFragmentWithBackStack(new PayFragment()));

        return root;
    }


    private void initViews(View root) {
        balance = root.findViewById(R.id.balanceTV);
        cost = root.findViewById(R.id.cost);
        pay = root.findViewById(R.id.payBtnDialog);
        pruchese = root.findViewById(R.id.btn_purchase_dialog);
    }

    void getCarInfo(OnBalanceReciveCallback callback){
        DatabaseReference reference  = appReference.child(garageInfoModule.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    modelMoney = snapshot.getValue(BalanceAppModule.class);
                    callback.OnBalanceAppRecive(modelMoney);
                }else {
                    callback.OnBalanceAppRecive(new BalanceAppModule(0,0,0));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        referenceCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carInfoModuleListener = snapshot.getValue(CarInfoModule.class);
                callback.OnBalanceRecive(carInfoModuleListener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void pushPurchaseModule(){
        PurchaseModule opreation = new PurchaseModule();
        Date date = new Date(System.currentTimeMillis());
        String dateOpreation = DateFormatUtil.allDataFormat.format(date);
        opreation.setDate(dateOpreation);
        opreation.setType("1");
        opreation.setFrom(FirebaseUtil.firebaseAuth.getUid());
        opreation.setTo(carInfoModuleListener.getId());
        opreation.setValue(costIN);
        opreation.setFromName(FirebaseUtil.carInfoModuleLogin.get(0).getName());
        opreation.setToName(garageInfoModule.getNameEn());
        opreation.setId(refPushase.push().getKey());
        refPushase.child(opreation.getId()).setValue(opreation);
    }

    private  void showRateDialog(){
        RateDialog rateDialog = new RateDialog(idLastOper, garageInfoModule.getId());
        rateDialog.show(getParentFragmentManager(), "Rate");
    }

    private void  replaceFragmentWithBackStack(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.commit();
        dismiss();
    }

    private void  replaceFragmentWithoutBackStack(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        dismiss();
    }



    private interface OnBalanceReciveCallback{
        void OnBalanceRecive(CarInfoModule carInfoModule);
        void OnBalanceAppRecive(BalanceAppModule modelMoney);
    }

}
