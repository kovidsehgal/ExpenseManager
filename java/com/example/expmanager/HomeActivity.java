package com.example.expmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.expmanager.DataBaseHandler.MyDBHandler;
import com.example.expmanager.Model.Data;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
//class for the home activity containing the navigation menu
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    //Fragment
    private DashboardFragment dashboardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;
    private ReportOptionsFragment optionsFragment;

    private FirebaseAuth mAuth;

    //Database
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //set toolbar
        try {
            Toolbar toolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle("Expense Manager");

            mAuth = FirebaseAuth.getInstance();

            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close
            );
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            //set side and bottom navigation
            NavigationView navigationView = findViewById(R.id.navView);
            navigationView.setNavigationItemSelectedListener(this);

            bottomNavigationView = findViewById(R.id.bottomNavigationBar);
            frameLayout = findViewById(R.id.main_frame);

            //fragment
            dashboardFragment = new DashboardFragment();
            incomeFragment = new IncomeFragment();
            expenseFragment = new ExpenseFragment();
            optionsFragment = new ReportOptionsFragment();

            //by default should be dashboard fragment
            setFragment(dashboardFragment);

        //functionality of bottom navigation buttons
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.dashboard:
                            setFragment(dashboardFragment);
                            bottomNavigationView.setItemBackgroundResource(R.drawable.backgroundcolor);
                            return true;

                        case R.id.income:
                            setFragment(incomeFragment);
                            bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                            return true;

                        case R.id.expense:
                            setFragment(expenseFragment);
                            bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                        return true;

                        default:
                            return false;
                    }
                }
            });

        }catch (Exception ex)
        {
            Log.d("Home Activity","Fragment set error" + ex.getMessage());
        }

    }


    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

//back press functionality
    @Override
    public void onBackPressed() {

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.END))
        {
            drawerLayout.closeDrawer(GravityCompat.END);
        }else {
            super.onBackPressed();
        }
    }

    //side navigation select functionality
    public void displaySelectedListener ( int itemID)
    {
        Fragment fragment = null;
        switch (itemID)
        {
            case R.id.dashboard:
                fragment = new DashboardFragment();
                break;

            case R.id.income:
                fragment = new IncomeFragment();
                break;

            case R.id.expense:
                fragment = new ExpenseFragment();
                break;
            case R.id.report:
                fragment= new ReportOptionsFragment();
                break;

            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),StartActivity.class));
                break;


        }
        if(fragment != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame,fragment);
            ft.commit();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return true;
    }
}