package com.ariondan.vendor.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.ariondan.vendor.R;

import java.util.ArrayList;
import java.util.List;

import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;
import me.alexrs.wavedrawable.WaveDrawable;

public class PayActivity extends AppCompatActivity implements View.OnClickListener, NfcAdapter.CreateNdefMessageCallback {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] responses;

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

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        intentFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)};
        responses = new String[][]{new String[]{Ndef.class.getName()}, new String[]{NdefFormatable.class.getName()}};
        if (nfcAdapter == null) {
            //Use other method to send data
            Toast.makeText(this, "Your phone does not have NFC.", Toast.LENGTH_SHORT).show();
        } else if (nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC turned on.", Toast.LENGTH_SHORT).show();
            nfcAdapter.setNdefPushMessageCallback(this, this);
            //do the transaction
        } else {
            Toast.makeText(this, "NFC turned off.", Toast.LENGTH_SHORT).show();
            //turn nfc on programmatically
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, responses);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_nfc:
                finish();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        SparseArray<String> res = new NfcReadUtilityImpl().readFromTagWithSparseArray(intent);
        for (int i = 0; i < res.size(); i++) {
            System.out.println("Intent with Sparse Array");
            System.out.println(res.get(i));
        }

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            List<Parcelable> rawMessages = intent.getParcelableArrayListExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                List<NdefMessage> messages = new ArrayList<>();
                for (Parcelable rawMessage : rawMessages) {
                    messages.add((NdefMessage) rawMessage);
                }
                for (NdefMessage message : messages) {
                    System.out.println("Intent with Sparse Array");
                    System.out.println(message.toString());
                    System.out.println(new String(message.getRecords()[0].getPayload()));
                }
            }
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String text = "Beam me up, Android! Beam Time: " + System.currentTimeMillis();
        return new NdefMessage(new NdefRecord[]{
                NdefRecord.createMime("text/plain", text.getBytes())
        });
    }
}
