package com.ariondan.vendor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ariondan.vendor.R;
import com.ariondan.vendor.model.LoginModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialEditText editEmail;
    private MaterialEditText editPassword;
    private String emailResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (MaterialEditText) findViewById(R.id.edit_login_email);
        editPassword = (MaterialEditText) findViewById(R.id.edit_login_password);
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.text_forget_password).setOnClickListener(this);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
//                logIn();
                sendLoginRequest("admin@gmail.com", "parola");
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
                        sendRequest(email);
//                        if (email.equals("admin@gmail.com")) {
//                            Toast.makeText(LoginActivity.this, "E-mail has been sent.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Wrong e-mail.", Toast.LENGTH_SHORT).show();
//                        }
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

    private void sendRequest(String email) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://festpay.azurewebsites.net/api/user/passwordForgotten?email=" + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        emailResponse = response;
                        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    private void sendLoginRequest(String email, String password) {
        LoginModel login = new LoginModel(email, password);
        try {
            final String json = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(login);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://festpay.azurewebsites.net/api/user/logIn";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return json == null ? null : json.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                        return null;
                    }
                }
            };
            queue.add(stringRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
