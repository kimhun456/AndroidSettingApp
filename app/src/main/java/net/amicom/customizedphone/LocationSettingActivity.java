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

                //placeTxt.append(" " + Data.getExtras().getDouble("latitude"));
                //placeTxt.append(" " + Data.getExtras().getDouble("longitude"));

            }
            else{
                placeTxt.setText("값이 들어오지 않았습니다.");
            }
        }

    }

}
