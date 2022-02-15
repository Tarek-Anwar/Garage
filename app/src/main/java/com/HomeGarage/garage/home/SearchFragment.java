package com.HomeGarage.garage.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.Adapter.SearchAdapter;
import com.HomeGarage.garage.home.models.SearchModel;

import java.util.ArrayList;


public class SearchFragment extends Fragment implements SearchAdapter.SearchListener {

    RecyclerView recyclerSearch;
    ArrayList<SearchModel> searchModelList = new ArrayList<>();

    public SearchFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchModelList.add(new SearchModel("Garage 1","cairo"));
        searchModelList.add(new SearchModel("Garage 2","Mahalla"));
        searchModelList.add(new SearchModel("Garage 3","Mansora"));
        searchModelList.add(new SearchModel("Garage 4","cairo"));
        searchModelList.add(new SearchModel("Garage 5","Alex"));
        searchModelList.add(new SearchModel("Garage 6","Mansora"));
        searchModelList.add(new SearchModel("Garage 7","cairo"));
        searchModelList.add(new SearchModel("Garage 8","Mahalla"));
        searchModelList.add(new SearchModel("Garage 9","cairo"));
        searchModelList.add(new SearchModel("Garage 10","Alex"));
        searchModelList.add(new SearchModel("Garage 11","Mansora"));
        searchModelList.add(new SearchModel("Garage 12","Mahalla"));
        searchModelList.add(new SearchModel("Garage 1","cairo"));
        searchModelList.add(new SearchModel("Garage 2","Mahalla"));
        searchModelList.add(new SearchModel("Garage 3","Mansora"));
        searchModelList.add(new SearchModel("Garage 4","cairo"));
        searchModelList.add(new SearchModel("Garage 5","Alex"));
        searchModelList.add(new SearchModel("Garage 6","Mansora"));
        searchModelList.add(new SearchModel("Garage 7","cairo"));
        searchModelList.add(new SearchModel("Garage 8","Mahalla"));
        searchModelList.add(new SearchModel("Garage 9","cairo"));
        searchModelList.add(new SearchModel("Garage 10","Alex"));
        searchModelList.add(new SearchModel("Garage 11","Mansora"));
        searchModelList.add(new SearchModel("Garage 12","Mahalla"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_search, container, false);
        recyclerSearch = root.findViewById(R.id.recycle_search);

        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerSearch.setAdapter(new SearchAdapter(searchModelList,getContext(),this));

        return root;
    }

    @Override
    public void SearchListener(SearchModel searchModel) {
        Toast.makeText(getContext(), searchModel.getNameGarage(), Toast.LENGTH_SHORT).show();
    }
}