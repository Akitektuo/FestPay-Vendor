package com.ariondan.vendor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ariondan.vendor.model.HistoryModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ariondan.vendor.database.DatabaseContract.CURSOR_CUSTOMER;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_ID;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_PICTURE;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_PRICE;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_PRODUCT;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_QUANTITY;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_TIME;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_TOTAL_PRICE;

/**
 * Created by Akitektuo on 22.07.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_QUERY = "CREATE TABLE " + DatabaseContract.HistoryContractEntry.TABLE_NAME + " (" +
            DatabaseContract.HistoryContractEntry.COLUMN_ID + " NUMBER," +
            DatabaseContract.HistoryContractEntry.COLUMN_PICTURE + " TEXT," +
            DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT + " TEXT," +
            DatabaseContract.HistoryContractEntry.COLUMN_PRICE + "TEXT," +
            DatabaseContract.HistoryContractEntry.COLUMN_QUANTITY + "NUMBER," +
            DatabaseContract.HistoryContractEntry.COLUMN_TOTAL_PRICE + "TEXT," +
            DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER + " TEXT," +
            DatabaseContract.HistoryContractEntry.COLUMN_TIME + "TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addHistory(String picture, String product, double price, int quantity, double totalPrice, String customer, Date time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_ID, getGeneratedId());
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_PICTURE, picture);
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT, product);
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_PRICE, String.valueOf(price));
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_TOTAL_PRICE, String.valueOf(totalPrice));
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER, customer);
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_TIME, new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss").format(time));
        getWritableDatabase().insert(DatabaseContract.HistoryContractEntry.TABLE_NAME, null, contentValues);
    }

    public List<HistoryModel> getHistory() {
        String[] list = {DatabaseContract.HistoryContractEntry.COLUMN_ID,
                DatabaseContract.HistoryContractEntry.COLUMN_PICTURE,
                DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT,
                DatabaseContract.HistoryContractEntry.COLUMN_PRICE,
                DatabaseContract.HistoryContractEntry.COLUMN_QUANTITY,
                DatabaseContract.HistoryContractEntry.COLUMN_TOTAL_PRICE,
                DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER,
                DatabaseContract.HistoryContractEntry.COLUMN_TIME};
        try {
            return getModelForCursor(getReadableDatabase().query(DatabaseContract.HistoryContractEntry.TABLE_NAME, list, null, null, null, null, null));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getGeneratedId() {
        return getHistory().size();
    }

    public List<HistoryModel> getHistoryForSearch(String searchField) {
        String[] results = {DatabaseContract.HistoryContractEntry.COLUMN_ID,
                DatabaseContract.HistoryContractEntry.COLUMN_PICTURE,
                DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT,
                DatabaseContract.HistoryContractEntry.COLUMN_PRICE,
                DatabaseContract.HistoryContractEntry.COLUMN_QUANTITY,
                DatabaseContract.HistoryContractEntry.COLUMN_TOTAL_PRICE,
                DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER,
                DatabaseContract.HistoryContractEntry.COLUMN_TIME};
        String selection = DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT + " LIKE ? OR " +
                DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER + " LIKE ?";
        String[] selectionArgs = {"%" + searchField + "%", "%" + searchField + "%"};
        try {
            return getModelForCursor(getReadableDatabase().query(DatabaseContract.HistoryContractEntry.TABLE_NAME, results, selection, selectionArgs, null, null, null));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<HistoryModel> getModelForCursor(Cursor cursor) throws ParseException {
        List<HistoryModel> historyModels = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                historyModels.add(new HistoryModel(cursor.getInt(CURSOR_ID),
                        cursor.getString(CURSOR_PICTURE),
                        cursor.getString(CURSOR_PRODUCT),
                        Double.parseDouble(cursor.getString(CURSOR_PRICE)),
                        cursor.getInt(CURSOR_QUANTITY),
                        Double.parseDouble(cursor.getString(CURSOR_TOTAL_PRICE)),
                        cursor.getString(CURSOR_CUSTOMER),
                        new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss").parse(cursor.getString(CURSOR_TIME))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyModels;
    }
}
