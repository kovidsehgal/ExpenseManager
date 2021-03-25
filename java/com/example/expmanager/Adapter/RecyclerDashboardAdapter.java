package com.example.expmanager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expmanager.Model.Data;
import com.example.expmanager.R;

import java.util.List;
//Adapter used to populate the scroll view on the dashboard showing the recent added transactions
//used this feature for the user to review latest income and expenses
public class RecyclerDashboardAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Data> dataList;

    public RecyclerDashboardAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public TextView type;
    public TextView amount;
    public TextView date;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income,parent,false);
        return new ViewHolder(view);
    }
//getting all the transaction details from Data Class and populating the view
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Data data = dataList.get(position);
        type.setText(data.getType());
        amount.setText(""+data.getAmount());
        if (dataList.get(position).getFlag().equals("I"))
        {
            amount.setTextColor(Color.parseColor("#21CC27"));
        }
        else {
            amount.setTextColor(Color.parseColor("#FF1100"));
        }
        date.setText(data.getDate());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount_income_ds);
            type = itemView.findViewById(R.id.type_Income_ds);
            date = itemView.findViewById(R.id.date_income_ds);
        }

    }
}
