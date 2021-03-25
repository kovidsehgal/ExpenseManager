package com.example.expmanager;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expmanager.Adapter.RecyclerDashboardAdapter;
import com.example.expmanager.DataBaseHandler.MyDBHandler;
import com.example.expmanager.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//class to manage the dashboard fragment
public class
DashboardFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //floating button text
    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    //Animation
    private boolean isOpen = false;
    private Animation FadOpen,FadClose;

    //Database
    MyDBHandler db;
    private String flag;


    FirebaseAuth mAuth;
    String email = "";

    //Result TextView
    private TextView income_set_result;
    private TextView expense_set_result;
    private TextView balance_set_result;

    // RecyclerView
    RecyclerView mRecyclerIncome;
    RecyclerView mRecyclerExpense;
    RecyclerDashboardAdapter incomeDashboardAdapter;
    RecyclerDashboardAdapter expenseDashboardAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //get current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //connect floating button to layout

        fab_main_btn = myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myview.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myview.findViewById(R.id.expense_ft_btn);

        //connect floating text

        fab_income_txt = myview.findViewById(R.id.income_ft_text);
        fab_expense_txt = myview.findViewById(R.id.expense_ft_text);

        //Animation connect..
        FadOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadClose = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        income_set_result = myview.findViewById(R.id.income_set_result);
        expense_set_result = myview.findViewById(R.id.expense_set_result);
        balance_set_result = myview.findViewById(R.id.balance_result);


    //functionality of the floating button
        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();

                if(isOpen)
                {
                    fab_income_btn.startAnimation(FadClose);
                    fab_expense_btn.startAnimation(FadClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_expense_txt.startAnimation(FadClose);
                    fab_income_txt.startAnimation(FadClose);
                    fab_expense_txt.setClickable(false);
                    fab_income_txt.setClickable(false);
                    isOpen=false;
                }
                else
                {
                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_expense_btn.setClickable(true);
                    fab_income_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_expense_txt.setClickable(true);
                    fab_income_txt.setClickable(true);
                    isOpen=true;
                }

            }
        });

        //title show the total expense, income and balance
            db = new MyDBHandler(getActivity());
            double amount = 0;
            double expense = 0;
            double balance = 0;
            // List of all income data
            List<Data> dataList = db.getAllTransactions();
            List<Data> incomeList = new ArrayList<>();
            List<Data> expenseList = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getFlag().equals("I")) {
                    amount += dataList.get(i).getAmount();
                    incomeList.add(dataList.get(i));
                } else {
                    expense += dataList.get(i).getAmount();
                    expenseList.add(dataList.get(i));
                }
            }
            income_set_result.setText("$"+String.format("%.2f",amount));
            if (expense == 0) {
                expense_set_result.setText("$ 0.00");
            } else {
                expense_set_result.setText("- $" + String.format("%.2f", expense));
            }

            balance = amount-expense;

            if(balance<0)
            {
                balance_set_result.setTextColor(Color.parseColor("#FF1100"));
            }
            else
            {
                balance_set_result.setTextColor(Color.parseColor("#21CC27"));

            }
            balance_set_result.setText(""+String.format("%.2f",balance));

        //connect recycler view
        mRecyclerIncome = myview.findViewById(R.id.recycler_income);
        mRecyclerExpense = myview.findViewById(R.id.recycler_expense);

        //Recycler view
        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);
        incomeDashboardAdapter = new RecyclerDashboardAdapter(getContext(),incomeList);
        mRecyclerIncome.setAdapter(incomeDashboardAdapter);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);
        expenseDashboardAdapter = new RecyclerDashboardAdapter(getContext(), expenseList);
        mRecyclerExpense.setAdapter(expenseDashboardAdapter);

        return myview;
    }

    //method to add transactions
    private void addData()
    {
        //Fab Button income..
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="I";

                DataInsertIncome();
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="E";

                DataInsertExpense();
            }
        });
    }

    //income data insert method
    private void DataInsertIncome()
    {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);

        AlertDialog dialog = mydialog.create();

        EditText editAmount = myview.findViewById(R.id.amount_edit);
        Spinner editType = myview.findViewById(R.id.type_edit);
        EditText editNote = myview.findViewById(R.id.note_edit);

        Button btnSave = myview.findViewById(R.id.btnSave);
        Button btnCancel = myview.findViewById(R.id.btnCancel);

        //type spinner for income type

        ArrayAdapter<CharSequence> incomeAdapter =ArrayAdapter.createFromResource(getContext(),R.array.spinnerTransactionIncomeOptions, android.R.layout.simple_spinner_item);
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editType.setAdapter(incomeAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String type = editType.getSelectedItem().toString().trim();
                String Stringamount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();


                //input validation
                if(editType.getSelectedItemPosition() == -1)
                {
                    editType.setSelection(0);
                    return;
                }
                if(TextUtils.isEmpty(Stringamount))
                {
                    editAmount.setError("Required Field..");
                    return;
                }
                double amount = Double.parseDouble(Stringamount);
                if(amount<=0)
                {
                    editAmount.setError("Enter Valid Number Greater than 0");
                    return;
                }

                if(TextUtils.isEmpty(note))
                {
                    editNote.setError("Required Field..");
                    return;
                }


                //input saved in database
                try {
                    Data inData = new Data(amount, type, note, (LocalDate.now()).toString(), email, flag);
                    MyDBHandler inDatabaseData = new MyDBHandler(getActivity());
                    inDatabaseData.addTransaction(inData);
                    Log.d("Data insert floating","User added with ID: "+inData.getUserId());
                    Toast.makeText(getActivity(),"Data saved",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                catch(Exception e)
                {
                    Log.d("Data insert floating","Data not saved");
                }
                refreshFrag();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void refreshFrag () {
        // Refresh tab data:
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

    //method to insert expense data
    private void DataInsertExpense()
    {

            AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View myview = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
            mydialog.setView(myview);

            AlertDialog dialog = mydialog.create();

            EditText editAmount = myview.findViewById(R.id.amount_edit);
            Spinner editType = myview.findViewById(R.id.type_edit);
            EditText editNote = myview.findViewById(R.id.note_edit);

            Button btnSave = myview.findViewById(R.id.btnSave);
            Button btnCancel = myview.findViewById(R.id.btnCancel);

            //type spinner for expense type
            ArrayAdapter<CharSequence> expenseAdapter =ArrayAdapter.createFromResource(getContext(),R.array.spinnerTransactionExpenseOptions, android.R.layout.simple_spinner_item);
            expenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            editType.setAdapter(expenseAdapter);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {

                    String type = editType.getSelectedItem().toString().trim();
                    String Stringamount = editAmount.getText().toString().trim();
                    String note = editNote.getText().toString().trim();

                    //input validation
                    if(editType.getSelectedItemPosition() == -1)
                    {
                        editType.setSelection(0);
                        return;
                    }
                    if(TextUtils.isEmpty(Stringamount))
                    {
                        editAmount.setError("Required Field..");
                        return;
                    }
                    double amount = Double.parseDouble(Stringamount);
                    if(amount<=0)
                    {
                        editAmount.setError("Enter Valid Number Greater than 0");
                        return;
                    }

                    if(TextUtils.isEmpty(note))
                    {
                        editNote.setError("Required Field..");
                        return;
                    }

                    try {

                        //demo data add for time series chart
                        //comment this
                        Data d1 = new Data(40, "Food","Dummy Subway" , "2020-05-10",email,"E");
                        Data d2 = new Data(450, "Business", "Dummy Garba", "2020-01-23",email, "I");
                        Data d3 = new Data(660, "Other", "Dummy Income", "2020-12-23",email, "I");
                        Data d4 = new Data(850, "Household","Dummy McD" , "2020-07-10",email,"E");
                        //comment end here



                        Data inData = new Data(amount, type, note, (LocalDate.now()).toString(),email, flag);
                        MyDBHandler inDatabaseData = new MyDBHandler(getActivity());
                        inDatabaseData.addTransaction(inData);

                        //demo data inserted
                        //comment this
                        inDatabaseData.addTransaction(d1);
                        inDatabaseData.addTransaction(d2);
                        inDatabaseData.addTransaction(d3);
                        inDatabaseData.addTransaction(d4);
                        //comment end here

                        Log.d("data insert floating","User added with ID: "+inData.getUserId());
                        Toast.makeText(getActivity(),"Data saved",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    catch(Exception e)
                    {
                        Log.d("data insert floating","Data not saved");
                    }
                refreshFrag();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

    }

}
