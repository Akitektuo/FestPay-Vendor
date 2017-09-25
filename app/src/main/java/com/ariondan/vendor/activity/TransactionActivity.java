package com.ariondan.vendor.activity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.ariondan.vendor.R;
import com.ariondan.vendor.model.CartModel;
import com.ariondan.vendor.model.HistoryModel;
import com.ariondan.vendor.network.NetworkManager;
import com.ariondan.vendor.network.TransactionResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.ariondan.vendor.util.ObjectPasser.cartObjects;
import static com.ariondan.vendor.util.ObjectPasser.vendor;

public class TransactionActivity extends AppCompatActivity implements TransactionResponse, NfcAdapter.CreateNdefMessageCallback {

    private List<CartModel> cartModels;
    private NetworkManager network;
    private double credits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        network = new NetworkManager(this, NetworkManager.KEY_TRANSACTION);
        cartModels = cartObjects;
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
            Toast.makeText(this, "Transfered data: " + new String(message.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
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
        for (CartModel product : cartModels) {
            new HistoryModel(this, product.getImage(), product.getName(), product.getPrice(), product.getQuantity(), product.getTotalPrice(), userName, new Date());
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
        this.credits = credits;
        NfcAdapter.getDefaultAdapter(this).setNdefPushMessageCallback(this, this);
        finish();
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = credits + "_;_" + codifyProducts() + "_;_" + vendor;
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        return new NdefMessage(ndefRecord);
    }

    private String codifyProducts() {
        StringBuilder builder = new StringBuilder();
        for (CartModel x : cartModels) {
            if (x != cartModels.get(0)) {
                builder.append("__;__");
            }
            builder.append(x.getName()).append("___;___")
                    .append(x.getPrice()).append("___;___")
                    .append(x.getQuantity()).append("___;___")
                    .append(x.getTotalPrice());
        }
        return builder.toString();
    }


}
