package net.amicom.customizedphone;

import android.database.Cursor;

public class DataForm {
    //
    private int primary_Key;

    // Distribute Time and Location
    private int time_Location_Checking;

    // Time Data
    private String start_Time;
    private String end_Time;
    private String day_Sum;

    // Location Data
    public String location_Name;
    private String location_Address_Name;
    private String longitude;
    private String latitude;
    private String diameter;

    // Setting Data
    private int wifi_Checking;
    private int sound_Checking;
    private int on_Off_Selecting;

    public DataForm() {

    }


    public DataForm(Cursor cu) {
        for (int i = 0; i < cu.getCount(); i++) {
            cu.moveToNext();
            primary_Key = cu.getInt(0);
            time_Location_Checking = cu.getInt(1);
            start_Time = cu.getString(2);
            end_Time = cu.getString(3);
            day_Sum = cu.getString(4);
            location_Name = cu.getString(5);
            location_Address_Name = cu.getString(6);
            longitude = cu.getString(7);
            latitude = cu.getString(8);
            diameter = cu.getString(9);
            wifi_Checking = cu.getInt(10);
            sound_Checking = cu.getInt(11);
            on_Off_Selecting = cu.getInt(12);

        }
    }

    public int getPrimary_Key() {
        return primary_Key;
    }

    public void setPrimary_Key(int primary_Key) {
        this.primary_Key = primary_Key;
    }

    public int getTime_Location_Checking() {
        return time_Location_Checking;
    }

    public void setTime_Location_Checking(int time_Location_Checking) {
        this.time_Location_Checking = time_Location_Checking;
    }

    public String getStart_Time() {
        return start_Time;
    }

    public void setStart_Time(String start_Time) {
        this.start_Time = start_Time;
    }

    public String getEnd_Time() {
        return end_Time;
    }

    public void setEnd_Time(String end_Time) {
        this.end_Time = end_Time;
    }

    public String getDay_Sum() {
        return day_Sum;
    }

    public void setDay_Sum(String day_Sum) {
        this.day_Sum = day_Sum;
    }

    public String getLocation_Name() {
        return location_Name;
    }

    public void setLocation_Name(String location_Name) {
        this.location_Name = location_Name;
    }

    public String getLocation_Address_Name() {
        return location_Address_Name;
    }

    public void setLocation_Address_Name(String location_Address_Name) {
        this.location_Address_Name = location_Address_Name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public int getWifi_Checking() {
        return wifi_Checking;
    }

    public void setWifi_Checking(int wifi_Checking) {
        this.wifi_Checking = wifi_Checking;
    }

    public int getSound_Checking() {
        return sound_Checking;
    }

    public void setSound_Checking(int sound_Checking) {
        this.sound_Checking = sound_Checking;
    }

    public int getOn_Off_Selecting() {
        return on_Off_Selecting;
    }

    public void setOn_Off_Selecting(int on_Off_Selecting) {
        this.on_Off_Selecting = on_Off_Selecting;
    }
}
