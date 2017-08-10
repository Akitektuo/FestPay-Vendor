package com.ariondan.vendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.ariondan.vendor.R;
import com.ariondan.vendor.adapter.grid.ProductAdapter;
import com.ariondan.vendor.adapter.list.CartAdapter;
import com.ariondan.vendor.model.CartModel;
import com.ariondan.vendor.model.ProductModel;

import java.util.ArrayList;
import java.util.List;


public class ProductsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView gridProducts;
    private RecyclerView listCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        findViewById(R.id.button_logout).setOnClickListener(this);
        findViewById(R.id.button_history).setOnClickListener(this);
        gridProducts = (RecyclerView) findViewById(R.id.grid_products);
        listCart = (RecyclerView) findViewById(R.id.list_cart);
        List<CartModel> cartModels = new ArrayList<>();
        List<ProductModel> productModels = new ArrayList<>();
        productModels.add(new ProductModel(1, "Coca", "", 2, "", "soda"));
        productModels.add(new ProductModel(2, "Cola", "", 3.5, "", "soda"));
        productModels.add(new ProductModel(3, "Coke", "", 100, "", "soda"));
        productModels.add(new ProductModel(4, "Big Cola", "", 7.5, "", "soda"));
        productModels.add(new ProductModel(5, "Coca-Cola", "", 5, "", "soda"));
        productModels.add(new ProductModel(6, "Mici", "", 5, "", "food"));
        gridProducts.setLayoutManager(new GridLayoutManager(this, 3));
        listCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CartAdapter cartAdapter = new CartAdapter(this, cartModels);
        listCart.setAdapter(cartAdapter);
        ProductAdapter productAdapter = new ProductAdapter(this, listCart, (RelativeLayout) findViewById(R.id.layout_cart), productModels, cartModels);
        gridProducts.setAdapter(productAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_logout:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.button_history:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
        }
    }
}

