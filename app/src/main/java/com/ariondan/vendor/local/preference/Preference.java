package com.ariondan.vendor.local.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AoD Akitektuo on 22-Sep-17 at 19:48.
 */

public class Preference {

    public static final String KEY_CREATED = "created";
    public static final String KEY_VENDOR_ID = "vendorId";
    public static final String KEY_VENDOR_NAME = "vendorName";
    public static final String KEY_VENDOR_SHOP = "vendorShop";
    public static final String KEY_NFC_DATA = "nfcData";
    private static final String KEY_INITIALIZE = "initialize";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Preference(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(KEY_INITIALIZE, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    private void savePreference() {
        editor.commit();
    }

    public void setPreference(String key, String str) {
        editor.putString(key, str);
        savePreference();
    }

    public void setPreference(String key, int num) {
        editor.putInt(key, num);
        savePreference();
    }

    public void setPreference(String key, boolean bool) {
        editor.putBoolean(key, bool);
        savePreference();
    }

    public String getPreferenceString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public Integer getPreferenceInteger(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public boolean getPreferenceBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

}
