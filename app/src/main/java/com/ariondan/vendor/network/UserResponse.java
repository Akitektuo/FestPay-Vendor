package com.ariondan.vendor.network;

/**
 * Created by AoD Akitektuo on 03-Sep-17 at 20:06.
 */

public interface UserResponse {

    void logIn(int vendorId, String vendorName);

    void passwordForgotten();
}
