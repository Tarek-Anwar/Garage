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
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeFragment;
import com.HomeGarage.garage.models.CarInfoModel;
import com.HomeGarage.garage.models.GrageInfoModel;
import com.HomeGarage.garage.models.MoneyModel;
import com.HomeGarage.garage.models.PurchaseModel;
import com.HomeGarage.garage.navfragment.PayFragment;
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
    CarInfoModel carInfoModelListener;
    GrageInfoModel grageInfoModel;
    String idLastOper;
    MoneyModel  modelMoney;

    DatabaseReference referenceCar ;
    DatabaseReference garageReference;
    DatabaseReference refPushase = FirebaseUtil.referencePurchase;
    DatabaseReference referenceOperattion = FirebaseUtil.referenceOperattion;
    DatabaseReference appReference = FirebaseDatabase.getInstance().getReference().child("App");
    SimpleDateFormat formatterLong = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa", new Locale("en"));

    public DialogPay(GrageInfoModel grageInfoModel, float  costIN , String idLastOper) {
        this.grageInfoModel = grageInfoModel;
        this.costIN =costIN;
        this.idLastOper = idLastOper;
        garageReference = FirebaseUtil.referenceGarage.child(grageInfoModel.getId());
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
            public void OnBalanceRecive(CarInfoModel carInfoModel) {
                if(costIN>0 && costIN<10 ){ costIN=10; }
                balance.setText(yourBalance +" : " + String.format("%.2f", carInfoModelListener.getBalance())+ eg);
                cost.setText(String.format("%.2f",costIN) + eg);

                if (costIN < carInfoModelListener.getBalance()) {
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
            public void OnBalanceAppRecive(MoneyModel modelMoney){
                pay.setOnClickListener(v -> {

                    //calc app and grage balance
                    float appBalance = (float) (costIN * .1);
                    float grageBalance = costIN - appBalance;

                    //update car owner balance
                    carInfoModelListener.setBalance(carInfoModelListener.getBalance() - costIN);
                    referenceCar.child("balance").setValue(carInfoModelListener.getBalance());
                    balance.setText(yourBalance +" : " + String.format("%.2f", carInfoModelListener.getBalance())+ eg);


                    //update balance for grage owner
                    grageInfoModel.setBalance(grageInfoModel.getBalance() + grageBalance);
                    garageReference.child("Balance").setValue(grageInfoModel.getBalance());
                    modelMoney.setMoneyForGarage(grageBalance + modelMoney.getMoneyForGarage());
                    appReference.child(grageInfoModel.getId()).child("moneyForGarage").setValue(modelMoney.getMoneyForGarage());

                    //update app balance
                    modelMoney.setAppPercent(appBalance + modelMoney.getAppPercent());
                    appReference.child(grageInfoModel.getId()).child("appPercent").setValue(modelMoney.getAppPercent());
                    appReference.child(grageInfoModel.getId()).child("totalBalance").setValue(modelMoney.getMoneyForGarage() - modelMoney.getAppPercent());

                    //creat opreation and save to last opreation list
                    PurchaseModel opreation = new PurchaseModel();
                    Date date = new Date(System.currentTimeMillis());
                    String dateOpreation = formatterLong.format(date);
                    opreation.setDate(dateOpreation);
                    opreation.setType("1");
                    opreation.setFrom(FirebaseUtil.firebaseAuth.getUid());
                    opreation.setTo(carInfoModelListener.getId());
                    opreation.setValue(costIN);
                    opreation.setFromName(FirebaseUtil.carInfoModelLogin.get(0).getName());
                    opreation.setToName(grageInfoModel.getNameEn());
                    opreation.setId(refPushase.push().getKey());
                    refPushase.child(opreation.getId()).setValue(opreation);

                    referenceOperattion.child(idLastOper).child("price").setValue(costIN);

                    RateDialog rateDialog = new RateDialog(idLastOper, grageInfoModel.getId());
                    rateDialog.show(getParentFragmentManager(), "Rate");

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView,new HomeFragment());
                    transaction.commit();
                    toast.show();
                    dismiss();
                });

            }
        });

        pruchese.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new PayFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            dismiss();
        });

        return root;
    }

    private void initViews(View root) {
        balance = root.findViewById(R.id.balanceTV);
        cost = root.findViewById(R.id.cost);
        pay = root.findViewById(R.id.payBtnDialog);
        pruchese = root.findViewById(R.id.btn_purchase_dialog);
    }

    void getCarInfo(OnBalanceReciveCallback callback){
        DatabaseReference reference  = appReference.child(grageInfoModel.getId());
       reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    modelMoney = snapshot.getValue(MoneyModel.class);
                    callback.OnBalanceAppRecive(modelMoney);
                }else {
                    callback.OnBalanceAppRecive(new MoneyModel(0,0,0));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        referenceCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carInfoModelListener = snapshot.getValue(CarInfoModel.class);
                callback.OnBalanceRecive(carInfoModelListener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private interface OnBalanceReciveCallback{
        void OnBalanceRecive(CarInfoModel carInfoModel);
        void OnBalanceAppRecive(MoneyModel modelMoney);
    }

}
