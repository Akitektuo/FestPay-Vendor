package com.ariondan.vendor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ariondan.vendor.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialEditText editEmail;
    private MaterialEditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (MaterialEditText) findViewById(R.id.edit_login_email);
        editPassword = (MaterialEditText) findViewById(R.id.edit_login_password);
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.text_forget_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
//                logIn();
                startActivity(new Intent(this, ProductsActivity.class));
                finish();
                break;
            case R.id.text_forget_password:
                requestForgottenPassword();
                break;
        }
    }

    private void logIn() {
        String email = editEmail.getText().toString(), password = editPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill in all fields.", Toast.LENGTH_SHORT).show();
        } else {
            if (email.contains("@")) {
                if (email.equals("admin@gmail.com") && password.equals("parola")) {
                    startActivity(new Intent(this, ProductsActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Wrong e-mail or password.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid e-mail.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestForgottenPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_forgotten_password, null);
        builder.setView(viewDialog);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = ((MaterialEditText) viewDialog.findViewById(R.id.edit_dialog_email)).getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Field was empty.", Toast.LENGTH_SHORT).show();
                } else {
                    if (email.contains("@")) {
                        if (email.equals("admin@gmail.com")) {
                            Toast.makeText(LoginActivity.this, "E-mail has been sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong e-mail.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid e-mail.", Toast.LENGTH_SHORT).show();
                    }
                }
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
