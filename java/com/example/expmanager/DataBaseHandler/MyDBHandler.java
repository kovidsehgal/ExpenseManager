package com.example.expmanager.DataBaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.expmanager.Model.Data;
import com.example.expmanager.Params.Params;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
//Class to handle sqlite database
public class MyDBHandler extends SQLiteOpenHelper {

    FirebaseAuth mAuth;

    public MyDBHandler(Context context) {
        super(context, Params.DB_Name, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//Authorization to get current user logged
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();

 //Create table query
        String create = "CREATE TABLE "+ Params.TABLE_Name +"("
                + Params.KEY_ID + " INTEGER PRIMARY KEY,"
                + Params.KEY_AMOUNT + " DECIMAL(10,2),"
                + Params.KEY_TYPE + " TEXT,"
                + Params.KEY_NOTES + " TEXT,"
                + Params.KEY_DATE + " TEXT,"
                + Params.KEY_USERID + " TEXT,"
                + Params.KEY_FLAG + " TEXT);";
        Log.d("MYDBHandler", "Query being run is " + create);
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //method for adding each transaction in the database
    public void addTransaction(Data data) {
       SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put(Params.KEY_ID, data.getId());
        values.put(Params.KEY_AMOUNT, data.getAmount());
        values.put(Params.KEY_TYPE, data.getType());
        values.put(Params.KEY_NOTES, data.getNote());
        values.put(Params.KEY_DATE, data.getDate());
        values.put(Params.KEY_USERID, data.getUserId());
        values.put(Params.KEY_FLAG, data.getFlag());

      db.insert(Params.TABLE_Name, null,values);
        Log.d("MYDBHandler","Insert added with userID: "+data.getUserId());
     db.close();

    }

    //method to get all transactions from the database
    public List<Data> getAllTransactions() {

        try {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();

            List<Data> contactList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM data_table WHERE userid == ?;", new String[]{email});

            if (cursor.moveToFirst()) {
                do {
                    Data data = new Data();

                    data.setId(Integer.parseInt(cursor.getString(0)));
                    data.setAmount(Double.parseDouble(cursor.getString(1)));
                    data.setType(cursor.getString(2));
                    data.setNote(cursor.getString(3));
                    data.setDate(cursor.getString(4));
                    data.setUserId(cursor.getString(5));
                    data.setFlag(cursor.getString(6));

                    contactList.add(data);
                } while (cursor.moveToNext());

            }
            return contactList;
        } catch (Exception e) {
            Log.d("MYDBHandler", "Email entry problem " + e.getMessage());
            return null;
        }
    }

    //method to get all the income transactions
    public List<Data> getIncomeTransactions() {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();


        List<Data> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //String select = "SELECT * FROM "+Params.TABLE_Name;
        Cursor cursor = db.rawQuery("SELECT * FROM data_table WHERE userid == ?;", new String[]{email});
        if (cursor.moveToFirst()) {
            do {
                Data data = new Data();

                data.setId(Integer.parseInt(cursor.getString(0)));
                data.setAmount(Double.parseDouble(cursor.getString(1)));
                data.setType(cursor.getString(2));
                data.setNote(cursor.getString(3));
                data.setDate(cursor.getString(4));
                data.setUserId(cursor.getString(5));
                data.setFlag(cursor.getString(6));
                if (data.getFlag().equals("I")) {
                    contactList.add(data);
                }
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    //method to get all the expense transactions
    public List<Data> getExpenseTransactions() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        List<Data> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM data_table WHERE userid == ?;", new String[]{email});
        if (cursor.moveToFirst()) {
            do {
                Data data = new Data();

                data.setId(Integer.parseInt(cursor.getString(0)));
                data.setAmount(Double.parseDouble(cursor.getString(1)));
                data.setType(cursor.getString(2));
                data.setNote(cursor.getString(3));
                data.setDate(cursor.getString(4));
                data.setUserId(cursor.getString(5));
                data.setFlag(cursor.getString(6));
                if (data.getFlag().equals("E")) {
                    contactList.add(data);
                }
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    // To delete record from DB with a id as input
    public void deleteContact (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Params.TABLE_Name, Params.KEY_ID + "=?", new String[]{String.valueOf(id)});
        Log.d("MYDBHandler", "Deleted with id: "+ id);
        db.close();
    }

    // For getting number of records from DB
    public int getCount () {
        String query = "SELECT * from " + Params.TABLE_Name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor.getCount();
    }
}
