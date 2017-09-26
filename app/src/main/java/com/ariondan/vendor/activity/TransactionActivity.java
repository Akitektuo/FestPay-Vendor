package com.ariondan.vendor.activity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.ariondan.vendor.R;
import com.ariondan.vendor.local.preference.Preference;
import com.ariondan.vendor.model.CartModel;
import com.ariondan.vendor.model.HistoryModel;
import com.ariondan.vendor.network.NetworkManager;
import com.ariondan.vendor.network.TransactionResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TransactionActivity extends AppCompatActivity implements TransactionResponse {

    private NetworkManager network;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        network = new NetworkManager(this, NetworkManager.KEY_TRANSACTION);
        preference = new Preference(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage message = (NdefMessage) rawMessages[0];
            //example of message
            // "3_;_Mihnea_;_true"
            try {
                List<String> transferredData = Arrays.asList(new String(message.getRecords()[0].getPayload()).split("_;_"));
                if (Boolean.parseBoolean(transferredData.get(2))) {
                    doTransaction(Integer.parseInt(transferredData.get(0)), transferredData.get(1));
                } else {
                    Toast.makeText(this, "Invalid device.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error while parsing data", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doTransaction(int userId, String userName) {
        System.out.println(preference.getPreferenceString(Preference.KEY_NFC_DATA));
        List<CartModel> cartModels = new ArrayList<>();
        List<String> products = Arrays.asList(preference.getPreferenceString(Preference.KEY_NFC_DATA).split("__;__"));
        for (String x : products) {
            System.out.println(x);
            List<String> items = Arrays.asList(x.split("_;_"));
            new HistoryModel(this, items.get(0), Double.parseDouble(items.get(1)), Integer.parseInt(items.get(2)), Double.parseDouble(items.get(3)), userName, new Date());

        }

        //connect server to send transaction details
        //HTTP POST:
        //  products
        //  vendorId
        //  userId

        network.doTransaction(cartModels, 1, userId);
    }

    @Override
    public void onTransaction(double credits) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }


}
