package com.example.expmanager;

import android.graphics.Color;
import android.os.Bundle;

import com.example.expmanager.DataBaseHandler.MyDBHandler;
import com.example.expmanager.Model.Data;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//class for Category wise Pie Chart Report
//used this to give user the comparison between different sources of income and different areas of expense
public class ActivityReportCategory extends AppCompatActivity {

    float Other = 0, Food = 0, Entertainment = 0, Healthcare = 0, Utilities = 0, Travel = 0, Household = 0, Personal = 0, PayCheck = 0, Business = 0;
    PieChart piechart;
    RadioGroup incomeExpense;
    List<Data> dataAllIncome;
    List<Data> dataAllExpense;

    Map<String,Float> categorySplitValuesIncome = new HashMap<>();
    Map<String,Float> categorySplitValuesExpense = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        incomeExpense = findViewById(R.id.expenseIncomeRadioGroup);

        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());

        dataAllIncome = dbHandler.getIncomeTransactions();
        dataAllExpense=dbHandler.getExpenseTransactions();

        curateData();
        displayPieChart();
        incomeExpense.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                piechart.invalidate();

                curateData();
                displayPieChart();
            }
        });


        }
    //method to prepare data for insertion
        private void curateData()
        {
            Other = 0;
            Food = 0;
            Entertainment = 0;
            Healthcare = 0;
            Utilities = 0;
            Travel = 0;
            Household = 0;
            Personal = 0;
            PayCheck = 0;
            Business = 0;

            try {

                    if (incomeExpense.getCheckedRadioButtonId() == R.id.IncomeRadioBtn)
                        {
                            //combine all the transactions based on their type
                            for (int i = 0; i < dataAllIncome.size(); i++)
                            {

                            switch (dataAllIncome.get(i).getType()) {
                                case "Other":
                                    Other += dataAllIncome.get(i).getAmount();
                                    break;

                                case "PayCheck":
                                    PayCheck += dataAllIncome.get(i).getAmount();
                                    break;

                                case "Business":
                                    Business += dataAllIncome.get(i).getAmount();
                                    break;

                                default:
                                    break;
                            }
                        }

                    }
                    else {
                        for (int i = 0; i < dataAllExpense.size(); i++)
                        {
                            switch (dataAllExpense.get(i).getType()) {
                                case "Other":
                                    Other += dataAllExpense.get(i).getAmount();
                                    break;
                                case "Food":
                                    Food += dataAllExpense.get(i).getAmount();
                                    break;
                                case "Entertainment":
                                    Entertainment += dataAllExpense.get(i).getAmount();
                                    break;
                                case "Healthcare":
                                    Healthcare += dataAllExpense.get(i).getAmount();
                                    break;
                                case "Utilities":
                                    Utilities += dataAllExpense.get(i).getAmount();
                                    break;
                                case "Travel":
                                    Travel += dataAllExpense.get(i).getAmount();
                                    break;
                                case "Household":
                                    Household += dataAllExpense.get(i).getAmount();
                                    break;
                                case "Personal":
                                    Personal += dataAllExpense.get(i).getAmount();
                                    break;

                                default:
                                    break;
                            }

                        }
                    }
                Toast.makeText(ActivityReportCategory.this, "Category split Done", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e)
            {
                Log.d("Janhvi", "Category split not happening" + e.getMessage());
            }

        }

        //method to design and display the Pie Chart
        private void displayPieChart()
        {
            piechart = findViewById(R.id.PieChartCategory);


            categorySplitValuesExpense.put("Other", Other);
            categorySplitValuesExpense.put("Food", Food);
            categorySplitValuesExpense.put("Entertainment", Entertainment);
            categorySplitValuesExpense.put("Healthcare", Healthcare);
            categorySplitValuesExpense.put("Utilities", Utilities);
            categorySplitValuesExpense.put("Travel", Travel);
            categorySplitValuesExpense.put("Household", Household);
            categorySplitValuesExpense.put("Personal", Personal);

            categorySplitValuesIncome.put("Other",Other);
            categorySplitValuesIncome.put("PayCheck", PayCheck);
            categorySplitValuesIncome.put("Business", Business);


            ArrayList<PieEntry> Categories = new ArrayList<>();
            try {

                if(incomeExpense.getCheckedRadioButtonId() == R.id.IncomeRadioBtn)
                {
                    for (Map.Entry<String, Float> e : categorySplitValuesIncome.entrySet()) {

                        if(e.getValue()==0)
                        {
                            continue;
                        }
                        else {

                            Categories.add(new PieEntry(e.getValue(), e.getKey()));
                            Toast.makeText(ActivityReportCategory.this, "PieChart Income Entry Done", Toast.LENGTH_SHORT).show();
                        }

                    }}
                else
                {
                    for (Map.Entry<String, Float> e : categorySplitValuesExpense.entrySet()) {

                        if(e.getValue()==0) {
                            continue;
                        }
                        else {
                            Categories.add(new PieEntry(e.getValue(), e.getKey()));
                        }

                    }
                    Toast.makeText(ActivityReportCategory.this, "PieChart Expense Entry Done", Toast.LENGTH_SHORT).show();
                } }
            catch (Exception e) {
                Log.d("Janhvi", "Piechart entry not happening");
            }


            PieDataSet pieDataSet = new PieDataSet(Categories,"");
            pieDataSet.setSliceSpace(3f);
            pieDataSet.setSelectionShift(5f);
            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(16f);

            PieData pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(15f);
            pieData.setValueTextColor(Color.BLACK);


            piechart.setData(pieData);

            piechart.setDrawHoleEnabled(true);
            piechart.setHoleRadius(50f);
            piechart.setTransparentCircleAlpha(10);
            piechart.setRotationAngle(0);
            piechart.setRotationEnabled(true);


            piechart.setCenterTextSize(15);
            piechart.setCenterTextColor(Color.GRAY);


            piechart.getDescription().setEnabled(false);
            piechart.setCenterText("Categories");
            piechart.animateY(1000, Easing.EaseInOutCubic);


        }
        }






