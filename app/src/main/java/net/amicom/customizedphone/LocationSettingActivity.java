package net.amicom.customizedphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LocationSettingActivity extends Activity {

    MySQLiteHandler handler;
    static final int MAP_CODE = 1001;
    EditText nameEdit;
    Button curLocBtn;
    Button locBtn;
    ToggleButton wifiBtn;
    RadioButton soundBtn;
    RadioButton vibrationBtn;
    RadioButton silentBtn;
    TextView placeTxt;

    Button setBtn;
    Button backBtn;
    GPStracker gps;

    double latitude;
    double longitude;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_setting);
        gps = new GPStracker(LocationSettingActivity.this);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        curLocBtn = (Button) findViewById(R.id.curplcBtn);
        locBtn = (Button) findViewById(R.id.plcBtn);
        wifiBtn = (ToggleButton) findViewById(R.id.wifiToggle);
        soundBtn = (RadioButton) findViewById(R.id.soundRadio);
        vibrationBtn = (RadioButton) findViewById(R.id.vibrationRadio);
        silentBtn = (RadioButton) findViewById(R.id.silentRadio);

        placeTxt = (TextView) findViewById(R.id.placeName);

        setBtn = (Button) findViewById(R.id.setbtn);
        backBtn = (Button) findViewById(R.id.backbtn);

        curLocBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (gps.canGetLocation()) {
                    gps.getLatitude();
                    gps.getLongitude();
                    Toast.makeText(LocationSettingActivity.this,
                            gps.getLatitude() + " " + gps.getLongitude() + "",
                            Toast.LENGTH_SHORT).show();
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                } else {
                    gps.showSettingsAlert();
                }
            }
        });

        locBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (gps.canGetLocation()) {
                    Intent intent = new Intent(LocationSettingActivity.this, SetMapActivity.class);
                    startActivityForResult(intent, MAP_CODE);
                } else {
                    gps.showSettingsAlert();
                }

            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DataForm df = new DataForm();
                df.setTime_Location_Checking(1);
                df.setOn_Off_Selecting(1);
                df.setTime_Location_Checking(0);
                df.setLocation_Address_Name(placeTxt.getText().toString());
                df.setLocation_Name(nameEdit.getText().toString());
                df.setLatitude(latitude + "");
                df.setLongitude(longitude + "");
                df.setDiameter("10");
                if (wifiBtn.isChecked()) {
                    df.setWifi_Checking(1);
                } else {
                    df.setWifi_Checking(0);
                }

                if (soundBtn.isChecked()) {
                    df.setSound_Checking(0);
                } else if (vibrationBtn.isChecked()) {
                    df.setSound_Checking(1);
                } else {
                    df.setSound_Checking(2);
                }

                handler = new MySQLiteHandler(LocationSettingActivity.this);
                handler.insert(df);
                Intent intent = new Intent(getApplicationContext(),
                        ListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {

        super.onActivityResult(requestCode, resultCode, Data);

        if (requestCode == MAP_CODE) {

            if (resultCode == RESULT_OK) {
                String name = Data.getExtras().getString("AddressName");

                placeTxt.setText(name);

                latitude = Data.getExtras().getDouble("latitude");
                longitude = Data.getExtras().getDouble("longitude");


            } else {
                placeTxt.setText("값이 들어오지 않았습니다.");
            }
        }

    }

}
