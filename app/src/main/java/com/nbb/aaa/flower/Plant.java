package com.nbb.aaa.flower;

public class Plant extends GenericInfo {
    //plant params
    String name;
    int lineNumber;
    long irrigationTime;//time(hour)
    long whenToWater;//mili since 1970 תאריך ההשקיה הבאה
    int waterFrequency;//כל מה זמן להשקות
    long waterTime;//כמה זמן להשקות
    int wasRaining;//נקודה ירוקה-הושקה, אפורה- תחת השקייה, אדומה- תקלה, כחולה-גשם


    public Plant(long id, String name, int lineNumber, long irrigationTime, long whenToWater, int waterFrequency, long waterTime, int wasRaining) {//constructor

        super(id);
        this.name = name;
        this.lineNumber = lineNumber;
        this.irrigationTime = irrigationTime;
        this.whenToWater = whenToWater;
        this.waterFrequency = waterFrequency;
        this.waterTime = waterTime;
        this.wasRaining = wasRaining;
    }

    public Plant(String name, int lineNumber, long irrigationTime, long whenToWater, int waterFrequency, long waterTime, int wasRaining) {//constructor

        this.name = name;
        this.lineNumber = lineNumber;
        this.irrigationTime = irrigationTime;
        this.whenToWater = whenToWater;
        this.waterFrequency = waterFrequency;
        this.waterTime = waterTime;
        this.wasRaining = wasRaining;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public long getIrrigationTime() {
        return irrigationTime;
    }

    public void setIrrigationTime(long irrigationTime) {
        this.irrigationTime = irrigationTime;
    }

    public long getWhenToWater() {
        return whenToWater;
    }

    public void setWhenToWater(long whenToWater) {
        this.whenToWater = whenToWater;
    }

    public int getWaterFrequency() {
        return waterFrequency;
    }

    public void setWaterFrequency(int waterFrequency) {
        this.waterFrequency = waterFrequency;
    }

    public long getWaterTime() {
        return waterTime;
    }

    public void setWaterTime(long waterTime) {
        this.waterTime = waterTime;
    }

    public int getWasRaining() {
        return wasRaining;
    }

    public void setWasRaining(int wasRaining) {
        this.wasRaining = wasRaining;
    }

    @Override
    public String toString() {//to string
        return "Plant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lineNumber=" + lineNumber +
                ", irrigationTime=" + irrigationTime +
                ", whenToWater=" + whenToWater +
                ", waterFrequency=" + waterFrequency +
                ", waterTime=" + waterTime +
                ", wasRaining=" + wasRaining +
                '}';
    }
}
