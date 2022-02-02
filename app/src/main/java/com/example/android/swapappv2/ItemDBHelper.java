package com.example.android.swapappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemDBHelper extends SQLiteOpenHelper {

    public ItemDBHelper(Context context) { super(context, "Items.db", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table items(itemName Text, itemDescription Text, itemID Text, userID Text, soldStatus Integer, itemImage Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists items");
    }

    // Returns "true" if the item was posted successfully, "false" if not
    public boolean insertData(String itemName, String itemDescription, String itemID, String userID, int soldStatus, String itemImage) { // Uri (?) itemImage
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("itemName", itemName);
        contentValues.put("itemDescription", itemDescription);
        contentValues.put("itemID", itemID);
        contentValues.put("userID", userID);
        contentValues.put("soldStatus", soldStatus);
        contentValues.put("itemImage", itemImage);

        long result = sqLiteDatabase.insert("items", null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    public String getImage(String itemID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from items where itemID = ?", new String[]{itemID});
        cursor.moveToFirst();
        return cursor.getString(5);

    }


    public String getOwnerID(String itemID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from items where itemID = ?", new String[]{itemID});
        cursor.moveToFirst();
        return cursor.getString(3);

    }

    public String getItemName(String itemID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from items where itemID = ?", new String[]{itemID});
        cursor.moveToFirst();
        return cursor.getString(0);

    }

    public String getItemDescription(String itemID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from items where itemID = ?", new String[]{itemID});
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public List<String> getAllItemIDs() throws SQLException {
        List<String> allItemIDs = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from items where soldStatus = ?", new String[]{"0"});
        while (cursor.moveToNext())
            allItemIDs.add(cursor.getString(2));

        return allItemIDs;
    }

    public List<String> getUserItemIDs(String userID) throws SQLException {
        List<String> userItemIDs = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from items where soldStatus = ? and userID = ?", new String[]{"0", userID});
        while (cursor.moveToNext())
            userItemIDs.add(cursor.getString(2));

        return userItemIDs;
    }

    public List<String> getUserItems(String userID) throws SQLException {
        List<String> userItems = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from items where soldStatus = ? and userID = ?", new String[]{"0", userID});
        while (cursor.moveToNext())
            userItems.add(cursor.getString(0));

        return userItems;
    }

    public void setSoldStatus(String itemID, int soldStatus) throws SQLException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("soldStatus", soldStatus);
        sqLiteDatabase.update("items", contentValues, "itemID = ?", new String[]{itemID});
    }
}