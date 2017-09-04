package com.ariondan.vendor.model;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ariondan.vendor.database.DatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Akitektuo on 22.07.2017.
 */

public class HistoryModel {

    private int id;
    private String imageName;
    private Bitmap image;
    private String product;
    private double price;
    private int quantity;
    private double totalPrice;
    private String customer;
    private Date time;

    private Context context;

    public HistoryModel(Context context, Bitmap image, String product, double price, int quantity, double totalPrice, String customer, Date time) {
        setContext(context);
        DatabaseHelper database = new DatabaseHelper(context);
        String imageName = "image" + database.getHistory().size();
        setImage(image);
        saveImage(imageName);
        database.addHistory(imageName, product, price, quantity, totalPrice, customer, time);
    }

    public HistoryModel(int id, String imageName, String product, double price, int quantity, double totalPrice, String customer, Date time) {
        setId(id);
        setImage(getImage(imageName));
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void saveImage(String name) {
        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File myPath = new File(directory, name + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            getImage().compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getImage(String name) {
        Bitmap bitmap = null;
        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        try {
            File f = new File(directory, name + ".jpg");
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
