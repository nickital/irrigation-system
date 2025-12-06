package com.nbb.aaa.flower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AllDetailsActivity extends AppCompatActivity {

    DataBaseFlower dbf;
    ListView lv;
    DetailsAdapter da;
    List<Details> details;
    TextView _id, plant_id, whenWasWatered, water, byRain;
    long id;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_details);

        id = getIntent().getExtras().getLong("plant_id");
        Log.d("tal", "" + id);
        dbf = new DataBaseFlower(this);
        details = dbf.getAllDetailsByPlantId(id);
        da = new DetailsAdapter(this,0,0,details);
        lv =  findViewById(R.id.lv);

        lv.setAdapter(da);

        //how every detail in list should be looking like
        plant_id = findViewById(R.id.tvIPlantId);
        whenWasWatered = findViewById(R.id.tvIWhenWasWatered);
        water = findViewById(R.id.tvIWater);
        byRain = findViewById(R.id.tvIByRain);
    }
}
