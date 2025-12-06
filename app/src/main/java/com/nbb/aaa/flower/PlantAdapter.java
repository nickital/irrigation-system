package com.nbb.aaa.flower;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PlantAdapter extends ArrayAdapter<Plant> {

    Context context;
    List<Plant> plants;
    public PlantAdapter(Context context, int resource,int textViewResourceId, List<Plant> plants) {
        super(context, resource,textViewResourceId,plants);
        this.context = context;
        this.plants = plants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//the unique params of every plant

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view  = layoutInflater.inflate(R.layout.custom_plant_in_listview,parent,false);
        TextView id = view.findViewById(R.id.tvIID);
        TextView name = view.findViewById(R.id.tvIName);
        TextView irrigationTime = view.findViewById(R.id.tvIIrrigationTime);
        TextView whenToWater = view.findViewById(R.id.tvIWhenToWater);
        TextView wasRaining = view.findViewById(R.id.tvIWasRaining);
        ImageView imgWasRaining = view.findViewById(R.id.ivIWasRaining);


        Plant tmp = plants.get(position);
        id.setText(id.getText() + String.valueOf(tmp.get_id()));
        name.setText(name.getText() + String.valueOf(tmp.getName()));

        long whenToWaterDate = tmp.getWhenToWater();//get irrigation date
        Date date = new Date(whenToWaterDate);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df2.format(date);


        Date dateTime = new Date(tmp.getIrrigationTime());//get the irrigation time
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("HH:mm");
        dateTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateTimeFormatted = dateTimeFormatter.format(dateTime);


        irrigationTime.setText(irrigationTime.getText() + String.valueOf(dateTimeFormatted));

        whenToWater.setText(whenToWater.getText() + String.valueOf(dateText));

        wasRaining.setText(wasRaining.getText() + String.valueOf(tmp.getWasRaining()));


        //which color should I put?
        if(0 == Integer.valueOf(String.valueOf(tmp.getWasRaining()))) {//irrigation started, waits for the water
            imgWasRaining.setImageResource(R.drawable.grey);
        }

        else if(1 == Integer.valueOf(String.valueOf(tmp.getWasRaining()))){//fill the water
            imgWasRaining.setImageResource(R.drawable.blue);
        }

        else if(2 == Integer.valueOf(String.valueOf(tmp.getWasRaining()))){//all good, waits for the next irrigation
            imgWasRaining.setImageResource(R.drawable.green);
        }

        else if(3 == Integer.valueOf(String.valueOf(tmp.getWasRaining()))){//has problem, didn't get the water from last irrigation
            imgWasRaining.setImageResource(R.drawable.red);
        }

        return view;
    }

}
