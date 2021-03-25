package com.example.expmanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expmanager.DataBaseHandler.MyDBHandler;
import com.example.expmanager.HomeActivity;
import com.example.expmanager.Model.Data;
import com.example.expmanager.R;

import java.util.List;
//Adapter used to populate the income and expense fragments.
//Used this feature to show the user bifurcated views of income and expenses.
public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Data> dataList;

    public TextView amount;
    public TextView type;
    public TextView note;
    public TextView date;
    public ImageView image;

    public RecyclerViewAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }



    // where to get the single card as view holder object
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data,parent,false);
        return new ViewHolder(view);
    }

    // Populate the view holder with the data from Data Class.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Data data = dataList.get(position);
        amount.setText(""+data.getAmount());
        type.setText(data.getType());
        note.setText(data.getNote());
        date.setText(data.getDate());
        image.setOnClickListener(v -> {
            MyDBHandler db = new MyDBHandler(context);
            Toast.makeText(context, "Record Deleted", Toast.LENGTH_SHORT).show();
            List<Data> dataList;
            if (data.getFlag().equals("I")) {
                dataList = db.getIncomeTransactions();
            } else {
                dataList = db.getExpenseTransactions();
            }
            db.deleteContact(dataList.get(position).getId());
            removeItem(holder.getAdapterPosition());

            v.getContext().startActivity(new Intent(context,HomeActivity.class));
        });

    }
    //Delete Transaction Actions
    private void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            amount = itemView.findViewById(R.id.amount_txt_income);
            type = itemView.findViewById(R.id.type_txt_income);
            note = itemView.findViewById(R.id.note_txt_income);
            date = itemView.findViewById(R.id.date_txt_income);
            image = itemView.findViewById(R.id.imageViewDelete);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
