package com.HomeGarage.garage.home.reservation;

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
import com.HomeGarage.garage.home.RateDialog;
import com.HomeGarage.garage.home.models.CarInfo;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.HomeGarage.garage.home.models.PurchaseModel;
import com.HomeGarage.garage.home.navfragment.PayFragment;
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

    TextView balance, cost , enough_txt;
    Button pay , purchase;
    Toast toast;

    SimpleDateFormat formatterLong = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa", new Locale("en"));

    DatabaseReference reference ;
    DatabaseReference garageReference;
    DatabaseReference referenceOperattion = FirebaseUtil.referenceOperattion;
    DatabaseReference refPushase = FirebaseUtil.referencePurchase;
    DatabaseReference appReference = FirebaseDatabase.getInstance().getReference().child("App");

    float  costIN;
    CarInfo carInfoListener;
    GrageInfo grageInfo;
    String idLastOper;

    public DialogPay(GrageInfo grageInfo , float  costIN , String idLastOper) {
        this.grageInfo = grageInfo;
        this.costIN =costIN;
        this.idLastOper = idLastOper;
        garageReference = FirebaseUtil.referenceGarage.child(grageInfo.getId());
        reference = FirebaseUtil.databaseReference.child(Objects.requireNonNull(FirebaseUtil.firebaseAuth.getUid()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //init views
        View root = inflater.inflate(R.layout.pay_dialog, container, false);
        initViews(root);

        //custom toast
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.castom_toast_layout,root.findViewById(R.id.custom_toast_thank_you));
        toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(view);

        purchase.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView,new PayFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            dismiss();
        });

        getCarInfo(new OnBalanceReciveCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void OnBalanceRecive(CarInfo carInfo) {
                balance.setText("Your Balance is : " + carInfoListener.getBalance()+ " E.G.");
                cost.setText("Cost Reservation "+ costIN +" E.G.");
                if (costIN < carInfoListener.getBalance()) {
                    pay.setVisibility(View.VISIBLE);
                    enough_txt.setVisibility(View.GONE);
                    purchase.setVisibility(View.GONE);
                }else {
                    enough_txt.setVisibility(View.VISIBLE);
                    pay.setVisibility(View.GONE);
                    purchase.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void OnBalanceAppRecive(Float balanceApp) {
                pay.setOnClickListener(v -> {
                    //update car owner balance
                    carInfoListener.setBalance(carInfoListener.getBalance() - costIN);

                    reference.child("balance").setValue(carInfoListener.getBalance());
                    balance.setText("Your Balance is : " + String.format("%.2f",carInfoListener.getBalance()) + " E.G.");

                    //calc app and grage balance
                    float appBalance = (float) (costIN * .1);
                    float grageBalance = costIN - appBalance;

                    //update balance for grage owner
                    grageInfo.setBalance(grageInfo.getBalance() + grageBalance);
                    garageReference.child("Balance").setValue(grageInfo.getBalance());

                    //update app balance
                    appReference.child("Balance").setValue(appBalance+balanceApp);

                    //creat opreation and save to last opreation list
                    PurchaseModel opreation = new PurchaseModel();
                    Date date = new Date(System.currentTimeMillis());
                    String dateOpreation = formatterLong.format(date);
                    opreation.setDate(dateOpreation);
                    opreation.setType("1");
                    opreation.setFrom(FirebaseUtil.firebaseAuth.getUid());
                    opreation.setTo(carInfoListener.getId());
                    opreation.setValue(costIN);
                    opreation.setFromName(FirebaseUtil.carInfoLogin.get(0).getName());
                    opreation.setToName(grageInfo.getNameEn());
                    opreation.setId(refPushase.push().getKey());
                    refPushase.child(opreation.getId()).setValue(opreation);

                    referenceOperattion.child(idLastOper).child("price").setValue(costIN);

                    RateDialog rateDialog=new RateDialog(grageInfo,referenceOperattion.child(idLastOper));
                    rateDialog.show(getParentFragmentManager(),"Rate");
                    toast.show();
                    dismiss();
                });
            }
        });
        return root;
    }

    private void initViews(View root) {
        balance = root.findViewById(R.id.balanceTV);
        cost = root.findViewById(R.id.cost);
        pay = root.findViewById(R.id.payBtnDialog);
        enough_txt = root.findViewById(R.id.not_enough_txt);
        purchase = root.findViewById(R.id.btn_purchase);
    }

    void getCarInfo(OnBalanceReciveCallback callback){
        appReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float balance  = snapshot.child("Balance").getValue(Float.class);
                callback.OnBalanceAppRecive(balance);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carInfoListener = snapshot.getValue(CarInfo.class);
                callback.OnBalanceRecive(carInfoListener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private interface OnBalanceReciveCallback{
        void OnBalanceRecive(CarInfo carInfo);
        void OnBalanceAppRecive(Float balance);
    }

}
