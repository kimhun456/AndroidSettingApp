package net.amicom.customizedphone;

import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SetMapActivity extends Activity {
    GoogleMap map;
    Geocoder gc;
    TextView txt;
    String address_name;
    Button map_ok_btn;
    Button map_cancel_btn;
    double lat;
    double lon;
    GPStracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingmap);

        gps = new GPStracker(SetMapActivity.this);
        gc = new Geocoder(this, Locale.KOREAN);
        txt = (TextView) findViewById(R.id.map_txt_l);
        map_ok_btn = (Button) findViewById(R.id.map_ok_btn_l);
        map_cancel_btn = (Button) findViewById(R.id.map_cancel_btn_l);

        map_ok_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent resultIntent = new Intent();
                resultIntent.putExtra("AddressName", address_name);
                resultIntent.putExtra("latitude", lat);
                resultIntent.putExtra("longitude", lon);
                setResult(-1, resultIntent);
                finish();

            }
        });

        // 맵 환경설정
        map = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map_map_l)).getMap();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL); // maptype is normal
        map.setMyLocationEnabled(true); // enable my location
        map.getUiSettings().setZoomControlsEnabled(false); // 줌컨트롤러 지우기
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(gps.getLatitude(), gps.getLongitude()), 16));

        map.setOnMapLongClickListener(new OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng loc) {
                // TODO Auto-generated method stub
                map.clear();
                addMarker("HERE", loc.latitude, loc.longitude);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        loc.latitude, loc.longitude), 18));
                Toast.makeText(SetMapActivity.this,
                        loc.latitude + "   " + loc.longitude + "  ",
                        Toast.LENGTH_SHORT).show();
                lat = loc.latitude;
                lon = loc.longitude;
                searchLocation(loc.latitude, loc.longitude);

            }
        });

    }

    private void addMarker(String title, double latitude, double longitude) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(latitude, longitude));
        marker.title(title);
        marker.draggable(true);
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        map.addMarker(marker);
    }

    private void searchLocation(double lat, double lon) {
        List<Address> addressList = null;

        try {
            addressList = gc.getFromLocation(lat, lon, 1);

            Address ad = addressList.get(0);

            address_name = ad.getAddressLine(0);

            txt.setText(address_name);

        } catch (Exception ex) {
            Toast.makeText(SetMapActivity.this, ex.toString(), Toast.LENGTH_SHORT)
                    .show();
        }

    }

}
