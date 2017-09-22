package com.ariondan.vendor.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.ariondan.vendor.R;
import com.ariondan.vendor.local.preference.Preference;
import com.ariondan.vendor.network.NetworkManager;
import com.ariondan.vendor.network.UserResponse;
import com.ariondan.vendor.nfc.CardService;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.ariondan.vendor.network.NetworkManager.KEY_USER;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, UserResponse {

    private MaterialEditText editEmail;
    private MaterialEditText editPassword;
    private NetworkManager network;
    private Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preference = new Preference(this);
        editEmail = (MaterialEditText) findViewById(R.id.edit_login_email);
        editPassword = (MaterialEditText) findViewById(R.id.edit_login_password);
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.text_forget_password).setOnClickListener(this);

        network = new NetworkManager(this, KEY_USER);

        getPackageManager().setComponentEnabledSetting(new ComponentName(this, CardService.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                network.logIn(editEmail.getText().toString(), editPassword.getText().toString());
                break;
            case R.id.text_forget_password:
                forgottenPasswordAlert();
                break;
        }
    }

    @Override
    public void logIn(int vendorId, String vendorShop) {
        preference.setPreference(Preference.KEY_VENDOR_ID, vendorId);
        preference.setPreference(Preference.KEY_VENDOR_SHOP, vendorShop);
        startActivity(new Intent(this, ProductsActivity.class));
        finish();

    }

    @Override
    public void passwordForgotten() {

    }

    private void forgottenPasswordAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_forgotten_password, null);
        builder.setView(viewDialog);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                network.passwordForgotten(((MaterialEditText) viewDialog.findViewById(R.id.edit_dialog_email)).getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        alertDialog.show();
    }

}
