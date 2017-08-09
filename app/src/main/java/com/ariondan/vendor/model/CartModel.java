package com.ariondan.vendor.model;

/**
 * Created by AoD Akitektuo on 09-Aug-17 at 22:21.
 */

public class CartModel {
    private int id;
    private String image;
    private String name;
    private double price;
    private int quantity;
    private double totalPrice;

    public CartModel(int id, String image, String name, double price, int quantity, double totalPrice) {
        setId(id);
        setImage(image);
        setName(name);
        setPrice(price);
        setQuantity(quantity);
        setTotalPrice(totalPrice);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
