package com.HomeGarage.garage;

import android.util.Log;

import androidx.annotation.NonNull;

import com.HomeGarage.garage.home.models.CarInfo;
import com.HomeGarage.garage.home.models.GrageInfo;
import com.HomeGarage.garage.home.models.Opreation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static final String TAG="grage tag";

    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseUtil firebaseUtil;
    public static FirebaseAuth.AuthStateListener listener;
    public static DatabaseReference referenceOperattion;

    public static ArrayList<CarInfo> carInfoLogin;
    public static ArrayList<GrageInfo> allGarage;
    public static ArrayList<Opreation> opreationEndList;
    public static ArrayList<Integer> stateList;
    public static ArrayList<Integer> typeList;

    public static void getInstence(String ref , String refOperattion) {
        if (firebaseUtil == null) {
            firebaseUtil=new FirebaseUtil();
            firebaseDatabase=FirebaseDatabase.getInstance();
            firebaseAuth=FirebaseAuth.getInstance();
            listener= firebaseAuth -> {
                if(firebaseAuth.getCurrentUser()!=null)
                { Log.i(TAG,"user is in "); }
                else
                { Log.i(TAG,"no user in "); }
            };
        }

        stateList = new ArrayList<>();
        stateList.add(R.string.waiting_requst);
        stateList.add(R.string.active_requst);
        stateList.add(R.string.finshed_requst);

        typeList = new ArrayList<>();
        typeList.add(R.string.requst_type);
        typeList.add(R.string.accpet_type);
        typeList.add(R.string.refusal_type);
        typeList.add(R.string.pay_type);

        carInfoLogin = new ArrayList<>();
        allGarage = new ArrayList<>();
        opreationEndList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference().child(ref);
        referenceOperattion = firebaseDatabase.getReference().child(refOperattion);
    }

    public static void attachListner()
    {
        firebaseAuth.addAuthStateListener(listener);
    }
    public static void detachListner() { firebaseAuth.removeAuthStateListener(listener); }
}
