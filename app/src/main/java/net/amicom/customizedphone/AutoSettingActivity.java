package net.amicom.customizedphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AutoSettingActivity extends Activity {

    MySQLiteHandler dataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_setting);

        dataHandler = new MySQLiteHandler(getApplicationContext());

        Button locActivity = (Button) findViewById(R.id.button1);

        locActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(),
                        LocationSettingActivity.class);
                startActivity(intent);

            }
        });

        Button setMap = (Button) findViewById(R.id.button2);
        setMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent myIntent = new Intent(getApplicationContext(),
                        MyService.class);
                startService(myIntent);

            }
        });
    }
}
