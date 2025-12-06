package com.nbb.aaa.flower;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class UpdatePlantActivity extends AppCompatActivity implements View.OnClickListener {
    long id;
    EditText etname, etlineNumber, etirrigationTime, etwhenToWater, etwaterFrequency, etwaterTime, etwasRaining;
    Button btnUpdate, btnDelete, btnDetails;
    Intent intent;
    TextView tvId;
    DataBaseFlower dbf;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plant);
        etname = findViewById(R.id.etUpdatePlantName);
        etlineNumber = findViewById(R.id.etUpdatePlantLineNumber);
        etirrigationTime = findViewById(R.id.etUpdatePlantIrrigationTime);
        etwhenToWater = findViewById(R.id.etUpdatePlantWhenToWater);
        etwaterFrequency = findViewById(R.id.etUpdatePlantWaterFrequency);
        etwaterTime = findViewById(R.id.etUpdatePlantWaterTime);
        etwasRaining = findViewById(R.id.etUpdatePlantWasRaining);
        btnUpdate = findViewById(R.id.btnUpdatePlant);
        btnDelete = findViewById(R.id.btnDeletePlant);
        btnDetails = findViewById(R.id.btnDetailsOfPlant);
        tvId = findViewById(R.id.updatePlantTvId);

        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnDetails.setOnClickListener(this);
        intent = getIntent();

        dbf = new DataBaseFlower(this);

        id = getIntent().getExtras().getLong("id");







        if (id != 0) {
            Plant plant = dbf.getPlantById(id);

            long whenToWaterDate = plant.getWhenToWater();//get plant's irrigation date in millis
            Date date = new Date(whenToWaterDate);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");//ho to show

            String dateText = df2.format(date);

            Date dateTime = new Date(plant.getIrrigationTime());//get plant's irrigation date
            DateFormat dateTimeformatter = new SimpleDateFormat("HH:mm");
            dateTimeformatter.setTimeZone(TimeZone.getTimeZone("GMT"));

            String dateTimeFormatted = dateTimeformatter.format(dateTime);

            Date time = new Date(plant.getWaterTime());//get plant's water time
            DateFormat formatter = new SimpleDateFormat("m:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

            String timeFormatted = formatter.format(time);

            etname.setText(plant.getName());
            etlineNumber.setText(String.valueOf(plant.getLineNumber()));
            etirrigationTime.setText(dateTimeFormatted);
            etwhenToWater.setText(dateText);
            etwaterFrequency.setText(String.valueOf(plant.getWaterFrequency()));
            etwaterTime.setText(timeFormatted);
            etwasRaining.setText(String.valueOf(plant.getWasRaining()));



            tvId.setText("Plant " + plant.toString());
            Log.d("nick", "UpdtaeET "+id);//check if it entered to the "if"
        }
    }

        public void onClick (View view){
            if (view == btnUpdate) {
                String strWhenToWater = etwhenToWater.getText().toString();
                String strIrrigationTime = etirrigationTime.getText().toString();
                String strWaterTime = etwaterTime.getText().toString();

                Log.d("nick", "UpdatePlantActivity: strWhenToWater="+strWhenToWater+", strIrrigationTime="+strIrrigationTime+", strWaterTime="+strWaterTime);

                //convert millis to date
                //<--------------------------------------------------------------------------->
                String myDate = String.valueOf(strWhenToWater);
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");

                Date date = null;
                try {
                    date = sdfDate.parse(myDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long dateInMillis = date.getTime();
                Log.d("nick", "UpdatePlantActivity: myDate="+myDate+", dateInMillis="+dateInMillis);
                //<--------------------------------------------------------------------------->

                //convert millis to time
                //<--------------------------------------------------------------------------->
                //creates a format that parses the time in the given format
                //time
                String myDateTime = String.valueOf(strIrrigationTime);
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm");
                sdfDateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date dateTime = null;
                try {
                    dateTime = sdfDateTime.parse(myDateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long dateTimeInMillis = dateTime.getTime();
                Log.d("nick", "UpdatePlantActivity: myDateTime="+myDateTime+", dateTimeInMillis="+dateTimeInMillis);
                //<--------------------------------------------------------------------------->


                //convert millis to time
                //<--------------------------------------------------------------------------->
                //creates a format that parses the time in the given format
                //how much time to irrigate
                String myTime = String.valueOf(strWaterTime);
                SimpleDateFormat sdfTime = new SimpleDateFormat("m:ss");
                sdfTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date time = null;
                try {
                    time = sdfTime.parse(myTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long timeInMillis = time.getTime();
                Log.d("nick", "UpdatePlantActivity: myTime="+myTime+", timeInMillis="+timeInMillis);

                //<--------------------------------------------------------------------------->



                String name = etname.getText().toString();
                int lineNumber = Integer.valueOf(etlineNumber.getText().toString());
                long irrigationTime = dateTimeInMillis;
                long whenToWater = dateInMillis;
                int waterFrequency = Integer.valueOf(etwaterFrequency.getText().toString());
                long waterTime = timeInMillis;
                int wasRaining = Integer.valueOf(etwasRaining.getText().toString());

                Plant plant = new Plant(id,name,lineNumber,irrigationTime,whenToWater,waterFrequency,waterTime,wasRaining);

                dbf.updatePlant(plant);//update plants' params

                intent.putExtra("plant", plant);
                setResult(RESULT_OK, intent);
                finish();
            }

             else if (view == btnDelete) {//delete plant
                dbf.deletePlantByRow(id);
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            else if(view == btnDetails){//see details of plant
                intent = new Intent(this, AllDetailsActivity.class);
                intent.putExtra("plant_id", id);
                startActivity(intent);
                dbf.getAllDetailsByPlantId(id);
                finish();
            }
        }
    }


