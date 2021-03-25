package com.example.expmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.expmanager.Adapter.RecyclerViewAdapter;
import com.example.expmanager.DataBaseHandler.MyDBHandler;
import com.example.expmanager.Model.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
//class for income fragment
public class IncomeFragment extends Fragment {
    // Empty Public Constructor
    public IncomeFragment() {
    }
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    TextView incomeResult;
    double amount;
    MyDBHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());


        db = new MyDBHandler(getActivity());

        //recycler view connect
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        incomeResult = view.findViewById(R.id.income_txt_result);
        adapter = new RecyclerViewAdapter(getContext(),initData());
        recyclerView.setAdapter(adapter);
        return view;
    }

//cumulate income data from all transactions
    public List<Data> initData(){
        amount = 0;
        // List of all income data
        List<Data> dataList = db.getAllTransactions();
        List<Data> filterDataList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {

            if (dataList.get(i).getFlag().equals("I")) {
                amount += dataList.get(i).getAmount();
                filterDataList.add(dataList.get(i));
            }
        }
        incomeResult.setText("$"+String.format("%.2f", amount));
        return filterDataList;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}