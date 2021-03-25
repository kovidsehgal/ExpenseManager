package com.example.expmanager.Model;
//Class to save all the transaction data.
public class Data {


    private int id;
    private double amount;
    private String type;
    private String note;
    private String date;
    private String userId;
    private String flag;

    // Empty Constructor
    public Data() {

    }

    // Overloading
    public Data(double amount, String type, String note, String date, String userId, String flag) {
        this.amount = amount;
        this.type = type;
        this.note = note;
        this.date = date;
        this.userId = userId;
        this.flag = flag;
    }


    public Data(int id, double amount, String type, String note, String date, String userId, String flag) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.note = note;
        this.date = date;
        this.userId = userId;
        this.flag = flag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
