package com.nbb.aaa.flower;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMyActivityAtBootReceiver extends BroadcastReceiver{//on boot up the application will open automatically
    public void onReceive(Context context, Intent intent){
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
                Intent activityIntent = new Intent(context, MainActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);
        }
    }
}


