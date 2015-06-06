package net.amicom.customizedphone;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardGridView;


public class ListActivity extends ActionBarActivity {


    Button addButton;
    Cursor mCursor;
    MySQLiteHandler handler;
    ArrayList<DataForm> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        DBtoList();
        showList();
        Intent myIntent = new Intent(getApplicationContext(),
                MyService.class);
        startService(myIntent);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationSettingActivity.class);
                startActivity(intent);
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
                            "bytes = " + df.getLocation_Address_Name() + "last-modified " + df.getLocation_Name(), Toast.LENGTH_SHORT).show();

                }
            });


            CardHeader header = new CardHeader(ListActivity.this);
            header.setTitle(df.getLocation_Name());
            card.addCardHeader(header);
            card.setTitle(df.getLocation_Address_Name());
            cards.add(card);


        }

        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(ListActivity.this, cards);

        CardGridView gridView = (CardGridView) ListActivity.this.findViewById(R.id.myGrid);
        if (gridView != null) {
            gridView.setAdapter(mCardArrayAdapter);
        }
    }


}
