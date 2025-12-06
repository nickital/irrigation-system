package com.nbb.aaa.flower;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DetailsAdapter extends ArrayAdapter<Details> {

        Context context;
        List<Details> details;
        public DetailsAdapter(Context context, int resource,int textViewResourceId, List<Details> details) {
            super(context, resource,textViewResourceId,details);
            this.context = context;
            this.details = details;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {//what the params are

            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            View view  = layoutInflater.inflate(R.layout.custom_detail_in_listview,parent,false);
            TextView id = view.findViewById(R.id.tvIID);
            TextView plant_id = view.findViewById(R.id.tvIPlantId);
            TextView whenWasWatered = view.findViewById(R.id.tvIWhenWasWatered);
            TextView water = view.findViewById(R.id.tvIWater);
            TextView byRain = view.findViewById(R.id.tvIByRain);


            Details tmp = details.get(position);
            id.setText(id.getText() + String.valueOf(tmp.get_id()));
            plant_id.setText(plant_id.getText() + String.valueOf(tmp.getPlantId()));

            long whenWasWateredDate = tmp.getWhenWasWatered();//get the date of the irrigation
            Date date = new Date(whenWasWateredDate);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String dateText = df2.format(date);

            Date time = new Date(tmp.getWater());//get how many time the plant was watered
            Log.d("nick", "Details adapter, tmp.getWater = " + tmp.getWater());
            DateFormat formatter = new SimpleDateFormat("mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timeFormatted = formatter.format(time);
            Log.d("nick", "Details adapter, timeFormatted  = " + timeFormatted);


            water.setText(water.getText() + String.valueOf(timeFormatted));

            whenWasWatered.setText(whenWasWatered.getText() + String.valueOf(dateText));

            if (0 == Integer.valueOf(String.valueOf(tmp.getByRain()))){//irrigated by rain
                byRain.setText(byRain.getText() + "rain");
            }

            if (1 == Integer.valueOf(String.valueOf(tmp.getByRain()))){//automatic irrigation
                byRain.setText(byRain.getText() + "irrigate");
            }

            if (2 == Integer.valueOf(String.valueOf(tmp.getByRain()))){//irrigated by man
                byRain.setText(byRain.getText() + "by man");
            }

            return view;
        }


    }
