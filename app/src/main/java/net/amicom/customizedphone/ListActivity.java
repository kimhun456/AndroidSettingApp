package net.amicom.customizedphone;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.listener.dismiss.DefaultDismissableManager;


public class ListActivity extends Activity {


    public static final String intentkey = "locatonSetting";
    Button addButton;
    Cursor mCursor;
    MySQLiteHandler handler;
    ArrayList<DataForm> datalist;
    GPStracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);


        DBtoList();
        showList();
        stopService(new Intent(this, MyService.class));
        startService(new Intent(this, MyService.class));

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationSettingActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }


    private void DBtoList() {
        datalist = new ArrayList<DataForm>();
        handler = new MySQLiteHandler(ListActivity.this);
        mCursor = null;
        mCursor = handler.select();

        while (mCursor.moveToNext()) {
            DataForm df = new DataForm(mCursor);
            datalist.add(df);
        }

        mCursor.close();
    }

    private void showList() {

        ArrayList<Card> cards = new ArrayList<Card>();

        for (final DataForm df : datalist) {

            Card card = new Card(ListActivity.this);

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {

                    Toast.makeText(ListActivity.this,
                            " 주소  = " + df.getLocation_Address_Name(), Toast.LENGTH_SHORT).show();

                }
            });


            CardHeader header = new CardHeader(ListActivity.this);
            header.setTitle(df.getLocation_Name());
            card.addCardHeader(header);
            card.setTitle(getContent(df.getWifi_Checking(), df.getSound_Checking()));
            card.setSwipeable(true);
            card.setId("" + df.getPrimary_Key());
            cards.add(card);
            card.setOnSwipeListener(new Card.OnSwipeListener() {
                @Override
                public void onSwipe(Card card) {
                    gps = new GPStracker(ListActivity.this);
                    datalist.remove(df);
                    handler.delete(df);

                    Intent proxintent = new Intent(intentkey);
                    proxintent.putExtra("wifiChecking", df.getWifi_Checking());
                    proxintent.putExtra("soundChecking", df.getSound_Checking());
                    proxintent.putExtra("name", df.getLocation_Name());

                    // 팬딩인텐트를 등록시킨다.
                    PendingIntent intent = PendingIntent.getBroadcast(ListActivity.this, df.getPrimary_Key(), proxintent,
                            PendingIntent.FLAG_CANCEL_CURRENT);

                    // 근접경보를 제거한다.
                    gps.locationManager.removeProximityAlert(intent);
                    stopService(new Intent(ListActivity.this, MyService.class));
                    startService(new Intent(ListActivity.this, MyService.class));
                    Toast.makeText(ListActivity.this, df.getLocation_Name() + " 설정이 제거되었습니다.", Toast.LENGTH_SHORT).show();

                }
            });


        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(ListActivity.this, cards);
        mCardArrayAdapter.setDismissable(new LEFTDismissableManager());


        CardListView listView = (CardListView) ListActivity.this.findViewById(R.id.myList);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    public String getContent(int wifiChecking, int soundChecking) {

        StringBuffer result = new StringBuffer();

        if (soundChecking == 0) {

            result.append("Sound Mode ");
        } else if (soundChecking == 1) {

            result.append("Vibration Mode ");

        } else if (soundChecking == 2) {

            result.append("Silent Mode ");

        } else {
        }

        if (wifiChecking == 1) {
            result.append("	WIFI ON");

        } else {
            result.append("	WIFI OFF");

        }

        return result.toString();
    }

    public class LEFTDismissableManager extends DefaultDismissableManager {

        @Override
        public SwipeDirection getSwipeDirectionAllowed() {
            return SwipeDirection.LEFT;
        }
    }


}
