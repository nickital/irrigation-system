package com.nbb.aaa.flower;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBaseFlower {//here, all is understandable :)
    SqliteHelper helper;
    Context context;
    public DataBaseFlower(Context context) {
        helper = new SqliteHelper(context);
        this.context = context;
    }

    String []allColumns={SqliteHelper.COL_ID_PLANT, SqliteHelper.COL_NAME,SqliteHelper.COL_LINE_NUMBER,
            SqliteHelper.COL_IRRIGATION_TIME,SqliteHelper.COL_WHEN_TO_WATER,SqliteHelper.COL_WATER_FREQUENCY,SqliteHelper.COL_WATER_TIME,SqliteHelper.COL_WAS_RAINING};

    public int deletePlantByRow(long rowId)
    {
        SQLiteDatabase database = helper.getWritableDatabase();
        int deletedRows = database.delete(SqliteHelper.TABLE_PLANTS_NAME, SqliteHelper.COL_ID_PLANT + "=" + rowId, null);
        database.close();
        return deletedRows;
    }

    public Plant getPlantById(long rowId)
    {
        Plant plant = null;
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(SqliteHelper.TABLE_PLANTS_NAME, allColumns, SqliteHelper.COL_ID_PLANT + "=" +rowId, null, null, null, null);

        if(cursor.moveToFirst())
        {
            long id = cursor.getLong(cursor.getColumnIndex(SqliteHelper.COL_ID_PLANT));

            String name = cursor.getString(cursor.getColumnIndex(SqliteHelper.COL_NAME));
            int lineNumber = cursor.getInt(cursor.getColumnIndex(SqliteHelper.COL_LINE_NUMBER));
            long irrigationTime = cursor.getLong(cursor.getColumnIndex(SqliteHelper.COL_IRRIGATION_TIME));
            long whenToWater = cursor.getLong(cursor.getColumnIndex(SqliteHelper.COL_WHEN_TO_WATER));
            int waterFrequency = cursor.getInt(cursor.getColumnIndex(SqliteHelper.COL_WATER_FREQUENCY));
            long waterTime = cursor.getLong(cursor.getColumnIndex(SqliteHelper.COL_WATER_TIME));
            int wasRaining = cursor.getInt(cursor.getColumnIndex(SqliteHelper.COL_WAS_RAINING));

            plant = new Plant(id,name,lineNumber,irrigationTime,whenToWater,waterFrequency,waterTime,wasRaining);
        }
        cursor.close();
        database.close();
        Log.d("nick", "getPlantById: " + plant.toString());

        return plant;
    }

    public Plant getPlantByLine(int line)
    {
        Plant plant = null;
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(SqliteHelper.TABLE_PLANTS_NAME, allColumns, SqliteHelper.COL_LINE_NUMBER + "=" +line, null, null, null, null);

        if(cursor.moveToFirst())
        {
            long id = cursor.getLong(cursor.getColumnIndex(SqliteHelper.COL_ID_PLANT));
            String name = cursor.getString(cursor.getColumnIndex(SqliteHelper.COL_NAME));
            int lineNumber = cursor.getInt(cursor.getColumnIndex(SqliteHelper.COL_LINE_NUMBER));
            long irrigationTime = cursor.getLong(cursor.getColumnIndex(SqliteHelper.COL_IRRIGATION_TIME));
            long whenToWater = cursor.getLong(cursor.getColumnIndex(SqliteHelper.COL_WHEN_TO_WATER));
            int waterFrequency = cursor.getInt(cursor.getColumnIndex(SqliteHelper.COL_WATER_FREQUENCY));
            long waterTime = cursor.getLong(cursor.getColumnIndex(SqliteHelper.COL_WATER_TIME));
            int wasRaining = cursor.getInt(cursor.getColumnIndex(SqliteHelper.COL_WAS_RAINING));

            plant = new Plant(id,name,lineNumber,irrigationTime,whenToWater,waterFrequency,waterTime,wasRaining);
        }
        cursor.close();
        database.close();
        Log.d("nick", "database: getPlantByLine: " + plant.toString());

        return plant;
    }


    public  void insertDetail(Details details) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SqliteHelper.COL_PLANT_ID, details.getPlantId());
        contentValues.put(SqliteHelper.COL_WHEN_WAS_WATERED, details.getWhenWasWatered());//current time in milliseconds
        contentValues.put(SqliteHelper.COL_WATER, 0);
        contentValues.put(SqliteHelper.COL_BY_RAIN, details.getByRain());

        database.insert(SqliteHelper.TABLE_DETAILS_NAME, null, contentValues);
        database.close();
        Log.d("nick", "insert details: " + details.toString());
    }





    public void updateDetailWater(long id, long water)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SqliteHelper.COL_WATER, water);

        String whereClause = SqliteHelper.COL_ID_DETAIL + "=" + id;
        database.update(SqliteHelper.TABLE_DETAILS_NAME, values, whereClause, null);
        database.close();
        Log.d("nick", "update detail water: " + water);
    }

    public void updateBluetooth(String address){
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SqliteHelper.COL_BLUETOOTH, address);
        Log.d("nick", "update table bluetooth address=" + address);

        int x = database.update(SqliteHelper.TABLE_BLUETOOTH_NAME, values, null, null);// I don't need:  SqliteHelper.COL_BLUETOOTH + "=" + strBluetooth
        database.close();
        Log.d("nick", "update bluetooth address: " + address);
        Log.d("nick", "update bluetooth: number of updated rows = " + x);

    }

    public Bluetooth getBluetooth(){
        SQLiteDatabase database = helper.getReadableDatabase();
        Bluetooth bluetooth = null;

        Cursor cursor = database.query(SqliteHelper.TABLE_BLUETOOTH_NAME, null,null, null, null, null, null);

        if(cursor.moveToFirst()){
            String address = cursor.getString(cursor.getColumnIndex(SqliteHelper.COL_BLUETOOTH));

            bluetooth = new Bluetooth(address);
        }
        cursor.close();
        database.close();
        Log.d("nick", "get bluetooth = " + bluetooth);
        return bluetooth;
    }
    public Details getLatestDetailByPlantId(long plant_id){
        SQLiteDatabase database = helper.getReadableDatabase();
        Details detail = null;

        String sql = "SELECT * from "+SqliteHelper.TABLE_DETAILS_NAME+" where " + SqliteHelper.COL_PLANT_ID + "=? ORDER BY " + SqliteHelper.COL_ID_DETAIL + " DESC LIMIT 1";
        String[] params = {Long.toString(plant_id)};
        Cursor cursor = database.rawQuery(sql, params);
        if (cursor.moveToNext())
        {
            long id;
            long whenWasWatered;
            long water ;
            int byRain;

            int indexId = cursor.getColumnIndex(SqliteHelper.COL_ID_DETAIL);
            int indexWhenWasWatered = cursor.getColumnIndex(SqliteHelper.COL_WHEN_WAS_WATERED);
            int indexWater = cursor.getColumnIndex(SqliteHelper.COL_WATER);
            int indexByRain = cursor.getColumnIndex(SqliteHelper.COL_BY_RAIN);
            id = cursor.getLong(indexId);
            whenWasWatered = cursor.getLong(indexWhenWasWatered);
            water = cursor.getLong(indexWater);
            byRain = cursor.getInt(indexByRain);

            cursor.close();
            database.close();
            detail = new Details(id, plant_id, whenWasWatered, water, byRain);
        }


        return detail;
    }




    public List<Details> getAllDetailsByPlantId(long plantId) {
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.query(SqliteHelper.TABLE_DETAILS_NAME, null, SqliteHelper.COL_PLANT_ID + "=" +plantId, null, null, null, null);

        List<Details> details = Collections.EMPTY_LIST;

        if (cursor.getColumnCount() > 0) {
            details = new ArrayList<>();
        }

        int indexId = cursor.getColumnIndex(SqliteHelper.COL_ID_DETAIL);
        int indexPlant_id = cursor.getColumnIndex(SqliteHelper.COL_PLANT_ID);
        int indexWhenWasWatered = cursor.getColumnIndex(SqliteHelper.COL_WHEN_WAS_WATERED);
        int indexWater = cursor.getColumnIndex(SqliteHelper.COL_WATER);
        int indexByRain = cursor.getColumnIndex(SqliteHelper.COL_BY_RAIN);

        long id;
        long plant_id;
        long whenWasWatered;
        long water ;
        int byRain;

        while (cursor.moveToNext())
        {
            id = cursor.getLong(indexId);
            plant_id = cursor.getLong(indexPlant_id);
            whenWasWatered = cursor.getLong(indexWhenWasWatered);
            water = cursor.getLong(indexWater);
            byRain = cursor.getInt(indexByRain);

            Details tmp = new Details(id, plant_id, whenWasWatered, water, byRain);
            details.add(tmp);
        }

        cursor.close();
        database.close();
        return details;
    }

    public void updateWasRainingOfPlant(Plant plant)
    {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.COL_WAS_RAINING, plant.getWasRaining());

        database.update(SqliteHelper.TABLE_PLANTS_NAME, values, SqliteHelper.COL_ID_PLANT + "=" + plant.get_id(), null);
        database.close();
        Log.d("nick", "update plant rain: " + plant.toString());
    }

    public void updateWhenToWaterOfPlant(Plant plant)
    {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.COL_WHEN_TO_WATER, plant.getWhenToWater());

        database.update(SqliteHelper.TABLE_PLANTS_NAME, values, SqliteHelper.COL_ID_PLANT + "=" + plant.get_id(), null);
        database.close();
        Log.d("nick", "update plant rain: " + plant.toString());
    }

    public  int insertPlant(Plant plant) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SqliteHelper.COL_NAME, plant.getName());
        contentValues.put(SqliteHelper.COL_LINE_NUMBER, plant.getLineNumber());
        contentValues.put(SqliteHelper.COL_IRRIGATION_TIME, plant.getIrrigationTime());
        contentValues.put(SqliteHelper.COL_WHEN_TO_WATER, plant.getWhenToWater());
        contentValues.put(SqliteHelper.COL_WATER_FREQUENCY, plant.getLineNumber());
        contentValues.put(SqliteHelper.COL_WATER_TIME, plant.getWaterTime());
        contentValues.put(SqliteHelper.COL_WAS_RAINING, plant.getWasRaining());


        int sol = (int)database.insert(SqliteHelper.TABLE_PLANTS_NAME, null, contentValues);
        database.close();
        Log.d("nick", "insert plant: " + plant.toString());

        return  sol;
    }


    public int updatePlant(Plant plant) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.COL_NAME, plant.getName());
        values.put(SqliteHelper.COL_LINE_NUMBER, plant.getLineNumber());
        values.put(SqliteHelper.COL_IRRIGATION_TIME, plant.getIrrigationTime());
        values.put(SqliteHelper.COL_WHEN_TO_WATER, plant.getWhenToWater());
        values.put(SqliteHelper.COL_WATER_FREQUENCY, plant.getWaterFrequency());
        values.put(SqliteHelper.COL_WATER_TIME, plant.getWaterTime());
        values.put(SqliteHelper.COL_WAS_RAINING, plant.getWasRaining());

        int sol = database.update(SqliteHelper.TABLE_PLANTS_NAME, values, SqliteHelper.COL_ID_PLANT + "=" + plant.get_id(), null);
        database.close();
        Log.d("nick", "update plant: " + plant.toString());

        return sol;

    }

    public List<Plant> getAllPlants() {
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.query(SqliteHelper.TABLE_PLANTS_NAME, null, null, null, null, null, null);

        List<Plant> plants = Collections.EMPTY_LIST;

        if (cursor.getColumnCount() > 0) {
            plants = new ArrayList<>();
        }

        int indexId = cursor.getColumnIndex(SqliteHelper.COL_ID_PLANT);
        int indexName = cursor.getColumnIndex(SqliteHelper.COL_NAME);
        int indexLineNumber = cursor.getColumnIndex(SqliteHelper.COL_LINE_NUMBER);
        int indexIrrigationTime = cursor.getColumnIndex(SqliteHelper.COL_IRRIGATION_TIME);
        int indexWhenToWater = cursor.getColumnIndex(SqliteHelper.COL_WHEN_TO_WATER);
        int indexWaterFrequency = cursor.getColumnIndex(SqliteHelper.COL_WATER_FREQUENCY);
        int indexWaterTime = cursor.getColumnIndex((SqliteHelper.COL_WATER_TIME));
        int indexWasRaining = cursor.getColumnIndex(SqliteHelper.COL_WAS_RAINING);

        long id;
        String name;
        int lineNumber;
        long irrigationTime;
        long whenToWater;
        int waterFrequency;
        long waterTime;
        int wasRaining;

        while (cursor.moveToNext())
        {
            id = cursor.getInt(indexId);
            name = cursor.getString(indexName);
            lineNumber = cursor.getInt(indexLineNumber);
            irrigationTime = cursor.getLong(indexIrrigationTime);
            whenToWater = cursor.getLong(indexWhenToWater);
            waterFrequency = cursor.getInt(indexWaterFrequency);
            waterTime = cursor.getLong(indexWaterTime);
            wasRaining = cursor.getInt(indexWasRaining);

            Plant tmp = new Plant(id,name,lineNumber,irrigationTime,whenToWater,waterFrequency,waterTime,wasRaining);
            plants.add(tmp);
        }

        cursor.close();
        database.close();
        return plants;
    }

    private class SqliteHelper extends SQLiteOpenHelper {
        Context context;
        private static final String DATABASE_NAME = "MidgeDatabase21";
        private static final int DATABASE_VERSION = 1;


        //Bluetooth table
        private static final String TABLE_BLUETOOTH_NAME = "Bluetooth";
        private static final String COL_BLUETOOTH = "Bluetooth";

        private static final String CREATE_TABLE_BLUETOOTH = "CREATE TABLE IF NOT EXISTS " +TABLE_BLUETOOTH_NAME
                + " ("
                + COL_BLUETOOTH + " String "
                + "); ";


        //Details Table

        private static final String TABLE_DETAILS_NAME = "Details";
        private static final String COL_ID_DETAIL = "_id";
        private static final String COL_PLANT_ID = "PlantId";
        private static final String COL_WHEN_WAS_WATERED = "WhenWasWatered";
        private static final String COL_WATER = "Water";
        private static final String COL_BY_RAIN = "ByRain";


        private static final String CREATE_TABLE_DETAILS = "CREATE TABLE IF NOT EXISTS " + TABLE_DETAILS_NAME
                + " ("
                + COL_ID_DETAIL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PLANT_ID + " LONG, "
                + COL_WHEN_WAS_WATERED + " LONG, "
                + COL_WATER + " LONG, "
                + COL_BY_RAIN + " INTEGER "//0- TRUE, 1- FALSE
                + ");";
        //End Of Detail Table

        //Plant Table

        private static final String TABLE_PLANTS_NAME = "Plants";
        private static final String COL_ID_PLANT = "_id";
        private static final String COL_NAME = "Name";
        private static final String COL_LINE_NUMBER = "LineNumber";
        private static final String COL_IRRIGATION_TIME = "Irrigation_Time";
        private static final String COL_WHEN_TO_WATER = "When_To_Water";
        private static final String COL_WATER_FREQUENCY = "WaterFrequency";
        private static final String COL_WATER_TIME = "WaterTime";
        private static final String COL_WAS_RAINING = "Was_Raining";

        private static final String CREATE_TABLE_PLANTS = "CREATE TABLE IF NOT EXISTS " + TABLE_PLANTS_NAME
                + "("
                + COL_ID_PLANT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " VARCHAR, "
                + COL_LINE_NUMBER + " INTEGER, "
                + COL_IRRIGATION_TIME + " LONG , "
                + COL_WHEN_TO_WATER + " LONG, "
                + COL_WATER_FREQUENCY + " INTEGER, "
                + COL_WATER_TIME + " LONG, "
                + COL_WAS_RAINING + " INTEGER "
                + ");";

        //End Plant Table

        private static final String DROP_TABLE_PLANTS = "drop table if exists " + TABLE_PLANTS_NAME + ";";
        private static final String DROP_TABLE_DETAILS = "drop table if exists " + TABLE_DETAILS_NAME + ";";
        private static final String DROP_TABLE_BLUETOOTH = "drop table if exists " + TABLE_BLUETOOTH_NAME + ";";


        public SqliteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE_PLANTS);
            Log.d("nick","create " + CREATE_TABLE_PLANTS);
//            db.execSQL(DROP_TABLE_DETAILS);
            db.execSQL(CREATE_TABLE_DETAILS);
            Log.d("nick","create " + CREATE_TABLE_DETAILS);

//            db.execSQL(DROP_TABLE_BLUETOOTH);
//            Log.d("nick", "drop table bluetooth");
            db.execSQL(CREATE_TABLE_BLUETOOTH);
            Log.d("nick","create " + CREATE_TABLE_BLUETOOTH);
            ContentValues value = new ContentValues();
            value.put(SqliteHelper.COL_BLUETOOTH, "");
            db.insert(SqliteHelper.TABLE_BLUETOOTH_NAME, null, value);
            Log.d("nick", "create table bluetooth");

            Toast.makeText(context, "Tables Created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(DROP_TABLE_PLANTS);
            db.execSQL(DROP_TABLE_DETAILS);
            db.execSQL(DROP_TABLE_BLUETOOTH);
            onCreate(db);
            Log.d("nick", "Database Upgraded");
            Toast.makeText(context, "Database Upgraded", Toast.LENGTH_SHORT).show();
        }
    }

}
