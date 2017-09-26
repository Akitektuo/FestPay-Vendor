package com.ariondan.vendor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private RelativeLayout layoutContainerSearch;
    private Button buttonSearch;
    private boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        database = new DatabaseHelper(this);
        editAutoSearch = (AutoCompleteTextView) findViewById(R.id.edit_search_history);
        editAutoSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchInHistory();
                    return true;
                }
                return false;
            }
        });
        isSearch = false;
        layoutContainerSearch = (RelativeLayout) findViewById(R.id.layout_container_search_history);
        layoutContainerSearch.setVisibility(View.GONE);
        listHistory = (RecyclerView) findViewById(R.id.list_history);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listHistory.getContext(),
                layoutManager.getOrientation());
        listHistory.addItemDecoration(dividerItemDecoration);
        listHistory.setLayoutManager(layoutManager);
        historyModels = database.getHistoryForSearch(editAutoSearch.getText().toString());
        listHistory.setAdapter(new HistoryAdapter(this, historyModels));
        setSearchSuggestions();
        findViewById(R.id.button_history_back).setOnClickListener(this);
        buttonSearch = (Button) findViewById(R.id.button_search_history);
        buttonSearch.setOnClickListener(this);
        findViewById(R.id.button_clear_search_history).setOnClickListener(this);
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
                if (isSearch) {
                    layoutContainerSearch.setVisibility(View.GONE);
                    editAutoSearch.setText("");
                    buttonSearch.setBackground(getResources().getDrawable(R.drawable.search));
                    hideKeyboard();
                } else {
                    layoutContainerSearch.setVisibility(View.VISIBLE);
                    buttonSearch.setBackground(getResources().getDrawable(R.drawable.search_cancel));
                    editAutoSearch.requestFocus();
                }
                isSearch = !isSearch;
                break;
            case R.id.button_clear_search_history:
                editAutoSearch.setText("");
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
