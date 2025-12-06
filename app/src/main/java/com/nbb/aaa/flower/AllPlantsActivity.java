package com.nbb.aaa.flower;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AllPlantsActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    EditText etname, etlineNumber, etirrigationTime, etwhenToWater, etwaterFrequency, etwaterTime, etwasRaining;
    DataBaseFlower db;
    ListView lv;
    PlantAdapter pa;
    List<Plant> plants;
    TextView _id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_plants);

        db = new DataBaseFlower(this);
        plants = db.getAllPlants();
        pa = new PlantAdapter(this,0,0,plants);
        lv =  findViewById(R.id.lv);

        lv.setAdapter(pa);

        lv.setOnItemLongClickListener(this);

        //how every plant in list should be looking
        etname = findViewById(R.id.etUpdatePlantName);
        etlineNumber = findViewById(R.id.etUpdatePlantLineNumber);
        etirrigationTime = findViewById(R.id.etUpdatePlantIrrigationTime);
        etwhenToWater = findViewById(R.id.etUpdatePlantWhenToWater);
        etwaterFrequency = findViewById(R.id.etUpdatePlantWaterFrequency);
        etwaterTime = findViewById(R.id.etUpdatePlantWaterTime);
        etwasRaining = findViewById(R.id.etUpdatePlantWasRaining);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {//on item click listener

        _id = view.findViewById(R.id.tvIID);
        Plant plant = db.getPlantById(Long.valueOf(_id.getText().toString()));
        if(plant == null) {
            return false;
        }

        Intent intent = new Intent(this, UpdatePlantActivity.class);
        intent.putExtra("id",plant.id);


        startActivityForResult(intent, 2);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//show me the plant
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {

            Plant plant = (Plant)data.getSerializableExtra("plant");
            Log.d("nick","AllPlantsActivity, get plant from intent "+plant.toString());

            if (db.updatePlant(plant) != 1) {
                Intent intent = new Intent(this, AddPlantActivity.class);//if fail
                Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
                Log.d("nick", "AllPlantsActivity, crash on update");
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}

