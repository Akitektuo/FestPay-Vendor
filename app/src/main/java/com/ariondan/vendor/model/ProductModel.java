package com.ariondan.vendor.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AoD Akitektuo on 30-Jul-17 at 02:52.
 */

public class ProductModel {
    private int id;
    private String name;
    private String imageURL;
    private Bitmap image;
    private double price;
    private String description;
    private String category;

    public ProductModel(int id, String name, String imageURL, double price, String description, String category) {
        setId(id);
        setName(name);
        setImageURL(imageURL);
        setPrice(price);
        setDescription(description);
        setCategory(category);
    }

    public static List<ProductModel> convertProducts(List<ProductNetworkModel> products) {
        List<ProductModel> results = new ArrayList<>();
        for (ProductNetworkModel product : products) {
            results.add(convertProduct(product));
        }
        return results;
    }

    public static ProductModel convertProduct(ProductNetworkModel product) {
        return new ProductModel(product.getId(), product.getName(), product.getImageURL(), product.getPrice(), product.getDescription(), product.getCategory());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
