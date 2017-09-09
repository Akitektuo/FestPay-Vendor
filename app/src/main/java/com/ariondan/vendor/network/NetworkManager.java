package com.ariondan.vendor.network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ariondan.vendor.model.LoginModel;
import com.ariondan.vendor.model.ProductModel;
import com.ariondan.vendor.model.ProductNetworkModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/*
  Created by AoD Akitektuo on 03-Sep-17 at 19:45.
 */

/**
 * If you have a lot of errors make sure you added the right dependency in the gradle file:
 * compile 'com.android.volley:volley:1.0.0'
 * compile (
 * [group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.4.1']
 * )
 */

public class NetworkManager {

    public static final int KEY_USER = 0;
    public static final int KEY_PRODUCT = 1;
    private static final String HOST = "http://festpay.azurewebsites.net/api/";
    private static final String HOST_USER = HOST + "user/";
    private static final String HOST_PRODUCT = HOST + "product/";
    private Context context;
    private UserResponse userResponse;
    private ProductResponse productResponse;
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
                    System.out.println(json);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (Boolean.parseBoolean(response)) {
                                getUserResponse().logIn();
                            } else {
                                Toast.makeText(getContext(), "Wrong credentials.", Toast.LENGTH_SHORT).show();
                            }
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
                    }
                });
                queue.add(stringRequest);
            } else {
                Toast.makeText(getContext(), "Invalid e-mail.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getProducts(String vendor) {
        String url = HOST_PRODUCT + "products"; //?vendor=" + vendor.replaceAll(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    List<ProductNetworkModel> productNetworkModels = new ObjectMapper().reader().readValue(response);
                    System.out.println(response);
                    System.out.println(new ObjectMapper().reader().readValue(response));
                    getProductResponse().loadProducts(ProductModel.convertProducts(productNetworkModels));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public void getProducts(String vendor, String search) {
        String url = HOST_PRODUCT + "products?vendor=" + vendor + "&search=" + search;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    List<ProductNetworkModel> productNetworkModels = new ObjectMapper().reader().readValue(response);
                    getProductResponse().loadProducts(ProductModel.convertProducts(productNetworkModels));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public void getProducts(String vendor, String search, String category) {
        String url = HOST_PRODUCT + "products?vendor=" + vendor + "&search=" + search + "&category=" + category;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    List<ProductNetworkModel> productNetworkModels = new ObjectMapper().reader().readValue(response);
                    getProductResponse().loadProducts(ProductModel.convertProducts(productNetworkModels));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public void loadImageTest() {
        String url = "http://img.webmd.com/dtmcms/live/webmd/consumer_assets/site_images/articles/health_tools/taking_care_of_kitten_slideshow/photolibrary_rm_photo_of_kitten_in_grass.jpg";
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                getUserResponse().loadImage(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
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
}
