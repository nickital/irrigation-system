package com.nbb.aaa.flower;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class DeviceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    DataBaseFlower dbf = new DataBaseFlower(this);
    ListView list;
    ArrayAdapter<String> adapter;
    Set<BluetoothDevice> pairedDevices;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        list = findViewById(R.id.lv);
        list.setOnItemClickListener(this);
        Log.d("nick","DeviceListActivity, onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();

        btCheckState();
        Log.d("nick","after check");
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d("nick","got the adapter again");
        adapter = new ArrayAdapter<String>(this,R.layout.device_name);
        Log.d("nick","initiated array adapter");
        if (btAdapter != null) {
            Log.d("nick","in if");
            pairedDevices = btAdapter.getBondedDevices();
            Log.d("nick","got the set");
        }
        else
        {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
           // return;
        }
        if (pairedDevices.size() > 0)
        {
            for (BluetoothDevice device : pairedDevices)
            {
                adapter.add(device.getName() + "\n" + device.getAddress());
            }
        }

        list.setAdapter(adapter);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//choose device

        String address = "";
        String info = ((TextView) view).getText().toString();
        if (info.length() >= 17)
        {
            address = info.substring(info.length() - 17);

            dbf.updateBluetooth(address);
            Log.d("nick", "DeviceListActivity, connect to bluetooth, address= " + address + " info= " + info);


            Intent intent = new Intent(this,BluetoothActivity.class);
            intent.putExtra("address",address);
            startActivity(intent);
        }
    }

    public void btCheckState()//check if it is supporting bluetooth
    {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter == null)
        {
            Toast.makeText(this,"Device does not support Bluetooth",Toast.LENGTH_LONG).show();
            return;
        }else {
            if (btAdapter.isEnabled()) {
                Toast.makeText(this,"Lets start",Toast.LENGTH_SHORT).show();

            } else {

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }
}
