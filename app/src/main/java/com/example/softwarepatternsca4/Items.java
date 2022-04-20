package com.example.softwarepatternsca4;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {

    private String id;
    private String name;
    private String manufacturer;
    private String price;
    private String category;
    private String image;
    private String size;
    private int stockLevel;


    public Items(String id, String name, String manufacturer, String price, String category, String image, String size, int stockLevel) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.category = category;
        this.image = image;
        this.size = size;
        this.stockLevel = stockLevel;
    }

    public Items(){}

    protected Items(Parcel in) {
        id = in.readString();
        name = in.readString();
        manufacturer = in.readString();
        price = in.readString();
        category = in.readString();
        image = in.readString();
        size = in.readString();
        stockLevel = in.readInt();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(manufacturer);
        parcel.writeString(price);
        parcel.writeString(category);
        parcel.writeString(image);
        parcel.writeString(size);
        parcel.writeInt(stockLevel);
    }
}
