package com.ariondan.vendor.network;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ariondan.vendor.model.CartModel;
import com.ariondan.vendor.model.LoginModel;
import com.ariondan.vendor.model.TransactionModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
  Created by AoD Akitektuo on 03-Sep-17 at 19:45.
 */

/**
 * If you have a lot of errors make sure you added the right dependencies in gradle
 */

public class NetworkManager {

    public static final int KEY_USER = 0;
    public static final int KEY_PRODUCT = 1;
    public static final int KEY_TRANSACTION = 2;
    private static final String HOST = "http://festpay.azurewebsites.net/api/";
    private static final String HOST_USER = HOST + "user/";
    private static final String HOST_PRODUCT = HOST + "product/";
    private Context context;
    private UserResponse userResponse;
    private ProductResponse productResponse;
    private TransactionResponse transactionResponse;
    private RequestQueue queue;

    public NetworkManager(Activity activity, int type) {
        setContext(activity);
        switch (type) {
            case 0:
                setUserResponse((UserResponse) activity);
                break;
            case 1:
                setProductResponse((ProductResponse) activity);
                break;
            case 2:
                setTransactionResponse((TransactionResponse) activity);
                break;
        }
        queue = Volley.newRequestQueue(getContext());
    }

    public void logIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Fill in all fields.", Toast.LENGTH_SHORT).show();
        } else {
            if (email.contains("@")) {
                String url = HOST_USER + "logIn";
                try {
                    final String json = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(new LoginModel(email, password));
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (Boolean.parseBoolean(response)) {
                                getUserResponse().logIn(1, "Pancake House");
                            } else {
                                Toast.makeText(getContext(), "Wrong credentials.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            sendErrorNetworkToUser();
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
            } else {
                Toast.makeText(getContext(), "Invalid e-mail.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void passwordForgotten(String email) {
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Field was empty.", Toast.LENGTH_SHORT).show();
        } else {
            if (email.contains("@")) {
                String url = HOST_USER + "passwordForgotten?email=" + email;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (Boolean.parseBoolean(response)) {
                                    getUserResponse().passwordForgotten();
                                } else {
                                    Toast.makeText(getContext(), "Wrong e-mail.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendErrorNetworkToUser();
                    }
                });
                queue.add(stringRequest);
            } else {
                Toast.makeText(getContext(), "Invalid e-mail.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getProducts(String vendor) {
        String url = HOST_PRODUCT + "vendor?vendor=" + vendor.replaceAll(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getProductResponse().loadProducts(new ParserJSON(getContext()).convertJSONtoObject(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendErrorNetworkToUser();
            }
        });
        queue.add(stringRequest);
    }

    public void getAllProducts(String vendor) {
        String url = HOST_PRODUCT + "vendor?vendor=" + vendor.replaceAll(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getProductResponse().loadAllProducts(new ParserJSON(getContext()).convertJSONtoObject(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendErrorNetworkToUser();
            }
        });
        queue.add(stringRequest);
    }

    public void getProducts(String vendor, String search) {
        String url = HOST_PRODUCT + "search?vendor=" + vendor.replaceAll(" ", "%20") + "&search=" + search.replaceAll(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getProductResponse().loadProducts(new ParserJSON(getContext()).convertJSONtoObject(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendErrorNetworkToUser();
            }
        });
        queue.add(stringRequest);
    }

    public void getProducts(String vendor, String search, String category) {
        String url = HOST_PRODUCT + "category?vendor=" + vendor.replaceAll(" ", "%20") + "&search="
                + search.replaceAll(" ", "%20") + "&category=" + category.replaceAll(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getProductResponse().loadProducts(new ParserJSON(getContext()).convertJSONtoObject(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendErrorNetworkToUser();
            }
        });
        queue.add(stringRequest);
    }

    public void doTransaction(List<CartModel> cartModels, int vendorId, int customerId) {
        String url = HOST_PRODUCT + "pay";
        List<Integer> productIds = new ArrayList<>();
        for (CartModel product : cartModels) {
            for (int i = 0; i < product.getQuantity(); i++) {
                productIds.add(product.getId());
            }
        }
        try {
            final String json = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(new TransactionModel(productIds, vendorId, customerId));
            System.out.println(json);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // TODO this will change with double
                    if (Boolean.parseBoolean(response)) {
                        getTransactionResponse().onTransaction(new Random().nextInt(9998) + 1);
                    } else {
                        Toast.makeText(getContext(), "Error occurred while making the transaction.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    sendErrorNetworkToUser();
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

    private void sendErrorNetworkToUser() {
        Toast.makeText(getContext(), "Please check your Wi-Fi connection.", Toast.LENGTH_SHORT).show();
    }

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private UserResponse getUserResponse() {
        return userResponse;
    }

    private void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    private ProductResponse getProductResponse() {
        return productResponse;
    }

    private void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    private TransactionResponse getTransactionResponse() {
        return transactionResponse;
    }

    private void setTransactionResponse(TransactionResponse transactionResponse) {
        this.transactionResponse = transactionResponse;
    }
}
