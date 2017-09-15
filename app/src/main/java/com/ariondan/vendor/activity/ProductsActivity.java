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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ariondan.vendor.R;
import com.ariondan.vendor.adapter.grid.ProductAdapter;
import com.ariondan.vendor.adapter.list.CartAdapter;
import com.ariondan.vendor.model.CartModel;
import com.ariondan.vendor.model.ProductModel;
import com.ariondan.vendor.network.NetworkManager;
import com.ariondan.vendor.network.ProductResponse;

import java.util.ArrayList;
import java.util.List;

import static com.ariondan.vendor.network.NetworkManager.KEY_PRODUCT;
import static com.ariondan.vendor.util.ObjectPasser.cartObjects;
import static com.ariondan.vendor.util.ObjectPasser.vendor;


public class ProductsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ProductResponse {

    private RecyclerView gridProducts;
    private RecyclerView listCart;
    private List<CartModel> cartModels;
    private List<ProductModel> productModels;
    private RelativeLayout layoutCart;
    private PopupMenu popupFilter;
    private NetworkManager network;
    private AutoCompleteTextView editAutoSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        findViewById(R.id.button_logout).setOnClickListener(this);
        findViewById(R.id.button_history).setOnClickListener(this);
        findViewById(R.id.button_cart_confirm).setOnClickListener(this);
        findViewById(R.id.button_cart_clear).setOnClickListener(this);
        findViewById(R.id.button_search).setOnClickListener(this);
        editAutoSearch = (AutoCompleteTextView) findViewById(R.id.edit_auto_search);
        Button buttonFilter = (Button) findViewById(R.id.button_filter);
        buttonFilter.setOnClickListener(this);
        layoutCart = (RelativeLayout) findViewById(R.id.layout_cart);
        gridProducts = (RecyclerView) findViewById(R.id.grid_products);
        listCart = (RecyclerView) findViewById(R.id.list_cart);
        popupFilter = new PopupMenu(this, buttonFilter);
        popupFilter.setOnMenuItemClickListener(this);
        cartModels = new ArrayList<>();
        productModels = new ArrayList<>();
        network = new NetworkManager(this, KEY_PRODUCT);
        gridProducts.setLayoutManager(new GridLayoutManager(this, 3));
        listCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listCart.setAdapter(new CartAdapter(this, cartModels));
        gridProducts.setAdapter(new ProductAdapter(this, listCart, (RelativeLayout) findViewById(R.id.layout_cart), productModels, cartModels));
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
            case R.id.button_search:
                network.getProducts(vendor, editAutoSearch.getText().toString());
                break;
            case R.id.button_cart_confirm:
                cartObjects = cartModels;
                startActivity(new Intent(this, PayActivity.class));
                break;
            case R.id.button_cart_clear:
                network.getProducts(vendor);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        network.getProducts(vendor);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getTitle().toString().equals("Clear")) {
            network.getProducts(vendor);
        } else {
            network.getProducts(vendor, editAutoSearch.getText().toString(), item.getTitle().toString());
        }
        return true;
    }

    @Override
    public void loadProducts(List<ProductModel> productModels) {
        layoutCart.setVisibility(View.GONE);
        this.productModels.clear();
        cartModels.clear();
        populatePopup(productModels);
        for (ProductModel product : productModels) {
            this.productModels.add(product);
        }
        if (gridProducts.getAdapter() != null) {
            gridProducts.getAdapter().notifyDataSetChanged();
        }
    }

    private void populatePopup(List<ProductModel> productModels) {
        boolean misses;
        popupFilter.getMenu().clear();
        for (ProductModel product : productModels) {
            misses = true;
            for (int i = 0; i < popupFilter.getMenu().size(); i++) {
                if (popupFilter.getMenu().getItem(i).getTitle().toString().equals(product.getCategory())) {
                    misses = false;
                }
            }
            if (misses) {
                popupFilter.getMenu().add(product.getCategory());
            }
        }
        popupFilter.getMenu().add("Clear");
    }
}

