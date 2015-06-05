package net.amicom.customizedphone;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context, String DatabaseName,
                              CursorFactory factory, int version) {
        super(context, DatabaseName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table datas ("
                + " primary_Key integer primary key autoincrement ,"
                + " time_Loacation_Checking integer , "
                + " start_Time text ,"
                + " end_Time text, "
                + " day_Sum text, "
                + " location_Name text , "
                + " location_Address_Name text , "
                + " longitude text, "
                + " latitude text , "
                + " diameter text, "
                + " wifi_Checking integer, "
                + " sound_Checking integer," + " on_Off_Selecting integer )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists datas");
        onCreate(db);
    }

}
