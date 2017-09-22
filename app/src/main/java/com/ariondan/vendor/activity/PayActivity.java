package com.ariondan.vendor.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.ariondan.vendor.R;
import com.ariondan.vendor.local.preference.Preference;
import com.ariondan.vendor.model.CartModel;
import com.ariondan.vendor.model.HistoryModel;
import com.ariondan.vendor.network.NetworkManager;
import com.ariondan.vendor.network.TransactionResponse;
import com.ariondan.vendor.nfc.CardReader;
import com.ariondan.vendor.nfc.CardService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import me.alexrs.wavedrawable.WaveDrawable;

import static com.ariondan.vendor.util.ObjectPasser.cartObjects;

public class PayActivity extends AppCompatActivity implements View.OnClickListener, CardReader.AccountCallback, TransactionResponse {

    private final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private CardReader cardReader;
    private NfcAdapter nfcAdapter;
    private Preference preference;
    private List<CartModel> cartModels;
    private NetworkManager network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        findViewById(R.id.layout_nfc).setOnClickListener(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ImageView imageNfc = (ImageView) findViewById(R.id.image_pay_nfc);
        ImageView imageBackground = (ImageView) findViewById(R.id.image_pay_background);
        WaveDrawable waveDrawable = new WaveDrawable(getResources().getColor(R.color.colorPrimary), 500);
        WaveDrawable waveBackground = new WaveDrawable(getResources().getColor(R.color.colorPrimary), 1000);
        imageNfc.setBackgroundDrawable(waveDrawable);
        imageBackground.setBackgroundDrawable(waveBackground);
        waveDrawable.setWaveInterpolator(new AccelerateDecelerateInterpolator());
        waveBackground.setWaveInterpolator(new AnticipateOvershootInterpolator());
        waveDrawable.startAnimation();
        waveBackground.startAnimation();
        preference = new Preference(this);
        network = new NetworkManager(this, NetworkManager.KEY_TRANSACTION);

        cartModels = cartObjects;

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            finish();
            Toast.makeText(this, "Your device does not have NFC.", Toast.LENGTH_SHORT).show();
        } else if (nfcAdapter.isEnabled()) {
            cardReader = new CardReader(this);
            Toast.makeText(this, "Waiting for connectivity.", Toast.LENGTH_SHORT).show();
            enableReaderMode();
        } else {
            Toast.makeText(this, "Please turn on NFC.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_nfc:
                finish();
                Toast.makeText(this, "Connectivity cancelled.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void enableReaderMode() {
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, cardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }
    }

    @Override
    public void onAccountReceived(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<String> transferredData = Arrays.asList(data.split("_;_"));
                if (Boolean.parseBoolean(transferredData.get(2))) {
                    doTransaction(Integer.parseInt(transferredData.get(0)), transferredData.get(1));
                }
            }
        });
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

        network.doTransaction(cartModels, preference.getPreferenceInteger(Preference.KEY_VENDOR_ID), userId);
    }

    @Override
    public void onTransaction(double credits) {
        String message = credits + "_;_" + codifyProducts() + "_;_" + preference.getPreferenceString(Preference.KEY_VENDOR_SHOP);
        preference.setPreference(Preference.KEY_NFC_DATA, message);
        getPackageManager().setComponentEnabledSetting(new ComponentName(getApplicationContext(), CardService.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
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
