package com.nbb.aaa.flower;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener {

    Button on, off, main, dialogStart, dialogStop;
    BlueToothSocket btSocket = null;
    Dialog d;
    EditText lineOfPlant;
    DataBaseFlower dbf ;
    SharedPreferences sp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        on = findViewById(R.id.btnOn);
        off = findViewById(R.id.btnOff);
        main = findViewById(R.id.btnReturn);

        on.setOnClickListener(this);
        off.setOnClickListener(this);
        main.setOnClickListener(this);
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        Log.d("nick", "BluetoothActivity, onCreate");
        dbf = new DataBaseFlower(this);

        btSocket = BlueToothSocket.getInstance(address);

        if (btCheckState() && btSocket.activate()) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            MaintanenceThread mt = new MaintanenceThread(dbf, btSocket);
            Log.d("nick", "before start");
            mt.start(); // activate maintenance thread
            Log.d("nick", "activate maintenance thread");
            Log.d("nick", "after start");

        }


    }

    public void createStartDialog()//create start dialog
    {
        d = new Dialog(this);
        d.setContentView(R.layout.custom_start_dialog);
        d.setTitle("start irrigate plant");
        d.setCancelable(true);
        lineOfPlant = d.findViewById(R.id.etLineOfPlant);
        dialogStart = d.findViewById(R.id.btnDialogStart);
        dialogStart.setOnClickListener(this);
        d.show();
    }

    public void createStopDialog()//create stop dialog
    {
        d = new Dialog(this);
        d.setContentView(R.layout.custom_stop_dialog);
        d.setTitle("start irrigate plant");
        d.setCancelable(true);
        lineOfPlant = d.findViewById(R.id.etLineOfPlant);
        dialogStop = d.findViewById(R.id.btnDialogStop);
        dialogStop.setOnClickListener(this);
        d.show();
    }


    @Override
    public void onClick(View view)
    {
        if(view == on){ //start irrigation
            createStartDialog();
        }
        else if(view == dialogStart)//start "start dialog"
        {
            Log.d("nick", "dialog start, line of plant is " + lineOfPlant.getText().toString());
            on();
            d.dismiss();
        }
        else if(view == off){ //stop irrigation
            createStopDialog();
            d.setTitle("stop irrigate plant");
        }
        else if(view == dialogStop) {//start "stop dialog"
            Log.d("nick", "dialog stop, line of plant is " + lineOfPlant.getText().toString());
            off();
            d.dismiss();
        }
        else if(view == main){//return to main
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public boolean btCheckState()
    {
        boolean bRet = true;
        int state = btSocket.init();
        if (state == -1)
        {
            Toast.makeText(this,"Device does not support Bluetooth",Toast.LENGTH_SHORT).show();
        }else {
            if (state == 1) {
                Toast.makeText(this,"Good to go",Toast.LENGTH_SHORT).show();
            } else {

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
                state = btSocket.init();
                if (state != 1) {
                    Log.d("nick", "Can't init BT: state = " + state);
                    bRet = false;
                }
            }
        }

        return bRet;
    }

    public void on() {  //start irrigate
       if (btSocket.write("m" + Integer.valueOf(lineOfPlant.getText().toString())) != -1) {//not working right now
           Toast.makeText(BluetoothActivity.this, "BluetoothActivity, sent- m" + Integer.valueOf(lineOfPlant.getText().toString()), Toast.LENGTH_LONG).show();//write bytes over BT connection via outstream
       }
        else
           Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
    }

    public void off() {  //stop irrigation
        if (btSocket.write("n" + Integer.valueOf(lineOfPlant.getText().toString())) != -1) {

            Toast.makeText(BluetoothActivity.this, "BluetoothActivity, sent- n" + Integer.valueOf(lineOfPlant.getText().toString()), Toast.LENGTH_LONG).show();//write bytes over BT connection via outstream
        }
        else
            Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
    }
}
