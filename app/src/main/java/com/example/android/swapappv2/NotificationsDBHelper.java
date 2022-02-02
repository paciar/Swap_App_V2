package com.example.android.swapappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationsDBHelper extends SQLiteOpenHelper {

    public NotificationsDBHelper(Context context) {
        super(context, "Notifications.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table notifications(notificationID Text, receivingItemID Text, receivingUserID Text, givingItemID Text, givingUserID Text, tradeStatus Integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists notifications");
    }

    // returns "true" if notification has been sent, "false" if not
    public boolean insertData(String notificationID, String receivingItemID, String receivingUserID, String givingItemID, String givingUserID, int tradeStatus) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("notificationID", notificationID);
        contentValues.put("receivingItemID", receivingItemID);
        contentValues.put("receivingUserID", receivingUserID);
        contentValues.put("givingItemID", givingItemID);
        contentValues.put("givingUserID", givingUserID);
        contentValues.put("tradeStatus", tradeStatus);

        long result = sqLiteDatabase.insert("notifications", null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }
    // shows the trades in which ur being asked for
    public List<String> getNotifications(String userID) throws SQLException {
        List<String> notifications = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from notifications where tradeStatus = ? and receivingUserID = ?", new String[]{"0", userID});
        while (cursor.moveToNext())
            notifications.add(cursor.getString(0)); // returns notification id

        return notifications;
    }
    // shows the trades in which ur giving something, will show awaiting response, accepted, or denied
    public List<String> getSentTrades(String userID) throws SQLException {
        List<String> sentTrades = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from notifications where givingUserID = ?", new String[]{userID});
        while (cursor.moveToNext())
            sentTrades.add(cursor.getString(0));
        return sentTrades;
    }

    public String getReceivingItemID(String notificationID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from notifications where notificationID = ?", new String[]{notificationID});
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    public String getReceivingUserID(String notificationID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from notifications where notificationID = ?", new String[]{notificationID});
        cursor.moveToFirst();
        return cursor.getString(2);
    }

    public String getGivingItemID(String notificationID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from notifications where notificationID = ?", new String[]{notificationID});
        cursor.moveToFirst();
        return cursor.getString(3);
    }

    public String getGivingUserID(String notificationID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from notifications where notificationID = ?", new String[]{notificationID});
        cursor.moveToFirst();
        return cursor.getString(4);
    }

    public int getTradeStatus(String notificationID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from notifications where notificationID = ?", new String[]{notificationID});
        cursor.moveToFirst();
        return cursor.getInt(5);
    }

    public void setTradeStatus(String notificationID, int tradeStatus) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("tradeStatus", tradeStatus);
        sqLiteDatabase.update("notifications", contentValues, "notificationID = ?", new String[]{notificationID});
    }
}
