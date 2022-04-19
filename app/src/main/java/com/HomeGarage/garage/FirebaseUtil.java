package com.HomeGarage.garage;

import android.util.Log;

import com.HomeGarage.garage.modules.CarInfoModule;
import com.HomeGarage.garage.modules.GarageInfoModule;
import com.HomeGarage.garage.modules.OpreationModule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.LinkedList;

public class FirebaseUtil {

    public static final String TAG="grage tag";

    public static DatabaseReference databaseReference;
    public static DatabaseReference referenceOperattion;
    public static DatabaseReference referenceGarage;
    public static StorageReference storageReference;
    public static DatabaseReference referencePurchase;

    public static FirebaseDatabase firebaseDatabase;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseUtil firebaseUtil;
    public static FirebaseAuth.AuthStateListener listener;
    public static FirebaseStorage firebaseStorage;


    public static ArrayList<CarInfoModule> carInfoModuleLogin;
    public static ArrayList<GarageInfoModule> allGarage;
    public static LinkedList<OpreationModule> opreationModuleEndList;
    public static ArrayList<Integer> stateList;
    public static ArrayList<Integer> typeList;
    public static ArrayList<Integer> paylist;

    public static void getInstence() {
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
            connectStorage();
        }

        stateList = new ArrayList<>();
        stateList.add(R.string.waiting_requst);
        stateList.add(R.string.active_requst);
        stateList.add(R.string.finshed_requst);

        typeList = new ArrayList<>();
        typeList.add(R.string.requst_type);
        typeList.add(R.string.accpet_type);
        typeList.add(R.string.refusal_type);
        typeList.add(R.string.cancel);
        typeList.add(R.string.done);

        paylist = new ArrayList<>();
        paylist.add(R.string.pay_type);
        paylist.add(R.string.Purchase);
        paylist.add(R.string.deposit);

        carInfoModuleLogin = new ArrayList<>();
        allGarage = new ArrayList<>();
        opreationModuleEndList = new LinkedList<>();
        databaseReference = firebaseDatabase.getReference().child("CarInfo");
        referenceOperattion = firebaseDatabase.getReference().child("Operation");
        referenceGarage = firebaseDatabase.getReference().child("GaragerOnwerInfo");
        referencePurchase = firebaseDatabase.getReference().child("Purchase");
    }

    public static void attachListner()
    {
        firebaseAuth.addAuthStateListener(listener);
    }
    public static void detachListner() { firebaseAuth.removeAuthStateListener(listener); }

    public static void connectStorage(){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("car_ower_image");

    }
}
