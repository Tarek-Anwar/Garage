package com.HomeGarage.garage;

import android.util.Log;

import androidx.annotation.NonNull;

import com.HomeGarage.garage.home.models.GrageInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static final String TAG="grage tag";
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    public static FirebaseAuth firebaseAuth;
    private static FirebaseUtil firebaseUtil;
    public static ArrayList<GrageInfo> allGarage;
    public static FirebaseAuth.AuthStateListener listener;

    public static void getInstence(String ref) {
        if (firebaseUtil == null) {
            firebaseUtil=new FirebaseUtil();
            firebaseDatabase=FirebaseDatabase.getInstance();
            firebaseAuth=FirebaseAuth.getInstance();
            listener=new FirebaseAuth.AuthStateListener(){

                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser()!=null)
                    {
                        Log.i(TAG,"user is in ");
                    }
                    else
                    {
                        Log.i(TAG,"no user in ");
                    }

                }
            };
        }
        allGarage = new ArrayList<>();
        databaseReference=firebaseDatabase.getReference().child(ref);
    }


    public static void attachListner()
    {
        firebaseAuth.addAuthStateListener(listener);
    }
    public static void detachListner() { firebaseAuth.removeAuthStateListener(listener); }
}
