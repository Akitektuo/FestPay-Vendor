package com.ariondan.vendor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ariondan.vendor.R;
import com.ariondan.vendor.adapter.grid.ProductAdapter;
import com.ariondan.vendor.adapter.list.CartAdapter;
import com.ariondan.vendor.local.preference.Preference;
import com.ariondan.vendor.model.CartModel;
import com.ariondan.vendor.model.ProductModel;
import com.ariondan.vendor.network.NetworkManager;
import com.ariondan.vendor.network.ProductResponse;

import java.util.ArrayList;
import java.util.List;

import static com.ariondan.vendor.network.NetworkManager.KEY_PRODUCT;
import static com.ariondan.vendor.util.ObjectPasser.cartObjects;


public class ProductsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ProductResponse {

    private RecyclerView gridProducts;
    private RecyclerView listCart;
    private List<CartModel> cartModels;
    private List<ProductModel> productModels;
    private RelativeLayout layoutCart;
    private PopupMenu popupFilter;
    private NetworkManager network;
    private AutoCompleteTextView editAutoSearch;
    private Preference preference;
    private RelativeLayout layoutContainerSearch;
    private Button buttonSearch;
    private boolean isSearch;
    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        findViewById(R.id.button_history).setOnClickListener(this);
        findViewById(R.id.button_cart_confirm).setOnClickListener(this);
        findViewById(R.id.button_cart_clear).setOnClickListener(this);
        findViewById(R.id.button_clear_search).setOnClickListener(this);
        buttonSearch = (Button) findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(this);
        layoutContainerSearch = (RelativeLayout) findViewById(R.id.layout_container_search);
        layoutContainerSearch.setVisibility(View.GONE);
        isSearch = false;
        editAutoSearch = (AutoCompleteTextView) findViewById(R.id.edit_auto_search);
        editAutoSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    network.getProducts(preference.getPreferenceString(Preference.KEY_VENDOR_SHOP), editAutoSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
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
        LinearLayoutManager layoutManagerCart = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listCart.setLayoutManager(layoutManagerCart);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listCart.getContext(),
                layoutManagerCart.getOrientation());
        listCart.addItemDecoration(dividerItemDecoration);
        listCart.setAdapter(new CartAdapter(this, cartModels));
        gridProducts.setAdapter(new ProductAdapter(this, listCart, (RelativeLayout) findViewById(R.id.layout_cart), productModels, cartModels));
        preference = new Preference(this);
        network.getProducts(preference.getPreferenceString(Preference.KEY_VENDOR_SHOP));
    }

    @Override
    public void onBackPressed() {
        isExit = false;
        AlertDialog.Builder builderExit = new AlertDialog.Builder(this);
        builderExit.setTitle("Do you want to exit?");
        builderExit.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isExit = true;
            }
        });
        builderExit.setNegativeButton("Log Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        builderExit.setNeutralButton("Cancel", null);
        AlertDialog dialogExit = builderExit.create();
        dialogExit.show();
        if (isExit) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_history:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case R.id.button_filter:
                popupFilter.show();
                break;
            case R.id.button_search:
                if (isSearch) {
                    layoutContainerSearch.setVisibility(View.GONE);
                    editAutoSearch.setText("");
                    buttonSearch.setBackground(getResources().getDrawable(R.drawable.search));
                } else {
                    layoutContainerSearch.setVisibility(View.VISIBLE);
                    buttonSearch.setBackground(getResources().getDrawable(R.drawable.search_cancel));
                }
                isSearch = !isSearch;
                break;
            case R.id.button_clear_search:
                editAutoSearch.setText("");
                break;
            case R.id.button_cart_confirm:
                cartObjects = cartModels;
                network.getProducts(preference.getPreferenceString(Preference.KEY_VENDOR_SHOP));
                startActivity(new Intent(this, PayActivity.class));
                break;
            case R.id.button_cart_clear:
                editAutoSearch.setText("");
                network.getProducts(preference.getPreferenceString(Preference.KEY_VENDOR_SHOP));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getTitle().toString().equals("Clear")) {
            network.getProducts(preference.getPreferenceString(Preference.KEY_VENDOR_SHOP));
        } else {
            network.getProducts(preference.getPreferenceString(Preference.KEY_VENDOR_SHOP), editAutoSearch.getText().toString(), item.getTitle().toString());
        }
        return true;
    }

    @Override
    public void loadProducts(List<ProductModel> productModels) {
        network.getAllProducts(preference.getPreferenceString(Preference.KEY_VENDOR_SHOP));
        layoutCart.setVisibility(View.GONE);
        this.productModels.clear();
        cartModels.clear();
        for (ProductModel product : productModels) {
            this.productModels.add(product);
        }
        if (gridProducts.getAdapter() != null) {
            gridProducts.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void loadAllProducts(List<ProductModel> productModels) {
        populatePopup(productModels);
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

