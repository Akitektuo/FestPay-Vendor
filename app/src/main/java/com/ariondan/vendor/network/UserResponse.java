package com.ariondan.vendor.network;

import android.graphics.Bitmap;

/**
 * Created by AoD Akitektuo on 03-Sep-17 at 20:06.
 */

public interface UserResponse {

    void logIn();

    void passwordForgotten();

    void loadImage(Bitmap bitmap);
}
