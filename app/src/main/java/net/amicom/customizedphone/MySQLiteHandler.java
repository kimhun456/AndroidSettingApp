package net.amicom.customizedphone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MySQLiteHandler {

    MySQLiteOpenHelper helper;
    SQLiteDatabase db;

    public MySQLiteHandler(Context context) {
        helper = new MySQLiteOpenHelper(context, "AutoSettingData", null, 1);
    }

    public static MySQLiteHandler open(Context context) {
        return new MySQLiteHandler(context);
    }

    public void close() {
        helper.close();
    }

    public void insert(DataForm df) {

        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("time_Loacation_Checking", df.getTime_Location_Checking());
        values.put("start_Time", df.getStart_Time());
        values.put("end_Time", df.getEnd_Time());
        values.put("day_Sum", df.getDay_Sum());
        values.put("location_Name", df.getLocation_Name());
        values.put("location_Address_Name", df.getLocation_Address_Name());
        values.put("longitude", df.getLongitude());
        values.put("latitude", df.getLatitude());
        values.put("diameter", df.getDiameter());
        values.put("wifi_Checking", df.getWifi_Checking());
        values.put("sound_Checking", df.getSound_Checking());
        values.put("on_Off_Selecting", df.getOn_Off_Selecting());


        db.insert("AutoSettingData", null, values);
    }

    public void update(DataForm df) {

        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("primary_Key", df.getPrimary_Key());
        db.update("AutoSettingData", values, "primary_Key = ?", new String[]{df.getTime_Location_Checking() + ""});
    }

    public void delete(DataForm df) {
        db = helper.getWritableDatabase();
        db.delete("AutoSettingData", "primary_Key = ? ", new String[]{df.getTime_Location_Checking() + ""});
    }

    public Cursor select() {
        db = helper.getReadableDatabase();
        Cursor c = db.query("AutoSettingData", null, null, null, null, null, null);
        return c;
    }

}






