package com.HomeGarage.garage.home;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.LastOperAdapter;
import com.HomeGarage.garage.home.models.Opreation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList<Opreation> opreationsEnd = FirebaseUtil.opreationEndList;
    DatabaseReference reference =  FirebaseUtil.referenceOperattion;
    Query query ;

    RecyclerView  recyclerLast ;
    LinearLayout layoutNearFind , layoutAllFind , layoutlast;
    View seeAllOper;
    ImageView notFind;
    Chronometer chronometer;
    ProgressBar progressBar;
    LastOperAdapter lastOperAdapter;
    int countProgress ;

    public HomeFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

       /* if(savedInstanceState == null) {
            if (reference != null) {
                getRequst(last -> {
                    lastOperAdapter = new LastOperAdapter(opreation -> {
                        OperationsFragment newFragment = new OperationsFragment(opreation);
                        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerView, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }, 3);
                    recyclerLast.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    recyclerLast.setAdapter(lastOperAdapter);
                });
            }
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        //find element
        initViews(root);

        lastOperAdapter = new LastOperAdapter(opreation -> {
            OperationsFragment newFragment = new OperationsFragment(opreation);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }, 3);

        recyclerLast.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerLast.setAdapter(lastOperAdapter);

        seeAllOper.setOnClickListener(v -> {
                replaceFragment(new LastOperFragment());
            });

        layoutAllFind.setOnClickListener(v -> {
            replaceFragment(new SearchFragment());
        });

        layoutNearFind.setOnClickListener(v ->{
            replaceFragment(new SearchFragment());
        });

        /*Date start = null;
            try {
                start = formatterLong.parse(opreation.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long diff = System.currentTimeMillis() - start.getTime();

            countProgress = (int) (diff / 10000);
            chronometer.setBase(SystemClock.elapsedRealtime() - diff);
            chronometer.start();
*/
        countProgress = 1;
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        progressBar.setMin(0);
        progressBar.setMax(200);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(countProgress<=200){
                    progressBar.setProgress(countProgress);
                    countProgress++;
                    handler.postDelayed(this,1000);}
                else {
                    handler.removeCallbacks(this);
                }
            }
        },1000);


        return root;
    }

    public  void getRequst(OnDataChangeCallback callback){
       query = reference.orderByChild("from").equalTo(FirebaseUtil.firebaseAuth.getUid());
       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()) {
                   opreationsEnd.clear();
                   for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                       Opreation opreation = snapshot1.getValue(Opreation.class);
                       if (opreation.getState().equals("3") && (opreation.getType().equals("3") || opreation.getType().equals("4"))) {
                            opreationsEnd.add(opreation);
                       }
                   }
                   callback.OnOpreationsEndChange(opreationsEnd);
               }
           }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private interface OnDataChangeCallback{
        void OnOpreationsEndChange(ArrayList<Opreation> last);
    }

    private void initViews(View v){
        recyclerLast = v.findViewById(R.id.recycler_last);
        layoutNearFind = v.findViewById(R.id.layout_near_find);
        layoutAllFind = v.findViewById(R.id.layout_all_find);
        seeAllOper = v.findViewById(R.id.see_all_last_oper);
        layoutlast = v.findViewById(R.id.layout_last);
        progressBar = v.findViewById(R.id.progress_bar_test);
        chronometer = v.findViewById(R.id.progress_bar_txt);
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}