package com.nbb.aaa.flower;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class PushService extends Service {//it is the code of the service, you can return back to the application by one touch

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1,getNotification());
        Toast.makeText(getApplicationContext(), "startForeground", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "stopForeground", Toast.LENGTH_LONG).show();
        stopForeground(true);
    }

    public Notification getNotification() {
        //phase 1
        int icon = android.R.drawable.star_on;
        String ticket = " this is ticket message";
        long when = System.currentTimeMillis();
        String title = "Flower";
        String ticker = "ticker";
        String text="text";
        //phase 2

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            //phase 3


            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "YOUR_CHANNEL_ID";
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                builder.setChannelId(channelId);
           }



        Notification notification = builder.setContentIntent(pendingIntent).setSmallIcon(icon).setTicker(ticker).setWhen(when)
                .setAutoCancel(true).setContentTitle(title)
                .setSmallIcon(android.R.drawable.star_on).setDefaults(Notification.DEFAULT_SOUND)
                .setContentText("you are looking good today!").build();



        return notification;
    }

}

