package com.example.android.swapappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper {

    public UserDBHelper(Context context) { super(context, "Users.db", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table users(username Text primary key, password Text, email Text," +
                "phone Text, adminStatus Integer, userID Text, profilePicture Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists users");
    }

    // Returns "true" if user was added successfully, "false" if not
    public boolean insertData(String username, String password, String email, String phone, int adminStatus, String userID, String profilePicture) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        contentValues.put("adminStatus", adminStatus);
        contentValues.put("userID", userID);
        contentValues.put("profilePicture", profilePicture);

        long result = sqLiteDatabase.insert("users", null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    // Returns "true" if user exists in the DB, "false" if not
    public boolean checkUsername(String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // Returns "true" if the user logs in successfully, "false" if the user fails to log in
    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username = ? and password = ?",
                new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // Returns "true" if the user is an admin, "false" if the user isn't an admin
    public boolean checkAdminStatus(String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username = ? and admin = ?",
                new String[]{username, "1"});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    /************************ GETTER METHODS *************************/
    public String getUserID(String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username = ?", new String[]{username});
        cursor.moveToFirst();
        return cursor.getString(5);

    }

    public String getName(String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where userID = ?", new String[]{userID});
        cursor.moveToFirst();
        return cursor.getString(0); // Searches "users" table with userID and returns their username

    }

    public String getPassword(String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where userID = ?", new String[]{userID});
        cursor.moveToFirst();
        return cursor.getString(1);

    }

    public String getUserEmail(String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where userID = ?", new String[]{userID});
        cursor.moveToFirst();
        return cursor.getString(2);
    }

    public String getUserPhone(String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where userID = ?", new String[]{userID});
        cursor.moveToFirst();
        return cursor.getString(3);
    }

    public String getUserProfilePicture(String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where userID = ?", new String[]{userID});
        cursor.moveToFirst();
        return cursor.getString(6);
    }
    /************************ GETTER METHODS *************************/

    /************************ SETTER METHODS *************************/
    public void setNewUsername(String newUsername, String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", newUsername);
        sqLiteDatabase.update("users", contentValues, "userID = ?", new String[]{userID});
    }

    public void setNewPassword(String newPassword, String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        sqLiteDatabase.update("users", contentValues, "userID = ?", new String[]{userID});
    }

    public void setNewEmail(String newEmail, String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("email", newEmail);
        sqLiteDatabase.update("users", contentValues, "userID = ?", new String[]{userID});
    }

    public void setNewPhoneNumber(String newPhoneNumber, String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", newPhoneNumber);
        sqLiteDatabase.update("users", contentValues, "userID = ?", new String[]{userID});
    }

    public void setNewProfilePicture(String newProfilePicture, String userID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("profilePicture", newProfilePicture);
        sqLiteDatabase.update("users", contentValues, "userID = ?", new String[]{userID});
    }
    /************************ SETTER METHODS *************************/
}
