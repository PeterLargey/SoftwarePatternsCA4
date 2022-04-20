package com.example.softwarepatternsca4;

import java.util.ArrayList;

public class Sales {

    private ArrayList<Items> items;
    private String total;
    private String customerEmail;
    private String date;

    public Sales(){}

    public Sales(String total, ArrayList<Items> items, String customerEmail, String date){
        this.total = total;
        this.items = items;
        this.customerEmail = customerEmail;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
