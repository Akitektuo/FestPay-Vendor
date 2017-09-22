package com.ariondan.vendor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.ariondan.vendor.R;
import com.ariondan.vendor.network.NetworkManager;
import com.ariondan.vendor.network.UserResponse;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.ariondan.vendor.network.NetworkManager.KEY_USER;
import static com.ariondan.vendor.util.ObjectPasser.id;
import static com.ariondan.vendor.util.ObjectPasser.vendor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, UserResponse {

    private MaterialEditText editEmail;
    private MaterialEditText editPassword;
    private NetworkManager network;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (MaterialEditText) findViewById(R.id.edit_login_email);
        editPassword = (MaterialEditText) findViewById(R.id.edit_login_password);
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.text_forget_password).setOnClickListener(this);

        network = new NetworkManager(this, KEY_USER);

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
    public void logIn(int vendorId, String vendorName) {
        id = vendorId;
        vendor = vendorName;
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
