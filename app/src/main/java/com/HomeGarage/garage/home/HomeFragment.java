package com.HomeGarage.garage.home;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.HomeGarage.garage.DB.AppDataBase;
import com.HomeGarage.garage.DB.AppExcutor;
import com.HomeGarage.garage.DB.DBViewModel;
import com.HomeGarage.garage.DB.GrageInfo;
import com.HomeGarage.garage.DB.Opreation;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.models.LastOperModels;
import com.HomeGarage.garage.home.models.OffersModels;
import com.HomeGarage.garage.home.Adapter.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OffersAdpter.OfferListener , LastOperAdapter.LastOperListener {
    private  final String TAG="ttt";


    AppDataBase dataBase;
    ArrayList <OffersModels> offersModels = new ArrayList<>();
    RecyclerView recyclerOffers , recyclerLast;
    LinearLayout layoutNearFind , layoutAllFind ;
    Button btn_db;
    View seeAllOper;
    LastOperAdapter lastOperAdapter;
    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase= AppDataBase.getInstance(getContext());
        lastOperAdapter=new LastOperAdapter(getContext(),HomeFragment.this,3);
        // add item toOffers
        offersModels.add(new OffersModels(R.drawable.offer_crisimis, "Special offer 40% off on the occasion of New Year's Eve on all Cairo financier garages"));
        offersModels.add(new OffersModels(R.drawable.offer_special,"Special offer for the first time using the program"));
        offersModels.add(new OffersModels(R.drawable.offer_get,"Use the program in two payments and get a free process"));
        offersModels.add(new OffersModels(R.drawable.offer_weekend, "Half the price when using the Mall of Arabia garage on the weekends"));
        offersModels.add(new OffersModels(R.drawable.offer_new,"Cash back 10% when using the program daily for a week"));


        setUpViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        //find element
        initViews(root);
        //put LinearLayoutManager to recyclerOffers
        recyclerOffers.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL , false));
        //set adapter recyclerOffers
        recyclerOffers.setAdapter(new OffersAdpter(offersModels,getContext(),this));

        //put LinearLayoutManager to recyclerFind
        recyclerLast.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.VERTICAL , false));
        recyclerLast.setAdapter(lastOperAdapter);

        seeAllOper.setOnClickListener(v -> {

                LastOperFragment newFragment = new LastOperFragment(lastOperAdapter.getLastOpereations());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
        });

        layoutAllFind.setOnClickListener(v -> {
            SearchFragment newFragment = new SearchFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        layoutNearFind.setOnClickListener(v ->{
            SearchFragment newFragment = new SearchFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btn_db.setOnClickListener(V->{
            insertGrageData();
            insertLastOpreationData();
                });

        return root;

    }

    private void initViews(View v){
        recyclerOffers = v.findViewById(R.id.recycle_offers);
        recyclerLast = v.findViewById(R.id.recycler_last);
        layoutNearFind = v.findViewById(R.id.layout_near_find);
        layoutAllFind = v.findViewById(R.id.layout_all_find);
        seeAllOper = v.findViewById(R.id.see_all_last_oper);
        btn_db = v.findViewById(R.id.btn_add_db);
    }

    @Override
    public void OfferListener(OffersModels offersModels) {

        OffersFragment newFragment = new OffersFragment(offersModels);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void LastOperListener(Opreation opreation) {

            OperationsFragment newFragment = new OperationsFragment(opreation);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
    }

    public void insertGrageData()
    {
        GrageInfo grageInfo=new GrageInfo("Name","GHarbia","Mahlla","Namra_ELBasel",
                    "location",2.00,3.00,R.id.image);
     AppExcutor.getInstance().getDiskIO().execute(new Runnable() {
         @Override
         public void run() {
             for (int i = 0; i < 20; i++) {
                 dataBase.grageDAO().insertGrage(grageInfo);
             }
         }
     });

    }
    public void insertLastOpreationData()
    {

        Date date=new Date();

        Opreation opreation=new Opreation("accept","grage owner","client","mansora",date,3.00);
        AppExcutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
               for (int i=0;i<15;i++)
               {
                   dataBase.grageDAO().insertOpreation(opreation);
               }
            }
        });
    }

    public  void setUpViewModel()
    {
        DBViewModel viewModel=new ViewModelProvider(this).get(DBViewModel.class);
        final LiveData<List<Opreation>> opreations=viewModel.getOpreations();
        opreations.observeForever( new Observer<List<Opreation>>() {
            @Override
            public void onChanged(List<Opreation> opreations) {
                Log.d(TAG,opreations.size()+"");
                lastOperAdapter.setLastOpereations(opreations);
                Log.d(TAG,lastOperAdapter.getLastOpereations().size()+"");
            }

        });
    }

}