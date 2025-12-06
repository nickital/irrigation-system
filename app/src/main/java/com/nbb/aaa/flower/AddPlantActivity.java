package com.nbb.aaa.flower;

import android.content.Intent;
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
import java.util.TimeZone;

public class  AddPlantActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name,lineNumber,irrigationTime,whenToWater,waterFrequency,waterTime,wasRaining;
    Button save;
    Intent intent;

    DataBaseFlower dataBaseFlower;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        name = findViewById(R.id.etAddPlantName);
        lineNumber = findViewById(R.id.etAddPlantLineNumber);
        irrigationTime = findViewById(R.id.etAddPlantIrrigationTime);
        whenToWater = findViewById(R.id.etAddPlantWhenToWater);
        waterFrequency = findViewById(R.id.etAddPlantWaterFrequency);
        waterTime = findViewById(R.id.etAddPlantWaterTime);
        wasRaining = findViewById(R.id.etAddPlantWasRaining);
        save = findViewById(R.id.btnAddPlantSave);

        save.setOnClickListener(this);
        dataBaseFlower = new DataBaseFlower(this);

        intent = getIntent();
    }

    public void onClick(View view) {

        if(view == save){
            String strName = name.getText().toString();
            String strLineNumber = lineNumber.getText().toString();
            String strIrrigationTime = irrigationTime.getText().toString();//long
            String strWhenToWater = whenToWater.getText().toString();
            String strWaterFrequency = waterFrequency.getText().toString();
            String strWaterTime = waterTime.getText().toString();
            String strWasRaining = wasRaining.getText().toString(); //
            //int intraining = Integer.valueOf(strwasraining);

            //creates a format that parses the date in the given format
            //for example- String myDate = "29/10/2018";
            //date
            String myDate = strWhenToWater; //convert string of date into milliseconds since 1970
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");

            Date date = null;
            try {
                date = sdfDate.parse(myDate);
            } catch (ParseException e) {
            e.printStackTrace();
        }
            long dateInMillis = date.getTime();

            //creates a format that parses the time in the given format
            //time
            //when to water
            String myDateTime = strIrrigationTime; //convert string of time into milliseconds
            SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm");
            sdfDateTime.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date dateTime = null;
            try {
                dateTime = sdfDateTime.parse(myDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long dateTimeInMillis = dateTime.getTime();


            //how much time to water
            String myTime = strWaterTime; //convert string of time into milliseconds
            SimpleDateFormat sdfTime = new SimpleDateFormat("m:ss");
            sdfTime.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date time = null;

            try {
                time = sdfTime.parse(myTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long timeInMillis = time.getTime();
            Log.d("nick", "AddPlantActivity " + timeInMillis);

            Plant plant = new Plant(strName, Integer.valueOf(strLineNumber), dateTimeInMillis, dateInMillis, Integer.valueOf(strWaterFrequency), timeInMillis, Integer.valueOf(strWasRaining));//create instance of plant
            int sol = dataBaseFlower.insertPlant(plant);//add plant to database
            plant.set_id(sol);

            Toast.makeText(this, "done" + sol, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
