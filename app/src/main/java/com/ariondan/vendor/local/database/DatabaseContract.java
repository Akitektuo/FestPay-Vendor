package com.ariondan.vendor.local.database;

import android.provider.BaseColumns;

/**
 * Created by Akitektuo on 22.07.2017.
 */

public class DatabaseContract {

    public static final int CURSOR_ID = 0;
    public static final int CURSOR_PRODUCT = 1;
    public static final int CURSOR_PRICE = 2;
    public static final int CURSOR_QUANTITY = 3;
    public static final int CURSOR_TOTAL_PRICE = 4;
    public static final int CURSOR_CUSTOMER = 5;
    public static final int CURSOR_TIME = 6;

    abstract class HistoryContractEntry implements BaseColumns {
        static final String TABLE_NAME = "History";
        static final String COLUMN_ID = "Id";
        static final String COLUMN_PRODUCT = "Product";
        static final String COLUMN_PRICE = "Price";
        static final String COLUMN_QUANTITY = "Quantity";
        static final String COLUMN_TOTAL_PRICE = "TotalPrice";
        static final String COLUMN_CUSTOMER = "Customer";
        static final String COLUMN_TIME = "Time";
    }
}
