package com.nbb.aaa.flower;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    Button bluetooth, btnPush, btnCancel;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bluetooth = findViewById(R.id.btnBluetooth);
        bluetooth.setOnClickListener(this);


        btnPush = findViewById(R.id.btnPush);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(this);
        btnPush.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {//start connecting to bluetooth
        if(v == bluetooth){
            Intent intent = new Intent(this, DeviceListActivity.class);
            startActivity(intent);
        }

        if(v == btnPush){//start service
            Intent intent = new Intent(this, PushService.class);
            startService(intent);

        }
        if(v == btnCancel){//stop service
            Intent intent = new Intent(this, PushService.class);
            stopService(intent);
        }
    }
}
