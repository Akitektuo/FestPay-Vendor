package com.ariondan.vendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ariondan.vendor.R;
import com.ariondan.vendor.adapter.grid.ProductAdapter;
import com.ariondan.vendor.adapter.list.CartAdapter;
import com.ariondan.vendor.model.CartModel;
import com.ariondan.vendor.model.ProductModel;

import java.util.ArrayList;
import java.util.List;


public class ProductsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private RecyclerView gridProducts;
    private RecyclerView listCart;
    private List<CartModel> cartModels;
    private List<ProductModel> productModels;
    private RelativeLayout layoutCart;
    private PopupMenu popupFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        findViewById(R.id.button_logout).setOnClickListener(this);
        findViewById(R.id.button_history).setOnClickListener(this);
        findViewById(R.id.button_cart_confirm).setOnClickListener(this);
        findViewById(R.id.button_cart_clear).setOnClickListener(this);
        Button buttonFilter = (Button) findViewById(R.id.button_filter);
        buttonFilter.setOnClickListener(this);
        layoutCart = (RelativeLayout) findViewById(R.id.layout_cart);
        gridProducts = (RecyclerView) findViewById(R.id.grid_products);
        listCart = (RecyclerView) findViewById(R.id.list_cart);
        popupFilter = new PopupMenu(this, buttonFilter);
        popupFilter.getMenu().add("Soda");
        popupFilter.getMenu().add("Food");
        popupFilter.setOnMenuItemClickListener(this);
        refreshPage();
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
            case R.id.button_filter:
                popupFilter.show();
                break;
            case R.id.button_cart_confirm:
                Toast.makeText(this, "Tap the screen to cancel the connection.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, PayActivity.class));
                break;
            case R.id.button_cart_clear:
                refreshPage();
                break;
        }
    }

    private void refreshPage() {
        layoutCart.setVisibility(View.GONE);
        cartModels = new ArrayList<>();
        productModels = new ArrayList<>();
        productModels.add(new ProductModel(1, "Coca", "", 2, "", "soda"));
        productModels.add(new ProductModel(2, "Cola", "", 3.5, "", "soda"));
        productModels.add(new ProductModel(3, "Coke", "", 100, "", "soda"));
        productModels.add(new ProductModel(4, "Big Cola", "", 7.5, "", "soda"));
        productModels.add(new ProductModel(5, "Coca-Cola", "", 5, "", "soda"));
        productModels.add(new ProductModel(6, "Mici", "", 5, "", "food"));
        gridProducts.setLayoutManager(new GridLayoutManager(this, 3));
        listCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listCart.setAdapter(new CartAdapter(this, cartModels));
        gridProducts.setAdapter(new ProductAdapter(this, listCart, (RelativeLayout) findViewById(R.id.layout_cart), productModels, cartModels));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "This will do stuff.", Toast.LENGTH_SHORT).show();
        return true;
    }
}

