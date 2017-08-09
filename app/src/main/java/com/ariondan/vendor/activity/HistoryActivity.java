package com.ariondan.vendor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ariondan.vendor.R;
import com.ariondan.vendor.adapter.list.HistoryAdapter;
import com.ariondan.vendor.model.HistoryModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        List<HistoryModel> historyModels = new ArrayList<>();
        historyModels.add(new HistoryModel(this, 1, "", "Coca", 3.5, 1, 3.5, "Gica", new Date()));
        historyModels.add(new HistoryModel(this, 2, "", "Cola", 3.5, 2, 7, "Ionel", new Date()));
        historyModels.add(new HistoryModel(this, 3, "", "Coke", 3.5, 4, 14, "Asian", new Date()));
        historyModels.add(new HistoryModel(this, 4, "", "Mici", 5, 1, 5, "Nicusor", new Date()));
        RecyclerView listHistory = (RecyclerView) findViewById(R.id.list_history);
        listHistory.setAdapter(new HistoryAdapter(this, historyModels));
        listHistory.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.button_history_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_history_back:
                finish();
                break;
        }
    }
}
