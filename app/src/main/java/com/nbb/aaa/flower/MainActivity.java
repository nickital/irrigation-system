package com.nbb.aaa.flower;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
//todo arduino- convert string to int;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button plant, addPlant, settings;
    DataBaseFlower dbf = new DataBaseFlower(this);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        plant = findViewById(R.id.btnPlant);
        addPlant = findViewById(R.id.btnAddPlant);
        settings = findViewById(R.id.btnSettings);

        plant.setOnClickListener(this);
        addPlant.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){//fast connection to the bluetooth, but first you have to connect via settings and choose the right macAddress
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if(id == R.id.action_connect_to_bluetooth)
        {
            String address = dbf.getBluetooth().getAddress();
            Log.d("nick", address);

            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            if (btAdapter != null) {
                Log.d("nick", "MainActivity: connect: btAdapter.isEnabled = " + btAdapter.isEnabled());
            }
            Intent intent = new Intent(this, BluetoothActivity.class);
            intent.putExtra("address", address);
            startActivity(intent);
        }

        return true;
    }





    @Override
    public void onClick(View v) {
        if(v== plant){//open plant list view
            Intent intent= new Intent(this, AllPlantsActivity.class);
            startActivity(intent);
        }

        if(v == addPlant){//add plant
            Intent intent = new Intent(this, AddPlantActivity.class);
            startActivity(intent);
        }

        if(v == settings){//open settings
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }


    }
}

