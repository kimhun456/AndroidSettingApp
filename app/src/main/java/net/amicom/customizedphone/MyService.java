package net.amicom.customizedphone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class MyService extends Service implements Runnable {

    public static final String TAG = "MyService";
    public static final String intentkey = "locatonSetting";
    GPStracker gps;
    LocationManager locationManager;
    locationIntentReciver intentReciever;
    NotificationManager notificationManager;
    AudioManager audiomanager;
    WifiManager wifimanager;
    ArrayList<TimeForm> timeList;
    Date date;

    public void onCreate() {
        super.onCreate();

        // 타임 객체를 저장할 어레이리스트 생성
        timeList = new ArrayList<TimeForm>();

        // 오디오매니저와 와이파이 매니저를 생성하여 와이파이설정과 소리설정을 바꾼다.
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // 위치를 찾기 위해 GPStracker객체를 생성하여 찾는다.
        gps = new GPStracker(MyService.this);
        // 로케이션매니저를 이용하여 위치를 추적한다.
        locationManager = gps.locationManager;

        DataForm df = new DataForm();
        df.setDay_Sum("01110101");
        df.setOn_Off_Selecting(1);
        df.setTime_Location_Checking(0);
        df.setLocation_Address_Name("아주대 팔달관");
        df.setLocation_Name("here");
        df.setLatitude("37.284272");
        df.setLongitude("127.044629");
        df.setDiameter("10");




        df.setWifi_Checking(0);
        df.setSound_Checking(1);
        df.setStart_Time("07:56");
        df.setEnd_Time("07:57");

        registerDataForm(df, 0);

        intentReciever = new locationIntentReciver(intentkey);
        registerReceiver(intentReciever, intentReciever.getFilter());

        Thread myThread = new Thread(this);
        myThread.start();

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

        while (true) {
            try {
                //db


                Log.i(TAG, "Thread is running");
                // 60초마다 위치를 받아옴
                gps.getLocation();

                Log.i(TAG, gps.getLatitude() + "---" + gps.getLongitude());

                compareTime();

                // 스레드를 60초마다 실행
                Thread.sleep(1000 * 60);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }

    }

    // 어레이 리스트로 저장된 DataForm 객체를 등록한다.
    public void registerDataFormList(ArrayList<DataForm> dataList) {

        for (int i = 0; i < dataList.size(); i++) {

            registerDataForm(dataList.get(i), i);
        }

    }

    public void compareTime() {

        // 현재 시간을 받아옵니다.
        date = new Date(System.currentTimeMillis());
        // 데이터를 hh:mm으로 저장합니다.
        SimpleDateFormat CurTimeFormat = new SimpleDateFormat("HH:mm");
        // curTime 변수에 현재시간을 지정된 형식으로 저장합니다.
        String currentTime = CurTimeFormat.format(date);

        // 타임리스트에 타임폼 객체마다 검사를 실시합니다.
        for (int i = 0; i < timeList.size(); i++) {

            TimeForm time = timeList.get(i);

            // 현재시간과 설정된 시간이 같으면 함수를 호출합니다.
            if (time.getStartTime().equals(currentTime)) {

                //code service start


                // 현재 와이파이 상태와 벨소리 상태를 저장합니다.
                if (wifimanager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                    time.setCurrentWifiChecking(1);
                } else {
                    time.setCurrentWifiChecking(0);
                }
                // 벨소리상태를 저장합니다.
                if (audiomanager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                    time.setCurrentSoundChecking(0);
                } else if (audiomanager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                    time.setCurrentSoundChecking(1);
                } else if (audiomanager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                    time.setCurrentSoundChecking(2);
                } else {
                    Log.i(TAG, "error in setCurrent Mode");
                }

                // 모드를 원하는 상태로 바꾸고 result 스트링에 저장합니다.
                String result = setMode(time.getWifiChecking(),
                        time.getSoundChecking());

                // 상태창을 띄워주는 함수를 호출합니다.
                makeNotification(time.getName(), result);

                // startTime이 실행되었기 떄문에 endTime때 저장된 와이파이상태와 벨소리상태를 가지고 올 수 있다는걸
                // 보여줍니다.
                time.setStartFlag(true);

            }

            // 현재 시간과 설정된 시간이 갖고 앞서 startTime때 설정이 되었다면 다시 호출됩니다.
            if (time.getEndTime().equals(currentTime) && time.isStartFlag()) {

                // 모드를 원하는 상태로 바꾸고 result 스트링에 저장합니다.
                String result = setMode(time.getCurrentWifiChecking(),
                        time.getCurrentSoundChecking());

                // 상태창을 띄워주는 함수를 호출합니다.
                makeNotification(time.getName() + "is end", result);

                // endTime이 실행되어 startTime 때 실행되게 합니다.
                time.setStartFlag(false);

            }

        }

        Log.i(TAG, currentTime);

    }

    // DataForm객체와 id를 받아 등록시킵니다.
    public void registerDataForm(DataForm dataform, int id) {

        // 이 데이타를 쓸지 않쓸지 확인합니다. 1이면 쓴다. 2이면 쓰지 않는다.
        if (dataform.getOn_Off_Selecting() == 1) {

            // 위치로 판별한다.
            if (dataform.getTime_Location_Checking() == 0) {
                registerLocation(dataform, id);
            }
            // 시간으로 판별한다.
            else if (dataform.getTime_Location_Checking() == 1) {
                registerTime(dataform, id);
            }
            // 시간과 위치를 동시에 사용한다.
            else if (dataform.getTime_Location_Checking() == 2) {

            } else {
                // 에러가발생
            }

        }
    }

    // DataForm에 있는 위치 정보를 저장시킨다.
    public void registerLocation(DataForm dataform, int id) {

        // 데이터를 원하는 변수로 변환시킨다.
        double latitude = Double.parseDouble(dataform.getLatitude());
        double longitude = Double.parseDouble(dataform.getLongitude());
        float radius = Float.parseFloat(dataform.getDiameter());
        String name = dataform.getLocation_Name();
        int wifiChecking = dataform.getWifi_Checking();
        int soundChecking = dataform.getSound_Checking();

        // 인텐트를 등록시킨다. 내가 원하는 정보를 집어 넣는다.
        Intent proxintent = new Intent(intentkey);
        proxintent.putExtra("wifiChecking", wifiChecking);
        proxintent.putExtra("soundChecking", soundChecking);
        proxintent.putExtra("name", name);

        // 팬딩인텐트를 등록시킨다.
        PendingIntent intent = PendingIntent.getBroadcast(this, id, proxintent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        // 근접경보를 등록시킨다.
        locationManager.addProximityAlert(latitude, longitude, radius, 10,
                intent);

        Log.i(TAG, "Location add complete");
    }

    // DataForm객체와 id 를 이용해서 시간에 따른 이벤트를 등록한다.
    public void registerTime(DataForm dataform, int id) {

        // 타임객체를 생성한다.
        TimeForm time = new TimeForm();

        // 타임객체에 데이터를 넣는다.
        time.setName(dataform.getLocation_Name());
        time.setStartTime(dataform.getStart_Time());
        time.setEndTime(dataform.getEnd_Time());
        time.setDaySum(dataform.getDay_Sum());
        time.setSoundChecking(dataform.getSound_Checking());
        time.setWifiChecking(dataform.getWifi_Checking());

        // 타임리스트에 타임객체를 넣는다.
        timeList.add(time);

        Log.i(TAG, "time is added in timeList");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("NewApi")
    // 상태창을 생성해주는 함수이다.
    public void makeNotification(String title, String text) {

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, LocationSettingActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification n = new Notification.Builder(this).setContentTitle(title)
                .setContentText(text).setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent).setAutoCancel(true).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, n);
        Log.i(TAG, title + "  " + text + "  is notificated");

    }

    // 모드를 바꿔주는 함수이다.
    public String setMode(int wifiChecking, int soundChecking) {

        Log.i(TAG, wifiChecking + " " + soundChecking);
        StringBuffer result = new StringBuffer();

        if (soundChecking == 0) {

            audiomanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            result.append("Normal Mode ON");
        } else if (soundChecking == 1) {

            audiomanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            result.append("Vibration Mode ON");

        } else if (soundChecking == 2) {

            audiomanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            result.append("Silent Mode ON");

        } else {
            Log.i(TAG, "error in set mode");
        }

        if (wifiChecking == 1) {
            wifimanager.setWifiEnabled(true);
            result.append("	WIFI ON");

        } else {
            wifimanager.setWifiEnabled(false);
            result.append("	WIFI OFF");

        }

        Log.i(TAG, result.toString());

        return result.toString();
    }

    // 근접경보가 처리되는 브로드캐스팅 리시버이다.
    private class locationIntentReciver extends BroadcastReceiver {

        private String mExpectedAction;

        public locationIntentReciver(String expectedAction) {
            mExpectedAction = expectedAction;
        }

        // 필터를 설정한다.
        public IntentFilter getFilter() {
            IntentFilter filter = new IntentFilter(mExpectedAction);
            return filter;
        }

        // 근접경보를 받았을때 처리하는 함수이다.
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                String result = "";
                String name = intent.getStringExtra("name");
                int wifiChecking = intent.getIntExtra("wifiChecking", 4);
                int soundChecking = intent.getIntExtra("soundChecking", 4);

                // 모드를 바꿔주는 함수를 호출한다.
                result = setMode(wifiChecking, soundChecking);

                // 상태창을 호출한다.
                makeNotification(name, result);

                //code  service start


            }
        }

    }

}
