package com.ariondan.vendor.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.ariondan.vendor.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import be.appfoundry.nfclibrary.exceptions.InsufficientCapacityException;
import be.appfoundry.nfclibrary.exceptions.ReadOnlyTagException;
import be.appfoundry.nfclibrary.exceptions.TagNotPresentException;
import be.appfoundry.nfclibrary.tasks.interfaces.AsyncOperationCallback;
import be.appfoundry.nfclibrary.utilities.interfaces.NfcWriteUtility;
import be.appfoundry.nfclibrary.utilities.sync.NfcReadUtilityImpl;
import me.alexrs.wavedrawable.WaveDrawable;

public class PayActivity extends AppCompatActivity implements View.OnClickListener, AsyncOperationCallback {

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
        String answer = "";
        for (int i = 0; i < res.size(); i++) {
            answer += res.valueAt(i);
            Toast.makeText(this, res.valueAt(i), Toast.LENGTH_SHORT).show();
        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        final View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_forgotten_password, null);
//        builder.setView(viewDialog);
//        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String email = ((MaterialEditText) viewDialog.findViewById(R.id.edit_dialog_email)).getText().toString();
//                if (email.isEmpty()) {
//                    Toast.makeText(PayActivity.this, "Field was empty.", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (email.contains("@")) {
//                        if (email.equals("admin@gmail.com")) {
//                            Toast.makeText(PayActivity.this, "E-mail has been sent.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(PayActivity.this, "Wrong e-mail.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(PayActivity.this, "Invalid e-mail.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
//                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
//            }
//        });
//        alertDialog.show();
    }

    @Override
    public boolean performWrite(NfcWriteUtility nfcWriteUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
        return nfcWriteUtility.writeTextToTagFromIntent("Testing, lol", getIntent());
    }
}
