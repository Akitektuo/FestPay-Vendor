package com.ariondan.vendor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.ariondan.vendor.R;
import com.ariondan.vendor.adapter.list.HistoryAdapter;
import com.ariondan.vendor.local.database.DatabaseHelper;
import com.ariondan.vendor.model.HistoryModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView editAutoSearch;
    private List<HistoryModel> historyModels;
    private DatabaseHelper database;
    private RecyclerView listHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        database = new DatabaseHelper(this);
        editAutoSearch = (AutoCompleteTextView) findViewById(R.id.edit_search_history);
        listHistory = (RecyclerView) findViewById(R.id.list_history);
        listHistory.setLayoutManager(new LinearLayoutManager(this));
        historyModels = database.getHistoryForSearch(editAutoSearch.getText().toString());
        listHistory.setAdapter(new HistoryAdapter(this, historyModels));
        setSearchSuggestions();
        findViewById(R.id.button_history_back).setOnClickListener(this);
        findViewById(R.id.button_search_history).setOnClickListener(this);
    }

    private void setSearchSuggestions() {
        ArrayList<String> productAndCustomers = new ArrayList<>();
        for (HistoryModel x : historyModels) {
            productAndCustomers.add(x.getCustomer());
            productAndCustomers.add(x.getProduct());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productAndCustomers);
        editAutoSearch.setAdapter(adapter);
    }

    private void searchInHistory() {
        historyModels.clear();
        List<HistoryModel> results = database.getHistoryForSearch(editAutoSearch.getText().toString());
        for (HistoryModel x : results) {
            historyModels.add(x);
        }
        listHistory.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_history_back:
                finish();
                break;
            case R.id.button_search_history:
                searchInHistory();
                break;
        }
    }
}
