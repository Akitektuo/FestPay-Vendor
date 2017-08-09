package com.ariondan.vendor.model;

import android.content.Context;

import com.ariondan.vendor.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Akitektuo on 22.07.2017.
 */

public class HistoryModel {

    //TODO: Insert history when object is created AND make a method getEntireHistory() which returns a List<History>

    private int id;
    private String picture;
    private String product;
    private double price;
    private int quantity;
    private double totalPrice;
    private String customer;
    private Date time;

    private DatabaseHelper database;
    private Context context;

    public HistoryModel(Context context) {
        setContext(context);
        database = new DatabaseHelper(context);
    }

    public HistoryModel(Context context, String picture, String product, double price, int quantity, double totalPrice, String customer, Date time) {
        setContext(context);
        database = new DatabaseHelper(context);
        database.addHistory(picture, product, price, quantity, totalPrice, customer, time);
    }

    public HistoryModel(int id, String picture, String product, double price, int quantity, double totalPrice, String customer, Date time) {
        setId(id);
        setPicture(picture);
        setProduct(product);
        setPrice(price);
        setQuantity(quantity);
        setTotalPrice(totalPrice);
        setCustomer(customer);
        setTime(time);
    }

    public HistoryModel(Context context, int id, String picture, String product, double price, int quantity, double totalPrice, String customer, Date time) {
        setContext(context);
        setId(id);
        setPicture(picture);
        setProduct(product);
        setPrice(price);
        setQuantity(quantity);
        setTotalPrice(totalPrice);
        setCustomer(customer);
        setTime(time);
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTimeAsString() {
        String rawDate = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss").format(getTime());
        return rawDate.replaceAll("/", ".").replaceAll("-", "\n");
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
