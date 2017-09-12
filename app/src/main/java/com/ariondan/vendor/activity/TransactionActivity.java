package com.ariondan.vendor.activity;

import android.content.Intent;
import android.nfc.NdefMessage;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.ariondan.vendor.util.ObjectPasser.cartObjects;

public class TransactionActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private List<CartModel> cartModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cartModels = cartObjects;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage message = (NdefMessage) rawMessages[0];
//            textNfc.setText(new String(message.getRecords()[0].getPayload()));
            //example of message
            // "3_;_Mihnea_;_true"
            List<String> transferredData = Arrays.asList(new String(message.getRecords()[0].getPayload()).split("_;_"));
            if (Boolean.parseBoolean(transferredData.get(2))) {
                doTransaction(Integer.parseInt(transferredData.get(0)), transferredData.get(1));
            } else {
                Toast.makeText(this, "Invalid device.", Toast.LENGTH_SHORT).show();
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
        //  date
        finish();
    }
}
