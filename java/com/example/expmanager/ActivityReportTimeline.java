package com.example.expmanager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.example.expmanager.DataBaseHandler.MyDBHandler;
import com.example.expmanager.Model.Data;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//class for Month wise Timeline Chart Report
//Used this to give user the comparison between income and expense over the year
public class ActivityReportTimeline extends AppCompatActivity {


    LineChart lineChart;
    List<Data> dataAllIncome, dataAllExpense;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_timeline);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MyDBHandler handler = new MyDBHandler(getApplicationContext());
        dataAllIncome=handler.getIncomeTransactions();
        dataAllExpense = handler.getExpenseTransactions();

        lineChart=findViewById(R.id.lineChartTimeline);

        //setting up the line chart data
        LineDataSet lineDataSet1 = new LineDataSet(lineChartDataSetsIncome(),"Income");
        LineDataSet lineDataSet2 = new LineDataSet(lineChartDataSetsExpense(),"Expense");
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet1);
        iLineDataSets.add(lineDataSet2);

        //line chart decoration
        lineDataSet1.setColor(Color.rgb(103, 192, 28));
        lineDataSet1.setCircleColor(Color.BLACK);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setLineWidth(5);
        lineDataSet1.setCircleRadius(2);
        lineDataSet1.setCircleHoleRadius(2);
        lineDataSet1.setValueTextSize(10);
        lineDataSet1.setValueTextColor(Color.BLACK);


        lineDataSet2.setColor(Color.rgb(248, 35, 35 ));
        lineDataSet2.setCircleColor(Color.BLACK);
        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setLineWidth(5);
        lineDataSet2.setCircleRadius(2);
        lineDataSet2.setCircleHoleRadius(2);
        lineDataSet2.setValueTextSize(10);
        lineDataSet2.setValueTextColor(Color.BLACK);

        LineData lineData= new LineData(iLineDataSets);
        lineChart.setData(lineData);

        lineChart.invalidate();

    }

    //cumulative data for income line chart
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Entry> lineChartDataSetsIncome()
    {

        float Jan=0, Feb=0, March=0, Apr=0, May=0, Jun=0, Jul=0, Aug=0, Sept=0, Oct=0, Nov=0, Dec=0;
         ArrayList<Entry> dataSet = new ArrayList<>();

         try {
             for (int i = 0; i < dataAllIncome.size(); i++)
             {
                 String monthData = dataAllIncome.get(i).getDate().substring(5,7);

                     switch (monthData)
                     {

                         case "01":
                             Jan+=dataAllIncome.get(i).getAmount();
                             break;

                         case "02":
                             Feb+=dataAllIncome.get(i).getAmount();
                             break;

                         case "03":
                             March+=dataAllIncome.get(i).getAmount();
                             break;
                         case "04":
                             Apr+=dataAllIncome.get(i).getAmount();
                             break;
                         case "05":
                             May+=dataAllIncome.get(i).getAmount();
                             break;
                         case "06":
                             Jun+=dataAllIncome.get(i).getAmount();
                             break;
                         case "07":
                             Jul+=dataAllIncome.get(i).getAmount();
                             break;
                         case "08":
                             Aug+=dataAllIncome.get(i).getAmount();
                             break;
                         case "09":
                             Sept+=dataAllIncome.get(i).getAmount();
                             break;
                         case "10":
                             Oct+=dataAllIncome.get(i).getAmount();
                             break;
                         case "11":
                             Nov+=dataAllIncome.get(i).getAmount();
                             break;
                         case "12":
                             Dec+=dataAllIncome.get(i).getAmount();
                             break;
                         default:
                             break;
                     }
                 }
             Toast.makeText(getApplicationContext(),"Moth calcultation done",Toast.LENGTH_SHORT).show();
         }
         catch (Exception ex)
         {
             Log.d("Report Timeline","Timeline data not set"+ ex.getMessage());

         }

         dataSet.add(new Entry(1,Jan));
        dataSet.add(new Entry(2,Feb));
        dataSet.add(new Entry(3,March));
        dataSet.add(new Entry(4,Apr));
        dataSet.add(new Entry(5,May));
        dataSet.add(new Entry(6,Jun));
        dataSet.add(new Entry(7,Jul));
        dataSet.add(new Entry(8,Aug));
        dataSet.add(new Entry(9,Sept));
        dataSet.add(new Entry(10,Oct));
        dataSet.add(new Entry(11,Nov));
        dataSet.add(new Entry(12,Dec));

        return dataSet;
    }

    //cumulative data for expense line chart
    private ArrayList<Entry> lineChartDataSetsExpense()
    {
        float Jan=0, Feb=0, March=0, Apr=0, May=0, Jun=0, Jul=0, Aug=0, Sept=0, Oct=0, Nov=0, Dec=0;
        ArrayList<Entry> dataSet = new ArrayList<>();

        try {
            for (int i = 0; i < dataAllExpense.size(); i++){

                    String monthData = dataAllExpense.get(i).getDate().substring(5,7);

                    switch (monthData)
                    {

                        case "01":
                            Jan+=dataAllExpense.get(i).getAmount();
                            break;

                        case "02":
                            Feb+=dataAllExpense.get(i).getAmount();
                            break;

                        case "03":
                            March+=dataAllExpense.get(i).getAmount();
                            break;
                        case "04":
                            Apr+=dataAllExpense.get(i).getAmount();
                            break;
                        case "05":
                            May+=dataAllExpense.get(i).getAmount();
                            break;
                        case "06":
                            Jun+=dataAllExpense.get(i).getAmount();
                            break;
                        case "07":
                            Jul+=dataAllExpense.get(i).getAmount();
                            break;
                        case "08":
                            Aug+=dataAllExpense.get(i).getAmount();
                            break;
                        case "09":
                            Sept+=dataAllExpense.get(i).getAmount();
                            break;
                        case "10":
                            Oct+=dataAllExpense.get(i).getAmount();
                            break;
                        case "11":
                            Nov+=dataAllExpense.get(i).getAmount();
                            break;
                        case "12":
                            Dec+=dataAllExpense.get(i).getAmount();
                            break;
                        default:
                            break;
                    }
                }
            Toast.makeText(getApplicationContext(),"Month calculation done",Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Log.d("Report Timeline","Timeline data not set"+ ex.getMessage());

        }

        dataSet.add(new Entry(1,Jan));
        dataSet.add(new Entry(2,Feb));
        dataSet.add(new Entry(3,March));
        dataSet.add(new Entry(4,Apr));
        dataSet.add(new Entry(5,May));
        dataSet.add(new Entry(6,Jun));
        dataSet.add(new Entry(7,Jul));
        dataSet.add(new Entry(8,Aug));
        dataSet.add(new Entry(9,Sept));
        dataSet.add(new Entry(10,Oct));
        dataSet.add(new Entry(11,Nov));
        dataSet.add(new Entry(12,Dec));
        return dataSet;
    }


}