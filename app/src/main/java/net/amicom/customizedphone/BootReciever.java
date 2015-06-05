package net.amicom.customizedphone;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;

public class BootReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Intent startServiceIntent = new Intent(context, MyService.class);
        context.startService(startServiceIntent);
    }

}
