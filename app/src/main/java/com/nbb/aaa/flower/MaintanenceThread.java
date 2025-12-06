package com.nbb.aaa.flower;

import java.lang.Thread;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.os.Handler;
import android.util.Log;


public class MaintanenceThread extends Thread { //control the automatic irrigation
    static final int sleepTime = 6000;
    DataBaseFlower dbf;
    StringBuilder recDataString = new StringBuilder();
    BlueToothSocket btSocket;
    public MaintanenceThread(DataBaseFlower dbf, BlueToothSocket btSocket) {
        this.dbf = dbf;
        this.btSocket = btSocket;
        Handler handler = new Handler() {//speaks with little brother, arduino.
            public void handleMessage(android.os.Message msg) {
                if (msg.what == BlueToothSocket.handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                          // msg.arg1 = bytes from connect thread
                    Log.d("nick", "MaintanenceThread, readMessage = " + readMessage);
                    filterMessage(readMessage);                                  //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        // TODO receive.setText(dataInPrint);
                        Log.d("nick", "received: " + dataInPrint);
                        int dataLength = dataInPrint.length();                          //get length of data received
                        updateHumidity(dataInPrint);
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        dataInPrint = " ";
                    }
                }
            }

            private void filterMessage(String readMessage) {//check if it is a valid string
                final String allowedChars = "mlndrw123~";
                char ch;
                for(int i = 0; i < readMessage.length(); i++) {
                    ch = readMessage.charAt(i);
                    if (allowedChars.indexOf(ch) != -1) {//if true, it will return the index of ch.
                        recDataString.append(ch);
                    }
                }
            }
        };

        this.btSocket.setHandler(handler);
    }


    public void updateHumidity(String data) {//execute orders
        long currentTime = System.currentTimeMillis();// time right now

        int day = 1000 * 60 * 60 * 24; //milliseconds in day
        int second =1000; //milliseconds in second
        char ch = data.charAt(0);
        String str = data.substring(1);
        int x = Integer.valueOf(str);
        Plant plant = dbf.getPlantByLine(x);
        //w- sensor is wet
        //d- sensor is dry
        if (ch == 'w') {//if it is wet
            if (plant.wasRaining == 0) {//under irrigate
                plant.setWasRaining(1);//it is feeling the water
                dbf.updateWasRainingOfPlant(plant);//update database

                Log.d("nick", "updateHumidity, w- updateHumidity, feeling the water, irrigate " + plant.getLineNumber());
            }
            else if (plant.wasRaining == 2 || plant.wasRaining == 3) {//its raining
                plant.setWasRaining(1);//it is raining
                Details details = new Details(plant.id, currentTime, 0, 0);//rain
                dbf.insertDetail(details);
                dbf.updateWasRainingOfPlant(plant);
                Log.d("nick", "updateHumidity, w- updateHumidity, feeling the water, raining " + plant.getLineNumber());


                long dateInMillis = getDateInMillis();
                if (dateInMillis + (plant.waterFrequency * day) > plant.whenToWater) {//should I move the next irrigation date to later //if it equals, don't do anything
                    //if yes
                    plant.setWhenToWater(dateInMillis + plant.waterFrequency * day);//change the next irrigation date
                    dbf.updateWhenToWaterOfPlant(plant);
                    Log.d("nick", "updateHumidity, w- updateHumidity, change irrigation date " + plant.whenToWater);
                    Details detail = new Details(plant.id, dateInMillis, 0, 0);//rain
                    dbf.insertDetail(detail);
                    Log.d("nick", "updateHumidity, w- updateHumidity, when was watered = " + detail.getWhenWasWatered());
                }
            }
        }
        else if (ch == 'm') {//if irrigates by man
            plant.setWasRaining(0);//irrigating by man has been started
            dbf.updateWasRainingOfPlant(plant);//update database
            Details detail = new Details(plant.id, currentTime, 0, 2);//by rain= by man
            dbf.insertDetail(detail);
            Log.d("nick", "updateHumidity, insert detail = " + detail.toString());
            Log.d("nick", "updateHumidity, m- updateHumidity, feeling the water, irrigate by man " + plant.getLineNumber());
        }

        else if (ch == 'n') {//if stops irrigate by man
            Log.d("nick", "n = " + plant.toString());

            Details detail = dbf.getLatestDetailByPlantId(plant.id);
            if (detail != null) {
                long water = currentTime - detail.getWhenWasWatered();
                dbf.updateDetailWater(detail.get_id(), water);
                Log.d("nick", "ch = n, all cool");
                plant.setWasRaining(2);//irrigating by man has been started
                dbf.updateWasRainingOfPlant(plant);//update database

                long dateInMillis = getDateInMillis();
                plant.setWhenToWater(dateInMillis + (plant.waterFrequency * day)); //date now + water frequency * milliseconds in day
                dbf.updateWhenToWaterOfPlant(plant);

                off(plant);
            }
            else{
                Log.d("nick", "ch = n, this detail doesn't exists");
            }
        }
        else if (ch == 'd') {//if dry
            if (plant.wasRaining == 0 || plant.wasRaining == 1) {
                plant.setWasRaining(2);
                dbf.updateWasRainingOfPlant(plant);

                long dateInMillis = getDateInMillis();
                plant.setWhenToWater(dateInMillis + (plant.waterFrequency * day)); //date now + water frequency * milliseconds in day
                dbf.updateWhenToWaterOfPlant(plant);
                
                off(plant);

                Log.d("nick", "d = " + plant.toString());

                Details detail = dbf.getLatestDetailByPlantId(plant.id);

                if (detail != null) {
                    long water = currentTime - detail.getWhenWasWatered();
                    dbf.updateDetailWater(detail.get_id(), water);
                    Log.d("nick", "ch = d, all cool");
                }
                else{
                    Log.d("nick", "ch = d, this detail doesn't exists");
                }
            }
        }
    }

    @Override
    public void run() {//automatic irrigation
        super.run();
        boolean bCanRun = true;
        int day = 1000*60*60*24; //milliseconds in day
        while(bCanRun) {
            try {
                Log.d("nick", "Before sleep");

                List<Plant> plants = dbf.getAllPlants();
                int plantSize = plants.size();
                int tmpPlant = 0;
                long currentTime = System.currentTimeMillis();
                while(tmpPlant < plantSize){
                    Plant plant = plants.get(tmpPlant);
                    if(plant.whenToWater + plant.irrigationTime <= currentTime && plant.wasRaining != 0 && plant.wasRaining != 1) {//if time to water <= currentTimer(now), and its not already wet or under irrigation
                        on(plant);//start irrigation
                        Details details = new Details(plant.id, currentTime, 0, 1);//create instance of object details. irrigation
                        dbf.insertDetail(details);//insert details to database
                        plant.setWasRaining(0);//start the irrigation now
                        dbf.updateWasRainingOfPlant(plant);//update wasRaining of plant in database
                        Log.d("nick", "currentTime > whenToWater, im starting irrigation, now its "+ currentTime + " o'clock");
                    }
                    //is it being irrigating too much?
                    else if(plant.getWasRaining() == 0) { //if it is still dry
                        Details detail = dbf.getLatestDetailByPlantId(plant.id);
                        if (detail.getWhenWasWatered() + plant.waterTime + (1000 * 20) < currentTime) { //one minute in this case //if more than hour past and its still irrigating //          should be this- Details detail = dbf.getLatestDetailByPlantId(plant.id);if (detail.getWhenWasWatered() + plant.waterTime*1000*60 + (1000 * 60 * 60) <= currentTime) { //if more than hour past and its still irrigating ///

                            if (detail != null) {
                                long water = currentTime - detail.getWhenWasWatered();
                                dbf.updateDetailWater(detail.get_id(), water);
                                Log.d("nick", "MT, update detail's -water- " + detail.toString());
                            }

                            plant.setWasRaining(3);
                            dbf.updateWasRainingOfPlant(plant);

                            long dateInMillis = getDateInMillis();
                            plant.setWhenToWater(dateInMillis + (plant.waterFrequency * day)); //water frequency * milliseconds in day
                            dbf.updateWhenToWaterOfPlant(plant);

                            Log.d("nick", "MaintanenceThread, plant is being irrigated too much");
                            off(plant);
                        }
                        else{
                            Log.d("nick", "MaintanenceThread, plant is being irrigated, still doesn't feel the water.");
                        }
//                        Details detail = new Details(plant.id, getDateInMillis(), 0, 1);//by rain= by irrigation
//                        dbf.insertDetail(detail);
//                        Log.d("nick", "updateHumidity, insert detail = " + detail.toString());
                    }
                    else if (plant.getWasRaining() == 1) { //if the the sensor is wet
                        Details detail = dbf.getLatestDetailByPlantId(plant.id);
                        if (detail.getWhenWasWatered() + plant.waterTime < currentTime) { //if all cool

                            long water = currentTime - detail.getWhenWasWatered();
                            dbf.updateDetailWater(detail.get_id(), water);

                            plant.setWasRaining(2);//all good
                            dbf.updateWasRainingOfPlant(plant);

                            long dateInMillis = getDateInMillis();
                            plant.setWhenToWater(dateInMillis + (plant.waterFrequency * day)); //date now + water frequency * milliseconds in day
                            dbf.updateWhenToWaterOfPlant(plant);

                            off(plant);
                        }
                    }

                    Log.d("nick", "MT, plant = " + plant.toString());
                    tmpPlant++;
                }
                sleep(sleepTime);
                Log.d("nick", "After sleep");
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("nick", "End of Maintenance thread run;");
                bCanRun = false;
            }
        }
    }

    public void on(Plant plant) { //irrigate someone specific
        if (btSocket.write("r" + plant.getLineNumber()) != -1) {
            Log.d("nick", "r" + plant.getLineNumber());
            Log.d("nick", "sent");
        }
        else
            Log.d("nick", "Connection failure");
    }

    public void off(Plant plant) { //close someone specific
        if (btSocket.write("l" + plant.getLineNumber()) != -1) {

            Log.d("nick", "l" + plant.getLineNumber());
            Log.d("nick", "sent");
        }
            //write bytes over BT connection via outstream
        else
            Log.d("nick", "Connection failure");

    }

    public long getDateInMillis() {
        Date dateReally = new Date();                                          //I want to get only the date(without time) of now.
        SimpleDateFormat df2really = new SimpleDateFormat("dd/MM/yyyy");//
        String dateTextReally = df2really.format(dateReally);                  //

        Date date = null;                                                      //
        try {                                                                  //
            date = df2really.parse(dateTextReally);                            //
        } catch (ParseException e) {                                           //
            e.printStackTrace();                                               //
        }
        long dateInMillis = date.getTime();                                    // now I have finally got it
        return dateInMillis;
    }

}
//m- man, on
//n- man, off
//r- computer, on
//l- computer, off

//<--------------->

//0- rain
//1- irrigate
//2- man

