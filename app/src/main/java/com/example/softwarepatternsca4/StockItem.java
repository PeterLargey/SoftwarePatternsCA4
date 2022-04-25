package com.example.softwarepatternsca4;

public class StockItem {

    String id;
    String amount;

    public StockItem (){}

    public StockItem(String id, String amount){
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
